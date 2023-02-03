package edu.eci.arsw.webapps;

import edu.eci.arsw.RestService;

public class HelloService implements RestService {


    @Override
    public String getHeader() {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html" +
                "\r\n";
    }

    @Override
    public String getResponse() {
        return "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\"\n" +
                "          content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                "    <title>Movie´s information</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<main>\n" +
                    "Hello World" +
                "</main>" +
                "</body>\n" +
                "</html>";
    }
}
