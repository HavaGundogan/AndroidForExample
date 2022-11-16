package com.gundogan.parselearning;

import android.app.Application;
import com.parse.Parse;
public class ParseLearn extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("ws7DOcbrr1Y0L4RwcqpYOaPQaGWlgB5VBhiS4qVr")
                .clientKey("esozcQsVWJnF29d3GghKhOI18GaqWY4yqg8mJrEy")
                .server("https://parse-dashboard.back4app.com/parse")
                .build()

        );


    }
}