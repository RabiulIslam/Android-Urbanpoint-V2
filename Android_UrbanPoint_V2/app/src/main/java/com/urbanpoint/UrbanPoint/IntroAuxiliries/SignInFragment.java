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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
//import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.PinEntry;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment implements View.OnClickListener {


    private EditText edtEmail;
    private TextView txvLogIn, txvEnterPin, txvPinHint, txvForgot, txvEmail;
    private PinEntry mPIN;
    private Button btnConfirm;
    private String strPIN;
    private RelativeLayout rlParentLayout;
    private RelativeLayout rlBack;
    CustomAlert customAlert;
    ProgressDilogue progressDilogue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, null);
        init();
        bindViews(view);
        return view;
    }

    private void init() {
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();

    }

    private void bindViews(View frg) {
        rlParentLayout = frg.findViewById(R.id.frg_sign_in_rl_parentlayout);
        rlBack = frg.findViewById(R.id.frg_sign_in_rl_back);

        edtEmail = frg.findViewById(R.id.frg_sign_in_edt_email);
        txvLogIn = frg.findViewById(R.id.frg_sign_in_txv_login);
        txvEnterPin = frg.findViewById(R.id.frg_sign_in_txv_enter_pin);
        txvPinHint = frg.findViewById(R.id.frg_sign_in_txv_pin_hint);
        txvForgot = frg.findViewById(R.id.frg_sign_in_txv_forgot);
        txvEmail = frg.findViewById(R.id.frg_sign_in_txv_email);


        mPIN = frg.findViewById(R.id.frg_sign_in_pin_entry);
        mPIN.setOnPinEnteredListener(new PinEntry.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                if (pin.length() == 4) {
                    strPIN = pin;
                    AppConfig.getInstance().closeKeyboard(getActivity());
                } else {
                    strPIN = "";
                }
            }
        });

        btnConfirm = frg.findViewById(R.id.frg_sign_in_btn_confirm);

        rlBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        txvForgot.setOnClickListener(this);
        rlParentLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_sign_in_btn_confirm:
                String fcmToken = AppConfig.getInstance().loadFCMToken();
                if (validatingRequired()) {
                    progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.progress_dialog_logging_your_account), false);
//                    requestSignIn(edtEmail.getText().toString().trim(), strPIN, fcmToken);

                    Log.e("check","login");
                }
                break;

            case R.id.frg_sign_in_rl_back:
                AppConfig.getInstance().closeKeyboard(getActivity());
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
                break;

            case R.id.frg_sign_in_rl_parentlayout:
                AppConfig.getInstance().closeKeyboard(getActivity());
                break;

            case R.id.frg_sign_in_txv_forgot:
                navToForgotPasswordFragment();
                break;
        }
    }

    private void navToForgotPasswordFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_intro_frm, new ForgotPasswordFragment(), AppConstt.FRGTAG.FN_ForgotPasswordFragment);
        fragmentTransaction.addToBackStack(AppConstt.FRGTAG.FN_ForgotPasswordFragment);
        fragmentTransaction.commit();
    }

    private void navToMainActivity() {
        AppConfig.getInstance().isCommingFromSplash = true;
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }

//    private void requestSignIn(String _email, String _pin, String _fcmToken) {
//        SignIn_WebHit_Post_signIn signIn_webHit_post_signIn = new SignIn_WebHit_Post_signIn();
//        signIn_webHit_post_signIn.requestSignIn(getContext(), new IWebCallbacks() {
//            @Override
//            public void onWebResult(boolean isSuccess, String strMsg) {
//                progressDilogue.stopiOSLoader();
//                Log.e("msg",strMsg);
//                if (isSuccess) {
//
////                    logFireBaseEvent();
////                    logFaceBookEvent();
////                    logMixPanelEvent();
//                    navToMainActivity();
//                } else {
//                    customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_in_unsuccess_login_heading), strMsg, null, null, false, null);
//
//                }
//            }
//
//            @Override
//            public void onWebException(Exception ex) {
//                progressDilogue.stopiOSLoader();
//                customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_in_unsuccess_login_heading), ex.getMessage(), null, null, false, null);
//
//            }
//
//            @Override
//            public void onWebLogout() {
//                progressDilogue.stopiOSLoader();
//            }
//        }, _email, _pin, _fcmToken);
//    }

    private boolean validatingRequired() {
        String message = "";
        String email = edtEmail.getText().toString().trim();
        String enteredPin = strPIN;
        String fcmToken = AppConfig.getInstance().loadFCMToken();

        //validate the content
        if (email.isEmpty()) {
            message = getResources().getString(R.string.EmailErrorMessage);
            //showError(this, message, textViewObj, emailObj);
            customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
        } else if (!checkEmail(email)) {
            message = getResources().getString(R.string.invalid_email);
            customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
        } else if (fcmToken.length() == 0) {
            message = getResources().getString(R.string.networkError);
            customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
        } else if (enteredPin.equalsIgnoreCase("")) {
            message = getResources().getString(R.string.PasswordErrorMessage);
            customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);

        } else if (enteredPin.length() < 4) {
            message = getResources().getString(R.string.PasswordErrorMessage);
            customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
        }
        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }
    }

    private void logFireBaseEvent() {
        Bundle params = new Bundle();
        params.putString("user_id", AppConfig.getInstance().mUser.getmUserId());
        params.putString("device_type", "Android");
        // Send the event
        FirebaseAnalytics.getInstance(getActivity()).logEvent(AppConstt.FireBaseEvents.Successful_Login, params);
    }
//    private void logFaceBookEvent() {
//        AppEventsLogger.newLogger(getActivity()).logEvent(AppConstt.FireBaseEvents.Successful_Login);
//    }
//
//    private void logMixPanelEvent() {
//        String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
//        MixpanelAPI mixpanel = MixpanelAPI.getInstance(getActivity(), MIXPANEL_TOKEN);
//        mixpanel.identify(AppConfig.getInstance().mUser.getmUserId());
//        mixpanel.getPeople().identify(AppConfig.getInstance().mUser.getmUserId());
//        mixpanel.getPeople().set("Last logged in at", timeStamp);
//    }


    public boolean checkEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
