package pl.horban.camel.twitterexample;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;


public class TwitterSearchProcessor implements Processor {

    static final String FOLLOWERS_COUNT_HEADER = "followersCountHeader";
    static final String LANG_HEADER = "langHeader";

    @Override
    public void process(Exchange exchange) throws Exception {
        twitter4j.Status twitterStatus = exchange.getIn().getBody(twitter4j.Status.class);

        TwitterObject tweet = new TwitterObject();
        tweet.setDate(twitterStatus.getCreatedAt());
        tweet.setUserName(twitterStatus.getUser().getName());
        tweet.setLang(twitterStatus.getUser().getLang());
        tweet.setFollowersCount(twitterStatus.getUser().getFollowersCount());
        tweet.setTweet(twitterStatus.getText());

        exchange.getIn().setBody(tweet.toString());
        exchange.getIn().setHeader(Exchange.FILE_NAME, twitterStatus.getId() + ".txt");
        exchange.getIn().setHeader(FOLLOWERS_COUNT_HEADER, tweet.getFollowersCount());
        exchange.getIn().setHeader(LANG_HEADER, tweet.getLang());
    }
}
