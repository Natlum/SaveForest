package es.jjsr.saveforest.contentProviderPackage;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import es.jjsr.saveforest.dto.Country;

/**
 * Created by José Juan Sosa Rodríguez on 26/11/2017.
 */

public class CountryProvider {

    public static void insertRecord(ContentResolver solve, Country country){
        Uri uri = Contract.Country.CONTENT_URI_COUNTRY;

        ContentValues values = new ContentValues();

        values.put(Contract.Country.NAME_COUNTRY, country.getName_country());
        values.put(Contract.Country.CODE_COUNTRY, country.getCode_country());

        solve.insert(uri, values);
    }

    public static Country readRecord(ContentResolver solve, int idCountry){
        Uri uri = Uri.parse(Contract.Country.CONTENT_URI_COUNTRY +"/" + idCountry);

        String[] projection = {
                Contract.Country.ID_COUNTRY,
                Contract.Country.NAME_COUNTRY,
                Contract.Country.CODE_COUNTRY
        };

        Cursor cursor = solve.query(uri, projection, null, null, null);

        if (cursor.moveToFirst()){
            Country country = new Country();
            country.setId_country(idCountry);
            country.setName_country(cursor.getString(cursor.getColumnIndex(Contract.Country.NAME_COUNTRY)));
            country.setCode_country(cursor.getString(cursor.getColumnIndex(Contract.Country.CODE_COUNTRY)));
            return country;
        }else {
            return null;
        }
    }

    public static int allRowsCountries(ContentResolver solve){
        int rows = 0;
        Uri uri = Contract.Country.CONTENT_URI_COUNTRY;

        String[] projection = {
                Contract.Country.ID_COUNTRY,
                Contract.Country.NAME_COUNTRY,
                Contract.Country.CODE_COUNTRY
        };

        Cursor cursor = solve.query(uri, projection, null, null, null);

        if (cursor.moveToFirst() == false){
            return 0;
        }

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            rows ++;
        }

        return rows;
    }

    public static void deleteAllRecord(ContentResolver solve){
        Uri uri = Contract.Country.CONTENT_URI_COUNTRY;
        solve.delete(uri, null, null);
    }
}
