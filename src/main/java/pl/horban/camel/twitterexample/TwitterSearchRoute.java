package pl.horban.camel.twitterexample;

import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.FileEndpoint;
import org.apache.camel.component.twitter.search.TwitterSearchComponent;
import org.apache.camel.component.twitter.search.TwitterSearchEndpoint;

import static org.apache.camel.builder.PredicateBuilder.or;
import static pl.horban.camel.twitterexample.TwitterConfiguration.accessToken;
import static pl.horban.camel.twitterexample.TwitterConfiguration.accessTokenSecret;
import static pl.horban.camel.twitterexample.TwitterConfiguration.consumerKey;
import static pl.horban.camel.twitterexample.TwitterConfiguration.consumerSecret;
import static pl.horban.camel.twitterexample.TwitterSearchProcessor.FOLLOWERS_COUNT_HEADER;
import static pl.horban.camel.twitterexample.TwitterSearchProcessor.LANG_HEADER;

public class TwitterSearchRoute extends RouteBuilder {

    private String searchTerm = "Poland OR Polska";
    private String outputFileEnglish = "C:/out/en";
    private String outpuFilePolish = "C:/out/pl";
    private String outputFileOthers = "C:/out/others";

    @Override
    public void configure() throws Exception {
        // setup Twitter component
        TwitterSearchComponent tc = getContext().getComponent("twitter-search", TwitterSearchComponent.class);
        tc.setAccessToken(accessToken);
        tc.setAccessTokenSecret(accessTokenSecret);
        tc.setConsumerKey(consumerKey);
        tc.setConsumerSecret(consumerSecret);

        TwitterSearchEndpoint twitterEndpoint = (TwitterSearchEndpoint) tc.createEndpoint("twitter-search://" + searchTerm);
        twitterEndpoint.setDelay(4000);
        
        // poll twitter search for new tweets
        from(twitterEndpoint)
            .to("log:tweet")
            .process(new TwitterSearchProcessor())
            .filter(onlyWellKnownUserPredicate(200))
            .choice() //content base router
                .when(langPredicate("PL"))
                    .to(getFileEndpoint(outpuFilePolish))
                .when(or(langPredicate("EN"), langPredicate("EN-GB")))
                    .to(getFileEndpoint(outputFileEnglish))
                .otherwise()
                    .to(getFileEndpoint(outputFileOthers))
            .end();
    }

    private FileEndpoint getFileEndpoint(String outDirectory) {
        return getContext().getEndpoint("file://" + outDirectory, FileEndpoint.class);
    }

    private Predicate langPredicate(String lang) {
        return ex -> ex.getIn().getHeader(LANG_HEADER, String.class).equalsIgnoreCase(lang);
    }

    private Predicate onlyWellKnownUserPredicate(int followersCount) {
        return ex -> ex.getIn().getHeader(FOLLOWERS_COUNT_HEADER, Integer.class) >= followersCount;
    }
}
