package pl.horban.camel.twitterexample;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import twitter4j.Status;

public class TwitterSearchProcessor implements Processor {

    private String NEW_LINE = System.lineSeparator();

    @Override
    public void process(Exchange exchange) throws Exception {
        Status twitterStatus = exchange.getIn().getBody(Status.class);

        StringBuilder tweet = new StringBuilder();
        tweet.append("TIMESTAMP: ").append(twitterStatus.getCreatedAt()).append(NEW_LINE);
        tweet.append("USER_NAME: ").append(twitterStatus.getUser().getName()).append(NEW_LINE);
        tweet.append("LANG: ").append(twitterStatus.getUser().getLang()).append(NEW_LINE);
        tweet.append("FOLLOWERS_COUNT: ").append(twitterStatus.getUser().getFollowersCount()).append(NEW_LINE);
        tweet.append("TWEET: ").append(twitterStatus.getText()).append(NEW_LINE);

        exchange.getIn().setBody(tweet);
        exchange.getIn().setHeader(Exchange.FILE_NAME, twitterStatus.getId() + ".txt");
    }
}
