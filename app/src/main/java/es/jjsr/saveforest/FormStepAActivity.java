package es.jjsr.saveforest;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import es.jjsr.saveforest.dto.AdviceGlobal;
import es.jjsr.saveforest.fragments.StepA.SectionsPagerAdapter;
import es.jjsr.saveforest.fragments.StepA.ValidateContent;

public class FormStepAActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_step_a);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.setContext(this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        initAdvice(getIntentReceived());
        initElements();
    }

    private String getIntentReceived(){
        Intent intent = this.getIntent();
        final String name = intent.getExtras().getString("name");
        return name;
    }

    private void initAdvice(String name){
        AdviceGlobal adviceGlobal = (AdviceGlobal) getApplication();
        adviceGlobal.setName(name);
    }

    private void initElements(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingButtonSendStepA);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateContent validateContent = new ValidateContent(mSectionsPagerAdapter, getApplicationContext());
                if (validateContent.getEndValue()){
                    saveContents();
                }
            }
        });
    }

    private void saveContents(){
        ((AdviceGlobal) getApplication()).setDescription(String.valueOf(mSectionsPagerAdapter.getStep1()
                .getDescription().getText()));
        ((AdviceGlobal) getApplication()).setIdCountry(mSectionsPagerAdapter.getStep2().getIdCountry());
        ((AdviceGlobal) getApplication()).setLatitude(mSectionsPagerAdapter.getStep2().getLatitude());
        ((AdviceGlobal) getApplication()).setLongitude(mSectionsPagerAdapter.getStep2().getLongitude());
        ((AdviceGlobal) getApplication()).setNameImage(null);
        Toast.makeText(this, "Se ha guardado el aviso ", Toast.LENGTH_LONG).show();
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
            intent.putExtra("help_activity", "stepA");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
