import {ACCESS_TOKEN, HOST} from "../util/Constants";
import {hateoasRequest, jsonRequest} from "../util/APIUtils";

export function getUser(id) {
    return hateoasRequest(HOST + "/users/" + id)
}

export function getCurrentUser() {
    if (!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }
    return jsonRequest({
        url: HOST + "/user/me",
        method: 'GET'
    });
}

export function getAdmins() {
    return hateoasRequest(HOST + "/users/admins")
}

export function sendContactMessage(msg) {
    return jsonRequest({
        url: HOST + "/contacts",
        method: "POST",
        body: JSON.stringify(msg)
    })
}

export function login(loginRequest) {
    return jsonRequest({
        url: HOST + "/auth/login",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function signup(signupRequest) {
    return jsonRequest({
        url: HOST + "/auth/signup",
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}

export function updateUser(id, userRequest) {
    return jsonRequest({
        url: HOST + '/users/' + id,
        method: 'POST',
        body: JSON.stringify(userRequest)
    })
}