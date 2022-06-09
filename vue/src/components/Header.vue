<template>
  <div style="display: flex;line-height: 60px;">
    <div style="flex: 1;font-size: 16px">
      <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"></span>
<!--        页签-->
        <el-breadcrumb separator="/" style="display: inline-block;margin-left: 15px">
          <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{currentPathName}}</el-breadcrumb-item>
        </el-breadcrumb>
    </div>
    <div style="text-align: right;font-size: 12px;width: 200px">
      <el-dropdown style="cursor: pointer">
        <div style="display: inline-block">
          <img :src="user.avatarUrl" style="width: 30px;border-radius: 50%;position: relative;top: 10px;right: 5px" alt="">
          <span>{{user.nickname}}</span>
          <i class="el-icon-arrow-down" style="margin-left: 8px"></i>
        </div>
        <el-dropdown-menu slot="dropdown" style="width: 100px;text-align: center">
          <el-dropdown-item style="font-size: 14px; padding: 5px 0">
            <router-link to="/person">个人信息</router-link>
          </el-dropdown-item>
          <el-dropdown-item style="font-size: 14px; padding: 5px 0">
            <router-link to="/password">修改密码</router-link>
          </el-dropdown-item>
          <el-dropdown-item style="font-size: 14px;padding: 5px 0"x>
            <span style="text-decoration: none" @click="logout">退出</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
export default {
  name: "Header",
  props:{
    collapseBtnClass: String,
    collapse: Function,
    user: Object
  },
  created() {
    this.currentPathName = localStorage.getItem("currentPathName")
  },
  watch:{ //监听路由变化
    '$route': function (){
      this.currentPathName = localStorage.getItem("currentPathName") //取出route里面设置的路由信息
    }
  },
  data(){
    return{
      currentPathName: '',
    }
  },
  methods:{
    logout(){
      this.$store.commit("logout")
      localStorage.removeItem("user")
      localStorage.removeItem("menus")
      this.$message.success("退出成功")
    }
  }
}
</script>

<style scoped>

</style>