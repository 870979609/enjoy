package com.lyq.framework.common.business;

import com.lyq.framework.common.exception.BusinessException;
import com.lyq.framework.util.CurrentUser;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @Author lixinyu
 * @Date 2020/10/28 21:25
 * @Version 1.0
 **/
public abstract class AbstractController {
    protected CurrentUser getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader("X-Token");

        // 根据token查询用户信息
        CurrentUser user = new CurrentUser();
        user.setUserId("1");
        user.setUserName("admin");
        return user;
    }
}
