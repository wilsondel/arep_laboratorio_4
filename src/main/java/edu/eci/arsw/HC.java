package edu.eci.arsw;


@Component
public class HC {


    @RequestMapping("/pi")
    public static String piService() {
        return String.valueOf(Math.PI);
    }

}
