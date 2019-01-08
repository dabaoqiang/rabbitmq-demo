package com.xxq.rest.rabbitmq.demo1.Demo5;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.omg.PortableServer.THREAD_POLICY_ID;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 公平消费，消费应答
 */
public class FairConsume implements Runnable {

    // 队列名称
    private static final String QUEUE_NAME = "xiaoqiang_workQueue";

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " ： 当前线程启动准备消费");
        try {
            try {
                ConnectionFactory connectionFactory = new ConnectionFactory();
                connectionFactory.setHost("localHost");
                Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel();
                // 绑定对应的队列频道，
                // 第一参数 队列名称，第二参数 队列是否持久
                channel.queueDeclare(QUEUE_NAME, true, false, false, null);
                System.out.println("当前线程 ：" + Thread.currentThread().getName() + " awaiting msg ");
                // 设置转发信息
                // 设置最大服务转发消息数量
                channel.basicQos(1);
                //  设置队列消息监听对象
                QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
                // 设置消费 第一个参数 队列名称，第二个参数 应答机制（开启是false）消息收取完返回通知，删除该信息；第三参数 ： 绑定频道的队列消费对象
                channel.basicConsume(QUEUE_NAME, false, queueingConsumer);
                while (true) {
                    QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
                    String message = new String(delivery.getBody());
                    doWork(message);
                    System.out.println(Thread.currentThread().getName() + " [x] Received '" + message + "'");
                    System.out.println(Thread.currentThread().getName() + " [x] Done");
                    // 消费完成，返回通知
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 模式耗时任务完成 暂停一秒
     *
     * @param task
     */
    private static void doWork(String task) {

        try {
            for (char ch : task.toCharArray()) {
                if (ch == '.')
                    Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
