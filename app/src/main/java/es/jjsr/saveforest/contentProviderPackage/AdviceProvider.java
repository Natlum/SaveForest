package es.jjsr.saveforest.contentProviderPackage;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.text.SimpleDateFormat;

import es.jjsr.saveforest.dto.Advice;
import es.jjsr.saveforest.dto.AdviceGlobal;
import es.jjsr.saveforest.dto.Binnacle;
import es.jjsr.saveforest.resource.constants.GConstants;

/**
 * Proveedor de acceso a la tabla Advice.
 * Contiene lo necesario para hacer un CRUD
 * Created by José Juan Sosa Rodríguez on 01/11/2017.
 */

public class AdviceProvider {

    public static Uri insertRecord(Activity activity, ContentResolver solve){
        Uri uri = Contract.Advice.CONTENT_URI_ADVICE;

        AdviceGlobal adviceGlobal = (AdviceGlobal) activity.getApplication();

        ContentValues values = new ContentValues();

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
        String dateString = originalFormat.format(adviceGlobal.getDate());
        Integer date = Integer.valueOf(dateString);

        values.put(Contract.Advice.NAME, adviceGlobal.getName());
        values.put(Contract.Advice.DATE, date);
        values.put(Contract.Advice.DESCRIPTION, adviceGlobal.getDescription());
        values.put(Contract.Advice.ID_COUNTRY, adviceGlobal.getIdCountry());
        values.put(Contract.Advice.LATITUDE, adviceGlobal.getLatitude());
        values.put(Contract.Advice.LONGITUDE, adviceGlobal.getLongitude());
        values.put(Contract.Advice.NAME_IMAGE, adviceGlobal.getNameImage());
        values.put(Contract.Advice.PHONE_NUMBER, adviceGlobal.getPhoneNumber());

        return solve.insert(uri, values);
    }

    public static void insertRecordWithBinnacle(Activity activity, ContentResolver solve){
        Uri uri = insertRecord(activity, solve);

        Binnacle binnacle = new Binnacle();
        binnacle.setId_advice(Integer.parseInt(uri.getLastPathSegment()));
        binnacle.setOperation(GConstants.OPERATION_INSERT);

        BinnacleProvider.insertRecord(solve, binnacle);
    }

    public static void deleteRecord(ContentResolver solve, int idAdvice){
        Uri uri = Uri.parse(Contract.Advice.CONTENT_URI_ADVICE +"/" + idAdvice);
        solve.delete(uri, null, null);
    }

    public static void deleteRecordWithBinnacle(ContentResolver solve, int idAdvice){
        deleteRecord(solve, idAdvice);

        Binnacle binnacle = new Binnacle();
        binnacle.setId_advice(idAdvice);
        binnacle.setOperation(GConstants.OPERATION_DELETE);

        BinnacleProvider.insertRecord(solve, binnacle);
    }

    public static void updateRecord(ContentResolver solve, Advice advice){
        Uri uri = Uri.parse(Contract.Advice.CONTENT_URI_ADVICE +"/" + advice.getId());
        ContentValues values = new ContentValues();
        values.put(Contract.Advice.DESCRIPTION, advice.getDescription());

        solve.update(uri, values, null, null);
    }

    public static void updateRecordWithBinnacle(ContentResolver solve, Advice advice){
        updateRecord(solve, advice);

        Binnacle binnacle = new Binnacle();
        binnacle.setId_advice(advice.getId());
        binnacle.setOperation(GConstants.OPERATION_UPDATE);

        BinnacleProvider.insertRecord(solve, binnacle);
    }

    public static Advice readRecord(ContentResolver solve, int idAdvice){
        Uri uri = Uri.parse(Contract.Advice.CONTENT_URI_ADVICE +"/" + idAdvice);

        String[] projection = {
                Contract.Advice.ID_ADVICE,
                Contract.Advice.DESCRIPTION
        };

        Cursor cursor = solve.query(uri, projection, null, null, null);

        if (cursor.moveToFirst()){
            Advice advice = new Advice();
            advice.setId(idAdvice);
            advice.setDescription(cursor.getString(cursor.getColumnIndex(Contract.Advice.DESCRIPTION)));
            return advice;
        }else {
            return null;
        }
    }
}
