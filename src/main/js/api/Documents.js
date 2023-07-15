import {BASE_API} from "../util/Constants";

export function saveTempFile(files) {
  const headers = new Headers({
    Accept: 'application/x-spring-data-verbose+json',
    Authorization: 'Bearer ' + localStorage.getItem('accessToken')
  })
  const formData = new FormData();
  files.forEach(file => {
    formData.append('file', file)
  })
  const options = {
    method: 'POST', headers: headers, body: formData
  };
  return fetch(`${BASE_API}/files`, options).then(response => {
        return response.json().then(fileId => {
          if (!response.ok) {
            return Promise.reject(fileId);
          }
          return fileId;
        });
      }
  )
}

export function getDocumentSrc(fileId) {
  return `${BASE_API}/files/download?fileId=${fileId}`
}