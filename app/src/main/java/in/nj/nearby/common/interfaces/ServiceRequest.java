package in.nj.nearby.common.interfaces;


import com.android.volley.Request;

import in.nj.nearby.common.interfaces.listeners.ServiceResponseListener;
import in.nj.nearby.services.DemoRequest;

/**
 * Created by hp on 22-11-2017.
 */

public abstract class ServiceRequest {

    public DemoRequest demoRequest;
    public ServiceResponseListener serviceResponseListener;

    public ServiceRequest(DemoRequest demoRequest, ServiceResponseListener serviceResponseListener){
        if(demoRequest==null || serviceResponseListener ==null)
            throw new NullPointerException("DemoRequest or ServiceResponseListener cannot be null");
        this.demoRequest = demoRequest;
        this.serviceResponseListener = serviceResponseListener;
    }

    /**
     * Creates and returns a network request depending on the implementation.
     *
     * @return Volley Request
     */
    public abstract Request getRequest();

}
