package edu.eci.arsw.webapps;

import edu.eci.arsw.RestService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageService implements RestService {

    @Override
    public String getHeader() {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: image/png \r\n" +
                "\r\n";
    }

    @Override
    public String getResponse() {
        BufferedImage br = null;
        ByteArrayOutputStream bytes = null;
        try {
            br = ImageIO.read(new File("src/main/resources/django.png"));
            bytes = new ByteArrayOutputStream();
            ImageIO.write(br, "png", bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.valueOf(bytes);
    }
}
