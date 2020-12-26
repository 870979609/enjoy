package com.lyq.enjoy.test;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.common.response.R;
import com.lyq.framework.spring.SpringBeanUtil;
import com.lyq.framework.util.FastDFSUtil;
import com.lyq.framework.util.MessageUtil;
import com.lyq.framework.util.RedisUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @ResponseBody
    @RequestMapping(value = "/gettest",method = RequestMethod.GET)
    public R test(@RequestParam Map para, HttpServletRequest request) throws AppException{
        System.out.print(para.toString());
        RedisUtil redisUtil = RedisUtil.getInstance();
        redisUtil.set("testlxyddd", "dadsa");
        HashMap map = new HashMap();
        map.put("dasda", 2312);
        map.put("dasda222", 3332312);
        map.put("das21da222", "32");
        redisUtil.hset("hmaptest", "s", map);
        return R.ok().data(redisUtil.hmget("hmaptest"));
    }
    @ResponseBody
    @RequestMapping(value = "/posttest",method = RequestMethod.POST)
    public R posttest(@RequestBody Map para, HttpServletRequest request) throws AppException{
        System.out.print(para.toString());

        HashMap map = new HashMap();
        map.put("ddd", 1312);
        map.put("fdgdf", 32432);
        return R.ok().data(map);
    }
    @ResponseBody
    @RequestMapping(value = "/mqtest",method = RequestMethod.POST)
    public String qmtest(@RequestBody Map para, HttpServletRequest request) throws Exception {

        String name = (String) para.get("name");

        // 通过sendResult返回消息是否成功送达
        System.out.printf("%s%n", MessageUtil.send("LxyTopic", name));
        return null;
    }

    @RequestMapping(value = "/testfile", method = RequestMethod.GET)
    public String testFile( HttpServletRequest request, HttpServletResponse response) throws Exception {

        return "testfile";
    }

    @ResponseBody
    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    public String uploadfile(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return FastDFSUtil.upload(file);
    }
}
