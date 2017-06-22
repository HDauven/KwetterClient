package com.kwetter;

import com.kwetter.jms.MessageBuilder;
import com.kwetter.jms.TweetMessageSubscriber;
import com.kwetter.soap.GoPlusClient;
import com.kwetter.soap.SimpleTweet;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@ClientEndpoint
public class Controller {

    private Session session;
    private ObservableList<String> socketTweets;
    private TweetMessageSubscriber subscriber;

    @FXML
    public void initialize() {
        socketTweets = FXCollections.observableArrayList();
        timelineGo.setItems(socketTweets);
        subscriber = new TweetMessageSubscriber();
        timelineMonitor.setItems(subscriber.getTweets());
        connectToWebSocket();
    }

    // Kwetter Go
    @FXML
    private TextField usernameGo;
    @FXML
    private TextField messageGo;
    @FXML
    private ListView<String> timelineGo;

    @FXML
    private void handleSendTweet(ActionEvent event) {
        String message = messageGo.getText();
        String username = usernameGo.getText();
        MessageBuilder messageBuilder = new MessageBuilder(message, username);
    }

    @FXML
    private void handleSendSocketTweet(ActionEvent event) {
        String message = messageGo.getText();
        String username = usernameGo.getText();
        JsonObject object = Json.createObjectBuilder()
                .add("action", "add")
                .add("message", message)
                .add("username", username)
                .build();
        System.out.println(object.toString());
        try {
            session.getBasicRemote().sendText(object.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Kwetter Go Plus
    @FXML
    private TextField usernameGoPlus;
    @FXML
    private TextField messageGoPlus;
    @FXML
    private ListView<String> timelineGoPlus;

    @FXML
    private void handleSendSoapTweet(ActionEvent event) {
        String message = messageGoPlus.getText();
        String username = usernameGoPlus.getText();
        GoPlusClient client = new GoPlusClient();
        client.createTweet(message, username);
    }

    @FXML
    private void handleTimelineSoap(ActionEvent event) {
        String username = usernameGoPlus.getText();
        GoPlusClient client = new GoPlusClient();
        List<SimpleTweet> tweets = client.getTimeline(username);
        List<String> newTweets = new ArrayList<>();
        for (SimpleTweet tweet: tweets) {
            Calendar calendar = tweet.getCreatedAt().toGregorianCalendar();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            formatter.setTimeZone(calendar.getTimeZone());
            String postedOn = formatter.format(calendar.getTime());
            newTweets.add(tweet.getUsername() + " posted:'" + tweet.getMessage() + "' on " + postedOn);
        }
        ObservableList<String> items = FXCollections.observableArrayList(newTweets);
        timelineGoPlus.setItems(items);
    }

    // Kwetter Monitor
    @FXML
    private TextField hashtagMonitor;
    @FXML
    private ListView<String> timelineMonitor;

    @FXML
    private void handleSearchMonitor(ActionEvent event) {
        subscriber.setHashtag(hashtagMonitor.getText());
        timelineMonitor.refresh();
    }

    // Client Socket
    private void connectToWebSocket() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            URI uri = URI.create("ws://localhost:15509/kwetter/websocket/tweet");
            container.connectToServer(this, uri);
        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @OnOpen
    public void open(Session session) {
        this.session = session;
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
    }

    @OnMessage
    public void processMessage(String message) {
        System.out.println("Received message in client: " + message);
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            Platform.runLater(() -> {
                socketTweets.add(0, jsonMessage.getString("tweeter") + " posted:'"
                        + jsonMessage.getString("message") + "' on "
                        + jsonMessage.getString("createdOn"));
            });
        }
        timelineGo.refresh();
    }

    @OnClose
    public void close() {
        connectToWebSocket();
    }
}
