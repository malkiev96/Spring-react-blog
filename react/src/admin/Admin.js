import React, {Component} from 'react';
import TagInfo from "./TagInfo";
import MessageInfo from "./MessageInfo";

class Admin extends Component {

    constructor(props) {
        super(props);
    }

    componentDidMount() {
        document.title = 'Админка'
    }

    render() {
        return (
            <div>
                <MessageInfo/>
                <TagInfo tags={this.props.tags}/>
            </div>
        )
    }
}

export default Admin;