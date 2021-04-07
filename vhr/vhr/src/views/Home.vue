<template>
  <el-container>
    <el-header class="custom-header">
      <div class="title">微人事</div>
      <!--      对于内部元素的点击事件会冒泡到外部元素,触发外部元素的点击事件,
                但是事件的srcElement属性依旧为内部被点击元素-->
      <div class="homeRight">
        <el-badge :is-dot="is_dot">
          <el-button @click="chat" type="text" size="normal" icon="el-icon-bell"></el-button>
        </el-badge>
        <el-dropdown @command="commandHandler">
        <span class="el-dropdown-link">
           <span> {{ user.name }}</span>
            <i v-if="!user.userface" class="el-icon-arrow-down el-icon--right"></i>
            <i v-else><img :src="user.userface"/></i>
        </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="personal">个人中心</el-dropdown-item>
            <!--          禁用的-->
            <!--          <el-dropdown- command=item disabled>双皮奶</el-dropdown-item>-->
            <!--          与其它item存在分隔线 -->
            <el-dropdown-item command="signOut" divided>注销登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>
    <el-container>
      <el-aside width="200px">
        <!--        Aside -->
        <!--   unique-opened确保一次只能开启一个菜单     -->
        <el-menu class="custom-el-menu" router :unique-opened="true" :default-active="open">
          <!--          index属性区分不同submenu,如果相同,打开某一个菜单会打开全部菜单-->
          <el-submenu :index="''+index" v-for="(item,index) in routes" v-if="!item.hidden">
            <template slot="title">
              <i style="color: #409eff;margin-right: 5px" :class="item.iconCls"></i>
              <span>{{ item.name }}</span>
            </template>
            <el-menu-item v-for="child in item.children"
                          :key="child.path"
                          :index="child.path">
              {{ child.name }}
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main>
        <!--        Main-->
        <el-breadcrumb separator="/" v-if="this.$route.name!='Home'">
          <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{ this.$route.name }}</el-breadcrumb-item>
        </el-breadcrumb>
        <template v-else>
          <div class="home-welcome">
            欢迎来到微人事
          </div>
        </template>
        <div class="router-view">
          <router-view/>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>

import {createNamespacedHelpers} from 'vuex'

const {mapGetters: chatGetters} = createNamespacedHelpers('chat')


export default {
  name: "Home",
  async beforeMount() {
    await this.$store.dispatch('chat/initData');
    console.log('chat/initData-OK')
  },
  async created() {
    await this.$confirm("当前为展示状态,每一刻钟会回滚数据库", "通知")
    let user=this.$store.getters.user;
    this.utils.setImgCache(user.userface,user,"userface");
    this.$store.commit('user',user);
  },
  data: function () {
    return {
      open: window.location.hash.substr(1),
    }
  },
  computed: {
    routes: function () {
      return this.$store.getters.routes
    },
    user() {
      return this.$store.getters.user
    },
    ...chatGetters(['is_dot'])
  },
  methods: {
    commandHandler: function (command) {
      this[command]();
    },
    chat() {
      this.$router.replace('/chat')
    },
    personal() {
      this.$router.replace('/personal')
    },
    signOut() {
      let node = this;
      this.$confirm('此操作将退出登录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        node.$axios.execute('get', '/logout').then(data => {
              node.$store.dispatch('reset')
            }
        )
      }).catch(() => {
        node.$message({
          type: 'info',
          message: '已取消'
        });
      });
    },

  }
}
</script>

<style>
.router-view {
  margin-top: 10px;
}

.home-welcome {
  text-align: center;
  font-size: 30px;
  font-family: 楷体;
  color: #409eff;
  padding-top: 50px;
}

.custom-header {
  background: #409eff;
  /*flex: flex-grow flex-shrink flex-basis|auto|initial|inherit;*/
  display: flex;
  /*align-items 属性定义flex子项在flex容器的当前行的侧轴（纵轴）方向上的对齐方式。*/
  align-items: center;
  /*justify-content: flex-start|flex-end|center|space-between|space-around|initial|inherit;*/
  /*justify-content 用于设置或检索弹性盒子内的元素在主轴（横轴）方向上的对齐方式。*/
  /*https://www.runoob.com/cssref/css3-pr-justify-content.html*/
  justify-content: space-between;
  padding: 0px 15px;
  /*	指定宽度和高度（最小/最大属性）确定元素边框。也就是说，对元素指定宽度和高度包括了 padding 和 border 。
  通过从已设定的宽度和高度分别减去边框和内边距才能得到内容的宽度和高度。*/
  box-sizing: border-box;
}

.custom-header .title {
  font-size: 30px;
  font-family: 楷体;
  color: #fff;
}

.homeRight .el-badge sup {
  height: 6px;
  width: 6px;
  border-radius: 10px;
  transform: translateX(3px);
}

.homeRight .el-button {
  color: black;
  /*font-size: inherit;*/
  margin-right: 8px;
  padding-top: 1px;
  padding-bottom: 1px;
}


.el-dropdown-link {
  cursor: pointer;
  color: #fff;
  display: flex;
  align-items: center;
}

.el-dropdown-link i img {
  width: 48px;
  height: 48px;
  border-radius: 24px;
  margin-left: 8px;
}

.el-icon-arrow-down {
  font-size: 12px;
}

.custom-el-menu {
  cursor: pointer
}
</style>