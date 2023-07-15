import {ACCESS_TOKEN, BASE_API} from "../util/Constants";
import {getRequest, jsonRequest} from "../util/APIUtils";

export function getUser(id) {
  return getRequest(BASE_API + "/users/" + id)
}

export function getCurrentUser() {
  if (!localStorage.getItem(ACCESS_TOKEN)) {
    return Promise.reject("No access token set.");
  }
  return jsonRequest({
    url: BASE_API + "/auth/user/me",
    method: 'GET'
  });
}

export function getAllUsers() {
  return getRequest(BASE_API + "/users")
}

export function getAdmins() {
  return getRequest(BASE_API + "/users/admins")
}

export function sendContactMessage(msg) {
  return jsonRequest({
    url: `${BASE_API}/contacts`,
    method: "POST",
    body: JSON.stringify(msg)
  })
}

export function login(loginRequest) {
  return jsonRequest({
    url: `${BASE_API}/auth/login`,
    method: 'POST',
    body: JSON.stringify(loginRequest)
  });
}

export function signup(signupRequest) {
  return jsonRequest({
    url: `${BASE_API}/auth/signup`,
    method: 'POST',
    body: JSON.stringify(signupRequest)
  });
}

export function updateUser(id, userRequest) {
  return jsonRequest({
    url: `${BASE_API}/users/${id}`,
    method: 'POST',
    body: JSON.stringify(userRequest)
  })
}