package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * @author Feng
 * @date 2021/7/4 15:06
 * <p>
 * 消息在手动应答时是不丢失、放回队列中重新消费
 */
public class Task02 {
    private static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] argv) throws Exception {

        try (Channel channel = RabbitMqUtils.getChannel()) {

            //开启发布确认
            channel.confirmSelect() ;
            //让消息持久化
            boolean durable = true;
            //声明队列
            channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
            //从控制台输入信息
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入信息");
            while (sc.hasNext()) {
                String message = sc.nextLine();
                //设置生产者发送消息为持久化消息（要求保存到磁盘上）保存在内存中  MessageProperties.PERSISTENT_TEXT_PLAIN
                channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
                System.out.println("生产者发出消息" + message);
            }
        }
    }
}
