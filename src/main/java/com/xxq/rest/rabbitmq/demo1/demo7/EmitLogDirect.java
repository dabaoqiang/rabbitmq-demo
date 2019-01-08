package com.xxq.rest.rabbitmq.demo1.demo7;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class EmitLogDirect {

    private static final String EXCHANGE_NAME = "ex_logs_direct";
    private static final String[] SEVERITIES = {"info", "warning", "error"};

    public static void main(String[] args) {
        produce();
    }

    public static void produce() {

        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localHost");
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            // 第一参数 ： 转发器
            // 第二个参数：方式
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            for (int i = 0; i < 10; i++) {
                String severity = getSeverity();
                String message = severity + "_log :" + UUID.randomUUID().toString();
                // 第一个参数 转发器
                // 第二个参数 那个queue，或者是 发布消息至转发器，指定routingkey
                // 第三个参数 持久化消息
                // 第四个参数 消息体
                channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
                System.out.println("sent msg " + message);
            }
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成随机日志
     *
     * @return
     */
    private static String getSeverity() {
        Random random = new Random();
        int ranVal = random.nextInt(3);
        return SEVERITIES[ranVal];
    }


}
