import React, {Component} from 'react';
import {Button, Message, Pagination, Segment, Table} from 'semantic-ui-react'
import {deleteMessage, getMessages} from "../../api/Contacts";
import NotFound from "../common/notFound/NotFound";
import Alert from "react-s-alert";
import DataLoader from "../common/DataLoader";

class MessageInfo extends Component {

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
    if (loading) {
      return <DataLoader/>
    }
    if (error) {
      return <NotFound/>
    }

    return (
        <Segment>
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
                              <Button onClick={() => {
                                if (window.confirm(
                                    "Are you sure?")) {
                                  this.onDeleteMessage(
                                      msg.id)
                                }
                              }} negative
                                      icon='delete'/>
                            </Table.Cell>
                          </Table.Row>
                      ))
                    }
                  </Table.Body>
                  {
                      page && page.totalPages > 1 &&
                      <Pagination style={{marginTop: '15px'}}
                                  activePage={page.number + 1}
                                  firstItem={null} lastItem={null}
                                  onPageChange={this.onPageChange}
                                  totalPages={page.totalPages}/>
                  }
                </Table> :
                <Message>Сообщений нет</Message>
          }
        </Segment>
    )
  }
}

export default MessageInfo;