package es.jjsr.saveforest.volley;

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

import java.text.SimpleDateFormat;

import es.jjsr.saveforest.aplication.AppController;
import es.jjsr.saveforest.contentProviderPackage.BinnacleProvider;
import es.jjsr.saveforest.dto.Advice;
import es.jjsr.saveforest.dto.AdviceGlobal;
import es.jjsr.saveforest.resource.constants.GConstants;
import es.jjsr.saveforest.sync.Synchronization;

/**
 * Created by José Juan Sosa Rodríguez on 02/12/2017.
 */

public class AdviceVolley {
    final static String route = GConstants.ADVICES_SERVER_ROUTE + "/all-advices";

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
                        Synchronization.doUpdatesFromServerOnceGot(response);
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Error response
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                    }
                });
        AdviceGlobal.getmInstance().addToRequestQueue(getRequest, tag_json_obj);
    }

    public static void addAdvice(Advice advice, final boolean withBinnacle, final int idBinnacle){
        String tag_json_obj = "addAdvice";
        String url = GConstants.ADVICES_SERVER_ROUTE + "/insert-advices";

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
        Log.i("Tibu", "Fecha" + advice.getDate().toString());
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
        }

        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(true);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Tibu", "Se insertó");
                        if (withBinnacle){
                            BinnacleProvider.deleteRecord(AdviceGlobal.getResolver(), idBinnacle);
                        }
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Tibu", "No se insertó");
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                    }
                });
        AdviceGlobal.getmInstance().addToRequestQueue(postRequest, tag_json_obj);
    }

    public static void updateAdvice(Advice advice, final boolean withBinnacle, final int idBinnacle){
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
        }

        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(true);

        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (withBinnacle){
                            BinnacleProvider.deleteRecord(AdviceGlobal.getResolver(), idBinnacle);
                        }
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                    }
                });
        AdviceGlobal.getmInstance().addToRequestQueue(putRequest, tag_json_obj);
    }

    public static void deleteAdvice(int id, final boolean withBinnacle, final int idBinnacle){
        String tag_json_obj = "deleteAdvice";
        String url = GConstants.ADVICES_SERVER_ROUTE + "/" + String.valueOf(id);

        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(true);

        StringRequest delRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (withBinnacle){
                            BinnacleProvider.deleteRecord(AdviceGlobal.getResolver(), idBinnacle);
                        }
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AdviceGlobal.getmInstance().getSynchronization().setWaitingForServerResponse(false);
                    }
                });
        AdviceGlobal.getmInstance().addToRequestQueue(delRequest, tag_json_obj);
    }
}
