import Vue from 'vue';
import Vuex from 'vuex';
import {default as router, initElse, initMenu, createRouter} from "@/router";
import chat from "@/store/chat";

Vue.use(Vuex)
const store = new Vuex.Store({
    state() {
        return {
            routes: [],
            user: null,
        }
    },
    mutations: {
        routes: function (state, payload) {
            state.routes = payload
        },
        user: function (state, payload) {
            window.sessionStorage.setItem('user', JSON.stringify(payload))
            state.user = payload
        },
    },
    getters: {
        routes: state => state.routes,
        user: state => state.user,
    },
    actions: {
        init({dispatch}) {
            dispatch('initUser')
            dispatch('initRoutes')
        },
        initUser({dispatch}) {
            let user = window.sessionStorage.getItem('user')
            if (user) user = JSON.parse(user);
            dispatch('setUser', user)
        },
        async login({dispatch}, {node, loginForm}) {
            await vue.$axios.formPost('/doLogin', loginForm).then(resp => {
                //只处理ture,拦截器已经处理了错误的情况
                if (resp) {
                    //push可回退,replace不可回退
                    vue.$store.dispatch('setUser', resp)
                    dispatch('initRoutes')
                    let path = vue.$route.query.redirect;
                    vue.$router.push(path ? path : 'home/')
                } else node.verifyCode = "/verifyCode?time=" + Date.now()
                node.lock = true
            }).catch(reason => {
                node.lock = true
            });
        },
        async initRoutes({dispatch, getters, commit}) {
            let routes = JSON.parse(window.sessionStorage.getItem('routes'))
            if (routes) {
                routes = initElse(routes);
                return commit('routes', routes)
            }
            if (getters.user && (!getters.routes || !getters.routes.length)) {
                dispatch('setRoutes', await initMenu(router))
            }
        },
        async setUser({commit, state}, value) {
            window.sessionStorage.setItem('user', JSON.stringify(value))
            commit('user', value)
        },
        setRoutes({commit}, value) {
            window.sessionStorage.setItem('routes', JSON.stringify(value))
            commit('routes', value)
        },
        reset({commit, dispatch}) {
            window.sessionStorage.removeItem('routes')
            window.sessionStorage.removeItem('user')
            commit('routes', null)
            commit('user', null)
            //清空之前加载的addRouters()
            router.matcher = createRouter().matcher;
            router.replace('/')
        }
    },
    modules: {
        chat,
    }
})
store.watch(function (state, getters) {
    return state.chat.friends
}, function (val) {
    localStorage.setItem('vue-chat-session' + store.state.user.id, JSON.stringify(val))
}, {
    deep: true/*这个貌似是开启watch监测的判断,官方说明也比较模糊*/
})
/*
好像只能开启一个
store.watch(function (state, getters) {
    return state.chat.friends
}, function (val) {
    if (val[0])
        store.commit('chat/changeCurrentSession', val[0])
}, {
    deep: true/!*这个貌似是开启watch监测的判断,官方说明也比较模糊*!/
})*/


export default store