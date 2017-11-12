package es.jjsr.saveforest.fragments.StepA;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import es.jjsr.saveforest.R;

import static android.app.Activity.RESULT_OK;

/**
 * Contiene lo necesario para manejar el paso 1 en el formulario de nuevo aviso.
 * A simple {@link Fragment} subclass.
 */
public class Step1Fragment extends Fragment {


    private EditText description;
    private ImageView viewPhoto;
    private final int PETITION_TAKE_A_PICTURE = 1;
    Bitmap photo = null;

    public EditText getDescription() {
        return description;
    }

    public void setDescription(EditText description) {
        this.description = description;
    }

    public ImageView getViewPhoto() {
        return viewPhoto;
    }

    public void setViewPhoto(ImageView viewPhoto) {
        this.viewPhoto = viewPhoto;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
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
        viewPhoto = (ImageView) v.findViewById(R.id.imageFromCamera);
        Button cameraButton = (Button) v.findViewById(R.id.buttonCamera);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeAPicture();
            }
        });
    }

    private void takeAPicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PETITION_TAKE_A_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PETITION_TAKE_A_PICTURE:
                if (resultCode == RESULT_OK){
                    photo = (Bitmap) data.getExtras().get("data");
                    viewPhoto.setImageBitmap(photo);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
