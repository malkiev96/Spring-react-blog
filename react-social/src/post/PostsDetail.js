import React, {Component} from 'react';
import {Button, Comment, Form, Header, Icon, Item, List, Loader, Segment} from "semantic-ui-react";
import {getComments, getPostById} from "../util/APIUtils";
import moment from "moment";
import {locale} from "moment/locale/ru";
import NotFound from "../common/NotFound";
import './posts.css'
import {Link} from "react-router-dom";
import Comments from "./Comments";

class PostsDetail extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: parseInt(this.props.match.params.id),
            post: {
                post: null,
                error: false,
                loading: true
            },
            comments: {
                comments: [],
                error: false,
                loading: true
            }
        }
        this.LoadData = this.LoadData.bind(this);
        this.loadComments = this.loadComments.bind(this);
    }

    componentDidUpdate(prevProps) {
        if (this.props.location.pathname !== prevProps.location.pathname) {
            this.LoadData()
        }
    }

    componentDidMount() {
        this.LoadData()
    }

    LoadData() {
        getPostById(this.state.id).then(response => {
            this.setState({
                post: {
                    post: response,
                    error: false,
                    loading: false
                }
            })
        }).catch(error => {
            this.setState({
                post: {
                    post: null,
                    error: true,
                    loading: false
                }
            })
        })
        this.loadComments(this.state.id)
    }

    loadComments(postId) {
        getComments(postId).then(response => {
            this.setState({
                comments: {
                    comments: response['content'],
                    error: false,
                    loading: false
                }
            })
        }).catch(error => {
            this.setState({
                comments: {
                    comments: [],
                    error: true,
                    loading: false
                }
            })
        })
    }

    render() {
        const {loading, error, post} = this.state.post
        const{comments} = this.state
        if (loading) return <Loader active inline='centered'/>
        if (error) return <NotFound/>

        const createdDate = moment(post.auditor.createdDate, "DD-MM-YYYY hh:mm", locale).fromNow()

        return (
            <div>
                <Segment>
                    <Header as='h1' dividing>{post.title}</Header>
                    {
                        post.preview !== null &&
                        <Item.Image src={post.preview.url} size='massive' bordered/>
                    }
                    <p id={'post-text'}>{post.text}</p>
                </Segment>
                <Segment>
                    <Item.Group>
                        <Item>
                            <Item.Content>
                                <Item.Meta>
                                    <Link to={'/category/' + post.category.name}>
                                        <Icon name='folder'/>
                                        {' ' + post.category.name}
                                    </Link>
                                </Item.Meta>
                                <Item.Meta>
                                    <Link to={'/user/' + post.auditor.createdBy.id}>
                                        <Icon name='user'/>
                                        {' ' + post.auditor.createdBy.name}
                                    </Link>
                                    {' ' + createdDate}
                                </Item.Meta>
                            </Item.Content>
                        </Item>
                    </Item.Group>

                    <Header as='h3' dividing>Теги</Header>
                    <List selection horizontal>
                        {post.tags.map(tag => {
                            return (
                                <List.Item key={tag.id}>
                                    <Link to={'/tags/' + tag.name}>
                                        <Button compact active size={'tiny'} basic>{tag.name}</Button>
                                    </Link>
                                </List.Item>
                            )
                        })}
                    </List>
                </Segment>
                <Segment loading={comments.loading}>
                    <Header as='h3' dividing>
                        Комментарии
                    </Header>
                    <Comments comments={comments}/>
                    <Form reply>
                        <Form.TextArea/>
                        <Button content='Добавить комментарий' labelPosition='left' icon='edit' primary/>
                    </Form>
                </Segment>
            </div>
        )
    }
}

export default PostsDetail;