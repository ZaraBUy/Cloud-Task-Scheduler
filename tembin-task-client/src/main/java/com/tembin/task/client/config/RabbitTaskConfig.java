package com.tembin.task.client.config;

import com.google.common.collect.Lists;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RabbitTaskConfig {

    /** 消息交换机的名字*/
    public static final String TASK_CALLBACK_EXCHANGE = "task_callBack_queue";


    /**
     * 配置消息交换机
     FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     HeadersExchange ：通过添加属性key-value匹配
     DirectExchange:按照routing_key分发到指定队列
     TopicExchange:多关键字匹配
     */
    @Bean
    public DirectExchange taskCallBackExchanges() {
        return new DirectExchange(TASK_CALLBACK_EXCHANGE,true,false);
    }

    /**
     * 生成RabbitMQ Template
     * @param connectionFactory RabbitMQ Connection Factory
     * @return RabbitTemplate对象
     */
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter());
        return template;
    }

    /**
     * 配置消息队列
     * 针对消费者配置
     * @return
     */
    @Bean
    public List<Queue> queue() {
        List<Queue> queues= Lists.newArrayList();
        queues.add(new Queue(TASK_CALLBACK_EXCHANGE,true));
        return queues;
    }

    /**
     * 将消息队列与交换机绑定
     * 针对消费者配置
     * @return
     */
    @Bean
    public List<Binding> binding(List<Queue> queues) {
        List<Binding> bindings=Lists.newArrayList();
        for (Queue queue : queues) {
            if(queue.getName().equalsIgnoreCase(TASK_CALLBACK_EXCHANGE)){
                bindings.add(BindingBuilder.bind(queue).to(taskCallBackExchanges()).withQueueName());
            }
        }
        return bindings;
    }

    /**
     * Json序列化与反序列化工具
     * @return Jackson JSON序列化程序
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}
