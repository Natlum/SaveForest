package es.jjsr.saveforest.fragments.StepA;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import es.jjsr.saveforest.R;

/**
 * Clase para validar que se han introducido correctamente los datos antes de ser guardados y enviados
 * a la base de datos.
 * Created by José Juan Sosa Rodríguez on 22/10/2017.
 */

public class ValidateContentStepA {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private Context ctx;
    private Boolean endValue = false;

    public Boolean getEndValue() {
        return endValue;
    }

    public ValidateContentStepA(SectionsPagerAdapter sectionsPagerAdapter, Context context) {
        this.sectionsPagerAdapter = sectionsPagerAdapter;
        this.ctx = context;
        validate();
    }

    private void validate(){
        if(validateDescription(sectionsPagerAdapter.getStep1().getDescription())){
            if(validateGPS(sectionsPagerAdapter.getStep2().getLatitude(),
                    sectionsPagerAdapter.getStep2().getLongitude())){
                endValue = true;
            }else {
                endValue = false;
                Toast.makeText(ctx, ctx.getString(R.string.validate_location), Toast.LENGTH_LONG).show();
            }
        }else {
            endValue = false;
        }
    }

    private Boolean validateDescription(EditText textName){
        Boolean value = true;
        textName.setError(null);

        if(TextUtils.isEmpty(textName.getText().toString())){
            value = false;
            textName.setError(ctx.getString(R.string.error_required_field));
            textName.requestFocus();
        }

        return value;
    }

    private Boolean validateGPS (double latitude, double longitude){
        Boolean value = false;
        if (latitude != 0 && longitude != 0){
            value = true;
        }
        return value;
    }
}
