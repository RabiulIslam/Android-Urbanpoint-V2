package com.urbanpoint.UrbanPoint.IntroAuxiliries;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.SignUp_WebHit_Get_sendMT;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.SignUp_WebHit_POST_addPremierUser;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.Signup_WEbhit_Post_validateCode;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.PinEntry;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

/**
 * A simple {@link Fragment} subclass.
 */

public class SignUpVerificationFragment extends Fragment implements View.OnClickListener {
    private TextView txvCreateAccount, txvEnterCode, txvFreeAccess, txvNtReceived;
    private PinEntry mPIN;
    private Button btnConfirm, btnSkip;
    private String strEnteredPIN, strPIN, strPhone;
    private RelativeLayout rlParentLayout;
    private RelativeLayout rlBack;
    CustomAlert customAlert;
    ProgressDilogue progressDilogue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_verification, null);
        initialize();
        bindViews(view);
        return view;
    }

    private void initialize() {
        strEnteredPIN = "";
        strPIN = "";
        strPhone = "";

        Bundle b = this.getArguments();
        if (b != null) {
            strPIN = b.getString(AppConstt.BundleStrings.premierUserPIN);
            strPhone = b.getString(AppConstt.BundleStrings.premierUserPhone);
        }

        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();

        if (AppConfig.getInstance().isComingFromHome) {
            requestResendMT(AppConfig.getInstance().mUser.getmPhoneNumber());
        } else {

        }
    }

    private void bindViews(View frg) {
        rlParentLayout = frg.findViewById(R.id.frg_sign_up_verifictn_ll_mainParentLayout);
        rlBack = frg.findViewById(R.id.frg_sign_up_verifictn_rl_back);
        txvCreateAccount = frg.findViewById(R.id.frg_sign_up_verifictn_txv_create_account);
        txvEnterCode = frg.findViewById(R.id.frg_sign_up_verifictn_txv_enter_code);
        txvFreeAccess = frg.findViewById(R.id.frg_sign_up_verifictn_txv_free_access);
        txvNtReceived = frg.findViewById(R.id.frg_sign_up_verifictn_txv_nt_received);

        mPIN = frg.findViewById(R.id.frg_sign_up_verifictn_pin_entry);

        mPIN.setOnPinEnteredListener(new PinEntry.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                if (pin.length() == 6) {
                    strEnteredPIN = pin;
                    AppConfig.getInstance().closeKeyboard(getActivity());
                } else {
                    strEnteredPIN = "";
                }
            }
        });

        btnConfirm = frg.findViewById(R.id.frg_sign_up_verifictn_btn_confirm);
        btnSkip = frg.findViewById(R.id.frg_sign_up_verifictn_btn_skip);

        rlParentLayout.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnSkip.setOnClickListener(this);
        txvNtReceived.setOnClickListener(this);
        rlBack.setOnClickListener(this);

        if (AppConfig.getInstance().isComingFromHome) {
            txvCreateAccount.setVisibility(View.GONE);
            btnSkip.setVisibility(View.GONE);
        } else {
            txvCreateAccount.setVisibility(View.VISIBLE);
            btnSkip.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.frg_sign_up_verifictn_btn_skip:
                navToMainActivity();
                break;

            case R.id.frg_sign_up_verifictn_txv_nt_received:
                Log.d("TRYAGAIN_CLICK", "true");
                if (AppConfig.getInstance().isComingFromHome) {
                    requestResendMT(AppConfig.getInstance().mUser.getmPhoneNumber());
                } else {
                    requestResendMT(strPhone);
                }


                break;

            case R.id.frg_sign_up_verifictn_btn_confirm:

                if (strEnteredPIN.length() == 6) {

                    if (AppConfig.getInstance().isComingFromHome) {
                        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
                        requestValidateCode(AppConfig.getInstance().mUser.getmPhoneNumber(), strEnteredPIN);
                    } else {
                        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
                        requestValidateCode(strPhone, strEnteredPIN);
                    }
                } else {
                    customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), getString(R.string.enter_valid_pin_message), null, null, false, null);
                }

                break;

            case R.id.frg_sign_up_verifictn_rl_back:
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
                break;

            case R.id.frg_sign_up_verifictn_ll_mainParentLayout:
                AppConfig.getInstance().closeKeyboard(getActivity());
                break;
        }


    }

    private void requestValidateCode(String _phone, String _code) {
        Signup_WEbhit_Post_validateCode signup_wEbhit_post_validateCode = new Signup_WEbhit_Post_validateCode();
        signup_wEbhit_post_validateCode.requestValidateCode(getActivity(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                if (isSuccess) {
                    progressDilogue.stopiOSLoader();
                    if (Signup_WEbhit_Post_validateCode.responseObject.getMessage().equals("matched")) {

                        if (AppConfig.getInstance().isComingFromHome) {

                            progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);

                            requestAddPremierUser(AppConfig.getInstance().mUser.getmPhoneNumber());


                        } else {
                            progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);

                            requestAddPremierUser(strPhone);

                        }
                    } else {

                    }
                } else {
                    customAlert.showToast(getContext(), strMsg, Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onWebException(Exception ex) {

            }

            @Override
            public void onWebLogout() {

            }
        }, _phone, _code);
    }


    private void navToMainActivity() {
        //call the intent for the next activity
        Intent intentObj = new Intent(getActivity(), MainActivity.class);
        intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentObj);
        getActivity().finish();
    }

    private void requestResendMT(String _phone) {
        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getResources().getString(R.string.please_wait), false);
        SignUp_WebHit_Get_sendMT signUp_webHit_get_sendMT = new SignUp_WebHit_Get_sendMT();
        signUp_webHit_get_sendMT.requestResendCode(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    logFireBaseEvent();
                    if (SignUp_WebHit_Get_sendMT.responseObject != null) {
                        strPIN = SignUp_WebHit_Get_sendMT.responseObject.getData();
                    }
                } else {
                    customAlert.showToast(getContext(), strMsg, Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                customAlert.showToast(getContext(), ex.getMessage(), Toast.LENGTH_SHORT);

            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();

            }
        }, _phone);
    }

    private void logFireBaseEvent() {
        Bundle params = new Bundle();
        params.putString("user_id", AppConfig.getInstance().mUser.getmUserId());
        params.putString("device_type", "Android");
        // Send the event
        FirebaseAnalytics.getInstance(getActivity())
                .logEvent(AppConstt.FireBaseEvents.OTP_Generation, params);
    }
    private void requestAddPremierUser(String _phone) {
        SignUp_WebHit_POST_addPremierUser signUp_webHit_post_addPremierUser = new SignUp_WebHit_POST_addPremierUser();
        signUp_webHit_post_addPremierUser.addPremierUser(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    AppConfig.getInstance().isCommingFromSplash = true;
                    AppConfig.getInstance().isEligible = false;
                    navToMainActivity();
                } else {
                    customAlert.showToast(getContext(), strMsg, Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                customAlert.showToast(getContext(), ex.getMessage(), Toast.LENGTH_SHORT);

            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();

            }
        }, _phone);
    }
}
