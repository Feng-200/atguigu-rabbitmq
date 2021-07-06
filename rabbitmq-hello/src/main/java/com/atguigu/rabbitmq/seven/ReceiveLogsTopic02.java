package com.atguigu.rabbitmq.seven;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author Feng
 * @date 2021/7/4 22:18
 * <p>
 * 声明主题交换机及队列
 */
public class ReceiveLogsTopic02 {
    //交换机名称
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        //声明Q1队列与绑定关系
        String queueName = "Q2";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");
        System.out.println(" 等待接收消息.....");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("接收队列:" + queueName + "绑定键:" + delivery.getEnvelope().getRoutingKey() + ",消息:" + message);
        };
        //接收消息
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
