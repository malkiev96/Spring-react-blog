export const ADMIN_ID = 1
export const ADMIN_NAME = 'Leonid'
export const ACCESS_TOKEN = 'accessToken'
export const SORT_TYPE = 'sortType'
export const SORT_DATE = 'createdDate,desc'

export const BASE_API = process.env.REACT_APP_BACK_HOST
export const OAUTH2_REDIRECT_URI = `${process.env.REACT_APP_FRONT_HOST}/oauth2/redirect`
export const GOOGLE_AUTH_URL = `${BASE_API}/oauth2/authorize/google?redirect_uri=${OAUTH2_REDIRECT_URI}`
export const GITHUB_AUTH_URL = `${BASE_API}/oauth2/authorize/github?redirect_uri=${OAUTH2_REDIRECT_URI}`
