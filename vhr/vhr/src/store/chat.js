import {execute} from '@/utils/http'
import Stomp from 'stompjs'
import SockJS from 'sockjs-client'
import fa from "element-ui/src/locale/lang/fa";

const now = new Date();

export default {
    namespaced: true,
    state() {
        return {
            friends: [],
            showFriends: [],
            currentSession: {},
            filterKey: '',
            stomp: null,
            root: {},
            is_dot: false,
        }
    },
    getters: {
        sessions: (state) => state.sessions,
        currentSession: (state) => state.currentSession,
        filterKey: (state) => state.filterKey,
        friends: (state) => state.friends,
        showFriends: (state) => state.friends.filter(it => !it.hidden),
        stomp: (state) => state.stomp,
        is_dot:(state)=>state.is_dot,
    },
    mutations: {
        changeCurrentSession(state, user) {
            if (user) user.new = false
            state.currentSession = user;
        },
        addMessage(state, value) {
            /*            state.sessions[state.currentSession - 1].messages.push({
                            content: msg,
                            date: new Date(),
                            self: true
                        })*/
            let body = value, user = state.root.user, from = (user.username == body.to) ? body.from : body.to,
                fromUser = state.friends.find(it => it.username == from)
            fromUser.id != state.currentSession.id ? fromUser.new = true : ''
            fromUser.messages.push(body)
        },
        setRoot(state, date) {
            state.root = date
        },
        setFriends(state, data) {
            state.friends = data
        },
        setStomp(state, data) {
            state.stomp = data
        },
        setIsDot(state, data) {
            state.is_dot = data
        },
    },
    actions: {
        async initData(context) {
            context.commit('setRoot', context.rootState)
            let list = await (() => {
                //TODO 本地历史聊天记录
                let data = localStorage.getItem('vue-chat-session' + context.state.root.user.id);
                //console.log(data)
                if (data && data != 'undefined') {
                    return JSON.parse(data);
                }
            })()
            let friends = await execute('get', '/chat/friends').then(data => {
                if (data) return data
            })
            //初始化容器
            if (friends) friends.forEach(it => {
                it.messages = []
                //是否展示
                it.hidden = false
                //是否有新消息
                it.new = false
                vue.utils.setImgCache(it.userface,it,"userface");
            })
            //初始化消息存储
            if (list)
                list.forEach(it => {
                    friends.find(me => me.id == it.id).messages = it.messages
                })
            if (friends) {
                context.commit('setFriends', friends)
                //默认选中会话
                context.commit('changeCurrentSession', friends[0])
            }
            await context.dispatch('connect')
        },
        //node_modules/stompjs/lib/stomp.js
        async connect(context) {
            //node_modules/sockjs-client/lib/main.js
            //function SockJS(url, protocols, options)
            let stomp = Stomp.over(new SockJS('/ws/ep'));
            context.commit('setStomp', stomp)
            //headers = out[0], this.connectCallback = out[1], errorCallback = out[2];
            await context.getters.stomp.connect({}, success => {
                //function(destination, callback, headers)
                context.getters.stomp.subscribe('/user/queue/chat', msg => {
                    let temp = JSON.parse(msg.body);
                    temp.self = false
                    let date = new Date(temp.date)
                    temp.date = `${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`
                    if (vue.$route.path != '/chat') context.commit('setIsDot', true)
                    context.commit('addMessage', temp)
                })
            }, error => {

            })
        },
        updateFriend(store, search) {
            let temp = search
            if (!temp) store.state.friends.forEach(it => it.hidden = false)
            else {
                store.state.friends.filter(it => it.name != temp).forEach(it => it.hidden = true)
                store.state.friends.filter(it => it.name == temp).forEach(it => it.hidden = false)

            }
            store.commit('setFriends', store.state.friends)
            store.commit('changeCurrentSession', store.state.friends.find(it => it.hidden == false))
        }
    }
}




