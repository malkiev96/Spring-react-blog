import {Dimmer, Image, Loader, Segment} from "semantic-ui-react";
import React, {Component} from 'react';

class DataLoader extends Component {
    render() {
        return (
            <Segment>
                <Dimmer active inverted>
                    <Loader active size='large' inline='centered'/>
                </Dimmer>
                <Image src='https://react.semantic-ui.com/images/wireframe/paragraph.png'/>
            </Segment>
        );
    }
}

export default DataLoader;