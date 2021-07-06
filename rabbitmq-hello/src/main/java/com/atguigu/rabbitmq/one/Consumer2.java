package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.*;

/**
 * @author Feng
 * @date 2021/7/3 23:10
 *
 * Federation Exchange
 */

public class Consumer2 {
    //队列
    private final static String QUEUE_NAME = "mirrior_hello";
    //交换机
    private final static String FED_EXCHANGE = "fed_exchange";

    public static void main(String[] args) throws Exception {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.6.140");
        factory.setUsername("admin");
        factory.setPassword("123");
        //channel实现了自动close接口 自动关闭 不需要显示关闭
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(FED_EXCHANGE, BuiltinExchangeType.DIRECT);

            /*channel.queueDeclare("node2_queue", true, false, false, null);
            channel.queueBind("node2_queue", FED_EXCHANGE, "routeKey");*/
            channel.queueDeclare("fed.queue", true, false, false, null);
            channel.queueBind("fed.queue", FED_EXCHANGE, "routeKey");
//            channel.queueDeclare("Q1", true, false, false, null);
//            channel.queueBind("Q1", FED_EXCHANGE, "routeKey");
            System.out.println("等待接收消息....");
            //推送的消息如何进行消费的接口回调
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());
                System.out.println(message);
            };
            //取消消费的一个回调接口 如在消费的时候队列被删除掉了
            CancelCallback cancelCallback = (consumerTag) -> {
                System.out.println("消息消费被中断");
            };
            /** * 消费者消费消息 *
             * 1.消费哪个队列 *
             * 2.消费成功之后是否要自动应答 true代表自动应答 false手动应答 *
             * 3.消费者未成功消费的回调
             * 4.消费者取消消费的回调
             */
            channel.basicConsume("fed.queue", true, deliverCallback, cancelCallback);
        }
    }
}
