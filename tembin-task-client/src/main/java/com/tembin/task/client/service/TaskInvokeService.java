package com.tembin.task.client.service;

import com.tembin.common.base.ApiResult;
import com.tembin.task.client.vo.ScheduleJobVO;

public interface TaskInvokeService {

    ApiResult invokeTask(ScheduleJobVO scheduleJobVO);
}
