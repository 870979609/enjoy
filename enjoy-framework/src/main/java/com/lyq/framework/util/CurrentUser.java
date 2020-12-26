package com.lyq.framework.util;

/**
 * @Description
 * @Author lixinyu
 * @Date 2020/10/28 22:48
 * @Version 1.0
 **/
public class CurrentUser {
    private String userId;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;

    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
