import React, {Component} from 'react';
import {Segment} from "semantic-ui-react";

class Home extends Component {

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
                home page
            </Segment>
        )
    }
}

export default Home;