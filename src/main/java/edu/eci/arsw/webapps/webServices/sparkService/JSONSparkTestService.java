package edu.eci.arsw.webapps.webServices.sparkService;

public class JSONSparkTestService implements  RestServiceSpark {

    @Override
    public String getResponse(String request, String response) {
        String header = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json \r\n" +
                "\r\n";
        return header + "{\"message\":\"This is a message example by application/json option\"}";
    }

}
