package com.atguigu.springbootrabbitmq.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Feng
 * @date 2021/7/6 0:37
 *
 * Rabbitmq 插件实现延迟队列  下载插件
 * 声明交换机，队列，routingKey
 *  * 交换机与队列绑定
 */
@Configuration
public class DelayedQueueConfig {
    //队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    //交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    //routingKey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    //声明交换机  基于插件的
    @Bean
    public CustomExchange delayedExchange() {

        Map<String,Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        /**
         *  1.交换机的名称
         *  2.交换机的类型
         *  3.是否需要持久化
         *  4.是否需要白动删除
         *  5.其它的参数
         */

        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, args);

    }
    //声明队列
    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    //绑定交换机和队列
    @Bean
    public Binding bindingDelayedQueue(@Qualifier("delayedQueue") Queue queue,
                                       @Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(queue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}

