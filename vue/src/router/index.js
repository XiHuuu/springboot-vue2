import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: ()=>import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: ()=>import('../views/Register.vue')
  },
  {
    path: '/404',
    name: '404',
    component: ()=>import('../views/404.vue')
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

//重置路由方法
export const resetRouter= () =>{
  router.matcher = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}


export const setRoutes = ()=>{
    const storeMenus = localStorage.getItem("menus");
    if (storeMenus){

      const currentRouteNames = router.getRoutes().map(v => v.name)
      if (!currentRouteNames.includes('Mange')){
        //拼装动态路由
        const manageRoute = {path: '/',name: 'Mange', component: ()=>import('../views/Manage.vue'), redirect: "/home", children:[
            {path:'person',name: '个人信息',component: ()=> import('../views/Person.vue')},
            {path:'password',name: '修改密码',component: ()=> import('../views/Password.vue')}
          ]}
        const menus = JSON.parse(storeMenus)
        menus.forEach(item => {
          if (item.path){
            let itemMenu = {path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
            manageRoute.children.push(itemMenu)
          }else if (item.children.length) {
            item.children.forEach(item=>{
              if (item.path){
                let itemMenu = {path: item.path.replace("/",""),name: item.name,component: () => import('../views/'+item.pagePath +'.vue')}
                manageRoute.children.push(itemMenu)
              }
            })
          }
        })
        //动态添加到现在的路由对象中
        router.addRoute(manageRoute)
      }
    }
}

setRoutes()

//路由守卫
router.beforeEach((to,from,next)=>{
  localStorage.setItem("currentPathName",to.name) //设置当前路由名称
  //未找到路由的情况
  if (!to.matched.length){
    const storeMenus = localStorage.getItem("menus")
    if (storeMenus){
      next("/404")
    }else {
      //跳回登录
      next("/login")
    }
  }
  next()

})

export default router
