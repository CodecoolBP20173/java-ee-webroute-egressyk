package com.simplewebserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class SimpleServer {
    private static RequestResponder requestResponder = RequestResponder.getInstance();
    private static RequestRouter requestRouter = RequestRouter.getInstance(requestResponder);

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        requestRouter.setupRoutes(server, new MyHandler());
        server.setExecutor(null);
        server.start();
    }

    private static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = requestRouter.getResponseForPath(httpExchange.getRequestURI().toString(), httpExchange);
            int responseCode = (!response.equals("404")) ? 200 : 404;
            httpExchange.sendResponseHeaders(responseCode, response.length());
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }
    }
}
