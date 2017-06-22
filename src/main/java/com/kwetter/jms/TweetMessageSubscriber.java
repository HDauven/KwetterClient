package com.kwetter.jms;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by hein on 5/26/17.
 */
public class TweetMessageSubscriber {

    private Connection connection;
    private ConnectionFactory factory;
    private Session session;
    private Topic topic;
    private MessageConsumer genericConsumer;
    private ObservableList<String> tweets;
    private String messageSelector;
    private String hashtag;

    public TweetMessageSubscriber() {
        hashtag = "";
        tweets = FXCollections.observableArrayList();
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
        closeConnection();
        if (!"".equalsIgnoreCase(hashtag) || hashtag != null) {
            // Closing of a connection is done to kill off expensive resources from existing within the Client aswell as in ActiveMQ
            // Every time a new dynamic selector was created, old ones stayed alive. The only way to prevent this from happening is by
            // killing the connection. Also, if a hashtag is empty, there is no need to create a connection.
            createConnection();
        }
    }

    public ObservableList<String> getTweets() {
        return tweets;
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void createConnection() {
        try {
            factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            connection = factory.createConnection();
            connection.setClientID("KwetterMonitor");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            topic = session.createTopic("tweetTopic");
            // Replaces the 4 consumers that existed before with a dynamic message selector.
            createListenerForNewHashtag();
            connection.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void createListenerForNewHashtag() {
        this.messageSelector = "typeMessage LIKE '" + hashtag + "'";
        System.out.println(messageSelector);
        try {
            this.genericConsumer = session.createConsumer(topic, messageSelector);
            this.genericConsumer.setMessageListener(message -> {
                try {
                    System.out.println(message.getStringProperty("typeMessage"));
                    if (message.getStringProperty("typeMessage").equalsIgnoreCase(hashtag)) {
                        Platform.runLater(() -> {
                            tweets.add(messageParser(message));
                        });
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private String messageParser(Message message) {
        String messageBody = null;
        String result = "";
        try {
            messageBody = ((TextMessage) message).getText();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        try (JsonReader reader = Json.createReader(new StringReader(messageBody))) {
            JsonObject jsonMessage = reader.readObject();
            result = (jsonMessage.getString("tweeter") + " posted:'"
                    + jsonMessage.getString("message") + "' on "
                    + jsonMessage.getString("createdOn"));
        }
        return result;
    }
}
