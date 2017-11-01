package es.jjsr.saveforest;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.jjsr.saveforest.adapter.AdapterForRecyclerView;
import es.jjsr.saveforest.contentProviderPackage.Contract;
import es.jjsr.saveforest.dto.Advice;

public class HistoryAdviceActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView mRecyclerView;
    private AdapterForRecyclerView mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private List<Advice> advices;

    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_advice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_items);
        mRecyclerView.setHasFixedSize(true);

        mlayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mlayoutManager);

        mCallbacks = this;
        getLoaderManager().initLoader(0, null, mCallbacks);
        //initializeData();

        /*mAdapter = new AdapterForRecyclerView(advices);
        mAdapter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "Se ha pulsado la tarjeta: " +
                        mRecyclerView.getChildAdapterPosition(v), Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);*/

    }

    /*private void initializeData(){
        advices = new ArrayList<>();
        advices.add(new Advice("Aviso 1"));
        advices.add(new Advice("Aviso 2"));
        advices.add(new Advice("Aviso 3"));
    }*/

    private void initializeAdapter(){
        mAdapter = new AdapterForRecyclerView(advices);
        mAdapter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "Se ha pulsado la tarjeta: " +
                        mRecyclerView.getChildAdapterPosition(v), Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
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
            intent.putExtra("help_activity", "history");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String columns[] = new String[] {Contract.Advice.ID_ADVICE,
                                         Contract.Advice.DESCRIPTION,
                                         Contract.Advice.DATE,
                                         Contract.Advice.LATITUDE,
                                         Contract.Advice.LONGITUDE,
                                         Contract.Advice.ID_COUNTRY,
                                         Contract.Advice.NAME_IMAGE};

        Uri baseUri = Contract.Advice.CONTENT_URI_ADVICE;

        String selection = null;
        String sortOrder = Contract.Advice.ID_ADVICE + " DESC";

        return new CursorLoader(this, baseUri, columns, selection, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Uri laUriBase = Contract.Advice.CONTENT_URI_ADVICE;
        data.setNotificationUri(this.getContentResolver(), laUriBase);

        if (data != null){
            advices = new ArrayList<>();
            //arrayNameCountries = new String[data.getCount()];

            data.moveToFirst();

            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");

            do{
                Advice advice = new Advice();
                advice.setId(Integer.parseInt(data.getString(data.getColumnIndex(Contract.Advice.ID_ADVICE))));
                advice.setDescription(data.getString(data.getColumnIndex(Contract.Advice.DESCRIPTION)));
                try {
                    advice.setDate(originalFormat.parse(data.getString(data.getColumnIndex(Contract.Advice.DATE))));
                } catch (ParseException e) {
                    advice.setDate(new Date(1984, 10, 03));
                    Log.e("Fecha ERROR", "Error al obtener la fecha de la base de datos");
                }
                advice.setLatitude(data.getDouble(data.getColumnIndex(Contract.Advice.LATITUDE)));
                advice.setLongitude(data.getDouble(data.getColumnIndex(Contract.Advice.LONGITUDE)));
                advice.setIdCountry(data.getInt(data.getColumnIndex(Contract.Advice.ID_COUNTRY)));
                advice.setNameImage(data.getString(data.getColumnIndex(Contract.Advice.NAME_IMAGE)));
                advices.add(advice);
            }while (data.moveToNext());

            initializeAdapter();

            Toast.makeText(this, "Datos cargados de la Base de datos", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, getString(R.string.fail_load_countries), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
