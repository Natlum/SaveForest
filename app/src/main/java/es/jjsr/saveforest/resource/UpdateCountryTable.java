package es.jjsr.saveforest.resource;

import android.os.AsyncTask;

import java.util.ArrayList;

import es.jjsr.saveforest.dto.Country;
import es.jjsr.saveforest.restful.CountryRest;

/**
 * Created by José Juan Sosa Rodríguez on 26/11/2017.
 */

public class UpdateCountryTable {

    public UpdateCountryTable() {
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
        }
    }
}
