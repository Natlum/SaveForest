package es.jjsr.saveforest.fragments.StepA;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.maps.model.MarkerOptions;

import es.jjsr.saveforest.R;
import es.jjsr.saveforest.resource.GPSPositionActivity;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Step2Fragment extends Fragment implements OnMapReadyCallback {


    private Spinner spinner;
    private final int ESPANA = 2;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private double latitude = 0;
    private double longitude = 0;
    private String country = "";
    int request_code = 1;
    CheckBox checkBoxGPS;

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
        View v = inflater.inflate(R.layout.fragment_step2, container, false);

        spinnerStart(v);
        mapStart();
        gpsStart(v);

        return v;
    }

    private void mapStart(){
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void spinnerStart(View v){
        spinner = v.findViewById(R.id.SpinerCountry);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.countries_array_name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(ESPANA);
        country = spinner.getSelectedItem().toString();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getContext(), "Se ha elegido el: " + i, Toast.LENGTH_SHORT).show();
                country = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void gpsStart(View v){
        checkBoxGPS = v.findViewById(R.id.checkBoxGPS);
        checkBoxGPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBoxGPS.isChecked()){
                    Intent intent = new Intent(getContext(), GPSPositionActivity.class);
                    startActivityForResult(intent, request_code);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requestCode && resultCode == RESULT_OK){
            latitude = data.getExtras().getDouble("latitude");
            longitude = data.getExtras().getDouble("longitude");
        }
        if (latitude != 0 || longitude != 0){
            gpsPositionMaps(latitude, longitude);
        }else {
            gpsPositionMaps(28.128092, -15.446435);
            Toast.makeText(getContext(), getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
            checkBoxGPS.setChecked(false);
        }
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
        map.addMarker(new MarkerOptions()
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

    public String getCountry() {
        return country;
    }
}
