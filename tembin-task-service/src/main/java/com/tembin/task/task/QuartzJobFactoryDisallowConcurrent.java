package com.tembin.task.task;

import com.tembin.common.exception.ServiceException;
import com.tembin.common.utils.EmailUtil;
import com.tembin.task.client.api.TaskInvokeApi;
import com.tembin.task.client.codeEnum.TaskInvokeStatusEnum;
import com.tembin.task.client.codeEnum.TaskStatusEnum;
import com.tembin.task.client.vo.ScheduleJobVO;
import com.tembin.task.domain.JobExecuteResult;
import com.tembin.task.domain.ScheduleJob;
import com.tembin.task.repositories.JobExecuteResultRepository;
import com.tembin.task.repositories.ScheduleJobRepository;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@DisallowConcurrentExecution
@Component
public class QuartzJobFactoryDisallowConcurrent implements Job {

    private static final Logger logger = LoggerFactory.getLogger(QuartzJobFactoryDisallowConcurrent.class);

    @Autowired
    private ScheduleJobRepository scheduleJobRepository;
    @Autowired
    private JobExecuteResultRepository jobExecuteResultRepository;
    @Autowired
    private List<TaskInvokeApi> taskInvokeApis;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob)jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
        logger.info("开始执行：{}-{}定时任务",scheduleJob.getJobMethod(),scheduleJob.getJobClass());
        ScheduleJob job = scheduleJobRepository.findByJobMethodAndJobClass(scheduleJob.getJobMethod(), scheduleJob.getJobClass());
        //生成一条任务运行记录
        JobExecuteResult jobExecuteResult = jobExecuteResultRepository.save(new JobExecuteResult(job.getJobClass(),job.getJobMethod(),job.getId(),
                                                                                                    job.getDescription(),job.getModularFeignName(),TaskInvokeStatusEnum.TASK_RUNNING));
        try {
            job.setExecuteTime(new Date());
            job.setJobStatus(TaskStatusEnum.RUNNING);
            ScheduleJobVO scheduleJobVO = new ScheduleJobVO();
            BeanUtils.copyProperties(scheduleJob,scheduleJobVO);
            scheduleJobVO.setScheduleJobId(scheduleJob.getId());
            scheduleJobVO.setJobExecuteResultId(jobExecuteResult.getId());
            //每个模块定义的被触发feign的接口名
            String invokeApiName = scheduleJobVO.getModularFeignName().toLowerCase();
            if(StringUtils.isEmpty(invokeApiName)){
                throw new ServiceException("任务对应的ModularFeignName为空，请检查是否设置");
            }
            //flag用于判断是否匹配到模块接口
            boolean flag = true;
            for (TaskInvokeApi taskInvokeApi : taskInvokeApis) {
                //获取代理的接口名称
                //todo 测试当接口继承多接口时 getInterfaces查找指定接口的顺序
                String simpleName = AopUtils.getTargetClass(taskInvokeApi).getInterfaces()[0].getSimpleName().toLowerCase();
                if(invokeApiName.equals(simpleName)){
                    logger.info("匹配对应的feign，进行任务远程触发，feign接口名：{}，invokeApiName：{}",simpleName,invokeApiName);
                    flag = false;
                    taskInvokeApi.executeJob(scheduleJobVO);
                }
            }
            if(flag){
                throw new ServiceException(String.format("任务:%s-%s-%s --未匹配到执行模块", job.getModularFeignName(), job.getJobClass(), job.getJobMethod()));
            }
            scheduleJobRepository.save(job);
        }catch (Exception e){
            logger.error("定时任务{}-{}执行出错",scheduleJob.getJobClass(),scheduleJob.getJobMethod(),e);
            job.setJobStatus(TaskStatusEnum.ERROR);
            jobExecuteResult.setTaskInvokeStatus(TaskInvokeStatusEnum.TASK_ERROR);
            jobExecuteResult.setErrorMessage(e.getMessage());
            EmailUtil.sendMail("jsb@tembin.com",String.format("任务:%s-%s --执行出错", scheduleJob.getJobClass(), scheduleJob.getJobMethod()),"错误原因："+e.getMessage());
            scheduleJobRepository.save(job);
            jobExecuteResultRepository.save(jobExecuteResult);
        }
    }
}
