/**axios封装
 * 请求拦截、相应拦截、错误统一处理
 */
import axios from "axios";
import { Local } from "@/utils/storage";
import { Modal, message } from "ant-design-vue";
import { postAction } from "@/request/manage";

const service = axios.create({
    baseURL: process.env.VUE_APP_API_BASE_URL,
    // baseURL: "http://120.76.216.29:8089/tuma-boot",
    timeout: 30000000
})
// 请求拦截器
service.interceptors.request.use(
    (config) => {
        config.headers["X-Access-Token"] = Local.get("token")
        return config
    },
    (error) => {
        switch (error.code) {
            default:
                message.warning(error.message)
        }
        return Promise.reject(error)
    }
)

// 响应拦截器
service.interceptors.response.use(
    (response) => {
        // console.log('响应response', response)
        const data = response.data
        if (response.status === 200) {
            switch (data.code) {
                case 0:
                    return Promise.resolve(data)
                case 200:
                    return Promise.resolve(data)
                default:
                    message.warning(data.message)
                    return Promise.reject(data)
            }
        } else {
            message.warning(data.message)
            return Promise.reject(data)
        }
    },
    // 服务器状态码不是200的情况
    (error) => {
        // console.log('响应error', error, '响应error response', error.response)
        const response = error.response;
        switch (response.status) {
            case 500:
                if (response.data.message.includes("Token失效")) {
                    Modal.confirm({
                        content: '未登录',
                        okText: '去登录',
                        cancelText: '取消',
                        mask: true,
                        onCancel: () => {
                        },
                        onOk: () => {
                            if (Local.get("token")) {
                                postAction("/sys/logout", {}).then(() => {
                                    Local.remove("token");
                                    Local.remove("userInfo");
                                    window.location.href = '/login'
                                })
                            } else {
                                window.location.href = '/login'
                            }
                        }
                    })
                }
                break;
            default:
                message.warning(response.data.message)
                return Promise.reject(response.data)
        }
    }
)

export { service }
