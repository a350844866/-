package com.jt.sso.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.sso.mapper.UserMapper;
import com.jt.sso.pojo.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JedisCluster jedisCluster;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Boolean findCheckUser(String param, int type) {

        String cloumn = null;
        switch (type) {
            case 1:
                cloumn = "username";
                break;
            case 2:
                cloumn = "phone";
                break;
            case 3:
                cloumn = "email";
                break;
            default:
                break;
        }
        int countNum = userMapper.findCheckUser(cloumn, param);


        return countNum > 0 ? true : false;
    }

    @Override
    public String findUserByTicket(String ticket) {
        String userJSON = jedisCluster.get(ticket);
        if (StringUtils.isEmpty(userJSON)) {
            return null;
        } else {
            return userJSON;
        }
    }

    @Override   //要求返回一个ticket
    public String findLogin(String username, String password) {
        //为了实现查询,将密码进行加密处理
        String md5Password = DigestUtils.md5Hex(password);

        User user = userMapper.findUSerByU_P(username, md5Password);
        //证明用户名和密码正确
        if (user != null) {
            String ticket = DigestUtils.md5Hex("JT_TICKET_" + System.currentTimeMillis() + username);
            try {
                //为了实现单点登录需要将ticket存入redis中
                String userJSON = objectMapper.writeValueAsString(user);
                jedisCluster.set(ticket, userJSON);


            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
            return ticket;
        }

        return null;
    }

    @Override
    public String saveUser(User user) {

        //准备数据
        //1.将密码进行MD5加密
        String md5Password = DigestUtils.md5Hex(user.getPassword());
        user.setPassword(md5Password);
        user.setEmail(user.getPhone());//为了防止null数据引入电话
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());

        userMapper.insert(user);

        return user.getUsername();
    }
}
