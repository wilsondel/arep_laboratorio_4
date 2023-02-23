package edu.eci.arsw;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a mini web server, you can access it in localhost:35000
 */
public class HttpServer {

    private Map<String, Method> servicesMethod = new HashMap<>();

    private static HttpServer _instance = new HttpServer(); //se carga la clase

    private HttpServer() { }

    public static String staticFilesLocation = "/public";

    public static HttpServer getInstance() { // implementacion singleton
        return _instance;
    }


    /**
     * @param args
     * @throws IOException
     */
public  void run(String[] args) throws IOException {

    String className = args[0];
    // paso 1: cargar clase con forname
    Class<?> c;
    try {
        c = Class.forName(className);
    } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
    Method[] methods = c.getMethods();


    // paso 2: extraer metodos con anotacion @RequestMapping
    for (Method m : methods) {
        if (m.isAnnotationPresent(RequestMapping.class)) {
            System.out.println("si entraaa");
            // paso 3: extraer el valor del path  ej => @RequestMapping("/pi") toma es /pi
            RequestMapping annotation = m.getAnnotation(RequestMapping.class);
            // paso 4: extraer una instancia del método
            String pathValue = annotation.value();
            // paso 5: poner en la tabla el método con llave path
            System.out.println("path Value: " + pathValue);
            servicesMethod.put(pathValue,m);

        }
    }



    ServerSocket serverSocket = null;
    try {
        serverSocket = new ServerSocket(35000);
    } catch (IOException e) {
        System.err.println("Could not listen on port: 35000.");
        System.exit(1);
    }
    boolean running = true;
    while (running) {

        Socket clientSocket = null;
        try {
            System.out.println("Listo para recibir ...");
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));
        String inputLine, outputLine;

        boolean flagForPath = true;
        String path = "piService";
        while ((inputLine = in.readLine()) != null) {
            if (flagForPath) {
                flagForPath = false;
                path = inputLine.split(" ")[1];
            }
            System.out.println("Received: " + inputLine);
            if (!in.ready()) {
                break;
            }
        }


        outputLine = "";
        if (servicesMethod.containsKey(path)) {
            Method method = servicesMethod.get(path);
            try {
                outputLine = selectHeader("html") + "<h1>" + method.invoke(null) + "</h1>";
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        out.println(outputLine);

        out.close();
        in.close();
        clientSocket.close();
    }

    serverSocket.close();
}


    public static String readFile(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources"+staticFilesLocation+"/" + fileName));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } finally {
            reader.close();
        }
        return content.toString();
    }

    public String selectHeader(String type) {
        switch (type) {
            case "html":
                return "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html \r\n" +
                        "\r\n";
            case "css":
                return "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/css \r\n" +
                        "\r\n";
            case "js":
                return "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: application/javascript \r\n" +
                        "\r\n";
            case "json":
                return "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: application/json \r\n" +
                        "\r\n";

            default:
                return "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html \r\n" +
                        "\r\n";
        }
    }


    public static String jsonSimple(String jsonResponse) {
        return  "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json \r\n" +
                "\r\n"
                +
                jsonResponse;
    }



}