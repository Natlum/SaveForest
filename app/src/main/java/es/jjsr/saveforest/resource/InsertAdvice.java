package es.jjsr.saveforest.resource;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.widget.Toast;

import es.jjsr.saveforest.FinishAdviceActivity;
import es.jjsr.saveforest.R;
import es.jjsr.saveforest.contentProviderPackage.AdviceProvider;

/**
 * Created by José Juan Sosa Rodríguez on 01/11/2017.
 */

public class InsertAdvice {

    public InsertAdvice(Activity activity, ContentResolver solve) {
        try {
            AdviceProvider.insertRecordWithBinnacle(activity, solve);
            Intent intent = new Intent(activity, FinishAdviceActivity.class);
            activity.startActivity(intent);
            Toast.makeText(activity, activity.getString(R.string.congrat_save_advice), Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(activity, activity.getString(R.string.fail_save_advice), Toast.LENGTH_LONG).show();
        }

    }
}
