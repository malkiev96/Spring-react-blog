import {BASE_API} from "../util/Constants";
import {hateoasRequest, jsonRequest, request} from "../util/APIUtils";

export function getCategories() {
    return hateoasRequest(`${BASE_API}/categories`)
}

export function deleteCategory(id) {
    const options = {
        method: 'DELETE',
        url: `${BASE_API}/categories/${id}`
    }
    return request(options)
}

export function createCategory(code, name, parentId = null) {
    return jsonRequest({
        url: `${BASE_API}/categories`,
        method: "POST",
        body: JSON.stringify({code, name, parentId})
    })
}

export function addChilds(id, childIds = []) {
    return jsonRequest({
        url: `${BASE_API}/categories/${id}/childs?childs?=${childIds}`,
        method: "POST"
    })
}