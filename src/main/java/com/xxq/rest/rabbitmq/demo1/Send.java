package com.xxq.rest.rabbitmq.demo1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送者
 * 具体参数参考：
 * https://blog.csdn.net/leisure_life/article/details/78663244
 */
public class Send {

    //队列名称
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {

        try {
            // 创建连接到rabbitmq工厂类
            ConnectionFactory connectionFactory = new ConnectionFactory();
            // 设置rabbitMq所在的主机名
            connectionFactory.setHost("localhost");
            // 创建一个链接
            Connection connection = connectionFactory.newConnection();
            // 打开一个频道
            Channel channel = connection.createChannel();
            // 指定某个频道里的某个队列
            // 第一参数 队列名称
            // 第二个参数 是否队列持久化
            // 第三参数 是否为独享队列（排他性队列），只有自己可见的队列，即不允许其它用户访问
            // 第四个参数 当没有任何消费者使用时，自动删除该队列
            // 第五个参数 其他参数
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // 设置发送的消息
            String msg = "hello word";
            // 往队列填充一条信息
            // 第一个参数：指定转发器名称—-ExchangeName，这里用空字符串，就表示消息会交给默认的Exchange
            // 第二个参数：发布到哪个队列
            // 第三参数：和消息有关的其他配置参数，路由报头等
            // 第四个参数：消息体
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            System.out.println("sent msg " + msg);
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


