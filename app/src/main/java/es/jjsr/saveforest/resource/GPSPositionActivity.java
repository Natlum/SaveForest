package es.jjsr.saveforest.resource;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class GPSPositionActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private double latitude = 0;
    private double longitude = 0;
    private final int GPSPETITION = 10;
    private GoogleApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void updateUI(Location loc) {
        if (loc != null) {
            latitude = loc.getLatitude();
            longitude = loc.getLongitude();
            Intent data = new Intent();
            data.putExtra("latitude", latitude);
            data.putExtra("longitude", longitude);
            setResult(RESULT_OK, data);
            finish();
        }else {
            Log.e("GPS","El objeto lastLocation es null. No se ha obtenido la ubicación");
            finish();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("GPS", "Error grave al conectar con Google Play Services");
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    GPSPETITION);
        }else {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            updateUI(lastLocation);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (apiClient != null) {
            apiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (apiClient != null) {
            apiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("GPS","Se ha interrumpido la conexión con Google Play Services");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == GPSPETITION){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                @SuppressWarnings("MissingPermission")
                Location lasLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
                updateUI(lasLocation);
            }else {
                Log.e("GPS","Permiso denegado");
            }
        }
    }
}
