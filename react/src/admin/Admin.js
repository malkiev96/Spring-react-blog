import React, {Component} from 'react';
import TagInfo from "./TagInfo";
import MessageInfo from "./MessageInfo";
import CategoryInfo from "./CategoryInfo";

class Admin extends Component {

    componentDidMount() {
        document.title = 'Админка'
    }

    render() {
        return (
            <div>
                <MessageInfo/>
                <TagInfo tags={this.props.tags}/>
                <CategoryInfo/>
            </div>
        )
    }
}

export default Admin;