package com.tembin.task.client.annontation;

import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TembinTask {
    /**
     * 此处可以定义注解的相关属性
     * @return
     */
    String value() default "";

    /**
     * 时间表达式
     * @return
     */
    @NotNull
    String cornExpression();

    /**
     * 任务描述
     * @return
     */
    @NotNull
    String description();

    /**
     * 每个模块中，被任务模块调用的feign的接口名
     * @return
     */
    @NotNull
    String modularFeignName();

    /**
     * 是否禁用
     * @return
     */
    boolean isDisable() default false;

    /**
     * 是否同步
     * @return
     */
    boolean isConcurrent() default false;
}
