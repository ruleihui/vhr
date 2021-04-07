<template>
  <el-form status-icon ref="form" :model="row"  label-width="80px">

    <el-form-item label="职位名称"  prop="name" :rules="{required:true,message:'请输入修改值',trigger:'blur'}">
      <el-input v-model="row.name"  size="small"></el-input>
    </el-form-item>
    <el-form-item  label="是否启用">
      <el-radio-group v-model="row.enabled">
        <el-radio :label="true">启用</el-radio>
        <el-radio :label="false">禁用</el-radio>
      </el-radio-group>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  name: "PositionUpdate",
  props: {
    index: Number,
    row: Object
  },
  data: function () {
    return {
      form: {
        id: '',
        name: '',
        createDate: '',
        enabled: '',
      }
    }
  },
}
</script>

<style scoped>

.el-form-item{
  line-height:0;
}

.el-radio-group {
  float: left;
  transform: translateY(80%);
  position: relative;
}
</style>