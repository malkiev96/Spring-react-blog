import React, {Component} from 'react';
import {ACCESS_TOKEN} from '../../util/Constants';
import {login} from '../../api/UserService';
import {Link, Redirect} from 'react-router-dom'
import Alert from 'react-s-alert';
import {Button, Form, Segment} from "semantic-ui-react";
import OauthButtons from "./OauthButtons";

class Login extends Component {
  componentDidMount() {
    if (this.props.location.state && this.props.location.state.error) {
      setTimeout(() => {
        Alert.error(this.props.location.state.error, {
          timeout: 5000
        });
        this.props.history.replace({
          pathname: this.props.location.pathname,
          state: {}
        });
      }, 100);
    }
  }

  render() {
    if (this.props.authenticated) {
      return <Redirect
          to={{
            pathname: "/",
            state: {from: this.props.location}
          }}/>;
    }
    document.title = 'Войти'
    return (
        <Segment>
          <OauthButtons title={'Войти с помощью'}/>
          <LoginForm {...this.props} />
          <div style={{paddingTop: '15px'}}>
            или <Link to="/signup">зарегистрироваться</Link>
          </div>
        </Segment>
    );
  }
}

class LoginForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      email: '',
      password: '',
      authenticated: false
    };
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
    const loginRequest = Object.assign({}, this.state);
    login(loginRequest)
    .then(response => {
      localStorage.setItem(ACCESS_TOKEN, response.accessToken);
      Alert.success("Вы успешно вошли в систему");
      this.setState({
        authenticated: true
      })
    }).catch(error => {
      Alert.error((error && error.message)
          || 'Что-то пошло не так, попробуйте еще раз');
    });
  }

  render() {
    const {authenticated, email, password} = this.state;
    if (authenticated) {
      window.location.reload()
    }
    return (
        <Form style={{paddingTop: '10px'}} onSubmit={this.handleSubmit}>
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
          <Button primary style={{backgroundColor: '#175e6b'}}>Войти</Button>
        </Form>
    );
  }
}

export default Login