import React, {Component} from 'react';
import {
  Button,
  Comment,
  Confirm,
  Form,
  Header,
  Icon,
  Item,
  List,
  Segment
} from "semantic-ui-react";
import {
  blockPost,
  deletePost,
  getPostById,
  hidePost,
  publishPost
} from "../../api/Posts";
import {addComment, deleteComment, getComments} from '../../api/Comments'
import moment from "moment";
import {locale} from "moment/locale/ru";
import NotFound from "../common/notFound/NotFound";
import './posts.css'
import {Link} from "react-router-dom";
import Alert from "react-s-alert";
import "pure-react-carousel/dist/react-carousel.es.css";
import {CarouselProvider, Image, Slide, Slider} from "pure-react-carousel";
import ReactMarkdown from "react-markdown";
import {getDocumentSrc} from "../../api/Documents";
import DataLoader from "../common/DataLoader";

class PostsDetail extends Component {

  constructor(props) {
    super(props);
    this.state = {
      id: parseInt(this.props.match.params.id, 10),
      post: {
        post: null,
        error: false,
        loading: true
      },
      currentUser: props.currentUser,
      comments: {
        comments: [],
        error: false,
        loading: true
      },
      images: [],
      documents: [],
      message: '',
      openDel: false,
      openHide: false,
      openPublish: false,
      openBlock: false
    }
    this.loadData = this.loadData.bind(this);
    this.loadComments = this.loadComments.bind(this);
    this.deleteHandler = this.deleteHandler.bind(this);
    this.createHandler = this.createHandler.bind(this);
    this.replyClick = this.replyClick.bind(this);
    this.replyHandler = this.replyHandler.bind(this);
    this.createComment = this.createComment.bind(this);
    this.onDeletePost = this.onDeletePost.bind(this)
    this.onHidePost = this.onHidePost.bind(this)
    this.onPublishPost = this.onPublishPost.bind(this)
    this.onBlockPost = this.onBlockPost.bind(this)
  }

  openDel = () => this.setState({openDel: true})
  closeDel = () => this.setState({openDel: false})
  openHide = () => this.setState({openHide: true})
  closeHide = () => this.setState({openHide: false})
  openPublish = () => this.setState({openPublish: true})
  closePublish = () => this.setState({openPublish: false})
  openBlock = () => this.setState({openBlock: true})
  closeBlock = () => this.setState({openBlock: false})

  componentDidUpdate(prevProps) {
    if (this.props.location.pathname !== prevProps.location.pathname) {
      this.loadData(parseInt(this.props.match.params.id, 10))
    }
  }

  componentDidMount() {
    this.loadData(this.state.id)
  }

  loadData(id) {
    getPostById(id).then(response => {
      document.title = response.title
      this.setState({
        post: {
          post: response,
          error: false,
          loading: false
        },
        images: response.documents.filter(img => img.isImage),
        documents: response.documents.filter(img => !img.isImage)
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
    this.loadComments(id)
  }

  createComment(postId, parentId = 0, message) {
    addComment(postId, parentId, message).then(() => {
      Alert.success("Комментарий успешно добавлен");
      this.setState({message: '', messageReply: ''})
      this.loadComments(postId)
    }).catch(error => {
      Alert.error((error && error.message)
          || 'Что-то пошло не так, попробуйте еще раз');
    })
  }

  createHandler() {
    this.createComment(this.state.id, 0, this.state.message)
  }

  replyClick(id) {
    let comments = this.replyChildren(id, this.state.comments.comments)
    this.setState({
      comments: {
        comments: comments,
        error: false,
        loading: false
      },
    })
  }

  replyChildren(id, comments) {
    comments.map(c => {
      if (c.id === id) {
        c.showReply = !c.showReply
      } else {
        c.showReply = false
      }
      if (c.children.length !== 0) {
        c.children = this.replyChildren(id, c.children)
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
      Alert.error((error && error.message)
          || 'Что-то пошло не так, попробуйте еще раз');
    })
  }

  loadComments(postId) {
    getComments(postId).then(response => {
      this.setState({
        comments: {
          comments: response,
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

  onHidePost() {
    this.setState({openHide: false})
    hidePost(this.state.id).then(() => {
      Alert.success("Публикация скрыта")
      this.loadData(this.state.id)
    }).catch((error) => {
      Alert.error((error && error.message)
          || 'Не удалось скрыть публикацию, попробуйте еще раз');
    })
  }

  onPublishPost() {
    this.setState({openPublish: false})
    publishPost(this.state.id).then(() => {
      Alert.success("Статья опубликована")
      this.loadData(this.state.id)
    }).catch((error) => {
      Alert.error((error && error.message)
          || 'Не удалось опубликовать статью, попробуйте еще раз');
    })
  }

  onDeletePost() {
    this.setState({openDel: false})
    deletePost(this.state.id).then(() => {
      Alert.success("Публикация удалена")
      this.loadData(this.state.id)
    }).catch((error) => {
      Alert.error((error && error.message)
          || 'Не удалось удалить публикацию, попробуйте еще раз');
    })
  }

  onBlockPost() {
    this.setState({openBlock: false})
    blockPost(this.state.id).then(() => {
      Alert.success("Публикация заблокирована")
      this.loadData(this.state.id)
    }).catch((error) => {
      Alert.error((error && error.message)
          || 'Не удалось заблокировать публикацию, попробуйте еще раз');
    })
  }

  canDeletePost(post, user) {
    return (post?.status === 'CREATED' || post?.status === 'PUBLISHED')
        && post?.createdBy?.id === user?.id
  }

  canEditPost(post, user) {
    return (post?.status === 'CREATED' || post?.status === 'DELETED')
        && post?.createdBy?.id === user?.id
  }

  canHidePost(post, user) {
    return post?.status === 'PUBLISHED' && post?.createdBy?.id === user?.id
  }

  canPublishPost(post, user) {
    return (post?.status === 'CREATED' || post?.status === 'DELETED')
        && post?.createdBy?.id === user?.id
  }

  canBlockPost(post) {
    return this.state.currentUser.isAdmin && post?.status !== 'BLOCKED'
  }

  render() {
    const {loading, error, post} = this.state.post
    const {
      comments, openDel, openPublish, openBlock,
      openHide, images, documents
    } = this.state
    const user = this.state.currentUser?.currentUser;
    if (loading) {
      return <DataLoader/>
    }
    if (error) {
      return <NotFound/>
    }
    const createdDate = moment(post.createdDate, "DD.MM.YYYY hh:mm",
        locale).fromNow()
    const canEdit = this.canEditPost(post, user)
    const canHide = this.canHidePost(post, user)
    const canPublish = this.canPublishPost(post, user)
    const canDelete = this.canDeletePost(post, user)
    const canBlock = this.canBlockPost(post)

    return (
        <div>
          <Segment>
            {
                canEdit &&
                <Link to={`/post/${post.id}/edit`}>
                  <Button style={{backgroundColor: '#175e6b'}} floated='right'
                          size='mini' primary>Редактировать
                  </Button>
                </Link>
            }
            {
                canHide &&
                <span>
                            <Button style={{backgroundColor: '#175e6b'}}
                                    floated='right'
                                    onClick={this.openHide} size='mini' primary>Скрыть
                        </Button>
                        <Confirm open={openHide} size='mini'
                                 content='Вы уверены что хотите скрыть публикацию?'
                                 onCancel={this.closeHide}
                                 onConfirm={this.onHidePost}/>
                        </span>
            }
            {
                canPublish &&
                <span>
                            <Button style={{backgroundColor: '#175e6b'}}
                                    floated='right'
                                    onClick={this.openPublish} size='mini'
                                    primary>Опубликовать
                            </Button>
                            <Confirm open={openPublish} size='mini'
                                     content='Вы уверены что хотите опубликовать статью?'
                                     onCancel={this.closePublish}
                                     onConfirm={this.onPublishPost}/>
                        </span>
            }
            {
                canDelete &&
                <span>
                            <Button style={{backgroundColor: '#175e6b'}}
                                    floated='right'
                                    onClick={this.openDel} size='mini' primary>Удалить
                            </Button>
                              <Confirm open={openDel}
                                       content='Вы уверены что хотите удалить публикацию?'
                                       size='mini' onCancel={this.closeDel}
                                       onConfirm={this.onDeletePost}/>
                        </span>
            }
            {
                canBlock &&
                <span>
                            <Button style={{backgroundColor: '#bd0629'}}
                                    floated='right'
                                    onClick={this.openBlock} size='mini'
                                    primary>BLOCK
                            </Button>
                              <Confirm open={openBlock}
                                       content='Вы уверены что хотите заблокировать публикацию?'
                                       size='mini' onCancel={this.closeBlock}
                                       onConfirm={this.onBlockPost}/>
                        </span>
            }
            <Header as='h1' dividing>{post.title}</Header>
            {
                post.preview !== null &&
                <Item.Image src={getDocumentSrc(post.preview.fileId)}
                            size='large'
                            bordered/>
            }
            <div id={'post-text'}>
              <ReactMarkdown source={post.text}/>
            </div>
          </Segment>
          {
              images && images.length !== 0 &&
              <Segment>
                <Header as='h3' dividing>Прикрепленные изображения</Header>
                <CarouselProvider
                    naturalSlideWidth={100}
                    naturalSlideHeight={50}
                    totalSlides={images.length}>
                  <Slider>
                    {
                      images.map((doc, index) =>
                          <Slide key={index} index={index}>
                            <Image src={getDocumentSrc(doc.fileId)}
                                   hasMasterSpinner/>
                          </Slide>
                      )
                    }
                  </Slider>
                </CarouselProvider>
              </Segment>
          }
          {
              documents && documents.length !== 0 &&
              <Segment>
                <Header as='h3' dividing>Прикрепленные документы</Header>
                {
                  documents.map((doc, index) =>
                      <Segment key={index} id={index} raised>
                        <a href={getDocumentSrc(doc.fileId)}>{doc.filename}</a>
                      </Segment>
                  )
                }
              </Segment>
          }
          <Segment>
            <Item.Group>
              <Item>
                <Item.Content>
                  <Item.Meta>
                    <Link to={'/category/' + post.category.code}>
                      <Icon name='folder'/>
                      {' ' + post.category.name}
                    </Link>
                  </Item.Meta>
                  <Item.Meta>
                    <Link to={'/user/' + post?.createdBy?.id}>
                      <Icon name='user'/>
                      {' ' + post?.createdBy?.name}
                    </Link>
                    {' ' + createdDate}
                  </Item.Meta>
                  <Item.Meta>
                    <Icon color='grey' name='eye'/>
                    <span>{post.viewCount} просмотров</span>
                  </Item.Meta>
                  <Item.Meta>
                    <Icon name='unlock'/>
                    <span>{post.status}</span>
                  </Item.Meta>
                </Item.Content>
              </Item>
            </Item.Group>

            <Header as='h3' dividing>Теги</Header>
            <List selection horizontal>
              {post.tags.map(tag => {
                return (
                    <List.Item key={tag.id}>
                      <Link to={'/tags/' + tag.code}>
                        <Button compact active size={'tiny'}
                                basic>{tag.name}</Button>
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
                  return <this.CommentItem user={user} comment={comment}
                                           key={comment.id}/>
                })
              }
            </Comment.Group>
            {
              this.props.currentUser.authenticated ?
                  <Form reply onSubmit={this.createHandler}>
                    <Form.TextArea value={this.state.message} rows={10}
                                   required
                                   onChange={event => this.setState(
                                       {message: event.target.value})}/>
                    <Button style={{backgroundColor: '#175e6b'}}
                            content='Добавить комментарий'
                            labelPosition='left' icon='edit' primary/>
                  </Form>
                  : <h4><Link to={'/login'}>Авторизуйтесь</Link>, чтобы оставить
                    комментарий</h4>
            }
          </Segment>
        </div>
    )
  }

  CommentItem = (props) => {
    const {comment, user} = props;
    const canReply = user && comment?.createdBy?.id !== user?.id
    const canDelete = comment?.createdBy?.id === user?.id
    const children = comment.children
    const createdDate = moment(comment.createdDate, "DD.MM.YYYY hh:mm",
        locale).fromNow()
    const createdBy = comment.createdBy;
    return (
        <Comment>
          {
              createdBy.preview && !comment.deleted &&
              <Comment.Avatar src={getDocumentSrc(createdBy.preview?.fileId)}/>
          }


          <Comment.Content>
            <Comment.Author as='a'
                            href={'/user/' + createdBy.id}>{comment.deleted ? ''
                : createdBy.name}</Comment.Author>
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
                                      onClick={() => this.replyClick(
                                          comment.id)}>Ответить</Comment.Action>
                  }
                  {
                      canDelete &&
                      <Comment.Action as={'a'} value={comment.id}
                                      onClick={() => this.deleteHandler(
                                          comment.id)}>Удалить</Comment.Action>
                  }
                </Comment.Actions>
            }
            {
                canReply && !comment.deleted && comment.showReply &&
                <Form onSubmit={() => this.replyHandler(comment.id)}>
                  <Form.TextArea value={this.state.messageReply} required
                                 onChange={event => this.setState(
                                     {messageReply: event.target.value})}/>
                  <Button style={{backgroundColor: '#175e6b'}}
                          content='Добавить комментарий'
                          labelPosition='left' icon='edit' primary/>
                </Form>
            }
          </Comment.Content>
          {
              children.length !== 0 &&
              <Comment.Group>
                {
                  children.map(c => <this.CommentItem key={c.id} comment={c}/>)
                }
              </Comment.Group>
          }
        </Comment>
    )
  }

}

export default PostsDetail;