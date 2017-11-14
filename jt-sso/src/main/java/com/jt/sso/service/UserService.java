package com.jt.sso.service;

import com.jt.sso.pojo.User;

public interface UserService {
    Boolean findCheckUser(String param, int type);

    //入库操作
    String saveUser(User user);

    //用户的登录操作
    String findLogin(String username, String password);

    //根据ticket查询用户信息
    String findUserByTicket(String ticket);
}
