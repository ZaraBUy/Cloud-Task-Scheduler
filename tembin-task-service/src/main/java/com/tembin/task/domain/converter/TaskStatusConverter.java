package com.tembin.task.domain.converter;


import com.tembin.task.client.codeEnum.TaskStatusEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TaskStatusConverter implements AttributeConverter<TaskStatusEnum,String> {
    @Override
    public String convertToDatabaseColumn(TaskStatusEnum taskStatusEnum) {
        if(taskStatusEnum==null){
            return null;
        }
        return taskStatusEnum.getCode();
    }

    @Override
    public TaskStatusEnum convertToEntityAttribute(String code) {
        if(code==null){
            return null;
        }
        return TaskStatusEnum.fromValue(code);
    }
}
