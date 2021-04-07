<template>
  <div id="list">
    <ul>
      <li v-for="item in showFriends"
          :class="{ active: currentSession ? item.username === currentSession.username:false }"
          @click="changeCurrentSession(item)"><!--   :class="[item.id === currentSessionId ? 'active':'']" -->
        <img class="avatar" :src="item.userface" :alt="item.name">
        <el-badge  is-dot :hidden="!item.new" value="new" class="item">
          <p class="name">{{ item.name }}</p>
        </el-badge>
      </li>
    </ul>
  </div>
</template>

<script>
import {createNamespacedHelpers} from 'vuex'

const {mapGetters: chatGetters, mapMutations: chatCommit} = createNamespacedHelpers('chat');
export default {
  name: 'list',
  data() {
    return {}
  },
  computed: {
    ...chatGetters(['sessions', 'currentSession', 'friends','showFriends',])
  },
  methods: chatCommit(['changeCurrentSession']),
}
</script>

<style lang="scss" scoped>
#list {
  display: flex;
  max-height: 79%;
  overflow-y: auto;
  overflow-x: hidden;

  ul {
    width: 100%;
    padding-inline-start: 0px;
  }

  li {
    display: inline-flex;
    padding: 12px 15px;
    box-sizing: border-box;
    width: inherit;
    align-items: center;
    border-bottom: 1px solid #292C33;
    cursor: pointer;

    &:hover {
      background-color: rgba(255, 255, 255, 0.03);
    }
    p{
      margin-top: 0px;
      margin-bottom: 0px;
    }
  }

  li.active { /*注意这个是.不是冒号:*/
    background-color: rgba(255, 255, 255, 0.1);
  }

  .avatar {
    border-radius: 2px;
    width: 30px;
    height: 30px;
    vertical-align: middle;
  }

  .name {
    margin-left: 15px;
  }
}
</style>
