package com.tembin.task.client.vo;

import com.tembin.task.client.codeEnum.TaskStatusEnum;


/**
 * @program: tembin-money
 * @description: 任务vo
 **/
public class ScheduleJobVO {

    private String scheduleJobId;

    private String jobExecuteResultId;

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
     * 任务执行时调用哪个类 包名+类名
     */
    private String jobClass;
    /**
     * 任务调用的方法名
     */
    private String jobMethod;
    /**
     * 任务是否并行
     */
    private boolean isConcurrent;
    /**
     * 是否禁用
     */
    private boolean isDisable=false;


    public String getScheduleJobId() {
        return scheduleJobId;
    }

    public void setScheduleJobId(String scheduleJobId) {
        this.scheduleJobId = scheduleJobId;
    }

    public String getJobExecuteResultId() {
        return jobExecuteResultId;
    }

    public void setJobExecuteResultId(String jobExecuteResultId) {
        this.jobExecuteResultId = jobExecuteResultId;
    }

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
        return isConcurrent;
    }

    public void setConcurrent(boolean concurrent) {
        isConcurrent = concurrent;
    }

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean disable) {
        isDisable = disable;
    }

    @Override
    public String toString() {
        return "ScheduleJobVO{" +
                "scheduleJobId='" + scheduleJobId + '\'' +
                ", jobExecuteResultId='" + jobExecuteResultId + '\'' +
                ", modularFeignName='" + modularFeignName + '\'' +
                ", jobStatus=" + jobStatus +
                ", cronExpression='" + cronExpression + '\'' +
                ", description='" + description + '\'' +
                ", jobClass='" + jobClass + '\'' +
                ", jobMethod='" + jobMethod + '\'' +
                ", isConcurrent=" + isConcurrent +
                ", isDisable=" + isDisable +
                '}';
    }
}
