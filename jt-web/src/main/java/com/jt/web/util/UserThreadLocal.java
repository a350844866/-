package com.jt.web.util;

import com.jt.web.pojo.User;

public class UserThreadLocal {

    /**
     * 1.泛型内容主要看以后存储的数据,如果需要存储多个数据 需要添加Map
     * 2.如果以后只需要单个数据,则直接写单个数据的泛型即可
     */
    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<User>();

    public static User getUser() {
        return userThreadLocal.get();
    }

    public static void setUser(User user) {
        userThreadLocal.set(user);
    }

}
