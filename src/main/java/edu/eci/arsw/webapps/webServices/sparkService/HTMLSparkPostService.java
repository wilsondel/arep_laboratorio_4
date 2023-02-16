package edu.eci.arsw.webapps.webServices.sparkService;

import edu.eci.arsw.HttpServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HTMLSparkPostService implements  RestServiceSpark{

    HttpServer server = HttpServer.getInstance();

    @Override
    public String getResponse(String request, String response) {
        String header = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html \r\n" +
                "\r\n";

        if (request != null) {
            System.out.println("LOS DATOS ENVIADOS FUERON: " + request);
            header = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/json \r\n" +
                    "\r\n";
            return header + "{\"message\":\"Data was sent\", \"name\": \""+ request.substring(5) +"\"}";
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources"+server.staticFilesLocation+"/post.html"))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sb.toString());
        return header + sb.toString();
    }

}
