package es.jjsr.saveforest.dto;

import java.io.Serializable;

/**
 * Created by José Juan Sosa Rodríguez on 26/11/2017.
 */

public class Country implements Serializable {

    private int id_country;
    private String name_country;
    private String code_country;

    public Country() {
    }

    public Country(int id_country, String name_country, String code_country) {
        this.id_country = id_country;
        this.name_country = name_country;
        this.code_country = code_country;
    }

    public int getId_country() {
        return id_country;
    }

    public void setId_country(int id_country) {
        this.id_country = id_country;
    }

    public String getName_country() {
        return name_country;
    }

    public void setName_country(String name_country) {
        this.name_country = name_country;
    }

    public String getCode_country() {
        return code_country;
    }

    public void setCode_country(String code_country) {
        this.code_country = code_country;
    }
}
