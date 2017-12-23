package es.jjsr.saveforest.volley;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
//import com.android.volley.error.VolleyError;
//import com.android.volley.request.ImageRequest;
//import com.android.volley.request.SimpleMultiPartRequest;

import es.jjsr.saveforest.dto.AdviceGlobal;
import es.jjsr.saveforest.resource.constants.GConstants;

/**
 * Created by José Juan Sosa Rodríguez on 22/12/2017.
 */

public class ImageVolley {
    final static String TAG = "JJSR response webapp";
    final static String routeUpload = GConstants.IMAGES_SERVER_ROUTE + "/upload";

  /*  public static void imageUpload(final String imagePath){
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, routeUpload,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Image is sended");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.VolleyError error) {
                Log.i(TAG, "Image is not sended");
            }
        });
        smr.addFile("uploadFile", imagePath);
        AdviceGlobal.getmInstance().addToRequestQueue(smr);
    }*/
}
