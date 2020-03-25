package com.tembin.task.client.codeEnum;

/**
 * @program: task-manager
 * @description: 任务的执行状态
 **/
public enum TaskInvokeStatusEnum {

    /**
     * 初始时运行的状态
     */
    TASK_RUNNING("RUNNING","运行中"),
    /**
     * 正常完成
     */
    TASK_FINISH("FINISH","完成"),
    /**
     * 错误
     */
    TASK_ERROR("ERROR","错误"),
    /**
     * 未知，失联
     */
    TASK_UNKNOWN("UNKNOWN","未知");

    private TaskInvokeStatusEnum(String code,String name){
        this.code = code;
        this.name = name;
    }
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
