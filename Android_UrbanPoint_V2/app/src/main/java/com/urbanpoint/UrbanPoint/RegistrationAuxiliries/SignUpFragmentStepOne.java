package com.urbanpoint.UrbanPoint.RegistrationAuxiliries;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.AppInstance;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;
import com.urbanpoint.UrbanPoint.Utils.Utility;
//import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
//import com.urbanpoint.UrbanPoint.dataobject.SignUpUser;
//import com.urbanpoint.UrbanPoint.utils.Utility;
//import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragmentStepOne extends Fragment implements View.OnClickListener {

    CustomAlert customAlert;
    ProgressDilogue progressDilogue;

    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private Button mSignUpBackView;
    private Button mSignUpContinueView;
    private EditText mSignUpUserName;
    private Utility utilObj;
    private LinearLayout mMainParentLayout;


    public SignUpFragmentStepOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.sign_up_step_one, null);


        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //---------
        initialize();
//        MyApplication.getInstance().trackScreenView(getResources().getString(R.string.get_started) + " " + getResources().getString(R.string.ga_with_name_field));
        //---------
        return view;
    }

    private void initialize() {
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        //utilObj = new Utility(mActivity);
        bindViews();
    }

    private void bindViews() {
        mMainParentLayout = (LinearLayout) mRootView.findViewById(R.id.mainParentLayout);
        mMainParentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppConfig.getInstance().closeKeyboard(getActivity());
                return false;
            }
        });

        mSignUpUserName = (EditText) mRootView.findViewById(R.id.signUpUserName);
        mSignUpBackView = (Button) mRootView.findViewById(R.id.signUpStepOneBackButton);
        mSignUpBackView.setOnClickListener(this);
        mSignUpContinueView = (Button) mRootView.findViewById(R.id.signUpStepOneContinueButton);
        mSignUpContinueView.setOnClickListener(this);

        if (AppConfig.getInstance().getName()!=null & AppConfig.getInstance().getName()!="null"
        &!(TextUtils.isEmpty(AppConfig.getInstance().getName())))

        {
            mSignUpUserName.setText(AppConfig.getInstance().getName());
        }
    }


    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.signUpStepOneBackButton:
                fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                fragmentTransaction.replace(R.id.containerIntroFragments, new GetStartedFragment());
                fragmentTransaction.commit();
               // MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_get_started_name_field_back),getResources().getString(R.string.ga_event_action_get_started_name_field_back),getResources().getString(R.string.ga_event_label_get_started_name_field_back));
                break;
            case R.id.signUpStepOneContinueButton:

                String name = mSignUpUserName.getText().toString();
               // AppConfig.getInstance().setUsername(name);
                if (name.trim().length() == 0) {
                    String message = getString(R.string.sign_up_enter_name_message);
                    customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
                  //  utilObj.showCustomAlertDialog(mActivity, getString(R.string.header_name), message, null, null, false, null);
                } else {
                    AppConfig.getInstance().setUsername(name);
                    fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragmentTransaction.replace(R.id.containerIntroFragments, new SignUpFragmentStepTwo());
                    fragmentTransaction.commit();
                }

                break;
            case R.id.mainParentLayout:
                AppConfig.getInstance().closeKeyboard(getActivity());
               // utilObj.keyboardClose(mContext, v);
                break;
        }


    }
}
