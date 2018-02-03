package pl.horban.camel.twitterexample;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;


public class TwitterSearchProcessor implements Processor {

    private String NEW_LINE = System.lineSeparator();

    static final String FOLLOWERS_COUNT_HEADER = "followersCountHeader";
    static final String LANG_HEADER = "langHeader";

    @Override
    public void process(Exchange exchange) throws Exception {
        twitter4j.Status twitterStatus = exchange.getIn().getBody(twitter4j.Status.class);

        String tweet = "TIMESTAMP: " + twitterStatus.getCreatedAt() + NEW_LINE +
                "USER_NAME: " + twitterStatus.getUser().getName() + NEW_LINE +
                "LANG: " + twitterStatus.getUser().getLang() + NEW_LINE +
                "FOLLOWERS_COUNT: " + twitterStatus.getUser().getFollowersCount() + NEW_LINE +
                "TWEET: " + twitterStatus.getText() + NEW_LINE;

        exchange.getIn().setHeader(Exchange.FILE_NAME, twitterStatus.getId() + ".txt");
        exchange.getIn().setHeader(FOLLOWERS_COUNT_HEADER, twitterStatus.getUser().getFollowersCount());
        exchange.getIn().setHeader(LANG_HEADER, twitterStatus.getUser().getLang());
        
        exchange.getIn().setBody(tweet);
    }
}
