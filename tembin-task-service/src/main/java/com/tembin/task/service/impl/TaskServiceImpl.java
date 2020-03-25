package com.tembin.task.service.impl;

import com.google.common.collect.Lists;
import com.tembin.common.base.ApiResult;
import com.tembin.common.exception.ServiceException;
import com.tembin.common.utils.EmailUtil;
import com.tembin.task.client.api.TaskInvokeApi;
import com.tembin.task.client.codeEnum.TaskInvokeStatusEnum;
import com.tembin.task.client.codeEnum.TaskStatusEnum;
import com.tembin.task.client.util.SpringContextHolder;
import com.tembin.task.client.vo.JobExecuteResultVO;
import com.tembin.task.client.vo.ScheduleJobVO;
import com.tembin.task.client.vo.TaskStatisticsVO;
import com.tembin.task.domain.JobExecuteResult;
import com.tembin.task.domain.ScheduleJob;
import com.tembin.task.repositories.JobExecuteResultRepository;
import com.tembin.task.task.QuartzJobFactoryDisallowConcurrent;
import com.tembin.task.repositories.ScheduleJobRepository;
import com.tembin.task.service.TaskService;
import com.tembin.task.vo.JobExecuteResultShowVo;
import com.tembin.task.vo.ScheduleJobShowVo;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskServiceImpl implements TaskService {


    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    @Autowired
    private ScheduleJobRepository scheduleJobRepository;
    @Autowired
    private JobExecuteResultRepository jobExecuteResultRepository;
    @Autowired
    private List<TaskInvokeApi> taskInvokeApis;

    @Override
    public void invokeMethod(ScheduleJob scheduleJob) {
        String jobClass = scheduleJob.getJobClass();
        if(StringUtils.isEmpty(jobClass)){
            throw new ServiceException(String.format("任务:%s-%s --的类为空",scheduleJob.getJobClass(),scheduleJob.getJobMethod()));
        }
        try {
            Object obj = SpringContextHolder.getBean(scheduleJob.getJobClass());
            Class<?> clazz = obj.getClass();
            Method method = clazz.getDeclaredMethod(scheduleJob.getJobMethod());
            method.invoke(obj);
        } catch (Exception e) {
            throw new ServiceException(String.format(String.format("任务:%s-%s --未找到执行该任务的类的方法/执行该方法失败",scheduleJob.getJobClass(),scheduleJob.getJobMethod())),e);
        }

    }
    @Override
    public ScheduleJob findJob(String jobName, String jobGroup) {
        return scheduleJobRepository.findByJobMethodAndJobClass(jobName,jobGroup);
    }

    @Override
    public void saveJob(ScheduleJob job) {
        scheduleJobRepository.save(job);
    }

    @Override
    public List<ScheduleJob> findAllJobs() {
        return scheduleJobRepository.findAll();
    }

    @Override
    public List<ScheduleJob> findRunningJob(){
        Scheduler scheduler=schedulerFactoryBean.getScheduler();
        List<ScheduleJob> scheduleJobs= Lists.newArrayList();
        List<JobExecutionContext> runningJobs= null;
        try {
            runningJobs = scheduler.getCurrentlyExecutingJobs();
        } catch (SchedulerException e) {
            logger.error("");
            throw new ServiceException("");
        }
        for (JobExecutionContext runningJob : runningJobs) {
            JobDetail jobDetail=runningJob.getJobDetail();
            JobKey jobKey=jobDetail.getKey();
            String jobName=jobKey.getName();
            String jobGroup=jobKey.getGroup();
            scheduleJobs.add(scheduleJobRepository.findByJobMethodAndJobClass(jobName,jobGroup));
        }
        return scheduleJobs;
    }

    @Override
    public Page<ScheduleJob> findPageRunningJob(int page, int pageSize){
        List<ScheduleJob> runningJobs=findRunningJob();
        //todo 排序
        int fromIndex=page*pageSize;
        if(fromIndex>=runningJobs.size()){
            return new PageImpl<>(Lists.<ScheduleJob>newArrayList(),PageRequest.of(page,pageSize),runningJobs.size());
        }
        int endIndex=(page+1)*pageSize>runningJobs.size()?runningJobs.size():(page+1)*pageSize;
        return new PageImpl<>(runningJobs.subList(fromIndex,endIndex),PageRequest.of(page,pageSize),runningJobs.size());
    }

    @Override
    public ScheduleJob findJobById(String jobId) {
        return scheduleJobRepository.findById(jobId)
                .orElseThrow(()->new ServiceException(jobId+"的任务不存在"));
    }

    @Override
    public void jobExecuteResult(JobExecuteResultVO jobExecuteResultVO) {
        ScheduleJob scheduleJob = scheduleJobRepository.findById(jobExecuteResultVO.getScheduleJobId()).orElseThrow(() -> new ServiceException("未找到job，id:" + jobExecuteResultVO.getScheduleJobId()));
        JobExecuteResult jobExecuteResult = jobExecuteResultRepository.findById(jobExecuteResultVO.getJobExecuteResultId()).orElseThrow(() -> new ServiceException("未找到结果jobExecuteResult，id:" + jobExecuteResultVO.getJobExecuteResultId()));

        if(jobExecuteResultVO.getTaskInvokeStatusEnum() == TaskInvokeStatusEnum.TASK_FINISH){
            scheduleJob.setJobStatus(TaskStatusEnum.COMPLETE);
            jobExecuteResult.setTaskInvokeStatus(jobExecuteResultVO.getTaskInvokeStatusEnum());
        }else {
            scheduleJob.setJobStatus(TaskStatusEnum.ERROR);
            jobExecuteResult.setTaskInvokeStatus(jobExecuteResultVO.getTaskInvokeStatusEnum());
            jobExecuteResult.setErrorMessage(jobExecuteResultVO.getErrorMessage());
            EmailUtil.sendMail("jsb@tembin.com",String.format("任务:%s-%s --执行出错", scheduleJob.getJobClass(), scheduleJob.getJobMethod()),"错误原因："+jobExecuteResultVO.getErrorMessage());
        }
        //处理结果
        ScheduleJob scheduleJob1 = scheduleJobRepository.save(scheduleJob);
        JobExecuteResult jobExecuteResult1 = jobExecuteResultRepository.save(jobExecuteResult);
        logger.info("保存任务结果,jobExecuteResult:{}，scheduleJob：{}",jobExecuteResult1, scheduleJob1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult jobRegister(ScheduleJobVO scheduleJobVO) {
        if(checkJobExist(scheduleJobVO.getJobMethod(),scheduleJobVO.getJobClass())){
            return new ApiResult(401,"任务已经存在，不再注册任务",scheduleJobVO);
        }
        ScheduleJob scheduleJob = new ScheduleJob();
        BeanUtils.copyProperties(scheduleJobVO,scheduleJob);
        this.addJob(scheduleJob);
        return new ApiResult(200,"任务注册成功!",scheduleJobVO);
    }

    @Override
    public void scanAndRegisterTask() {
        for (TaskInvokeApi taskInvokeApi : taskInvokeApis) {
            logger.info("向各个模块发送注册任务请求--{}", AopUtils.getTargetClass(taskInvokeApi).getInterfaces()[0].getSimpleName());
            taskInvokeApi.doTaskRegister();
        }
    }

    @Override
    public boolean checkJobExist(String jobMethod, String jobClass){
        ScheduleJob JobMethodAndJobClass = scheduleJobRepository.findByJobMethodAndJobClass(jobMethod, jobClass);
        if(JobMethodAndJobClass != null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public TaskStatisticsVO taskStatistics(Date date) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(java.util.Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        Date endTime = calendar.getTime();

        List<JobExecuteResult> jobExecuteResultList = jobExecuteResultRepository.findByCreateTime(date, endTime);
        ArrayList<JobExecuteResultVO> failResult = new ArrayList<>();
        ArrayList<JobExecuteResult> finishResult = new ArrayList<>();
        for (JobExecuteResult jobexecute : jobExecuteResultList) {
            if (jobexecute.getTaskInvokeStatus() == TaskInvokeStatusEnum.TASK_FINISH) {
                finishResult.add(jobexecute);
            } else if (jobexecute.getTaskInvokeStatus() == TaskInvokeStatusEnum.TASK_ERROR) {
                JobExecuteResultVO jobExecuteResultVO = new JobExecuteResultVO();
                BeanUtils.copyProperties(jobexecute,jobExecuteResultVO);
                jobExecuteResultVO.setJobExecuteResultId(jobexecute.getId());
                jobExecuteResultVO.setScheduleJobId(jobexecute.getJobId());
                jobExecuteResultVO.setTaskInvokeStatusEnum(jobexecute.getTaskInvokeStatus());
                failResult.add(jobExecuteResultVO);
            }
        }

        TaskStatisticsVO taskStatisticsVO = new TaskStatisticsVO();
        taskStatisticsVO.setExecuteCount(jobExecuteResultList.size());
        taskStatisticsVO.setFinishCount(finishResult.size());
        taskStatisticsVO.setFailCount(failResult.size());
        taskStatisticsVO.setFailResultList(failResult);
        taskStatisticsVO.setOtherCount(jobExecuteResultList.size() - finishResult.size() - failResult.size());
        return taskStatisticsVO;
    }

    @Override
    public void addJob(ScheduleJob job){
        Scheduler scheduler=schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobMethod(), job.getJobClass());

        //获取trigger
        CronTrigger trigger = null;
        try {
            trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //不存在，创建一个
            if (null == trigger) {
                JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactoryDisallowConcurrent.class)
                        .withIdentity(job.getJobMethod(), job.getJobClass()).build();
                jobDetail.getJobDataMap().put("scheduleJob", job);

                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
                //按新的cronExpression表达式构建一个新的trigger
                trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobMethod(), job.getJobClass()).withSchedule(scheduleBuilder).build();

                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
                        .getCronExpression());
                //按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder).build();
                //按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
            }
            Trigger.TriggerState status=scheduler.getTriggerState(triggerKey);
            TaskStatusEnum jobStatus=TaskStatusEnum.fromValue(status.name());
            job.setJobStatus(jobStatus);
            job.setConcurrent(false);
            scheduleJobRepository.save(job);
        } catch (SchedulerException e) {
            logger.error("添加任务失败，scheduleJob:{},message:{}",job,e.getMessage());
            throw new ServiceException(e);
        }


    }

    @Override
    public void pauseJob(ScheduleJob scheduleJob){
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobMethod(), scheduleJob.getJobClass());
        TriggerKey triggerKey=TriggerKey.triggerKey(scheduleJob.getJobMethod(), scheduleJob.getJobClass());
        Trigger.TriggerState status;
        try {
            scheduler.pauseJob(jobKey);
            status=scheduler.getTriggerState(triggerKey);
            TaskStatusEnum jobStatus=TaskStatusEnum.fromValue(status.name());
            scheduleJob.setJobStatus(jobStatus);
            scheduleJobRepository.save(scheduleJob);
        } catch (SchedulerException e) {
            logger.error("暂停任务失败，scheduleJob:{},message:{}",scheduleJob,e.getMessage());
            throw new ServiceException(e);
        }

    }

    @Override
    public void resumeJob(ScheduleJob scheduleJob){
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobMethod(), scheduleJob.getJobClass());
        TriggerKey triggerKey=TriggerKey.triggerKey(scheduleJob.getJobMethod(), scheduleJob.getJobClass());
        try {
            scheduler.resumeJob(jobKey);
            Trigger.TriggerState status=scheduler.getTriggerState(triggerKey);
            TaskStatusEnum jobStatus=TaskStatusEnum.fromValue(status.name());
            scheduleJob.setJobStatus(jobStatus);
            scheduleJobRepository.save(scheduleJob);
        } catch (SchedulerException e) {
            logger.error("重启任务失败，scheduleJob:{},message:{}",scheduleJob,e.getMessage());
            throw new ServiceException(e);
        }

    }

    @Override
    public void deleteJob(ScheduleJob scheduleJob){
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobMethod(), scheduleJob.getJobClass());
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            logger.error("删除任务失败，scheduleJob:{},message:{}",scheduleJob,e.getMessage());
            throw new ServiceException(e);
        }
        scheduleJobRepository.delete(scheduleJob);
    }

    @Override
    public void executeJob(ScheduleJob scheduleJob){

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobMethod(), scheduleJob.getJobClass());

        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            logger.error("执行任务失败，scheduleJob:{},message:{}",scheduleJob,e.getMessage());
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateJop(ScheduleJob scheduleJob){

        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobMethod(), scheduleJob.getJobClass());
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            logger.error("更新任务失败，scheduleJob:{},message:{}",scheduleJob,e.getMessage());
            throw new ServiceException(e);
        }
        addJob(scheduleJob);
    }

    @Override
    public Page<ScheduleJobShowVo> listJobs(com.tembin.common.vo.PageRequest request, String jobName, String jobClass, TaskStatusEnum jobStatus, String description, String modularFeignName) {
        PageRequest pageRequest = getPageRequest(request);
        Page<ScheduleJob> scheduleJobs = scheduleJobRepository.findAll(new Specification<ScheduleJob>() {
            @Override
            public Predicate toPredicate(Root<ScheduleJob> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (StringUtils.isNotEmpty(jobName)) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("jobMethod"), jobName));
                }
                if (StringUtils.isNotEmpty(jobClass)) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("jobClass"), jobClass));
                }
                if (StringUtils.isNotEmpty(description)) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("description"), "%"+description+"%"));
                }
                if (StringUtils.isNotEmpty(modularFeignName)) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("modularFeignName"), modularFeignName));
                }
                if (jobStatus != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("jobStatus"), jobStatus));
                }
                return predicate;
            }
        }, pageRequest);
        ArrayList<ScheduleJobShowVo> scheduleJobShowVos = new ArrayList<>();
        scheduleJobs.getContent().stream().map(scheduleJob -> {
            ScheduleJobShowVo scheduleJobShowVo = new ScheduleJobShowVo();
            BeanUtils.copyProperties(scheduleJob,scheduleJobShowVo);
            scheduleJobShowVos.add(scheduleJobShowVo);
            return scheduleJob;
        }).collect(Collectors.toList());
        return new PageImpl<ScheduleJobShowVo>(scheduleJobShowVos,pageRequest,scheduleJobs.getTotalElements());
    }

    private PageRequest getPageRequest(com.tembin.common.vo.PageRequest request){
        String sortBy = request.getSortBy() == null ? "createTime" : request.getSortBy();
        Sort sort=new Sort(Sort.Direction.DESC,sortBy);
        return PageRequest.of(request.getPage(), request.getPageSize(), sort);
    }

    @Override
    public Page<JobExecuteResultShowVo> listJobDetail(com.tembin.common.vo.PageRequest request, String jobId, TaskInvokeStatusEnum taskInvokeStatusEnum) {
        PageRequest pageRequest = getPageRequest(request);
        Page<JobExecuteResult> jobExecuteResultRepositoryAll = jobExecuteResultRepository.findAll(new Specification<JobExecuteResult>() {
            @Override
            public Predicate toPredicate(Root<JobExecuteResult> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if (StringUtils.isNotEmpty(jobId)) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("jobId"), jobId));
                }
                if (taskInvokeStatusEnum != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("taskInvokeStatus"), taskInvokeStatusEnum));
                }
                return predicate;
            }
        }, pageRequest);
        ArrayList<JobExecuteResultShowVo> jobExecuteResultShowVos = new ArrayList<>();
        jobExecuteResultRepositoryAll.getContent().stream().map(jobExecuteResult -> {
            JobExecuteResultShowVo jobExecuteResultShowVo = new JobExecuteResultShowVo();
            BeanUtils.copyProperties(jobExecuteResult,jobExecuteResultShowVo);
            jobExecuteResultShowVos.add(jobExecuteResultShowVo);
            return jobExecuteResult;
        }).collect(Collectors.toList());
        return new PageImpl<JobExecuteResultShowVo>(jobExecuteResultShowVos,pageRequest,jobExecuteResultRepositoryAll.getTotalElements());
    }
}
