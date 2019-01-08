package com.xxq.rest.rabbitmq.demo1.demo6;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * 任务发布者
 */
public class EmitProduceLog {
    // 转发器名称
    private static final String EXCHANGE_NAME = "ex_log";

    public static void main(String[] args) {

        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localHost");
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            // 第一参数 转发器名称 ，第二个 策略
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String message = new Date().toLocaleString() + " : 小强修的世间无上法 ";
            // 向转发器发送信息,因为是发布订阅，所以只要是这个转发器上的队列，全部发布信息
            // 第一个参数，转发器，第二个参数，队列名称；第三参数是否持久化；第四个参数是信息；
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
            System.out.println("send msg " + message);
            // 关闭频道，以及连接
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


    }


}
