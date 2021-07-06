package com.atguigu.rabbitmq.five;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author Feng
 * @date 2021/7/4 21:05
 *
 * 消息接收
 */
public class ReceiveLogs01 {
    //交换机名称
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        /*  生成一个临时的队列 队列的名称是随机的 *
         *  当消费者断开和该队列的连接时 队列自动删除 */
        String queueName = channel.queueDeclare().getQueue();
        //把该临时队列绑定我们的exchange
        // 其中routingkey(也称之为binding key)为空字符串
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("ReceiveLogs01等待接收消息,把接收到的消息打印在屏幕.....");
        //接收消息
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("ReceiveLogs01控制台打印接收到的消息" + message);
        };
        //消费者取消消息时回调接口
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
