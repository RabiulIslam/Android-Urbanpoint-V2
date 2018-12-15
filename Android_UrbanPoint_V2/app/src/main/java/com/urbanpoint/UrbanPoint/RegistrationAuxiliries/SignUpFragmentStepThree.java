package com.urbanpoint.UrbanPoint.RegistrationAuxiliries;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.AppInstance;
import com.urbanpoint.UrbanPoint.Utils.HoloCircleSeekBar;
import com.urbanpoint.UrbanPoint.Utils.Utility;
//import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
//import com.urbanpoint.UrbanPoint.utils.HoloCircleSeekBar;
//import com.urbanpoint.UrbanPoint.utils.Utility;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragmentStepThree extends Fragment implements View.OnClickListener {

    private Utility utilObj;

    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private Button mSignUpBackView;
    private Button mSignUpContinueView;

    private ImageView imvArrowLeft, imvArrowRight, imvCircleLeft, imvCircleRight;
    private TextView txvMonth, txvYear, txvSkip;
    private RelativeLayout rlMonth, rlYear;
    private boolean isMonthFocused;
    private List<String> lstMonths;
    private int monthNo, yearNo;
    HoloCircleSeekBar picker;

    public SignUpFragmentStepThree() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_step_three, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
       initialize();
        return view;
    }

    private void initialize() {
        utilObj = new Utility(mActivity);
        bindViews();
//        UpdateFoucs();
    }

    private void UpdateFoucs() {
        if (isMonthFocused) {
        }
    }

    private void bindViews() {

        txvSkip = (TextView) mRootView.findViewById(R.id.frg_sign_up_three_txv_skip);
        txvSkip.setOnClickListener(this);

        picker = (HoloCircleSeekBar) mRootView.findViewById(R.id.picker);
        picker.getValue();
        //value set to 25
//        if (AppInstance.signUpUser.getAge() != null) {
//            if (AppInstance.signUpUser.getAge().equals("70+")) {
//                Log.d("PICKERR", "bindViews: --->70+ ");
//                picker.setValue(60);
//
//            } else {
//                int age = Integer.parseInt(AppInstance.signUpUser.getAge());
//                Log.d("PICKERR", "bindViews: " + AppInstance.signUpUser.getAge());
//                picker.setValue(age - 10);
//
//             }
//        } else {
//            picker.setValue(picker.getValue() + 5);
//        }

        if (AppConfig.getInstance().getAge()!=null && AppConfig.getInstance().getAge()!="null"
                && !(TextUtils.isEmpty(AppConfig.getInstance().getAge())))
        {
            if (AppConfig.getInstance().getAge().equalsIgnoreCase("70+"))
            {
                picker.setValue(60);
            }
            else {
                picker.setValue(Float.parseFloat(AppConfig.getInstance().getAge())-10);
            }
        }
        else
        {
            picker.setValue(picker.getValue() + 5);
        }
        mSignUpBackView = (Button) mRootView.findViewById(R.id.signUpStepFiveBackButton);
        mSignUpBackView.setOnClickListener(this);
        mSignUpContinueView = (Button) mRootView.findViewById(R.id.signUpStepFiveContinueButton);
        mSignUpContinueView.setOnClickListener(this);


    }


//
    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.signUpStepFiveBackButton:
                fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                fragmentTransaction.replace(R.id.containerIntroFragments, new SignUpFragmentStepTwo());
                fragmentTransaction.commit();
                break;

            case R.id.signUpStepFiveContinueButton:

                if (picker.getValue() > 70) {

                    AppConfig.getInstance().setAge( "70+");
                   // Log.d("PICKERR", "bindViews age:" + AppConfig.mSignupAge);
                } else {
                    AppConfig.getInstance().setAge(picker.getValue() + "");
                 //   Log.d("PICKERR", "bindViews age:" + AppConfig.mSignupAge);
                }

                fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                fragmentTransaction.replace(R.id.containerIntroFragments, new SignUpFragmentStepSix());
                fragmentTransaction.commit();
                break;
            case R.id.frg_sign_up_three_txv_skip:
                fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                fragmentTransaction.replace(R.id.containerIntroFragments, new SignUpFragmentStepSix());
                fragmentTransaction.commit();
                break;


//            case R.id.frg_sign_up_three_imv_arrow_right:
//                if (monthNo > 10) {
//                    monthNo = 0;
//                } else {
//                    monthNo++;
//                }
//                txvMonth.setText(lstMonths.get(monthNo));
//                Log.d("MONTT", "onClick: No. " + monthNo);
//                updateBtnBg(false);
//                break;
//
//            case R.id.frg_sign_up_three_imv_arrow_left:
//                if (monthNo < 1) {
//                    monthNo = 11;
//                } else {
//                    monthNo--;
//                }
//                txvMonth.setText(lstMonths.get(monthNo));
//                Log.d("MONTT", "onClick: No. " + monthNo);
//                updateBtnBg(false);
//                break;
//
//            case R.id.frg_sign_up_three_imv_circle_left:
//                if (yearNo > 1960) {
//                    yearNo--;
//                }
//                txvYear.setText("" + yearNo);
//                updateBtnBg(true);
//                break;
//
//            case R.id.frg_sign_up_three_imv_circle_right:
//                if (yearNo < 2017) {
//                    yearNo++;
//                }
//                txvYear.setText("" + yearNo);
//                updateBtnBg(true);
//                break;
        }
    }

//    private void updateBtnBg(boolean _isCircle) {
//        if (_isCircle) {
//            imvArrowRight.setBackgroundDrawable(getResources().getDrawable(R.mipmap.right_arrow_transparent));
//            imvArrowLeft.setBackgroundDrawable(getResources().getDrawable(R.mipmap.left_arrow_transparent));
//            imvCircleRight.setBackgroundDrawable(getResources().getDrawable(R.mipmap.right_circle_white));
//            imvCircleLeft.setBackgroundDrawable(getResources().getDrawable(R.mipmap.left_circle_white));
//        } else {
//            imvArrowRight.setBackgroundDrawable(getResources().getDrawable(R.mipmap.right_arrow_white));
//            imvArrowLeft.setBackgroundDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
//            imvCircleRight.setBackgroundDrawable(getResources().getDrawable(R.mipmap.right_circle_transparent));
//            imvCircleLeft.setBackgroundDrawable(getResources().getDrawable(R.mipmap.left_circle_transparent));
//
//        }
//    }
}
