package es.jjsr.saveforest.resource;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import es.jjsr.saveforest.R;
import es.jjsr.saveforest.contentProviderPackage.CountryProvider;
import es.jjsr.saveforest.dto.Country;
import es.jjsr.saveforest.restful.CountryRest;

/**
 * Clase se que sencarga de decidir si actualiza la tabla de paises o no.
 * Created by José Juan Sosa Rodríguez on 26/11/2017.
 */

public class UpdateCountryTable {

    private ContentResolver solve;
    private Activity activity;

    public UpdateCountryTable(ContentResolver solve, Activity activity) {
        this.solve = solve;
        this.activity = activity;
        new DownloadTableData().execute();
    }

    class DownloadTableData extends AsyncTask<Void, Void,ArrayList<Country>>{

        @Override
        protected ArrayList<Country> doInBackground(Void... voids) {
            ArrayList<Country> countries = CountryRest.getAllCountries();
            return countries;
        }

        @Override
        protected void onPostExecute(ArrayList<Country> countries) {
            super.onPostExecute(countries);
            if (countries == null){
                Log.e("Update Country", "Recibir los datos");
                return;
            }
            int contentProviderCountryRows = new CountryProvider().allRowsCountries(solve);
            if (contentProviderCountryRows != countries.size()){
                if (contentProviderCountryRows != 0) {
                    new CountryProvider().deleteAllRecord(solve);
                }
                for (Country c : countries){
                    new CountryProvider().insertRecord(solve, c);
                    Log.i("Update Country:", "Se está actualizando");
                }
                showMessage();
            }
        }

        private void showMessage(){
            LinearLayout layout = new LinearLayout(activity);
            layout.setBackgroundResource(R.color.colorPrimaryLight);

            TextView textView = new TextView(activity);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(15);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setText(R.string.country_table_updated);
            textView.setPadding(15,15,15,15);

            layout.addView(textView);

            Toast toast = new Toast(activity);
            toast.setView(layout);
            toast.setGravity(Gravity.BOTTOM, 0, 50);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
    }


}
