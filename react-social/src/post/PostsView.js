import React, {Component} from 'react';
import moment from "moment";
import {locale} from "moment/locale/ru";
import {deletePost, getPostsByUrl, hidePost, publishPost} from "../util/PostService"
import {Button, ButtonGroup, Divider, Header, Image, Segment} from "semantic-ui-react";
import {Link} from "react-router-dom";
import Alert from 'react-s-alert';
import './posts.css'

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
        if (posts.posts.length === 0) return <Segment>Статей нет</Segment>
        return (
            <div>
                {
                    posts.posts.map(post =>
                        <PostsRow key={post.id}
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
        hidePost(id)
            .then(() => {
                Alert.success("Публикация скрыта")
                this.updatePosts(this.props.posts.self)
            })
            .catch((error) => {
                Alert.error((error && error.message) || 'Не удалось скрыть публикацию, попробуйте еще раз');
            })
    }

    onPublishPost(id) {
        publishPost(id)
            .then(() => {
                Alert.success("Статья опубликована")
                this.updatePosts(this.props.posts.self)
            })
            .catch((error) => {
                Alert.error((error && error.message) || 'Не удалось опубликовать статью, попробуйте еще раз');
            })
    }

    onDeletePost(id) {
        deletePost(id)
            .then(() => {
                Alert.success("Публикация удалена")
                this.updatePosts(this.props.posts.self)
            })
            .catch((error) => {
                Alert.error((error && error.message) || 'Не удалось удалить публикацию, попробуйте еще раз');
            })
    }
}

const PostsRow = ({post, onHidePost, onPublishPost, onDeletePost, showActions}) => {
    let canHide, canPublish, canDelete;
    const createdDate = moment(post.auditor.createdDate, "DD-MM-YYYY hh:mm", locale).fromNow()
    if (showActions) {
        canHide = post.links.find(link => link.rel === 'hide');
        canPublish = post.links.find(link => link.rel === 'publish');
        canDelete = post.links.find(link => link.rel === 'delete');
    }
    return (
        <Segment key={post.id}>
            <Header size="larger" as="h2">
                <Header.Content style={{paddingBottom: '10px'}}>
                    <Link to={'/post/' + post.id}>{post.title}</Link>
                </Header.Content>
                <Header.Subheader>
                    <Link to={'/user/' + post.auditor.createdBy.id}>
                        {' ' + post.auditor.createdBy.name}
                    </Link>
                    {' ' + createdDate}
                </Header.Subheader>
                <Header.Subheader>
                    <span>{post.viewCount === 0 ? 'Нет' : post.viewCount} просмотров</span>
                </Header.Subheader>
            </Header>
            {
                post.preview !== null &&
                <Image size={"large"} src={post.preview.url}/>
            }
            <Divider hidden/>
            <p>{post.description}</p>
            <Link to={'/post/' + post.id}>Подробнее</Link>
            <div>
                {
                    showActions &&
                    <ButtonGroup style={{marginTop: '10px'}} basic size={'mini'} compact>
                        {
                            canDelete &&

                            <Button onClick={() => onDeletePost(post.id)}
                                    size={'mini'} compact>Удалить</Button>

                        }
                        {
                            canHide &&

                            <Button onClick={() => onHidePost(post.id)}
                                    size={'mini'} compact>Cкрыть</Button>

                        }
                        {
                            canPublish &&

                            <Button onClick={() => onPublishPost(post.id)}
                                    size={'mini'} compact>Опубликовать</Button>

                        }
                    </ButtonGroup>
                }
            </div>
        </Segment>
    )
}

export default PostsView;