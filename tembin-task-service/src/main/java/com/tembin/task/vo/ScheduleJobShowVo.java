package com.tembin.task.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tembin.task.client.codeEnum.TaskStatusEnum;

import java.util.Date;

/**
 * @program: task-manager
 * @description: 展示vo类
 **/
public class ScheduleJobShowVo {

    /**
     * 每个模块被调用的接口名
     */
    private String modularFeignName;
    /**
     * 任务状态
     */
    private TaskStatusEnum jobStatus;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 描述
     */
    private String description;
    /**
     * 任务执行时调用类
     */
    private String jobClass;
    /**
     * 任务调用的方法名
     */
    private String jobMethod;
    /**
     * 任务是否并行
     */
    private boolean concurrent;
    /**
     * 是否禁用
     */
    private boolean disable=false;

    /**
     * 上次执行的时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date executeTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String id;


    public String getModularFeignName() {
        return modularFeignName;
    }

    public void setModularFeignName(String modularFeignName) {
        this.modularFeignName = modularFeignName;
    }

    public TaskStatusEnum getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(TaskStatusEnum jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getJobMethod() {
        return jobMethod;
    }

    public void setJobMethod(String jobMethod) {
        this.jobMethod = jobMethod;
    }

    public boolean isConcurrent() {
        return concurrent;
    }

    public void setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ScheduleJobShowVo{" +
                "modularFeignName='" + modularFeignName + '\'' +
                ", jobStatus=" + jobStatus +
                ", cronExpression='" + cronExpression + '\'' +
                ", description='" + description + '\'' +
                ", jobClass='" + jobClass + '\'' +
                ", jobMethod='" + jobMethod + '\'' +
                ", isConcurrent=" + concurrent +
                ", isDisable=" + disable +
                ", executeTime=" + executeTime +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                ", id='" + id + '\'' +
                '}';
    }
}
