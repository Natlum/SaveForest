package es.jjsr.saveforest.historyAdvicePackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import es.jjsr.saveforest.R;
import es.jjsr.saveforest.contentProviderPackage.AdviceProvider;
import es.jjsr.saveforest.dto.Advice;

/**
 * Actividad que permitirá modificar la descripción del aviso y guardarla.
 */

public class UpdateAdviceActivity extends AppCompatActivity {

    private TextView idTextView;
    private EditText editTextDescription;
    private Advice advice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_advice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        advice = (Advice) intent.getSerializableExtra("adviceObject");

        initializeElements();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabUpdate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            saveUpdateRecord();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeElements() {
        idTextView = findViewById(R.id.textViewIdAdvice);
        editTextDescription = findViewById(R.id.updateTextDescription);

        idTextView.setText(idTextView.getText() + String.valueOf(advice.getId()));
        editTextDescription.setText(advice.getDescription());
    }

    private void saveUpdateRecord(){
        editTextDescription.setError(null);
        String newDescription = String.valueOf(editTextDescription.getText());

        if (TextUtils.isEmpty(newDescription)){
            editTextDescription.setError(getString(R.string.error_required_field));
            editTextDescription.requestFocus();
            return;
        }
        advice.setDescription(newDescription);
        AdviceProvider.updateRecord(getContentResolver(), advice);
        finish();
    }

}
