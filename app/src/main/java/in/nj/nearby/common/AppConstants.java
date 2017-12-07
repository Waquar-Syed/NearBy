package in.nj.nearby.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hp on 30-11-2017.
 */

public class AppConstants {

    public static final String CURRENT_LOCATION_KEY = "CURRENT_LOCATION_KEY";

    private static final String[] OFFERS = {"Spend more then 100$ and get 10% off",
            "Spend more then 200$ get 20% off",
            "Buy one get one"};

    public static List<String> getCatagories(){
        List<String> items = new ArrayList<>();
        items.add("Dining");
        items.add("Cloths");
        items.add("Travel");
        items.add("Electronics");
        return items;
    }

    public static String getOffer(){
        Random random = new Random();
        int val = random.nextInt(OFFERS.length);
        return OFFERS[val];
    }

    public static final String GET_ADDRESS_URL = "https://maps.googleapis.com/maps/api/geocode/json?";
    public static final String GET_POS_URL = "http://192.168.43.228:8080/api/getPOS?";
}
