package es.jjsr.saveforest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import es.jjsr.saveforest.dto.AdviceGlobal;
import es.jjsr.saveforest.resource.ResetAdviceGlobal;
import es.jjsr.saveforest.sync.Synchronization;

/**
 * Esta actividad se muestra cuando se ha guardado el aviso en la base de datos.
 * Al final se ponen los valores por defecto en la clase AdviceGlobal para así volverla a usar.
 */
public class FinishAdviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_advice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView = (TextView) findViewById(R.id.textNameFinish);
        textView.setText(((AdviceGlobal) getApplication()).getName());
        TextView call = (TextView) findViewById(R.id.txtViewCall);
        if (((AdviceGlobal) getApplication()).getPhoneNumber()!= 0){
            call.setText(getString(R.string.message));
        }

        new ResetAdviceGlobal(this);

        AdviceGlobal.getmInstance().setSynchronization(new Synchronization(getApplicationContext()));
        AdviceGlobal.getmInstance().getSynchronization().syncUp();
    }

}
