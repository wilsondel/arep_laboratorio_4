package edu.eci.arsw;


@Component
public class HC {


    @RequestMapping("/pi")
    public static String pi() {
        return String.valueOf(Math.PI);
    }

}
