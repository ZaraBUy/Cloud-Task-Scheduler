package com.tembin.task.client.codeEnum;

public enum TaskStatusEnum {

    NONE("NONE","未知"),
    NORMAL("NORMAL", "正常"),
    PAUSED("PAUSED", "暂停"),
    COMPLETE("ACCEPT","完成"),
    ERROR("ERROR","错误"),
    RUNNING("RUNNING","运行"),
    BLOCKED("BLOCKED","锁定");

     TaskStatusEnum(String code, String name) {
        this.name = name;
        this.code = code;
    }
    private String code;
    private String name;
    public String getName() {
        return name;
    }
    public String getCode() {
        return code;
    }

    public static TaskStatusEnum fromValue(String code){
        for (TaskStatusEnum status : TaskStatusEnum.values()) {
            if(status.code.equals(code)){
                return  status;
            }
        }
        throw new IllegalArgumentException(String.format("%s不是正确的用户状态代码", code));
    }
}
