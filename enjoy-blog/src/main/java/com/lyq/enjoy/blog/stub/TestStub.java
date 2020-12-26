package com.lyq.enjoy.blog.stub;

import com.lyq.framework.common.GlobalNames;
import com.lyq.framework.common.response.R;
import com.lyq.framework.spring.SpringBeanUtil;
import com.lyq.framework.util.StringUtil;
import com.lyq.framework.util.Stub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Description
 * @Author lixinyu
 * @Date 2020/11/22 18:42
 * @Version 1.0
 **/
@Component
public class TestStub extends Stub {

    private static String APPID = "test".toUpperCase();
    private static String prefix = "enjoy-test/";

    @Autowired
    TestInterface tstInterface;

    public static TestStub getInstance() {
        return SpringBeanUtil.getBean(TestStub.class);
    }

    public R gettest(Map para) {

        R result = null;
        if (GlobalNames.DEBUGMODE == true && GlobalNames.TESTURL_MAP != null && GlobalNames.TESTURL_MAP.containsKey(APPID)) {
            String url = GlobalNames.TESTURL_MAP.get(APPID);
            if (!StringUtil.isEmpty(url)) {
                result = tstInterface.gettest();
            } else {
                url = dealRequestURL(url) + prefix + "gettest";
                RestTemplate new_rest_template = new RestTemplate();
                result = new_rest_template.getForObject(url, R.class);
            }
        } else {
            result = tstInterface.gettest();
        }

        return result;
    }

    public Object posttest(Map para) {

        R result = null;
        if (GlobalNames.DEBUGMODE == true && GlobalNames.TESTURL_MAP != null && GlobalNames.TESTURL_MAP.containsKey(APPID)) {
            String url = GlobalNames.TESTURL_MAP.get(APPID);
            if (StringUtil.isEmpty(url)) {
                result = tstInterface.posttest(para);
            } else {
                url = dealRequestURL(url) + prefix + "posttest";
                RestTemplate new_rest_template = new RestTemplate();
                result = new_rest_template.postForObject(url, para, R.class);
            }
        } else {
            result = tstInterface.posttest(para);
        }

        return result != null ? result.getData() : null;
    }

    @FeignClient("enjoy-test")
    interface TestInterface {

        @GetMapping("enjoy-test/gettest")
        R gettest();

        @PostMapping("enjoy-test/posttest")
        R posttest(@RequestBody Map map);
    }
}
