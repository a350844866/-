package com.jt.sso.controller;

import com.jt.common.util.CookieUtils;
import com.jt.common.vo.SysResult;
import com.jt.sso.pojo.User;
import com.jt.sso.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private static final Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private JedisCluster jedisCluster;


    /**
     * 参数类型 1 username、2 phone、3 email
     *
     * @return
     */
    @RequestMapping("/check/{param}/{type}")
    @ResponseBody
    public Object findCheckUser(@PathVariable String param, @PathVariable int type, String callback) {
        //true表示已经存在,false表示可以使用
        Boolean checkResult = userService.findCheckUser(param, type);
        MappingJacksonValue jacksonValue = new MappingJacksonValue(SysResult.oK(checkResult));
        jacksonValue.setJsonpFunction(callback);

        return jacksonValue;
    }

    //前台调用注册机制 SSO
    @RequestMapping("/register")
    @ResponseBody
    public SysResult doRegister(User user) {
        try {
            //将用户入库操作后,将用户名返回
            String username = userService.saveUser(user);
            return SysResult.oK(username);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return SysResult.build(201, "用户新增失败");
        }
    }

    //前台调用单点的登录操作
    @RequestMapping("/login")
    @ResponseBody
    public SysResult login(@RequestParam("u") String username, @RequestParam("p") String password) {
        String ticket = userService.findLogin(username, password);

        if (StringUtils.isEmpty(ticket)) {
            //根据用户名和密码失败
            return SysResult.build(201, "用户名或密码错误");
        } else {
            //ticket中有数据
            return SysResult.oK(ticket);
        }
    }

    //根据ticket查询用户信息
    @RequestMapping("/query/{ticket}")
    @ResponseBody
    public Object findUserByTicket(@PathVariable String ticket, String callback) {
        //从redis中获取该数据
        String userJSON = userService.findUserByTicket(ticket);
        if (StringUtils.isEmpty(userJSON)) {
            return SysResult.build(201, "根据ticket没有查询到用户信息");
        } else {
            SysResult sysResult = SysResult.oK(userJSON);
            MappingJacksonValue jacksonValue = new MappingJacksonValue(sysResult);
            jacksonValue.setJsonpFunction(callback);
            return jacksonValue;
        }
    }


}
