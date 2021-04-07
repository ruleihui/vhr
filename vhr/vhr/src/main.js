import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import App from './App.vue'
import router, {createRouter} from './router'
//@代表项目根目录,src
import api from "@/utils/http";
import store from "./store"
import 'font-awesome/css/font-awesome.min.css'
import utils from "@/utils/utils";


Vue.prototype.restRouter = createRouter;
let prefix = "";

Vue.use(ElementUI);

Vue.use(api, {prefix: prefix});
Vue.use(utils)

async function main() {
    store.dispatch('init');
    window.vue = new Vue({
        router,
        store,
        data: {
            user: {},
        },
        render: h => h(App)
    }).$mount('#app')
}

main();


/*new Vue({
  render(h){
    return h('ul',{'class':'movies'},[
      h('li',{'class':'movie'},'Star Wars'),
      h('li',{'class':'movie'},'Blade Runner'),
    ])
  }
}).$mount('#app')*/
