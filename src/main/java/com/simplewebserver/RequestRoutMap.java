package com.simplewebserver;

import java.util.List;

public class RequestRoutMap {
    private static RequestRoutMap ourInstance = new RequestRoutMap();

    static RequestRoutMap getInstance() {
        return ourInstance;
    }

    private RequestRoutMap() {
    }

    @WebRoute(urlPath = "/")
    public String indexHandler(){
        return "You're on the index page";
    }

    @WebRoute(urlPath = "/", requestMethod = "post")
    public String indexPostHandler(){
        return "You're on the index page, with POST";
    }

    @WebRoute(urlPath = "/test")
    public String testHandler() {
        return "You're on the test page";
    }

    @WebRoute(urlPath = "/test/<username>")
    public String testUsernameHandler(List<String> variables) {
        String username = variables.get(0);
        return String.format("Hi %s, you're on the test page", username);
    }

    @WebRoute(urlPath = "/othertest")
    public String otherTestHandler() {
        return "You're on the OTHER test page";
    }
}
