package in.nj.nearby.services;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

import in.nj.nearby.ApplicationController;
import in.nj.nearby.common.interfaces.listeners.ServiceResponseListener;

/**
 * Created by rishikapriya on 01/12/17.
 */

public class ServerCommunication {

    private static final String TAG = ServerCommunication.class.getName();
    private static ServerCommunication mInstance;

    public static ServerCommunication getmInstance(){
        if(mInstance == null){
            mInstance = new ServerCommunication();
        }
        return mInstance;
    }

    public void addJSONGetRequest(String url, final Map<String, String> requestParameters, final Map<String,String> headers, final ServiceResponseListener responseListener){
        DemoRequest demoRequest;
        if(requestParameters != null){
             demoRequest= DemoRequest.builder().requestMethod(DemoRequest.TYPE.GET)
                                    .url(url + getStringFromRequest(requestParameters))
                                    .headers(headers).build();
        }else {
            demoRequest = DemoRequest.builder().requestMethod(DemoRequest.TYPE.GET)
                    .url(url)
                    .headers(headers).build();
        }

        ApplicationController.getInstance().getRequestQueue().add(new CustomJSONRequest(demoRequest, responseListener));
    }

    private String getStringFromRequest(Map<String, String> request) {
        Iterator iterator = request.entrySet().iterator();
        String request_string = "";

        while (iterator.hasNext()){
            Map.Entry request_item = (Map.Entry) iterator.next();
            request_string += request_item.getKey() + "=" + request_item.getValue() + "&";
        }
        return request_string.substring(0, request_string.length()-1);
    }
}
