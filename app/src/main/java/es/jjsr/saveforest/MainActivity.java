package es.jjsr.saveforest;


import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import es.jjsr.saveforest.resource.ResetAdviceGlobal;
import es.jjsr.saveforest.resource.UpdateCountryTable;

/**
 * Aplicación creada por José Juan Sosa Rodríguez
 * Esta es la actividad que muestra la pantalla de inicio y luego carga la actividad principal.
 * También pone los valores por defecto de la Clase AdviceGlobal.
 */

public class MainActivity extends AppCompatActivity {


    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ResetAdviceGlobal(this);
        new UpdateCountryTable();

        mHandler = new Handler();
        try {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }catch (Exception ex){
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.fail_message), Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
