<template>
  <div class="permission">
    <div class="header">
      <el-form ref="addFrom" :model="role">
        <el-form-item prop="name" :rules="{required:true,message:'请输入角色英文名',trigger:'blur'}">
          <el-input :clearable="true" auto-complete="false"
                    placeholder="角色英文名"
                    v-model="role.name" size="small">
            <template slot="prepend">{{ prepend }}</template>
          </el-input>
        </el-form-item>
        <el-form-item prop="nameZh" :rules="{required:true,message:'请输入角色中文名',trigger:'blur'}">
          <el-input :clearable="true" auto-complete="false"
                    placeholder="角色中文名" v-model="role.nameZh" size="small">
          </el-input>
        </el-form-item>
        <el-button @click="add" type="primary" size="small" icon="el-icon-plus">添加角色</el-button>
      </el-form>
    </div>
    <div class="body">
      <el-collapse @change="change" v-model="activeName" accordion>
        <template v-for="(role,index) in  roles">
          <el-collapse-item :title="role.nameZh" :name="''+role.id" :key="''+role.id">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>可访问的资源</span>
                <el-button @click="deleteRole" icon="el-icon-delete"
                           style="float: right; padding: 3px 0;color: red"
                           type="text"></el-button>
              </div>
              <div>
                <el-tree v-if="loading&&activeName==role.id" v-loading="loading">
                </el-tree>
                <el-tree v-else
                         :ref="'tree'+index"
                         :default-checked-keys="role.treeDefaultChecked"
                         :show-checkbox="true" :data="treeData"
                         :props="defaultProps"
                         node-key="id"
                ></el-tree>
                <div class="buttons">
                  <el-button @click="cancelUpdate" size="small">取消修改</el-button>
                  <el-button @click="updateSubmit(index)" size="small" type="primary">确认修改</el-button>
                </div>
              </div>
            </el-card>
          </el-collapse-item>
        </template>
      </el-collapse>
    </div>
  </div>

</template>

<script>
export default {
  name: "PermissionGroup",
  data() {
    return {
      role: {
        name: '',
        nameZh: '',
      },
      prepend: 'ROLE_',
      roles: [],
      activeName: '',
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      loading: true,
    }
  },
  mounted() {
    this.init()
  },
  computed: {
    allNameEn() {
      return this.prepend + this.role.name
    }
  },
  methods: {
    init() {
      this.initAllRole();
      this.initAllMenu();
    },
    initAllRole() {
      this.$axios.execute('get', '/sys/basic/permission').then(data => {
        if (data) this.roles = data
      })
    },
    initAllMenu() {
      this.$axios.execute('get', '/sys/basic/permission/menus').then(data => {
        if (data) this.treeData = data
      })
    },
    add() {
      this.$refs.addFrom.validate((isOk, obj) => {
        if (!isOk) return
        let role = this.utils.clone(this.role)
        role.name = this.allNameEn
        this.$axios.executeArgs('post', '/sys/basic/permission/add', role).then(data => {
          if (data) {
            this.initAllRole();
            this.utils.toNull(this.role)
          }
        })
      })
    },
    deleteRole() {
      let operation = () => this.$axios.execute('delete', '/sys/basic/permission/' + this.activeName).then(data => {
        if (data) this.initAllRole();
      })
      this.utils.deleteConfirm(operation);
    },
    cancelUpdate() {
      this.loading = true
      setTimeout(() => {
        this.loading = false
      }, 300)
    },
    updateSubmit(index) {
      let checkedKeys = this.$refs['tree' + index][0].getCheckedKeys(true);
      this.loading = true
      this.$axios.executeArgs('post', '/sys/basic/permission/update/' + this.activeName, checkedKeys).then(
          data => {
            if (data) {
              this.roles.find(it => it.id == this.activeName).treeDefaultChecked = false
              this.change(this.activeName)
            }
          }
      )
    },
    //每当选项卡展开,加载对应角色权限的选择状态
    change(id) {
      if (id) {
        let role = this.roles.find(it => it.id == id)
        if (role && !role.treeDefaultChecked) {
          this.loading = true
          this.$axios.execute('get', '/sys/basic/permission/' + id).then(data => {
            if (data) {
              role.treeDefaultChecked = data
              this.loading = false
            }
          })
        }
      }
    }
  },
}
</script>

<style scoped>

.permission .header .el-form-item {
  /*让每个元素作为行内块展示,并排一行*/
  display: inline-block;
  margin-right: 10px;
  width: 25%;
}

.permission .body {
  margin-top: 20px;
  width: 60%;
}

.permission .body .buttons {
  margin-top: 10px;
  display: flex;
  /*用于设置或检索弹性盒子内的元素在主轴（横轴）方向上的对齐方式。*/
  /*居右排列*/
  justify-content: flex-end
}
</style>