java-amqp-to-http-worker-queue
==============================

This is a simple Java app that will connect to an AMQP based message queue, such as RabbitMQ and push message contents to an endpoint URL.  It requires the RabbitMQ JAR files.

I will say right now that I am not an experienced Java developer in that I do a lot of programming with Java.  However, I do know the language fairly well and so it tends to be my goto when I need something daemon related.

This project is based off of a need to have a web-based application push messages (which are just serialized classes to be executed elsewhere) into a message queue such as RabbitMQ and push them to an HTTP-based endpoint.  I checked around and didn't see anything that did this in the manner that I was expecting.  Additionally, I tried writing this in Node but my Node chops are nothing to write home about and I found that managing connection pools and other resource limitations were going to take me longer than I wanted to spend.  So I fell back on Java since I knew how to do all of these things in Java.

If you are a Java developer who comes across this and vomits on your screen when you see the code please feel free to re-implement, as long as the simplicity remains.  My intent is to have this work like the following:

~~~
web 1 \                                                                   / HTTP server 1
web 2 - message queue -> java-amqp-to-http-worker-queue -> load balancer  - HTTP server 2
web 3 /                                                                   \ HTTP server 3
~~~
