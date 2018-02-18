package in.nj.nearby.services;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.Map;

import in.nj.nearby.common.interfaces.listeners.ServiceResponseListener;


/**
 * Created by hp on 22-11-2017.
 */

public class CustomJSONRequest extends JsonObjectRequest {

    private DemoRequest demoRequest;

    public CustomJSONRequest(DemoRequest demoRequest , ServiceResponseListener listener){
        super(demoRequest.getType().getValue(),demoRequest.getUrl(),
                demoRequest.getJsonObject(),listener,listener);
        this.demoRequest = demoRequest;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if(demoRequest.getParameters()==null)
            return super.getParams();
//hi

        return demoRequest.getParameters();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if(demoRequest.getHeaders()==null)
            return super.getHeaders();

        return demoRequest.getHeaders();
    }

    @Override
    public Priority getPriority() {
        return demoRequest.getPriority();
    }
}
