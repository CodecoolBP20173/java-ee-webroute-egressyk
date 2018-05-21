package com.simplewebserver;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class RequestRouter {
    private static RequestRouter ourInstance;
    private RequestResponder requestResponder;

    static RequestRouter getInstance(RequestResponder responder) {
        return (ourInstance == null) ? new RequestRouter(responder) : ourInstance;
    }

    private RequestRouter(RequestResponder responder) {
        this.requestResponder = responder;
        ourInstance = this;
    }

    void setupRoutes(HttpServer server, HttpHandler handler) {
        Method[] responderMethods = requestResponder.getClass().getMethods();
        for (Method method : responderMethods) {
            WebRoute annotation = method.getAnnotation(WebRoute.class);
            if (annotation != null) {
                server.createContext(annotation.urlPath(), handler);
            }
        }
    }

    String getResponseForPath(String path) {
        Method[] responderMethods = requestResponder.getClass().getMethods();
        for (Method method : responderMethods) {
            WebRoute annotation = method.getAnnotation(WebRoute.class);
            if (annotation != null && annotation.urlPath().equals(path)) {
                try {
                    return (String)method.invoke(requestResponder);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return "404: Page not found";
    }
}
