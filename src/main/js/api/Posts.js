import {BASE_API} from "../util/Constants";
import {getRequest, jsonRequest, request} from "../util/APIUtils";

export function getPostById(id) {
  return getRequest(BASE_API + "/posts/" + id)
}

export function getPosts(page = 1,
    size = 10,
    sort = "createdDate",
    tagCodes,
    catIds,
    statuses = 'PUBLISHED',
    userId) {
  page -= 1
  let url = `${BASE_API}/posts?page=${page}&size=${size}&sort=${sort}&statuses=${statuses}`
  if (tagCodes) {
    url += `&tagCodes=${tagCodes}`
  }
  if (catIds) {
    url += `&catIds=${catIds}`
  }
  if (userId) {
    url += `&userId=${userId}`
  }
  return getRequest(url)
}

export function getPostsByUrl(url) {
  return getRequest(url)
}

export function createPost(postRequest) {
  return jsonRequest({
    url: `${BASE_API}/posts`, method: "POST", body: JSON.stringify(postRequest)
  })
}

export function publishPost(id) {
  return request({
    method: 'POST',
    url: `${BASE_API}/posts/${id}/publish`
  })
}

export function hidePost(id) {
  return request({
    method: 'POST',
    url: `${BASE_API}/posts/${id}/hide`
  })
}

export function deletePost(id) {
  return request({
    method: 'POST',
    url: `${BASE_API}/posts/${id}/delete`
  })
}

export function blockPost(id) {
  return request({
    method: 'POST',
    url: `${BASE_API}/posts/${id}/block`
  })
}