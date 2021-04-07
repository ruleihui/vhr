<template>
  <div id="app">
    <div class="sidebar">
      <card></card>
      <list></list>
    </div>
    <div class="main">
      <message></message>
      <userText></userText>
    </div>
  </div>
</template>

<script>
import card from '@/components/chat/card'
import list from '@/components/chat/list'
import message from '@/components/chat/message'
import userText from '@/components/chat/userText'

export default {
  name: 'FriendChat',
  created() {
    this.$store.state["chat/is_dot"]=true
  },
  data() {
    return {}
  },
  components: {
    card,
    list,
    message,
    userText
  }
}
</script>

<style lang="scss" scoped>
#app {
  margin: 20px auto;
  width: 800px;
  height: 600px;
  overflow: hidden;
  border-radius: 10px;

  .sidebar, .main {
    height: 100%;
  }

  .sidebar {
    float: left;
    color: #f4f4f4;
    background-color: #2e3238;
    width: 200px;
  }

  .main {
    position: relative;
    overflow: hidden;
    background-color: #eee;
  }
}
</style>
