package com.xxq.rest.rabbitmq.demo1.demo2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产端
 */
public class NewTask {

    // 队列名称
    private static final String QUEUE_NAME = "workQueue";

    public static void main(String[] args) {
        try {
            // 指定某个机器上的链接
            ConnectionFactory connectionFactory = new ConnectionFactory();
            // 指定IP
            connectionFactory.setHost("localHost");
            // 打开链接
            Connection connection = connectionFactory.newConnection();
            // 打开链接获取某个通道实例
            Channel channel = connection.createChannel();
            // 指定通道需要所绑定的指定队列，指定固定的通道
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            for (int i = 0; i <= 10; i++) {
                String dtos = "";
                for (int j = 0; j <= i; j++) {
                    dtos += ".";
                }
                String message = "helloWord" + dtos + dtos.length();
                // 通道发布,频道发布信息，到队列QUEUE_NAME
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println("sent" + message + "'");
            }
            // 关闭频道
            channel.close();
            // 关闭链接
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
