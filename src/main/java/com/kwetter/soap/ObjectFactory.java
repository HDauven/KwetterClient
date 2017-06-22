
package com.kwetter.soap;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.kwetter.soap package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetTimeline_QNAME = new QName("http://soap.kwetter.com/", "getTimeline");
    private final static QName _GetTimelineResponse_QNAME = new QName("http://soap.kwetter.com/", "getTimelineResponse");
    private final static QName _CreateTweetResponse_QNAME = new QName("http://soap.kwetter.com/", "createTweetResponse");
    private final static QName _CreateTweet_QNAME = new QName("http://soap.kwetter.com/", "createTweet");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.kwetter.soap
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetTimeline }
     * 
     */
    public GetTimeline createGetTimeline() {
        return new GetTimeline();
    }

    /**
     * Create an instance of {@link GetTimelineResponse }
     * 
     */
    public GetTimelineResponse createGetTimelineResponse() {
        return new GetTimelineResponse();
    }

    /**
     * Create an instance of {@link CreateTweetResponse }
     * 
     */
    public CreateTweetResponse createCreateTweetResponse() {
        return new CreateTweetResponse();
    }

    /**
     * Create an instance of {@link CreateTweet }
     * 
     */
    public CreateTweet createCreateTweet() {
        return new CreateTweet();
    }

    /**
     * Create an instance of {@link SimpleTweet }
     * 
     */
    public SimpleTweet createSimpleTweet() {
        return new SimpleTweet();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTimeline }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.kwetter.com/", name = "getTimeline")
    public JAXBElement<GetTimeline> createGetTimeline(GetTimeline value) {
        return new JAXBElement<GetTimeline>(_GetTimeline_QNAME, GetTimeline.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTimelineResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.kwetter.com/", name = "getTimelineResponse")
    public JAXBElement<GetTimelineResponse> createGetTimelineResponse(GetTimelineResponse value) {
        return new JAXBElement<GetTimelineResponse>(_GetTimelineResponse_QNAME, GetTimelineResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateTweetResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.kwetter.com/", name = "createTweetResponse")
    public JAXBElement<CreateTweetResponse> createCreateTweetResponse(CreateTweetResponse value) {
        return new JAXBElement<CreateTweetResponse>(_CreateTweetResponse_QNAME, CreateTweetResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateTweet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.kwetter.com/", name = "createTweet")
    public JAXBElement<CreateTweet> createCreateTweet(CreateTweet value) {
        return new JAXBElement<CreateTweet>(_CreateTweet_QNAME, CreateTweet.class, null, value);
    }

}
