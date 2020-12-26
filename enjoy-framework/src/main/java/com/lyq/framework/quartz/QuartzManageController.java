package com.lyq.framework.quartz;

import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.common.response.R;
import com.lyq.framework.util.SeqUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

@Controller
public class QuartzManageController {

    @Resource
    private QuartzManageService quartzManageService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/quartzManage/test")
    @ResponseBody
    public R testQuartz(Model model) throws AppException {
       System.out.println( SeqUtil.getId("seq_a"));
        System.out.println( SeqUtil.getId("seq_b"));
        System.out.println( SeqUtil.getId("seq_c"));
        return R.ok().data("");
    }

    @RequestMapping("/quartzManage")
    @ResponseBody
    public String listJobDetails(Model model) throws AppException {
    /*    CountDownLatch count = new CountDownLatch(1);
        for(int i = 1; i< 500; i++) {
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        count.await();

                        System.out.println("seq_A:AAA:::::" + SeqUtil.getId("seq_a"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }  catch (AppException e) {
                    e.printStackTrace();
                }
                }
            });
            th.start();
        }

        for(int i = 1; i< 500; i++) {
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        count.await();

                        System.out.println("seq_B:BBB:::::" + SeqUtil.getId("seq_b"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (AppException e) {
                        e.printStackTrace();
                    }
                }
            });
            th.start();
        }
            count.countDown();*/
        return "";
    }

    @RequestMapping("/quartzManage/lockTest/{id}")
    @ResponseBody
    public R lockTest(@PathVariable("id") String id) {
        return R.ok().data(quartzManageService.save(id));
    }
}
