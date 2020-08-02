import {HOST} from "../constants";
import {hateoasRequest, jsonRequest, request} from "./APIUtils";

export function addComment(postId, parentId = 0, message) {
    let url;
    if (parentId !== 0) {
        url = HOST + '/comments/post/' + postId + "/reply/" + parentId
    } else {
        url = HOST + '/comments/post/' + postId
    }
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