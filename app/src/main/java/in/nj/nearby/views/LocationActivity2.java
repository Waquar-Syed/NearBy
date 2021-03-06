package in.nj.nearby.views;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import in.nj.nearby.BuildConfig;
import in.nj.nearby.R;
import in.nj.nearby.common.AppConstants;
import in.nj.nearby.common.adapters.POSItemsListAdapter;
import in.nj.nearby.common.adapters.SearchListAdapter;
import in.nj.nearby.common.interfaces.listeners.PosButtons;
import in.nj.nearby.common.interfaces.listeners.ServiceResponseListener;
import in.nj.nearby.model.Coordinates;
import in.nj.nearby.model.POS;
import in.nj.nearby.model.POSDetails;
import in.nj.nearby.model.POSModel;
import in.nj.nearby.services.ServerCommunication;

/**
 * Created by hp on 30-11-2017.
 */

public class LocationActivity2 extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = LocationActivity2.class.getSimpleName();

    private GoogleMap gMap;
    boolean zoomToLocationOnlyOnce;

    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;

    private SupportMapFragment map;


    // UI Widgets.
   /* private Button mStartUpdatesButton;
    private Button mStopUpdatesButton;
   */
    //private TextView mLastUpdateTimeTextView;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private TextView mSearchTextView;
    private EditText mSearchEditText;

    // Labels.
    private String mLatitudeLabel;
    private String mLongitudeLabel;
    //private String mLastUpdateTimeLabel;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    private String mLastUpdateTime;

    SearchListAdapter mAdapter;
    final Set<String> checkedItems = new HashSet<>();

    //this is used to store permanent markers on the map
    Map<String, Marker> markerHashMap = new HashMap<>();

    //this is used to stop temporary markers on the map
    List<Marker> markersOnMap = new ArrayList<>();

    List<POSModel> posModelList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mLatitudeTextView = findViewById(R.id.latitude_text);
        mLongitudeTextView = findViewById(R.id.longitude_text);
        mSearchTextView = findViewById(R.id.search);

        // Set labels.
        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        startLocationUpdates();

        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        map.getMapAsync(this);

        mSearchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });
        posDialog = (View) findViewById(R.id.posDialog);

        Intent intent = getIntent();
        String categories = intent.getStringExtra("CATEGORIES");
        if(categories!=null && categories.length()>0){
            mSearchTextView.setText(categories);
            String[] temp = categories.split(",");
            for(String s : temp){
                if(s.length()>0){
                    checkedItems.add(s);
                }
            }
            getMerchantList(checkedItems.toArray(new String[checkedItems.size()]));
        }else
            getMerchantList(AppConstants.getCatagories().toArray(new String[AppConstants.getCatagories().size()]));
        //createMarkersDialog(null,0);
    }

    private void createDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(LocationActivity2.this);
        dialog.setContentView(R.layout.dialog_search_items);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setTitle("Type preferences");

        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerview);
        mSearchEditText = (EditText) dialog.findViewById(R.id.search_edittext);

        List<String> items = AppConstants.getCatagories();

        mAdapter = new SearchListAdapter(items, checkedItems);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mAdapter != null) {
                    mAdapter.getFilter().filter(charSequence.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("Checked-ITEMS:", checkedItems.toString());
                mSearchTextView.setText("");
                for (String s : checkedItems) {
                    if (mSearchTextView.getText().length() < 1) {
                        mSearchTextView.setText(s);
                    } else
                        mSearchTextView.setText(mSearchTextView.getText() + "," + s);
                }
                //TODO:

                clearMarkerOnMap();

                getMerchantList(mSearchTextView.getText().toString().split(","));

                //CAll api , get marker and then call set markers on the map,


                //setMarkersOnMap(latLng.getResults().get(0).getFormatted_address(), latLng.getResults().get(0).getGeometry().getLocation());
            }
        });

        dialog.show();
    }

    private void clearMarkerOnMap() {
        for (Marker marker : markersOnMap) {
            marker.remove();
        }
        markersOnMap.clear();
    }

    private void getMerchantList(String[] type) {
        Map<String, String> parameter = new HashMap<>();
        int i = 1;
        for (String types : type) {
            parameter.put("type" + (i++), types);
        }
        ServerCommunication.getmInstance().addJSONGetRequest(AppConstants.GET_POS_URL, parameter, null, new ServiceResponseListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("No Response");
            }

            @Override
            public void onResponse(Object response) {
                Gson gson = new Gson();
                POS pos = gson.fromJson(response.toString(), POS.class);
                getCoordinatesForPosList(pos);
            }
        });
    }

    private void getCoordinatesForPosList(POS pos) {
        posModelList = new ArrayList<>();
        for (POSDetails posDetails : pos.getPos()) {
            POSModel posModel = new POSModel(posDetails.getM_NAME(), posDetails.getOUTER_POSTAL_CODE() + posDetails.getINNER_POSTAL_CODE(),
                    AppConstants.getOffer(), posDetails.getMID());

            posModel.setOnCallClickListener(new PosButtons.OnCallClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CALLCLICK", "hanji");
                }
            });
            posModel.setOnNavigationClickListener(new PosButtons.OnNavigationClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("NavigationCLICK", "hanji");
                }
            });
            posModel.setOnShareClickListener(new PosButtons.OnShareClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("shareCLICK", "hanji");
                }
            });
            posModel.setPhoneNumber(posDetails.getPHONE_NO());
            posModelList.add(posModel);
            getLatLngForAddress(posDetails.getOUTER_POSTAL_CODE() + posDetails.getINNER_POSTAL_CODE(), posDetails.getM_NAME(), posDetails.getMCC_DSC(), posModel);
        }
    }

    private void getLatLngForAddress(String address, final String mName, final String desc, final POSModel posModel) {
        final Map<String, String> parameter = new HashMap<>();
        posModel.setCategory(desc);
        parameter.put("address", address);
        parameter.put("key", "AIzaSyCWcjr8FbpiGxerhFjKRWJH0j6LEl9A4OU");


        ServerCommunication.getmInstance().addJSONGetRequest(AppConstants.GET_ADDRESS_URL, parameter, getHeaders(), new ServiceResponseListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {
                Gson gson = new Gson();

                Coordinates latLng = gson.fromJson(response.toString(), Coordinates.class);
                setMarkersOnMap(latLng.getResults().get(0).getFormatted_address(), latLng.getResults().get(0).getGeometry().getLocation(), mName, desc, posModel);
            }
        });
    }

    private Map<String, String> getHeaders() {
        return null;
    }

    private void setMarkersOnMap(String formatted_address, in.nj.nearby.model.Location location, String mName, String desc, POSModel posModel) {
        //remove temporary markers from the map

        //clear list of temp markers
        //markersOnMap.clear();

        //create and add temp markers on the list
        /*markersOnMap.add(gMap.addMarker(new MarkerOptions().position(new LatLng(18.5540681,73.8798155)).
                icon(BitmapDescriptorFactory.fromResource(R.drawable.shop))));*/
        posModel.setAddress(formatted_address);

       /* TextView text = new TextView(this);
        text.setText(posModel.getTitle()+"\n"+AppConstants.getOffer());
        IconGenerator generator = new IconGenerator(this);
        generator.setBackground(getDrawable(getDrawableForDescription(desc)));
        generator.setContentView(text);
        Bitmap icon = generator.makeIcon();*/

        final MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(location.getLat(), location.getLng()))
                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromDesc(desc, posModel)))
                .anchor(0.5f, 1);
        markerOptions.snippet(formatted_address);
        markerOptions.title(mName);
        final Marker marker = gMap.addMarker(markerOptions);
        posModel.setMarker(marker);


        markersOnMap.add(marker);

        posModel.setDistance(new DecimalFormat("0.000").format(getDistance(marker.getPosition())) + "");


        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker m : markersOnMap) {
            builder.include(m.getPosition());
        }
        for (String key : markerHashMap.keySet()) {
            Marker m = markerHashMap.get(key);
            builder.include(m.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        gMap.animateCamera(cu);
        posModel.setOnNavigationClickListener(new PosButtons.OnNavigationClickListener() {
            @Override
            public void onClick(View view) {
                setNavigationButton(marker);
            }
        });
        posModel.setOnCallClickListener(new PosButtons.OnCallClickListener() {
            @Override
            public void onClick(View view) {
                setCallButton(marker);
            }
        });
        posModel.setOnShareClickListener(new PosButtons.OnShareClickListener() {
            @Override
            public void onClick(View v) {
                setShareButton(marker);
            }
        });
    }


    private int getDrawableForDescription(String desc) {
        switch (desc) {
            case "Cloths":
                return R.drawable.ic_action_cloth;
            case "Dining":
                return R.drawable.ic_action_dining;
            case "Travel":
                return R.drawable.ic_action_travel;
            case "Electronics":
                return R.drawable.ic_action_electronics;
        }
        return -1;
    }


    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(final Marker marker) {
            if (!marker.equals(markerHashMap.get(AppConstants.CURRENT_LOCATION_KEY))) {
                /*Dialog dialog = new Dialog(LocationActivity.this);
                dialog.setContentView(R.layout.info_dialog);
                dialog.setCancelable(true);
                TextView title = dialog.findViewById(R.id.title);
                title.setText(marker.getTitle());
                TextView address = dialog.findViewById(R.id.address);
                address.setText(marker.getSnippet().toString());
                TextView offers = dialog.findViewById(R.id.offer);
                offers.setText(AppConstants.getOffer());
                Button navigate = dialog.findViewById(R.id.btn_navigate);
                navigate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setNavigationButton(marker);
                    }
                });

                Button share = dialog.findViewById(R.id.btn_share);
                setShareButtonClickListener(share, marker);
                dialog.show();*/
                int index = 0;
                for (POSModel posModel : posModelList) {
                    if (posModel.getMarker().equals(marker)) {
                        createMarkersDialog(posModelList, index);
                        return true;
                    }
                    index++;
                }
                return true;
            }
            return false;
        }
    };

    private boolean isCurrentLocation(Marker marker) {
        return ((marker.getPosition().longitude == mCurrentLocation.getLongitude()) && (marker.getPosition().latitude == mCurrentLocation.getLatitude()));
    }

    private void setShareButtonClickListener(Button share, final Marker marker) {
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShareButton(marker);
            }
        });
    }

    private void setShareButton(Marker marker) {
        String message = "http://maps.google.com/maps?q=" + marker.getPosition().latitude + "," + marker.getPosition().longitude;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "See you at: " + message);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share Through"));
    }

    private void setNavigationButton(Marker marker) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + "51.461561,-0.210521" + "&daddr=" + marker.getPosition().latitude + "," + marker.getPosition().longitude));
        startActivity(intent);
    }

    private void setCallButton(Marker marker) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            String number = "";
            try {
                for (POSModel posModel : posModelList) {
                    if (posModel.getMarker().equals(marker)) {
                        number = posModel.getPhoneNumber();
                    }
                }
            }catch (NullPointerException e){
                Log.d("exception",e.toString());
            }
            number = number.length()>0?number:"9555308310";
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(LocationActivity2.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
            updateUI();
        }
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                //mCurrentLocation = locationResult.getLastLocation();

                mCurrentLocation = new Location("My Location");
                mCurrentLocation.setLatitude(51.461561);
                mCurrentLocation.setLongitude(-0.210521);
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };
    }

    /**
     * Uses a {@link LocationSettingsRequest.Builder} to build
     * a {@link LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        updateUI();
                        break;
                }
                break;
        }
    }

    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * updates have already been requested.
     */
    public void startUpdatesButtonHandler(View view) {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            setButtonsEnabledState();
            startLocationUpdates();
        }
    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates.
     */
    public void stopUpdatesButtonHandler(View view) {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        stopLocationUpdates();
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        if (ActivityCompat.checkSelfPermission(LocationActivity2.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationActivity2.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(LocationActivity2.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(LocationActivity2.this, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                        updateUI();
                    }
                });
    }

    /**
     * Updates all UI fields.
     */
    private void updateUI() {
        setButtonsEnabledState();
        updateLocationUI();
    }

    /**
     * Disables both buttons when functionality is disabled due to insuffucient location settings.
     * Otherwise ensures that only one button is enabled at any time. The Start Updates button is
     * enabled if the user is not requesting location updates. The Stop Updates button is enabled
     * if the user is requesting location updates.
     */
    private void setButtonsEnabledState() {
        /*if (mRequestingLocationUpdates) {
            mStartUpdatesButton.setEnabled(false);
            mStopUpdatesButton.setEnabled(true);
        } else {
            mStartUpdatesButton.setEnabled(true);
            mStopUpdatesButton.setEnabled(false);
        }*/
    }

    /**
     * Sets the value of the UI fields for the location latitude, longitude and last update time.
     */
    private void updateLocationUI() {
        if (gMap == null) return;
        if (mCurrentLocation != null) {
            mLatitudeTextView.setText(String.format(Locale.ENGLISH, "%s: %f", mLatitudeLabel,
                    mCurrentLocation.getLatitude()));
            mLongitudeTextView.setText(String.format(Locale.ENGLISH, "%s: %f", mLongitudeLabel,
                    mCurrentLocation.getLongitude()));
          /*  mLastUpdateTimeTextView.setText(String.format(Locale.ENGLISH, "%s: %s",
                    mLastUpdateTimeLabel, mLastUpdateTime));
        */
            /*LatLng currentLocation = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
            if (gMap != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    callForLocationPermission();
                    return;
                }
                gMap.setMyLocationEnabled(true);
                if(markerHashMap.containsKey(AppConstants.CURRENT_LOCATION_KEY))
                    markerHashMap.get(AppConstants.CURRENT_LOCATION_KEY).remove();
                Marker marker = gMap.addMarker(new MarkerOptions().position(currentLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_here)));
                marker.setDraggable(true);
                markerHashMap.put(AppConstants.CURRENT_LOCATION_KEY,marker);
                if(!zoomToLocationOnlyOnce) {
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
                    zoomToLocationOnlyOnce = true;
                }
            }*/
        }
    }

    private void callForLocationPermission() {
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                        setButtonsEnabledState();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
            requestPermissions();
        }

        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Remove location updates to save battery.
        stopLocationUpdates();
    }

    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        /*Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();*/
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CALL_PHONE);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(LocationActivity2.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(LocationActivity2.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
                    startLocationUpdates();
                }
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setTrafficEnabled(true);
        gMap.setOnMarkerClickListener(markerClickListener);
        mCurrentLocation = new Location("My Location");
        mCurrentLocation.setLatitude(51.461561);
        mCurrentLocation.setLongitude(-0.210521);
        if (mCurrentLocation != null) {
//            LatLng currentLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

            LatLng currentLocation = new LatLng(51.461561, -0.210521);
            //gMap.clear();
            Marker marker = gMap.addMarker(new MarkerOptions().position(currentLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_here)));
            marker.setDraggable(true);
            gMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    if (marker.equals(markerHashMap.get(AppConstants.CURRENT_LOCATION_KEY))) {
                        LatLng latLng = marker.getPosition();
                        Log.d("DRAGGED Location", latLng.toString());
                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                        //call APis
                        Location location = new Location("markerLocation");
                        location.setLatitude(latLng.latitude);
                        location.setLongitude(latLng.longitude);
                        double distance = AppConstants.getFixedLocation().distanceTo(location);
                        Log.d("Distance", distance + "");
                        getMerchantList(mSearchTextView.getText().toString().split(","));
                    }
                }
            });
            markerHashMap.put(AppConstants.CURRENT_LOCATION_KEY, marker);
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
        }
    }

    private View posDialog;
    RecyclerView posItemRecycleView;
    int lastPosition;

    void createMarkersDialog(List<POSModel> posModels, int focusPosition) {
       /* final Dialog dialog = new Dialog(LocationActivity.this);
        dialog.setContentView(R.layout.layout_dialog_poslist);
       */// dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        posDialog.setVisibility(View.GONE);
        posItemRecycleView = findViewById(R.id.recyclerview);

        List<POSModel> items = posModels;

        POSItemsListAdapter adapter = new POSItemsListAdapter(items, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        posItemRecycleView.setLayoutManager(layoutManager);
        posItemRecycleView.setItemAnimator(new DefaultItemAnimator());
        posItemRecycleView.setAdapter(adapter);

        posItemRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                //gMap.animateCamera(CameraUpdateFactory.newLatLng(posModelList.get(centerPos).getMarker().getPosition()));
            }

            boolean scrolling, stoped;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        System.out.println("The RecyclerView is not scrolling");
                        if (scrolling) {
                            int center = posItemRecycleView.getWidth() / 2;
                            View centerView = posItemRecycleView.findChildViewUnder(center, posItemRecycleView.getTop());
                            int centerPos = posItemRecycleView.getChildAdapterPosition(centerView);
                            Log.d("CenterPOS", centerPos + "");
                            if (lastPosition != centerPos && centerPos >= 0) {
                                POSModel posModel = posModelList.get(centerPos);
                                LatLng latLng = posModel.getMarker().getPosition();
                                gMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                lastPosition = centerPos;
                                posItemRecycleView.getLayoutManager().smoothScrollToPosition(recyclerView, new RecyclerView.State(), centerPos);
                            }
                            scrolling = false;
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        System.out.println("Scrolling now");
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        System.out.println("Scroll Settling");

                        scrolling = true;
                        break;
                }
            }
        });

        posDialog.setVisibility(View.VISIBLE);
        focusPosItem(focusPosition);
        //dialog.show();
    }

    void focusPosItem(int position) {
        if (posItemRecycleView != null) {
            posItemRecycleView.getLayoutManager().scrollToPosition(position);
        }
    }

    @Override
    public void onBackPressed() {
        if (posDialog != null && posDialog.getVisibility() == View.VISIBLE) {
            posDialog.setVisibility(View.GONE);
        } else
            super.onBackPressed();
    }

    Bitmap getBitmapFromDesc(String desc, POSModel posModel) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_marker_icon, null);
        TextView tv = (TextView) view.findViewById(R.id.marker_text);
        ImageView iv = (ImageView) view.findViewById(R.id.marker_icon);
        Random random = new Random();
        int x = random.nextInt(4) + 1;
        tv.setText(posModel.getTitle() + "\n" + x + "X");
        iv.setBackground(getDrawable(getDrawableForDescription(desc)));

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        // view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bitmap);

        //Render this view (and all of its children) to the given Canvas
        view.draw(c);

        return bitmap;
    }

    private double getDistance(LatLng latLng) {
        Location location = new Location("markerLocation");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        double distance = AppConstants.getFixedLocation().distanceTo(location);
        Log.d("Distance", distance + "");
        return distance;
    }
}