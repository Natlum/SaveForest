package es.jjsr.saveforest.fragments.StepA;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.jjsr.saveforest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Step2Fragment extends Fragment {


    public Step2Fragment() {
        // Required empty public constructor
    }

    /**
     * Returns a new instance of this fragment.
     */
    public static Step2Fragment newInstance() {
        Step2Fragment fragment = new Step2Fragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step2, container, false);
    }

}
