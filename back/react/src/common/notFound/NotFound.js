import React, {Component} from 'react';
import './NotFound.css';
import {Link} from 'react-router-dom';
import {Button, Segment} from "semantic-ui-react";

class NotFound extends Component {
    render() {
        return (
            <Segment className='page-not-found'>
                <h1 className="title">404</h1>
                <div className="desc">Страница не найдена</div>
                <Link to="/"><Button size='large' primary>На главную</Button></Link>
            </Segment>
        );
    }
}

export default NotFound;