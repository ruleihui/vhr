<template>
  <div class="SalSobCfg">
    <el-table :data="tableData"
              style="width: 100%"
              stripe
              border>
      <el-table-column label="姓名" width="100" prop="name">

      </el-table-column>
      <el-table-column label="工号" width="100" prop="workId">

      </el-table-column>
      <el-table-column label="电子邮件" width="200" prop="email">

      </el-table-column>
      <el-table-column label="电话号码" width="100" prop="phone">

      </el-table-column>
      <el-table-column label="所属部门" width="100" prop="department.name">

      </el-table-column>
      <el-table-column label="工资账套" width="200">
        <template slot-scope="scope">
          <el-tooltip v-if="scope.row.salary" class="item" effect="dark" placement="right">
            <div slot="content">
              <div class="SalSobCfgTooltipBody">
                <div>
                  <el-tag size="mini">基本工资</el-tag>
                  ¥ {{ scope.row.salary.basicSalary }}
                </div>
                <div>
                  <el-tag size="mini">奖金</el-tag>
                  ¥ {{ scope.row.salary.bonus }}
                </div>
                <div>
                  <el-tag size="mini">午餐补助</el-tag>
                  ¥ {{ scope.row.salary.lunchSalary }}
                </div>
                <div>
                  <el-tag size="mini">交通补助</el-tag>
                  ¥ {{ scope.row.salary.trafficSalary }}
                </div>
                <div>
                  <el-tag size="mini">养老金比率</el-tag>
                  {{ scope.row.salary.pensionPer }}
                </div>
                <div>
                  <el-tag size="mini">养老金基数</el-tag>
                  ¥ {{ scope.row.salary.pensionBase }}
                </div>
                <div>
                  <el-tag size="mini">公积金比率</el-tag>
                  {{ scope.row.salary.accumulationFundPer }}
                </div>
                <div>
                  <el-tag size="mini">公积金基数</el-tag>
                  ¥ {{ scope.row.salary.accumulationFundBase }}
                </div>
                <div>
                  <el-tag size="mini">医疗保险比率</el-tag>
                  {{ scope.row.salary.medicalPer }}
                </div>
                <div>
                  <el-tag size="mini">医疗保险基数</el-tag>
                  ¥ {{ scope.row.salary.medicalBase }}
                </div>
              </div>
            </div>
            <el-tag>{{ scope.row.salary.name }}</el-tag>
          </el-tooltip>
          <el-tag v-else>暂无</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-popover
              placement="left"
              width="200"
              trigger="click">
            <el-select v-model="scope.row.optionsId"
                       @change="setValue($event,scope.row)"
                       placeholder="请选择">
              <el-option
                  v-for="item in options"
                  :label="item.name"
                  :key="item.id"
                  :value="item.id">
              </el-option>
            </el-select>
            <el-button slot="reference" size="mini" type="danger">修改账套</el-button>
          </el-popover>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        background
        :page-size="pageSize"
        @current-change="currentPage"
        @size-change="sizeChange"
        @prev-click="prevClick"
        @next-click="nextClick"
        layout="sizes, prev, pager, next, jumper, ->, total, slot"
        :total="total">
    </el-pagination>
  </div>
</template>

<script>
//1.定义一个或多个普通函数，函数必须返回Promise对象，如果返回其他类型的数据，将按照普通同步程序处理
//async await
export default {
  name: "SalSobCfg",
  data() {
    return {
      tableData: [],
      total: 0,
      pageSize: 10,
      currentIndex: 1,
      prevAndNext: false,
      loading: false,
      options: [],
    }
  },
  async created() {
    await this.initSalary()
    await this.init()
  },
  methods: {
    init() {
      return this.$axios.execute('get', '/salary/sobCfg').then(data => {
        if (data) {
          this.tableData = data.data
          this.total = data.total
        }
      })
    },
    initSalary() {
      return this.$axios.execute('get', '/salary/sob').then(data => {
        if (data)
          this.options = data;
      })
    },
    setValue(value, employee) {
      if (employee.salary)
        this.$axios.execute('put', '/salary/sobCfg/' + employee.id + '/' + employee.optionsId).then(data => {
          this.$set(employee, 'optionsId', value)
          this.currentPage(this.currentIndex)
        })
      else
        this.$axios.execute('post', '/salary/sobCfg/' + employee.id + '/' + employee.optionsId).then(data => {
          this.$set(employee, 'optionsId', value)
          this.currentPage(this.currentIndex)
        })
    },
    currentPage(current) {
      this.currentIndex = current
      if (this.prevAndNext) return
      this.loading = true
      let url = '/salary/sobCfg?page=' + current + '&size=' + this.pageSize + "&" + this.uri;
      this.$axios.execute('get', url).then(data => {
        if (data) {
          this.tableData = data.data
          this.total = data.total
          this.loading = false
        }
      })
    },
    sizeChange(size) {
      if (this.prevAndNext) return
      this.loading = true
      let url = '/salary/sobCfg?page=' + '&size=' + size + "&" + this.uri;
      this.$axios.execute('get', url).then(data => {
        if (data) {
          this.tableData = data.data
          this.total = data.total
          this.loading = false
        }
      })
    },
    prevClick(current) {
      this.currentIndex = current
      this.prevAndNext = true
      this.loading = true
      let url = '/salary/sobCfg?page=' + current + '&size=' + this.pageSize + "&" + this.uri;
      this.$axios.execute('get', url).then(data => {
        if (data) {
          this.tableData = data.data
          this.total = data.total
          this.prevAndNext = false
          this.loading = false
        }
      })
    },
    nextClick(current) {
      this.currentIndex = current
      this.prevAndNext = true
      this.loading = true
      let url = '/salary/sobCfg?page=' + current + '&size=' + this.pageSize + "&" + this.uri;
      this.$axios.execute('get', url).then(data => {
        if (data) {
          this.tableData = data.data
          this.total = data.total
          this.prevAndNext = false
          this.loading = false
        }
      })
    },
  }

}
</script>

<style>
.SalSobCfg .el-table .gutter {
  display: block !important;
}

.SalSobCfg .el-table td div {
  display: flex;
  justify-content: center;
}

.SalSobCfgTooltipBody span {
  /*上右下左*/
  margin: 1px 2px 2px 1px;
}
</style>