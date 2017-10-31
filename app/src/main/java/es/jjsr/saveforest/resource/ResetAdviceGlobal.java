package es.jjsr.saveforest.resource;

import android.app.Activity;

import es.jjsr.saveforest.dto.AdviceGlobal;

/**
 * Esta clase pondrá los valores por defecto a la Clase AdviceGlobal.
 * Se usará para inicializar el objeto o limpiar los valores.
 * Created by José Juan Sosa Rodríguez on 22/10/2017.
 */

public class ResetAdviceGlobal extends Activity {

    public ResetAdviceGlobal() {
        /*AdviceGlobal adviceGlobal = (AdviceGlobal) getApplication();
        adviceGlobal.setName("");
        adviceGlobal.setDescription("");
        adviceGlobal.setCountry("");
        adviceGlobal.setLatitude(0);
        adviceGlobal.setLongitude(0);
        adviceGlobal.setDate(null);*/
        ((AdviceGlobal) this.getApplication()).setName("");
        ((AdviceGlobal) this.getApplication()).setDescription("");
        ((AdviceGlobal) this.getApplication()).setIdCountry(0);
        ((AdviceGlobal) this.getApplication()).setLatitude(0);
        ((AdviceGlobal) this.getApplication()).setLongitude(0);
        ((AdviceGlobal) this.getApplication()).setDate(null);
        ((AdviceGlobal) this.getApplication()).setNameImage("");
    }
}
