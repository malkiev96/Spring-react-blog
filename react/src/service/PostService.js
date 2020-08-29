import {HOST} from "../util/Constants";
import {hateoasRequest, jsonRequest} from "../util/APIUtils";

export function getPostById(id) {
    return hateoasRequest(HOST + "/posts/" + id)
}

export function getPosts(page = 1,
                         size = 10,
                         sort = "createdDate",
                         tagNames,
                         catIds,
                         statuses = 'PUBLISHED',
                         userId,
                         liked) {
    page -= 1
    let url = HOST + "/posts?page=" + page + "&size=" + size + "&sort=" + sort + "&statuses=" + statuses
    if (tagNames) url += "&tagNames=" + tagNames
    if (catIds) url += "&catIds=" + catIds
    if (userId) url += "&userId=" + userId
    if (liked) url += "&liked=true"
    return hateoasRequest(url)
}

export function addStar(postId, star) {
    return hateoasRequest(HOST + "/posts/" + postId + "/rating?star=" + star)
}

export function getPostsByUrl(url) {
    return hateoasRequest(url)
}

export function createPost(postRequest) {
    return jsonRequest({
        url: HOST + "/posts", method: "POST", body: JSON.stringify(postRequest)
    })
}

export function likePost(id) {
    return hateoasRequest(HOST + "/posts/" + id + "/like")
}

export function publishPost(id) {
    return hateoasRequest(HOST + "/posts/" + id + "/publish")
}

export function hidePost(id) {
    return hateoasRequest(HOST + "/posts/" + id + "/hide")
}

export function deletePost(id) {
    return hateoasRequest(HOST + "/posts/" + id + "/delete")
}