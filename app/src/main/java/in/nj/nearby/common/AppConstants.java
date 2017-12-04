package in.nj.nearby.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 30-11-2017.
 */

public class AppConstants {

    public static final String CURRENT_LOCATION_KEY = "CURRENT_LOCATION_KEY";

    public static List<String> getCatagories(){
        List<String> items = new ArrayList<>();
        items.add("Dining");
        items.add("Cloths");
        items.add("Travel");
        items.add("Electronics");
        return items;
    }

    public static final String GET_ADDRESS_URL = "https://maps.googleapis.com/maps/api/geocode/json?";
    public static final String GET_POS_URL = "http://192.168.43.228:8080/api/getPOS?";
}
