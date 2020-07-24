import React, {Component} from 'react';
import PostsView from "./PostsView";
import {getPostsByTagIds} from '../util/APIUtils';
import {Loader, Pagination, Segment} from "semantic-ui-react";
import NotFound from "../common/NotFound";

class PostsTag extends Component {

    constructor(props) {
        super(props);
        this.state = {
            page: parseInt(this.props.match.params.page) || 1,
            size: 10,
            sort: "createdDate,desc",
            tagName: this.props.match.params.tagName,
            posts: {
                posts: [],
                page: null,
                loading: true
            }
        }
        this.loadPosts = this.loadPosts.bind(this);
        this.getIdByName = this.getIdByName.bind(this);
        this.pageChange = this.pageChange.bind(this);
    }

    componentDidUpdate(prevProps) {
        if (this.props.location.pathname !== prevProps.location.pathname) {
            const page = parseInt(this.props.match.params.page) || 1
            const tagName = this.props.match.params.tagName
            this.setState({
                page: page,
                error: false,
                tagName: tagName
            })
            this.loadPosts(tagName,page)
        }
    }

    componentDidMount() {
        this.loadPosts(this.state.tagName,this.state.page)
    }

    getIdByName(tagName) {
        let id = false
        this.props.tags.forEach(tag => {
            if (tag.name === tagName) {
                id = tag.id
            }
        })
        return id
    }

    loadPosts(tagName,page) {
        const tagId = this.getIdByName(tagName)
        if (tagId){
            getPostsByTagIds(tagId, page, this.state.size, this.state.sort)
                .then(response => {
                    this.setState({
                        posts: {
                            posts: response['content'],
                            page: response['page'],
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
        }else {
            this.setState({
                error: true
            })
        }

    }

    render() {
        const {posts,error} = this.state
        if (error) return <NotFound/>
        if (posts.loading) return <Loader active inline='centered'/>
        return (
            <div>
                <PostsView posts={posts}/>
                {
                    posts.posts.length !== 0 &&
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
        this.props.history.push("/tags/" + this.state.tagName + "/p/" + activePage)
    }
}

export default PostsTag;