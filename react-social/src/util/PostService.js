import {HOST} from "../constants";
import {hateoasRequest, jsonRequest} from "./APIUtils";

export function getPostById(id) {
    return hateoasRequest(HOST + "/posts/" + id)
}

export function getPosts(page = 1,
                         size = 10,
                         sort = "createdDate",
                         tagNames,
                         catIds,
                         statuses = 'PUBLISHED',
                         userId) {
    page -= 1
    let url = HOST + "/posts?page=" + page + "&size=" + size + "&sort=" + sort + "&statuses=" + statuses
    if (tagNames) url += "&tagNames=" + tagNames
    if (catIds) url += "&catIds=" + catIds
    if (userId) url += "&userId=" + userId
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

export function publishPost(id) {
    return hateoasRequest(HOST + "/posts/" + id + "/publish")
}

export function hidePost(id) {
    return hateoasRequest(HOST + "/posts/" + id + "/hide")
}

export function deletePost(id) {
    return hateoasRequest(HOST + "/posts/" + id + "/delete")
}