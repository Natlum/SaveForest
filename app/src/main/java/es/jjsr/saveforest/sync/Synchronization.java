package es.jjsr.saveforest.sync;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.jjsr.saveforest.contentProviderPackage.AdviceProvider;
import es.jjsr.saveforest.contentProviderPackage.BinnacleProvider;
import es.jjsr.saveforest.dto.Advice;
import es.jjsr.saveforest.dto.Binnacle;
import es.jjsr.saveforest.resource.constants.GConstants;
import es.jjsr.saveforest.volley.AdviceVolley;

/**
 * Created by José Juan Sosa Rodríguez on 02/12/2017.
 */

public class Synchronization {
    private static final String LOGTAG = "JJSR - Synchronization";
    private static ContentResolver resolver;
    private static Context context;
    private static boolean waitingForServerResponse = false;

    public Synchronization(Context context) {
        this.resolver = context.getContentResolver();
        this.context = context;
    }

    public synchronized static boolean isWaitingForServerResponse(){
        return waitingForServerResponse;
    }

    public synchronized static void  setWaitingForServerResponse(boolean waitingForServerResponse){
        Synchronization.waitingForServerResponse = waitingForServerResponse;
    }

    public synchronized boolean syncUp(){
        Log.i(LOGTAG, "Sync Up");

        if (isWaitingForServerResponse()){
            return true;
        }

        if (GConstants.VERSION_ADMINISTRATOR){
            //getUpdatesFromServer();
            Log.i(LOGTAG, "Esntra a sincronizar");
            sendUpdatesToServer();
        }else{
            getUpdatesFromServer();
        }

        return true;
    }

    private void sendUpdatesToServer() {
        ArrayList<Binnacle> regBinnacle = BinnacleProvider.readAllRecord(resolver);
        for (Binnacle binnacle : regBinnacle){
            switch (binnacle.getOperation()){
                case GConstants.OPERATION_INSERT:
                    Advice advice = null;
                    try {
                        advice = AdviceProvider.readFullRecord(resolver, binnacle.getId_advice());
                        AdviceVolley.addAdvice(advice, true, binnacle.getId());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case GConstants.OPERATION_UPDATE:
                    try {
                        advice = AdviceProvider.readFullRecord(resolver, binnacle.getId_advice());
                        AdviceVolley.updateAdvice(advice, true, binnacle.getId());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case GConstants.OPERATION_DELETE:
                    AdviceVolley.deleteAdvice(binnacle.getId_advice(), true, binnacle.getId());
                    break;
            }
        }
    }

    private void getUpdatesFromServer() {
        AdviceVolley.getAllAdvices();
    }

    public static void doUpdatesFromServerOnceGot(JSONArray jsonArray){
        Log.i(LOGTAG, "Recibir actualizaciones del servidor");

        try {
            ArrayList<Integer> updateRecordIdentifiers = new ArrayList<Integer>();
            ArrayList<Advice> newRecord = new ArrayList<>();
            ArrayList<Advice> oldRecord = AdviceProvider.readAllRecord(resolver);
            ArrayList<Integer> oldRecordIdentifiers = new ArrayList<Integer>();

            for (Advice i : oldRecord){
                oldRecordIdentifiers.add(i.getId());
            }

            JSONObject obj = null;
            for (int i = 0; i < jsonArray.length(); i++){
                obj = jsonArray.getJSONObject(i);
                Advice newAdvice = new Advice();
                newAdvice.setId(obj.getInt("idAdvice"));
                newAdvice.setName(obj.getString("name"));
                newAdvice.setDescription(obj.getString("description"));
                newAdvice.setIdCountry(obj.getInt("idCountry"));
                newAdvice.setLatitude(obj.getDouble("latitude"));
                newAdvice.setLongitude(obj.getDouble("longitude"));

                Integer value = obj.getInt("date");
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
                Date date = originalFormat.parse(value.toString());

                newAdvice.setDate(date);
                if (!obj.getString("nameImage").equals("none")){
                    newAdvice.setNameImage(obj.getString("nameImage"));
                }
                newAdvice.setPhoneNumber(obj.getInt("phoneNumber"));

                newRecord.add(newAdvice);
            }

            for (Advice advice : newRecord){
                try {
                    if (oldRecordIdentifiers.contains(advice.getId())){
                        AdviceProvider.updateRecord(resolver, advice);
                    }else {
                        AdviceProvider.insertRecordFromServer(resolver, advice);
                    }
                    updateRecordIdentifiers.add(advice.getId());
                }catch (Exception e){
                    Log.i(LOGTAG, "Probablemente el registro ya existía en la BD. \n" + e.getMessage());
                }
            }

            for (Advice advice : oldRecord){
                if (!updateRecordIdentifiers.contains(advice.getId())){
                    try {
                        AdviceProvider.deleteRecord(resolver, advice.getId());
                    }catch (Exception e){
                        Log.i(LOGTAG, "Error al borrar el registro con id:" + advice.getId() + "\n" + e.getMessage());
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
