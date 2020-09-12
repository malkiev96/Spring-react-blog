import React, {Component} from 'react';
import {getUser} from '../service/UserService';
import {Button, Divider, Header, Icon, Image, Loader, Segment, Table} from "semantic-ui-react";
import NotFound from "../common/notFound/NotFound";
import {Link} from "react-router-dom";
import {Tab, TabList, TabPanel, Tabs} from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import UserPosts from "./UserPosts";

class UserDetail extends Component {

    constructor(props) {
        super(props);
        const id = parseInt(this.props.match.params.id, 10)
        const profile = props.currentUser.authenticated && props.currentUser.currentUser.id === id
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
            this.loadUser(parseInt(this.props.match.params.id, 10))
        }
    }

    componentDidMount() {
        this.loadUser(this.state.id)
    }

    loadUser(id) {
        getUser(id).then(response => {
            document.title = 'Профиль - '+ response.name
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
        const {user, profile} = this.state
        if (user.loading) return <Loader active inline='centered'/>
        if (user.error) return <NotFound/>
        const admin = this.props.currentUser.admin;
        return (
            <div>
                <Segment>
                    <Header as='h2'>
                        <Image avatar src={user.user.imageUrl}/>
                        {user.user.name}
                        {
                            profile &&
                            <Link to={'/user/' + user.user.id + '/edit'}>
                                <Button style={{backgroundColor: '#175e6b'}}
                                        floated='right'
                                        size='mini' primary>Настроить профиль
                                </Button>
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
                                <Table.Cell>{user.user.birthDate}</Table.Cell>
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
                    !profile && !admin &&
                    <Tabs>
                        <TabList>
                            <Tab>Публикации</Tab>
                            <Tab>Добавленные в закладки</Tab>
                        </TabList>
                        <TabPanel>
                            <UserPosts profile={false} id={user.user.id} status={'PUBLISHED'}/>
                        </TabPanel>
                        <TabPanel>
                            <UserPosts profile={false} liked id={user.user.id} status={'PUBLISHED'}/>
                        </TabPanel>
                    </Tabs>
                }
                {
                    (profile || admin) &&
                    <Tabs>
                        <TabList>
                            <Tab>Опубликованные</Tab>
                            <Tab>Неопубликованное</Tab>
                            <Tab>Удаленные</Tab>
                            <Tab>Добавленные в закладки</Tab>
                        </TabList>
                        <TabPanel>
                            <UserPosts profile id={user.user.id} status={'PUBLISHED'}/>
                        </TabPanel>
                        <TabPanel>
                            <UserPosts profile id={user.user.id} status={'CREATED'}/>
                        </TabPanel>
                        <TabPanel>
                            <UserPosts profile id={user.user.id} status={'DELETED'}/>
                        </TabPanel>
                        <TabPanel>
                            <UserPosts liked profile id={user.user.id} status={'PUBLISHED'}/>
                        </TabPanel>
                    </Tabs>
                }
            </div>
        )
    }
}

export default UserDetail;