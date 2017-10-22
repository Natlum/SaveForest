package es.jjsr.saveforest.dto;



/**
 * Esta clase es para usarla en un listado de objetos con valores obtenidos de la base de datos
 * Created by José Juan Sosa Rodríguez on 12/10/2017.
 */

public class Advice {

    private String text;

    public Advice(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
