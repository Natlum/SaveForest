package es.jjsr.saveforest.contentProviderPackage;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import es.jjsr.saveforest.dto.AdviceGlobal;

/**
 * Created by José Juan Sosa Rodríguez on 01/11/2017.
 */

public class AdviceProvider {

    public static void insert(Activity activity, ContentResolver solve){
        Uri uri = Contract.Advice.CONTENT_URI_ADVICE;

        AdviceGlobal adviceGlobal = (AdviceGlobal) activity.getApplication();

        ContentValues values = new ContentValues();

        values.put(Contract.Advice.NAME, adviceGlobal.getName());
        values.put(Contract.Advice.DATE, adviceGlobal.getDate().getTime()/1000);
        values.put(Contract.Advice.DESCRIPTION, adviceGlobal.getDescription());
        values.put(Contract.Advice.ID_COUNTRY, adviceGlobal.getIdCountry());
        values.put(Contract.Advice.LATITUDE, adviceGlobal.getLatitude());
        values.put(Contract.Advice.LONGITUDE, adviceGlobal.getLongitude());
        values.put(Contract.Advice.NAME_IMAGE, adviceGlobal.getNameImage());
        values.put(Contract.Advice.PHONE_NUMBER, adviceGlobal.getPhoneNumber());

        solve.insert(uri, values);
    }
}
