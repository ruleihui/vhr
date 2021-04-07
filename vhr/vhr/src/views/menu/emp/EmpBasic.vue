<template>
  <div class="EmpBasic">
    <div class="EmpBasicHeader">
      <div class="left">
        <el-form size="small" ref="form" :model="keywords">
          <el-form-item id="firstItem" prop="name"
                        :rules="{required:true,message:'不能为空',trigger:'blur'}">
            <el-input :disabled="!this.transition" v-model="keywords.name" size="small" prefix-icon="el-icon-search"
                      placeholder="通过员工名搜索员工">
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button :disabled="!this.transition" size="small" icon="el-icon-search" @click="search" type="primary">
              搜索
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button @click="highSearchOpen" size="small" type="primary">
              <i :class="!transition?'fa fa-angle-double-up':'fa fa-angle-double-down'" aria-hidden="true"></i>
              高级搜索
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button @click="refresh" icon="el-icon-refresh" size="small" type="primary">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="right">
        <el-upload
            action="/employee/basic/import"
            multiple
            :show-file-list="false"
            :before-upload="beforeUpload"
            :on-success="uploadSuccess"
            :on-error="uploadError"
            :disabled="uploading"
        >
          <el-button :disabled="uploading" :icon="uploadButtonIcon" size="small" type="success">
            {{ uploadButtonMsg }}
          </el-button>
        </el-upload>

        <el-button icon="el-icon-download" @click="exportData" size="small" type="success">
          导出数据
        </el-button>
        <el-button @click="addOpen" size="small" icon="el-icon-plus" type="primary">添加员工</el-button>
      </div>
    </div>
    <div class="EmpBasicHigh">
      <transition-group name="EmpBasicHighSearch">
        <div v-show="!transition" class="EmpBasicHighOut" key="EmpBasicHighOut">
          <div key="transitionBody" class="transitionBody">
            <el-form :model="searchForm">
              <el-form-item label="政治面貌:">
                <el-select :size="fromSize" v-model="searchForm.politicId" placeholder="政治面貌">
                  <el-option
                      v-for="(item,index) in politics"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="民族:">
                <el-select :size="fromSize" v-model="searchForm.nationId" placeholder="请选择民族">
                  <el-option
                      v-for="(item,index) in nations"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="职位:">
                <el-select :size="fromSize" v-model="searchForm.posId" placeholder="请选择职位">
                  <el-option
                      v-for="(item,index) in positions"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="职称:">
                <el-select :size="fromSize" v-model="searchForm.jobLevelId" placeholder="请选择职称">
                  <el-option
                      v-for="(item,index) in jobLevels"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="聘用形式">
                <el-radio-group v-model="searchForm.engageForm">
                  <el-radio label="劳动合同">劳动合同</el-radio>
                  <el-radio label="劳务合同">劳务合同</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="所属部门">
                <el-select :size="fromSize" v-model="searchForm.departmentId" placeholder="请选择部门">
                  <el-option
                      v-for="(item,index) in departments"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="入职日期">
                <el-date-picker
                    v-model="searchForm.time"
                    type="datetimerange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期">
                </el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-button @click="highSearchSubmit" :size="fromSize" type="primary" icon="el-icon-search">搜索
                </el-button>
                <el-button :size="fromSize" @click="searchForm={}">取消</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </transition-group>
    </div>
    <div class="EmpBasicBody">
      <el-table
          :data="tableData"
          v-loading="loading"
          border
          size="mini"
          style="width: 100%">
        <el-table-column
            fixed="left"
            width="40"
            type="selection">
        </el-table-column>
        <el-table-column
            fixed="left"
            prop="name"
            label="姓名"
            width="100">
        </el-table-column>
        <el-table-column
            prop="workId"
            label="工号"
            width="100">
        </el-table-column>
        <el-table-column
            prop="gender"
            label="性别"
            width="70">
        </el-table-column>
        <el-table-column
            prop="birthday"
            label="出生日期"
            :formatter="this.utils.tableDateFormat"
            width="150">
        </el-table-column>
        <el-table-column
            prop="idCard"
            label="身份证号"
            width="200">
        </el-table-column>
        <el-table-column
            prop="wedlock"
            label="婚姻状况"
            width="100">
        </el-table-column>
        <el-table-column
            prop="nation.name"
            label="民族"
            width="100">
        </el-table-column>
        <el-table-column
            prop="nativePlace"
            label="籍贯"
            width="100">
        </el-table-column>
        <el-table-column
            prop="politicsStatus.name"
            label="政治面貌"
            width="100">
        </el-table-column>
        <el-table-column
            prop="email"
            label="邮箱"
            width="100">
        </el-table-column>
        <el-table-column
            prop="phone"
            label="手机号"
            width="100">
        </el-table-column>
        <el-table-column
            prop="address"
            label="地址"
            width="300">
        </el-table-column>
        <el-table-column
            prop="department.name"
            label="部门"
            width="100">
        </el-table-column>
        <el-table-column
            prop="jobLevel.name"
            label="职称名称"
            width="100">
        </el-table-column>
        <el-table-column
            prop="jobLevel.titleLevel"
            label="职称等级"
            width="100">
        </el-table-column>
        <el-table-column
            prop="position.name"
            label="职位名称"
            width="100">
        </el-table-column>
        <el-table-column
            prop="engageForm"
            label="聘用形式"
            width="100">
        </el-table-column>
        <el-table-column
            prop="tiptopDegree"
            label="最高学历"
            width="100">
        </el-table-column>
        <el-table-column
            prop="specialty"
            label="所属专业"
            width="100">
        </el-table-column>
        <el-table-column
            prop="school"
            label="毕业院校"
            width="100">
        </el-table-column>
        <el-table-column
            prop="beginDate"
            label="入职日期"
            :formatter="this.utils.tableDateFormat"
            width="150">
        </el-table-column>
        <el-table-column
            prop="workState"
            label="在职状态"
            width="100">
        </el-table-column>
        <el-table-column
            prop="contractTerm"
            label="合同期限(年)"
            width="120">
        </el-table-column>
        <el-table-column
            prop="conversionTime"
            label="转正日期"
            :formatter="this.utils.tableDateFormat"
            width="150">
        </el-table-column>
        <el-table-column
            prop="notworkDate"
            label="离职日期"
            :formatter="this.utils.tableDateFormat"
            width="150">
        </el-table-column>
        <el-table-column
            prop="beginContract"
            label="合同起始日期"
            :formatter="this.utils.tableDateFormat"
            width="150">
        </el-table-column>
        <el-table-column
            prop="endContract"
            label="合同终止日期"
            :formatter="this.utils.tableDateFormat"
            width="150">
        </el-table-column>
        <el-table-column
            prop="workAge"
            label="工龄"
            width="100">
        </el-table-column>
        <el-table-column
            fixed="right"
            label="操作"
            width="120">
          <template
              slot-scope="scope">
            <el-button @click="updateOpen(scope.row)">编辑</el-button>
            <!--            <el-button @click="handleClick(scope.row)" type="primary">查看高级资料</el-button>-->
            <el-button @click="deleteById(scope.row)" type="danger">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="EmpBasicBottom">
      <el-button disabled type="danger">批量删除</el-button>
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
    <div class="EmpBasicHidden">
      <el-dialog width="90%" :title="dialogTitle" :visible.sync="dialogFormVisible">
        <el-form ref="addForm" :model="form" :rules="formRules">
          <el-form-item prop="name" label="姓名">
            <el-input placeholder="请输入员工姓名"
                      prefix-icon="el-icon-edit"
                      :size="fromSize" v-model="form.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="gender" label="性别">
            <el-radio-group v-model="form.gender">
              <el-radio label="男">男</el-radio>
              <el-radio label="女">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="birthday" label="出生日期">
            <el-date-picker :size="fromSize"
                            v-model="form.birthday"
                            align="right"
                            type="date"
                            placeholder="出生日期"
                            :picker-options="pickerOptions">
            </el-date-picker>
          </el-form-item>
          <el-form-item prop="politicId" label="政治面貌">
            <el-select :size="fromSize" v-model="form.politicId" placeholder="政治面貌">
              <el-option
                  v-for="item in politics"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="nationId" label="民族">
            <el-select :size="fromSize" v-model="form.nationId" placeholder="请选择民族">
              <el-option
                  v-for="item in nations"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="nativePlace" label="籍贯">
            <el-input :size="fromSize" v-model="form.nativePlace" placeholder="员工籍贯(省)" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="email" label="电子邮箱">
            <el-input prefix-icon="el-icon-message" :size="fromSize" v-model="form.email" placeholder="电子邮箱"
                      autocomplete="off">
            </el-input>
          </el-form-item>
          <el-form-item prop="address" label="联系地址">
            <el-input :size="fromSize" v-model="form.address" placeholder="联系地址" prefix-icon="el-icon-edit"
                      autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="posId" label="职位">
            <el-select :size="fromSize" v-model="form.posId" placeholder="请选择职位">
              <el-option
                  v-for="item in positions"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="jobLevelId" label="职称">
            <el-select :size="fromSize" v-model="form.jobLevelId" placeholder="请选择职称">
              <el-option
                  v-for="item in jobLevels"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="departmentId" label="所属部门">
            <el-select :size="fromSize" v-model="form.departmentId" placeholder="请选择部门">
              <el-option
                  v-for="item in departments"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="phone" label="电话号码">
            <el-input prefix-icon="el-icon-phone" :size="fromSize" placeholder="电话号码" v-model="form.phone"
                      autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="workId" label="工号">
            <el-input disabled :size="fromSize" v-model="form.workId" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="tiptopDegree" label="学历">
            <el-select :size="fromSize" v-model="form.tiptopDegree" placeholder="最高学历">
              <el-option
                  v-for="(item,index) in degrees"
                  :label="item"
                  :value="item">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="school" label="毕业院校">
            <el-input prefix-icon="el-icon-edit" placeholder="毕业院校名称" :size="fromSize" v-model="form.school"
                      autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="specialty" label="专业名称">
            <el-input prefix-icon="el-icon-edit" placeholder="专业名称" :size="fromSize" v-model="form.specialty"
                      autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="beginDate" label="入职日期">
            <el-date-picker
                :size="fromSize"
                v-model="form.beginDate"
                type="date"
                placeholder="入职日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item prop="conversionTime" label="转正日期">
            <el-date-picker
                :size="fromSize"
                v-model="form.conversionTime"
                type="date"
                placeholder="转正日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item prop="beginContract" label="合同起始日期">
            <el-date-picker
                :size="fromSize"
                v-model="form.beginContract"
                type="date"
                placeholder="合同起始日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item prop="endContract" label="合同终止日期">
            <el-date-picker
                :size="fromSize"
                v-model="form.endContract"
                type="date"
                placeholder="合同终止日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item prop="idCard" label="身份证号码">
            <el-input placeholder="请输入身份证号" :size="fromSize" v-model="form.idCard" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="engageForm" label="聘用形式">
            <el-radio-group v-model="form.engageForm">
              <el-radio label="劳动合同">劳动合同</el-radio>
              <el-radio label="劳务合同">劳务合同</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="wedlock" label="婚姻状况">
            <el-radio-group v-model="form.wedlock">
              <el-radio label="未婚">未婚</el-radio>
              <el-radio label="已婚">已婚</el-radio>
              <el-radio label="离异">离异</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="dialogMethod">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  name: "EmpBasic",
  data() {
    let emailValidate = (rule, value, callback) => {
      if (value.indexOf('@') == -1 || value.indexOf('.') == -1) {
        callback(new Error('无效的邮箱地址'));
      } else {
        callback();
      }
    }
    let conversionTimeValidate = (rule, value, callback) => {
      if (!this.form.beginDate) callback(new Error('请先选择入职时间'));
      let conversionTime = new Date(value).getTime()
      let beginDate = new Date(this.form.beginDate).getTime()
      if (beginDate > conversionTime) {
        callback(new Error('转正时间需大于入职时间'));
      } else {
        callback();
      }
    }
    let endContractValidate = (rule, value, callback) => {
      if (!this.form.beginContract) callback(new Error('请先选择起始时间'));
      let endContract = new Date(value).getTime()
      let beginContract = new Date(this.form.beginContract).getTime()
      if (beginContract > endContract) {
        callback(new Error('起始时间需大于终止时间'));
      } else {
        callback();
      }
    }
    let dialogMethod = () => {
      if (this.dialogFlag == 'add') this.addSubmit();
      else if (this.dialogFlag == 'update') this.updateSubmit();
    }
    return {
      dialogTitle: '添加员工',
      dialogFlag: '',
      dialogMethod,
      tableData: [],
      transition: true,
      searchForm: {},
      uploadButtonMsg: '导入数据',
      uploadButtonIcon: 'el-icon-upload2',
      uploading: false,
      uri: '',
      currentIndex: 1,
      total: 0,
      pageSize: 10,
      prevAndNext: false,
      loading: false,
      keywords: {
        name: '',
      },
      searchLock: false,
      dialogFormVisible: false,
      degrees: ["小学", "初中", "高中", "大专", "本科", "硕士", "博士"],
      form: {
        name: "",
        gender: "",
        birthday: "",
        idCard: "",
        wedlock: "",
        nationId: "",
        nativePlace: "",
        politicId: "",
        email: "",
        phone: "",
        address: "",
        departmentId: "",
        jobLevelId: "",
        posId: "",
        engageForm: "",
        tiptopDegree: "",
        specialty: "",
        school: "",
        beginDate: "",
        workState: "在职",
        workId: "",
        contractTerm: "",
        conversionTime: "",
        notworkDate: "",
        beginContract: "",
        endContract: "",
        workAge: "",
      },
      formRules: {
        name: [
          {required: true, message: "请输入姓名", trigger: 'blur'},
          {min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur'}
        ],
        gender: {required: true, message: "请选择性别", trigger: 'blur'},
        birthday: {required: true, message: "不能为空", trigger: 'blur'},
        idCard: [
          {required: true, message: "不能为空", trigger: 'blur'},
          {min: 18, max: 18, message: '无效的身份证号', trigger: 'blur'}
        ],
        wedlock: {required: true, message: "不能为空", trigger: 'blur'},
        nationId: {required: true, message: "不能为空", trigger: 'blur'},
        nativePlace: {required: true, message: "不能为空", trigger: 'blur'},
        politicId: {required: true, message: "不能为空", trigger: 'blur'},
        email: [
          {required: true, message: "不能为空", trigger: 'blur'},
          {validator: emailValidate, trigger: 'blur'},
        ],
        phone: [{required: true, message: "不能为空", trigger: 'blur'},
          {min: 11, max: 11, message: '无效的手机号', trigger: 'blur'}],
        address: {required: true, message: "不能为空", trigger: 'blur'},
        departmentId: {required: true, message: "不能为空", trigger: 'blur'},
        jobLevelId: {required: true, message: "不能为空", trigger: 'blur'},
        posId: {required: true, message: "不能为空", trigger: 'blur'},
        engageForm: {required: true, message: "不能为空", trigger: 'blur'},
        tiptopDegree: {required: true, message: "不能为空", trigger: 'blur'},
        specialty: {required: true, message: "不能为空", trigger: 'blur'},
        school: {required: true, message: "不能为空", trigger: 'blur'},
        beginDate: {required: true, message: "不能为空", trigger: 'blur'},
        workState: {required: true, message: "不能为空", trigger: 'blur'},
        workId: {required: true, message: "不能为空", trigger: 'blur'},
        contractTerm: {required: true, message: "不能为空", trigger: 'blur'},
        conversionTime: [{required: true, message: "不能为空", trigger: 'blur'},
          {validator: conversionTimeValidate, trigger: 'blur'}],
        // notworkDate: {required: true, message: "不能为空", trigger: 'blur'},
        beginContract: {required: true, message: "不能为空", trigger: 'blur'},
        endContract: [{required: true, message: "不能为空", trigger: 'blur'},
          {validator: endContractValidate, trigger: 'blur'}],
        // workAge: {required: true, message: "不能为空", trigger: 'blur'},
      },
      nations: [],
      politics: [],
      departments: [],
      jobLevels: [],
      positions: [],
      fromSize: 'small',
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
    }
  },
  created() {
    this.init();
  },
  methods: {
    beforeUpload(file) {
      this.uploading = true
      this.uploadButtonMsg = '正在上传'
      this.uploadButtonIcon = 'el-icon-loading'
    },
    uploadSuccess(response, file, fileList) {
      this.uploadButtonMsg = "导入数据"
      this.uploadButtonIcon = "el-icon-upload2"
      if (response.status == 500) this.$message.error(response.msg)
      else this.$message.success('上传成功')

      this.uploading = false
    },
    uploadError(response, file, fileList) {
      this.uploadButtonMsg = "导入数据"
      this.uploadButtonIcon = "el-icon-upload2"
      this.$message.error('上传失败')
      this.uploading = false
    },
    exportData() {
      //https://www.runoob.com/jsref/met-win-open.html
      window.open('/employee/basic/export', '_parent')
    },
    highSearchOpen() {
      this.transition = !this.transition
      this.keywords.name = ''
      this.uri = ''
    },
    highSearchSubmit() {
      if (this.searchForm.time && !this.utils.isNull(this.searchForm.time)) {
        this.searchForm.beginDate = this.searchForm.time[0].getTime();
        this.searchForm.beginDateEnd = this.searchForm.time[1].getTime()
        delete this.searchForm.time;
      }
      this.uri = this.utils.getUri(this.searchForm)
      this.loading = true
      this.$axios.execute('get', '/employee/basic?' + this.uri).then(data => {
        if (data) {
          this.tableData = data.data
          this.total = data.total
          this.loading = false
        }
      })
    },
    deleteById(row) {
      let operation = () => this.$axios.execute('delete', '/employee/basic/' + row.id).then(data => {
        if (data) this.currentPage(this.currentIndex);
      })
      this.utils.deleteConfirm(operation)
    },
    addOpen() {
      // this.utils.toNull(this.form)
      this.form={"id":1,"name":"test","gender":"男","birthday":"1990-01-01T00:00:00.000+08:00","idCard":"610122199001011256","wedlock":"已婚","nation":{"id":null,"name":"汉族"},"nativePlace":"陕西","politicsStatus":{"id":null,"name":"普通公民"},"email":"","phone":"18565558897","address":"深圳市南山区","department":{"id":null,"name":"总办","parentId":null,"depPath":null,"enabled":null,"children":[],"affectRow":null,"parent":null},"jobLevel":null,"position":{"id":null,"name":"技术总监","createDate":null,"enabled":null},"engageForm":"劳务合同","tiptopDegree":"本科","specialty":"信息管理与信息系统","school":"深圳大学","beginDate":"2018-01-01T00:00:00.000+08:00","workState":"在职","workId":"00000001","contractTerm":2,"conversionTime":"2018-04-01T00:00:00.000+08:00","notWorkDate":null,"beginContract":"2018-01-01T00:00:00.000+08:00","endContract":"2020-01-01T00:00:00.000+08:00","workAge":null,"beginDateEnd":null,"nationId":1,"politicId":13,"departmentId":5,"jobLevelId":9,"posId":29,"salary":null}
      this.dialogTitle = '添加员工'
      this.dialogFlag = 'add'
      this.dialogFormVisible = true
      this.$axios.execute('get', '/employee/basic/workId').then(data => {
        if (data) this.form.workId = data
      })
    },
    addSubmit() {
      let start = new Date(this.form.beginContract).getFullYear()
      let start1 = new Date(this.form.beginContract).getMonth()
      let start2 = new Date(this.form.beginContract).getDay()
      let end = new Date(this.form.endContract).getFullYear()
      let end1 = new Date(this.form.endContract).getMonth()
      let end2 = new Date(this.form.endContract).getDay()
      let year = end - start;
      let month = ((end2 - end1) / 12)
      let day = ((end2 - start2) / 365)
      this.form.contractTerm = '' + (year + month + day)
      this.form.contractTerm = this.form.contractTerm.substr(0, this.form.contractTerm.indexOf('.') + 3)
      this.form.workState = '在职'
      this.$refs.addForm.validate((boolean, obj) => {
        if (boolean)
          this.$axios.execute('post', '/employee/basic', this.form).then(data => {
            if (data) {
              this.currentPage(this.currentIndex);
              this.dialogFormVisible = false
              this.utils.toNull(this.form)
            }
          })
      })
    },
    updateOpen(row) {
      this.dialogTitle = '修改员工'
      this.dialogFlag = 'update'
      this.dialogFormVisible = true
      this.form = this.utils.clone(row)
    },
    updateSubmit() {
      this.$axios.execute('put', '/employee/basic', this.form).then(data => {
        if (data) {
          this.currentPage(this.currentIndex);
          this.dialogFormVisible = false
          this.utils.toNull(this.form)
        }
      })
    },
    refresh() {
      this.initEmp()
      this.utils.toNull(this.keywords)
    },
    search() {
      if (this.searchLock || !this.keywords.name) {
        this.$refs.form.validate(() => {
        })
        return;
      }
      this.loading = true
      this.$axios.execute('get', '/employee/basic?name=' + this.keywords.name).then(data => {
        this.tableData = data.data
        this.total = data.total
        this.loading = false
      })
    },
    init() {
      this.initEmp()
      this.initNations()
      this.initPolitics()
      this.initDepartments()
      this.initJobLevels()
      this.initPositions()
    },
    initNations() {
      if (!window.sessionStorage.getItem('nations'))
        this.$axios.execute('get', '/employee/basic/nations').then(data => {
          if (data) {
            this.nations = data
            window.sessionStorage.setItem('nations', JSON.stringify(data))
          }
        })
      else this.nations = JSON.parse(window.sessionStorage.getItem('nations'))
    },
    initPolitics() {
      if (!window.sessionStorage.getItem('politics'))
        this.$axios.execute('get', '/employee/basic/politics').then(data => {
          if (data) {
            this.politics = data
            window.sessionStorage.setItem('politics', JSON.stringify(data))
          }
        })
      else this.politics = JSON.parse(window.sessionStorage.getItem('politics'))
    },
    initDepartments() {
      if (!window.sessionStorage.getItem('departments'))
        this.$axios.execute('get', '/employee/basic/departments').then(data => {
          if (data) {
            this.departments = data
            window.sessionStorage.setItem('departments', JSON.stringify(data))
          }
        })
      else this.departments = JSON.parse(window.sessionStorage.getItem('departments'))
    },
    initJobLevels() {
      if (!window.sessionStorage.getItem('jobLevels'))
        this.$axios.execute('get', '/employee/basic/jobLevels').then(data => {
          if (data) {
            this.jobLevels = data
            window.sessionStorage.setItem('jobLevels', JSON.stringify(data))
          }
        })
      else this.jobLevels = JSON.parse(window.sessionStorage.getItem('jobLevels'))
    },
    initPositions() {
      if (!window.sessionStorage.getItem('positions'))
        this.$axios.execute('get', '/employee/basic/positions').then(data => {
          if (data) {
            this.positions = data
            window.sessionStorage.setItem('positions', JSON.stringify(data))
          }
        })
      else this.positions = JSON.parse(window.sessionStorage.getItem('positions'))
    },
    initEmp() {
      this.loading = true
      this.$axios.execute('get', '/employee/basic').then(data => {
        if (data) {
          this.tableData = data.data
          this.total = data.total
          this.loading = false
        }
      })
    },
    currentPage(current) {
      this.currentIndex = current
      if (this.prevAndNext) return
      this.loading = true
      let url = '/employee/basic?page=' + current + '&size=' + this.pageSize + "&" + this.uri;
      if (this.keywords.name) url += '&name=' + this.keywords.name
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
      let url = '/employee/basic?page=' + '&size=' + size + "&" + this.uri;
      if (this.keywords.name) url += '&name=' + this.keywords.name
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
      let url = '/employee/basic?page=' + current + '&size=' + this.pageSize + "&" + this.uri;
      if (this.keywords.name) url += '&name=' + this.keywords.name
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
      let url = '/employee/basic?page=' + current + '&size=' + this.pageSize + "&" + this.uri;
      if (this.keywords.name) url += '&name=' + this.keywords.name
      this.$axios.execute('get', url).then(data => {
        if (data) {
          this.tableData = data.data
          this.total = data.total
          this.prevAndNext = false
          this.loading = false
        }
      })
    },
    handleClick(row) {
      console.log(row);
    }
  },
}
</script>

<style>
.EmpBasic {
  width: 97%;
}

.EmpBasicHeader {
  display: flex;
  justify-content: space-between;
}


.EmpBasicHeader .left .el-form {
  display: flex;
  /*overflow-y: hidden;*/
}

.el-form #firstItem {
  width: 270px;
}

.EmpBasicHeader .left .el-form .el-form-item {
  margin-right: 10px;
}

.EmpBasicHeader .right div:first-child {
  display: inline-flex;
  margin-right: 5px;
}

/*

.EmpBasicHeader .right {
  display: flex;
  padding: 0px;
  align-items: flex-start;
}
.EmpBasicHeader .right div{
  margin-right: 5px;
}
*/

/*过度内容开始*/

.EmpBasicHigh span {
  display: flex;
}

.EmpBasicHighOut {
  width: 100%;
}

.transitionBody .el-button {
  display: inline-flex;
  margin: 0px 0px 5px 10px;
}

.transitionBody .el-form {
  display: inline-flex;
  justify-content: space-between;
  flex-wrap: wrap;
  align-items: center;
}

.transitionBody .el-form .el-form-item {
  display: inline-flex;
  margin-left: 3px;
}

.transitionBody .el-form-item__content {
  display: flex;
  align-items: center;
}

.transitionBody .el-radio-group {
  display: flex;
}

.transitionBody .el-radio {
  display: flex;
}

.EmpBasicHighSearch-enter, .EmpBasicHighSearch-leave-to {
  transform: translateY(-50px);
  opacity: 0;
  z-index: -100;
}

.EmpBasicHighSearch-enter-active, .EmpBasicHighSearch-leave-active {
  transition: all .8s ease;
  z-index: -100;
}

/*过度内容结束*/

/*如果style为scoped,无法覆盖原有的样式*/
.EmpBasicBody .el-table--border th.gutter:last-of-type {
  /*!important在IE6以上被!important标记的属性拥有更高的优先级
    可以覆盖原本的css样式
    此元素将显示为块级元素，此元素前后会带有换行符。*/
  display: block !important;
  width: 17px !important;
}

.EmpBasicBody td:last-child .cell {
  display: flex;
}

.EmpBasicBody .cell .el-button {
  padding: 5px;
}

.EmpBasicBody .cell span {
  font-size: 12px;
}

.EmpBasicBottom {
  margin-top: 5px;
  display: flex;
  justify-content: space-between;
}

.EmpBasicHidden .el-dialog .el-form {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
}

.EmpBasicHidden .el-dialog .el-form .el-form-item {
  display: flex;
  width: 25%;
  font-size: 12px;
  /*border-box:指定宽度和高度（最小/最大属性）确定元素边框。也就是说，
  对元素指定宽度和高度包括了 padding 和 border 。通过从已设定的宽度和高度分别减去边框和内边距才能得到内容的宽度和高度。*/
  box-sizing: border-box;
}

.EmpBasicHidden .el-dialog .el-form .el-form-item .el-form-item__label {
  font-size: 13px;
}

.EmpBasicHidden .el-dialog .el-form .el-form-item .el-input__inner {
  width: 220px;
}
</style>