import React, {Component} from 'react';
import {ACCESS_TOKEN} from './Constants';
import {Redirect} from 'react-router-dom'

class OAuth2RedirectHandler extends Component {
    getUrlParameter(name) {
        //eslint-disable-next-line
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        const regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
        const results = regex.exec(this.props.location.search);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    };

    render() {
        const token = this.getUrlParameter('token');
        const error = this.getUrlParameter('error');

        if (token) {
            localStorage.setItem(ACCESS_TOKEN, token)
            this.props.history.push('/')
            window.location.reload()
        } else return <Redirect to={{
            pathname: "/login",
            state: {
                from: this.props.location,
                error: error
            }
        }}/>
    }
}

export default OAuth2RedirectHandler;