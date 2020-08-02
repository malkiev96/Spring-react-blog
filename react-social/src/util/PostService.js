import {HOST} from "../constants";
import {hateoasRequest, jsonRequest} from "./APIUtils";

export function getPostById(id) {
    return hateoasRequest(HOST + "/posts/" + id)
}

export function getPostsByUserAndStatus(userId, status = 'PUBLISHED', page = 1, size = 10, sort = "createdDate") {
    page -= 1
    const url = HOST + "/posts?userId=" + userId + "&statuses=" + status + "&page=" + page + "&size=" + size + "&sort=" + sort
    return hateoasRequest(url)
}

export function getPostsByStatus(status = 'PUBLISHED', page = 1, size = 10, sort = "createdDate") {
    page -= 1
    const url = HOST + "/posts?statuses=" + status + "&page=" + page + "&size=" + size + "&sort=" + sort
    return hateoasRequest(url)
}

export function getPosts(page = 1, size = 10, sort = "createdDate") {
    page -= 1
    const url = HOST + "/posts?page=" + page + "&size=" + size + "&sort=" + sort
    return hateoasRequest(url)
}

export function getPostsByTagIds(ids, page = 1, size = 10, sort = "createdDate") {
    page -= 1
    const url = HOST + "/posts?tagIds=" + ids + "&page=" + page + "&size=" + size + "&sort=" + sort
    return hateoasRequest(url)
}


export function getPostsByCategory(ids, page = 1, size = 10, sort = "createdDate") {
    page -= 1
    const url = HOST + "/posts?catIds=" + ids + "&page=" + page + "&size=" + size + "&sort=" + sort
    return hateoasRequest(url)
}

export function getPostsByUrl(url) {
    return hateoasRequest(url)
}

export function createPost(postRequest) {
    return jsonRequest({
        url: HOST + "/posts",
        method: "POST",
        body: JSON.stringify(postRequest)
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