import React, {Component} from 'react';
import {Image, Message, Segment, Table} from 'semantic-ui-react'
import {getAllUsers} from "../service/UserService";
import {Link} from "react-router-dom";

class UserInfo extends Component {

    constructor(props) {
        super(props);
        this.state = {
            users: []
        }
    }

    componentDidMount() {
        this.loadUsers()
    }

    loadUsers() {
        getAllUsers().then(result =>
            this.setState({
                users: result.content
            })).catch(() =>
            this.setState({
                users: []
            }))
    }

    render() {
        const {users} = this.state

        return (
            <Segment>
                {
                    users.length > 0 ?
                        <Table celled>
                            <Table.Header>
                                <Table.Row>
                                    <Table.HeaderCell/>
                                    <Table.HeaderCell>Id</Table.HeaderCell>
                                    <Table.HeaderCell>Имя</Table.HeaderCell>
                                    <Table.HeaderCell>Email</Table.HeaderCell>
                                </Table.Row>
                            </Table.Header>
                            <Table.Body>
                                {
                                    users.map((user, index) => (
                                        <Table.Row key={index}>
                                            <Table.Cell>
                                                <Image avatar src={user.imageUrl}/>
                                            </Table.Cell>
                                            <Table.Cell>{user.id}</Table.Cell>
                                            <Table.Cell>
                                                <Link to={`/user/${user.id}`}>{user.name}</Link>
                                            </Table.Cell>
                                            <Table.Cell>{user.email}</Table.Cell>
                                        </Table.Row>
                                    ))
                                }
                            </Table.Body>
                        </Table> :
                        <Message>Пользователей нет</Message>
                }
            </Segment>
        )
    }
}

export default UserInfo;