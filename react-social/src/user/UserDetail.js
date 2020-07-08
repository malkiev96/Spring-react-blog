import React, {Component} from 'react';
import {getPostsByUser, getUser} from '../util/APIUtils';
import {Button, Divider, Header, Icon, Image, Loader, Pagination, Segment, Table} from "semantic-ui-react";
import NotFound from "../common/NotFound";
import {Link} from "react-router-dom";
import PostsView from "../post/PostsView";

class UserDetail extends Component {

    constructor(props) {
        super(props);
        const id = parseInt(this.props.match.params.id)
        let profile
        try {
            profile = id === props.currentUser.currentUser.id
        } catch (e) {
            profile = false
        }
        this.state = {
            id: id,
            page: 1,
            profile: profile,
            user: {
                user: null,
                error: false,
                loading: true
            },
            posts: {
                posts: [],
                loading: true
            }
        }
        this.loadUser = this.loadUser.bind(this);
        this.loadPosts = this.loadPosts.bind(this);
        this.pageChange = this.pageChange.bind(this);
    }

    componentDidUpdate(prevProps) {
        if (this.props.location.pathname !== prevProps.location.pathname) {
            this.loadUser(parseInt(this.props.match.params.id))
            this.loadPosts(parseInt(this.props.match.params.id), this.state.page)
        }
    }

    componentDidMount() {
        this.loadUser(this.state.id)
        this.loadPosts(this.state.id, this.state.page)
    }

    loadPosts(userId, page) {
        getPostsByUser(userId, page, 10, 'createdDate').then(response => {
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

    loadUser(id) {
        getUser(id).then(response => {
            this.setState({
                user: {
                    user: response,
                    loading: false,
                    error: false
                }
            })
        }).catch(error => {
            this.setState({
                user: {
                    user: null,
                    loading: false,
                    error: true
                }
            })
        })
    }

    render() {
        const {user, profile, posts} = this.state
        if (user.loading) return <Loader active inline='centered'/>
        if (user.error) return <NotFound/>
        return (
            <div>
                <Segment>
                    <Header as='h2'>
                        <Image avatar src={user.user.imageUrl}/>
                        {user.user.name}
                        {
                            profile &&
                            <Link to={'/profile/'}>
                                <Button floated='right' size='mini' primary>Настроить профиль</Button>
                            </Link>
                        }
                    </Header>
                    <Divider horizontal>
                        <Header as='h4'><Icon name='tag'/>Информация</Header>
                    </Divider>
                    <Table basic definition>
                        <Table.Body>
                            <Table.Row>
                                <Table.Cell width={3}>Группа</Table.Cell>
                                <Table.Cell>
                                    {
                                        user.user.admin ? 'Администраторы' : 'Пользователи'
                                    }
                                </Table.Cell>
                            </Table.Row>
                        </Table.Body>
                    </Table>
                </Segment>

                <Header as='h1' dividing>Публикации</Header>
                <PostsView posts={posts}/>
                {
                    posts.posts.length !== 0 &&
                    <Pagination
                        activePage={posts.page.number + 1}
                        firstItem={null}
                        lastItem={null}
                        onPageChange={this.pageChange}
                        totalPages={posts.page.totalPages}
                    />
                }


            </div>
        )
    }

    pageChange(e, {activePage}) {
        this.setState({
            page: activePage
        })
        this.loadPosts(this.state.id, activePage)
    }
}

export default UserDetail;