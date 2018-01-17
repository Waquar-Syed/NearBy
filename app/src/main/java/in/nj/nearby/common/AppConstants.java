package in.nj.nearby.common;

import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import in.nj.nearby.common.interfaces.listeners.PosButtons;
import in.nj.nearby.model.Location;
import in.nj.nearby.model.POSModel;

/**
 * Created by hp on 30-11-2017.
 */

public class AppConstants {

    public static final String CURRENT_LOCATION_KEY = "CURRENT_LOCATION_KEY";

    private static final String[] OFFERS = {"Spend more then 100$ and get 10% off",
            "Spend more then 200$ get 20% off",
            "Buy one get one"};

    public static final String response = "{\"message\":\"success\",\"status\":200,\"pos\":[{\"MID\":\"416696\",\"TID\":\"16885527\",\"MCC_CDE\":\"2741\",\"M_NAME\":\"S Dining & Sweets\",\"MCC_DSC\":\"Dining\",\"OUTER_POSTAL_CODE\":\"SW15\",\"INNER_POSTAL_CODE\":\"2NQ\",\"PREMISE_NUM\":\"\",\"PREMISE_NME\":\"COTTESBROOKE PARK\",\"STREET_NUM\":\"148\",\"STREET_NME\":\"PUTNEY BRIDGE RD\",\"DISTRICT_NME\":\"\",\"CITY_NME\":\"LONDON\"},{\"MID\":\"416697\",\"TID\":\"16885532\",\"MCC_CDE\":\"2741\",\"M_NAME\":\"D Garments\",\"MCC_DSC\":\"Cloths\",\"OUTER_POSTAL_CODE\":\"SW15\",\"INNER_POSTAL_CODE\":\"2NQ\",\"PREMISE_NUM\":\"\",\"PREMISE_NME\":\"COTTESBROOKE PARK\",\"STREET_NUM\":\"148\",\"STREET_NME\":\"PUTNEY BRIDGE RD\",\"DISTRICT_NME\":\"\",\"CITY_NME\":\"LONDON\"},{\"MID\":\"416694\",\"TID\":\"16885537\",\"MCC_CDE\":\"2741\",\"M_NAME\":\"Stuart Travels\",\"MCC_DSC\":\"Travel\",\"OUTER_POSTAL_CODE\":\"SW15\",\"INNER_POSTAL_CODE\":\"2NL\",\"PREMISE_NUM\":\"\",\"PREMISE_NME\":\"COTTESBROOKE PARK\",\"STREET_NUM\":\"148\",\"STREET_NME\":\"PUTNEY BRIDGE RD\",\"DISTRICT_NME\":\"\",\"CITY_NME\":\"LONDON\"},{\"MID\":\"416695\",\"TID\":\"16885542\",\"MCC_CDE\":\"2741\",\"M_NAME\":\"Mike Electronics\",\"MCC_DSC\":\"Electronics\",\"OUTER_POSTAL_CODE\":\"SW15\",\"INNER_POSTAL_CODE\":\"2NJ\",\"PREMISE_NUM\":\"\",\"PREMISE_NME\":\"COTTESBROOKE PARK\",\"STREET_NUM\":\"148\",\"STREET_NME\":\"PUTNEY BRIDGE RD\",\"DISTRICT_NME\":\"\",\"CITY_NME\":\"LONDON\"},{\"MID\":\"416698\",\"TID\":\"16885547\",\"MCC_CDE\":\"2741\",\"M_NAME\":\"S Dining & Snaks\",\"MCC_DSC\":\"Dining\",\"OUTER_POSTAL_CODE\":\"SW15\",\"INNER_POSTAL_CODE\":\"2PE\",\"PREMISE_NUM\":\"\",\"PREMISE_NME\":\"COTTESBROOKE PARK\",\"STREET_NUM\":\"148\",\"STREET_NME\":\"PUTNEY BRIDGE RD\",\"DISTRICT_NME\":\"\",\"CITY_NME\":\"LONDON\"},{\"MID\":\"416699\",\"TID\":\"16885567\",\"MCC_CDE\":\"2741\",\"M_NAME\":\"David Garments\",\"MCC_DSC\":\"Cloths\",\"OUTER_POSTAL_CODE\":\"SW15\",\"INNER_POSTAL_CODE\":\"2NL\",\"PREMISE_NUM\":\"\",\"PREMISE_NME\":\"COTTESBROOKE PARK\",\"STREET_NUM\":\"148\",\"STREET_NME\":\"PUTNEY BRIDGE RD\",\"DISTRICT_NME\":\"\",\"CITY_NME\":\"LONDON\"},{\"MID\":\"416704\",\"TID\":\"16885573\",\"MCC_CDE\":\"2741\",\"M_NAME\":\"Peter Travels\",\"MCC_DSC\":\"Travel\",\"OUTER_POSTAL_CODE\":\"SW15\",\"INNER_POSTAL_CODE\":\"2NN\",\"PREMISE_NUM\":\"\",\"PREMISE_NME\":\"COTTESBROOKE PARK\",\"STREET_NUM\":\"148\",\"STREET_NME\":\"PUTNEY BRIDGE RD\",\"DISTRICT_NME\":\"\",\"CITY_NME\":\"LONDON\"},{\"MID\":\"416703\",\"TID\":\"16885575\",\"MCC_CDE\":\"2338\",\"M_NAME\":\"Indian Dining\",\"MCC_DSC\":\"Dining\",\"OUTER_POSTAL_CODE\":\"SW15\",\"INNER_POSTAL_CODE\":\"2NG\",\"PREMISE_NUM\":\"\",\"PREMISE_NME\":\"PALM HILLS\",\"STREET_NUM\":\"42\",\"STREET_NME\":\"ORCHARD LN\",\"DISTRICT_NME\":\"\",\"CITY_NME\":\"SOUTHAMPTON\"},{\"MID\":\"416703\",\"TID\":\"16885575\",\"MCC_CDE\":\"2338\",\"M_NAME\":\"Inetrnational Dining\",\"MCC_DSC\":\"Dining\",\"OUTER_POSTAL_CODE\":\"SW15\",\"INNER_POSTAL_CODE\":\"2NQ\",\"PREMISE_NUM\":\"\",\"PREMISE_NME\":\"PALM HILLS\",\"STREET_NUM\":\"42\",\"STREET_NME\":\"ORCHARD LN\",\"DISTRICT_NME\":\"\",\"CITY_NME\":\"SOUTHAMPTON\"},{\"MID\":\"416703\",\"TID\":\"16885575\",\"MCC_CDE\":\"2338\",\"M_NAME\":\"India house\",\"MCC_DSC\":\"Dining\",\"OUTER_POSTAL_CODE\":\"SW15\",\"INNER_POSTAL_CODE\":\"2PX\",\"PREMISE_NUM\":\"\",\"PREMISE_NME\":\"PALM HILLS\",\"STREET_NUM\":\"42\",\"STREET_NME\":\"ORCHARD LN\",\"DISTRICT_NME\":\"\",\"CITY_NME\":\"SOUTHAMPTON\"},{\"MID\":\"416703\",\"TID\":\"16885575\",\"MCC_CDE\":\"2338\",\"M_NAME\":\"Asian Rest\",\"MCC_DSC\":\"Dining\",\"OUTER_POSTAL_CODE\":\"SW15\",\"INNER_POSTAL_CODE\":\"2LP\",\"PREMISE_NUM\":\"\",\"PREMISE_NME\":\"PALM HILLS\",\"STREET_NUM\":\"42\",\"STREET_NME\":\"ORCHARD LN\",\"DISTRICT_NME\":\"\",\"CITY_NME\":\"SOUTHAMPTON\"},{\"MID\":\"416708\",\"TID\":\"16885589\",\"MCC_CDE\":\"2338\",\"M_NAME\":\"Indian Garments\",\"MCC_DSC\":\"Cloths\",\"OUTER_POSTAL_CODE\":\"SW15\",\"INNER_POSTAL_CODE\":\"2NW\",\"PREMISE_NUM\":\"\",\"PREMISE_NME\":\"COTTESBROOKE PARK\",\"STREET_NUM\":\"148\",\"STREET_NME\":\"PUTNEY BRIDGE RD\",\"DISTRICT_NME\":\"\",\"CITY_NME\":\"LONDON\"},{\"MID\":\"416709\",\"TID\":\"16885591\",\"MCC_CDE\":\"2339\",\"M_NAME\":\"new India Garments\",\"MCC_DSC\":\"Cloths\",\"OUTER_POSTAL_CODE\":\"SW15\",\"INNER_POSTAL_CODE\":\"2PU\",\"PREMISE_NUM\":\"\",\"PREMISE_NME\":\"COTTESBROOKE PARK\",\"STREET_NUM\":\"148\",\"STREET_NME\":\"PUTNEY BRIDGE RD\",\"DISTRICT_NME\":\"\",\"CITY_NME\":\"LONDON\"},{\"MID\":\"416709\",\"TID\":\"16885591\",\"MCC_CDE\":\"2339\",\"M_NAME\":\"new England Garments\",\"MCC_DSC\":\"Cloths\",\"OUTER_POSTAL_CODE\":\"SW15\",\"INNER_POSTAL_CODE\":\"2LR\",\"PREMISE_NUM\":\"\",\"PREMISE_NME\":\"COTTESBROOKE PARK\",\"STREET_NUM\":\"148\",\"STREET_NME\":\"PUTNEY BRIDGE RD\",\"DI\n";

    public static List<String> getCatagories(){
        List<String> items = new ArrayList<>();
        items.add("Dining");
        items.add("Cloths");
        items.add("Travel");
        items.add("Electronics");
        return items;
    }

    public static List<POSModel> getpPosModels(){
        List<POSModel> posModels = new ArrayList<>();
        POSModel posModel1 = new POSModel("title","address","offer","catagory");
        POSModel posModel2 = new POSModel("title","address","offer","catagory");
        /*POSModel posModel3 = new POSModel("title","address","offer","catagory");
        POSModel posModel4 = new POSModel("title","address","offer","catagory");
        posModels.add(posModel1);
        posModels.add(posModel1);
        posModels.add(posModel1);*/
        posModel1.setOnCallClickListener(new PosButtons.OnCallClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CALLCLICK","hanji");
            }
        });
        posModel1.setOnNavigationClickListener(new PosButtons.OnNavigationClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("NavigationCLICK","hanji");
            }
        });
        posModel1.setOnShareClickListener(new PosButtons.OnShareClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("shareCLICK","hanji");
            }
        });

        posModel2.setOnCallClickListener(new PosButtons.OnCallClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CALLCLICK","hanji");
            }
        });
        posModel2.setOnNavigationClickListener(new PosButtons.OnNavigationClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("NavigationCLICK","hanji");
            }
        });
        posModel2.setOnShareClickListener(new PosButtons.OnShareClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("shareCLICK","hanji");
            }
        });

        posModels.add(posModel1);
        posModels.add(posModel2);
        return posModels;
    }

    public static String getOffer(){
        Random random = new Random();
        int val = random.nextInt(OFFERS.length);
        return OFFERS[val];
    }

    public static final String GET_ADDRESS_URL = "https://maps.googleapis.com/maps/api/geocode/json?";
    public static final String GET_POS_URL = "http://192.168.43.97:8080/api/getPOS?";

    public static android.location.Location getFixedLocation(){
        android.location.Location location = new android.location.Location("FixedLocation");
        location.setLongitude(-0.210521);
        location.setLatitude(51.461561);
        return location;
    }

    public static double calculateDistance( android.location.Location location1, android.location.Location location2) {
        double distance = location1.distanceTo(location2);
        Log.d("Distance", distance + "");
        return distance;
    }
}
