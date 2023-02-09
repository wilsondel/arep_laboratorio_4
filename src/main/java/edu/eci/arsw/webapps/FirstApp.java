package edu.eci.arsw.webapps;

import edu.eci.arsw.HttpServer;
import edu.eci.arsw.webapps.webServices.CSSService;
import edu.eci.arsw.webapps.webServices.HTMLService;
import edu.eci.arsw.webapps.webServices.ImageService;
import edu.eci.arsw.webapps.webServices.JSService;

import java.io.IOException;

public class FirstApp {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.getInstance();
        server.addService("/hello",new HTMLService());
        server.addService("/style",new CSSService());
        server.addService("/javascript",new JSService());
        server.addService("/image",new ImageService());
        server.run(args);
    }

}
