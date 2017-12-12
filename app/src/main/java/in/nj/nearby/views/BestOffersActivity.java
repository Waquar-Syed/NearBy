package in.nj.nearby.views;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import in.nj.nearby.R;
import in.nj.nearby.common.AppConstants;
import in.nj.nearby.common.PosOffersComparator;
import in.nj.nearby.common.adapters.POSItemsListAdapter2;
import in.nj.nearby.common.adapters.SearchListAdapter;
import in.nj.nearby.common.interfaces.listeners.ServiceResponseListener;
import in.nj.nearby.model.Coordinates;
import in.nj.nearby.model.POS;
import in.nj.nearby.model.POSDetails;
import in.nj.nearby.model.POSModel;
import in.nj.nearby.services.ServerCommunication;

/**
 * Created by nitesh on 09-12-2017.
 */

public class BestOffersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fab;

    List<POSModel> posModels = new ArrayList<>();
    POSItemsListAdapter2 adapter2;

    String searchText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offerslist);
        intializeViews();
        updateView();
    }

    private void intializeViews() {
        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
    }


    private void updateView() {

        getMerchantList(AppConstants.getCatagories().toArray(new String[AppConstants.getCatagories().size()]));
        //getPosData();

        //populatePOSList(posModels);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenuPopup(view,view.getLeft()-(view.getWidth()*2),
                        (view.getHeight()*2));
            }
        });
    }

    private List<POSModel> getPosData() {
        POSModel posModel1 = new POSModel("T1","Address1", AppConstants.getOffer(),"Dining");
        posModel1.setRewardMultiplier(2);

        POSModel posModel2 = new POSModel("T2","Address2", AppConstants.getOffer(),"Dining");
        posModel2.setRewardMultiplier(2);
        POSModel posModel3 = new POSModel("T3","Address3", AppConstants.getOffer(),"Travel");
        posModel3.setRewardMultiplier(3);
        POSModel posModel4 = new POSModel("T4","Address4", AppConstants.getOffer(),"Electronics");
        posModel4.setRewardMultiplier(4);

        POSModel posModel5 = new POSModel("T2","Address2", AppConstants.getOffer(),"Dining");
        posModel2.setRewardMultiplier(2);
        POSModel posModel6 = new POSModel("T3","Address3", AppConstants.getOffer(),"Travel");
        posModel3.setRewardMultiplier(3);
        POSModel posModel7 = new POSModel("T4","Address4", AppConstants.getOffer(),"Electronics");
        posModel4.setRewardMultiplier(4);

        posModels.add(posModel1);
        posModels.add(posModel2);
        posModels.add(posModel3);
        posModels.add(posModel4);
        posModels.add(posModel5);
        posModels.add(posModel6);
        posModels.add(posModel7);
        Collections.sort(posModels,new PosOffersComparator());
        return posModels;
    }

    void populatePOSList(final List<POSModel> posModels){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter2 = new POSItemsListAdapter2(posModels,this);
        adapter2.setOnClickListener(new OffersListActivity.OnClickListener() {
            @Override
            public void onClick(View v, int position) {
               // Toast.makeText(OffersListActivity.this,"Pos: "+position+"",Toast.LENGTH_SHORT).show();
                //startNavigation(posModels.get(position));
                showDialog(posModels.get(position));
            }
        });
        recyclerView.setAdapter(adapter2);
    }

    SearchListAdapter mAdapter;
    final Set<String> checkedItems = new HashSet<>();

    private void createDialog() {
// custom dialog
        final Dialog dialog = new Dialog(BestOffersActivity.this);
        dialog.setContentView(R.layout.dialog_search_items);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setTitle("Type preferences");

        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerview);
        EditText   mSearchEditText = (EditText) dialog.findViewById(R.id.search_edittext);

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
                searchText = "";
                if(checkedItems.size()!=0) {
                    List<POSModel> temp = new ArrayList<>();
                    for (POSModel posModel : posModels) {
                        for (String s : checkedItems) {
                            if (s.equals(posModel.getCategory())) {
                                temp.add(posModel);
                                break;
                            }
                        }
                    }
                    for (String s : checkedItems) {
                        if (checkedItems.size() < 1) {
                            searchText = s;
                        } else
                            searchText += "," + s;
                    }

                    populatePOSList(temp);
                }else populatePOSList(posModels);
                //TODO:

                //CAll api , get marker and then call set markers on the map,


                //setMarkersOnMap(latLng.getResults().get(0).getFormatted_address(), latLng.getResults().get(0).getGeometry().getLocation());
            }
        });

        dialog.show();
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
                Log.d(BestOffersActivity.this.getLocalClassName() + ":RESPONSE:", response.toString());
                Gson gson = new Gson();
                POS pos = gson.fromJson(response.toString(), POS.class);
                posModels = new ArrayList<>();
                for (POSDetails posDetails : pos.getPos()) {
                    POSModel posModel = new POSModel(posDetails.getM_NAME(), "",
                            AppConstants.getOffer(), posDetails.getMCC_DSC());
                    Log.d("Catagory",posModel.getCategory());
                    posModel.setRewardMultiplier(new Random().nextInt(4)+1);
                    posModels.add(posModel);
                    getLatLngForAddress(posDetails.getOUTER_POSTAL_CODE() + posDetails.getINNER_POSTAL_CODE(),posModel);
                }
                Collections.sort(posModels,new PosOffersComparator());
                populatePOSList(posModels);
            }
        });
    }

    private void getLatLngForAddress(String address, final POSModel posModel) {
        final Map<String, String> parameter = new HashMap<>();
        parameter.put("address", address);
        parameter.put("key", "AIzaSyCWcjr8FbpiGxerhFjKRWJH0j6LEl9A4OU");


        ServerCommunication.getmInstance().addJSONGetRequest(AppConstants.GET_ADDRESS_URL, parameter, null, new ServiceResponseListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {
                Gson gson = new Gson();

                Coordinates latLng = gson.fromJson(response.toString(), Coordinates.class);
                String address = latLng.getResults().get(0).getFormatted_address();
                posModel.setAddress(address);
                posModel.setLat(latLng.getResults().get(0).getGeometry().getLocation().getLat());
                posModel.setLon(latLng.getResults().get(0).getGeometry().getLocation().getLng());
                Location posLocation = new Location("posLocaiton");
                posLocation.setLatitude(posModel.getLat());
                posLocation.setLongitude(posModel.getLon());
                posModel.setDistance(new DecimalFormat("0.000").format(AppConstants.calculateDistance(AppConstants.getFixedLocation(),posLocation))+"");
                adapter2.notifyDataSetChanged();
                //setMarkersOnMap(latLng.getResults().get(0).getFormatted_address(), latLng.getResults().get(0).getGeometry().getLocation(), mName, desc, posModel);
            }
        });
    }

    private void startNavigation(POSModel posModel) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + "51.461561,-0.210521" + "&daddr=" + posModel.getLat()+ "," + posModel.getLon()));
        startActivity(intent);
    }

    private void setShareButton(POSModel posModel) {
        String message = "http://maps.google.com/maps?q=" + posModel.getLat() + "," + posModel.getLon();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "See you at: " + message);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share Through"));
    }

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private void setCallButton(POSModel posModel) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9555308310"));
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(BestOffersActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    public interface OnClickListener{
        void onClick(View v, int position);
    }

    void showDialog(final POSModel posModel){
        final Dialog view = new Dialog(BestOffersActivity.this);
        view.setContentView(R.layout.layout_pos_items);
        view.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView merchant,address,offers,category,distance;
        Button navigate,share,call;
        LinearLayout bg;

        merchant = (TextView)view.findViewById(R.id.merchant);
        address = (TextView)view.findViewById(R.id.address);
        offers = (TextView)view.findViewById(R.id.offer);
        category = (TextView)view.findViewById(R.id.category);
        distance = (TextView) view.findViewById(R.id.distance);

        navigate = (Button)view.findViewById(R.id.navigate);
        share= (Button)view.findViewById(R.id.share);
        call = (Button)view.findViewById(R.id.call);

        bg = (LinearLayout)view.findViewById(R.id.bg_image);

        merchant.setText(posModel.getTitle());
        category.setText(posModel.getCategory());
        address.setText(posModel.getAddress());
        offers.setText(posModel.getOffers());
        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNavigation(posModel);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShareButton(posModel);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCallButton(posModel);
            }
        });
        distance.setText(posModel.getDistance()+" m");

        String catagory = posModel.getCategory();
        if(catagory.toLowerCase().contains("elect")){
            bg.setBackground(getResources().getDrawable(R.drawable.electronics));
        }else
        if(catagory.toLowerCase().contains("trav")){
            bg.setBackground(getResources().getDrawable(R.drawable.travle));
        }else
        if(catagory.toLowerCase().contains("clot")){
            bg.setBackground(getResources().getDrawable(R.drawable.cloths));
        }else
        if(catagory.toLowerCase().contains("din")){
            bg.setBackground(getResources().getDrawable(R.drawable.dining));
        }
        view.show();
    }

    void showMenuPopup(View v,int x,int y){
        final PopupWindow popupWindow = new PopupWindow(this);

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_menu_items,null);

        ImageButton showMap,showfilter;
        showMap = view.findViewById(R.id.showOnMap);
        showfilter = view.findViewById(R.id.showFilter);
        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(BestOffersActivity.this,LocationActivity2.class);
                if(searchText.trim().length()==0){
                    for(String s : AppConstants.getCatagories()){
                        searchText+= s+",";
                    }
                    searchText = searchText.substring(0,searchText.length()-1);
                }
                Log.d("SearchText",searchText);
                in.putExtra("CATEGORIES",searchText);

                startActivity(in);
            }
        });

        showfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        popupWindow.setFocusable(true);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(v,x,-y);
    }

}
