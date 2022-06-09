package com.it.springboot.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.it.springboot.entity.Menu;
import com.it.springboot.entity.Role;
import com.it.springboot.entity.RoleMenu;
import com.it.springboot.mapper.RoleMapper;
import com.it.springboot.service.IMenuService;
import com.it.springboot.service.IRoleMenuService;
import com.it.springboot.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author XiHu
 * @since 2022-06-04
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Resource
    private IRoleMenuService roleMenuService;

    @Resource
    private IMenuService menuService;

    @Override
    @Transactional
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
//        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("role_id",roleId);
//        getBaseMapper().delete(queryWrapper);

        //先删除当前角色id所有的绑定关系
        getBaseMapper().deleteByRoleId(roleId);

        //再把前端传过来的菜单id数组绑定到当前的这个角色id上去
        List<Integer> menuIdsCopy = CollUtil.newArrayList(menuIds);
        for (Integer menuId: menuIds){
            Menu menu = menuService.getById(menuId);
            if (menu.getPid() != null && !menuIdsCopy.contains(menu.getPid())){ //二级菜单 并且menuId数组里没有它的父级id
                //就补上父级id
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menu.getPid());
                roleMenuService.getBaseMapper().insert(roleMenu);
                menuIdsCopy.add(menu.getPid());
            }
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuService.getBaseMapper().insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return getBaseMapper().selectByRoleId(roleId);
    }

    @Override
    public Integer selectByFlag(String role) {
         return getBaseMapper().selectByFlag(role);
    }
}
