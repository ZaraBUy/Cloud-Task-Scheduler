package com.tembin.task.repositories;

import com.tembin.common.jpa.IBaseRepository;
import com.tembin.task.domain.ScheduleJob;

public interface ScheduleJobRepository extends IBaseRepository<ScheduleJob, String> {
    /**
     * 查找任务
     * @param jobMethod
     * @param jobClass
     * @return
     */
    ScheduleJob findByJobMethodAndJobClass(String jobMethod, String jobClass);
}
