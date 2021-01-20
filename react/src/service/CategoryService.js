import {BASE_API} from "../util/Constants";
import {hateoasRequest} from "../util/APIUtils";

export function getCategories() {
    return hateoasRequest(`${BASE_API}/categories`)
}
