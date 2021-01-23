import {BASE_API} from "../util/Constants";
import {hateoasRequest, jsonRequest, request} from "../util/APIUtils";

export function getPostById(id) {
    return hateoasRequest(BASE_API + "/posts/" + id)
}

export function getPosts(page = 1,
                         size = 10,
                         sort = "createdDate",
                         tagCodes,
                         catIds,
                         statuses = 'PUBLISHED',
                         userId,
                         liked) {
    page -= 1
    let url = `${BASE_API}/posts?page=${page}&size=${size}&sort=${sort}&statuses=${statuses}`
    if (tagCodes) url += `&tagCodes=${tagCodes}`
    if (catIds) url += `&catIds=${catIds}`
    if (userId) url += `&userId=${userId}`
    if (liked) url += "&liked=true"
    return hateoasRequest(url)
}

export function addStar(postId, star) {
    return hateoasRequest(`${BASE_API}/posts/${postId}/rating?star=${star}`)
}

export function deleteStar(postId) {
    return request({
        url: `${BASE_API}/posts/${postId}/rating`, method: "DELETE"
    })
}

export function getPostsByUrl(url) {
    return hateoasRequest(url)
}

export function createPost(postRequest) {
    return jsonRequest({
        url: `${BASE_API}/posts`, method: "POST", body: JSON.stringify(postRequest)
    })
}

export function likePost(id) {
    return hateoasRequest(`${BASE_API}/posts/${id}/like`)
}

export function publishPost(id) {
    return hateoasRequest(`${BASE_API}/posts/${id}/publish`)
}

export function hidePost(id) {
    return hateoasRequest(`${BASE_API}/posts/${id}/hide`)
}

export function deletePost(id) {
    return hateoasRequest(`${BASE_API}/posts/${id}/delete`)
}