package com.kwetter.jms;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * Created by hein on 5/25/17.
 */
public class MessageBuilder {

    public MessageBuilder(String message, String username) {
        JsonObject object = Json.createObjectBuilder()
                .add("message", message)
                .add("username", username)
                .build();
        System.out.println(object.toString());
        sendTweet(object.toString());
    }

    private void sendTweet(String message) {
        Connection connection = null;
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            connection = factory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("tweetq");
            MessageProducer producer = session.createProducer(queue);
            producer.send(session.createTextMessage(message));
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
