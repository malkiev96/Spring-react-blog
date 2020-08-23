import React, {Component} from 'react';
import {Button, Form, Header, Image, Segment, Table, TextArea} from "semantic-ui-react";
import {getAdmins, sendContactMessage} from "../service/UserService";
import {Link} from "react-router-dom";
import Alert from "react-s-alert";

class Contacts extends Component {

    constructor(props) {
        super(props);
        this.state = {
            admins: [],
            name: '',
            email: '',
            message: ''
        }
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        this.loadAdmins()
    }

    loadAdmins() {
        getAdmins().then(response => {
            this.setState({
                admins: response['content']
            })
        }).catch(() => {
            this.setState({
                admins: []
            })
        })
    }

    handleInputChange(event) {
        const target = event.target;
        const inputName = target.name;
        const inputValue = target.value;

        this.setState({
            [inputName]: inputValue
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        const {name, email, message} = this.state
        const msgRequest = {name, email, message}
        sendContactMessage(msgRequest).then(() => {
            this.setState({
                name: '',
                email: '',
                message: ''
            })
            Alert.success("Сообщение успешно отправлено")
        }).catch(() => {
            Alert.error('Не удалось отправить сообщение, попробуйте еще раз');
        })
    }

    render() {
        const {admins, name, email, message} = this.state
        return (
            <div>
                <Segment>
                    <Header as='h3' dividing>Администраторы</Header>
                    <Table basic='very' celled collapsing>
                        <Table.Body>
                            {
                                admins.map(admin =>
                                    <Table.Row key={admin.id}>
                                        <Table.Cell>
                                            <Header as='h4' image>
                                                <Image src={admin.imageUrl} rounded size='mini'/>
                                                <Header.Content>
                                                    <Link to={'/user/' + admin.id}>{admin.name}</Link>
                                                </Header.Content>
                                            </Header>
                                        </Table.Cell>
                                        <Table.Cell>
                                            <a href={'mailto:' + admin.email}>{admin.email}</a>
                                        </Table.Cell>
                                    </Table.Row>
                                )
                            }
                        </Table.Body>
                    </Table>
                    <Header as='h3' dividing>Отправить сообщение</Header>
                    <Form onSubmit={this.handleSubmit}>
                        <Form.Group widths='equal'>
                            <Form.Field required>
                                <label>Имя</label>
                                <Form.Input type='text'
                                            name='name'
                                            placeholder='Имя'
                                            value={name}
                                            onChange={this.handleInputChange}
                                            required
                                />
                            </Form.Field>
                            <Form.Field required>
                                <label>Email</label>
                                <Form.Input type='email'
                                            name='email'
                                            placeholder='Email'
                                            value={email}
                                            onChange={this.handleInputChange}
                                            required
                                />
                            </Form.Field>
                        </Form.Group>
                        <Form.Field required>
                            <label>Сообщение</label>
                            <TextArea value={message}
                                      name={'message'}
                                      onChange={this.handleInputChange}
                                      required
                                      placeholder={'Ваше сообщение'}
                                      rows={15}/>
                        </Form.Field>
                        <Button style={{backgroundColor: '#175e6b'}} content='Отправить' primary/>
                    </Form>
                </Segment>
            </div>
        )
    }
}

export default Contacts;