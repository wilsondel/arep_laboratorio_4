package edu.eci.arsw;

@Component
public class HC2 {


    @RequestMapping("/hello")
    public static String hello() {
        return "Hello World!";
    }

}
