package es.jjsr.saveforest.dto;

import java.util.Date;

/**
 * Created by José Juan Sosa Rodríguez on 12/10/2017.
 */

public class Advice {

    //private Date dateAdvice;
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
