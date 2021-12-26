import {ACCESS_TOKEN} from './Constants';

export const jsonRequest = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    })
    if (localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }
    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);
    return fetch(options.url, options).then(response =>
        response.json().then(json => !response.ok ? Promise.reject(json) : json)
    )
}

export const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    })
    if (localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }
    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);
    return fetch(options.url, options).then(response => !response.ok ? Promise.reject(response) : response)
}

export const hateoasRequest = (url) => {
    const headers = new Headers({
        Accept: 'application/x-spring-data-verbose+json'
    })
    if (localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }
    return fetch(url, {headers: headers, method: 'GET'}).then(response =>
        response.json().then(json => !response.ok ? Promise.reject(json) : json)
    )
}