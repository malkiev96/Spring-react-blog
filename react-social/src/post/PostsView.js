import React, {Component} from 'react';
import moment from "moment";
import {locale} from "moment/locale/ru";
import {Icon, Item, Message, Segment} from "semantic-ui-react";
import {Link} from "react-router-dom";

class PostsView extends Component {

    render() {
        const {posts} = this.props
        if (posts.posts.length === 0) return <Message>Статей нет</Message>
        return (
            <div>{posts.posts.map(post => <PostsRow post={post}/>)}</div>
        )
    }
}


const PostsRow = ({post}) => {
    const createdDate = moment(post.auditor.createdDate, "DD-MM-YYYY hh:mm", locale).fromNow()
    return (
        <Segment key={post.id}>
            <Item.Group>
                <Item>
                    {
                        post.preview !== null &&
                        <Item.Image as={Link} to={'/posts/' + post.id} src={post.preview.url}/>
                    }
                    <Item.Content>
                        <Item.Header as={Link} to={'/posts/' + post.id}>{post.title}</Item.Header>
                        <Item.Meta>
                            <Link to={'/category/' + post.category.name}>
                                <Icon name='folder'/>
                                {' ' + post.category.name}
                            </Link>
                        </Item.Meta>
                        <Item.Meta>
                            <Link to={'/user/' + post.auditor.createdBy.id}>
                                <Icon name='user'/>
                                {' ' + post.auditor.createdBy.name}
                            </Link>
                            {' ' + createdDate}
                        </Item.Meta>
                        <Item.Meta>{post.status}</Item.Meta>
                        <Item.Description>{post.description}</Item.Description>
                    </Item.Content>
                </Item>

            </Item.Group>
        </Segment>
    )
}

export default PostsView;