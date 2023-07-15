import React, {Component} from 'react';
import moment from "moment";
import {locale} from "moment/locale/ru";
import {Divider, Header, Icon, Image, Segment} from "semantic-ui-react";
import {Link} from "react-router-dom";
import './posts.css'
import {getDocumentSrc} from "../../api/Documents";

class PostsItem extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const {post} = this.props
    const createdDate = moment(post?.createdDate, "DD.MM.YYYY hh:mm",
        locale).fromNow()

    return (
        <Segment key={post.id}>
          <Header size="large" as="h2">
            <Header.Content style={{paddingBottom: '10px'}}>
              <Link to={'/post/' + post.id}>{post.title}</Link>
            </Header.Content>
            <Header.Subheader>
              <Link to={'/user/' + post?.createdBy?.id}>
                {' ' + post?.createdBy?.name}
              </Link>
              {' ' + createdDate}
            </Header.Subheader>
          </Header>
          {
              post.preview !== null &&
              <Image size={"large"} src={getDocumentSrc(post.preview.fileId)}/>
          }
          <Divider hidden/>
          <p>{post.description}</p>
          <div>
            <span style={{float: 'right'}}>
                         <Icon color='grey' name='eye'/>
                         <span>{post.viewCount}</span>
                    </span>
          </div>
        </Segment>
    )
  }
}

export default PostsItem;