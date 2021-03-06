package in.nj.nearby.model;

import com.google.android.gms.maps.model.Marker;

import in.nj.nearby.common.interfaces.listeners.PosButtons;

/**
 * Created by nitesh on 07-12-2017.
 */

public class POSModel {
    private String title,address,offers,category,distance="~",phoneNumber="9555308310";
    private double lat,lon;
    private PosButtons.OnCallClickListener onCallClickListener;
    private PosButtons.OnNavigationClickListener onNavigationClickListener;
    private PosButtons.OnShareClickListener onShareClickListener;
    private Marker marker;
    int rewardMultiplier;

    public POSModel(String title, String address, String offers, String category) {
        this.title = title;
        this.address = address;
        this.offers = offers;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public PosButtons.OnCallClickListener getOnCallClickListener() {
        return onCallClickListener;
    }

    public void setOnCallClickListener(PosButtons.OnCallClickListener onCallClickListener) {
        this.onCallClickListener = onCallClickListener;
    }

    public PosButtons.OnNavigationClickListener getOnNavigationClickListener() {
        return onNavigationClickListener;
    }

    public void setOnNavigationClickListener(PosButtons.OnNavigationClickListener onNavigationClickListener) {
        this.onNavigationClickListener = onNavigationClickListener;
    }

    public PosButtons.OnShareClickListener getOnShareClickListener() {
        return onShareClickListener;
    }

    public void setOnShareClickListener(PosButtons.OnShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getRewardMultiplier() {
        return rewardMultiplier;
    }

    public void setRewardMultiplier(int rewardMultiplier) {
        this.rewardMultiplier = rewardMultiplier;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
