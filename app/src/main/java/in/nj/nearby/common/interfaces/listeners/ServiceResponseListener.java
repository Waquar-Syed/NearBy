package in.nj.nearby.common.interfaces.listeners;

import com.android.volley.Response;

/**
 * Created by hp on 22-11-2017.
 */


public interface ServiceResponseListener<T> extends Response.Listener<T> , Response.ErrorListener {
}