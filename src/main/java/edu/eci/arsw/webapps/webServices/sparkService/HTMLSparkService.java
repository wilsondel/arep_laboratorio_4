package edu.eci.arsw.webapps.webServices.sparkService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HTMLSparkService implements  RestServiceSpark{
    @Override
    public String getResponse(String request, String response) {
        String header = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html \r\n" +
                "\r\n";

        return header + "<html><body><h1>Hola, mundo!</h1></body></html>";
    }
}
