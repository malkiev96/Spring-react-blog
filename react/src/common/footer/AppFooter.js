import React, {Component} from 'react';
import './AppFooter.css';
import {Container} from "semantic-ui-react";
import {Link} from "react-router-dom";
import {ADMIN_ID, ADMIN_NAME} from "../../util/Constants";

class AppFooter extends Component {
    render() {
        return (
            <div className='app-footer'>
                <Container>
                    © {new Date().getFullYear()}. Created by
                    <Link to={`/user/${ADMIN_ID}`}>
                        <span id='footer-user-link'> {ADMIN_NAME}</span>
                    </Link>
                </Container>
            </div>
        );
    }
}

export default AppFooter;