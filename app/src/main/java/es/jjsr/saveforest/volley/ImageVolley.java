package es.jjsr.saveforest.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.ImageRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;

import java.io.IOException;

import es.jjsr.saveforest.dto.AdviceGlobal;
import es.jjsr.saveforest.resource.LoadAndSaveImage;
import es.jjsr.saveforest.resource.constants.GConstants;

/**
 * Created by José Juan Sosa Rodríguez on 22/12/2017.
 */

public class ImageVolley {
    final static String TAG = "JJSR response webapp";
    final static String routeUpload = GConstants.IMAGES_SERVER_ROUTE + "/upload";
    final static String routeDownload = GConstants.IMAGES_SERVER_ROUTE + "/download/";
    final static String routeDelete = GConstants.IMAGES_SERVER_ROUTE + "/delete/";

    public static void imageUpload(final String imagePath){
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, routeUpload,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Image is sended");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.error.VolleyError error) {
                Log.i(TAG, "Image is not sended");
            }
        });
        smr.addFile("uploadFile", imagePath);
        AdviceGlobal.getmInstance().addToRequestQueue(smr);
    }

    public static void imageRequest(final String fileName, final Context ctx){
        final Bitmap bitmap;

        ImageRequest request = new ImageRequest(routeDownload + fileName, null, null,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        Log.i(TAG, "Image is received");
                        try {
                            LoadAndSaveImage.saveImage(response, ctx, fileName);
                        } catch (IOException e) {
                            Log.i(TAG, "Fail to save image received\n" + e.getMessage());
                        }
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Image is not received");
                    }
                });
        AdviceGlobal.getmInstance().addToRequestQueue(request);
    }

    public static void imageDelete(final String fileName){
        String tag_json_obj = "deleteImage";
        String url = routeDelete + fileName;

        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(true);

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Image is deleted on server");
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Image is not received");
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                    }
                }
        );

        deleteRequest.setShouldCache(false);
        AdviceGlobal.getmInstance().addToRequestQueue(deleteRequest, tag_json_obj);
    }
}
