package com.jt.web.service;

import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;

public interface UserSerivce {
    //实现用户的注册功能
    SysResult doRegister(User user);

    //用户的登录
    SysResult doLogin(String username, String password);
}
