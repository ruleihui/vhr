<template>
  <div class="SalSob">
    <div class="SalSobHeader">
      <el-button size="small" type="primary" @click="addOpen" icon="el-icon-plus">添加工资账套
      </el-button>
      <el-button type="success" size="small" @click="init" icon="el-icon-refresh"></el-button>
    </div>
    <div class="SalSobBody">
      <el-table
          :data="tableData"
          style="width: 100%"
          stripe
          border
          @selection-change="selected"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="账套名称" prop="name" width="120"></el-table-column>
        <el-table-column label="奖金" prop="bonus" width="70"></el-table-column>
        <el-table-column label="基本工资" prop="basicSalary" width="70"></el-table-column>
        <el-table-column label="午餐补助" prop="lunchSalary" width="70"></el-table-column>
        <el-table-column label="交通补助" prop="trafficSalary" width="70"></el-table-column>
        <!--        <el-table-column label="应发工资" prop="allSalary" width="70"></el-table-column>-->
        <el-table-column label="启用时间" :formatter="this.utils.tableDateFormat" prop="createDate"
                         width="100"></el-table-column>
        <el-table-column label="养老金" align="center">
          <el-table-column label="比率" prop="pensionPer" width="70"></el-table-column>
          <el-table-column label="基数" prop="pensionBase" width="70"></el-table-column>
        </el-table-column>
        <el-table-column label="医疗金" align="center">
          <el-table-column label="比率" prop="medicalPer" width="70"></el-table-column>
          <el-table-column label="基数" prop="medicalBase" width="70"></el-table-column>
        </el-table-column>
        <el-table-column label="公积金" align="center">
          <el-table-column label="比率" prop="accumulationFundPer" width="70"></el-table-column>
          <el-table-column label="基数" prop="accumulationFundBase" width="70"></el-table-column>
        </el-table-column>
        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button size="small" @click="updateOpen(scope.row)" type="primary">修改</el-button>
            <el-button size="small" @click="deleteById(scope.row)" type="danger">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button style="margin-top: 10px" type="danger" @click="deleteMulti" :disabled="disabled">批量删除</el-button>
    </div>
    <div class="SalSobHide">
      <el-dialog
          title="添加工资账套"
          :visible.sync="dialogVisible"
          width="50%"
      >
        <div class="left">
          <el-steps direction="vertical" :active="stepsActive.active">
            <el-step @click.native="skip(index)" :title="item" v-for="(item,index) in salaryItem"></el-step>
          </el-steps>
          <template v-for="(value,key,index) in from">
            <div v-show="stepsActive.active==index" class="inputAndError">
              <el-input :placeholder="'请输入'+salaryItem[index]+'...'"
                        size="small"
                        :ref="'input'+index"
                        @input="inputCheck($event,key)"
                        v-model="from[key]"

              ></el-input>
              <div v-show="fromError[key]" class="error_">
                <div :class="'error_'+key">1123</div>
              </div>
            </div>
          </template>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button size="mini" :style="{display:(this.stepsActive.active==0?'none':'')}" @click="previousStep">
            {{ (stepsActive.active != 0) ? '上一步' : '' }}
          </el-button>
          <el-button size="mini" @click="nextStep" type="primary">
            {{ stepsActive.active == (salaryItem.length - 1) ? '完成' : '下一步' }}
          </el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  name: "SalSob",
  data() {
    let addForm = {
      'name': '',
      'basicSalary': '',
      'trafficSalary': '',
      'lunchSalary': '',
      'bonus': '',
      'pensionPer': '',
      'pensionBase': '',
      'medicalPer': '',
      'medicalBase': '',
      'accumulationFundPer': '',
      'accumulationFundBase': '',
    }
    let addFormError = this.utils.clone(addForm)
    let fromError = this.utils.clone(addForm)
    let from = this.utils.clone(addForm)
    let updateFormError = this.utils.clone(addForm)
    let updateForm = this.utils.clone(addForm)
    return {
      tableData: [],
      dialogVisible: false,
      stepsActive: {active: 0},
      addActive: {active: 0},
      updateActive: {active: 0},
      salaryItem: [
        '账套名称',
        '基本工资',
        '交通补助',
        '午餐补助',
        '奖金',
        '养老金比率',
        '养老金基数',
        '医疗保险比率',
        '医疗保险基数',
        '公积金比率',
        '公积金基数',
      ],
      from,
      fromError,
      addForm,
      addFormError,
      updateForm,
      updateFormError,
      updateId: 0,
      fromFlag: '',
      selectedAll: [],
      disabled: true,
    }
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.$axios.execute('get', '/salary/sob').then(data => {
        if (data)
          this.tableData = data;
      })
    },
    skip(index) {
      this.stepsActive.active = index
    },
    addOpen() {
      this.from = this.addForm
      this.fromError = this.addFormError
      this.stepsActive = this.addActive
      this.fromFlag = 'add'
      this.dialogVisible = true

    },
    previousStep() {
      //上一步
      if (this.stepsActive.active != 0) {
        this.stepsActive.active--
        setTimeout(()=>{this.$refs['input' + this.stepsActive.active][0].focus();},100)
      }
    },
    nextStep() {
      //完成
      if (this.stepsActive.active == this.salaryItem.length - 1) {
        let temp = this.from;
        //空检查
        if (this.utils.isNull(temp)) {
          this.$message.error('请检查输入值是否有效/填写完整')
          return
        }
        let flag = this.fromFlag;
        if (flag == 'add')
          this.$axios.execute('post', '/salary/sob', temp).then(data => {
            if (data) {
              this.dialogVisible = false
              this.addActive.active = 0
              this.init()
            }
          })
        else if (flag == 'update') {
          temp.id = this.updateId
          this.$axios.execute('put', '/salary/sob', temp).then(data => {
            if (data) {
              this.dialogVisible = false
              this.updateActive.active = 0
              this.init()
            }
          })
        }
        this.utils.toNull(temp)
      }
      //下一步
      else {
        //因为this.stepsActive.active导致的切换是异步的
        //所以focus()不能在还没切换完成前立即调用
        let index = ++this.stepsActive.active
        setTimeout(()=>{this.$refs['input' + index][0].focus();},100)
      }
    },
    inputCheck(value, key) {
      //如果为''[vNode.$emit('input', '')]直接退出
      if (!value) return
      //数字效验
      if (this.stepsActive.active > 0 && !this.utils.numberReg(value)) {
        this.fromError[key] = 'true'
        document.querySelector('.error_' + key).innerText = '请输入整形/浮点型-->(1/1.0)'
        let vNode = this.$refs['input' + this.stepsActive.active][0];
        //取消输入框聚焦
        vNode.blur()
        //清空值
        vNode.$emit('input', '')
      }//清除效验提示
      else {
        this.fromError[key] = ''
      }
    },
    updateOpen(row) {
      this.utils.copy(this.updateForm, row)
      this.from = this.updateForm
      this.fromError = this.updateFormError
      this.stepsActive = this.updateActive
      this.fromFlag = 'update'
      this.updateId = row.id
      this.dialogVisible = true
    },
    selected(select) {
      if (this.disabled) this.disabled = false;
      if (select.length == 0) this.disabled = true;
      this.selectedAll = select;
    },
    deleteById(row) {
      let operation = () => this.$axios.execute('delete', '/salary/sob/' + row.id).then(data => {
        if (data) this.init()
      })
      this.utils.deleteConfirm(operation)
    },
    deleteMulti() {
      let args = this.selectedAll.map(it => it.id).join('&ids=');
      let operation = () => this.$axios.execute('delete', '/salary/sob?ids=' + args).then(data => {
        if (data) {
          this.disabled = true
          this.init()
        }
      })
      this.utils.deleteConfirm(operation)
    },
  }
}
</script>

<style>
.SalSobHeader {
  margin-bottom: 10px;
  display: flex;
  justify-content: space-between;
}

.SalSobBody .el-table .gutter {
  display: block !important;
}

.SalSobHide .left {
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.SalSobHide .left .el-step {
  cursor: pointer;
}

.SalSobHide .inputAndError {
  width: 50%;
}

.error_ {
  color: red;
  position: absolute;
  font-size: 13px;
}
</style>