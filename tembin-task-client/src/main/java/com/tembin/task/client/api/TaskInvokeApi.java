package com.tembin.task.client.api;

import com.tembin.common.base.ApiResult;
import com.tembin.task.client.vo.ScheduleJobVO;
import org.springframework.web.bind.annotation.RequestBody;

public interface TaskInvokeApi {

    ApiResult executeJob(@RequestBody ScheduleJobVO scheduleJobVO);

    void doTaskRegister();
}
