import React, {Component} from 'react';
import {getPosts} from "../../api/Posts";
import {Button} from "semantic-ui-react";
import PostsView from "../post/PostsView";
import {Link} from "react-router-dom";
import DataLoader from "../common/DataLoader";

class Home extends Component {

  constructor(props) {
    super(props);
    this.state = {
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
  }

  componentDidMount() {
    document.title = 'Главная'
    this.loadPosts()
  }

  loadPosts() {
    getPosts(1, 10, 'createdDate,desc', null, null).then(response => {
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
              posts && posts.totalPages > 1 &&
              <div style={{textAlign: 'center', paddingTop: '15px'}}>
                <Link to={'/posts/2'}>
                  <Button style={{backgroundColor: '#175e6b'}} primary
                          size='medium'>Показать еще</Button>
                </Link>
              </div>
          }
        </div>
    )
  }
}

export default Home;