package edu.eci.arsw.webapps;

import edu.eci.arsw.HttpServer;
import edu.eci.arsw.RestService;

import java.io.IOException;

public class FirstApp {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.getInstance();
        server.addService("/cine",
                new RestService() {

                    @Override
                    public String getHeader() {
                        return "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: application/json \r\n" +
                                "\r\n";
                    }

                    @Override
                    public String getResponse() {
                        return "{\"title\": \"Tenet\" }";
                    }
                }
        );

        server.addService("/hello",new HTMLService());
        server.addService("/style",new CSSService());
        server.addService("/javascript",new JSService());
        server.addService("/image",new ImageService());
        server.run(args);
    }

}
