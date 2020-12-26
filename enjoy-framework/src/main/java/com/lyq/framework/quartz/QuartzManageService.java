package com.lyq.framework.quartz;

import com.lyq.framework.common.GlobalNames;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class QuartzManageService {

    @Resource
    private QuartzManageMapper quartzManageMapper;

    public List<JobEntity> listJob(){
        return null;
    }

    public JobEntity lockTest(String id){
        JobEntity jobEntity = quartzManageMapper.lockById(id, GlobalNames.WEB_APPID);
        return jobEntity;
    }
    public Integer save(String id){
        HashMap<String, Object> logPara = new HashMap<String, Object>();
        logPara.clear();
        logPara.put("twdId", "1");
        logPara.put("startTime", new Date());
        logPara.put("serverIp", GlobalNames.CURRENT_NODE_IP);
        logPara.put("serverPort", GlobalNames.CURRENT_NODE_PORT);
        System.out.println(logPara.toString());
        Integer logId = quartzManageMapper.saveTWOLog(logPara);
        System.out.println(logPara.toString());
        return logId;
    }
}
