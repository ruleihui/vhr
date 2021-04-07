import axios from "axios";
import {Message} from "element-ui";
import router from "@/router";
import store from '@/store'

let message = Message;
let messages = {
    404: '服务端不存在',
    504: '服务端不存在',
    500: '服务器内部错误/已关闭',
    403: '权限不足,请联系管理员',
    401: '尚未登录,请登录',
}
//拦截响应
axios.interceptors.response.use(success => {
    //200,的回调处理,参数类型是AxiosResponse,其中有个data属性才是server return的json
    //如果出错弹出错误提示,并返回空值
    if (success.data && success.data.status == 500) {
        message.error(success.data.msg);
        return;
    } else if (success.data.status == 401) {
        message.error(success.data.msg);
        store.dispatch('reset')
        vue.$router.matcher = vue.restRouter().matcher;
        router.replace('/')
        return;
    }
    if (success.data && success.data.msg) message.info(success.data.msg)
    return success.data.obj;
}, error => {
    //400/500的回调处理,参数类型是AxiosError(?)
    let status, data
    if (error.response) {
        status = error.response.status
        data = error.response.data
    }
    let msg = messages[status]
    if (status == 500) {
        store.dispatch('reset')
        vue.$router.matcher = vue.restRouter().matcher;
        router.replace('/')
    }
    if (msg) message.error(msg)
    else message.error('未知错误')
    return;
});

let base = ''

let formPost = (uri, params) => {
    uri = base + uri;
    return axios({
        url: uri,
        method: 'post',
        data: params,
        transformRequest: (data, headers) => {
            let parse = '';
            Object.keys(data).forEach(
                it => {
                    parse += it + '=' + data[it] + '&';
                }
            )
            parse = parse.substr(0, parse.lastIndexOf('&'))
            return encodeURI(parse);
        },
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
    })
}
//post/put/patch
export const executeArgs = (type, uri, params, config) => {
    uri = base + uri;
    return axios[type](uri, params, config);
}
//get/delete/head/options
export const execute = (type, uri, config) => {
    uri = base + uri;
    return axios[type](uri, config);
}
const install = function (vue, opts = {}) {
    base = opts.prefix ? opts.prefix : '';
    vue.prototype.$axios = axios;
    let short=vue.prototype.$axios;
    short.formPost = formPost;
    short.executeArgs = executeArgs;
    short.execute = execute;
    console.log("axios install success")
}
//axios的catch块执行.在then逻辑报错时,取消请求，超时，以及状态码 > 300 都会进入catch块
//代码参考node_modules/axios/lib/core/settle.js
//node_modules/axios/lib/adapters/xhr.js

export default {
    install,
}