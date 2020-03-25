package com.tembin.task.domain;

import com.tembin.common.eclipselink.domain.IdEntity;
import com.tembin.task.client.codeEnum.TaskStatusEnum;
import com.tembin.task.domain.converter.TaskStatusConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "schedule_job")
public class ScheduleJob extends IdEntity {

    private static final long serialVersionUID = 8262906672048080926L;

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
    private Date executeTime;

    @Column(name = "modular_feign_name")
    public String getModularFeignName() {
        return modularFeignName;
    }

    public void setModularFeignName(String modularFeignName) {
        this.modularFeignName = modularFeignName;
    }

    @Column(name = "execute_time")
    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    @Column(name = "is_disable")
    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    @Column(name = "job_status")
    @Convert(converter = TaskStatusConverter.class)
    public TaskStatusEnum getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(TaskStatusEnum jobStatus) {
        this.jobStatus = jobStatus;
    }


    @Column(name = "corn_expression")
    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Column(name = "is_concurrent")
    public boolean isConcurrent() {
        return concurrent;
    }

    public void setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}

        ScheduleJob that = (ScheduleJob) o;

        if (!jobMethod.equals(that.jobMethod)) {return false;}
        return jobClass.equals(that.jobClass);
    }

    @Override
    public int hashCode() {
        int result = jobMethod.hashCode();
        result = 31 * result + jobClass.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ScheduleJob{" +
                "modularFeignName='" + modularFeignName + '\'' +
                ", jobStatus=" + jobStatus +
                ", cronExpression='" + cronExpression + '\'' +
                ", description='" + description + '\'' +
                ", jobClass='" + jobClass + '\'' +
                ", jobMethod='" + jobMethod + '\'' +
                ", concurrent=" + concurrent +
                ", disable=" + disable +
                ", executeTime=" + executeTime +
                ", id='" + id + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
