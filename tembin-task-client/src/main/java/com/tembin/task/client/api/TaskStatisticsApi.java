package com.tembin.task.client.api;

import com.tembin.task.client.vo.TaskStatisticsVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @program: tembin-task
 * @description: 任务统计api
 **/
@FeignClient(value = "task-scheduler", path = "/task-scheduler")
public interface TaskStatisticsApi {

    /**
     * 统计任务执行情况
     * @param date 需要统计的指定日期
     */
    @GetMapping("/service/task/task-statistics")
    TaskStatisticsVO taskStatistics(@RequestParam(value = "date") String date);
}
