package com.xxq.rest.rabbitmq.demo1.demo7;

/**
 * https://blog.csdn.net/lmj623565791/article/details/37669573
 * RabbitMQ （四） 路由选择 (Routing)
 * 按某路线发送
 *
 * 引入：
 * 上一篇博客我们建立了一个简单的日志系统，我们能够广播日志消息给所有你的接收者，如果你不了解，请查看：RabbitMQ （三） 发布/订阅。
 * 本篇博客我们准备给日志系统添加新的特性，让日志接收者能够订阅部分消息。
 * 例如，我们可以仅仅将致命的错误写入日志文件，然而仍然在控制面板上打印出所有的其他类型的日志消息。
 *
 *1、绑定（Bindings）
 * 在上一篇博客中我们已经使用过绑定。类似下面的代码：
 * channel.queueBind(queueName, EXCHANGE_NAME, "");
 * 绑定表示转发器与队列之间的关系。我们也可以简单的认为：队列对该转发器上的消息感兴趣。
 * 绑定可以附带一个额外的参数routingKey。为了与避免basicPublish方法（发布消息的方法）的参数混淆，我们准备把它称作绑定键（binding key）。
 * 下面展示如何使用绑定键（binding key）来创建一个绑定：
 * channel.queueBind(queueName, EXCHANGE_NAME, "black");
 * 绑定键的意义依赖于转发器的类型。对于fanout类型，忽略此参数。
 *
 *2、直接转发（Direct exchange）
 * 上一篇的日志系统广播所有的消息给所有的消费者。
 * 我们希望可以对其扩展，来允许根据日志的严重性进行过滤日志。
 * 例如：我们可能希望把致命类型的错误写入硬盘，而不把硬盘空间浪费在警告或者消息类型的日志上。
 * 之前我们使用fanout类型的转发器，但是并没有给我们带来更多的灵活性：仅仅可以愚蠢的转发。
 * 我们将会使用direct类型的转发器进行替代。
 * direct类型的转发器背后的路由转发算法很简单：消息会被推送至绑定键（binding key）和消息发布附带的选择键（routing key）完全匹配的队列。
 ，我们可以看到direct类型的转发器与两个队列绑定。第一个队列与绑定键orange绑定，第二个队列与转发器间有两个绑定，一个与绑定键black绑定，另一个与green绑定键绑定。
 * 这样的话，当一个消息附带一个选择键（routing key） orange发布至转发器将会被导向到队列Q1。消息附带一个选择键（routing key）black或者green将会被导向到Q2.所有的其他的消息将会被丢弃。
 *
 * 3、多重绑定（multiple bindings）
 * 使用一个绑定键（binding key）绑定多个队列是完全合法的。
 *
 *
 * 结论：
 * 可以看到我们实现了博文开头所描述的特性，接收者可以自定义自己感兴趣类型的日志。
 * 其实文章这么长就在说：发送消息时可以设置routing_key，接收队列与转发器间可以设置binding_key，接收者接收与binding_key与routing_key相同的消息。
 *
 */
public class Demo7 {

    public static void main(String[] args) {
        ReceiveLogsDirect receiveLogsDirect = new ReceiveLogsDirect();
        new Thread(receiveLogsDirect).start();
        new Thread(receiveLogsDirect).start();
        new Thread(receiveLogsDirect).start();
    }



}
