import React, {Component} from 'react';
import {Button, Comment, Form, Header, Icon, Item, List, Loader, Segment} from "semantic-ui-react";
import {addComment, deleteComment, getComments, getPostById} from "../util/APIUtils";
import moment from "moment";
import {locale} from "moment/locale/ru";
import NotFound from "../common/NotFound";
import './posts.css'
import {Link} from "react-router-dom";
import Alert from "react-s-alert";

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
            },
            message: ''
        }
        this.LoadData = this.LoadData.bind(this);
        this.loadComments = this.loadComments.bind(this);
        this.deleteHandler = this.deleteHandler.bind(this);
        this.createHandler = this.createHandler.bind(this);
        this.replyClick = this.replyClick.bind(this);
        this.replyHandler = this.replyHandler.bind(this);
        this.createComment = this.createComment.bind(this);
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

    createComment(postId, parentId = 0, message) {
        addComment(postId, parentId, message).then(() => {
            Alert.success("Комментарий успешно добавлен");
            this.setState({message: '', messageReply: ''})
            this.loadComments(postId)
        }).catch(error => {
            Alert.error((error && error.message) || 'Что-то пошло не так, попробуйте еще раз');
        })
    }

    createHandler() {
        this.createComment(this.state.id, 0, this.state.message)
    }

    replyClick(id) {
        let comments = this.replyChilds(id, this.state.comments.comments)
        this.setState({
            comments: {
                comments: comments,
                error: false,
                loading: false
            },
        })
    }

    replyChilds(id, comments) {
        comments.map(c => {
            if (c.id === id) {
                c.showReply = !c.showReply
            } else {
                c.showReply = false
            }
            if (c.childs['content'].length !== 0) {
                c.childs['content'] = this.replyChilds(id, c.childs['content'])
            }
            return c
        })

        return comments
    }

    replyHandler(id) {
        this.createComment(this.state.id, id, this.state.messageReply)
    }

    deleteHandler(id) {
        deleteComment(id).then(response => {
            Alert.success("Комментарий успешно удален");
            this.loadComments(this.state.id)
        }).catch(error => {
            Alert.error((error && error.message) || 'Что-то пошло не так, попробуйте еще раз');
        })
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
        const {comments} = this.state
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
                    <Comment.Group>
                        {
                            comments.comments.map(comment => {
                                return <this.CommentItem comment={comment} key={comment.id}/>
                            })
                        }
                    </Comment.Group>
                    <Form reply onSubmit={this.createHandler}>
                        <Form.TextArea value={this.state.message}
                                       onChange={event => this.setState({message: event.target.value})}/>
                        <Button content='Добавить комментарий' labelPosition='left' icon='edit' primary/>
                    </Form>
                </Segment>
            </div>
        )
    }

    CommentItem = (props) => {
        const {comment} = props;
        const canReply = comment.links.find(link => link.rel === 'reply')
        const canDelete = comment.links.find(link => link.rel === 'delete')
        const childs = comment.childs['content']
        const createdDate = moment(comment.auditor.createdDate, "DD-MM-YYYY hh:mm", locale).fromNow()
        const createdBy = comment.auditor.createdBy;
        return (
            <Comment>
                {
                    createdBy.imageUrl && !comment.deleted &&
                    <Comment.Avatar src={createdBy.imageUrl}/>
                }
                {
                    comment.deleted &&
                    <Comment.Avatar src={'https://zaborkin.ru/img/calend/ava-no.png'}/>
                }


                <Comment.Content>
                    <Comment.Author as='a'
                                    href={'/user/' + createdBy.id}>{comment.deleted ? '' : createdBy.name}</Comment.Author>
                    <Comment.Metadata>
                        <div>{createdDate}</div>
                    </Comment.Metadata>
                    <Comment.Text>{comment.message}</Comment.Text>
                    {
                        !comment.deleted &&
                        <Comment.Actions>
                            {
                                canReply &&
                                <Comment.Action as={'a'} value={comment.id}
                                                onClick={() => this.replyClick(comment.id)}>Ответить</Comment.Action>
                            }
                            {
                                canDelete &&
                                <Comment.Action as={'a'} value={comment.id}
                                                onClick={() => this.deleteHandler(comment.id)}>Удалить</Comment.Action>
                            }
                        </Comment.Actions>
                    }
                    {
                        canReply && !comment.deleted && comment.showReply &&
                        <Form onSubmit={() => this.replyHandler(comment.id)}>
                            <Form.TextArea value={this.state.messageReply}
                                           onChange={event => this.setState({messageReply: event.target.value})}/>
                            <Button content='Добавить комментарий' labelPosition='left' icon='edit' primary/>
                        </Form>
                    }
                </Comment.Content>
                {
                    childs.length !== 0 &&
                    <Comment.Group>
                        {
                            childs.map(c => <this.CommentItem comment={c}/>)
                        }
                    </Comment.Group>
                }
            </Comment>
        )
    }

}

export default PostsDetail;