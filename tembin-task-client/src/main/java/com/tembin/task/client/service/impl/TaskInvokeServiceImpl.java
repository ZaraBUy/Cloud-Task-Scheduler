package com.tembin.task.client.service.impl;

import com.tembin.common.base.ApiResult;
import com.tembin.common.exception.ServiceException;
import com.tembin.task.client.codeEnum.TaskInvokeStatusEnum;
import com.tembin.task.client.service.TaskInvokeService;
import com.tembin.task.client.util.SpringContextHolder;
import com.tembin.task.client.vo.JobExecuteResultVO;
import com.tembin.task.client.vo.ScheduleJobVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * @program: task-manager
 * @description: 任务服务类
 **/
@Service
public class TaskInvokeServiceImpl implements TaskInvokeService {
    private static final Logger logger = LoggerFactory.getLogger(TaskInvokeServiceImpl.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ApiResult invokeTask(ScheduleJobVO scheduleJobVO) {
        logger.info("执行任务了,jobClass:{},jobMethod:{}",scheduleJobVO.getJobClass(),scheduleJobVO.getJobMethod());
        new Thread(new Runnable() {
            @Override
            public void run() {
                invoke(scheduleJobVO);
            }
        }).start();
        return new ApiResult(200,"调用成功",null);
    }

    public void invoke(ScheduleJobVO scheduleJobVO) {
        String jobClass = scheduleJobVO.getJobClass();
        if (StringUtils.isEmpty(jobClass)) {
            throw new ServiceException(String.format("任务:%s-%s --的类为空", scheduleJobVO.getJobClass(), scheduleJobVO.getJobMethod()));
        }
        JobExecuteResultVO jobExecuteResultVO = new JobExecuteResultVO();
        BeanUtils.copyProperties(scheduleJobVO,jobExecuteResultVO);
        try {
            Object obj = SpringContextHolder.getBean(scheduleJobVO.getJobClass());
            if (obj == null) {
                throw new ServiceException(String.format("任务:%s-%s --未找到类的实例bean", scheduleJobVO.getJobClass(), scheduleJobVO.getJobMethod()));
            }
            Class<?> clazz = obj.getClass();
            Method method = clazz.getDeclaredMethod(scheduleJobVO.getJobMethod());
            logger.info("通过反射执行任务方法，scheduleJobVO:{},jobExecuteResultVO:{}",scheduleJobVO,jobExecuteResultVO);
            method.invoke(obj);
            //发消息回调
            jobExecuteResultVO.setTaskInvokeStatusEnum(TaskInvokeStatusEnum.TASK_FINISH);
            rabbitTemplate.convertAndSend("task_callBack_queue",jobExecuteResultVO);
        } catch (Exception e) {
            logger.error("任务{}--{}:执行失败,原因:{}",scheduleJobVO.getJobClass(), scheduleJobVO.getJobMethod(),e);
            jobExecuteResultVO.setTaskInvokeStatusEnum(TaskInvokeStatusEnum.TASK_ERROR);
            jobExecuteResultVO.setErrorMessage(e.getClass().getName()+":"+e.getMessage());
            rabbitTemplate.convertAndSend("task_callBack_queue",jobExecuteResultVO);
            throw new ServiceException(String.format(String.format("任务:%s-%s --未找到执行该任务的类的方法/执行该方法失败", scheduleJobVO.getJobClass(), scheduleJobVO.getJobMethod())), e);
        }
    }
}
