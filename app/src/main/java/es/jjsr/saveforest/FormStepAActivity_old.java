package es.jjsr.saveforest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Esta actividad es para que el usuario rellene un formulario con los datos del aviso
 * @author José Juan Sosa Rodríguez
 */

public class FormStepAActivity_old extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_step_a_old);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getIntentReceived();
        formPreparation();

    }

    private void getIntentReceived(){
        Intent intent = this.getIntent();
        final String name = intent.getExtras().getString("name");
        final TextView textName = (TextView) findViewById(R.id.textViewName);
        textName.setText(name);
    }

    private void formPreparation(){
        final View parentLayout = findViewById(android.R.id.content);
        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.radioButtonMan:
                        Snackbar.make(parentLayout, getString(R.string.message_form_man), Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        break;
                    case R.id.radioButtonWoman:
                        Snackbar.make(parentLayout, getString(R.string.message_form_woman), Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        break;
                }
            }
        });
    }

}
