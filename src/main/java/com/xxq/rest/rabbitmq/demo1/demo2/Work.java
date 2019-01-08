package com.xxq.rest.rabbitmq.demo1.demo2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费端
 */
public class Work implements Runnable {
    // 指定队列
    private static final String QUEUE_NAME = "workQueue";
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

    @Override
    public void run() {
        try {
            try {
                System.out.println(Thread.currentThread().getName()+"当前线程进入. . . ");
                // 区分不同工作进程的输出
                int hashCode = Work.class.hashCode();
                // 根据IP创建工程
                ConnectionFactory connectionFactory = new ConnectionFactory();
                // 指定IP
                connectionFactory.setHost("localhost");
                // 工厂创建一个链接
                Connection connection = connectionFactory.newConnection();
                // 获取一个频道实例
                Channel channel = connection.createChannel();
                // 频道绑定队列名称
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                System.out.println("频道，进入绑定状态，开始可以接受信息！");
                // 该频道进行消费
                QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
                // 频道指定消费队列名称；
                channel.basicConsume(QUEUE_NAME, true, queueingConsumer);
                while (true) {
                    QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
                    String msg = new String(delivery.getBody());
                    System.out.println(hashCode + "recived" + msg + "");
                    doWork(msg);
                    System.out.println(Thread.currentThread().getName() + "当前线程跑完");
                    System.out.println("done");
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
}
