import React, {Component} from 'react';
import moment from "moment";
import {locale} from "moment/locale/ru";
import {likePost} from "../service/PostService"
import {Button, ButtonGroup, Divider, Header, Icon, Image, Segment} from "semantic-ui-react";
import {Link} from "react-router-dom";
import Alert from 'react-s-alert';
import './posts.css'
import {getDocumentSrc} from "../service/DocumentService";

class PostsItem extends Component {

    constructor(props) {
        super(props);
        this.state = {
            likedCount: props.post.likedCount,
            liked: props.post.liked,
        }
    }


    render() {
        const {post, onHidePost, onPublishPost, onDeletePost, showActions} = this.props
        let canHide, canPublish, canDelete
        let {likedCount, liked} = this.state
        const createdDate = moment(post.auditor.createdDate, "DD-MM-YYYY hh:mm", locale).fromNow()
        const canLike = post.links.find(link => link.rel === 'like')
        if (showActions) {
            canHide = post.links.find(link => link.rel === 'hide')
            canPublish = post.links.find(link => link.rel === 'publish')
            canDelete = post.links.find(link => link.rel === 'delete')
        }

        const addLike = (id) => {
            likePost(id).then((response) => {
                const msg = response ? 'Публикация сохранена в закладки' : 'Публикация удалена из закладок'
                Alert.success(msg)
                this.setState({
                    liked: response,
                    likedCount: response ? likedCount + 1 : likedCount - 1
                })
            }).catch((error) => {
                Alert.error((error && error.message) || 'Что-то пошло не так, попробуйте еще раз');
            })
        }

        return (
            <Segment key={post.id}>
                <Header size="large" as="h2">
                    <Header.Content style={{paddingBottom: '10px'}}>
                        <Link to={'/post/' + post.id}>{post.title}</Link>
                    </Header.Content>
                    <Header.Subheader>
                        <Link to={'/user/' + post.auditor.createdBy.id}>
                            {' ' + post.auditor.createdBy.name}
                        </Link>
                        {' ' + createdDate}
                    </Header.Subheader>
                </Header>
                {
                    post.preview !== null &&
                    <Image size={"large"} src={getDocumentSrc(post.preview.id)}/>
                }
                <Divider hidden/>
                <p>{post.description}</p>
                <div>
                    {
                        canLike
                            ? <Icon link onClick={() => addLike(post.id)}
                                    color={liked ? 'blue' : 'grey'}
                                    name='bookmark'/>
                            : <Icon color={liked ? 'blue' : 'grey'}
                                    name='bookmark'/>
                    }
                    <span>{likedCount}</span>
                    <span style={{float: 'right'}}>
                         <Icon color='grey' name='eye'/>
                         <span>{post.viewCount}</span>
                    </span>
                </div>
                <div>
                    {
                        showActions &&
                        <ButtonGroup style={{marginTop: '10px'}} basic size={'mini'} compact>
                            {
                                canDelete &&
                                <Button onClick={() => onDeletePost(post.id)} size={'mini'} compact>Удалить</Button>
                            }
                            {
                                canHide &&
                                <Button onClick={() => onHidePost(post.id)} size={'mini'} compact>Cкрыть</Button>
                            }
                            {
                                canPublish &&
                                <Button onClick={() => onPublishPost(post.id)}
                                        size={'mini'} compact>Опубликовать</Button>
                            }
                        </ButtonGroup>
                    }
                </div>
            </Segment>
        )
    }
}

export default PostsItem;