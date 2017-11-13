package com.jt.web.controller;

import com.jt.common.util.CookieUtils;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;
import com.jt.web.service.UserSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserSerivce userSerivce;

    @Autowired
    private JedisCluster jedisCluster;

    @RequestMapping("/register")
    public String register() {

        return "register";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/doRegister")
    @ResponseBody
    public SysResult doRegister(User user) {
        SysResult sysResult = userSerivce.doRegister(user);
        return sysResult;
    }

    //用户的登录操作

    /**
     * 1.判断数据是否为空
     * 2.调用service进行查询操作
     * 3.返回结果集
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public SysResult doLogin(String username, String password, HttpServletResponse response, HttpServletRequest request) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return SysResult.build(201, "用户名或密码不能为空");
        }

        SysResult sysResult = userSerivce.doLogin(username, password);
        CookieUtils.setCookie(request, response, "JT_TICKET", (String) sysResult.getData());
        return sysResult;


    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String cookieName = "JT_TICKET";
        //将数据从redis中删除
        String ticket = CookieUtils.getCookieValue(request, cookieName);
        System.out.println(ticket);
        jedisCluster.del(ticket);
        //应该将cookie删除
        CookieUtils.deleteCookie(request, response, cookieName);
        return "redirect:/index.html";//返回值结果应该是伪静态的
    }
}
