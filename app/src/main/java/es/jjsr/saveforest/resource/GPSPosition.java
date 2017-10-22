package es.jjsr.saveforest.resource;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by José Juan Sosa Rodríguez on 21/10/2017.
 */

public class GPSPosition extends Activity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    private static final int PETICION_PERMISO_LOCALIZACION = 1984;
    private double latitude;
    private double longitude;
    private Context ctx;
    private GoogleApiClient apiClient;
    private Boolean permissions = false;

    public GPSPosition(Context context) {
        this.ctx = context;

        apiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Boolean getPermissions() {
        return permissions;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("GOOGLE PLAY SERVICES", "Error grave al conectar con Google Play Services");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(ctx,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        }else {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);

            if (lastLocation != null) {
                latitude = lastLocation.getLatitude();
                longitude = lastLocation.getLongitude();
                permissions = true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                @SuppressWarnings("MissingPermission")
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);

                if (lastLocation != null) {
                    latitude = lastLocation.getLatitude();
                    longitude = lastLocation.getLongitude();
                    this.permissions = true;
                }

            } else {
                Log.e("PERMISOS DE USUARIO", "Permiso denegado");
                this.permissions = false;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("GOOGLE PLAY SERVICES", "Se ha interrumpido la conexión con Google Play Services");
    }

    @Override
    protected void onStart(){
        super.onStart();
        apiClient.connect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        apiClient.disconnect();
    }
}