package es.jjsr.saveforest.aplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import es.jjsr.saveforest.contentProviderPackage.Contract;
import es.jjsr.saveforest.resource.constants.GConstants;
import es.jjsr.saveforest.sync.Synchronization;

/**
 * Created by José Juan Sosa Rodríguez on 16/11/2017.
 */

public class AppController extends Application{

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static AppController mInstance;
    private static ContentResolver resolver;

    //The authority for the sync adapter's content provider
    public static final String AUTHORITY = Contract.AUTHORITY;
    //An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "es.jjsr.saveforest";
    //The account name
    public static final String ACCOUNT = "SaveForest";
    //Sync interval constants
    public static final long SYNC_INTERVAL = GConstants.SYNC_INTERVAL;

    private Synchronization synchronization;

    //Instance fields
    Account mAccount;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        resolver = getContentResolver();

        activeSynchronization();
    }


    public static ContentResolver getResolver() {
        return resolver;
    }

    public static synchronized AppController getmInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        getRequestQueue();
        if (mImageLoader == null){
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> reg){
        reg.setTag(TAG);
        getRequestQueue().add(reg);
    }

    public void cancelPendingRequests(Object tag){
        if (mRequestQueue != null){
            mRequestQueue.cancellAll(tag);
        }
    }

    public static Account CreateSyncAccount(Context context){
        //Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        //Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
        Add the account and account type, no password or user data
        If successful, return the Account object, othervise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)){

        }else {

        }
        return newAccount;
    }

    private void activeSynchronization(){
        //Create the dummy account
        mAccount = CreateSyncAccount(this);
        //Se lanza la sincronización siempre que hay conexión de INTERNET

        resolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        resolver.setMasterSyncAutomatically(true);
        resolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
    }

    public Synchronization getSynchronization() {
        return synchronization;
    }

    public void setSynchronization(Synchronization synchronization) {
        this.synchronization = synchronization;
    }
}
