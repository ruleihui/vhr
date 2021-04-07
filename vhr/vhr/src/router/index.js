import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "@/views/Login";
import {Message} from "element-ui";
import store from "@/store"
import Home from "@/views/Home";
import Personal from "@/views/Personal";
import {formatRoutes} from "@/utils/utils";
import axios from "axios";
import FriendChat from "@/views/chat/FriendChat";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: "login",
        component: Login,
        hidden: true,
    },
    /*    {
            path: '/home',
            name: "Home",
            component: Home,
            hidden: true,
        },*/
    /*    {
            path: '/home',
            name: "导航1",
            component: Home,
            children: [
                {path: 'test1', name: '选项1', component: Test1},
                {path: 'test2', name: '选项2', component: Test2},
            ]
        },*/
]


const router = new VueRouter({
    routes,
})

router.beforeEach((to, from, next) => {
    //filter.doChain()
    //next();
    console.log(to)
    console.log(from)
    if (to.path == '/' && store.getters.user) {
        next('/home/')
    }
    if (to.path == '/') {
        next()
    } else if (store.getters.user == null) {
        Message.error("请先登录")
        next('/' + '?redirect=' + to.path)
    } else {
        /*        //对于组件内的router-view会调用两次
                //第一次'/'下的router-view->到'Home'
                //第二次'Home'下的router-view->到'Home'.children
                if (to.path != from.path)
                    initMenu(router, store)*/
        next()
    }
})

let obj = () => {
    return {
        path: '/home',
        name: "Home",
        component: Home,
        hidden: true,
        children: [{
            path: '/personal',
            name: "个人设置",
            component: Personal,
        }, {
            path: '/chat',
            name: "在线聊天",
            component: FriendChat,
        }],
    }
}

export const initElse = (routes) => {
    let temp=obj();
    let fmtRoutes = formatRoutes(temp, routes)
    router.addRoutes([temp])
    return fmtRoutes
}

export const initMenu = async (router) => {
    let temp=obj();
    let fmtRoutes
    await axios.get('/system/config/menu').then(data => {
        if (data) {
            fmtRoutes = formatRoutes(temp, data)
            router.addRoutes([temp]);
        }
    });
    return fmtRoutes
}

export const createRouter = () => new VueRouter({
    routes: routes
});


export default router
