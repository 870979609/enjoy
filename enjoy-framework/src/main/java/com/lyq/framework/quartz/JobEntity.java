package com.lyq.framework.quartz;

import com.lyq.framework.common.GlobalNames;

import java.util.Date;

public class JobEntity {
    private String twdId;
    private String twdName;          //job名称
    private String cron;          //执行的cron
    private String appId;          //appid
    private String parameter;     //job的参数
    private String description;   //job描述信息
    private String workClassName;     //job执行的class
    private String concurrent;  //job允许并发  1允许  0不允许
    private String available;  //是否启用  1启  0不
    private Date pauseBeginTime;  //暂停起始
    private Date pauseEndTime;  //暂停终止

    public JobEntity() {
    }

    public Date getPauseBeginTime() {
        return pauseBeginTime;
    }

    public void setPauseBeginTime(Date pauseBeginTime) {
        this.pauseBeginTime = pauseBeginTime;
    }

    public Date getPauseEndTime() {
        return pauseEndTime;
    }

    public void setPauseEndTime(Date pauseEndTime) {
        this.pauseEndTime = pauseEndTime;
    }

    public String getTwdId() {
        return twdId;
    }

    public void setTwdId(String id) {
        this.twdId = id;
    }

    public String getTwdName() {
        return twdName;
    }

    public void setTwdName(String name) {
        this.twdName = name;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appid) {
        this.appId = appid;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkClassName() {
        return workClassName;
    }

    public void setWorkClassName(String className) {
        this.workClassName = className;
    }

    public String getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(String concurrent) {
        this.concurrent = concurrent;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "JobEntity{" +
                "twdid=" + twdId +
                ", twdname='" + twdName + '\'' +
                ", cron='" + cron + '\'' +
                ", appid='" + appId + '\'' +
                ", parameter='" + parameter + '\'' +
                ", description='" + description + '\'' +
                ", workClassName='" + workClassName + '\'' +
                ", concurrent='" + concurrent + '\'' +
                ", available='" + available + '\'' +
                '}';
    }

    //新增Builder模式,可选,选择设置任意属性初始化对象
    public JobEntity(Builder builder) {
        twdId = builder.id;
        twdName = builder.name;
        appId = builder.appid;
        cron = builder.cron;
        parameter = builder.parameter;
        description = builder.description;
        workClassName = builder.className;
        concurrent = builder.concurrent;
        available = builder.available;
    }

    public static class Builder {
        private String id;
        private String name = "";          //job名称
        private String appid = GlobalNames.WEB_APPID;         //job应用名
        private String cron = "";          //执行的cron
        private String parameter = "";     //job的参数
        private String description = "";   //job描述信息
        private String className = "";       //执行的class
        private String concurrent = "0";   //是否允许并发  默认不允许
        private String available = "1";   //默认启用

        public Builder withId(String i) {
            id = i;
            return this;
        }

        public Builder withName(String n) {
            name = n;
            return this;
        }

        public Builder withCron(String c) {
            cron = c;
            return this;
        }

        public Builder withParameter(String p) {
            parameter = p;
            return this;
        }

        public Builder withDescription(String d) {
            description = d;
            return this;
        }

        public Builder withClassName(String cn) {
            className = cn;
            return this;
        }

        public Builder withAvailable(String s) {
            available = s;
            return this;
        }

        public Builder withConcurrent(String s) {
            concurrent = s;
            return this;
        }

        public JobEntity newJobEntity() {
            return new JobEntity(this);
        }
    }
}
