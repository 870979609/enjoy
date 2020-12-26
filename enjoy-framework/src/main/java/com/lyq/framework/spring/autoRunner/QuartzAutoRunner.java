package com.lyq.framework.spring.autoRunner;

import com.lyq.framework.common.GlobalNames;
import com.lyq.framework.quartz.JobEntity;
import com.lyq.framework.quartz.QuartzManageMapper;
import com.lyq.framework.quartz.TWO;
import com.lyq.framework.spring.SpringBeanUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component
@Order(2)
public class QuartzAutoRunner implements ApplicationRunner {
    private static Logger logger = LoggerFactory.getLogger(AppPluginRunner.class);

    private static Scheduler scheduler = null;

    private static QuartzManageMapper quartzManageMapper = null;

    static {
        try {
            quartzManageMapper = SpringBeanUtil.getBean(QuartzManageMapper.class);
            scheduler = SpringBeanUtil.getBean(Scheduler.class);

            logger.info("========定时任务初始化完成！============");
        }catch (Throwable e) {
            logger.error("===========定时任务初始化失败！==========");
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (scheduler == null || quartzManageMapper == null) {
            return;
        }

        // 加载数据库中所有定时任务定义
        List<JobEntity> jobs = loadAllAvailableWork();

        for (JobEntity job : jobs) {
            String twdId = job.getTwdId();
            String twdName = job.getTwdName();
            String cron = job.getCron();
            String concurrent = job.getConcurrent();
            String className = job.getWorkClassName();
            String parameter = job.getParameter();
            Date pauseBeginTime = job.getPauseBeginTime();
            Date pauseEndTime = job.getPauseEndTime();

            // 是否暂停期间
            if (isPause(pauseBeginTime, pauseEndTime)) {
                continue;
            }

            try {
                //实例化任务类
                Class WorkClass = Class.forName(className);
                boolean assignableFrom = TWO.class.isAssignableFrom(WorkClass);
                if (assignableFrom) {
                    JobDetail jobDetail = JobBuilder.newJob(WorkClass)
                            .withIdentity(twdId, GlobalNames.WEB_APPID)
                            .usingJobData("twdId", twdId)
                            .usingJobData("twdName", twdName)
                            .usingJobData("parameter", parameter)
                            .usingJobData("concurrent", concurrent)
                            .build();

                    CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(twdId, GlobalNames.WEB_APPID)
                            .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                            .build();

                    scheduler.scheduleJob(jobDetail, trigger);
                } else {
                    logger.error("[" + twdId + "][" + twdName + "]定时任务执行类没有继承TWO超类，无法执行！");
                }
            } catch (Throwable e) {
                logger.error(e.getMessage());
            }

        }
    }

    /**
     * 当前是否在暂停区间
     *
     * @param pauseBeginTime
     * @param pauseEndTime
     * @return
     */
    private boolean isPause(Date pauseBeginTime, Date pauseEndTime) {
        Date now = new Date();

        if (pauseBeginTime == null || pauseEndTime == null) {
            return false;
        }

        if (now.after(pauseBeginTime) && now.before(pauseEndTime)) {
            return true;
        }

        return false;
    }

    private List<JobEntity> loadAllAvailableWork() {
        List<JobEntity> jobEntities = quartzManageMapper.loadAllAvailableWork(GlobalNames.WEB_APPID);
        return jobEntities;
    }
}
