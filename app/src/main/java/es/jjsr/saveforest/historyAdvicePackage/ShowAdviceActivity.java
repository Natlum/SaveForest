package es.jjsr.saveforest.historyAdvicePackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;

import es.jjsr.saveforest.R;
import es.jjsr.saveforest.dto.Advice;

/**
 * Contiene lo necesario para mostrar el contenido de un aviso seleccionado.
 * Si el aviso no tiene imagen, ocultará esa opción.
 * Si el aviso no tiene ubicación GPS, ocultará esa opticón.
 */

public class ShowAdviceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Advice advice;
    private GoogleMap map;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_advice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        advice = (Advice) intent.getSerializableExtra("adviceObject");

        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_show_advice) +" "+ advice.getId());

        initBasicElements();
        initMaps();
    }

    private void initMaps() {
        if (advice.getLatitude() != -1 && advice.getLongitude() != -1){
            mapStart();
            //gpsPositionMaps(advice.getLatitude(), advice.getLongitude());
        }else {
            LinearLayout layout = (LinearLayout) findViewById(R.id.layoutShowMaps);
            layout.setVisibility(View.GONE);
        }
    }

    private void initBasicElements() {
        TextView textDate =(TextView) findViewById(R.id.textDate);
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formatedDate = newFormat.format(advice.getDate());
        textDate.setText(formatedDate);

        TextView textDescription = (TextView) findViewById(R.id.textShowDescription);
        textDescription.setText(advice.getDescription());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);

        gpsPositionMaps(advice.getLatitude(), advice.getLongitude());
    }

    private void mapStart(){
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void gpsPositionMaps(double latitude, double longitude){
        LatLng gpsPosition = new LatLng(latitude,longitude);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(gpsPosition)  //Centramos el mapa en la posición gps
                .zoom(19)            //Establecemos el zoom
                .bearing(45)        //Establecemos la orientación con el noreste arriba
                .tilt(0)           //Bajamos el punto de vista de la cámara en grados
                .build();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(cameraUpdate);
        insertMark(latitude, longitude);
    }

    private void insertMark(double latitude, double longitude){
        map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(getString(R.string.mark_show_advice)+ latitude + ", " + longitude +")"));
    }

}
