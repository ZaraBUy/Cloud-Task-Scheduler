package com.tembin.task.client.config;

import com.tembin.common.base.ApiResult;
import com.tembin.task.client.annontation.TembinTask;
import com.tembin.task.client.api.TaskCallBackApi;
import com.tembin.task.client.vo.ScheduleJobVO;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

/**
 * @program: task-manager
 * @description: 扫描任务注解，注册任务的工具类
 **/
@Component
public class TembinTaskRegister implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(TembinTaskRegister.class);
    @Autowired
    private TaskCallBackApi taskCallBackApi;
    @Value("${spring.autoRegisterTask.package}")
    private String autoRegisterTaskPackage;

    private void annotationFilter(Method method) {

        Class<?> declaringClass = method.getDeclaringClass();
        String className = declaringClass.getSimpleName();
        String methodName = method.getName();

        String description = "";
        String cornExpression = "";
        String modularFeignName = "";
        boolean isDisable = false;
        boolean isConcurrent = false;
        Annotation[] methodAnnotations = method.getAnnotations();

        for (Annotation annotation : methodAnnotations) {
            if (annotation.annotationType().equals(TembinTask.class)) {
                // 抓取自定义注解信息
                TembinTask tembinTask = (TembinTask) annotation;
                description = tembinTask.description();
                cornExpression = tembinTask.cornExpression();
                isDisable = tembinTask.isDisable();
                modularFeignName = tembinTask.modularFeignName();
                isConcurrent = tembinTask.isConcurrent();
            }
        }
        ScheduleJobVO scheduleJobVO = new ScheduleJobVO();
        scheduleJobVO.setDescription(description);
        scheduleJobVO.setCronExpression(cornExpression);
        scheduleJobVO.setModularFeignName(modularFeignName);
        scheduleJobVO.setJobMethod(methodName);
        scheduleJobVO.setJobClass(className);
        scheduleJobVO.setDisable(isDisable);
        scheduleJobVO.setConcurrent(isConcurrent);

        ApiResult apiResult = taskCallBackApi.jobRegister(scheduleJobVO);
        logger.info("注册任务{}-{}：{}",scheduleJobVO.getJobMethod(),scheduleJobVO.getJobClass(),apiResult.getMsg());
    }

    /**
     * 容器初始化完成后时，扫描注册任务
     * @return
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.findAndRegister();
    }

    public void findAndRegister(){
        Reflections reflections = new Reflections(autoRegisterTaskPackage, new MethodAnnotationsScanner());
        Set<Method> onlineTaskMethods = reflections.getMethodsAnnotatedWith(TembinTask.class);
        Iterator iterator = onlineTaskMethods.iterator();

        while(iterator.hasNext()) {
            Method method = (Method)iterator.next();

            try {
                this.annotationFilter(method);
            } catch (Exception var7) {
                logger.error("自动注册任务出错,method:{},{}", method.getName(), var7);
            }
        }
    }
}
