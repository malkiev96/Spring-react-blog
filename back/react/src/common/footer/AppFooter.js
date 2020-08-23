import React, {Component} from 'react';
import './AppFooter.css';
import {Container} from "semantic-ui-react";

class AppFooter extends Component {
    render() {
        return (
            <div className='app-footer'>
                <Container>
                    Copyright © 2020. All rights reserved!
                </Container>
            </div>
        );
    }
}

export default AppFooter;