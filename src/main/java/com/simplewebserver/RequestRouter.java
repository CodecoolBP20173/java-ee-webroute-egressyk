package com.simplewebserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class RequestRouter {
    private static RequestRouter ourInstance;
    private RequestRoutMap requestRoutMap;

    static RequestRouter getInstance(RequestRoutMap responder) {
        return (ourInstance == null) ? new RequestRouter(responder) : ourInstance;
    }

    private RequestRouter(RequestRoutMap responder) {
        this.requestRoutMap = responder;
        ourInstance = this;
    }

    void setupRoutes(HttpServer server, HttpHandler handler) {
        Method[] responderMethods = requestRoutMap.getClass().getMethods();
        for (Method method : responderMethods) {
            WebRoute annotation = method.getAnnotation(WebRoute.class);
            if (annotation != null) {
                server.createContext(annotation.urlPath(), handler);
            }
        }
    }

    String getResponseForPath(String path, HttpExchange httpExchange) {
        String requestMethod = httpExchange.getRequestMethod().toLowerCase();
        Method[] responderMethods = requestRoutMap.getClass().getMethods();
        for (Method method : responderMethods) {
            WebRoute annotation = method.getAnnotation(WebRoute.class);
            if (annotation != null
                    && path.matches(getRegexFromPath(annotation.urlPath()))
                    && annotation.requestMethod().equals(requestMethod)) {
                try {
                    if (method.getParameterCount() == 0) {
                        return (String)method.invoke(requestRoutMap);
                    } else {
                        return (String)method.invoke(requestRoutMap, getVariablesFromPath(annotation.urlPath(), path));
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return "404";
    }

    private String getRegexFromPath(String path) {
        String[] pathPieces = path.split("/");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=1; i<pathPieces.length; i++) {
            stringBuilder.append("/");
            stringBuilder.append((pathPieces[i].startsWith("<"))? "[0-9a-zA-Z_]*" : pathPieces[i] );
        }
        return stringBuilder.toString();
    }

    private List<String> getVariablesFromPath(String annotationPath, String path) {
        List<String> variables = new ArrayList<>();
        String[] annotationPathPieces = annotationPath.split("/");
        String[] pathPieces = path.split("/");
        for (int i=1; i<annotationPathPieces.length; i++) {
            if (annotationPathPieces[i].startsWith("<")) {
                variables.add(pathPieces[i]);
            }
        }
        return variables;
    }


}
