package com.wuliji.pagehelper.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class ActivemqTest {
	
	/**
	 * 点到点形式发送消息
	 * @throws Exception
	 */
	@Test
	public void testQueueProducer() throws Exception{
		//1.创建一个连接工厂对象，需要指定服务的IP及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.133:61616");
		//2.使用工厂对象创建一个Collection对象
		Connection connection = connectionFactory.createConnection();
		//3.开启连接，调用对象的start方法
		connection.start();
		//4.创建一个Session对象 
		//参数一：是否开启事务（一般不开启事务，开启事务则第二个参数无意义） 参数二：应答模式。一般是自动应答和手动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.使用Session对象创建一个Destination对象。两种形式queue，topic，现在应该使用队列
		Queue queue = session.createQueue("test-queue");
		//6.使用Session对象创建一个Producer对象
		MessageProducer producer = session.createProducer(queue);
		//7.创建一个Message对象，可以使用TextMessage
		TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("hello ActiveMq");
		//8.发送消息
		producer.send(textMessage);
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testQueueConsumer() throws Exception{
		//1.创建一个ConnectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.133:61616");
		//2.创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//3.开启连接
		connection.start();
		//4.使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.创建一个Destination对象，queue对象
		Queue queue = session.createQueue("test-queue");
		//6.使用Session对象创建一个消费者对象
		MessageConsumer consumer = session.createConsumer(queue);
		//7.接受消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//打印结果
				TextMessage textMessage = (TextMessage) message;
				String text;
				try {
					text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//等待接收消息
		System.in.read();
		//8.关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	/**
	 * 广播形式发送
	 */
	@Test
	public void testTopicProducer() throws Exception{
		//1.创建一个连接工厂对象，需要指定服务的IP及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.133:61616");
		//2.使用工厂对象创建一个Collection对象
		Connection connection = connectionFactory.createConnection();
		//3.开启连接，调用对象的start方法
		connection.start();
		//4.创建一个Session对象 
		//参数一：是否开启事务（一般不开启事务，开启事务则第二个参数无意义） 参数二：应答模式。一般是自动应答和手动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.使用Session对象创建一个Destination对象。两种形式queue，topic，现在应该使用topic
		Topic topic = session.createTopic("test-topic");
		//6.使用Session对象创建一个Producer对象
		MessageProducer producer = session.createProducer(topic);
		//7.创建一个Message对象，可以使用TextMessage
		TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("hello topic message");
		//8.发送消息
		producer.send(textMessage);
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testTopicConsumer() throws Exception{
		//1.创建一个ConnectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.133:61616");
		//2.创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//3.开启连接
		connection.start();
		//4.使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.创建一个Destination对象，queue对象
		Topic topic = session.createTopic("test-topic");
		//6.使用Session对象创建一个消费者对象
		MessageConsumer consumer = session.createConsumer(topic);
		//7.接受消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//打印结果
				TextMessage textMessage = (TextMessage) message;
				String text;
				try {
					text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("topic消费者3已经启动");
		//等待接收消息
		System.in.read();
		//8.关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
