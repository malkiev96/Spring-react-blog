import React, {Component} from 'react'
import {getPosts} from '../util/PostService'
import {Button, Header, List, Segment} from "semantic-ui-react";
import {Link} from "react-router-dom";

class RightMenu extends Component {
    constructor(props) {
        super(props);
        this.state = {
            posts: {
                posts: [],
                loading: true
            }
        }
        this.loadPosts = this.loadPosts.bind(this)
    }

    componentDidMount() {
        this.loadPosts()
    }

    loadPosts() {
        getPosts(1, 5, 'postRatings.star,desc').then(result => {
            this.setState({
                posts: {
                    posts: result['content'],
                    loading: false
                }
            })
        }).catch(error => {
            this.setState({
                posts: {
                    posts: [],
                    loading: false
                }
            })
        })
    }

    render() {
        const {tags} = this.props
        const {posts, loading} = this.state.posts
        return (
            <div>
                <Segment loading={loading}>
                    <Header>Популярное</Header>
                    <List divided relaxed>
                        {
                            posts.map(post => (
                                <List.Item key={post.id}>
                                    <List.Content>
                                        <List.Header as={Link} to={'/post/' + post.id}>
                                            <Header as='h4'> {post.title}</Header>
                                        </List.Header>
                                    </List.Content>
                                </List.Item>
                            ))
                        }
                    </List>
                </Segment>
                <Segment>
                    <Header>Теги</Header>
                    <List size='mini' selection horizontal>
                        {
                            tags.map((tag, i) => (
                                <List.Item key={i}>
                                    <Button style={{margin: '0', backgroundColor: '#175e6b'}}
                                            compact active size='mini'
                                            content={tag.name}
                                            as={Link}
                                            to={'/tags/' + tag.name}
                                            primary={window.location.href.includes(tag.name)}
                                            basic={!window.location.href.includes(tag.name)}
                                    />
                                </List.Item>
                            ))
                        }
                    </List>
                </Segment>
            </div>
        )
    }
}

export default RightMenu