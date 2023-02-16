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

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/index.html"))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sb.toString());
        return header + "<html><body><h1>Hola, mundo!</h1></body></html>";
//        return header + sb.toString();
    }
}
