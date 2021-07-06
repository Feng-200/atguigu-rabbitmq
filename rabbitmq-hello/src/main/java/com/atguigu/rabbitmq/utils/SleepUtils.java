package com.atguigu.rabbitmq.utils;

/**
 * @author Feng
 * @date 2021/7/4 15:13
 */
public class SleepUtils {
    public static void sleep(int second) {
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
