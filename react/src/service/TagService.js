import {BASE_API} from "../util/Constants";
import {hateoasRequest} from "../util/APIUtils";

export function getTags() {
    return hateoasRequest(`${BASE_API}/tags`)
}
