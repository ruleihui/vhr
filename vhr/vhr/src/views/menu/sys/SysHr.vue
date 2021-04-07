<template>
  <div class="SysHr">
    <div class="SysHrHeader">
      <el-input v-model="keyword" prefix-icon="el-icon-search" size="small"
                :clearable="true"
                placeholder="默认展示部分用户,可通过用户名搜索更多用户"></el-input>
      <el-button size="small" @click="search" icon="el-icon-search" type="primary">搜索</el-button>
    </div>
    <div class="SysHrBody">
      <el-card v-for="hr in hrs" class="box-card" :key="hr.id">
        <div slot="header" class="clearfix">
          <span>{{ hr.name }}</span>
          <el-button
              @click="deleteById(hr)"
              style="float: right; padding: 3px 0;color: red" icon="el-icon-delete" type="text"></el-button>
        </div>
        <div class="hrContent">
          <div class="hrImage">
            <img :src="hr.userface"/>
          </div>
          <div class="hrInfo">
            <div>
              <el-tag>用户名</el-tag>
              {{ hr.username }}
            </div>
            <div>
              <el-tag>手机号</el-tag>
              {{ hr.phone }}
            </div>
            <div>
              <el-tag>座机号</el-tag>
              {{ hr.telephone }}
            </div>
            <div>
              <el-tag>地址</el-tag>
              {{ hr.address }}
            </div>
            <div>
              <el-tag>是否启用</el-tag>
              <el-switch
                  v-model="hr.enabled"
                  active-text="启用"
                  inactive-text="禁用"
                  @change="enableChange(hr)"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
              >
              </el-switch>
            </div>
            <div class="hrRole">
              <el-tag>用户角色</el-tag>
              <el-tag type="success" v-for="role in hr.roles" :key="role.id">
                {{ role.nameZh }}
              </el-tag>
              <el-popover
                  @hide="hidePop(hr)"
                  title="角色列表"
                  placement="right"
                  width="10"
                  trigger="click">
                <el-select v-model="hr.selectValues"
                           :multiple="true" placeholder="请选择">
                  <el-option
                      v-for="item in roles"
                      :key="item.name"
                      :label="item.nameZh"
                      :value="item.id">
                  </el-option>
                </el-select>
                <el-button slot="reference" icon="el-icon-more" type="text"></el-button>
              </el-popover>
            </div>
            <div>
              <el-tag>备注</el-tag>
              {{ hr.remark }}
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: "SysHr",
  data() {
    return {
      keyword: '',
      hrs: [],
      roles: [],
      searchLock: false,
    }
  },
  mounted() {
    this.initHr()
    this.initRoles()
  },
  methods: {
    search() {
      if (this.searchLock && !this.keyword) return;
      if (!this.keyword) {
        this.initHr();
        this.searchLock = true
        return
      }
      this.$axios.execute('get', '/system/hr?username=' + this.keyword).then(data => {
        if (data) {
          this.hrs = data
          this.searchLock = false
        }
      })
    },
    deleteById(hr) {
      let operation = () => this.$axios.execute('delete', '/system/hr/' + hr.id).then(data => {
        if (data) {
          let index = this.hrs.findIndex(it => it.id == hr.id)
          this.hrs.splice(index,1)
        }
      })
      this.utils.deleteConfirm(operation)
    },
    initHr() {
      this.$axios.execute('get', '/system/hr').then(data => {
        if (data) {
          data.forEach(it => it.enabled = it.enabled == 1 ? true : false)
          data.forEach(it => {
            it.selectValues = it.roles.map(ix => ix.id)
          })
          this.hrs = data
        }
      })
    },
    initRoles() {
      this.$axios.execute('get', '/sys/basic/permission').then(data => {
        if (data) this.roles = data
      })
    },
    hidePop(hr) {
      let tempId = hr.roles.map(it => it.id);
      if (this.utils.same(tempId, hr.selectValues)) {
        return
      }
      hr.roles = [];
      hr.selectValues.forEach(it => hr.roles.push(this.roles.find(ix => ix.id == it)))
      let temp = {
        id: hr.id,
        roles: hr.roles,
      }
      this.$axios.execute('put', '/system/hr/roles', temp).then(data => {
        if (data) return
      })
    },
    enableChange(hr) {
      //通过delete关键字删除对象的某个字段
      // delete hr.roles;
      let temp = {
        id: hr.id,
        enabled: hr.enabled ? 1 : 0,
      }
      this.$axios.executeArgs('put', '/system/hr', temp)
    }
  }
}
</script>

<style scoped>
.SysHrHeader {
  display: flex;
  flex: 1;
  justify-content: center;
}

.SysHrHeader .el-input {
  width: 35%;
  margin-right: 10px;
}

.SysHrBody {
  /*display: block;*/
  display: flex;
  /*定义一行排不下时如何换行*/
  /*
  nowrap	默认值。规定灵活的项目不拆行或不拆列。
  wrap	规定灵活的项目在必要的时候拆行或拆列。
  wrap-reverse	规定灵活的项目在必要的时候拆行或拆列，但是以相反的顺序。
  initial	设置该属性为它的默认值。请参阅 initial。
  inherit	从父元素继承该属性。请参阅 inherit。
  */
  flex-wrap: wrap;
  /*在横轴上,如果周围没有其它标签,则用空格填充*/
  justify-content: space-around;
}

.SysHrBody .el-card {
  width: 30%;
  height: auto;
  margin: 10px;
  /*float: left;*/
}

.SysHrBody .hrContent .hrImage {
  display: flex;
  justify-content: center;
  margin: 2px auto;
}

.SysHrBody .hrContent .hrImage img {
  width: 100px;
  height: 100px;
  border-radius: 100px;
}

.hrContent .hrInfo {
  color: #409eff;
}

.hrContent .hrInfo .el-tag {
  margin-right: 10px;
  margin-top: 3px;
}

/*.hrInfo .hrRole .el-tag:nth-of-type(n+5) {*/
/*  display: none;*/
/*}*/
</style>