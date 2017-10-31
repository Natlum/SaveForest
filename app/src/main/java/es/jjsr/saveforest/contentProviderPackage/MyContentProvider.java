package es.jjsr.saveforest.contentProviderPackage;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;

/**
 * Created by José Juan Sosa Rodríguez on 29/10/2017.
 */

public class MyContentProvider extends ContentProvider {

    private static final int ADVICE_ONE_REG = 1; // content://es.jjsr.saveforest.contentProviderPackage.MyContentProvider/Advice/#
    private static final int ADVICE_ALL_REG = 2; // content://es.jjsr.saveforest.contentProviderPackage.MyContentProvider/Advice

    private static final int COUNTRY_ONE_REG = 3; // content://es.jjsr.saveforest.contentProviderPackage.MyContentProvider/Country/#
    private static final int COUNTRY_ALL_REG = 4; // content://es.jjsr.saveforest.contentProviderPackage.MyContentProvider/Country

    private SQLiteDatabase sqlDB;
    public DataBaseHelper dbHelper;
    private static final String DATABASE_NAME = "SaveForest.db";
    private static final int DATABASE_VERSION = 1;

    private static final String ADVICE_TABLE_NAME = "Advice";
    private static final String COUNTRY_TABLE_NAME = "Country";

    //Indicates an invalid content URI
    public static final int INVALID_URI = -1;

    //Defines a helper object that matches content URIs to table-specific parameters
    private static  final UriMatcher sUriMatcher;

    //Stores the MIME types served by this provider
    private static final SparseArray<String> sMimeTypes;


    /**
     * Initializes meta-data used by the content provider:
     * - UriMatcher that maps content URIs to codes
     * - MimeType array that return the custom MIME type of table
     */
    static {
        sUriMatcher = new UriMatcher(0);
        sMimeTypes = new SparseArray<String>();

        sUriMatcher.addURI(
                Contract.AUTHORITY,
                ADVICE_TABLE_NAME,
                ADVICE_ALL_REG);
        sUriMatcher.addURI(
                Contract.AUTHORITY,
                ADVICE_TABLE_NAME + "/#",
                ADVICE_ONE_REG);
        sUriMatcher.addURI(
                Contract.AUTHORITY,
                COUNTRY_TABLE_NAME,
                COUNTRY_ALL_REG);
        sUriMatcher.addURI(
                Contract.AUTHORITY,
                COUNTRY_TABLE_NAME + "/#",
                COUNTRY_ONE_REG);

        sMimeTypes.put(
                ADVICE_ALL_REG,
                "vnd.android.cursor.dir/vnd." +
                        Contract.AUTHORITY + "." + ADVICE_TABLE_NAME);
        sMimeTypes.put(
                ADVICE_ONE_REG,
                "vnd.android.cursor.item/vnd." +
                        Contract.AUTHORITY + "." + ADVICE_TABLE_NAME);
        sMimeTypes.put(
                COUNTRY_ALL_REG,
                "vnd.android.cursor.dir/vnd." +
                        Contract.AUTHORITY + "." + COUNTRY_TABLE_NAME);
        sMimeTypes.put(
                COUNTRY_ONE_REG,
                "vnd.android.cursor.item/vnd." +
                        Contract.AUTHORITY + "." + COUNTRY_TABLE_NAME);
    }

    public MyContentProvider() {
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DataBaseHelper(getContext(), DATABASE_NAME, DATABASE_VERSION);
        return (dbHelper == null) ? false : true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = null;

        switch (sUriMatcher.match(uri)){
            case ADVICE_ONE_REG:
                if (null == selection) selection = "";
                selection += Contract.Advice.ID_ADVICE + " = "
                        + uri.getLastPathSegment();
                qb.setTables(ADVICE_TABLE_NAME);
                break;
            case ADVICE_ALL_REG:
                if (TextUtils.isEmpty(sortOrder)) sortOrder = Contract.Advice.ID_ADVICE + " ASC";
                qb.setTables(ADVICE_TABLE_NAME);
                break;
            case COUNTRY_ONE_REG:
                if (null == selection) selection = "";
                selection += Contract.Country.ID_COUNTRY + " = "
                        + uri.getLastPathSegment();
                qb.setTables(COUNTRY_TABLE_NAME);
                break;
            case COUNTRY_ALL_REG:
                if (TextUtils.isEmpty(sortOrder)) sortOrder = Contract.Country.ID_COUNTRY + " ASC";
                qb.setTables(COUNTRY_TABLE_NAME);
                break;
        }

        Cursor c;
        c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        sqlDB = dbHelper.getWritableDatabase();

        String table = "";
        switch (sUriMatcher.match(uri)){
            case ADVICE_ALL_REG:
                table = ADVICE_TABLE_NAME;
                break;
        }

        long rowId = sqlDB.insert(table, "", contentValues);

        if (rowId > 0){
            Uri rowUri = ContentUris.appendId(uri.buildUpon(), rowId).build();
            getContext().getContentResolver().notifyChange(rowUri, null);
            return rowUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        sqlDB = dbHelper.getWritableDatabase();

        String table = "";
        switch (sUriMatcher.match(uri)){
            case ADVICE_ONE_REG:
                if (null == selection) selection = "";
                selection += Contract.Advice.ID_ADVICE + " = "
                        + uri.getLastPathSegment();
                table = ADVICE_TABLE_NAME;
                break;
            case ADVICE_ALL_REG:
                table = ADVICE_TABLE_NAME;
                break;
        }
        int rows = sqlDB.delete(table, selection, selectionArgs);
        if (rows > 0){
            getContext().getContentResolver().notifyChange(uri, null);
            return rows;
        }
        throw new SQLException("Failed to delete row into " + uri);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        sqlDB = dbHelper.getWritableDatabase();

        String table = "";
        switch (sUriMatcher.match(uri)){
            case ADVICE_ONE_REG:
                if (null == selection) selection = "";
                selection += Contract.Advice.ID_ADVICE + " = "
                        +uri.getLastPathSegment();
                table = ADVICE_TABLE_NAME;
                break;
            case ADVICE_ALL_REG:
                table = ADVICE_TABLE_NAME;
                break;
        }

        int rows = sqlDB.update(table, values, selection, selectionArgs);
        if (rows > 0){
            getContext().getContentResolver().notifyChange(uri, null);
            return rows;
        }
        throw new SQLException("Failed to update row into " + uri);
    }
}
