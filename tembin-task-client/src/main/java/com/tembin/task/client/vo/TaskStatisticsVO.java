package com.tembin.task.client.vo;

import java.util.List;

/**
 * @program: tembin-task
 * @description: 任务统计vo
 **/
public class TaskStatisticsVO {
    /**
     * 执行次数
     */
    private Integer executeCount;
    /**
     * 完成次数
     */
    private Integer finishCount;
    /**
     * 失败次数
     */
    private Integer failCount;
    /**
     * 其他次数
     */
    private Integer otherCount;
    /**
     * 失败记录集合
     */
    private List<JobExecuteResultVO> failResultList;

    public List<JobExecuteResultVO> getFailResultList() {
        return failResultList;
    }

    public void setFailResultList(List<JobExecuteResultVO> failResultList) {
        this.failResultList = failResultList;
    }

    public Integer getExecuteCount() {
        return executeCount;
    }

    public void setExecuteCount(Integer executeCount) {
        this.executeCount = executeCount;
    }

    public Integer getFinishCount() {
        return finishCount;
    }

    public void setFinishCount(Integer finishCount) {
        this.finishCount = finishCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public Integer getOtherCount() {
        return otherCount;
    }

    public void setOtherCount(Integer otherCount) {
        this.otherCount = otherCount;
    }

    @Override
    public String toString() {
        return "TaskStatisticsVO{" +
                "executeCount=" + executeCount +
                ", finishCount=" + finishCount +
                ", failCount=" + failCount +
                ", otherCount=" + otherCount +
                '}';
    }
}
