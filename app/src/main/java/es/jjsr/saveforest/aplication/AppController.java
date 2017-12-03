package es.jjsr.saveforest.aplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import es.jjsr.saveforest.resource.constants.GConstants;
import es.jjsr.saveforest.contentProviderPackage.Contract;
import es.jjsr.saveforest.sync.Sincronizacion;
import es.jjsr.saveforest.volley.Utils.LruBitmapCache;

/**
 * Created by José Juan Sosa Rodríguez on 16/11/2017.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static AppController mInstance;
    private static ContentResolver resolvedor;

    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = Contract.AUTHORITY;
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "es.jjsr.saveforest";
    // The account name
    public static final String ACCOUNT = "Save Forest";
    // Sync interval constants
    public static final long SYNC_INTERVAL = GConstants.SYNC_INTERVAL; // Cada minuto

    private Sincronizacion sincronizacion;


    // Instance fields
    Account mAccount;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        resolvedor = getContentResolver();

        activarSincronizacion();
    }

    public static ContentResolver getResolvedor(){
        return resolvedor;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return newAccount;
    }

    private void activarSincronizacion() {
        // Create the dummy account
        mAccount = CreateSyncAccount(this);
        //Se lanza la sincronización siempre que hay conexión de INTERNET

        //resolvedor.setIsSyncable(mAccount, AUTHORITY, 1); //Creo que no hace falta ponerlo porque ya está en el proveedor en el manifest android:syncable="true"
        resolvedor.setSyncAutomatically(mAccount, AUTHORITY, true);
        resolvedor.setMasterSyncAutomatically(true);
        resolvedor.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
    }

    public Sincronizacion getSincronizacion() {
        return sincronizacion;
    }

    public void setSincronizacion(Sincronizacion sincronizacion) {
        this.sincronizacion = sincronizacion;
    }
}
