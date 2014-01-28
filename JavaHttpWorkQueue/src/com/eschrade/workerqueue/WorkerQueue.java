package com.eschrade.workerqueue;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WorkerQueue {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			File fXmlFile = new File("queues.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			XPath query = XPathFactory.newInstance().newXPath();
			XPathExpression expression = query.compile("/queues/queue");
			NodeList nodes = (NodeList)expression.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				QueueWorkerThreadGroup wg = new QueueWorkerThreadGroup();
				Element node = (Element)nodes.item(i);
				
				for (int ni = 0; ni < node.getChildNodes().getLength(); ni++) {
					Node configItem = node.getChildNodes().item(ni);
					if ("username".equals(configItem.getNodeName())) {
						wg.setUserName(configItem.getTextContent());
					} else if ("virtualHost".equals(configItem.getNodeName())) {
						wg.setVirtualHost(configItem.getTextContent());
					} else if ("password".equals(configItem.getNodeName())) {
						wg.setPassword(configItem.getTextContent());
					} else if ("hostName".equals(configItem.getNodeName())) {
						wg.setHostName(configItem.getTextContent());
					} else if ("port".equals(configItem.getNodeName())) {
						wg.setPort(Integer.parseInt(configItem.getTextContent()));
					} else if ("queueName".equals(configItem.getNodeName())) {
						wg.setQueueName(configItem.getTextContent());
					} else if ("workerCount".equals(configItem.getNodeName())) {
						wg.setWorkerCount(Integer.parseInt(configItem.getTextContent()));
					} else if ("httpQueueUrl".equals(configItem.getNodeName())) {
						wg.setHttpQueueUrl(configItem.getTextContent());
					}
				}
				wg.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
