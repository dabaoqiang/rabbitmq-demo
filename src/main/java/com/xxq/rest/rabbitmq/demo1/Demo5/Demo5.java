package com.xxq.rest.rabbitmq.demo1.Demo5;

/**
 * 4、公平转发（Fair dispatch） (fe l)
 * 或许会发现，目前的消息转发机制（Round-robin）并非是我们想要的。
 * 例如，这样一种情况，对于两个消费者，有一系列的任务，奇数任务特别耗时，而偶数任务却很轻松，这样造成一个消费者一直繁忙，另一个消费者却很快执行完任务后等待。
 * 造成这样的原因是因为RabbitMQ仅仅是当消息到达队列进行转发消息。
 * 并不在乎有多少任务消费者并未传递一个应答给RabbitMQ。
 * 仅仅盲目转发所有的奇数给一个消费者，偶数给另一个消费者。
 * 为了解决这样的问题，我们可以使用basicQos方法，传递参数为prefetchCount = 1。
 * 这样告诉RabbitMQ不要在同一时间给一个消费者超过一条消息。换句话说，只有在消费者空闲的时候会发送下一条信息。
 * 配置项：
 * int prefetchCount = 1;
 * channel.basicQos(prefetchCount);
 *
 * 注：如果所有的工作者都处于繁忙状态，你的队列有可能被填充满。
 * 你可能会观察队列的使用情况，然后增加工作者，或者使用别的什么策略。
 * 测试：改变发送消息的代码，将消息末尾点数改为6-2个，然后首先开启两个工作者，接着发送消息：
 *
 * 效果：
 * 可以看出此时并没有按照之前的Round-robin机制进行转发消息，而是当消费者不忙时进行转发。
 * 且这种模式下支持动态增加消费者，因为消息并没有发送出去，动态增加了消费者马上投入工作。
 * 而默认的转发机制会造成，即使动态增加了消费者，此时的消息已经分配完毕，无法立即加入工作，即使有很多未完成的任务。
 */
public class Demo5 {

    public static void main(String[] args) {
        FairConsume work = new FairConsume();
        new  Thread(work).start();
        new  Thread(work).start();

    }

}
