import { service } from './http';

export function getAction(url, params) {
    return service({
        url,
        method: 'get',
        params: params
    })
}

export function postAction(url, params) {
    return service({
        url,
        method: 'post',
        data: params
    })
}

export function putAction(url, params) {
    return service({
        url,
        method: 'put',
        data: params
    })
}

export function deleteAction(url, params) {
    return service({
        url,
        method: 'delete',
        data: params
    })
}

export function httpAction(url, params, method, headers) {
    return service({
        url,
        method,
        data: params,
        headers
    })
}

export function formDataAction(url, data) {
    return service({
        url,
        method: 'post',
        headers: {
            'content-type': 'multipart/form-data'
        },
        data,
    })
}
