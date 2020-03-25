package com.tembin.task.domain;

import com.tembin.common.hibernate.domian.IdEntity;
import com.tembin.task.client.codeEnum.TaskInvokeStatusEnum;
import com.tembin.task.domain.converter.TaskInvokeStatusConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @program: task-manager
 * @description: 任务执行结果
 **/
@Entity
@Table(name="job_execute_result")
public class JobExecuteResult extends IdEntity {
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


    public JobExecuteResult() {
    }

    public JobExecuteResult(String jobClass, String jobMethod, String jobId, String jobDescription, String modularFeignName) {
        this.jobClass = jobClass;
        this.jobMethod = jobMethod;
        this.jobId = jobId;
        this.jobDescription = jobDescription;
        this.modularFeignName = modularFeignName;
    }

    public JobExecuteResult(String jobClass, String jobMethod, String jobId, String jobDescription, String modularFeignName, TaskInvokeStatusEnum taskInvokeStatus) {
        this.jobClass = jobClass;
        this.jobMethod = jobMethod;
        this.jobId = jobId;
        this.jobDescription = jobDescription;
        this.modularFeignName = modularFeignName;
        this.taskInvokeStatus = taskInvokeStatus;
    }


    @Column(name = "modular_feign_name")
    public String getModularFeignName() {
        return modularFeignName;
    }

    public void setModularFeignName(String modularFeignName) {
        this.modularFeignName = modularFeignName;
    }

    @Column(name = "jobId")
    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Column(name = "task_invoke_status")
    @Convert(converter = TaskInvokeStatusConverter.class)
    public TaskInvokeStatusEnum getTaskInvokeStatus() {
        return taskInvokeStatus;
    }

    public void setTaskInvokeStatus(TaskInvokeStatusEnum taskInvokeStatus) {
        this.taskInvokeStatus = taskInvokeStatus;
    }

    @Column(name = "error_message",length = 512)
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    @Column(name = "params")
    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
    @Column(name = "job_class")
    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }
    @Column(name = "job_method")
    public String getJobMethod() {
        return jobMethod;
    }

    public void setJobMethod(String jobMethod) {
        this.jobMethod = jobMethod;
    }
    @Column(name = "job_description")
    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    @Override
    public String toString() {
        return "JobExecuteResult{" +
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
