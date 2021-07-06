package com.atguigu.springbootrabbitmq.consumer;

import com.atguigu.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Feng
 * @date 2021/7/6 16:02
 *
 * 发布确认高级  消费者
 * 报警消费者
 */

@Slf4j
@Component
public class WaringConsumer {

    //接收报警信息
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWaringMsg(Message message) {
        String msg = new String(message.getBody());
        log.error("报警发现不可路由消息：{}", msg);
    }

}
