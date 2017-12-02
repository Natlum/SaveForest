package es.jjsr.saveforest.contentProviderPackage;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import es.jjsr.saveforest.dto.Binnacle;

/**
 * Proveedor de acceso a la tabla Binaccle.
 * Contiene lo necesario para hacer un CRUD
 * Created by José Juan Sosa Rodríguez on 01/11/2017.
 */

public class BinnacleProvider {

    public static void insertRecord(ContentResolver solve, Binnacle binnacle){
        Uri uri = Contract.Binnacle.CONTENT_URI_BINNACLE;

        ContentValues values = new ContentValues();
        values.put(Contract.Binnacle.ID_ADVICE, binnacle.getId_advice());
        values.put(Contract.Binnacle.OPERATION, binnacle.getOperation());

        solve.insert(uri, values);
    }

    public static void deleteRecord(ContentResolver solve, int idBinnacle){
        Uri uri = Uri.parse(Contract.Binnacle.CONTENT_URI_BINNACLE +"/" + idBinnacle);
        solve.delete(uri, null, null);
    }

    public static void updateRecord(ContentResolver solve, Binnacle binnacle){
        Uri uri = Uri.parse(Contract.Binnacle.CONTENT_URI_BINNACLE +"/" + binnacle.getId());
        ContentValues values = new ContentValues();
        values.put(Contract.Binnacle.ID_ADVICE, binnacle.getId_advice());
        values.put(Contract.Binnacle.OPERATION, binnacle.getOperation());

        solve.update(uri, values, null, null);
    }

    public static Binnacle readRecord(ContentResolver solve, int idBinnacle){
        Uri uri = Uri.parse(Contract.Binnacle.CONTENT_URI_BINNACLE +"/" + idBinnacle);

        String[] projection = {
                Contract.Binnacle.ID_ADVICE,
                Contract.Binnacle.OPERATION
        };

        Cursor cursor = solve.query(uri, projection, null, null, null);

        if (cursor.moveToFirst()){
            Binnacle binnacle = new Binnacle();
            binnacle.setId(idBinnacle);
            binnacle.setId_advice(cursor.getInt(cursor.getColumnIndex(Contract.Binnacle.ID_ADVICE)));
            binnacle.setOperation(cursor.getInt(cursor.getColumnIndex(Contract.Binnacle.OPERATION)));
            return binnacle;
        }else {
            return null;
        }
    }

    public static ArrayList<Binnacle> readAllRecord(ContentResolver solve){
        Uri uri = Contract.Binnacle.CONTENT_URI_BINNACLE;

        String[] projection = {
                Contract.Binnacle.ID,
                Contract.Binnacle.ID_ADVICE,
                Contract.Binnacle.OPERATION
        };

        Cursor cursor = solve.query(uri, projection, null, null, null);

        ArrayList<Binnacle> binnacles = new ArrayList<>();
        Binnacle binnacle;

        while(cursor.moveToNext()){
            binnacle = new Binnacle();
            binnacle.setId(cursor.getInt(cursor.getColumnIndex(Contract.Binnacle.ID)));
            binnacle.setId_advice(cursor.getInt(cursor.getColumnIndex(Contract.Binnacle.ID_ADVICE)));
            binnacle.setOperation(cursor.getInt(cursor.getColumnIndex(Contract.Binnacle.OPERATION)));
            binnacles.add(binnacle);
        }

        return binnacles;
    }

}
