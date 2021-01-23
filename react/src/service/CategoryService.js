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

export function createOrEditCategory(code, name, parentId = null, categoryId = null) {
    return jsonRequest({
        url: `${BASE_API}/categories`,
        method: "POST",
        body: JSON.stringify({code, name, parentId, categoryId})
    })
}