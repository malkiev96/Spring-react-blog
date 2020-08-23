import React, {Component} from 'react';
import PostsView from "./PostsView";
import {getPosts} from "../service/PostService";
import {Button, Loader, Pagination, Segment} from "semantic-ui-react";

class Posts extends Component {

    constructor(props) {
        super(props);
        const page = this.props.match.params.page || 1
        const tagCode = this.props.match.params.tagCode
        const categoryCode = this.props.match.params.categoryCode
        this.state = {
            page: page,
            tagCode: tagCode,
            categoryCode: categoryCode,
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
        this.onPopularClick = this.onPopularClick.bind(this);
        this.onNewClick = this.onNewClick.bind(this);
        this.onViewClick = this.onViewClick.bind(this);
        this.getIds = this.getIds.bind(this);
    }

    componentDidUpdate(prevProps) {
        if (this.props.location.pathname !== prevProps.location.pathname) {
            const page = this.props.match.params.page || 1
            const tagCode = this.props.match.params.tagCode
            const categoryCode = this.props.match.params.categoryCode
            this.setState({page: page, tagCode: tagCode, categoryCode: categoryCode})
            this.loadPosts(page, this.state.size, this.state.sort, tagCode, categoryCode)
        }
    }

    componentDidMount() {
        const {page, size, sort, tagCode, categoryCode} = this.state;
        this.loadPosts(page, size, sort, tagCode, categoryCode)
    }

    onPopularClick() {
        this.setState({sort: 'postRatings.star,desc', page: 1})
        const {size, tagCode, categoryCode} = this.state;
        this.loadPosts(1, size, 'postRatings.star,desc', tagCode, categoryCode)
    }

    onNewClick() {
        this.setState({sort: 'createdDate,desc', page: 1})
        const {size, tagCode, categoryCode} = this.state;
        this.loadPosts(1, size, 'createdDate,desc', tagCode, categoryCode)
    }

    onViewClick() {
        this.setState({sort: 'viewCount,desc', page: 1})
        const {size, tagCode, categoryCode} = this.state;
        this.loadPosts(1, size, 'viewCount,desc', tagCode, categoryCode)
    }

    loadPosts(page, size, sort, tagCode, categoryCode) {
        const catIds = this.getIds(categoryCode)
        getPosts(page, size, sort, tagCode, catIds).then(response => {
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
    }

    getIds(categoryCode) {
        if (categoryCode) {
            let exist = false
            let ids = []
            this.props.categories.forEach(cat => {
                let childs = cat.childs;
                if (cat.description === categoryCode) {
                    ids.push(cat.id)
                    childs.forEach(child => ids.push(child.id))
                    exist = true
                }
                childs.forEach(child => {
                    if (child.description === categoryCode) {
                        ids.push(child.id)
                        exist = true
                    }
                })
            })
            if (exist) {
                return ids.toString().split(",")
            }
        }
        return false
    }

    render() {
        const {posts, sort} = this.state
        if (posts.loading) return <Loader active inline='centered'/>
        return (
            <div>
                <Segment textAlign={'center'}>
                    <Button primary={sort === 'createdDate,desc'}
                            basic={sort !== 'createdDate,desc'}
                            size={"tiny"} onClick={this.onNewClick}
                            style={{backgroundColor: '#175e6b'}}>Сначала новые</Button>
                    <Button primary={sort === 'postRatings.star,desc'}
                            basic={sort !== 'postRatings.star,desc'}
                            size={"tiny"} onClick={this.onPopularClick}
                            style={{backgroundColor: '#175e6b'}}>Популярное</Button>
                    <Button primary={sort === 'viewCount,desc'}
                            basic={sort !== 'viewCount,desc'}
                            size={"tiny"} onClick={this.onViewClick}
                            style={{backgroundColor: '#175e6b'}}>Просматриваемое</Button>
                </Segment>
                <PostsView posts={posts}/>

                <Pagination style={{marginTop: '15px'}} activePage={posts.page.number + 1}
                            firstItem={null} lastItem={null} onPageChange={this.pageChange}
                            totalPages={posts.page.totalPages}/>
            </div>
        )
    }

    pageChange(e, {activePage}) {
        this.setState({page: activePage})
        const {tagCode, categoryCode} = this.state
        if (tagCode) this.props.history.push("/tags/" + tagCode + "/" + activePage)
        else if (categoryCode) this.props.history.push("/category/" + categoryCode + "/" + activePage)
        else this.props.history.push("/posts/" + activePage)
    }
}

export default Posts;