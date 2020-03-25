package com.tembin.task.web.api;

import com.tembin.common.exception.ServiceException;
import com.tembin.common.vo.PageRequest;
import com.tembin.task.client.codeEnum.TaskInvokeStatusEnum;
import com.tembin.task.client.codeEnum.TaskStatusEnum;
import com.tembin.task.client.vo.TaskStatisticsVO;
import com.tembin.task.domain.ScheduleJob;
import com.tembin.task.service.TaskService;
import com.tembin.task.vo.JobExecuteResultShowVo;
import com.tembin.task.vo.ScheduleJobShowVo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
@RequestMapping("/api/xxx/task-api")
public class TaskApi {
    private static final Logger logger = LoggerFactory.getLogger(TaskApi.class);
    @Autowired
    private TaskService taskService;

    @GetMapping("list-jobs")
    @ApiOperation("任务列表")
    public Page<ScheduleJobShowVo> listJobs(PageRequest request, String jobMethod, String jobClass, TaskStatusEnum jobStatus, String description, String modularFeignName){
        return taskService.listJobs(request,jobMethod,jobClass,jobStatus,description,modularFeignName);
    }

    @GetMapping("list-job-detail")
    @ApiOperation("查询任务执行详情")
    public Page<JobExecuteResultShowVo> listJobDetail(PageRequest request, String taskId, TaskInvokeStatusEnum taskInvokeStatus){
        return taskService.listJobDetail(request,taskId,taskInvokeStatus);
    }

    @PostMapping("add-job")
    public void addJob(ScheduleJob job){
        //查询对应的任务类是不是存在
        if(taskService.checkJobExist(job.getJobMethod(), job.getJobClass())){
            throw new ServiceException(String.format("任务:%s-%s --存在相同任务，不能再添加", job.getJobClass(), job.getJobMethod()));
        }else {
            taskService.addJob(job);
        }

    }
    @PostMapping("pause-job")
    public void pauseJob(String jobId){
        ScheduleJob job=taskService.findJobById(jobId);
        taskService.pauseJob(job);
    }
    @PostMapping("resume-job")
    public void resumeJob(String jobId){
        ScheduleJob job=taskService.findJobById(jobId);
        taskService.resumeJob(job);
    }

    /**
     * 更新任务：只能更新描述和时间表达式，是否禁用。
     * 不能在此修改类名和方法名，由于注解自动注册的存在，即使修改了，每次重启项目时，还是会自动扫描注册任务
     * 如果需要修改类名和方法名，需要修改对应代码和注解信息
     * @param scheduleJob
     */
    @PostMapping("update-job")
    public void updateJob(ScheduleJob scheduleJob){
        logger.info("更新任务：scheduleJob：{}",scheduleJob);
        ScheduleJob job=taskService.findJobById(scheduleJob.getId());
        if(StringUtils.isNotEmpty(scheduleJob.getCronExpression().trim())){
            job.setCronExpression(scheduleJob.getCronExpression());
        }
        if(StringUtils.isNotEmpty(scheduleJob.getDescription().trim())){
            job.setDescription(scheduleJob.getDescription());
        }
        job.setDisable(scheduleJob.isDisable());
        job.setConcurrent(scheduleJob.isConcurrent());
        taskService.updateJop(job);
    }

    @PostMapping("execute-job")
    public void executeJob(String jobId){
        ScheduleJob job=taskService.findJobById(jobId);
        taskService.executeJob(job);
    }

    @PostMapping("delete-job")
    public void deleteJob(String jobId){
        ScheduleJob job=taskService.findJobById(jobId);
        taskService.deleteJob(job);
    }

    @PostMapping("do-task-register")
    @ApiOperation("扫描所有模块，注册任务")
    public void doTaskRegister(){
        taskService.scanAndRegisterTask();
    }

}
