import React, {Component} from 'react';
import {Loader, Pagination} from "semantic-ui-react";
import {getPostsByUserAndStatus} from "../util/PostService";
import PostsView from "../post/PostsView";

class UserPosts extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: props.id,
            page: 1,
            profile: props.profile,
            status: props.status,
            posts: {
                loading: true,
                posts: [],
                page: null
            }
        }
        this.loadPosts = this.loadPosts.bind(this);
        this.pageChange = this.pageChange.bind(this);
    }

    componentDidMount() {
        this.loadPosts(this.state.id,this.state.page,this.state.status)
    }

    loadPosts(userId, page, status) {
        getPostsByUserAndStatus(userId,status, page, 10, 'createdDate').then(response => {
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

    render() {
        const {posts, profile} = this.state
        if (posts.loading) return <Loader active inline='centered'/>
        return (
            <div>
                <PostsView showActions={profile} posts={posts}/>
                {
                    posts.posts.length !== 0 && posts.page.totalPages!==1 &&
                    <Pagination
                        activePage={posts.page.number + 1}
                        firstItem={null}
                        lastItem={null}
                        onPageChange={this.pageChange}
                        totalPages={posts.page.totalPages}
                    />
                }
            </div>
        )
    }

    pageChange(e, {activePage}) {
        this.setState({
            page: activePage
        })
        this.loadPosts(this.state.id, activePage,this.state.status)
    }
}

export default UserPosts;