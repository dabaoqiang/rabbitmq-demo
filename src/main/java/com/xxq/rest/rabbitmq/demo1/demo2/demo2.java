package com.xxq.rest.rabbitmq.demo1.demo2;

/**
 * 绍如何通过一个工作队列（queue）分发耗时任务给不同的工作者(consumers)。
 * Round-robin 轮询调度算法
 * 使用任务队列的好处是能够很容易的并行工作。如果我们积压了很多工作，我们仅仅通过增加更多的工作者就可以解决问题，使系统的伸缩性更加容易。
 * 下面我们先运行3个工作者（WorkAcknowledgments.java）实例，然后运行NewTask.java，3个工作者实例都会得到信息。
 */
public class demo2 {
    public static void main(String[] args) {
        Work work = new Work();
        new  Thread(work).start();
        new  Thread(work).start();
        new  Thread(work).start();

    }
}
