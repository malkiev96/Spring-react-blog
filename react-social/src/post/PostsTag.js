import React, {Component} from 'react';
import PostsView from "./PostsView";
import {getPostsByTagIds} from '../util/PostService';
import {Button, List, Loader, Pagination, Segment} from "semantic-ui-react";
import NotFound from "../common/NotFound";

class PostsTag extends Component {

    constructor(props) {
        super(props);
        let name = this.props.match.params.tagName;
        this.state = {
            page: parseInt(this.props.match.params.page, 10) || 1,
            size: 10,
            sort: "createdDate,desc",
            tagName: name,
            tagId: this.getIdByName(name),
            posts: {
                posts: [],
                page: null,
                loading: true
            }
        }
        this.loadPosts = this.loadPosts.bind(this);
        this.getIdByName = this.getIdByName.bind(this);
        this.pageChange = this.pageChange.bind(this);
        this.onTagClick = this.onTagClick.bind(this);
    }

    componentDidUpdate(prevProps) {
        if (this.props.location.pathname !== prevProps.location.pathname) {
            const page = parseInt(this.props.match.params.page, 10) || 1
            const tagName = this.props.match.params.tagName
            this.setState({
                page: page,
                error: false,
                tagName: tagName,
                tagId: this.getIdByName(tagName)
            })
            this.loadPosts(tagName, page)
        }
    }

    componentDidMount() {
        this.loadPosts(this.state.tagName, this.state.page)
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

    loadPosts(tagName, page) {
        const tagId = this.getIdByName(tagName)
        if (tagId) {
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
        } else {
            this.setState({
                error: true
            })
        }

    }

    onTagClick = (e, {id, tagName}) => {
        this.setState({
            id: id,
            tagName: tagName
        })
        this.props.history.push("/tags/" + tagName)
    }

    render() {
        const {posts, error, tagId} = this.state
        const {tags} = this.props
        if (error) return <NotFound/>
        if (posts.loading) return <Loader active inline='centered'/>
        return (
            <div>
                <Segment raised textAlign={'center'}>
                    <List size='mini' selection horizontal>
                        {
                            tags.map((tag, i) => (
                                <List.Item key={i}>
                                    <Button compact active size='small'
                                            id={tag.id}
                                            tagName={tag.name}
                                            onClick={this.onTagClick}
                                            content={tag.name}
                                            style={{backgroundColor:'#175e6b'}}
                                            primary={tagId === tag.id}
                                            basic={tagId !== tag.id}/>
                                </List.Item>
                            ))
                        }
                    </List>
                </Segment>
                <PostsView posts={posts}/>
                <Pagination style={{marginTop: '15px'}}
                            activePage={posts.page.number + 1}
                            firstItem={null}
                            lastItem={null}
                            onPageChange={this.pageChange}
                            totalPages={posts.page.totalPages}/>
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