package com.lyq.enjoy.global.controller;

import com.lyq.enjoy.blog.stub.TestStub;
import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.common.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private TestStub testStub;
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public R login(@RequestBody Map<String, String> para, HttpServletRequest request) throws AppException, JSONException {
        String username = para.get("username");
        String password = para.get("password");

        para.clear();
        para.put("token", "admin-token");
        return R.ok().data(para);
    }

    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public R logout(HttpServletRequest request) throws AppException, JSONException {
        return R.ok();
    }

    @RequestMapping(value = "/user/info",method = RequestMethod.GET)
    public R userInfo(@RequestParam Map para, HttpServletRequest request) throws AppException, JSONException {
        String token = (String) para.get("token");
        para.clear();
        para.put("roles", new String[]{"admin"});
        para.put("introduction", "I am a super administrator");
        para.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        para.put("name", "超级管理员");
        return R.ok().data(para);
    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public R test(@RequestParam Map para, HttpServletRequest request) throws AppException, JSONException {

        para.put("顶", 1232);
        para.put("广泛广泛的", "dssadsad");
        return R.ok().data(testStub.posttest(para));
    }
}
