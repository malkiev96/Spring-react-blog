export const ADMIN_ID = 1;
export const ADMIN_NAME = 'Leonid';
export const ACCESS_TOKEN = 'accessToken';
export const SORT_TYPE = 'sortType';
export const SORT_DATE = 'createdDate,desc';

export const BASE_API = 'http://localhost:8080';
export const HOST = 'http://localhost:8080';
export const OAUTH2_REDIRECT_URI = 'http://localhost:3000/oauth2/redirect'
export const GOOGLE_AUTH_URL = HOST + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const FACEBOOK_AUTH_URL = HOST + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const GITHUB_AUTH_URL = HOST + '/oauth2/authorize/github?redirect_uri=' + OAUTH2_REDIRECT_URI;
