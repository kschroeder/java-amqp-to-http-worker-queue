package com.eschrade.workerqueue;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class QueueWorkerThreadGroup extends Thread {
	protected String userName = "guest";
	protected String password = "guest";
	protected String virtualHost = "/";
	protected String hostName = "localhost";
	protected int port = 5672;
	protected String queueName = "jobrequests";
	protected int workerCount = 10;
	protected String httpQueueUrl = "http://localhost/queue";

	public void setHttpQueueUrl(String httpQueueUrl) {
		this.httpQueueUrl = httpQueueUrl;
	}

	public void setWorkerCount(int workerCount) {
		this.workerCount = workerCount;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void run()
	{
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername(userName);
		factory.setPassword(password);
		factory.setVirtualHost(virtualHost);
		factory.setHost(hostName);
		factory.setPort(port);
		try {
			Connection conn = factory.newConnection();
			for (int i = 0; i < workerCount; i++) {
				Channel channel = conn.createChannel();
				channel.queueDeclare(queueName, true, false, false, null);
				channel.basicConsume(queueName, true, new HttpConsumer(channel, httpQueueUrl));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
