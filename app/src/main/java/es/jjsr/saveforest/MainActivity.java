package es.jjsr.saveforest;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.util.Locale;

import es.jjsr.saveforest.resource.ResetAdviceGlobal;
import es.jjsr.saveforest.resource.UpdateCountryTable;

/**
 * Aplicación creada por José Juan Sosa Rodríguez
 * Esta es la actividad que muestra la pantalla de inicio y luego carga la actividad principal.
 * También pone los valores por defecto de la Clase AdviceGlobal.
 */

public class MainActivity extends AppCompatActivity {


    Handler mHandler;
    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        new ResetAdviceGlobal(this);
        new UpdateCountryTable(getContentResolver(), this);

        mHandler = new Handler();
        try {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        String lanSettings = settings.getString("pref_language", "");
                        setLocale(lanSettings);
                    }catch (Exception e){
                        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                        startActivity(intent);
                    }finally {
                        finish();
                    }
                }
            }, 3000);
        }catch (Exception ex){
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.fail_message), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    //* manually changing current locale/
    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}
