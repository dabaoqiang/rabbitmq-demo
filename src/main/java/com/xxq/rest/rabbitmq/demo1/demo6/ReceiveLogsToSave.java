package com.xxq.rest.rabbitmq.demo1.demo6;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import java.util.logging.SimpleFormatter;

/**
 * 接收端1：写入日志文件
 */
public class ReceiveLogsToSave implements Runnable {

    //  定义转转发器
    private static final String EXCHANGE_NAME = "ex_log";

    public static void testRecive() {

        try {
            try {
                System.out.println(Thread.currentThread().getName()+ " 当前线程 开始监听");
                ConnectionFactory connectionFactory = new ConnectionFactory();
                connectionFactory.setHost("localhost");
                Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel();
                //  转发器声明 第一个参数 转发器名称，第二个参数 策略，
                channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
                //  创建一个非持久的，且唯一的，并且自动删除的队列
                String queueName = channel.queueDeclare().getQueue();
                //  为转发器指定队列 第一参数 队列名称，第二个转发器名称，第三个参数...
                channel.queueBind(queueName, EXCHANGE_NAME, "");
                System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
                // 消息队列
                QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
                // 进行消费  第一参数 队列名称，指定接收者，第二个参数(true)为自动应答，无需手动应答；第三个参数消费队列
                channel.basicConsume(queueName, true, queueingConsumer);
                while (true) {
                    QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
                    String message = new String(delivery.getBody());
                    print2File(message);
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


    @Override
    public void run() {
        testRecive();
    }

    /**
     * 输出
     * @param msg
     */
    private static void print2File(String msg) {
        try {
            System.out.println("当前线程" + Thread.currentThread().getName() + " 获取信息：" + msg);
            String dir = ReceiveLogsToSave.class.getClassLoader().getResource("").getPath();
            System.out.println("当前线程" + Thread.currentThread().getName() + "的地址是：" + dir);
            String logFoleName = new SimpleDateFormat("YYYY-MM-dd").format(new Date());
            File file = new File(dir, logFoleName + ".txt");
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write((msg + "\r\n").getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
