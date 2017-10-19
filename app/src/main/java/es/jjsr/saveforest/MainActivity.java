package es.jjsr.saveforest;


import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Aplicación creada por José Juan Sosa Rodríguez
 * Esta es la actividad que muestra la pantalla de inicio y luego carga la actividad principal.
 */

public class MainActivity extends AppCompatActivity {


    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
