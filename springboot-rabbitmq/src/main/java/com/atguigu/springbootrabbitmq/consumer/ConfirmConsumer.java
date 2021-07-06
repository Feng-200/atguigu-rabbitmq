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
 *
 */

@Slf4j
@Component
public class ConfirmConsumer {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveMsg(Message message) {
        String msg = new
                String(message.getBody());
        log.info("接受到队列 confirm.queue 消息:{}", msg);
    }

}
