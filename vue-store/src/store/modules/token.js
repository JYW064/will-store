/*
 * @Description: 用户登录状态模块
 * @Author: hai-27
 * @Date: 2020-02-19 17:42:11
 * @LastEditors: hai-27
 * @LastEditTime: 2020-02-26 23:14:32
 */
export default {
    state: {
        // 存储token
        token: localStorage.getItem('token') ? localStorage.getItem('token') : ''
    },
    getters: {
        getToken (state) {
            return state
        },
    },
    mutations: {
        setToken (state, data) {
            state.token = data;
            localStorage.setItem('token', data);
        }
    },
    actions: {
        setToken ({ commit }, data) {
            commit('setToken', data);
        },
    }
}
