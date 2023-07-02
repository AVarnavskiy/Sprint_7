package utils;

import java.util.Random;

public class GenerateData {

    public static String generateCourierLogin() {
        Random rand = new Random();
        return "CourierTest" + rand.nextInt(100_000_000);
    }

}
