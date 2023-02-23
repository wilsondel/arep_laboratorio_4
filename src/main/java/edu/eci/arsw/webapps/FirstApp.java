package edu.eci.arsw.webapps;

import edu.eci.arsw.HttpServer;
import static edu.eci.arsw.HttpServer.*;
import edu.eci.arsw.webapps.webServices.normalService.CSSService;
import edu.eci.arsw.webapps.webServices.normalService.HTMLService;
import edu.eci.arsw.webapps.webServices.normalService.ImageService;
import edu.eci.arsw.webapps.webServices.normalService.JSService;
import edu.eci.arsw.webapps.webServices.sparkService.HTMLSparkPostService;
import edu.eci.arsw.webapps.webServices.sparkService.HTMLSparkService;
import edu.eci.arsw.webapps.webServices.sparkService.HTMLSparkTestService;
import edu.eci.arsw.webapps.webServices.sparkService.JSONSparkTestService;

import java.io.IOException;

public class FirstApp {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.getInstance();
        server.run(args);

    }

}
