import {BASE_API} from "../util/Constants";
import {request} from "../util/APIUtils";

export const FILE_TYPE = 'FILE'
export const IMAGE_TYPE = 'IMAGE'
export const ZIP_FILE = 'ZIP'

export function saveDocument(files, type = FILE_TYPE) {
    const headers = new Headers({
        Accept: 'application/x-spring-data-verbose+json',
        Authorization: 'Bearer ' + localStorage.getItem('accessToken')
    })
    const formData = new FormData();
    formData.append('type', type)
    files.forEach(file => {
        formData.append('file', file)
    })
    const options = {
        method: 'POST', headers: headers, body: formData
    };
    return fetch(`${BASE_API}/documents`, options).then(response =>
        response.json().then(json => {
            if (!response.ok) {
                return Promise.reject(json);
            }
            return json;
        })
    )
}

export function deleteDocument(id) {
    const options = {
        method: 'DELETE',
        url: `${BASE_API}/documents/${id}`
    }
    return request(options)
}

export function getDocumentSrc(id) {
    return `${BASE_API}/documents/${id}/download`
}