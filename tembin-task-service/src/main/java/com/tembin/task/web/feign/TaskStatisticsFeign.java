package com.tembin.task.web.feign;

import com.tembin.common.exception.ServiceException;
import com.tembin.task.client.api.TaskStatisticsApi;
import com.tembin.task.client.vo.TaskStatisticsVO;
import com.tembin.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: tembin-task
 * @description: 实现feign
 **/
@RestController
public class TaskStatisticsFeign implements TaskStatisticsApi {
    @Autowired
    private TaskService taskService;

    @Override
    public TaskStatisticsVO taskStatistics(String date) {
        Date parse = null;
        try {
            parse = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            throw new ServiceException("时间转换出错，date:"+date);
        }
        return taskService.taskStatistics(parse);
    }
}
