import React, {Component} from 'react';
import {Link, Redirect} from 'react-router-dom'
import {login, signup} from '../service/UserService';
import Alert from 'react-s-alert';
import {Button, Form, Segment} from "semantic-ui-react";
import OauthButtons from "./OauthButtons";
import {ACCESS_TOKEN} from "../util/Constants";

class Signup extends Component {
    render() {
        if (this.props.authenticated) {
            return <Redirect
                to={{
                    pathname: "/",
                    state: {from: this.props.location}
                }}/>;
        }
        document.title = 'Регистрация'
        return (
            <Segment>
                <OauthButtons title='Зарегистрироваться через'/>
                <SignupForm {...this.props} />
                <div style={{paddingTop: '15px'}}>
                    Уже есть аккаунт? <Link to="/login">Войти!</Link>
                </div>
            </Segment>
        )
    }
}

class SignupForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            email: '',
            password: ''
        }
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
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
        const signUpRequest = Object.assign({}, this.state);
        signup(signUpRequest).then(response => {
            login(signUpRequest).then(response => {
                localStorage.setItem(ACCESS_TOKEN, response.accessToken);
                Alert.success("Регистрация прошла успешно");
                this.setState({
                    authenticated: true
                })
            }).catch(error => {
                Alert.error((error && error.message) || 'Что-то пошло не так, попробуйте еще раз');
            });
        }).catch(error => {
            if (error && error.errors) {
                error.errors.map(msg => Alert.error(msg.defaultMessage))
            }
        });
    }

    render() {
        const {name, email, password, authenticated} = this.state
        if (authenticated) {
            window.location.reload()
        }
        return (
            <Form style={{paddingTop: '10px'}} onSubmit={this.handleSubmit}>
                <Form.Field>
                    <label>Имя</label>
                    <Form.Input type='text'
                                name='name'
                                placeholder='Имя'
                                value={name}
                                onChange={this.handleInputChange}
                                required
                    />
                </Form.Field>
                <Form.Field>
                    <label>Email</label>
                    <Form.Input type='email'
                                name='email'
                                placeholder='Email'
                                value={email}
                                onChange={this.handleInputChange}
                                required
                    />
                </Form.Field>
                <Form.Field>
                    <label>Пароль</label>
                    <Form.Input type='password'
                                name='password'
                                placeholder='Пароль'
                                value={password}
                                onChange={this.handleInputChange}
                                required
                    />
                </Form.Field>
                <Button primary style={{backgroundColor: '#175e6b'}}>Зарегистрироваться</Button>
            </Form>
        );
    }
}

export default Signup