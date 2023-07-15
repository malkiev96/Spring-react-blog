import {BASE_API} from "../util/Constants";
import {getRequest, jsonRequest, request} from "../util/APIUtils";

export function getCategories() {
  return getRequest(`${BASE_API}/categories`)
}

export function getAllCategories() {
  return getRequest(`${BASE_API}/categories/all`)
}

export function deleteCategory(id) {
  const options = {
    method: 'DELETE',
    url: `${BASE_API}/categories/${id}`
  }
  return request(options)
}

export function createOrEditCategory(code, name, categoryId = null,
    parentId = null) {
  return jsonRequest({
    url: `${BASE_API}/categories`,
    method: "POST",
    body: JSON.stringify({code, name, categoryId, parentId})
  })
}