package es.jjsr.saveforest.dto;

import es.jjsr.saveforest.resource.constants.GConstants;

/**
 * Clase Bitácoras para saber que es lo que se ha sincronizado con el servicio web
 * Created by José Juan Sosa Rodríguez on 01/12/2017.
 */

public class Binnacle {
    private int id;
    private int id_advice;
    private int operation;
    private String image_name;

    public Binnacle() {
        this.id = GConstants.SIN_VALOR_INT;
        this.id_advice = GConstants.SIN_VALOR_INT;
        this.operation = GConstants.SIN_VALOR_INT;
        this.image_name = null;
    }

    public Binnacle(int id, int id_advice, int operation, String image_name) {
        this.id = id;
        this.id_advice = id_advice;
        this.operation = operation;
        this.image_name = image_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_advice() {
        return id_advice;
    }

    public void setId_advice(int id_advice) {
        this.id_advice = id_advice;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
