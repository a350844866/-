package com.jt.web.intercept;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.util.CookieUtils;
import com.jt.web.pojo.User;
import com.jt.web.util.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//定义SpringMVC中的拦截器
public class WebInterceptor implements HandlerInterceptor {
    @Autowired
    private JedisCluster jedisCluster;

    public static final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * @param request
     * @param response
     * @param handler
     * @return boolean:
     * true:表示拦截器放行
     * false:表示拦截器继续拦截  应该指定页面的跳转路径,否则程序卡死
     * @throws Exception
     */
    //请求执行之前拦截的操作
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 1.先获取ticket信息
         * 2.获取userJSON串
         * 3.将数据进行转化为User对象
         */
        String ticket = CookieUtils.getCookieValue(request, "JT_TICKET");
        if (!StringUtils.isEmpty(ticket)) {
            //如果ticket信息不为null则进行取值工作
            String userJSON = jedisCluster.get(ticket);
            if (!StringUtils.isEmpty(userJSON)) {
                //从redis中获取ticket信息不为空,则进行转换工作
                User user = objectMapper.readValue(userJSON, User.class);
                UserThreadLocal.setUser(user);
                return true; //表示拦截器放行
            }
        }
        //转向用户的登录页面
        response.sendRedirect("/user/login.html");

        return false;//false表示不能跳转到程序的指定页面,应该按照设定进行跳转
    }

    //请求执行之后拦截
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    //最终都要拦截
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
