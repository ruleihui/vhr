<template>
  <div class="position">
    <el-input
        size="small"
        style="width: 30%;margin-right: 10px"
        placeholder="添加职位"
        prefix-icon="el-icon-plus"
        v-model="position.name">
    </el-input>
    <el-button icon="el-icon-plus" type="primary" @click="add" size="small">添加</el-button>
    <br/>
    <el-table
        :data="positions"
        stripe
        :border="true"
        size="small"
        @selection-change="selected"
    >
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column
          prop="id"
          label="编号"
          width="55"
      >
      </el-table-column>
      <el-table-column
          prop="name"
          label="职位名称"
          width="180"
      >
      </el-table-column>
      <el-table-column
          prop="createDate"
          label="创建时间"
          :formatter="this.utils.tableDateFormat"
          width="180"
      >
      </el-table-column>
      <el-table-column
          prop="enabled"
          label="是否启用"
          width="80"
      >
        <template v-slot:default="scope">
          <el-tag v-if="scope.row.enabled" type="success">已启用</el-tag>
          <el-tag v-else type="danger">未启用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" class-name="last-column">
        <template slot-scope="scope">
          <el-button
              size="mini"
              @click="update(scope.$index, scope.row)">编辑
          </el-button>
          <el-button
              size="mini"
              type="danger"
              @click="deleteRow(scope.$index, scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-button style="margin-top: 10px" type="danger" @click="deleteMulti" ref="delMulti" :disabled="disabled">批量删除
    </el-button>
  </div>
</template>

<script>
import PositionUpdate from "@/components/sys/basic/update/PositionUpdate";

export default {
  //职位管理
  name: "PositionManage",
  components: {PositionUpdate},
  mounted() {
    this.init();
  },
  data: function () {
    return {
      position: {
        name: '',
      },
      temp: '',
      positions: [],
      updateFrom: {},
      updateFromTemp: {},
      selectedAll: [],
      disabled: true,
    }
  },
  methods: {
    selected(select) {
      if (this.disabled) this.disabled = false;
      if (select.length == 0) this.disabled = true;
      this.selectedAll = select;
    },
    deleteMulti() {
      let args = this.selectedAll.map(it=>it.id).join('&ids=');
      this.$axios.execute('delete', '/sys/basic/position?ids=' + args).then(data => {
        if (data) {
          this.disabled = true
          this.init()
        }
      })
    },
    init: function () {
      this.$axios.execute('get', '/sys/basic/position').then(data => {
        if (data)
          this.positions = data
      })
    },
    add: function () {
      if (this.position.name && (!this.temp || this.temp != this.position.name))
        this.$axios.executeArgs('post', '/sys/basic/position', this.position)
            .then(data => {
              if (data) {
                //刷新
                this.init()
                this.temp = this.position.name
              } else this.temp = this.position.name
            });
      else this.$message.error('请设置/修改提交值')
    },
    updateSubmit(action, instance, done) {
      if (!this.updateFrom.name && action != 'cancel') return
      if (action === 'confirm') {
        instance.confirmButtonLoading = true;
        instance.confirmButtonText = '执行中...';
        if (this.updateFrom.name != this.updateFromTemp.name||
            this.updateFrom.enabled != this.updateFromTemp.enabled)
          this.$axios.executeArgs('put', '/sys/basic/position', this.updateFrom)
              .then(data => {
                if (data) {
                  done();
                  setTimeout(() => {
                    instance.confirmButtonLoading = false;
                    this.init()
                  }, 300);
                }
              }).catch(reason => {
            done();
          })
        else {
          this.$message.error("请先修改")
          instance.confirmButtonLoading = false;
          instance.confirmButtonText = '确定';
        }
      } else {
        done();
      }
    },
    update(index, row) {
      this.updateFrom = this.utils.clone(row)
      console.log(index, this.updateFrom)
      this.updateFromTemp.name = this.updateFrom.name
      this.updateFromTemp.enabled = this.updateFrom.enabled
      //或者使用dialog对话框
      this.$msgbox({
        title: '修改名称',
        //node_modules/vue/types/vue.d.ts#CreateElement
        //node_modules/vue/types/options.d.ts#Component
        message: this.$createElement(
            //创建一个组件
            {
              render: (createElement, context) => {
                return <position-update index={index} row={this.updateFrom}/>
              }
            }
        ),
        center: true,
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        beforeClose: this.updateSubmit
      }).then(action => {
        console.log(action)
      }).catch(reason => {
        console.log(reason)
      })
    },
    deleteRow(index, row) {
      let operation = () => this.$axios.execute('delete', '/sys/basic/position/' + row.id,).then(data => {
        if (data) {
          this.init()
        }
      })
      this.utils.deleteConfirm(operation)
    }
  }
}
</script>

<style>
.position .el-table {
  margin-top: 10px;
  width: 70%
}
.position .el-table--border th.gutter:last-of-type {
  /*!important在IE6以上被!important标记的属性拥有更高的优先级
    可以覆盖原本的css样式
    此元素将显示为块级元素，此元素前后会带有换行符。*/
  display: block !important;
  width: 17px !important;
}

.position .el-table th:nth-last-child(2) {
  border-right: none;
}
</style>