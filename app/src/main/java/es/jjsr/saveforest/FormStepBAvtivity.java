package es.jjsr.saveforest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

import es.jjsr.saveforest.dto.AdviceGlobal;
import es.jjsr.saveforest.fragments.StepB.StepBFragment;
import es.jjsr.saveforest.fragments.StepB.ValidateContentStepB;
import es.jjsr.saveforest.resource.InsertAdvice;

/**
 * Esta actividad es para cuando el usuario, ha elegido que lo llamen a la hora de hacer un aviso nuevo.
 * @author José Juan Sosa Rodríguez
 */

public class FormStepBAvtivity extends AppCompatActivity{

    private StepBFragment stepBFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_step_b);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        stepBFragment = new StepBFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_advice_stepB, stepBFragment);
        transaction.commit();

        getIntentReceived();
        initElements();

    }

    private void initElements(){
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingButtonSendStepB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateContentStepB validateContentStepB = new ValidateContentStepB(getApplicationContext(), stepBFragment);
                if (validateContentStepB.getEndValue()){
                    saveContents();
                }
            }
        });
    }

    private void getIntentReceived(){
        Intent intent = this.getIntent();
        final String name = intent.getExtras().getString("name");
        AdviceGlobal adviceGlobal = (AdviceGlobal) getApplication();
        adviceGlobal.setName(name);
    }

    private void saveContents(){
        ((AdviceGlobal) getApplication()).setDescription(String.valueOf(stepBFragment.getDescription().getText()));
        ((AdviceGlobal) getApplication()).setIdCountry(stepBFragment.getIdCountry());
        ((AdviceGlobal) getApplication()).setPhoneNumber(Integer.valueOf(String.valueOf(stepBFragment.getPhoneNumber().getText())));
        ((AdviceGlobal) getApplication()).setDate(new Date());
        new InsertAdvice(this, getContentResolver());
        finish();
        //Toast.makeText(this, "Se ha guardado el aviso ", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_help){
            Intent intent = new Intent(this, HelpActivity.class);
            intent.putExtra("help_activity", "stepB");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
