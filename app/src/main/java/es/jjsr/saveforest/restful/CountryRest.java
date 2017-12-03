package es.jjsr.saveforest.restful;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import es.jjsr.saveforest.dto.Country;

/**
 * Clase que se encarga de Recibir el JSON desde el servicio web
 * Created by José Juan Sosa Rodríguez on 26/11/2017.
 */

public class CountryRest {
    private final static String route_all_countries = "http://jjsr.es:8080/WebASaveForest/webresources/countries/all-countries";

    public static ArrayList<Country> getAllCountries(){
        ArrayList<Country> countries = new ArrayList<>();

        try {
            URL url = new URL(route_all_countries);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String data;

            while ((data = bufferedReader.readLine()) != null){
                stringBuilder.append(data);
            }

            JSONArray jsonArray = new JSONArray(stringBuilder.toString());

            for (int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                countries.add(new Country(jsonObject.getInt("idCountry"),
                                          jsonObject.getString("nameCountry"),
                                          jsonObject.getString("codeCountry")));
            }

            return countries;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
