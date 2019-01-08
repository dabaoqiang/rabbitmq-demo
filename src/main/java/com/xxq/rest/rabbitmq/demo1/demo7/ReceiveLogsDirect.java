package com.xxq.rest.rabbitmq.demo1.demo7;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * 随机发送6条随机类型（routing key）的日志给转发器
 */
public class ReceiveLogsDirect implements Runnable {
    private static final String EXCHANGE_NAME = "ex_logs_direct";
    private static final String[] SEVERITIES = {"info", "warning", "error"};

    public static void consumer() throws IOException, TimeoutException, InterruptedException {
        System.out.println("当前线程：" + Thread.currentThread().getName() + " 开始进行消费。");
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localHost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        // 转发器类型
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();
        String routingKey = getSeverity();
        // 指定binding_key 频道绑定指定转发器的路由
        // 第一个参数：队列名称
        // 第二个参数：转发器名称
        // 第三个参数：路由名称
        channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
        System.out.println(" [*] Waiting for " + routingKey + " logs. To exit press CTRL+C");
        QueueingConsumer consumer = new QueueingConsumer(channel);
        // String queue, boolean autoAck, Consumer callback
        channel.basicConsume(queueName, true, consumer);
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println("线程 :" + Thread.currentThread().getName() + "获取信息 ："  + routingKey  + message);
        }
    }


    /**
     * 随机产生一种日志类型
     *
     * @return
     */
    private static String getSeverity() {
        Random random = new Random();
        int ranVal = random.nextInt(3);
        return SEVERITIES[ranVal];
    }


    @Override
    public void run() {
        try {
            consumer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
