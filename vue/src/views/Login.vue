<template>
  <div class="wrapper">
    <div style="margin: 200px auto;background-color: #fff;width: 400px;height: 300px;padding: 20px;border-radius: 10px">
      <div style="margin: 20px 0;text-align: center;font-size: 24px"><b>登 录</b></div>
      <el-form :model="user" :rules="rulers" ref="userForm">
        <el-form-item prop="username">
          <el-input  size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        </el-form-item>
        <div style="margin: 10px 0;text-align: right">
          <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import {setRoutes} from "@/router";

export default {
  name: "Login",
  data(){
    return{
      user: {},
      rulers: {
        username: [
          {required: true,message: '请输入用户名',trigger: 'blur'},
          {min: 3,max: 10,message: '长度3到10个字符',trigger: 'blur'}
        ],
        password: [
          {required: true,message: '请输入密码',trigger: 'blur'},
          {min: 1,max: 16,message: '长度1到16个字符',trigger: 'blur'}
        ]
      }
    }
  },
  methods:{
    login(){
      this.$refs['userForm'].validate((valid)=>{
        if (valid){//表单校验合法
          request.post("/user/login",this.user).then(res =>{
            if (res.code === '200'){
              localStorage.setItem("user",JSON.stringify(res.data))//存储用户信息到浏览器
              localStorage.setItem("menus",JSON.stringify(res.data.menus))//存储用户信息到浏览器
              //动态设置当前用户的路由
              setRoutes()
              this.$router.push("/")
              this.$message.success("登录成功")
            }else {
              this.$message.error(res.msg)
            }
          })
        }
      })

    }
  }
}
</script>

<style scoped>
  .wrapper{
    height: 100vh;
    background-image: linear-gradient(to bottom right,#FC466B,#3F5EFB);
    overflow: hidden;
  }
</style>