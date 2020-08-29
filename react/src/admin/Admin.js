import React, {Component} from 'react';
import {Button, Header, Loader, Message, Pagination, Table} from 'semantic-ui-react'
import {deleteMessage, getMessages} from "../service/ContactService";
import NotFound from "../common/notFound/NotFound";
import Alert from "react-s-alert";

class Admin extends Component {

    constructor(props) {
        super(props);
        this.state = {
            messages: {
                loading: true,
                page: null,
                messages: [],
                error: false
            },
            page: 1
        }
        this.onPageChange = this.onPageChange.bind(this);
        this.onDeleteMessage = this.onDeleteMessage.bind(this);
    }

    componentDidMount() {
        this.loadMessages(this.state.page)
    }

    loadMessages(page) {
        getMessages(page).then(result =>
            this.setState({
                messages: {
                    messages: result['content'],
                    page: result['page'],
                    loading: false,
                    error: false
                }
            })).catch(() =>
            this.setState({
                messages: {
                    messages: [],
                    page: null,
                    loading: false,
                    error: true
                }
            }))
    }

    onPageChange(e, {activePage}) {
        this.setState({page: activePage})
        this.loadMessages(activePage)
    }

    onDeleteMessage(id) {
        deleteMessage(id).then(() => {
            this.loadMessages(this.state.page)
            Alert.success("Сообщение успешно удалено")
        }).catch(() => {
            Alert.error('Не удалось удалить сообщение, попробуйте еще раз');
        })
    }

    render() {
        const {messages, page, loading, error} = this.state.messages
        if (loading) return <Loader active inline='centered'/>
        if (error) return <NotFound/>

        return (
            <div>
                <Header as='h3' dividing>Сообщения</Header>
                {
                    messages.length > 0 ?
                        <Table celled>
                            <Table.Header>
                                <Table.Row>
                                    <Table.HeaderCell>Имя</Table.HeaderCell>
                                    <Table.HeaderCell>Email</Table.HeaderCell>
                                    <Table.HeaderCell>Дата отправления</Table.HeaderCell>
                                    <Table.HeaderCell>Сообщение</Table.HeaderCell>
                                    <Table.HeaderCell>Удалить</Table.HeaderCell>
                                </Table.Row>
                            </Table.Header>

                            <Table.Body>
                                {
                                    messages.map((msg, index) => (
                                        <Table.Row key={index}>
                                            <Table.Cell>{msg.name}</Table.Cell>
                                            <Table.Cell>
                                                <a href={'mailto:' + msg.email}>{msg.email}</a>
                                            </Table.Cell>
                                            <Table.Cell>{msg.createdDate}</Table.Cell>
                                            <Table.Cell>{msg.message}</Table.Cell>
                                            <Table.Cell>
                                                <Button onClick={() => this.onDeleteMessage(msg.id)} negative
                                                        icon='delete'/>
                                            </Table.Cell>
                                        </Table.Row>
                                    ))
                                }
                            </Table.Body>
                            {
                                page && page.totalPages > 1 &&
                                <Pagination style={{marginTop: '15px'}} activePage={page.number + 1}
                                            firstItem={null} lastItem={null} onPageChange={this.onPageChange}
                                            totalPages={page.totalPages}/>
                            }
                        </Table> :
                        <Message>Сообщений нет</Message>
                }
            </div>
        )
    }
}

export default Admin;