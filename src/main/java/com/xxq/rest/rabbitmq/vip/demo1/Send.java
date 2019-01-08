package com.xxq.rest.rabbitmq.vip.demo1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 介绍
 * <p>
 * RabbitMQ是一个消息代理:它接受和转发消息。
 * 你可以把它想象成一个邮局:当你把你想要投寄的邮件放在一个邮箱里时，你可以确定邮递员先生或女士最终会把邮件送到你的收件人那里。
 * 在这个类比中，RabbitMQ是一个邮箱、一个邮局和一个邮递员。
 * <p>
 * RabbitMQ与邮局的主要区别在于它不处理纸张，而是接收、存储和转发二进制数据块——消息。
 * <p>
 * RabbitMQ和消息传递通常使用一些术语。
 * <p>
 * 生产就是发送。发送消息的程序是生产者 P
 * <p>
 * 队列是居住在RabbitMQ中的邮箱的名称。
 * 尽管消息流经RabbitMQ和应用程序，但它们只能存储在队列中。
 * 队列只受主机内存和磁盘限制的约束，它本质上是一个大型消息缓冲区。
 * 许多生产者可以发送消息到一个队列，许多消费者可以尝试从一个队列接收数据。这是我们表示队列的方式: queue_name
 * <p>
 * 消费和接受有着相似的含义。消费者是一个程序，主要等待接收消息:C
 * <p>
 * 注意，生产者、使用者和代理不必驻留在同一个主机上;事实上，在大多数应用程序中，它们都不是。应用程序也可以是生产者和消费者。
 * <p>
 * 在本教程的这一部分中，我们将用Java编写两个程序;发送单个消息的生产者和接收消息并将其打印出来的消费者。
 * 我们将忽略Java API中的一些细节，只关注这个非常简单的东西。这是一个短信的“你好世界”。
 * <p>
 * 在下面的图中，“P”是我们的生产者，“C”是我们的消费者。中间的框是一个队列——RabbitMQ代表使用者保存的消息缓冲区。
 * p -> queue -> C
 * <p>
 * 我们将调用消息发布者(发送方)Send和消息使用者(接收方)Recv。发布者将连接到RabbitMQ，发送一条消息，然后退出。
 * p - > Queue(hello)
 */
public class Send {

    private final static String QUEUE_NAME = "hello";

    /**
     * 连接抽象套接字连接，为我们处理协议版本协商和身份验证等。
     * 在这里，我们连接到本地机器上的代理——也就是本地主机。
     * 如果我们想连接到另一台机器上的代理，我们只需在这里指定它的名称或IP地址。
     *
     * 接下来，我们创建一个通道，这是用于完成任务的大部分API所在的地方。
     * 注意，我们可以使用带有资源的try语句，因为连接和通道都实现了java.io.Closeable。这样我们就不需要在代码中显式地关闭它们。
     *
     * 要发送，必须声明要发送到的队列;然后我们可以向队列发布一条消息，所有这些都在try with-resources语句中:
     *
     * @param args
     * @throws IOException
     * @throws TimeoutException
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localHost");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.basicPublish();

    }


}
