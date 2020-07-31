import {ACCESS_TOKEN, API_BASE_URL} from '../constants';

const jsonRequest = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    })
    if (localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }
    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);
    return fetch(options.url, options).then(response =>
        response.json().then(json => {
            if (!response.ok) {
                return Promise.reject(json);
            }
            return json;
        })
    )
}

const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    })
    if (localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }
    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);
    return fetch(options.url, options).then(response => {
        if (!response.ok) {
            return Promise.reject(response);
        }
        return response
    })
}

const hateoasRequest = (url) => {
    console.log(url)
    const headers = new Headers({
        Accept: 'application/x-spring-data-verbose+json'
    })
    if (localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }
    return fetch(url, {headers: headers, method: 'GET'}).then(response =>
        response.json().then(json => {
            if (!response.ok) {
                return Promise.reject(json)
            }
            return json
        })
    )
}

export function deleteComment(id) {
    const options = {
        method: 'DELETE',
        url: API_BASE_URL + '/comments/' + id
    }
    return request(options)
}

export function addComment(postId, parentId = 0, message) {
    let url;
    if (parentId !== 0) {
        url = API_BASE_URL + '/comments/post/' + postId + "/reply/" + parentId
    } else {
        url = API_BASE_URL + '/comments/post/' + postId
    }
    const options = {
        method: 'POST',
        url: url,
        body: message
    }
    return jsonRequest(options)
}

export function getComments(postId) {
    return hateoasRequest(API_BASE_URL + "/comments/post/" + postId)
}

export function getFilters() {
    return hateoasRequest(API_BASE_URL + "/filters")
}

export function getPostById(id) {
    return hateoasRequest(API_BASE_URL + "/posts/" + id)
}

export function getUser(id) {
    return hateoasRequest(API_BASE_URL + "/users/" + id)
}

export function getPostsByUser(userId, page = 1, size = 10, sort = "createdDate") {
    page -= 1
    const url = API_BASE_URL + "/posts?userId=" + userId + "&page=" + page + "&size=" + size + "&sort=" + sort
    return hateoasRequest(url)
}

export function getPostsByUserAndStatus(userId, status = 'PUBLISHED', page = 1, size = 10, sort = "createdDate") {
    page -= 1
    const url = API_BASE_URL + "/posts?userId=" + userId + "&statuses=" + status + "&page=" + page + "&size=" + size + "&sort=" + sort
    return hateoasRequest(url)
}

export function getPostsByStatus(status = 'PUBLISHED', page = 1, size = 10, sort = "createdDate") {
    page -= 1
    const url = API_BASE_URL + "/posts?statuses=" + status + "&page=" + page + "&size=" + size + "&sort=" + sort
    return hateoasRequest(url)
}

export function getPosts(page = 1, size = 10, sort = "createdDate") {
    page -= 1
    const url = API_BASE_URL + "/posts?page=" + page + "&size=" + size + "&sort=" + sort
    return hateoasRequest(url)
}

export function getPostsByTagIds(ids, page = 1, size = 10, sort = "createdDate") {
    page -= 1
    const url = API_BASE_URL + "/posts?tagIds=" + ids + "&page=" + page + "&size=" + size + "&sort=" + sort
    return hateoasRequest(url)
}


export function getPostsByCategory(ids, page = 1, size = 10, sort = "createdDate") {
    page -= 1
    const url = API_BASE_URL + "/posts?catIds=" + ids + "&page=" + page + "&size=" + size + "&sort=" + sort
    return hateoasRequest(url)
}

export function saveImages(files) {
    const headers = new Headers({
        Accept: 'application/x-spring-data-verbose+json',
        Authorization: 'Bearer ' + localStorage.getItem('accessToken')
    })
    const formData = new FormData();
    files.map(file => {
        formData.append('files', file)
    })
    const options = {
        method: 'POST',
        headers: headers,
        body: formData
    };
    return fetch(API_BASE_URL + '/images', options).then(response =>
        response.json().then(json => {
            if (!response.ok) {
                return Promise.reject(json);
            }
            return json;
        })
    )
}

export function createPost(postRequest) {
    return jsonRequest({
        url: API_BASE_URL + "/posts",
        method: "POST",
        body: JSON.stringify(postRequest)
    })
}

export function getCurrentUser() {
    if (!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return jsonRequest({
        url: API_BASE_URL + "/user/me",
        method: 'GET'
    });
}

export function login(loginRequest) {
    return jsonRequest({
        url: API_BASE_URL + "/auth/login",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function signup(signupRequest) {
    return jsonRequest({
        url: API_BASE_URL + "/auth/signup",
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}