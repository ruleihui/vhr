<template>
  <div id="userText">
    <textarea placeholder="按 Ctrl + Enter 发送" v-model="content" @keyup.ctrl.enter="addMessage0"></textarea>
  </div>
</template>

<script>
import {createNamespacedHelpers} from 'vuex'

const {mapMutations: chatCommit, mapGetters: chatGetters} = createNamespacedHelpers('chat');
export default {
  name: 'userText',
  data() {
    return {
      content: '',
    }
  },
  computed: chatGetters(['stomp', 'currentSession']),
  methods: {
    addMessage0(e) {
      if (e.ctrlKey && e.keyCode === 13 && this.content.length) {
        let obj = new Object(), temp, format;
        obj.to = this.currentSession.username
        obj.message = this.content
        temp = JSON.stringify(obj)
        this.stomp.send('/ws/chat', {}, temp)
        format = new Date();
        obj.date = `${format.getHours()}:${format.getMinutes()}:${format.getSeconds()}`
        obj.self=true
        this.addMessage(obj)
        this.content = '';
      }
    },
    ...chatCommit(['addMessage'])
  }
}
</script>

<style lang="scss" scoped>
#userText {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 100%;
  height: 30%;
  border-top: solid 1px #DDD;

  > textarea {
    padding: 10px;
    width: 100%;
    height: 100%;
    border: none;
    outline: none;
  }
}
</style>
