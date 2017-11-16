package com.jt.order.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;


public class mq_3_simple {

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

    //定于发布订阅模式
    //@Test
    public void provider() throws IOException {
        Channel channel = connection.createChannel();
        String exchange_name = "ex1";

        //定义交换机模式 参数1 表示交换机的类型
        /**
         * 参数2 交换机执行的模式
         *  fanout 发布订阅模式
         *  direct 表示路由模式
         *  topic 表示主题模式
         */
        channel.exchangeDeclare(exchange_name, "fanout");

        //发送消息

        for (int i = 0; i < 10; i++) {
            String msg = "生成者定义的发布订阅模式"+i;
            channel.basicPublish(exchange_name, "", null, msg.getBytes());
        }
    }

    //定义消费者
    //@Test
    public void consumer() throws IOException, InterruptedException {
        Channel channel = connection.createChannel();

        String queueName = "ex_c1";

        String exhange = "ex1";
        channel.queueDeclare(queueName, false, false, false, null);
        //声明交换机
        channel.exchangeDeclare(exhange, "fanout");

        channel.basicQos(1);//每次获取一次数据

        channel.queueBind(queueName, exhange, "");

        //定义消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        //设定数据的回复模式
        channel.basicConsume(queueName, false, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            System.out.println("消费者1从队列中获取数据"+new String(delivery.getBody()));
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        }

    }

    //定义消费者
    //@Test
    public void consumer2() throws IOException, InterruptedException {
        Channel channel = connection.createChannel();

        String queueName = "ex_c2";

        String exhange = "ex1";
        channel.queueDeclare(queueName, false, false, false, null);


        //声明交换机
        channel.exchangeDeclare(exhange, "fanout");

        channel.basicQos(1);//每次获取一次数据

        channel.queueBind(queueName, exhange, "");

        //定义消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        //设定数据的回复模式
        channel.basicConsume(queueName, false, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            System.out.println("消费者2从队列中获取数据"+new String(delivery.getBody()));
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        }

    }

}