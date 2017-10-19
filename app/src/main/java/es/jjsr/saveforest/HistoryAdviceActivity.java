package es.jjsr.saveforest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.jjsr.saveforest.adapter.AdapterForRecyclerView;
import es.jjsr.saveforest.dto.Advice;

public class HistoryAdviceActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private AdapterForRecyclerView mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private List<Advice> advices;

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

        initializeData();

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

    private void initializeData(){
        advices = new ArrayList<>();
        advices.add(new Advice("Aviso 1"));
        advices.add(new Advice("Aviso 2"));
        advices.add(new Advice("Aviso 3"));
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
}
