package es.jjsr.saveforest.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Esta clase es para usarla en un listado de objetos con valores obtenidos de la base de datos
 * Implementa Serializable para pasar objetos de este tipo de clase a otras actividades.
 * Created by José Juan Sosa Rodríguez on 12/10/2017.
 */

public class Advice implements Serializable{

    private String text;

    private int id;
    private String name;
    private String description;
    private int idCountry;
    private double latitude;
    private double longitude;
    private Date date;
    private String nameImage;
    private int phoneNumber;
    private String countryCode;

    public Advice() {
    }

    public Advice(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(int idCountry) {
        this.idCountry = idCountry;
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

    public String getNameImage() {
        return nameImage;
    }

    public void setNameImage(String nameImage) {
        this.nameImage = nameImage;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
