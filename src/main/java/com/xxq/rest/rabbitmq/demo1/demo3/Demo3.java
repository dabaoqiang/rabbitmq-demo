package com.xxq.rest.rabbitmq.demo1.demo3;

/**
 * 2、 消息应答（message acknowledgments）
 *
 * 执行一个任务需要花费几秒钟。你可能会担心当一个工作者在执行任务时发生中断。
 * 我们上面demo2的代码，一旦RabbItMQ交付了一个信息给消费者，会马上从内存中移除这个信息。在这种情况下，如果杀死正在执行任务的某个工作者，我们会丢失它正在处理的信息。
 * 我们也会丢失已经转发给这个工作者且它还未执行的消息。
 */
public class Demo3 {

    public static void main(String[] args) {

        WorkAcknowledgments work = new WorkAcknowledgments();
        new  Thread(work).start();
        new  Thread(work).start();
    }
}
