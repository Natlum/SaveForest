package es.jjsr.saveforest;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Intent intent = this.getIntent();
        final String option = intent.getExtras().getString("help_activity");
        final TextView textHelp = (TextView) findViewById(R.id.textHelp);

        Resources res = this.getResources();
        String [] options = res.getStringArray(R.array.help);

        if(option.equals("start")){
            textHelp.setText(options[0]);
        }else if (option.equals("stepA")){
            textHelp.setText(options[1]);
        }else if (option.equals("stepB")){
            textHelp.setText(options[2]);
        }else if (option.equals("history")){
            textHelp.setText(options[3]);
        }
    }
}
