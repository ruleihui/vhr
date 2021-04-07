<template>
  <div class="JobLevel">
    <div class="header">
      <el-input prefix-icon="el-icon-plus" placeholder="添加职称" v-model="jobLevel.name" size="small"/>
      <el-select v-model="jobLevel.titleLevel" placeholder="职称等级" size="small">
        <el-option
            v-for="item in options"
            :key="item"
            :label="item"
            :value="item">
        </el-option>
      </el-select>
      <el-button type="primary" icon="el-icon-plus" @click="add" size="small">添加</el-button>
    </div>
    <div class="body">
      <el-table
          :border="true"
          :data="tableData"
          stripe
          @selection-change="selected"
          size="small">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
            prop="id"
            label="编号"
            width="55">
        </el-table-column>
        <el-table-column
            prop="name"
            label="职称名称"
            width="140">
        </el-table-column>
        <el-table-column
            prop="titleLevel"
            label="职称等级"
            width="120">
        </el-table-column>
        <el-table-column
            prop="createDate"
            label="创建时间"
            width="180"
            :formatter="this.utils.tableDateFormat">
        </el-table-column>
        <el-table-column
            prop="enabled"
            label="是否启用"
            width="100"
        >
          <template v-slot:default="scope">
            <el-tag v-if="scope.row.enabled" type="success">已启用</el-tag>
            <el-tag v-else type="danger">未启用</el-tag>
          </template>
        </el-table-column>

        <el-table-column
            label="操作"
        >
          <template slot-scope="scope">
            <el-button
                size="mini"
                @click="updateOpen(scope.$index, scope.row)">编辑
            </el-button>
            <el-button
                size="mini"
                type="danger"
                @click="deleteJob(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button :disabled="disable" size="small" type="danger" @click="deleteMulti" style="margin-top: 10px">批量删除
      </el-button>
    </div>
    <div class="hidden">
      <el-dialog title="修改职称" :visible.sync="dialogFormVisible">
        <!--        可以通过表格排序-->
        <el-form :model="form">
          <el-form-item prop="name" label="职称名称" :label-width="formLabelWidth"
                        :rules="{required:true,message:'name不能为空',trigger:'blur'}">
            <el-input v-model="form.name" autocomplete="off" style="width: 220px"></el-input>
          </el-form-item>
          <el-form-item prop="titleLevel" label="职称等级" :label-width="formLabelWidth"
                        :rules="{required:true,message:'请选择值',trigger:'blur'}">
            <el-select v-model="form.titleLevel" placeholder="请选择活动区域">
              <el-option
                  v-for="item in options"
                  :key="item"
                  :label="item"
                  :value="item">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="是否启用" :label-width="formLabelWidth">
            <el-radio-group v-model="form.enabled">
              <el-radio :label="true">启用</el-radio>
              <el-radio :label="false">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="updateSubmit">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  name: "JobLevelManage",
  mounted() {
    this.init()
  },
  data() {
    return {
      jobLevel: {name: '', titleLevel: ''},
      jobLevelTemp: {name: '', titleLevel: ''},
      options: ['正高级', '副高级', '中级', '初级', '员级'],
      tableData: [],
      form: {name: '', titleLevel: '', enabled: '',},
      formTemp: {name: '', titleLevel: '', enabled: '',},
      dialogFormVisible: false,
      formLabelWidth: "120px",
      selects: [],
      disable: true,
    }
  },
  methods: {
    init() {
      this.$axios.execute('get', '/sys/basic/jobLevel').then(data => {
        if (data)
          this.tableData = data
      })
    },
    selected(selected) {
      if (!selected.length) this.disable = true
      else {
        this.selects = selected;
        this.disable = false
      }
    },
    updateOpen(index, row) {
      this.dialogFormVisible = true
      this.form = this.utils.clone(row)
      this.formTemp = this.utils.clone(row)
    },
    updateSubmit() {
      if (!this.utils.isNull(this.form) && !this.utils.same(this.form, this.formTemp))
        this.$axios.executeArgs('put', '/sys/basic/jobLevel', this.form).then(
            data => {
              if (data) {
                this.init()
                Object.assign(this.formTemp, this.form)
                this.dialogFormVisible = false
              }
            }
        )
      else {
        this.$message.error('请修改/填写值')
      }
    },
    add() {
      if (!this.utils.isNull(this.jobLevel) && !this.utils.same(this.jobLevel, this.jobLevelTemp))
        this.$axios.executeArgs('post', '/sys/basic/jobLevel', this.jobLevel).then(
            data => {
              if (data) {
                this.init()
                this.utils.toNull(this.jobLevel)
                Object.assign(this.jobLevelTemp, this.jobLevel)
              }
            }
        )
      else {
        Object.assign(this.jobLevelTemp, this.jobLevel)
        this.$message.error('请修改/填写值')
      }
    },
    deleteJob(index, row) {
      let operation = () => this.$axios.execute('delete', '/sys/basic/jobLevel/' + row.id).then(data => {
        if (data) {
          this.init()
        }
      })
      this.utils.deleteConfirm(operation)
    },
    deleteMulti() {
      let ids = this.selects.map(it => it.id).join('&ids=')
      let operation = () => this.$axios.execute('delete', '/sys/basic/jobLevel?ids=' + ids).then(data => {
        if (data) {
          this.init()
        }
      })
      this.utils.deleteConfirm(operation)
    }
  }
}
</script>

<style scoped>
.JobLevel button {
  cursor: pointer;
}

.JobLevel .header div {
  /*元素外的右间隔值*/
  margin-right: 10px;
}


.JobLevel .header .el-input {
  width: 220px;
}

.JobLevel .body .el-table {
  margin-top: 10px;
  width: 70%
}

.JobLevel .body .el-table--border th.gutter:last-of-type {
  /*!important在IE6以上被!important标记的属性拥有更高的优先级
    可以覆盖原本的css样式
    此元素将显示为块级元素，此元素前后会带有换行符。*/
  display: block !important;
  width: 17px !important;
}

.JobLevel .body .el-table th:nth-last-child(2) {
  border-right: none;
}
</style>