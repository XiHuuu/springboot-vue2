package com.it.springboot.controller.dto;

import com.it.springboot.entity.Menu;
import lombok.Data;

import java.util.List;

/*
* 接受前端登录请求的参数
* */
@Data
public class UserDTO {
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menus;
}
