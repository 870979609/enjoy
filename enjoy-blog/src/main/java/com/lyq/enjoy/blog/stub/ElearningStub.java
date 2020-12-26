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

import java.util.Map;

/**
 * @Description
 * @Author lixinyu
 * @Date 2020/11/22 18:42
 * @Version 1.0
 **/
@Component
public class ElearningStub extends Stub {

    private String APPID = "Elearning".toUpperCase();

    @Autowired
    ElearningInterface elearningInterface;

    public static ElearningStub getInstance() {
        return SpringBeanUtil.getBean(ElearningStub.class);
    }

    public R gettest(Map para) {

        R result = null;
        if (GlobalNames.DEBUGMODE == true && GlobalNames.TESTURL_MAP != null && GlobalNames.TESTURL_MAP.containsKey(APPID)) {
            String url = GlobalNames.TESTURL_MAP.get(APPID);
            if (!StringUtil.isEmpty(url)) {
                result = elearningInterface.gettest();
            } else {
                url = dealRequestURL(url) + "gettest";
                RestTemplate new_rest_template = new RestTemplate();
                result = new_rest_template.getForObject(url, R.class);
            }
        } else {
            result = elearningInterface.gettest();
        }

        return result;
    }

    public R posttest(Map para) {

        R result = null;
        if (GlobalNames.DEBUGMODE == true && GlobalNames.TESTURL_MAP != null && GlobalNames.TESTURL_MAP.containsKey(APPID)) {
            String url = GlobalNames.TESTURL_MAP.get(APPID);
            if (!StringUtil.isEmpty(url)) {
                result = elearningInterface.posttest(para);
            } else {
                url = dealRequestURL(url) + "posttest";
                RestTemplate new_rest_template = new RestTemplate();
                result = new_rest_template.getForObject(url, R.class);
            }
        } else {
            result = elearningInterface.posttest(para);
        }

        return result;
    }

    @FeignClient("elearning")
    interface ElearningInterface {

        @GetMapping("elearning/gettest")
        R gettest();

        @PostMapping("elearning/posttest")
        R posttest(@RequestBody Map map);
    }
}
