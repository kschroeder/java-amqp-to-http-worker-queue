package com.eschrade.workerqueue;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class HttpConsumer extends DefaultConsumer
{
	protected String httpQueueUrl;

	public HttpConsumer(Channel channel, String httpQueueUrl) {
		super(channel);
		this.httpQueueUrl = httpQueueUrl;
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope,
			BasicProperties properties, byte[] body) throws IOException {
		
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(httpQueueUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(body.length));
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			String message = new String(body);
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