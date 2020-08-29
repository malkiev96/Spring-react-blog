import {HOST} from "../util/Constants";
import {hateoasRequest, request} from "../util/APIUtils";

export function getMessages(page = 1,
                            size = 10,
                            sort = "createdDate,desc") {
    page -= 1
    const url = HOST + "/contacts?page=" + page + "&size=" + size + "&sort=" + sort
    return hateoasRequest(url)
}

export function deleteMessage(id) {
    return request({
        method: 'POST',
        url: HOST + '/contacts/' + id + '/delete'
    })
}