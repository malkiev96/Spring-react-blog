import {BASE_API} from "../util/Constants";
import {getRequest, jsonRequest, request} from "../util/APIUtils";

export function getTags() {
  return getRequest(`${BASE_API}/tags`)
}

export function createTag(code, name, tagId = null) {
  return jsonRequest({
    url: `${BASE_API}/tags`,
    method: "POST",
    body: JSON.stringify({code, name, tagId})
  })
}

export function deleteTag(id) {
  const options = {
    method: 'DELETE',
    url: `${BASE_API}/tags/${id}`
  }
  return request(options)
}
