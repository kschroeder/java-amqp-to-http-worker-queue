package com.eschrade.workerqueue;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import com.rabbitmq.client.Channel;

public class Worker extends Thread {
	protected QueueWorkerThreadGroup threadGroup;
	protected Channel channel;
	protected String queueUrl;
	protected String queueName;
	protected HttpConsumer consumer;
	protected LinkedList<Worker> workers;
	protected String message;
	protected boolean available = false;



	public void run() {
		while (true) {

			workers.add(this);
			available = true;
			workers.notify();
			try {
				wait();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			available = false;
			URL url;
			HttpURLConnection connection = null;
			try {
				// Create connection
				url = new URL(queueUrl);
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Length",
						"" + Integer.toString(message.length()));
				connection.setUseCaches(false);
				connection.setDoInput(true);
				connection.setDoOutput(true);

				// Send request
				DataOutputStream wr = new DataOutputStream(
						connection.getOutputStream());
				wr.writeBytes(message);
				wr.flush();
				wr.close();

				// Get Response
				InputStream is = connection.getInputStream();
				is.read();
			} catch (Exception e) {
				e.printStackTrace();
			}
			connection.disconnect();

		}
	}
}
