import React, {Component} from 'react';
import {Form, Segment} from "semantic-ui-react";

class Contacts extends Component {

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
            <div>
                <Segment>
                    <Form>
                        <Form.Field>
                            <label>Имя</label>
                            <input placeholder='Имя' />
                        </Form.Field>
                    </Form>
                </Segment>
            </div>
        )
    }
}

export default Contacts;