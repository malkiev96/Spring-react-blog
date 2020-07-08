import React from 'react'
import {Comment, Message} from 'semantic-ui-react'
import {locale} from "moment/locale/ru";
import moment from "moment";

const Comments = (props) => {
    const {comments} = props.comments
    if (comments.length === 0) return <Message>Комментариев нет</Message>

    return (
        <Comment.Group>
            {
                comments.map(comment => {
                    return <CommentItem comment={comment} key={comment.id}/>
                })
            }
        </Comment.Group>
    )
}

const CommentItem = (props) => {
    const {comment} = props;
    const childs = comment.childs['content']
    const createdDate = moment(comment.auditor.createdDate, "DD-MM-YYYY hh:mm", locale).fromNow()
    return (
        <Comment>
            {
                comment.auditor.createdBy.imageUrl &&
                <Comment.Avatar src={comment.auditor.createdBy.imageUrl}/>
            }
            <Comment.Content>
                <Comment.Author as='a'>{comment.auditor.createdBy.name}</Comment.Author>
                <Comment.Metadata>
                    <div>{createdDate}</div>
                </Comment.Metadata>
                <Comment.Text>{comment.message}</Comment.Text>
                <Comment.Actions>
                    <Comment.Action>Reply</Comment.Action>
                </Comment.Actions>
            </Comment.Content>
            {
                childs.length !== 0 &&
                <Comment.Group>
                    {
                        childs.map(c => <CommentItem comment={c}/>)
                    }
                </Comment.Group>
            }
        </Comment>
    )
}

export default Comments