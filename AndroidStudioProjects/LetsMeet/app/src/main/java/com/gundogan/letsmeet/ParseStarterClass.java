package com.gundogan.letsmeet;

import android.app.Application;

import com.parse.Parse;

public class ParseStarterClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("rR0yCZoIk5uM7qhKKq8VNgz8C5FeCbRXSYOaUPI7")
                .clientKey("wLXCPZNYrfmZ21tzObme7IiHsPryqivYVdeKhbRs")
                .server("https://parseapi.back4app.com/")
                .build()
        );
   }
}