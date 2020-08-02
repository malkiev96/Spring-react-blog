import React, {Component} from 'react';
import moment from "moment";
import {locale} from "moment/locale/ru";
import {hidePost, publishPost, deletePost, getPostsByUrl, getPostsByUserAndStatus} from "../util/PostService"
import {Button, ButtonGroup, Icon, Item, Label, Message, Segment} from "semantic-ui-react";
import {Link} from "react-router-dom";
import Alert from 'react-s-alert';

class PostsView extends Component {

    constructor(props) {
        super(props);
        this.state = {
            posts: props.posts,
            showActions: props.showActions || false
        }
        this.updatePosts = this.updatePosts.bind(this)
        this.onDeletePost = this.onDeletePost.bind(this)
        this.onHidePost = this.onHidePost.bind(this)
        this.onPublishPost = this.onPublishPost.bind(this)
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps.posts !== this.props.posts){
            this.setState({
                posts: this.props.posts
            })
        }
    }

    render() {
        const {posts, showActions} = this.state
        if (posts.posts.length === 0) return<Segment>Статей нет</Segment>
        return (
            <div>{posts.posts.map(post => <PostsRow
                showActions={showActions}
                onHidePost={this.onHidePost}
                onPublishPost={this.onPublishPost}
                onDeletePost={this.onDeletePost}
                post={post}/>)}</div>
        )
    }

    updatePosts(self) {
        if (self) {
            getPostsByUrl(self.href).then(response => {
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
    }

    onHidePost(id) {
        hidePost(id)
            .then(() => {
                Alert.success("Публикация скрыта")
                this.updatePosts(this.props.posts.self)
            })
            .catch((error) => {
                Alert.error((error && error.message) || 'Не удалось скрыть публикацию, попробуйте еще раз');
            })
    }

    onPublishPost(id) {
        publishPost(id)
            .then(() => {
                Alert.success("Статья опубликована")
                this.updatePosts(this.props.posts.self)
            })
            .catch((error) => {
                Alert.error((error && error.message) || 'Не удалось опубликовать статью, попробуйте еще раз');
            })
    }

    onDeletePost(id) {
        deletePost(id)
            .then(() => {
                Alert.success("Публикация удалена")
                this.updatePosts(this.props.posts.self)
            })
            .catch((error) => {
                Alert.error((error && error.message) || 'Не удалось удалить публикацию, попробуйте еще раз');
            })
    }
}

const PostsRow = ({post, onHidePost, onPublishPost, onDeletePost,showActions}) => {
    let canHide, canPublish, canDelete;
    const createdDate = moment(post.auditor.createdDate, "DD-MM-YYYY hh:mm", locale).fromNow()
    if (showActions) {
        canHide = post.links.find(link => link.rel === 'hide');
        canPublish = post.links.find(link => link.rel === 'publish');
        canDelete = post.links.find(link => link.rel === 'delete');
    }
    return (
        <Segment key={post.id}>
            <Item.Group>
                <Item>
                    {
                        post.preview !== null &&
                        <Item.Image as={Link} to={'/posts/' + post.id} src={post.preview.url}/>
                    }
                    <Item.Content>
                        <Item.Header as={Link} to={'/posts/' + post.id}>{post.title}</Item.Header>
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
                        {
                            showActions &&
                            <ButtonGroup basic size={'mini'} compact>
                                {
                                    canDelete &&
                                    <Item.Meta>
                                        <Button onClick={() => onDeletePost(post.id)}
                                                size={'mini'} compact>Удалить</Button>
                                    </Item.Meta>
                                }
                                {
                                    canHide &&
                                    <Item.Meta>
                                        <Button onClick={() => onHidePost(post.id)}
                                                size={'mini'} compact>Cкрыть</Button>
                                    </Item.Meta>
                                }
                                {
                                    canPublish &&
                                    <Item.Meta>
                                        <Button onClick={() => onPublishPost(post.id)}
                                                size={'mini'} compact>Опубликовать</Button>
                                    </Item.Meta>
                                }
                            </ButtonGroup>
                        }
                        <Item.Description>{post.description}</Item.Description>
                    </Item.Content>
                </Item>

            </Item.Group>
        </Segment>
    )
}

export default PostsView;