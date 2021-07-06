package com.atguigu.rabbitmq.eight;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Feng
 * @date 2021/7/5 13:37
 * <p>
 * 死信队列实战
 */
public class Consumer01 {
    //普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";
    //普通队列名称
    private static final String NORMAL_QUEUE = "normal_queue";
    //死信队列名称
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();


        //声明死信和普通交换机 类型为 direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        //声明死信队列
        String deadQueue = "dead-queue";
        channel.queueDeclare(deadQueue, false, false, false, null);
        //死信队列绑定死信交换机与 routingkey
        channel.queueBind(deadQueue, DEAD_EXCHANGE, "lisi");
        //正常队列绑定死信队列信息
        Map<String, Object> params = new HashMap<>();
        //正常队列设置死信交换机 参数 key 是固定值
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //正常队列设置死信 routing-key 参数 key 是固定值
        params.put("x-dead-letter-routing-key", "lisi");
        //设置正常队列的长度的限制
//        params. put("x-max-length",6) ;
        String normalQueue = "normal-queue";
        channel.queueDeclare(normalQueue, false, false, false, params);
        channel.queueBind(normalQueue, NORMAL_EXCHANGE, "zhangsan");
        System.out.println("等待接收消息........... ");
        /*DeliverCallback deliverCallback = (consumerTag, delivery) ->
        {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Consumer01 接收到消息" + message);
        };*/

        DeliverCallback deliverCallback = (consumerTag, delivery) ->
        {
            String message = new String(delivery.getBody(), "UTF-8");
            if (message.equals("info5")) {
                System.out.println("Consumer01 接收到消息" + message + "并拒绝签收该消息");
            //requeue 设置为 false 代表拒绝重新入队 该队列如果配置了死信交换机将发送到死信队列中
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer01 接收到消息" + message);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        channel.basicConsume(normalQueue, false, deliverCallback, consumerTag -> {
        });


        /*//声明死信和普通交换机 类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
        //绑定死信的交换机与死信的队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        //声明普通队列
        Map<String, Object> params = new HashMap<>();

        //过期时间10s=10000ms
        // arguments.put ("x-message-ttl",100000) ;

        //正常队列设置死信交换机 参数key是固定值
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //正常队列设置死信routing-key 参数key是固定值
        params.put("x-dead-letter-routing-key", "lisi");
        //设置正常队列的长度的限制
        params. put("x-max-length",6) ;

        channel.queueDeclare(NORMAL_QUEUE, false, false, false, params);

        //绑定普通的交换机与普通的队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, " zhangsan");

        System.out.println("等待接收消息........... ");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Consumer01接收到消息" + message);
        };
        channel.basicConsume(NORMAL_QUEUE, true, deliverCallback, consumerTag -> {
        });*/


    }
}

