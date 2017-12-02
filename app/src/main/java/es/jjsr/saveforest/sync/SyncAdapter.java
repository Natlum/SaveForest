package es.jjsr.saveforest.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import es.jjsr.saveforest.dto.AdviceGlobal;

/**
 * Handle que transfiere datos entre el servidor y la app, usando el Framework Android Sync Adapter
 * Created by José Juan Sosa Rodríguez on 02/12/2017.
 */

public class SyncAdapter  extends AbstractThreadedSyncAdapter{

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        AdviceGlobal.getmInstance().setSynchronization(new Synchronization(context));
    }

    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs){
        super(context, autoInitialize, allowParallelSyncs);

        AdviceGlobal.getmInstance().setSynchronization(new Synchronization(context));
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s,
                              ContentProviderClient contentProviderClient, SyncResult syncResult) {
        AdviceGlobal.getmInstance().getSynchronization().syncUp();
    }
}
