package pl.horban.camel.twitterexample;


import java.util.Date;

public class TwitterObject {

    private String NEW_LINE = System.lineSeparator();

    private Date date;
    private String userName;
    private String lang;
    private int followersCount;
    private String tweet;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    @Override
    public String toString() {
        return "DATE: " + date + NEW_LINE +
                "USERNAME: " + userName + NEW_LINE +
                "LANG: " + lang + NEW_LINE +
                "FOLLOWERS_COUNT: " + followersCount + NEW_LINE +
                "TWEET: " + tweet;
    }
}
