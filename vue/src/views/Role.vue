<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button type="primary" class="ml-5" @click="load">搜索</el-button>
      <el-button type="warning" class="ml-5" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline ml-5"></i></el-button>
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

    <el-table :data="tableData" border stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="名称" width="140"></el-table-column>
      <el-table-column prop="flag" label="唯一标识" width="140"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column label="操作" width="290">
        <template slot-scope="scope">
          <el-button type="info" @click="selectMenu(scope.row)">分配菜单<i class="el-icon-menu ml-5"></i></el-button>
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

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="pageSize"
          :current-page="pageNum"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="唯一标识">
          <el-input v-model="form.flag" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="菜单分配" :visible.sync="menuDialogVisible" width="30%">
      <el-tree
        :props="props"
        :data="menuData"
        show-checkbox
        node-key="id"
        ref="tree"
        :default-expanded-keys="expends"
        :default-checked-keys="checks"
      >
      <span class="custom-tree-node" slot-scope="{node,data}">
        <span><i :class="data.icon"></i>  {{data.name}}</span>
      </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Role",
  data(){
    return{
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      menuDialogVisible: false,
      multipleSelection: [],
      dialogFormVisible: false,
      menuData: [],
      props: {
        label: 'name'
      },
      expends: [],
      checks: [],
      roleId: 0,
      roleFlag: '',
    }
  },
  created() {
    this.load()
  },
  methods:{
    //分页查询
    load(){
      request.get("/role/page",{
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res=>{
        this.tableData=res.data.records
        this.total=res.data.total
      })
    },
    //新增或修改
    save(){
      request.post("/role",this.form).then(res=>{
        if (res.code === '200'){
          this.$message.success("保存成功")
          this.dialogFormVisible=false
          this.load()
        }else {
          this.$message.error("保存失败")
        }
      })
    },
    saveRoleMenu(){
      request.post("/role/roleMenu/"+this.roleId,this.$refs.tree.getCheckedKeys()).then(res=>{
        if (res.code === '200'){
          this.$message.success("绑定成功")
          this.menuDialogVisible = false
          if(this.roleFlag === 'ROLE_ADMIN'){
            this.$store.commit("logout")
          }
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    //开启修改弹窗
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    //弹窗取消
    cancel(){
      this.dialogFormVisible = false
      this.load()
    },
    //删除
    del(id){
      request.delete("/role/"+id).then(res=>{
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
      request.post("/role/del/batch",ids).then(res=>{
        if (res.code === '200'){
          this.$message.success("批量删除成功")
          this.load()
        }else {
          this.$message.error("批量删除失败")
        }
      })
    },
    //开启新增弹窗
    handleAdd(){
      this.dialogFormVisible=true
      this.form={}
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
    selectMenu(role){
      this.menuDialogVisible = true
      this.roleId = role.id
      this.roleFlag = role.flag
      //请求菜单数据
      request.get("/menu").then(res=>{
        this.menuData=res.data

        //把后台返回的菜单数据处理成id数组
        this.expends = this.menuData.map(v => v.id)
      })

      request.get("/role/roleMenu/"+this.roleId).then(res=>{
        //this.menuDialogVisible = true
        this.checks=res.data

        request.get("/menu/ids").then(r=>{
          const ids = r.data
          ids.forEach(id =>{
            if (!this.checks.includes(id)){
              this.$refs.tree.setChecked(id,false)
            }
          })
          this.menuDialogVisible = true
        })
      })
    },
  }
}
</script>

<style scoped>

</style>