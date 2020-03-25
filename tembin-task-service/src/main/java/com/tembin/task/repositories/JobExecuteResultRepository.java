package com.tembin.task.repositories;

import com.tembin.common.jpa.IBaseRepository;
import com.tembin.task.domain.JobExecuteResult;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface JobExecuteResultRepository extends IBaseRepository<JobExecuteResult, String> {

    @Query(value = "select * from job_execute_result where create_time between ?1 and ?2 ",nativeQuery = true)
    List<JobExecuteResult> findByCreateTime(Date startTime, Date endTime);
}
