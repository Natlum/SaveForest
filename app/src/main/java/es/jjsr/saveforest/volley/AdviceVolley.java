package es.jjsr.saveforest.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

import es.jjsr.saveforest.aplication.AppController;
import es.jjsr.saveforest.contentProviderPackage.BinnacleProvider;
import es.jjsr.saveforest.dto.Advice;
import es.jjsr.saveforest.dto.AdviceGlobal;
import es.jjsr.saveforest.resource.LoadAnsSaveImage;
import es.jjsr.saveforest.resource.constants.GConstants;
import es.jjsr.saveforest.restful.ImageRest;
import es.jjsr.saveforest.sync.Synchronization;

/**
 * Created by José Juan Sosa Rodríguez on 02/12/2017.
 */

public class AdviceVolley {
    final static String TAG = "JJSR response webapp";
    final static String route = GConstants.ADVICES_SERVER_ROUTE + "/all-advices";
    private static Context context;

    public AdviceVolley(Context context) {
        this.context = context;
    }

    public static void getAllAdvices(){
        String tag_json_obj = "getAllAdvices";
        String url = route;
        //Prepare the Request

        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(true);

        JsonArrayRequest getRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Display response RESPUESTA CORRECTA
                        Log.i(TAG, "Display response");
                        Synchronization.doUpdatesFromServerOnceGot(response);
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                        Log.i("RESPONSE_SERVER", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Error response
                        Log.i(TAG, "Error response");
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                    }
                });
        AdviceGlobal.getmInstance().addToRequestQueue(getRequest, tag_json_obj);
    }

    public static Boolean addAdvice(final Advice advice, final boolean withBinnacle, final int idBinnacle){
        final Boolean[] value = {false};
        String tag_json_obj = "addAdvice";
        String url = GConstants.ADVICES_SERVER_ROUTE + "/insert-advices";

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
        String dateString = originalFormat.format(advice.getDate());
        Integer date = Integer.valueOf(dateString);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idAdvice", advice.getId());
            jsonObject.put("name", advice.getName());
            jsonObject.put("description", advice.getDescription());
            jsonObject.put("idCountry", advice.getIdCountry());
            jsonObject.put("latitude", advice.getLatitude());
            jsonObject.put("longitude", advice.getLongitude());
            jsonObject.put("date", date);
            jsonObject.put("nameImage", advice.getNameImage());
            jsonObject.put("phoneNumber", advice.getPhoneNumber());
        } catch (JSONException e) {
            e.printStackTrace();
            return value[0];
        }

        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(true);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "It has been inserted correctly");
                        if (withBinnacle){
                            BinnacleProvider.deleteRecord(AdviceGlobal.getResolver(), idBinnacle);
                        }
                        if (advice.getNameImage() != null){
                            try {
                                Bitmap bitmap = LoadAnsSaveImage.loadImageFromStorageToSaveOnServer(context, advice.getNameImage());
                                new ImageVolley(context).uploadBitmap(bitmap, advice.getNameImage());
                            } catch (FileNotFoundException e) {
                                Log.i(TAG, "Fail to load image from device");
                            }
                        }
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                        value[0] = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "It has not been inserted");
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                        value[0] = false;
                    }
                });
        AdviceGlobal.getmInstance().addToRequestQueue(postRequest, tag_json_obj);
        return value[0];
    }

    public static Boolean updateAdvice(Advice advice, final boolean withBinnacle, final int idBinnacle){
        final Boolean[] value = {false};
        String tag_json_obj = "updateAdvice";
        String url = GConstants.ADVICES_SERVER_ROUTE + "/" + advice.getId();

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
        String dateString = originalFormat.format(advice.getDate());
        Integer date = Integer.valueOf(dateString);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idAdvice", advice.getId());
            jsonObject.put("name", advice.getName());
            jsonObject.put("description", advice.getDescription());
            jsonObject.put("idCountry", advice.getIdCountry());
            jsonObject.put("latitude", advice.getLatitude());
            jsonObject.put("longitude", advice.getLongitude());
            jsonObject.put("date", date);
            jsonObject.put("nameImage", advice.getNameImage());
            jsonObject.put("phoneNumber", advice.getPhoneNumber());
        } catch (JSONException e) {
            e.printStackTrace();
            return value[0];
        }

        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(true);

        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "It has been updated correctly");
                        if (withBinnacle){
                            BinnacleProvider.deleteRecord(AdviceGlobal.getResolver(), idBinnacle);
                        }
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                        value[0] = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "It has not been updated");
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                        value[0] = false;
                    }
                });
        AdviceGlobal.getmInstance().addToRequestQueue(putRequest, tag_json_obj);
        return value[0];
    }

    public static Boolean deleteAdvice(int id, final boolean withBinnacle, final int idBinnacle){
        final Boolean[] value = {false};
        String tag_json_obj = "deleteAdvice";
        String url = GConstants.ADVICES_SERVER_ROUTE + "/" + String.valueOf(id);

        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(true);

        StringRequest delRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "It has been deleted correctly");
                        if (withBinnacle){
                            BinnacleProvider.deleteRecord(AdviceGlobal.getResolver(), idBinnacle);
                        }
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                        value[0] = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "It has not been deleted");
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                        value[0] = false;
                    }
                });
        AdviceGlobal.getmInstance().addToRequestQueue(delRequest, tag_json_obj);
        return value[0];
    }
}
