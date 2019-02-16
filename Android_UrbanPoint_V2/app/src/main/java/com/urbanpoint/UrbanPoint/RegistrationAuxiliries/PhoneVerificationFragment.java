package com.urbanpoint.UrbanPoint.RegistrationAuxiliries;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.urbanpoint.UrbanPoint.R;

public class PhoneVerificationFragment extends Fragment implements View.OnClickListener {


    public PhoneVerificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_verification, null);

        return view;
    }

    @Override
    public void onClick(View v) {

    }
}