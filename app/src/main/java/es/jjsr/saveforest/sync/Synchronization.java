package es.jjsr.saveforest.sync;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import es.jjsr.saveforest.contentProviderPackage.AdviceProvider;
import es.jjsr.saveforest.contentProviderPackage.BinnacleProvider;
import es.jjsr.saveforest.dto.Advice;
import es.jjsr.saveforest.dto.Binnacle;
import es.jjsr.saveforest.resource.constants.GConstants;

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
                        advice = AdviceProvider.readRecord(resolver, binnacle.getId_advice());
                        AdviceVolley.addAdvice(advice, true, binnacle.getId());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case GConstants.OPERATION_UPDATE:
                    try {
                        advice = AdviceProvider.readRecord(resolver, binnacle.getId_advice());
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
        AdviceVolley.getAllAdvice();
    }
}
