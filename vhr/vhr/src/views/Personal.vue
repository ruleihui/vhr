<template>
  <div class="Personal">
    <!--    <el-upload
            class="avatar-uploader"
            :action="'/userFace/'+form.id"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
          <img v-if="form.userface" :src="form.userface" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
        <el-form :model="form">
          <el-form-item label="名称" prop="name">
            <el-input v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="password2">
            <el-input v-model="form.password2"></el-input>
          </el-form-item>
          <el-form-item label="地址" prop="address">
            <el-input v-model="form.address"></el-input>
          </el-form-item>
          <el-form-item label="电话号码" prop="telephone">
            <el-input v-model="form.telephone"></el-input>
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="form.phone"></el-input>
          </el-form-item>
        </el-form>-->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ form.name }}</span>
      </div>
      <div class="text item">
        <div>
          <el-upload
              class="avatar-uploader"
              :action="'/userFace/'+form.id"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload">
            <img title="点击上传新的头像" v-if="form.userface" :src="form.userface" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <div class="info">
          <span>联系地址:</span>
          <el-tag size="medium">{{ form.address }}</el-tag>
        </div>
        <div class="info">
          <span>电话号码:</span>
          <el-tag size="medium">{{ form.telephone }}</el-tag>
        </div>
        <div class="info">
          <span>手机号码:</span>
          <el-tag size="medium">{{ form.phone }}</el-tag>
        </div>
        <div class="info">
          <span>用户角色:</span>
          <el-tag size="medium" type="success" v-for="role in  form.roles">
            {{ role.nameZh }}
          </el-tag>
        </div>
        <div style="display: flex; justify-content: space-between;margin-top: 10px">
          <el-button size="medium"
                     @click="updateInfoOpen"
                     type="primary">修改信息
          </el-button>
          <el-button size="medium" @click="updatePasswordOpen" type="danger">修改密码</el-button>
        </div>
      </div>
    </el-card>
    <div class="personalHidden">
      <el-dialog
          title="修改用户信息"
          width="30%"
          :visible.sync="dialogVisible"
      >
        <div>
          <table>
            <tr>
              <td>
                <el-tag>用户名称:</el-tag>
              </td>
              <td>
                <el-input size="small" v-model="updateForm.name"></el-input>
              </td>
            </tr>
            <tr>
              <td>
                <el-tag>联系地址:</el-tag>
              </td>
              <td>
                <el-input size="small" v-model="updateForm.address"></el-input>
              </td>
            </tr>
            <tr>
              <td>
                <el-tag>电话号码:</el-tag>
              </td>
              <td>
                <el-input size="small" v-model="updateForm.telephone"></el-input>
              </td>
            </tr>
            <tr>
              <td>
                <el-tag>手机号码:</el-tag>
              </td>
              <td>
                <el-input size="small" v-model="updateForm.phone"></el-input>
              </td>
            </tr>
            <tr>
              <td>
                <el-tag>用户头像:</el-tag>
              </td>
              <td>
                <el-input placeholder="请输入图片地址" size="small" v-model="updateForm.userface"></el-input>
              </td>
            </tr>
          </table>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="updateInfoSubmit">确 定</el-button>
        </span>
      </el-dialog>
      <el-dialog
          title="修改密码"
          width="30%"
          :visible.sync="passwordDialogVisible">
        <!--        <el-form ref="form" :model="updateForm" :rules="rules" label-width="80px">
                  <el-form-item label="密码" prop="password">
                    <el-input v-model="updateForm.password"/>
                  </el-form-item>
                  <el-form-item label="确认密码" prop="password2">
                    <el-input v-model="updateForm.password2"/>
                  </el-form-item>
                </el-form>-->
        <el-form :model="updateForm" status-icon :rules="rules" ref="ruleForm" label-width="100px"
                 class="demo-ruleForm">
          <el-form-item label="旧密码" prop="old">
            <el-input type="password" v-model="updateForm.old" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="updateForm.password" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="password2">
            <el-input type="password" v-model="updateForm.password2" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="resetForm('ruleForm')">重置</el-button>
          <el-button type="primary" @click="updatePasswordSubmit('ruleForm')">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>

</template>

<script>
export default {
  name: "Personal",
  data() {
    let validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.updateForm.password !== '') {
          this.$refs.ruleForm.validateField('password2');
        }
        callback();
      }
    };
    let validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.updateForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      updateForm: '',
      dialogVisible: false,
      passwordDialogVisible: false,
      userface: '',
      rules: {
        old: [
          {required: true, message: "请输入旧密码", trigger: 'blur'},
        ],
        password: [
          {validator: validatePass, trigger: 'blur'},
        ],
        password2: [
          {validator: validatePass2, trigger: 'blur'},
        ],
        userface: [
          {required: true, message: "请输入图片地址", trigger: 'blur'}
        ],
      },
    }
  },
  created() {
    this.form.password2 = ''
    this.form.old = ''
  },
  computed: {
    form() {
      return this.$store.getters.user
    }
  },
  methods: {
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    updateInfoOpen() {
      this.dialogVisible = true
      this.updateForm = this.utils.clone(this.form);
    },
    updatePasswordOpen() {
      this.passwordDialogVisible = true
      this.updateForm = this.utils.clone(this.form);
    },
    updatePasswordSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let temp = {}
          temp.old = this.updateForm.old
          temp.password = this.updateForm.password
          temp.password2 = this.updateForm.password2
          this.$axios.executeArgs('put', '/hrInfo/password', temp).then(data => {
            if (data) {
              this.$store.dispatch('reset')
              this.passwordDialogVisible = false
            }
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    updateInfoSubmit() {
      delete this.updateForm.authorities
      if (!this.utils.isAllNull(this.updateForm)) {
        this.updateForm.userface=this.updateForm.userface.indexOf('blob') != -1 ? this.userface : this.updateForm.userface
        this.$axios.executeArgs('put', '/hrInfo', this.updateForm).then(data => {
          if (data) {
            this.$store.commit('user', data)
            this.dialogVisible = false
          }
        })
      } else this.$message.error("信息不能完全为空")
    },
    handleAvatarSuccess(res, file) {
      this.$message.info("修改头像成功");
      let updateForm=this.utils.clone(this.form)
      updateForm.userface = res.obj
      this.userface = res.obj
      window.sessionStorage.setItem('user', JSON.stringify(updateForm))
      this.form.userface = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 1;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    }
  }
}
</script>

<style>
.Personal {
  display: flex;
  width: 100%;
  height: 100%;
  justify-items: center;
  justify-content: center;
  align-items: start;
}

.Personal .el-form {
  position: relative;
}

.Personal .el-form-item {
  display: flex;
}

.Personal .demo-ruleForm .el-form-item {
  display: block;
}


.Personal .el-upload {
  border: none !important;
  /*transform: translateX(-140px);*/
  display: flex;
  justify-content: center;
  z-index: 10;
}

/*上传*/
.Personal .avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.Personal .avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.Personal .avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.Personal .avatar {
  width: 120px;
  height: 120px;
  border-radius: 100px;
}

.Personal .text {
  font-size: 14px;
}

.Personal .item {
  margin-bottom: 18px;
  font-size: 15px;
  font-family: 微软雅黑;
}

.Personal .info span {
  margin-bottom: 5px;
  margin-right: 10px;
}

.Personal .clearfix:before,
.Personal .clearfix:after {
  display: table;
  content: "";
}

.Personal .clearfix:after {
  clear: both
}

.Personal .box-card {
  width: 480px;
}


</style>