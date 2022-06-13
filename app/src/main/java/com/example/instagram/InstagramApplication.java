package com.example.instagram;

import android.app.Application;

import com.parse.Parse;

public class InstagramApplication extends Application {

    private final String APPLICATION_ID = "xYaAGd4rAIkwmAtntYZyEyED7ocSvREy7ahThrJK";
    private final String CLIENT_KEY = "3nynKeLhSpUwHT9mm1cgR7u6bEBJTu1EwhQXSnJv";
    private final String SERVER = "https://parseapi.back4app.com";

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(APPLICATION_ID)
                .clientKey(CLIENT_KEY)
                .server(SERVER)
                .build());
    }
}
