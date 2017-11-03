package es.jjsr.saveforest.contentProviderPackage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by José Juan Sosa Rodríguez on 29/10/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String ADVICE_TABLE_NAME = "Advice";
    private static final String COUNTRY_TABLE_NAME = "Country";

    public DataBaseHelper(Context context, String DATABASE_NAME, int  DATABASE_VERSION) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys='ON';");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Date save to System.currentTimeMillis() or CURRENT_TIMESTAMP
        db.execSQL("CREATE TABLE "
                    + ADVICE_TABLE_NAME
                    + "( " + Contract.Advice.ID_ADVICE + " INTEGER PRIMARY KEY ON CONFLICT ROLLBACK AUTOINCREMENT, "
                    + Contract.Advice.NAME + " TEXT, "
                    + Contract.Advice.DATE + " INTEGER, "
                    + Contract.Advice.DESCRIPTION + " TEXT, "
                    + Contract.Advice.ID_COUNTRY + " INTEGER, "
                    + Contract.Advice.PHONE_NUMBER + " INTEGER, "
                    + Contract.Advice.LATITUDE + " REAL, "
                    + Contract.Advice.LONGITUDE + " REAL, "
                    + Contract.Advice.NAME_IMAGE + " TEXT ); "
        );

        db.execSQL("CREATE TABLE "
                + COUNTRY_TABLE_NAME
                + "( " + Contract.Country.ID_COUNTRY + " INTEGER PRIMARY KEY ON CONFLICT ROLLBACK AUTOINCREMENT, "
                + Contract.Country.NAME_COUNTRY + " TEXT, "
                + Contract.Country.CODE_COUNTRY + " TEXT ); "
        );

        initializerData(db);
    }

    private void initializerData(SQLiteDatabase db){
        int now = (int) (new Date().getTime()/1000);
        db.execSQL("INSERT INTO " + COUNTRY_TABLE_NAME + "( " + Contract.Country.NAME_COUNTRY + ", " +
                Contract.Country.CODE_COUNTRY + ") " +
                "VALUES ('España', '+34'), "
                + "('Reino Unido', '+44'), "
                + "('Alemania', '+49'), "
                + "('Dinamarca', '+45'), "
                + "('Francia', '+33'), "
                + "('Greecia', '+30'), "
                + "('Italia', '+39')");

        db.execSQL("INSERT INTO " + ADVICE_TABLE_NAME + "( " + Contract.Advice.NAME + ", " +
                Contract.Advice.DATE + ", " + Contract.Advice.DESCRIPTION + ", " +
                Contract.Advice.ID_COUNTRY + ", " + Contract.Advice.PHONE_NUMBER + ", " +
                Contract.Advice.LATITUDE + ", " + Contract.Advice.LONGITUDE + ", " +
                Contract.Advice.NAME_IMAGE + ") " +
                "VALUES ('Usuario de prueba 1', 19841003, 'Esto es una prueba precargada.', 1, 666777888, null, null, null), "
                + "('Usuario de prueba 2', 19841003, 'Esto es una prueba precargada.', 3, null, 28.128041, -15.446438, null), "
                + "('Usuario de prueba 3', 19841003, 'Esto es una prueba precargada.', 1, null, 28.127343, -15.446991, null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ADVICE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COUNTRY_TABLE_NAME);

        onCreate(db);
    }
}