<template>
  <div class="department">
    <div class="departmentTree">
      <el-input
          prefix-icon="el-icon-search"
          placeholder="根据部门名称进行搜索"
          v-model="filterText">
      </el-input>

      <el-tree
          :expand-on-click-node="false"
          class="filter-tree"
          :data="treeData"
          :props="defaultProps"
          :filter-node-method="filterNode"
          ref="tree">
        <div class="custom-tree-node" slot-scope="{ node, data }">
          <span>{{ node.label }}</span>
          <span>
          <el-button
              type="primary"
              size="small"
              @click="() => append(data)">
            添加部门
          </el-button>
          <el-button
              type="danger"
              size="small"
              @click="() => remove(node,data)">
            删除部门
          </el-button>
        </span>
        </div>
      </el-tree>
      <div class="departmentHidden">
        <el-dialog title="添加部门" :visible.sync="dialogFormVisible">
          <div>
            <el-tag>上级部门</el-tag>
            {{ currentNodeData.name }}
          </div>
          <el-form ref="form" :model="model">
            <el-form-item>
              <el-tag>部门名称</el-tag>
            </el-form-item>
            <el-form-item prop="addName"
                          :rules="{required:true,message:'不能为空',trigger:'blur'}"
            >
              <el-input placeholder="请输入部门名称" size="mini" v-model="model.addName" autocomplete="off"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="appendSubmit">确 定</el-button>
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "DepManage",
  mounted() {
    this.init()
  },
  data() {
    return {
      filterText: '',
      treeData: [],
      defaultProps: {
        //tree需要的变量名:treeData中对象对应的变量名
        children: 'children',
        label: 'name'
      },
      dialogFormVisible: false,
      model: {addName: '',},
      currentNodeData: {},
    }
  },
  watch: {
    filterText(val) {
      //filter方法 等于 tree的filter-node-method属性中定义的方法
      //这里也就是filterNode
      this.$refs.tree.filter(val);
    }
  },
  methods: {
    init() {
      this.$axios.execute('get', '/system/basic/department').then(data => {
        if (data) this.treeData = data;
      })
    },
    //data表示treeData中的一个json对象
    //treeData中的数据对象会按照顺序挨个作为data参数传入调用这个方法
    //顺序是先传入自身,如果存在children数组,遍历数组中的数据挨个传入调用,
    //数组遍历结束后将下一个同级节点作为data传入调用,调用结束后,
    //如果存在children数组,遍历数组中的数据挨个传入调用
    //children数组的元素如果也存在children,按照上述流程继续,直到所有的
    //节点都被调用过一次
    //如果根据这个data节点返回true,这个data节点和它的直属上级节点都将展开
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    append(data) {
      this.currentNodeData = data;
      this.dialogFormVisible = true;
    },
    appendSubmit() {
      if (this.utils.isNull(this.model.addName)) {
        this.$refs.form.validate(() => {
        })
        return
      }
      this.$axios.executeArgs('post', '/system/basic/department',
          {
            name: this.model.addName,
            parentId: this.currentNodeData.id,
          }).then(data => {
        if (data) {
          this.dialogFormVisible = false
          let temp = this.currentNodeData.children
          if (temp instanceof Array) temp.push(data)
          else temp = [data]
        }
      })
    },
    remove(node) {
      if (!this.utils.isNull(node.data.children)) {
        this.$message.error("删除失败,存在子部门");
        return;
      }
      let operation = () => this.$axios.execute('delete', '/system/basic/department/' + node.data.id).then(data => {
        if (data) {
          node.remove(node)
        }
      })
      this.utils.deleteConfirm(operation)
    },
  },
}
</script>

<style scoped>
.department .departmentTree {
  width: 50%;
}

.department .departmentTree .custom-tree-node {
  /*按照父标签宽高作为分配宽高的上限*/
  /*flex 属性是 flex-grow、flex-shrink 和 flex-basis 属性的简写属性。*/
  /*第一个参数表示: flex-grow 定义项目的放大比例，默认为0，即如果存在剩余空间，也不放大*/
  /*第二个参数表示: flex-shrink 定义了项目的缩小比例，默认为1，即如果空间不足，该项目将缩小*/
  /*第三个参数表示: flex-basis给上面两个属性分配多余空间之前, 计算项目是否有多余空间, 默认值为 auto, 即标签内容本身的大小*/
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
}

.department .departmentTree .filter-tree {
  margin-top: 10px;
}

.department .departmentTree .custom-tree-node button {
  padding: 5px 15px;
}

.department .departmentTree .custom-tree-node button:last-child {
  margin-right: 5px;
}

.department .el-form {
  display: flex;
  justify-content: flex-start;
}

.department .el-form .el-form-item {
  margin-right: 5px;
}

/*与上面的写法等价*/
/*.department .el-form{
/*  display: inline-block;
/*}
/*.department .el-form .el-form-item{
/*  float: left;
/*  margin-right: 5px;
   }*/
</style>