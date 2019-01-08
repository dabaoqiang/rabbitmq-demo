package com.xxq.rest.rabbitmq.demo1.Demo5;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 公平转发task，持久化task，应答task
 */
public class FairTaskPodurce {

    // 队列名称
    private static final String QUEUE_NAME = "xiaoqiang_workQueue";

    public static void main(String[] args) {

        try {
            // 打开远程某台机器的工厂实例
            ConnectionFactory connectionFactory = new ConnectionFactory();
            // 设定机器的ip
            connectionFactory.setHost("localHost");
            // 从工厂获取连接实例
            Connection connection = connectionFactory.newConnection();
            // 从连接中获取一个频道实例
            Channel channel = connection.createChannel();
            // 第一个参数 声明指定频道所对应的队列
            // 第二个参数 队列持久化，默认否
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            // 发送10条信息
            for (int i = 0; i < 10; i++) {
                String dots = "";
                for (int j = 0; j <= i; j++) {
                    dots += ".";
                }
                String msg = "helloworld" + dots + dots.length();
                // 设置消息发布并且持久化
                // 第一参数 交换机，第二个参数：队列名称，第三个参数：消息的持久化策略，第四个是消息的字节流
                channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
                System.out.println("send msg :" + msg);
            }
            // 关闭通道，与连接
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }


}
