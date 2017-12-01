package in.nj.nearby.services;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

import in.nj.nearby.common.interfaces.listeners.ServiceResponseListener;


/**
 * Created by hp on 22-11-2017.
 */

public class CustomStringRequest extends StringRequest {

    private DemoRequest demoRequest;

    public CustomStringRequest(DemoRequest demoRequest , ServiceResponseListener listener){
        super(demoRequest.getType().getValue(),demoRequest.getUrl(),listener,listener);
        this.demoRequest = demoRequest;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if(demoRequest.getParameters()==null)
            return super.getParams();

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
