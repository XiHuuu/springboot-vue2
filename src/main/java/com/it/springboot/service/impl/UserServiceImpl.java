package com.it.springboot.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.it.springboot.common.Constants;
import com.it.springboot.controller.dto.UserDTO;
import com.it.springboot.controller.dto.UserPasswordDTO;
import com.it.springboot.entity.Menu;
import com.it.springboot.entity.User;
import com.it.springboot.exception.ServiceException;
import com.it.springboot.mapper.UserMapper;
import com.it.springboot.service.IMenuService;
import com.it.springboot.service.IRoleMenuService;
import com.it.springboot.service.IRoleService;
import com.it.springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.springboot.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author XiHu
 * @since 2022-05-31
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG = Log.get();

    @Resource
    private IRoleService roleService;

    @Resource
    private IMenuService menuService;

    @Override
    public UserDTO login(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if (one != null){
            BeanUtils.copyProperties(one,userDTO);
            //设置token
            String token = TokenUtils.getToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);

            String role = one.getRole();//ROLE_ADMIN
            //设置用户的菜单列表
            Integer roleId = roleService.selectByFlag(role);
            List<Menu> roleMenus = getRoleMenus(roleId);
            userDTO.setMenus(roleMenus);
            return userDTO;
        }else {
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
    }

    @Override
    public User register(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if (one == null){
            one = new User();
            BeanUtils.copyProperties(userDTO,one);
            save(one);
        }else {
            throw new ServiceException(Constants.CODE_600,"用户已存在");
        }
        return one;
    }

    @Override
    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        int update = getBaseMapper().updatePassword(userPasswordDTO);
        if (update < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
    }

    private User getUserInfo(UserDTO userDTO){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userDTO.getUsername());
        queryWrapper.eq("password",userDTO.getPassword());
        User one;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return one;
    }

    /**
     * 获取当前角色的菜单列表
     * @param roleId
     * @return
     */
    private List<Menu> getRoleMenus(Integer roleId){
        //当前角色的所有菜单id集合
        List<Integer> menuIds = roleService.getRoleMenu(roleId);
        //查出系统所有的菜单
        List<Menu> menus = menuService.findMenus("");
        //new一个最后筛选好的list
        List<Menu> roleMenus = new ArrayList<>();
        //筛选用户角色菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())){
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            children.removeIf(child-> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}
