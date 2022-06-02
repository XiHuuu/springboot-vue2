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
          <el-button type="warning" size="small" autocomplete="off">注册</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

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
            if (!res){
              this.$message.error("用户名或密码错误")
            }else {
              this.$router.push("/")
            }
          })
        }else {
          return false;
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