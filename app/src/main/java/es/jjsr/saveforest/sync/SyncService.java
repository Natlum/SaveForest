package es.jjsr.saveforest.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Define un servicio que devuelve un IBinder de la clase syncAdapter
 * Created by José Juan Sosa Rodríguez on 02/12/2017.
 */

public class SyncService extends Service{

    private static SyncAdapter sSyncAdapter = null;
    private static final Object sSyncAdapterLock = new Object();

    //Inicializamos el sync adapter object
    @Override
    public void onCreate(){
        //Creamos un sync adapter como singleton.
        synchronized (sSyncAdapterLock){
            if (sSyncAdapter == null){
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent){
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
