package com.tembin.task.service;

import com.tembin.common.base.ApiResult;
import com.tembin.common.vo.PageRequest;
import com.tembin.task.client.codeEnum.TaskInvokeStatusEnum;
import com.tembin.task.client.codeEnum.TaskStatusEnum;
import com.tembin.task.client.vo.JobExecuteResultVO;
import com.tembin.task.client.vo.ScheduleJobVO;
import com.tembin.task.client.vo.TaskStatisticsVO;
import com.tembin.task.domain.ScheduleJob;
import com.tembin.task.vo.JobExecuteResultShowVo;
import com.tembin.task.vo.ScheduleJobShowVo;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;


public interface TaskService {

    void invokeMethod(ScheduleJob scheduleJob);

    ScheduleJob findJob(String jobName, String jobGroup);

    void saveJob(ScheduleJob job);

    List<ScheduleJob> findAllJobs();

    List<ScheduleJob> findRunningJob();

    Page<ScheduleJob> findPageRunningJob(int page, int pageSize);

    void addJob(ScheduleJob job);

    void pauseJob(ScheduleJob scheduleJob);

    void resumeJob(ScheduleJob scheduleJob);

    void deleteJob(ScheduleJob scheduleJob);

    void executeJob(ScheduleJob scheduleJob);

    void updateJop(ScheduleJob scheduleJob);

    Page<ScheduleJobShowVo> listJobs(PageRequest request, String jobName, String JobClass, TaskStatusEnum jobStatus, String description, String modularFeignName);

    Page<JobExecuteResultShowVo> listJobDetail(PageRequest request, String jobId, TaskInvokeStatusEnum taskInvokeStatusEnum);

    ScheduleJob findJobById(String jobId);

    void jobExecuteResult(JobExecuteResultVO jobExecuteResultVO);

    ApiResult jobRegister(ScheduleJobVO scheduleJobVO);

    void scanAndRegisterTask();

    boolean checkJobExist(String jobMethod, String jobClass);

    TaskStatisticsVO taskStatistics(Date date);
}
