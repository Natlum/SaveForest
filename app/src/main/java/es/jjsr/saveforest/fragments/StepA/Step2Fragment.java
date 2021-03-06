package es.jjsr.saveforest.fragments.StepA;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import es.jjsr.saveforest.R;
import es.jjsr.saveforest.contentProviderPackage.Contract;
import es.jjsr.saveforest.resource.GPSService;

/**
 * Contiene lo necesario para manejar el paso 2 en el formulario de nuevo aviso.
 * A simple {@link Fragment} subclass.
 */
public class Step2Fragment extends Fragment implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor>{


    private Spinner spinner;
    private int espanaPosition = 0;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private double latitude = 0;
    private double longitude = 0;
    private int idCountry = 0;
    CheckBox checkBoxGPS;
    private Marker marker;

    private String [] arrayNameCountries;
    int [] arrayIdCountries;
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

    private Receiver receiver;

    private View v;

    private static final int PETITION_PERMISSION_LOCATION = 101;

    public Step2Fragment() {
        // Required empty public constructor
    }

    /**
     * Returns a new instance of this fragment.
     */
    public static Step2Fragment newInstance() {
        Step2Fragment fragment = new Step2Fragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_step2, container, false);

        //spinnerStart(v);
        mapStart();
        initElements(v);

        IntentFilter filter = new IntentFilter();
        filter.addAction(GPSService.ACTION);
        receiver = new Receiver();
        getActivity().registerReceiver(receiver, filter);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCallbacks = this;
        getLoaderManager().initLoader(0, null, mCallbacks);
    }

    private void mapStart(){
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void spinnerStart(){
        spinner = v.findViewById(R.id.SpinerCountry);
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

    private void initElements(View v){
        ejecuteService();

        checkBoxGPS = v.findViewById(R.id.checkBoxGPS);
        checkBoxGPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBoxGPS.isChecked()){
                    if (isMyServiceRunning(GPSService.class)){
                        if (latitude != 0 || longitude != 0){
                            gpsPositionMaps(latitude, longitude);
                            getActivity().stopService(new Intent(getActivity(), GPSService.class));
                        }else {
                            setDefaultMaps();
                        }
                    }else {
                        ejecuteService();
                        setDefaultMaps();
                    }
                }
            }
        });
    }

    private void setDefaultMaps(){
        gpsPositionMaps(28.128092, -15.446435);
        Toast.makeText(getContext(), getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
        checkBoxGPS.setChecked(false);
    }

    private void gpsPositionMaps(double latitude, double longitude){
        LatLng gpsPosition = new LatLng(latitude,longitude);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(gpsPosition)  //Centramos el mapa en la posición gps
                .zoom(19)            //Establecemos el zoom
                .bearing(45)        //Establecemos la orientación con el noreste arriba
                .tilt(0)           //Bajamos el punto de vista de la cámara en grados
                .build();

        //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(40.41, -3.69), 15);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(cameraUpdate);
        insertMark(latitude, longitude);
    }

    private void insertMark(double latitude, double longitude){
        try {
            marker.remove();
        }catch (Exception e){
            Log.e("RemoveMarker", "Fail to load marker");
        }
        marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(getString(R.string.mark)+ latitude + ", " + longitude +")"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getIdCountry() {
        return idCountry;
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
            Toast.makeText(getContext(), getString(R.string.fail_load_countries), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void ejecuteService(){
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETITION_PERMISSION_LOCATION);
        } else {
            getActivity().startService(new Intent(getActivity(), GPSService.class));
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(receiver);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GPSService.ACTION)){
                Log.e("onReceive","onReceive called");
                latitude = intent.getDoubleExtra("latitude", 0);
                longitude = intent.getDoubleExtra("longitude", 0);
                Log.e("receiver", "Got Latitude: " + latitude);
                Log.e("receiver", "Got Longitude: " + longitude);
                gpsPositionMaps(latitude, longitude);
            }
        }
    }

}
