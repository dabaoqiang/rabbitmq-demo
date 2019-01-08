package com.xxq.rest.rabbitmq.demo1.demo6;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * 控制器日志打印
 */
public class ReceiveLogsToConsole implements  Runnable {

    // 转发器
    private static  final String EXCHANGE_NAME = "ex_log";

    public static void text() {
     try {
         System.out.println(Thread.currentThread().getName()+ " 当前线程 开始监听");
         ConnectionFactory connectionFactory = new ConnectionFactory();
         connectionFactory.setHost("localHost");
         Connection connection = connectionFactory.newConnection();
         Channel channel = connection.createChannel();
         String queueName = channel.queueDeclare().getQueue();
         // 第一个参数 队列名称，第二个参数转发器；
         channel.queueBind(queueName, EXCHANGE_NAME, "");
         System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
         QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
         // 第一参数：队列名称，第二个参数 自动回答，第三个参数 消息队列
         channel.basicConsume(queueName,true, queueingConsumer);
         while (true) {
             QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
             String message = new String(delivery.getBody());
             System.out.println(Thread.currentThread().getName() + ": 获取信息 ：" + message);
         }

     }catch (Exception e){
         e.getStackTrace();
     }

    }

    @Override
    public void run() {
        text();
    }
}
