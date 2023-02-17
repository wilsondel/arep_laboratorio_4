package edu.eci.arsw;

import edu.eci.arsw.repository.Cache;
import edu.eci.arsw.service.HttpConnection;
import edu.eci.arsw.webapps.webServices.normalService.RestService;
import edu.eci.arsw.webapps.webServices.sparkService.RestServiceSpark;

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

    private Map<String, RestService> services = new HashMap<>();

    private static HttpServer _instance = new HttpServer(); //se carga la clase

    private HttpServer() { }

    private static Map<String, RestServiceSpark> servicesSpark = new HashMap<>();

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
            String path = annotation.value();
            try {
                System.out.println("Path: " + m.invoke(null));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            // paso 4: extraer una instancia del método

            // paso 5: poner en la tabla el método con llave path


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
        String path = "/?name=test";
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

        URL basePathForm = new URL("http://www.localhost:35000" + path); //http://www.localhost:35000/?t=indiana
        String baseQueryForm = basePathForm.getQuery();

        String apiResponse = "";
        if (baseQueryForm != null) {
            if (Cache.hasQuery(baseQueryForm)) {
                apiResponse = Cache.getQuery(baseQueryForm);
            } else {
                apiResponse = HttpConnection.getAPIInfo(baseQueryForm);
                Cache.saveQuery(baseQueryForm, apiResponse);
            }

        }

        outputLine = "";
        if (basePathForm.getPath().contains("/apps/")) { // localhost:35000/apps/hello
            outputLine = executeService(basePathForm.getPath().substring(5)); // /apps/hello toma solo hello
        } else if (basePathForm.getPath().contains("/spark/")) {
            outputLine = executeServiceSpark(basePathForm.getPath().substring(6), basePathForm.getQuery());
        } else if (basePathForm.getPath().contains("/public/")) {
            outputLine = executeServiceSparkPublic(basePathForm.getPath().substring(7));

        } else {
            outputLine = htmlWithForms(apiResponse);
        }


        out.println(outputLine);

        out.close();
        in.close();
        clientSocket.close();
    }

    serverSocket.close();
}


    public String executeService(String serviceName) {
        RestService rs = services.get(serviceName);
        String header = rs.getHeader();
        String body = rs.getResponse(); //body viene leido de disco
        return header + body;
    }

    public String executeServiceSpark(String serviceName, String query) {
        System.out.println("this is the query: " + query);
        RestServiceSpark rs = servicesSpark.get(serviceName);

        String response = rs.getResponse(query,"");
        return response;
    }

    public String executeServiceSparkPublic(String fileName) {
        String response = "";
        String header = "";

        System.out.println("NOMBRE DEL ARCHIVO: " + fileName);
        try {
            if (fileName.contains("html")) {
                header = selectHeader("html");
            } else if (fileName.contains("css")) {
                header = selectHeader("css");

            } else if (fileName.contains("js")) {
                header = selectHeader("js");
            } else {
                return jsonSimple("{\"message\":\"File not found\"}");
            }
            response = readFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return header + response;
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

    public void addService(String key, RestService service) {
    services.put(key, service);
    }

    public static void get(String key, RestServiceSpark service) {
        servicesSpark.put(key,service);
    }

    public static void get(String key, String type,RestServiceSpark service) {
        if (type.equals("application/json")) {
            servicesSpark.put(key,service);
        }
    }


    public static void post(String key, RestServiceSpark service) {
        servicesSpark.put(key,service);
    }

    public static void staticFiles(String location) {
        staticFilesLocation = location;
    }



    public static String jsonSimple(String jsonResponse) {
        return  "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json \r\n" +
                "\r\n"
                +
                jsonResponse;
    }


    /**
     * @param apiResponse JSON string response of the API
     * @return a String representing the html response
     */
    public static String htmlWithForms(String apiResponse) {

        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html" +
                "\r\n"
                +
                "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\"\n" +
                "          content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                "    <title>Movie´s information</title>\n" +
                "</head>\n" +
                "<body style=\"margin:0;padding: 0;\">\n" +
                "\n" +
                "<main class=\"main\" style=\"background: rgb(179,153,212);background: linear-gradient(90deg, rgba(179,153,212,1) 0%, rgba(88,166,187,1) 47%, rgba(71,214,244,1) 100%);\">\n" +
                "\n" +
                "    <form action=\"localhost:35000\" onsubmit=\"return false\" class=\"form\" style=\"  display: flex;justify-content: center;align-items: center;background-color:rgba(255, 255, 255, 0.6);border-radius: 15px;width: 35%;height: 20%;margin-top: 10px; \">\n" +
                "        <div style=\"  \" >\n" +
                "            <p>Movie Name</p>\n" +
                "            <div>\n" +
                "            <label for=\"t\"></label>\n" +
                "                <input type=\"text\" name=\"t\" id=\"t\">\n" +
                "            </div>\n" +
                "            <input type=\"button\" value=\"Get information\" onclick=\"loadGetMsg()\">\n" +
                "        </div>\n" +
                "    </form>\n" +
                "    <div id=\"getrespmsg\" style=\" \" >\n" + apiResponse +
                "\n" +
                "    </div>\n" +
                "</main>" +
                "\n" +
                "<script>\n" +
                "    function loadGetMsg() {\n" +
                "        let nameVar = document.getElementById(\"t\").value;\n" +
                "        const xhttp = new XMLHttpRequest();\n"  +
                "        xhttp.onload = function() {\n" +
                "            document.getElementById(\"getrespmsg\").innerHTML= \n"  +
                "                this.responseText;\n" +
                "        }\n" +
                "        xhttp.open(\"GET\", \"/?t=\"+nameVar);\n" +
                "        xhttp.send();\n" +
                "    }\n" +
                "</script>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

    }


}