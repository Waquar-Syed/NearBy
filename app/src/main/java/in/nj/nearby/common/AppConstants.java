package in.nj.nearby.common;

import android.util.Log;
import android.view.View;

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
    public static final String GET_POS_URL = "http://192.168.43.228:8080/api/getPOS?";

    public static android.location.Location getFixedLocation(){
        android.location.Location location = new android.location.Location("FixedLocation");
        location.setLongitude(-0.210521);
        location.setLatitude(51.461561);
        return location;
    }
}
