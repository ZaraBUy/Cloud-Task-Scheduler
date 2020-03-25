package com.tembin.task.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tembin.task.client.codeEnum.TaskInvokeStatusEnum;

import java.util.Date;

/**
 * @program: task-manager
 * @description: 展示vo
 **/
public class JobExecuteResultShowVo {

    /**
     * 执行状态
     */
    private TaskInvokeStatusEnum taskInvokeStatus;
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

    private String jobId;

    /**
     * 每个模块被调用的接口名
     */
    private String modularFeignName;

    private String id;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public TaskInvokeStatusEnum getTaskInvokeStatus() {
        return taskInvokeStatus;
    }

    public void setTaskInvokeStatus(TaskInvokeStatusEnum taskInvokeStatus) {
        this.taskInvokeStatus = taskInvokeStatus;
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

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getModularFeignName() {
        return modularFeignName;
    }

    public void setModularFeignName(String modularFeignName) {
        this.modularFeignName = modularFeignName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "JobExecuteResultShowVo{" +
                "taskInvokeStatus=" + taskInvokeStatus +
                ", errorMessage='" + errorMessage + '\'' +
                ", params='" + params + '\'' +
                ", jobClass='" + jobClass + '\'' +
                ", jobMethod='" + jobMethod + '\'' +
                ", jobDescription='" + jobDescription + '\'' +
                ", jobId='" + jobId + '\'' +
                ", modularFeignName='" + modularFeignName + '\'' +
                ", id='" + id + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
