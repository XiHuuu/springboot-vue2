package com.it.springboot.service;

import com.it.springboot.controller.dto.UserDTO;
import com.it.springboot.controller.dto.UserPasswordDTO;
import com.it.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author XiHu
 * @since 2022-05-31
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);

    void updatePassword(UserPasswordDTO userPasswordDTO);
}
