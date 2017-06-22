package com.kwetter.soap;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by hein on 5/26/17.
 */
public class GoPlusClient {

  GoPlusService goPlusService = new GoPlusService();
  GoPlusServiceImpl service;

  public GoPlusClient() {
    service = goPlusService.getGoPlusServiceImplPort();
  }

  public void createTweet(String message, String username) {
    SimpleTweet tweet = new SimpleTweet();
    tweet.setMessage(message);
    tweet.setUsername(username);
    GregorianCalendar now = new GregorianCalendar();
    now.setTime(new Date());
    try {
      tweet.setCreatedAt(DatatypeFactory.newInstance().newXMLGregorianCalendar(now));
    } catch (DatatypeConfigurationException e) {
      e.printStackTrace();
    }
    service.createTweet(tweet);
  }

  public List<SimpleTweet> getTimeline(String username) {
    return service.getTimeline(username);
  }
}
