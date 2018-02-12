package es.jjsr.saveforest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.InputStream;

import es.jjsr.saveforest.resource.DownloadManualAsyncTask;
import es.jjsr.saveforest.resource.SettingActivity;


/**
 * Esta es la actividad principal, contiene el menú lateral.
 * Y el inicio para insertar un nuevo aviso.
 * @author José Juan Sosa Rodríguez
 */

public class StartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Activity context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        formPreparation();
    }

    private void formPreparation(){
        context = this;
        final EditText textName = (EditText) findViewById(R.id.textName);
        final CheckBox callme = (CheckBox) findViewById(R.id.checkBoxCallMe);
        Button next = (Button) findViewById(R.id.next);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate(textName)){
                    if(callme.isChecked()){
                        Intent startFromSetpB = new Intent(context, FormStepBAvtivity.class);
                        startFromSetpB.putExtra("name", textName.getText().toString());
                        startActivity(startFromSetpB);
                    }else{
                        Intent startFromStepA = new Intent(context, FormStepAActivity.class);
                        startFromStepA.putExtra("name", textName.getText().toString());
                        startActivity(startFromStepA);
                    }
                }
            }
        });
    }

    private Boolean validate(EditText textName){
        Boolean value = true;
        textName.setError(null);

        if(TextUtils.isEmpty(textName.getText().toString())){
            value = false;
            textName.setError(getString(R.string.error_required_field));
            textName.requestFocus();
        }

        return value;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            Intent intent = new Intent(context, HelpActivity.class);
            intent.putExtra("help_activity", "start");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_newAdvice) {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            this.finish();
        } else if (id == R.id.nav_historyAdvice) {
            Intent intent = new Intent(this, HistoryAdviceActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_aboutUs) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_download){
            downloadManual();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void downloadManual(){
        String urlToDownload = "https://jjsr.es/wp-content/uploads/2017/07/cv_jjsr.pdf";
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage(getResources().getString(R.string.message_downloading));
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        new DownloadManualAsyncTask(getApplicationContext(), progressDialog).execute(urlToDownload);
    }
}
