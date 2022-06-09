<template>
  <el-container style="height: 100vh">
    <el-aside :width="sideWidth+'px'" style="background-color: rgb(238, 241, 246);">
      <Aside :isCollapse="isCollapse"></Aside>
    </el-aside>

    <el-container>
      <el-header style="border-bottom: 1px solid #ccc;">
        <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse" :user="user"></Header>
      </el-header>

      <el-main>
<!--        当前页面的子路由在router-view展示-->
        <router-view @refreshUser="getUser"></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
// @ is an alias to /src
import request from "@/utils/request";
import Aside from "@/components/Aside";
import Header from "@/components/Header";

export default {
  name: 'Manage',
  data(){
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      user: {}
    }
  },
  components: {
    Aside,
    Header
  },
  created() {
    //从后台获取最新的User数据
    this.getUser()
  },
  methods: {
    collapse(){
      this.isCollapse = !this.isCollapse
      if (this.isCollapse){
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
      }else {
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
      }
    },
    getUser() {
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      this.request.get("/user/username/" + username).then(res=>{
        this.user=res.data
      })
    },
  }
}
</script>


