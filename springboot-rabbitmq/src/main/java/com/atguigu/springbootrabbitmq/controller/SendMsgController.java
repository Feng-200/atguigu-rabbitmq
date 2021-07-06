package com.atguigu.springbootrabbitmq.controller;

import com.atguigu.springbootrabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * @author Feng
 * @date 2021/7/5 22:48
 * <p>
 * 发行延迟消息
 * http:/ localhost:8080/ttl/sendMsg/嘻嘻嘻
 * <p>
 *
 * 开始发消息 消息TTL
 * http://localhost:8080/ttl/sendExpirationMsg/你好1/20000
 * http://localhost:8080/ttl/sendExpirationMsg/你好2/2000
 *
 *开始发消息  基于插件的消息 及 延迟的时间
 * http://localhost:8080/ttl/sendDelayMsg/come on baby1/20000
 * http://localhost:8080/ttl/sendDelayMsg/come on baby2/2000
 */
@Slf4j
@Controller
@RequestMapping("ttl")
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 开始发消息
     */
    @GetMapping("/sendMsg/{msg}")
    public void sendMsg(@PathVariable("msg") String msg) {
        /**后者会给占位符赋值，实现动态传递*/
        log.info("当前时间:{},发送一条消息给两个TTL队列", new Date(), msg);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl为10s的队列:" + msg);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl为40s的队列:" + msg);
    }

    //开始发消息 消息TTL
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message, @PathVariable String ttlTime) {
        log.info("当前时间:{},发送第一条时长{}毫秒  信息给队列C:{}", new Date(), ttlTime, message);
        rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    //开始发消息  基于插件的消息 及 延迟的时间
    @GetMapping("sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message, @PathVariable Integer delayTime) {
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY, message, msg -> {
                    //发送消息的时候延迟时长   单位:ms
                    msg.getMessageProperties().setDelay(delayTime);
                    return msg;
                });
        log.info(" 当前时间 ： {}, 发 送 一 条 延 迟 {} 毫秒的信息给队列 delayed.queue:{}", new Date(), delayTime, message);
    }



}
