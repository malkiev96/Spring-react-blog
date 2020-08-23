import React, {Component} from 'react';
import {Segment} from "semantic-ui-react";

class About extends Component {

    constructor(props) {
        super(props);
        this.state = {
            posts: {
                loading: true,
                posts: [],
                page: null
            }
        }
    }

    render() {
        return (
            <Segment>
                About page
            </Segment>
        )
    }
}

export default About;