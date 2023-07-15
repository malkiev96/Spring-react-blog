import React, {Component} from 'react';
import {Pagination} from "semantic-ui-react";
import {getPosts} from "../../api/Posts";
import PostsView from "../post/PostsView";
import DataLoader from "../common/DataLoader";

class UserPosts extends Component {

  constructor(props) {
    super(props);
    this.state = {
      id: props.id,
      page: 1,
      status: props.status,
      liked: props.liked || false,
      posts: {
        loading: true,
        posts: [],
        last: true,
        totalPages: 0,
        totalElements: 0,
        size: 10,
        number: 0,
      }
    }
    this.loadPosts = this.loadPosts.bind(this);
    this.pageChange = this.pageChange.bind(this);
  }

  componentDidMount() {
    this.loadPosts(this.state.id, this.state.page, this.state.status,
        this.state.liked)
  }

  loadPosts(userId, page, status, liked) {
    getPosts(page, 10, 'createdDate', null, null, status, userId, liked)
    .then(response => {
      this.setState({
        posts: {
          posts: response.content,
          last: response.last,
          totalPages: response.totalPages,
          totalElements: response.totalElements,
          size: response.size,
          number: response.number,
          loading: false
        }
      })
    }).catch(() => {
      this.setState({
        posts: {
          posts: [],
          loading: false
        }
      })
    })
  }

  render() {
    const {posts} = this.state
    if (posts.loading) {
      return <DataLoader/>
    }
    return (
        <div>
          <PostsView posts={posts}/>
          {
              posts.totalPages > 1 &&
              <Pagination
                  activePage={posts.number + 1}
                  firstItem={null}
                  lastItem={null}
                  onPageChange={this.pageChange}
                  totalPages={posts.totalPages}
              />
          }
        </div>
    )
  }

  pageChange(e, {activePage}) {
    this.setState({
      page: activePage
    })
    this.loadPosts(this.state.id, activePage, this.state.status)
  }
}

export default UserPosts;