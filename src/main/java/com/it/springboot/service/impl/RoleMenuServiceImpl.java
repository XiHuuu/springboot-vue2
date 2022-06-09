package com.it.springboot.service.impl;

import com.it.springboot.entity.RoleMenu;
import com.it.springboot.mapper.RoleMenuMapper;
import com.it.springboot.service.IRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色菜单关系表 服务实现类
 * </p>
 *
 * @author XiHu
 * @since 2022-06-04
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

}
