package edu.eci.arsw.webapps.webServices;

import java.io.*;

public class ImageService implements RestService {

    @Override
    public String getHeader() {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: image/jpeg\r\n" +
                "\r\n";
    }

    @Override
    public String getResponse() {
        String file;
        file = readImage("src/main/resources/django.jpg");
        return file;
    }


    public String readImage(String imageName) {

        File file = new File(imageName);
        byte[] fileData = new byte[(int) file.length()];
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            dis.readFully(fileData);
            dis.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return new String(fileData);
    }

}
