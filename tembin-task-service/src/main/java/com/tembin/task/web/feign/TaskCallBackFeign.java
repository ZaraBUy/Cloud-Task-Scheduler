package com.tembin.task.web.feign;

import com.tembin.common.base.ApiResult;
import com.tembin.task.client.api.TaskCallBackApi;
import com.tembin.task.client.vo.JobExecuteResultVO;
import com.tembin.task.client.vo.ScheduleJobVO;
import com.tembin.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: task-manager
 * @description: feign
 **/
@RestController
public class TaskCallBackFeign implements TaskCallBackApi {
    @Autowired
    private TaskService taskService;
    @Override
    public void jobExecuteResult(@RequestBody JobExecuteResultVO jobExecuteResultVO) {
    }

    @Override
    public ApiResult jobRegister(@RequestBody ScheduleJobVO scheduleJobVO) {
        return taskService.jobRegister(scheduleJobVO);
    }
}
