package es.jjsr.saveforest.dto;

import android.app.Application;

import java.util.Date;

/**
 * Esta es la class que se usará de manera global en la aplicación,
 * para ir guardando los datos antes de enviarlos a la base de datos.
 * Created by José Juan Sosa Rodríguez on 22/10/2017.
 */

public class AdviceGlobal extends Application{

    private String name;
    private String description;
    private String country;
    private double latitude;
    private double longitude;
    private Date date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
