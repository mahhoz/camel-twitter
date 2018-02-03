package pl.horban.camel.twitterexample;

import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.FileEndpoint;
import org.apache.camel.component.twitter.search.TwitterSearchComponent;
import org.apache.camel.component.twitter.search.TwitterSearchEndpoint;
import org.apache.camel.model.ModelCamelContext;

import static pl.horban.camel.twitterexample.TwitterConfiguration.accessToken;
import static pl.horban.camel.twitterexample.TwitterConfiguration.accessTokenSecret;
import static pl.horban.camel.twitterexample.TwitterConfiguration.consumerKey;
import static pl.horban.camel.twitterexample.TwitterConfiguration.consumerSecret;
import static pl.horban.camel.twitterexample.TwitterSearchProcessor.FOLLOWERS_COUNT_HEADER;
import static pl.horban.camel.twitterexample.TwitterSearchProcessor.LANG_HEADER;

public class TwitterSearchRoute extends RouteBuilder {

    private String searchTerm = "Poland OR Polska";
    private String outEn = "C:/out/en";
    private String outPl = "C:/out/pl";
    private String outRest = "C:/out/rest";

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
                    .to(getFileEndpoint(getContext(), outPl))
                .when(langPredicate("EN"))
                    .to(getFileEndpoint(getContext(), outEn))
                .otherwise()
                    .to(getFileEndpoint(getContext(), outRest))
            .end();
    }

    private FileEndpoint getFileEndpoint(ModelCamelContext context, String outDirectory) {
        return getContext().getEndpoint("file://" + outDirectory, FileEndpoint.class);
    }

    private Predicate langPredicate(String lang) {
        return ex -> ex.getIn().getHeader(LANG_HEADER, String.class).equalsIgnoreCase(lang);
    }

    private Predicate onlyWellKnownUserPredicate(int followersCount) {
        return ex -> ex.getIn().getHeader(FOLLOWERS_COUNT_HEADER, Integer.class) >= followersCount;
    }
}
