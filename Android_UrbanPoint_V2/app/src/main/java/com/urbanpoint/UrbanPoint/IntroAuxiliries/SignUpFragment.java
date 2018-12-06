package com.urbanpoint.UrbanPoint.IntroAuxiliries;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.WebViewFragment;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.SignUp_WebHit_Post_addUser;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.SignUp_WebHit_Post_checkPhoneEmail;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CellNoEntryView;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.urbanpoint.UrbanPoint.Utils.AppConstt.MIXPANEL_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {


    private EditText edtName, edtEmail;
    private TextView txvCreateAccount, txvGender, txvGirl, txvGuy, txvTermsNConditions, txvPhone, txvCharges, txvBirth, txvPin, txvLogin;
    private CellNoEntryView mPhnNumber, mPIN;
    private Button btnGetStarted;
    private ImageView imvGirl, imvGuy;
    private String strPhnNumber, strPIN;
    private String genderValue;
    private LinearLayout llParentLayout;
    private ScrollView scrollView;
    CustomAlert customAlert;
    ProgressDilogue progressDilogue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, null);
        initialize();
        bindViews(view);

        if (AppConfig.getInstance().isComingFromLogout) {
            getAppVersion();
            AppConfig.getInstance().isComingFromLogout = false;
            AppConfig.getInstance().isCommingFromSplash = true;
            navToSignInFragment();
        }

//        edtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    if (validatingRequired()) {
//                        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.progress_dialog_sign_up_loading_message), false);
//                        requestCheckEmailPhone(edtEmail.getText().toString(), false);
//                    }
//                }
//            }
//        });

//        mPhnNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    if (strPhnNumber.length() == 8) {
//                      }
//                }
//            }
//        });
        return view;
    }

    private void initialize() {
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        strPIN = "";
        strPhnNumber = "";
        genderValue = "";
    }

    private void bindViews(View frg) {
        llParentLayout = frg.findViewById(R.id.frg_sign_up_ll_parentlayout);
        scrollView = frg.findViewById(R.id.frg_sign_up_scrolview);

        edtName = frg.findViewById(R.id.frg_sign_up_edt_name);
        edtEmail = frg.findViewById(R.id.frg_sign_up_edt_email);
        txvCreateAccount = frg.findViewById(R.id.frg_sign_up_txv_create_account);
        txvGender = frg.findViewById(R.id.frg_sign_up_txv_gender);
        txvGirl = frg.findViewById(R.id.frg_sign_up_txv_girl);
        txvGuy = frg.findViewById(R.id.frg_sign_up_txv_guy);
        txvPhone = frg.findViewById(R.id.frg_sign_up_txv_phn_numbr);
        txvCharges = frg.findViewById(R.id.frg_sign_up_txv_charges);
        txvBirth = frg.findViewById(R.id.frg_sign_up_txv_birth);
        txvPin = frg.findViewById(R.id.frg_sign_up_txv_pin);
        txvLogin = frg.findViewById(R.id.frg_sign_up_txv_login);
        txvTermsNConditions = frg.findViewById(R.id.frg_sign_up_txv_terms_n_conditions);

        imvGirl = frg.findViewById(R.id.frg_sign_up_imv_girl);
        imvGuy = frg.findViewById(R.id.frg_sign_up_imv_guy);

        mPhnNumber = frg.findViewById(R.id.frg_sign_up_mobile_entry);
        mPIN = frg.findViewById(R.id.frg_sign_up_pin_entry);

        mPhnNumber.setOnPinEnteredListener(new CellNoEntryView.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                if (pin.length() == 8) {
                    strPhnNumber = pin;
                    AppConfig.getInstance().closeKeyboard(getActivity());
//                    progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.progress_dialog_sign_up_loading_message), false);
//                    requestCheckEmailPhone(strPhnNumber, true);

                } else {
                    strPhnNumber = "";
                }
//                utilObj.keyboardClose(mContext, mPhnNumber);
//                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
        mPIN.setOnPinEnteredListener(new CellNoEntryView.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                strPIN = pin;
                AppConfig.getInstance().closeKeyboard(getActivity());
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        edtEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    if (validatingRequired()) {
//                        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.progress_dialog_sign_up_loading_message), false);
//                        requestCheckEmailPhone(edtEmail.getText().toString(), false);
                    AppConfig.getInstance().closeKeyboard(getActivity());
//                    }
                    return true;
                }
                return false;
            }
        });

        btnGetStarted = frg.findViewById(R.id.frg_sign_up_btn_get_started);
        btnGetStarted.setOnClickListener(this);
        llParentLayout.setOnClickListener(this);
        txvTermsNConditions.setOnClickListener(this);
        imvGuy.setOnClickListener(this);
        imvGirl.setOnClickListener(this);
        txvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.frg_sign_up_ll_parentlayout:
                AppConfig.getInstance().closeKeyboard(getActivity());
                break;
            case R.id.frg_sign_up_txv_terms_n_conditions:
                Bundle b = new Bundle();
                b.putString("page", AppConstt.LOGIN_RULES);
                navToTermsAndConditionsFragment(b);
                break;
            case R.id.frg_sign_up_btn_get_started:
                if (edtName.length() > 0) {
                    if (edtEmail.length() > 0) {
                        if (genderValue.length() > 0) {
                            if (strPhnNumber != null && strPhnNumber.length() == 8) {
                                if (strPIN.length() == 4) {
                                    String fcmToken = AppConfig.getInstance().loadFCMToken();
                                    if (validatingRequired()) {
                                        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.progress_dialog_sign_up_loading_message), false);
                                        requestSignUp(edtName.getText().toString(),
                                                edtEmail.getText().toString().trim(),
                                                genderValue + "", strPhnNumber, strPIN, fcmToken);
                                    }
                                } else {
                                    customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), getString(R.string.enter_valid_pin_message), null, null, false, null);
                                }
                            } else {
                                customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), getString(R.string.voda_do_unsubscribe_enter_mobile_number), null, null, false, null);
                            }
                        } else {
                            customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), getString(R.string.sign_up_select_gender_heading), null, null, false, null);
                        }
                    } else {
                        customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), getString(R.string.EmailErrorMessage), null, null, false, null);
                    }
                } else {
                    customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), getString(R.string.sign_up_enter_name_message), null, null, false, null);
                }
                break;
            case R.id.frg_sign_up_imv_girl:
                genderValue = AppConstt.Gender.FEMALE;
                imvGirl.setBackgroundResource(R.drawable.icn_girl_slc);
                imvGuy.setBackgroundResource(R.drawable.icn_guy);

                txvGirl.setTextColor(getResources().getColor(R.color.black));
                txvGuy.setTextColor(getResources().getColor(R.color.gray));
                AppConfig.getInstance().closeKeyboard(getActivity());
                break;

            case R.id.frg_sign_up_imv_guy:
                genderValue = AppConstt.Gender.MALE;
                imvGirl.setBackgroundResource(R.drawable.icn_girl);
                imvGuy.setBackgroundResource(R.drawable.icn_guy_slc);

                txvGirl.setTextColor(getResources().getColor(R.color.gray));
                txvGuy.setTextColor(getResources().getColor(R.color.black));
                AppConfig.getInstance().closeKeyboard(getActivity());
                break;

            case R.id.frg_sign_up_txv_login:
                navToSignInFragment();
                break;
        }
    }

    private void navToTermsAndConditionsFragment(Bundle bundle) {
        Fragment fr = new WebViewFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fr.setArguments(bundle);
        ft.add(R.id.activity_intro_frm, fr, AppConstt.FRGTAG.WebViewFragment);
        ft.addToBackStack(AppConstt.FRGTAG.WebViewFragment);
        ft.hide(this);
        ft.commit();
    }

    private void navToSignUpVerificationFragment(Bundle b) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment frg = new SignUpVerificationFragment();
        frg.setArguments(b);
        ft.add(R.id.activity_intro_frm, frg, AppConstt.FRGTAG.FN_SignUpVerificationFragment);
        ft.addToBackStack(AppConstt.FRGTAG.FN_SignUpVerificationFragment);
        ft.hide(this);
        ft.commit();
    }

    private void navToSignInFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment frg = new SignInFragment();
        ft.add(R.id.activity_intro_frm, frg, AppConstt.FRGTAG.FN_SignInFragment);
        ft.addToBackStack(AppConstt.FRGTAG.FN_SignInFragment);
        ft.hide(this);
        ft.commit();
    }

    private void navToMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }

    private void requestSignUp(String _name, String _email, String _gender, String _phone, String _pin, String _fcmToken) {
        SignUp_WebHit_Post_addUser signUp_webHit_post_addUser = new SignUp_WebHit_Post_addUser();
        signUp_webHit_post_addUser.requestSignUp(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    logFireBaseEvent();
                    logFaceBookEvent();
                    logMixPanelEvent();
                    if (SignUp_WebHit_Post_addUser.responseObject != null) {
                        if (SignUp_WebHit_Post_addUser.responseObject.getData().getPremierUser().equalsIgnoreCase("1")) {
                            Bundle b = new Bundle();
                            b.putString(AppConstt.BundleStrings.premierUserPhone, AppConfig.getInstance().mUser.getmPhoneNumber());
                            b.putString(AppConstt.BundleStrings.premierUserPIN, SignUp_WebHit_Post_addUser.responseObject.getData().getVerificationCode());
                            navToSignUpVerificationFragment(b);
                        } else {
                            AppConfig.getInstance().isCommingFromSplash = true;
                            navToMainActivity();
                        }
                    }
                } else {
                    customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), strMsg, null, null, false, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_in_unsuccess_login_heading), ex.getMessage(), null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();

            }
        }, _name, _email, _gender, _phone, _pin, _fcmToken);
    }

    private void requestCheckEmailPhone(String _value, final boolean _isPhone) {
        SignUp_WebHit_Post_checkPhoneEmail signUp_webHit_post_checkPhoneEmail = new SignUp_WebHit_Post_checkPhoneEmail();
        signUp_webHit_post_checkPhoneEmail.requestcheckPhoneEmail(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    if (_isPhone) {
                        mPIN.requestFocus();
                    }
                } else {
                    if (_isPhone) {
                        mPhnNumber.clearText();
                    } else {
                        edtEmail.setText("");
                    }
                    customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), strMsg, null, null, false, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                if (_isPhone) {
                    mPhnNumber.clearText();
                } else {
                    edtEmail.setText("");
                }
                customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_in_unsuccess_login_heading), ex.getMessage(), null, null, false, null);
            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();
            }
        }, _value, _isPhone);
    }

    private void getAppVersion() {
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            float version = Float.parseFloat(pInfo.versionName);
            Log.d("AppVersion", "showAppUpdateDialogue: " + version);
            AppConfig.getInstance().mUser.setmAppVersion(version);


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean validatingRequired() {
        String message = "";
        String email = edtEmail.getText().toString().trim();
        String fcmToken = AppConfig.getInstance().loadFCMToken();
/*  String enteredPin = enteredNewPin;

        String confirmPin = enteredConfirmPin;*/

        //validate the content
        if (email.isEmpty()) {
            message = getResources().getString(R.string.EmailErrorMessage);
            //utilObj.showError(this, message, textViewObj, emailObj);
            customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_up_enter_account_setup_heading), message, null, null, false, null);
        } else if (fcmToken.length() == 0) {
            message = getResources().getString(R.string.networkError);
            customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
        } else if (!checkEmail(email)) {
            message = getResources().getString(R.string.invalid_email);
            customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_up_enter_account_setup_heading), message, null, null, false, null);
        }

        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }

    }

    public boolean checkEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void logFireBaseEvent() {
        Bundle params = new Bundle();
        params.putString("user_id", AppConfig.getInstance().mUser.getmUserId());
        params.putString("device_type", "Android");
        // Send the event
        FirebaseAnalytics.getInstance(getActivity()).logEvent(AppConstt.FireBaseEvents.Successful_Signup, params);
    }
    private void logFaceBookEvent() {
         AppEventsLogger.newLogger(getActivity()).logEvent(AppConstt.FireBaseEvents.Successful_Signup);
    }

    private void logMixPanelEvent() {
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(getActivity(), MIXPANEL_TOKEN);
        mixpanel.identify(AppConfig.getInstance().mUser.getmUserId());
        mixpanel.getPeople().identify(AppConfig.getInstance().mUser.getmUserId());
        mixpanel.getPeople().set("Email", AppConfig.getInstance().mUser.getmEmail());
        mixpanel.getPeople().set("Gender", AppConfig.getInstance().mUser.getmGender());
        mixpanel.getPeople().set("Created at", timeStamp);
        mixpanel.getPeople().set("Last logged in at", timeStamp);
    }

}
