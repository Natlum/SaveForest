package es.jjsr.saveforest.fragments.StepB;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import es.jjsr.saveforest.R;

/**
 * Clase para validar que se han introducido correctamente los datos antes de ser guardados y enviados
 * a la base de datos.
 * Created by José Juan Sosa Rodríguez on 04/11/2017.
 */

public class ValidateContentStepB {

    private Context ctx;
    private Boolean endValue = false;
    private StepBFragment stepBFragment;

    public ValidateContentStepB(Context ctx, StepBFragment stepBFragment) {
        this.ctx = ctx;
        this.stepBFragment = stepBFragment;

        if(validateDescription(stepBFragment.getDescription())){
            if(validatePhoneNumber(stepBFragment.getPhoneNumber())){
                endValue = true;
            }else {
                endValue = false;
                Toast.makeText(ctx, ctx.getString(R.string.validate_location), Toast.LENGTH_LONG).show();
            }
        }else {
            endValue = false;
        }

    }

    public Boolean getEndValue() {
        return endValue;
    }

    private Boolean validateDescription(EditText editText){
        Boolean value = true;
        editText.setError(null);

        if(TextUtils.isEmpty(editText.getText().toString())){
            value = false;
            editText.setError(ctx.getString(R.string.error_required_field));
            editText.requestFocus();
        }

        return value;
    }

    private Boolean validatePhoneNumber(EditText editText){
        Boolean value = true;
        editText.setError(null);

        if(TextUtils.isEmpty(editText.getText().toString())){
            value = false;
            editText.setError(ctx.getString(R.string.error_required_field));
            editText.requestFocus();
        }

        String regxStr = "^[0-9]{9}$";
        String number = editText.getText().toString();

        if (!number.matches(regxStr)){
            value = false;
            editText.setError(ctx.getString(R.string.error_required_field));
            editText.requestFocus();
        }

        return value;
    }
}
