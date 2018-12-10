package com.urbanpoint.UrbanPoint.RegistrationAuxiliries;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.AppInstance;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.Utility;
//import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
//import com.urbanpoint.UrbanPoint.utils.Constants;
//import com.urbanpoint.UrbanPoint.utils.Utility;
//import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragmentStepTwo extends Fragment implements View.OnClickListener {


    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private Button mSignUpBackView;
    private Button mSignUpContinueView;
    private ImageView mFemaleImageView;
    private ImageView mMaleImageView;
    private int genderValue = 0;
    private Utility utilObj;
    CustomAlert customAlert;
    public SignUpFragmentStepTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_step_two, null);
        Log.e("name", AppConfig.mSignupUsername+"");
        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        initialize();
//        MyApplication.getInstance().trackScreenView(getResources().getString(R.string.get_started) + " " + getResources().getString(R.string.header_gender));
        return view;
    }
//
    private void initialize() {
        utilObj = new Utility(mActivity);

        bindViews();
    }
//
    private void bindViews() {
        mSignUpBackView = (Button) mRootView.findViewById(R.id.signUpStepTwoBackButton);
        mSignUpBackView.setOnClickListener(this);

        mSignUpContinueView = (Button) mRootView.findViewById(R.id.signUpStepTwoContinueButton);
        mSignUpContinueView.setOnClickListener(this);

        mFemaleImageView = (ImageView) mRootView.findViewById(R.id.iv_female);
        mFemaleImageView.setOnClickListener(this);

        mMaleImageView = (ImageView) mRootView.findViewById(R.id.iv_male);
        mMaleImageView.setOnClickListener(this);

       // genderValue = AppInstance.signUpUser.getGender();
//        if (genderValue != 0) {
//            if (genderValue == Constants.Gender.FEMALE) {
//                mFemaleImageView.setBackgroundResource(R.mipmap.female_selected);
//            } else {
//                mMaleImageView.setBackgroundResource(R.mipmap.male_selected);
//            }
//        }
    }


//
    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.signUpStepTwoBackButton:
                fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                fragmentTransaction.replace(R.id.containerIntroFragments, new SignUpFragmentStepOne());
                fragmentTransaction.commit();
             //   MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_get_started_select_gender_back),getResources().getString(R.string.ga_event_action_get_started_select_gender_back),getResources().getString(R.string.ga_event_label_get_started_select_gender_back));
                break;
            case R.id.signUpStepTwoContinueButton:
                if (AppConfig.mSignupGender.length() == 0) {
                    String message = getString(R.string.sign_up_select_gender);
                    customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
                } else {

                    fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragmentTransaction.replace(R.id.containerIntroFragments, new SignUpFragmentStepThree());
                    fragmentTransaction.commit();
                }
                break;
            case R.id.iv_female:

                AppConfig.mSignupGender= AppConstt.Gender.FEMALE;
                mFemaleImageView.setBackgroundResource(R.mipmap.female_selected);
                mMaleImageView.setBackgroundResource(R.mipmap.male_default);
                break;
            case R.id.iv_male:

                AppConfig.mSignupGender= AppConstt.Gender.MALE;
                mMaleImageView.setBackgroundResource(R.mipmap.male_selected);
                mFemaleImageView.setBackgroundResource(R.mipmap.female_default);
                break;
        }
    }
}
