package com.xxq.rest.rabbitmq.demo1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 接受者
 *
 *
 * RabbitMQ 是信息传输的中间者。
 * 本质上，他从生产者（producers）接收消息，转发这些消息给消费者（consumers）.换句话说，他能够按根据你指定的规则进行消息转发、缓冲、和持久化。
 *
 * RabbitMQ 的一些常见的术语：
 * Producing意味着无非是发送。一个发送消息的程序是一个producer(生产者)。
 * Queue（队列）类似邮箱。依存于RabbitMQ内部。虽然消息通过RabbitMQ在你的应用中传递，但是它们只能存储在queue中。
 * 队列不受任何限制，可以存储任何数量的消息—本质上是一个无限制的缓存。
 * 很多producers可以通过同一个队列发送消息，相同的很多consumers可以从同一个队列上接收消息。
 * Consuming（消费）类似于接收。consumer是基本属于等待接收消息的程序。
 * 注意：producer（生产者）,consumer（消费者）,broker（RabbitMQ服务）并不需要部署在同一台机器上，实际上在大多数实际的应用中，也不会部署在同一台机器上。
 *
 * 2、Java入门实例
 *  一个producer发送消息，一个接收者接收消息，并在控制台打印出来.
 *
 */
public class Rec {

    // 队列名称
    private final static  String QUEUE_NAME ="hello";

    public static void main(String[] args) {
        try {
            // 打开工厂连接，设定工厂的ip
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            // 设定该工厂的链接
            Connection connection = connectionFactory.newConnection();
            // 指定频道;
            Channel channel = connection.createChannel();
            //  设定频道所对的消息队列
            channel.queueDeclare(QUEUE_NAME, false,false,false,null);
            System.out.println("wait message");
            // 指定消费者
            QueueingConsumer consumer = new QueueingConsumer(channel);
            // 指定消费队列的名称
            // 第一个参数：所订阅的队列
            // 第二个参数：是否开启自动应答，默认是开启的，如果需要手动应答应该设置为false
            // 注意：为了确保消息一定被消费者处理，rabbitMQ提供了消息确认功能，
            // 就是在消费者处理完任务之后，就给服务器一个回馈，服务器就会将该消息删除， 如果消费者超时不回馈，那么服务器将就将该消息重新发送给其他消费者，
            // 当autoAck设置为true时，只要消息被消费者处理，不管成功与否，服务器都会删除该消息，
            // 而当autoAck设置为false时，只有消息被处理，且反馈结果后才会删除。
            // 第三参数：接收到消息之后执行的回调方法
            channel.basicConsume(QUEUE_NAME, true, consumer);
            while (true){
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String msg = new String(delivery.getBody());
                System.out.println("recived " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }



}
