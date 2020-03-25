package com.tembin.task.config;

import com.tembin.task.client.codeEnum.TaskStatusEnum;
import com.tembin.task.domain.ScheduleJob;
import com.tembin.task.service.TaskService;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.List;

@Configuration
public class LoadTask {
    private static final Logger logger = LoggerFactory.getLogger(LoadTask.class);

    @Autowired
    private TaskService taskService;

    @Bean
    public SchedulerFactoryBean getSchedulerFactoryBean(JobFactory jobFactory){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(jobFactory);
        return schedulerFactoryBean;
    }

    @Bean
    public String initTask() throws SchedulerException {
        // 可执行的任务列表
        List<ScheduleJob> taskList = taskService.findAllJobs();
        logger.info("初始化加载定时任务......");
        for (ScheduleJob job : taskList) {
            if(!job.isDisable()){
                if(job.getJobStatus()== TaskStatusEnum.PAUSED){
                    taskService.addJob(job);
                    taskService.pauseJob(job);
                }else {
                    taskService.addJob(job);
                }
                logger.info("任务{}-{}--{}加载完成！",job.getJobMethod(),job.getJobClass(),job.getModularFeignName());
            }
        }
        return "";
    }
}
