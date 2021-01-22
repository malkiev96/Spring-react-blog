import {BASE_API} from "../util/Constants";
import {hateoasRequest, jsonRequest, request} from "../util/APIUtils";

export function getTags() {
    return hateoasRequest(`${BASE_API}/tags`)
}

export function createTag(code, name) {
    return jsonRequest({
        url: `${BASE_API}/tags`,
        method: "POST",
        body: JSON.stringify({code, name})
    })
}

export function deleteTag(id) {
    const options = {
        method: 'DELETE',
        url: `${BASE_API}/tags/${id}`
    }
    return request(options)
}
