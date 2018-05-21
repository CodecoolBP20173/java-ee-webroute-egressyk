package com.simplewebserver;

public class RequestResponder {
    private static RequestResponder ourInstance = new RequestResponder();

    static RequestResponder getInstance() {
        return ourInstance;
    }

    private RequestResponder() {
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

    @WebRoute(urlPath = "/othertest")
    public String otherTestHandler() {
        return "You're on the OTHER test page";
    }
}
