package com.lyq.framework.quartz;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface QuartzManageMapper {
    List<JobEntity> listJob();
    JobEntity lockById(@Param("twdId") String twdId, @Param("appId") String appId);
    JobEntity getById(@Param("twdId") String twdId, @Param("appId") String appId);
    Integer saveTWOLog(Map map);
    void endTWOLog(Map map);
    void saveLogDetails(Map map);
    void saveExceptionDetails(Map map);
    List<JobEntity> loadAllAvailableWork(String appid);
}