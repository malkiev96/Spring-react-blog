import {API_BASE_URL, ACCESS_TOKEN} from '../constants';

const request = (options) => {
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

export function getComments(postId) {
    return hateoasRequest(API_BASE_URL+"/comments/post/"+postId)
}

export function getFilters() {
    return hateoasRequest(API_BASE_URL+"/filters")
}

export function getPostById(id) {
    return hateoasRequest(API_BASE_URL+"/posts/"+id)
}

export function getPosts(page = 1, size = 10, sort = "createdDate") {
    page-=1
    const url = API_BASE_URL + "/posts?page=" + page + "&size=" + size + "&sort=" + sort
    return hateoasRequest(url)
}

export function getPostsByTagIds(ids, page = 1, size = 10, sort = "createdDate") {
    page-=1
    const url = API_BASE_URL + "/posts/tag?ids=" + ids + "&page=" + page + "&size=" + size + "&sort=" + sort
    return hateoasRequest(url)
}


export function getPostsByCategory(ids, page = 1, size = 10, sort = "createdDate") {
    page-=1
    const url = API_BASE_URL + "/posts/category?ids=" + ids + "&page=" + page + "&size=" + size + "&sort=" + sort
    return hateoasRequest(url)
}

export function getCurrentUser() {
    if (!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/user/me",
        method: 'GET'
    });
}

export function login(loginRequest) {
    return request({
        url: API_BASE_URL + "/auth/login",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function signup(signupRequest) {
    return request({
        url: API_BASE_URL + "/auth/signup",
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}