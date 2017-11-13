package com.jt.sso.mapper;

import com.jt.common.mapper.SysMapper;
import com.jt.sso.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends SysMapper<User> {


    int findCheckUser(@Param("cloumn") String cloumn, @Param("param") String param);

    //根据用户名和密码查询用户信息
    User findUSerByU_P(@Param("username") String username, @Param("password") String password);
}
