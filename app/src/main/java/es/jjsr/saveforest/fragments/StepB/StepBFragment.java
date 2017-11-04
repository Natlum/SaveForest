package es.jjsr.saveforest.fragments.StepB;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import es.jjsr.saveforest.R;
import es.jjsr.saveforest.contentProviderPackage.Contract;

/**
 * Contiene lo necesario para manejar el formulario de nuevo aviso que requiere un número de teléfono.
 * A simple {@link Fragment} subclass.
 */
public class StepBFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private EditText phoneNumber;
    private EditText description;
    private int idCountry = 0;
    private int espanaPosition = 0;
    private Spinner spinner;
    private String [] arrayNameCountries;
    int [] arrayIdCountries;
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

    private View v;

    public StepBFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_step_b, container, false);
        initElements();
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCallbacks = this;
        getLoaderManager().initLoader(0, null, mCallbacks);
    }

    private void initElements(){
        description = v.findViewById(R.id.editTextDescriptionStepB);
        phoneNumber = v.findViewById(R.id.editTextPhoneNumber);
    }

    private void spinnerStart(){
        spinner = v.findViewById(R.id.SpinerCountryStepB);
        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.countries_array_name, android.R.layout.simple_spinner_item);*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayNameCountries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(espanaPosition);
        //idCountry = spinner.getSelectedItemPosition();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idCountry = arrayIdCountries[spinner.getSelectedItemPosition()];
                //Toast.makeText(getContext(), "Se ha elegido el: " + i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String columns[] = new String[] {Contract.Country.ID_COUNTRY, Contract.Country.NAME_COUNTRY};
        Uri baseUri = Contract.Country.CONTENT_URI_COUNTRY;

        String selection = null;
        String sortOrder = Contract.Country.NAME_COUNTRY + " COLLATE LOCALIZED ASC";

        return new CursorLoader(getActivity(), baseUri, columns, selection, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Uri laUriBase = Contract.Country.CONTENT_URI_COUNTRY;
        data.setNotificationUri(getActivity().getContentResolver(), laUriBase);

        if (data != null){
            arrayNameCountries = new String[data.getCount()];
            arrayIdCountries = new int[data.getCount()];

            data.moveToFirst();

            int nameColumnIndex = data.getColumnIndex(Contract.Country.ID_COUNTRY);
            int nameColumnIndexName = data.getColumnIndex(Contract.Country.NAME_COUNTRY);
            int i = 0;

            do{
                arrayNameCountries[i]=data.getString(nameColumnIndexName);
                arrayIdCountries[i]=data.getInt(nameColumnIndex);
                if (arrayNameCountries[i].equals("España")){
                    espanaPosition = i;
                    idCountry = arrayIdCountries[i];
                }
                i++;
            }while (data.moveToNext());

            spinnerStart();

            //Toast.makeText(getActivity(), "Datos cargados de la Base de datos", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getActivity(), getString(R.string.fail_load_countries), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public EditText getPhoneNumber() {
        return phoneNumber;
    }

    public EditText getDescription() {
        return description;
    }

    public int getIdCountry() {
        return idCountry;
    }
}
