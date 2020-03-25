package com.tembin.task.domain.converter;

import com.tembin.task.client.codeEnum.TaskInvokeStatusEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @program: task-manager
 * @description: 枚举转换类
 **/
@Converter
public class TaskInvokeStatusConverter implements AttributeConverter<TaskInvokeStatusEnum,String> {

    @Override
    public String convertToDatabaseColumn(TaskInvokeStatusEnum taskInvokeStatusEnum) {
        if (taskInvokeStatusEnum == null) {
            return null;
        }
        return taskInvokeStatusEnum.name();
    }

    @Override
    public TaskInvokeStatusEnum convertToEntityAttribute(String name) {
        if (name == null) {
            return null;
        }
        return TaskInvokeStatusEnum.valueOf(name);
    }
}
