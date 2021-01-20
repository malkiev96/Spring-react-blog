import {BASE_API} from "../util/Constants";
import {hateoasRequest, jsonRequest, request} from "../util/APIUtils";

export function addComment(postId, parentId = 0, message) {
    const url = parentId !== 0
        ? `${BASE_API}/comments/post/${postId}/reply/${parentId}`
        : `${BASE_API}/comments/post/${postId}`;
    const options = {
        method: 'POST',
        url: url,
        body: message
    }
    return jsonRequest(options)
}

export function getComments(postId) {
    return hateoasRequest(`${BASE_API}/comments/post/${postId}`)
}

export function deleteComment(id) {
    const options = {
        method: 'DELETE',
        url: `${BASE_API}/comments/${id}`
    }
    return request(options)
}