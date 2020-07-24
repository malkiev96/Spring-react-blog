import React, {Component} from 'react';
import PostsView from "./PostsView";
import {getPostsByCategory} from '../util/APIUtils';
import {Loader, Pagination, Segment} from "semantic-ui-react";
import NotFound from "../common/NotFound";

class PostsCategory extends Component {

    constructor(props) {
        super(props);

        this.state = {
            page: parseInt(this.props.match.params.page) || 1,
            size: 10,
            sort: "createdDate,desc",
            categoryName: this.props.match.params.categoryName,
            posts: {
                posts: [],
                page: null,
                loading: true
            }
        }
        this.loadPosts = this.loadPosts.bind(this);
        this.getIds = this.getIds.bind(this);
        this.pageChange = this.pageChange.bind(this);
    }

    componentDidUpdate(prevProps) {
        if (this.props.location.pathname !== prevProps.location.pathname) {
            const page = parseInt(this.props.match.params.page) || 1
            const categoryName = this.props.match.params.categoryName
            const exist = this.getIds(categoryName)
            this.setState({
                page: page,
                error: !exist,
                categoryName: categoryName
            })
            this.loadPosts(categoryName,page)
        }
    }

    componentDidMount() {
        this.loadPosts(this.state.categoryName,this.state.page)
    }

    loadPosts(categoryName,page) {
        const ids = this.getIds(categoryName);
        if (ids) {
            getPostsByCategory(ids, page, this.state.size, this.state.sort)
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
        } else {
            this.setState({error: true})
        }
    }

    getIds(categoryName) {
        let exist = false
        let ids = []
        this.props.categories.forEach(cat => {
            let childs = cat.childs;
            if (cat.name === categoryName) {
                ids.push(cat.id)
                childs.forEach(child => {
                    ids.push(child.id)
                })
                exist = true
            }
            childs.forEach(child => {
                if (child.name === categoryName) {
                    ids.push(child.id)
                    exist = true
                }
            })
        })
        if (exist){
            return ids.toString().split(",")
        }
        return false
    }

    render() {
        const {posts, error} = this.state
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
        this.props.history.push("/category/" + this.state.categoryName + "/p/" + activePage)
    }
}

export default PostsCategory;