package edu.eci.arsw.webapps;

import edu.eci.arsw.RestService;
import java.io.FileReader;

import java.io.BufferedReader;
import java.io.IOException;

public class HTMLService implements RestService {


    @Override
    public String getHeader() {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html \r\n" +
                "\r\n";
    }

    @Override
    public String getResponse() {
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
        return sb.toString();
    }

}
