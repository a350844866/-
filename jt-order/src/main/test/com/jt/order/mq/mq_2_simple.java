package com.jt.order.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;


public class mq_2_simple {

    private Connection connection = null;

    //表示初始化链接
    //@Before
    public void initConnection() throws IOException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设定连接的参数 IP地址 虚拟主机 用户名和密码
        connectionFactory.setHost("192.168.159.136");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/jt");
        connectionFactory.setUsername("jtadmin");
        connectionFactory.setPassword("jtadmin");
        connection = connectionFactory.newConnection();


    }

    //编辑消息的生产者 生产消息
    //@Test
    public void provider() throws IOException {
        //1.创建通道 只有通过通道才能连接RabbitMQ
        Channel channel = connection.createChannel();

        //定义队列的名称
        String queue = "work_1";

        //设定每个队列执行消息的个数 一般都为1 一般最多运行一个队列在没有返回值的情况下拿3个
        channel.basicQos(1);

        //3.声明队列
        /**
         * 参数说明
         * 1.queue 表示队列的名称
         * 2.durable 是否持久化 一般都不会人为的保存消息 一般都为false
         * 3.exclusive 如果该属性为true 当前的队列消息只属于生产者 消费者不能调用 一般为false
         * 4.autoDelete 如果消息队列中没有信息 则自动删除 所以一般为false 不进行删除操作 保留队列
         * 5.arguments 格外的扩充参数 一般不用   为null
         */
        channel.queueDeclare(queue, false, false, false, null);
        //表示消息
        String msg = "我是生产者 发红包的 你们来抢,抢到今晚吃鸡";

        //消息队列发布
        /**
         * 参数说明
         * 1.exchange 表示交换机的名称 如果没有交换机 写""
         * 2.routingkey 路由key 标识信息的执行的关键字  只有满足关键字的消费者才能消费
         * 3.props: 多于的参数 一般不用
         * 4.body: 消息的字节数组格式
         */
        System.out.println("生产者生产消息成功");

        while (true) {
            channel.basicPublish("", queue, null, msg.getBytes());
        }

    }

    //@Test
    public void consumer() throws IOException, InterruptedException {
        //1.配置通道
        Channel channel = connection.createChannel();

        //2.定义队列名称
        String queue = "work_1";

        //3.定义队列
        channel.queueDeclare(queue, false, false, false, null);

        //4.定义消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        //5.定义消费者的回传方式 如果为true表示消费后自动回复 如果为false 表示消费后需要通过手动回复
        channel.basicConsume(queue, false, consumer);
        while (true) {
            //获取消息队列
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = "消费者1" + new String(delivery.getBody());
            System.out.println(msg);
            //表示返回值的结果
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }

    }


    //@Test
    public void consumer2() throws IOException, InterruptedException {
        //1.配置通道
        Channel channel = connection.createChannel();

        //2.定义队列名称
        String queue = "work_1";

        //3.定义队列
        channel.queueDeclare(queue, false, false, false, null);

        //4.定义消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        //5.定义消费者的回传方式 如果为true表示消费后自动回复 如果为false 表示消费后需要通过手动回复
        channel.basicConsume(queue, false, consumer);
        while (true) {
            //获取消息队列
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = "消费者2" + new String(delivery.getBody());
            System.out.println(msg);
            //表示返回值的结果
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }

    }

}
