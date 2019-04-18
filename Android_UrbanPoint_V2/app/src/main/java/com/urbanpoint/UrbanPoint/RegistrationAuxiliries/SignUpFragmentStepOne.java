package com.urbanpoint.UrbanPoint.RegistrationAuxiliries;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;
import com.urbanpoint.UrbanPoint.Utils.Utility;

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
    private Spinner mSignUpOccupation;
    private EditText mSignUpReferralCode;
    ArrayAdapter OccupationAdapter;
    TextView RefCode;
    String occupation="";

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

        mSignUpOccupation=(Spinner) mRootView.findViewById(R.id.signUpUserOccupation);
        RefCode=(TextView)mRootView.findViewById(R.id.tv_ref_code);
        mSignUpReferralCode=(EditText)mRootView.findViewById(R.id.signUpUserReferralCode);
        mSignUpReferralCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    mSignUpReferralCode.setHint("");
                }
            }
        });
        mSignUpUserName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    Utility.hideVirtualKeyboard(getActivity());
                    textView.clearFocus();
                    mSignUpOccupation.requestFocus();
                    mSignUpOccupation.performClick();
                }
                return true;
            }
        });
        OccupationAdapter=new ArrayAdapter(getActivity(),R.layout.simple_dropdown_item,
                getContext().getResources().getStringArray(R.array.occupation));
        mSignUpOccupation.setAdapter(OccupationAdapter);
//        mSignUpOccupation.setThreshold(1);
        mSignUpOccupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView) mSignUpOccupation.getSelectedView();
                if (textView != null) {
                    textView.setTextColor(Color.WHITE);
                }
                occupation= getResources().getStringArray(R.array.occupation)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                }
                if(occupation.length()<=0){
                    String message = "Please select occupation";
                    customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_up_enter_account_setup_heading),
                            message, null, null, false,
                            null);
                    return;
                }
                AppConfig.getInstance().setOccupation(occupation);
                AppConfig.getInstance().setReferralCode(mSignUpReferralCode.getText().toString());

                fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                fragmentTransaction.replace(R.id.containerIntroFragments, new SignUpFragmentStepTwo());
                fragmentTransaction.commit();
                break;
            case R.id.mainParentLayout:
                AppConfig.getInstance().closeKeyboard(getActivity());
                // utilObj.keyboardClose(mContext, v);
                break;
        }


    }
}
