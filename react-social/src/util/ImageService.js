import {HOST} from "../constants";

export function saveImages(files) {
    const headers = new Headers({
        Accept: 'application/x-spring-data-verbose+json',
        Authorization: 'Bearer ' + localStorage.getItem('accessToken')
    })
    const formData = new FormData();
    files.forEach(file => {
        formData.append('files', file)
    })
    const options = {
        method: 'POST',
        headers: headers,
        body: formData
    };
    return fetch(HOST + '/images', options).then(response =>
        response.json().then(json => {
            if (!response.ok) {
                return Promise.reject(json);
            }
            return json;
        })
    )
}