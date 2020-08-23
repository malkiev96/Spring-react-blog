import {HOST} from "../util/Constants";
import {hateoasRequest, jsonRequest, request} from "../util/APIUtils";

export function addComment(postId, parentId = 0, message) {
    const url = parentId !== 0
        ? HOST + '/comments/post/' + postId + "/reply/" + parentId
        : HOST + '/comments/post/' + postId;
    const options = {
        method: 'POST',
        url: url,
        body: message
    }
    return jsonRequest(options)
}

export function getComments(postId) {
    return hateoasRequest(HOST + "/comments/post/" + postId)
}

export function deleteComment(id) {
    const options = {
        method: 'DELETE',
        url: HOST + '/comments/' + id
    }
    return request(options)
}