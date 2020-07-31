import React, {Component} from 'react';
import PostsView from "./PostsView";
import {getPosts} from '../util/APIUtils';
import {Loader, Pagination, Segment} from "semantic-ui-react";

class Posts extends Component {

    constructor(props) {
        super(props);
        const page = this.props.match.params.page || 1
        this.state = {
            page: page,
            size: 10,
            sort: "createdDate,desc",
            posts: {
                posts: [],
                page: null,
                loading: true
            }
        }
        this.loadPosts = this.loadPosts.bind(this);
        this.pageChange = this.pageChange.bind(this);
    }

    componentDidUpdate(prevProps) {
        if (this.props.location.pathname !== prevProps.location.pathname) {
            this.loadPosts()
        }
    }

    componentDidMount() {
        this.loadPosts()
    }

    loadPosts() {
        getPosts(this.state.page, this.state.size, this.state.sort).then(response => {
            this.setState({
                posts: {
                    posts: response['content'],
                    page: response['page'],
                    loading: false
                }
            })
        })
            .catch(error => {
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
                    posts.posts.length !== 0 && posts.page.totalPages!==1 &&
                    <Segment>
                        <Pagination
                            activePage={posts.page.number + 1}
                            firstItem={null}
                            lastItem={null}
                            onPageChange={this.pageChange}
                            totalPages={posts.page.totalPages}
                        />
                    </Segment>
                }

            </div>
        )
    }

    pageChange(e, {activePage}) {
        this.setState({
            page: activePage
        })
        this.props.history.push("/posts/p/" + activePage)
    }
}

export default Posts;