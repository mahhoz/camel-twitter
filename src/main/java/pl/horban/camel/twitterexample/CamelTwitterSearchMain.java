package pl.horban.camel.twitterexample;

import org.apache.camel.main.Main;


public final class CamelTwitterSearchMain {

    private CamelTwitterSearchMain() {   //by design
    }

    public static void main(String[] args) throws Exception {

        // create a new Camel Main so we can easily start Camel
        Main main = new Main();

        // add our routes to Camel
        main.addRouteBuilder(new TwitterSearchRoute());
        
        main.run();
    }
}
