package com.tembin.task.handler;

import com.tembin.task.client.vo.JobExecuteResultVO;
import com.tembin.task.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: task-manager
 * @description: 任务回调结果处理
 **/
@Component
public class TaskCallBackHandler {
    private static final Logger logger = LoggerFactory.getLogger(TaskCallBackHandler.class);

    @Autowired
    private TaskService taskService;

    @RabbitHandler
    @RabbitListener(queues = "task_callBack_queue")
    public void taskCallBack(JobExecuteResultVO jobExecuteResultVO){
        logger.info("开始处理任务回调结果，jobExecuteResultVO:{}",jobExecuteResultVO);
        try {
            taskService.jobExecuteResult(jobExecuteResultVO);
        }catch (Exception e){
            logger.error("处理任务回调出错，e:{}",e);
        }
    }
}
