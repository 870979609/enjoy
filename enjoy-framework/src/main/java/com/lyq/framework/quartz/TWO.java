package com.lyq.framework.quartz;

import com.lyq.framework.common.GlobalNames;
import com.lyq.framework.common.exception.AppException;
import com.lyq.framework.spring.SpringBeanUtil;
import com.lyq.framework.util.DataStore;
import com.lyq.framework.util.ExceptionUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;

public abstract class TWO implements Job {
    private String twdId = null;
    private Long logId = null;
    private DataStore logDs;
    private DataStore exceptionDs;

    private static Logger logger = LoggerFactory.getLogger(TWO.class);

    private QuartzManageMapper quartzManageMapper = SpringBeanUtil.getBean(QuartzManageMapper.class);

    public String getTwdid() {
        return twdId;
    }

    public void setTwdid(String twdId) {
        this.twdId = twdId;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public DataStore getLogDs() {
        return logDs == null ? DataStore.getInstance() : logDs;
    }

    public void saveLog(String message) throws AppException {
        if (logDs == null) {
            logDs = DataStore.getInstance();
        }

        this.logDs.put(logDs.rowCount(), "log", message);
    }

    public DataStore getExceptionDs() {
        return exceptionDs == null ? DataStore.getInstance() : exceptionDs;
    }

    public void saveException(String exception) throws AppException {
        if (exceptionDs == null) {
            exceptionDs = DataStore.getInstance();
        }

        this.exceptionDs.put(exceptionDs.rowCount(), "exception", exception);
    }

    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext){
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        twdId = jobDataMap.getString("twdId");
        String twdName = jobDataMap.getString("twdName");
        String parameter = jobDataMap.getString("parameter");
        String concurrent = jobDataMap.getString("concurrent");

        try {
            // 行锁控制并发
            if (!lockById(twdId) && !"1".equals(concurrent)) {
                return;
            }

            // 记录执行日志
            logId = startTWOLog();

            int executeCount = 0;
            try {
                executeCount = doExecute(parameter);
            } catch (Exception e) {
                throw new AppException("[" + twdName + "]定时任务执行内部出错,错误信息:" + e.getMessage());
            }

            // 获取TWO中的日志
            DataStore twoLog = getLogDs();
            DataStore twoException = getExceptionDs();

            for (int i = 0; i < twoLog.rowCount(); i++) {
                String log = twoLog.getString(i, "log");
                String businessId = twoLog.getString(i, "businessId");

                addLog(businessId, log);
            }

            for (int i = 0; i < twoException.rowCount(); i++) {
                String exception = twoException.getString(i, "exception");
                String businessId = twoLog.getString(i, "businessId");

                addException(businessId, exception);
            }
            endTWOLog(executeCount);
        } catch (Throwable e) {
            logger.error("TWO error :" + ExceptionUtil.getMessage(e));
            endTWOLog(-1);
        }
    }

    /**
     * 行锁控制并发
     *
     * @param jobId
     * @return
     */
    private boolean lockById(String jobId) {
        boolean lock = true;
        try {
            JobEntity jobEntity = quartzManageMapper.lockById(jobId, GlobalNames.WEB_APPID);
            if (jobEntity == null) {
                lock = false;
            }
        } catch (Throwable e) {
            lock = false;
        }
        return lock;
    }

    private Long startTWOLog(){
        HashMap<String, Object> logPara = new HashMap<String, Object>();
        logPara.clear();
        logPara.put("twdId", twdId);
        logPara.put("serverIp", GlobalNames.CURRENT_NODE_IP);
        logPara.put("serverPort", GlobalNames.CURRENT_NODE_PORT);
        quartzManageMapper.saveTWOLog(logPara);
        return ((Number) logPara.get("logId")).longValue();
    }

    private void endTWOLog(Integer count) {
        if (logId == null) {
            return;
        }

        HashMap<String, Object> logPara = new HashMap<String, Object>();
        logPara.clear();
        logPara.put("logId", logId);
        logPara.put("executeCount", count);
        quartzManageMapper.endTWOLog(logPara);
    }

    private void addLog(String businessId, String log) {
        HashMap<String, Object> logPara = new HashMap<String, Object>();
        logPara.clear();
        logPara.put("logId", logId);
        logPara.put("businessId", businessId);
        logPara.put("message", log);
        quartzManageMapper.saveLogDetails(logPara);
    }

    private void addException(String businessId, String exception) {
        HashMap<String, Object> logPara = new HashMap<String, Object>();
        logPara.clear();
        logPara.put("logId", logId);
        logPara.put("businessId", businessId);
        logPara.put("message", exception);
        quartzManageMapper.saveExceptionDetails(logPara);
    }

    protected abstract int doExecute(String parameter);
}
