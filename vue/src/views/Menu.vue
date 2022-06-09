<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button type="primary" class="ml-5" @click="load">搜索</el-button>
      <el-button type="warning" class="ml-5" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd()">新增<i class="el-icon-circle-plus-outline ml-5"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除<i class="el-icon-remove-outline ml-5"></i></el-button>
      </el-popconfirm>
    </div>

    <el-table :data="tableData" border stripe @selection-change="handleSelectionChange"
      row-key="id" default-expand-all>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="名称" width="140"></el-table-column>
      <el-table-column prop="path" label="路径"></el-table-column>
      <el-table-column prop="pagePath" label="页面路径"></el-table-column>
      <el-table-column prop="icon" label="图标" width="100" align="center">
        <template slot-scope="scope">
          <i :class="scope.row.icon" style="font-size: 25px"></i>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column label="操作" width="320">
        <template slot-scope="scope">
          <el-button type="primary" @click="handleAdd(scope.row.id)" v-if="!scope.row.pid && !scope.row.path">新增子菜单<i class="el-icon-plus ml-5"></i></el-button>
          <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit ml-5"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline ml-5"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="35%">
      <el-form label-width="130px" :model="form">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off" style="width: 300px"></el-input>
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="form.path" autocomplete="off" style="width: 300px"></el-input>
        </el-form-item>
        <el-form-item label="页面路径">
          <el-input v-model="form.pagePath" autocomplete="off" style="width: 300px"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-select clearable v-model="form.icon" placeholder="请选择" style="width: 70%">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
              <i :class="item.value">{{item.name}}</i>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off" style="width: 300px"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Menu",
  data(){
    return{
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      multipleSelection: [],
      dialogFormVisible: false,
      options: []
    }
  },
  created() {
    this.load()
  },
  methods:{
    //分页查询
    load(){
      request.get("/menu",{
        params:{
          name: this.name,
        }
      }).then(res=>{
        this.tableData=res.data
      })
    },
    //新增或修改
    save(){
      request.post("/menu",this.form).then(res=>{
        if (res.code === '200'){
          this.$message.success("保存成功")
          this.dialogFormVisible=false
          this.load()
        }else {
          this.$message.error("保存失败")
        }
      })
    },
    //开启修改弹窗
    handleEdit(row){
      this.form=Object.assign({},row)
      this.dialogFormVisible = true

      request.get("/menu/icons").then(res=>{
        this.options = res.data
      })
    },
    //弹窗取消
    cancel(){
      this.dialogFormVisible = false
      this.load()
    },
    //删除
    del(id){
      request.delete("/menu/"+id).then(res=>{
        if (res.code === '200'){
          this.$message.success("删除成功")
          this.load()
        }else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val){
      this.multipleSelection = val;
    },
    //批量删除
    delBatch(){
      let ids = this.multipleSelection.map(v=>v.id)
      request.post("/menu/del/batch",ids).then(res=>{
        if (res.code === '200'){
          this.$message.success("批量删除成功")
          this.load()
        }else {
          this.$message.error("批量删除失败")
        }
      })
    },
    //开启新增弹窗
    handleAdd(pid){
      this.dialogFormVisible=true
      this.form={}
      if (pid){
        this.form.pid = pid
      }
    },
    //搜索框重置
    reset(){
      this.name = ""
      this.load()
    },
    handleSizeChange(pageSize){
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      this.pageNum = pageNum
      this.load()
    },
  }
}
</script>

<style scoped>

</style>