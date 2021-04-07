<template>
  <div>
    <el-form
        v-loading="!this.lock"
        status-icon class="loginContainer" ref="loginForm" :rules="rules" :model="loginForm">
      <h3 class="loginTitle">系统登录</h3>
      <el-form-item prop="username">
        <el-input size="larger" type="text" v-model="loginForm.username" auto-complete="off"
                  :placeholder="placeholder.username" :clearable="true"
                  @keydown.enter.native="submit"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <!--   clearable='true'会传入一个字符串导致报错
              通过:clearable绑定使用,"true"会作为一个JavaScript表达式传入,type=Boolean
              相对的字符串形式"'true'"-->
        <el-input size="larger" type="password" v-model="loginForm.password" auto-complete="off"
                  :placeholder="placeholder.password" :clearable="true"
                  @keydown.enter.native="submit"></el-input>
      </el-form-item>
      <el-form-item prop="code"  >
        <el-input style="display: inline-flex;width: 70%;margin-right: 20px"
            size="larger" type="text" v-model="loginForm.code" auto-complete="off"
                  :placeholder="placeholder.code" :clearable="true"
                  @keydown.enter.native="submit"></el-input>
        <img :src="verifyCode" @click="updateVerifyCode"/>
      </el-form-item>
      <el-form-item label="记住我:">
        <el-checkbox size="larger" class="loginRemember" v-model="checked"></el-checkbox>
      </el-form-item>
      <el-form-item>
        <el-button size="larger" type="primary" @click="submit">
          登录
        </el-button>
        <el-button size="larger" @click="resetFrom">
          重置
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
var messages = {
  username: '请输入用户名',
  password: '请输入密码',
  code: '请输入验证码',
}
export default {
  name: "Login",
  data() {
    return {
      verifyCode: "/verifyCode?time="+Date.now(),
      rules: {
        username: [
          {required: true, message: messages['username'], trigger: 'blur'},
        ],
        password: [
          {required: true, message: messages['password'], trigger: 'blur'},
        ],
        code: [
          {required: true, message: messages['code'], trigger: 'blur'},
        ]
      },
      loginForm: {
        username: '',
        password: '',
        code: '',
      },
      messages,
      placeholder: {
        username: 'youke1/youke2/youke3',
        password: 123,
        code: '点击图片更换验证码',
      },
      checked: true,
      lock: true,
    }
  },
  methods: {
    updateVerifyCode() {
      this.verifyCode="/verifyCode?time="+Date.now();
    },
    resetFrom: function (formName) {
      formName = 'loginForm'
      this.$refs[formName].resetFields();
      this.$refs[formName].clearValidate();

    },
    submit: function (formName) {
      formName = 'loginForm'
      let node = this
      this.$refs[formName].validate((valid, object) => {
        if (valid && node.lock) {
          node.lock = false
          let loginForm = this.loginForm;
          this.$store.dispatch('login', {node, loginForm})
        } else {
          if (!Object.keys(object).length)
            this.$message.error('invalid submit');
          return false;
        }
      });
    }
  }
}
</script>


<style >
/*display的样式布局,只作用于元素的直接子节点*/
.loginTitle {
  /*距离父元素上下边距为20px,左右auto*/
  margin: 20px auto;
  /*文本对齐为居中*/
  text-align: center;
  /*字体颜色*/
  color: #505458;

}

.loginContainer {
  /*边框圆角*/
  border-radius: 15px;
  /*指定背景绘制区域从边框内部绘制背景,不包括边框*/
  background-clip: padding-box;
  /*距离父元素的上下边距为180px,左右边距auto(居中)*/
  margin: 180px auto;
  /*宽度为350像素*/
  width: 350px;
  /*内边距为上右35px,下15,左35*/
  padding: 35px 35px 15px 35px;
  /*背景色为白色#fff*/
  background: #fff;
  /*边框线条为1像素,实线,颜色为#eaeaea*/
  border: 1px solid #eaeaea;
  /*边框阴影 box-shadow: h-shadow v-shadow blur spread color inset;*/
  /*水平阴影和垂直阴影不相对于box移动,以box为中心向外的模糊距离为25,阴影颜色为#cac6c6*/
  box-shadow: 0 0 25px #cac6c6;
}
.loginContainer .el-form-item:nth-of-type(3) .el-form-item__content{
  display: flex;
  align-content: center;
}
</style>