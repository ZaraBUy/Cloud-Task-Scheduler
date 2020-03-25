package com.tembin.task.client.vo;

import com.tembin.task.client.codeEnum.TaskInvokeStatusEnum;

/**
 * @program: task-manager
 * @description: VO类
 **/
public class JobExecuteResultVO {

    /**
     * 执行状态
     */
    private TaskInvokeStatusEnum taskInvokeStatusEnum;
    /**
     * 错误消息
     */
    private String errorMessage;
    /**
     * 执行任务的参数
     */
    private String params;
    /**
     * 任务类
     */
    private String jobClass;
    /**
     * 任务方法
     */
    private String jobMethod;
    /**
     * 任务描述
     */
    private String jobDescription;

    private String scheduleJobId;

    private String jobExecuteResultId;

    /**
     * 每个模块被调用的接口名
     */
    private String modularFeignName;

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

    public TaskInvokeStatusEnum getTaskInvokeStatusEnum() {
        return taskInvokeStatusEnum;
    }

    public void setTaskInvokeStatusEnum(TaskInvokeStatusEnum taskInvokeStatusEnum) {
        this.taskInvokeStatusEnum = taskInvokeStatusEnum;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
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

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getScheduleJobId() {
        return scheduleJobId;
    }

    public void setScheduleJobId(String scheduleJobId) {
        this.scheduleJobId = scheduleJobId;
    }

    @Override
    public String toString() {
        return "JobExecuteResultVO{" +
                "taskInvokeStatusEnum=" + taskInvokeStatusEnum +
                ", jobExecuteResultId=" + jobExecuteResultId +
                ", errorMessage='" + errorMessage + '\'' +
                ", params='" + params + '\'' +
                ", jobClass='" + jobClass + '\'' +
                ", jobMethod='" + jobMethod + '\'' +
                ", jobDescription='" + jobDescription + '\'' +
                ", scheduleJobId='" + scheduleJobId + '\'' +
                ", modularFeignName='" + modularFeignName + '\'' +
                '}';
    }
}
