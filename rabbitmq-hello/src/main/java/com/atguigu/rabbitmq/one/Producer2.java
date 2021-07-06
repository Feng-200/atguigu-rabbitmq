package com.atguigu.rabbitmq.one;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Feng
 * @date 2021/7/6 18:55
 * <p>
 * 优先级队列
 * 生产者 发消息
 */
public class Producer2 {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        try (Channel channel = RabbitMqUtils.getChannel();) {

            Map<String, Object> arguments = new HashMap<>();
            //官方允许是0-255之间﹑此处设置10允许优化级范围为0-10不要设置过大浪费CPU与内存
            arguments.put("x-max-priority",10);
            channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);
            for (int i = 1; i < 11; i++) {
                String message = "info" + i;
                if (i == 5) {
                    AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                    channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
                } else {
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                }
                System.out.println("发送消息完成:" + message);
            }
        }
    }
}