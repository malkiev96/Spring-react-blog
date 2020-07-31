import React, {Component} from 'react';
import {getUser} from '../util/APIUtils';
import {Button, Divider, Header, Icon, Image, Loader, Segment, Table} from "semantic-ui-react";
import NotFound from "../common/NotFound";
import {Link} from "react-router-dom";
import {Tab, TabList, TabPanel, Tabs} from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import UserPosts from "./UserPosts";

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
            profile: profile,
            user: {
                user: null,
                error: false,
                loading: true
            }
        }
        this.loadUser = this.loadUser.bind(this);
    }

    componentDidUpdate(prevProps) {
        if (this.props.location.pathname !== prevProps.location.pathname) {
            this.loadUser(parseInt(this.props.match.params.id))
        }
    }

    componentDidMount() {
        this.loadUser(this.state.id)
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
                            <Link to={'/user/' + user.user.id + '/edit'}>
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
                            <Table.Row>
                                <Table.Cell width={3}>Дата рождения</Table.Cell>
                                <Table.Cell>{user.user.birthday}</Table.Cell>
                            </Table.Row>
                            <Table.Row>
                                <Table.Cell width={3}>Город</Table.Cell>
                                <Table.Cell>{user.user.city}</Table.Cell>
                            </Table.Row>
                            <Table.Row>
                                <Table.Cell width={3}>О себе</Table.Cell>
                                <Table.Cell>
                                    {user.user.about}
                                </Table.Cell>
                            </Table.Row>

                        </Table.Body>
                    </Table>
                </Segment>
                {
                    !profile &&
                    <div>
                        <Header>Статьи</Header>
                        <UserPosts id={user.user.id} status={'PUBLISHED'}/>
                    </div>
                }
                {
                    profile &&
                    <div>
                        <Header>Статьи</Header>
                        <Tabs>
                            <TabList>
                                <Tab>Опубликованные</Tab>
                                <Tab>Неопубликованное</Tab>
                                <Tab>Ожидают подтверждения</Tab>
                                <Tab>Удаленные</Tab>
                            </TabList>
                            <TabPanel>
                                <UserPosts id={user.user.id} status={'PUBLISHED'}/>
                            </TabPanel>
                            <TabPanel>
                                <UserPosts id={user.user.id} status={'CREATED'}/>
                            </TabPanel>
                            <TabPanel>
                                <UserPosts id={user.user.id} status={'PENDING'}/>
                            </TabPanel>
                            <TabPanel>
                                <UserPosts id={user.user.id} status={'DELETED'}/>
                            </TabPanel>
                        </Tabs>
                    </div>
                }

            </div>
        )
    }
}

export default UserDetail;