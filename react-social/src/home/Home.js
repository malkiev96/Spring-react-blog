import React, {Component} from 'react';
import {getPosts} from "../util/PostService";
import {Button, Loader} from "semantic-ui-react";
import PostsView from "../post/PostsView";
import {Link} from "react-router-dom";

class Home extends Component {

    constructor(props) {
        super(props);
        this.state = {
            posts: {
                loading: true,
                posts: [],
                page: null
            }
        }
    }

    componentDidMount() {
        this.loadPosts()
    }

    loadPosts() {
        getPosts(1, 10, 'createdDate,desc', null, null).then(response => {
            this.setState({
                posts: {
                    posts: response['content'],
                    page: response['page'],
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
        if (posts.loading) return <Loader active inline='centered'/>

        return (
            <div>
                <PostsView posts={posts}/>
                {
                    posts.page.totalPages > 1 &&
                    <div style={{textAlign: 'center', paddingTop: '15px'}}>
                        <Link to={'/posts/2'}>
                            <Button style={{backgroundColor: '#175e6b'}} primary size='medium'>Показать еще</Button>
                        </Link>
                    </div>
                }
            </div>
        )
    }
}

export default Home;