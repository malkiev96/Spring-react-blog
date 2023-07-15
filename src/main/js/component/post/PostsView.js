import React, {Component} from 'react';
import {deletePost, getPostsByUrl, hidePost, publishPost} from "../../api/Posts"
import {Segment} from "semantic-ui-react";
import Alert from 'react-s-alert';
import './posts.css'
import PostsItem from "./PostsItem";
import DataLoader from "../common/DataLoader";

class PostsView extends Component {

  constructor(props) {
    super(props);
    this.state = {
      posts: props.posts,
      showActions: props.showActions || false
    }
    this.updatePosts = this.updatePosts.bind(this)
    this.onDeletePost = this.onDeletePost.bind(this)
    this.onHidePost = this.onHidePost.bind(this)
    this.onPublishPost = this.onPublishPost.bind(this)
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    if (prevProps.posts !== this.props.posts) {
      this.setState({
        posts: this.props.posts
      })
    }
  }

  render() {
    const {posts, showActions} = this.state
    if (posts.loading) {
      return <DataLoader/>
    }
    if (posts.posts.length === 0) {
      return <Segment>Статей нет</Segment>
    }
    return (
        <div>
          {
            posts.posts.map(post =>
                <PostsItem key={post.id}
                           showActions={showActions}
                           onHidePost={this.onHidePost}
                           onPublishPost={this.onPublishPost}
                           onDeletePost={this.onDeletePost}
                           post={post}/>
            )
          }
        </div>
    )
  }

  updatePosts(self) {
    if (self) {
      getPostsByUrl(self.href).then(response => {
        this.setState({
          posts: {
            posts: response['content'],
            page: response['page'],
            self: response['links'][0],
            loading: false
          }
        })
      }).catch(error => {
        this.setState({
          posts: {
            posts: [],
            loading: false
          }
        })
      })
    }
  }

  onHidePost(id) {
    hidePost(id).then(() => {
      Alert.success("Публикация скрыта")
      this.updatePosts(this.props.posts.self)
    }).catch((error) => {
      Alert.error((error && error.message)
          || 'Не удалось скрыть публикацию, попробуйте еще раз');
    })
  }

  onPublishPost(id) {
    publishPost(id).then(() => {
      Alert.success("Статья опубликована")
      this.updatePosts(this.props.posts.self)
    }).catch((error) => {
      Alert.error((error && error.message)
          || 'Не удалось опубликовать статью, попробуйте еще раз');
    })
  }

  onDeletePost(id) {
    deletePost(id).then(() => {
      Alert.success("Публикация удалена")
      this.updatePosts(this.props.posts.self)
    }).catch((error) => {
      Alert.error((error && error.message)
          || 'Не удалось удалить публикацию, попробуйте еще раз');
    })
  }
}

export default PostsView;