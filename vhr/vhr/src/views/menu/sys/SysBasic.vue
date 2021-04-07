<template>
  <div class="SysBasic">
    <el-tabs v-model="activeName" type="card">
      <template v-for="item in items">
        <el-tab-pane :label="item.label" :name="item.label">
          <div :is="item.is" v-if="item.label==activeName"></div>
        </el-tab-pane>
      </template>
    </el-tabs>
  </div>
</template>

<script>
import DepManage from '@/components/sys/basic/DepManage'
import PositionManage from '@/components/sys/basic/PositionManage'
import JobLevelManage from '@/components/sys/basic/JobLevelManage'
import RewardsRule from '@/components/sys/basic/RewardsRule'
import PermissionGroup from '@/components/sys/basic/PermissionGroup'

export default {
  name: "SysBasic",
  // components: {DepManage, PositionManage, RewardsRule, JobLevelManage, PermissionGroup},
  components: {DepManage, PositionManage, JobLevelManage, PermissionGroup},
  data: function () {
    return {
      activeName: '部门管理',
      items: [
        {label: '部门管理', is: 'DepManage'},
        {label: '职位管理', is: 'PositionManage'},
        // {label: '奖惩规则', is: 'RewardsRule'},
        {label: '职称管理', is: 'JobLevelManage'},
        {label: '权限组', is: 'PermissionGroup'},
      ]
    }
  }
}
</script>

<style scoped>

</style>