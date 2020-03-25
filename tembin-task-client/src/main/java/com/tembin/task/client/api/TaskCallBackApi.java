package com.tembin.task.client.api;

import com.tembin.common.base.ApiResult;
import com.tembin.task.client.vo.JobExecuteResultVO;
import com.tembin.task.client.vo.ScheduleJobVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "task-scheduler", path = "/task-scheduler")
public interface TaskCallBackApi {

    /**
     * 任务执行的回调结果
     * @param jobExecuteResultVO
     */
    @PostMapping("/service/task/job-execute-result")
    void jobExecuteResult(@RequestBody JobExecuteResultVO jobExecuteResultVO);

    @PostMapping("/service/task/job-register")
    ApiResult jobRegister(@RequestBody ScheduleJobVO scheduleJobVO);
}
