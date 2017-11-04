package es.jjsr.saveforest.fragments.StepA;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import es.jjsr.saveforest.R;
import es.jjsr.saveforest.dto.AdviceGlobal;

/**
 * Contiene lo necesario para manejar el paso 1 en el formulario de nuevo aviso.
 * A simple {@link Fragment} subclass.
 */
public class Step1Fragment extends Fragment {


    private EditText description;
    private ImageView photo;

    public EditText getDescription() {
        return description;
    }

    public void setDescription(EditText description) {
        this.description = description;
    }

    public ImageView getPhoto() {
        return photo;
    }

    public void setPhoto(ImageView photo) {
        this.photo = photo;
    }

    public Step1Fragment() {
        // Required empty public constructor
    }

    /**
     * Returns a new instance of this fragment.
     */
    public static Step1Fragment newInstance() {
        Step1Fragment fragment = new Step1Fragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_step1, container, false);

        initElements(v);

        return v;
    }

    private void initElements(View v){
        description = (EditText) v.findViewById(R.id.editTextDescription);
    }
}
