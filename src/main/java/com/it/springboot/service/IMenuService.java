package com.it.springboot.service;

import com.it.springboot.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author XiHu
 * @since 2022-06-04
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
