package es.jjsr.saveforest.contentProviderPackage;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    public static Uri insertRecordFromServer(ContentResolver solve, Advice advice){
        Uri uri = Contract.Advice.CONTENT_URI_ADVICE;

        ContentValues values = new ContentValues();

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
        String dateString = originalFormat.format(advice.getDate());
        Integer date = Integer.valueOf(dateString);

        values.put(Contract.Advice.NAME, advice.getName());
        values.put(Contract.Advice.DATE, date);
        values.put(Contract.Advice.DESCRIPTION, advice.getDescription());
        values.put(Contract.Advice.ID_COUNTRY, advice.getIdCountry());
        values.put(Contract.Advice.LATITUDE, advice.getLatitude());
        values.put(Contract.Advice.LONGITUDE, advice.getLongitude());
        values.put(Contract.Advice.NAME_IMAGE, advice.getNameImage());
        values.put(Contract.Advice.PHONE_NUMBER, advice.getPhoneNumber());
        values.put(Contract.Advice.ID_ADVICE, advice.getId());

        return solve.insert(uri, values);
    }

    public static void deleteRecord(ContentResolver solve, int idAdvice){
        Uri uri = Uri.parse(Contract.Advice.CONTENT_URI_ADVICE +"/" + idAdvice);
        solve.delete(uri, null, null);
    }

    public static void deleteRecordWithBinnacle(ContentResolver solve, int idAdvice){
        Advice advice = null;
        try {
            advice = readFullRecord(solve, idAdvice);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("Resolv", "Nombre de la imagen que se le pasa al binnacle " + advice.getNameImage());
        deleteRecord(solve, idAdvice);

        Binnacle binnacle = new Binnacle();
        binnacle.setId_advice(idAdvice);
        binnacle.setOperation(GConstants.OPERATION_DELETE);

        if (advice.getNameImage() != null){
            binnacle.setImage_name(advice.getNameImage());
        }
        Log.i("Resolv", "Nombre que tiene guardado el binnacle" + binnacle.getImage_name());
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

    public static Advice readFullRecord(ContentResolver solve, int idAdvice) throws ParseException {
        Uri uri = Uri.parse(Contract.Advice.CONTENT_URI_ADVICE +"/" + idAdvice);

        String[] projection = {
                Contract.Advice.ID_ADVICE,
                Contract.Advice.DESCRIPTION,
                Contract.Advice.ID_COUNTRY,
                Contract.Advice.NAME,
                Contract.Advice.NAME_IMAGE,
                Contract.Advice.PHONE_NUMBER,
                Contract.Advice.DATE,
                Contract.Advice.LONGITUDE,
                Contract.Advice.LATITUDE
        };

        Cursor cursor = solve.query(uri, projection, null, null, null);

        if (cursor.moveToFirst()){
            Advice advice = new Advice();
            advice.setId(idAdvice);
            advice.setDescription(cursor.getString(cursor.getColumnIndex(Contract.Advice.DESCRIPTION)));
            advice.setIdCountry(cursor.getInt(cursor.getColumnIndex(Contract.Advice.ID_COUNTRY)));
            advice.setName(cursor.getString(cursor.getColumnIndex(Contract.Advice.NAME)));
            advice.setNameImage(cursor.getString(cursor.getColumnIndex(Contract.Advice.NAME_IMAGE)));
            advice.setPhoneNumber(cursor.getInt(cursor.getColumnIndex(Contract.Advice.PHONE_NUMBER)));
            advice.setLongitude(cursor.getDouble(cursor.getColumnIndex(Contract.Advice.LONGITUDE)));
            advice.setLatitude(cursor.getDouble(cursor.getColumnIndex(Contract.Advice.LATITUDE)));
            Integer dateInt = cursor.getInt(cursor.getColumnIndex(Contract.Advice.DATE));
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = originalFormat.parse(dateInt.toString());
            advice.setDate(date);
            return advice;
        }else {
            return null;
        }
    }

    public static ArrayList<Advice> readAllRecord(ContentResolver solve) throws ParseException {
        Uri uri = Contract.Advice.CONTENT_URI_ADVICE;

        String[] projection = {
                Contract.Advice.ID_ADVICE,
                Contract.Advice.DESCRIPTION,
                Contract.Advice.ID_COUNTRY,
                Contract.Advice.NAME,
                Contract.Advice.NAME_IMAGE,
                Contract.Advice.PHONE_NUMBER,
                Contract.Advice.DATE,
                Contract.Advice.LONGITUDE,
                Contract.Advice.LATITUDE
        };

        Cursor cursor = solve.query(uri, projection, null, null, null);

        ArrayList<Advice> advices = new ArrayList<>();
        Advice advice;

        while(cursor.moveToNext()){
            advice = new Advice();
            advice.setId(cursor.getInt(cursor.getColumnIndex(Contract.Advice.ID_ADVICE)));
            advice.setDescription(cursor.getString(cursor.getColumnIndex(Contract.Advice.DESCRIPTION)));
            advice.setIdCountry(cursor.getInt(cursor.getColumnIndex(Contract.Advice.ID_COUNTRY)));
            advice.setName(cursor.getString(cursor.getColumnIndex(Contract.Advice.NAME)));
            advice.setNameImage(cursor.getString(cursor.getColumnIndex(Contract.Advice.NAME_IMAGE)));
            advice.setPhoneNumber(cursor.getInt(cursor.getColumnIndex(Contract.Advice.PHONE_NUMBER)));

            Integer value = cursor.getInt(cursor.getColumnIndex(Contract.Advice.DATE));
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = originalFormat.parse(value.toString());

            advice.setDate(date);
            advice.setLongitude(cursor.getDouble(cursor.getColumnIndex(Contract.Advice.LONGITUDE)));
            advice.setLatitude(cursor.getDouble(cursor.getColumnIndex(Contract.Advice.LATITUDE)));
            advices.add(advice);
        }

        return advices;
    }
}
