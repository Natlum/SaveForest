package es.jjsr.saveforest.resource;

import android.app.Activity;

import es.jjsr.saveforest.dto.AdviceGlobal;

/**
 * Esta clase pondrá los valores por defecto a la Clase AdviceGlobal.
 * Se usará para inicializar el objeto o limpiar los valores.
 * Created by José Juan Sosa Rodríguez on 22/10/2017.
 */

public class ResetAdviceGlobal {

    public ResetAdviceGlobal(Activity activity) {
        AdviceGlobal adviceGlobal = (AdviceGlobal) activity.getApplication();
        adviceGlobal.setName(null);
        adviceGlobal.setDescription(null);
        adviceGlobal.setIdCountry(-1);
        adviceGlobal.setLatitude(-1);
        adviceGlobal.setLongitude(-1);
        adviceGlobal.setDate(null);
        adviceGlobal.setNameImage(null);
        adviceGlobal.setPhoneNumber(0);
    }
}
