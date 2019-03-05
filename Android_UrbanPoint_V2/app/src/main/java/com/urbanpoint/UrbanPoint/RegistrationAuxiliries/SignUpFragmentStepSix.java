package com.urbanpoint.UrbanPoint.RegistrationAuxiliries;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.HomeAuxiliries.WebViewFragment;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.SignUp_WebHit_Post_addUser;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.SignUp_WebHit_Post_verifyEmail;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;
import com.urbanpoint.UrbanPoint.Utils.Utility;
import com.urbanpoint.UrbanPoint.customViews.pinEntry.PinEntryView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.mixpanel.android.mpmetrics.MixpanelAPI;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragmentStepSix extends Fragment implements View.OnClickListener {


    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private Button mSignUpBackView;
    private Button mSignUpFinishView;
    private EditText mSignUpUserEmail;
    private Utility utilObj;
    //    private SignUpManager mSignUpManagerObj;
    CustomAlert customAlert;
    ProgressDilogue progressDilogue;
    private PinEntryView mSignUpNewPinEntry;
    private PinEntryView mSignUpConfirmPinEntry;
    private String enteredNewPin = "";
    private String enteredConfirmPin = "";
    private LinearLayout mMainParentLayout;
    /*private CustomTextView mLoginTermsAndConditions;

    private CustomTextView mLoginTermsAndConditionsTwo;*/
    private TextView mLoginTermsAndConditions;
    private TextView mLoginTermsAndConditionsTwo;
    private ScrollView mScrollViewMainLayout;
    private int currentApiVersion;
    private boolean isValidEmail;
    private RelativeLayout rlProgressBar;
//    private IntroActivityManager introActivityManager;

    public SignUpFragmentStepSix() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_step_six, null);
        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;

        initialize();

        return view;
    }


    private void initialize() {

        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        isValidEmail = false;

        bindViews();

    }

    private void bindViews() {
        rlProgressBar = (RelativeLayout) mRootView.findViewById(R.id.frg_sign_up_rl_progrssbar);

        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        mScrollViewMainLayout = (ScrollView) mRootView.findViewById(R.id.scrollViewMainLayout);

        mLoginTermsAndConditions = (TextView) mRootView.findViewById(R.id.loginTermsAndConditions);
        mLoginTermsAndConditions.setOnClickListener(this);
        mLoginTermsAndConditionsTwo = (TextView) mRootView.findViewById(R.id.loginTermsAndConditions_Two);
        mLoginTermsAndConditionsTwo.setOnClickListener(this);
        mMainParentLayout = (LinearLayout) mRootView.findViewById(R.id.mainParentLayout);
        mSignUpBackView = (Button) mRootView.findViewById(R.id.signUpStepSixBackButton);
        mSignUpBackView.setOnClickListener(this);
        mSignUpFinishView = (Button) mRootView.findViewById(R.id.signUpStepSixFinishButton);
        mSignUpFinishView.setOnClickListener(this);
        mSignUpUserEmail = (EditText) mRootView.findViewById(R.id.signUpUserEmail);
//        mSignUpReferralCode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if (s.length()>0)
//            {
//                RefCode.setText(getActivity().getString(R.string.referral_code));
//            }
//            else
//            {
//                RefCode.setText(getActivity().getString(R.string.referral_code_optional));
//            }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        mSignUpNewPinEntry = (PinEntryView) mRootView.findViewById(R.id.signUpNewPinEntry);
        mSignUpConfirmPinEntry = (PinEntryView) mRootView.findViewById(R.id.signUpConfirmPinEntry);
        mSignUpNewPinEntry.setOnPinEnteredListener(new PinEntryView.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                enteredNewPin = pin;
                if (currentApiVersion < Build.VERSION_CODES.LOLLIPOP) {
                    mScrollViewMainLayout.fullScroll(View.FOCUS_DOWN);
                }
                mSignUpConfirmPinEntry.requestFocus();
            }
        });
        mSignUpConfirmPinEntry.setOnPinEnteredListener(new PinEntryView.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                enteredConfirmPin = pin;
                //   utilObj.keyboardClose(mContext, mSignUpConfirmPinEntry);
                AppConfig.getInstance().closeKeyboard(getActivity());
                if (currentApiVersion < Build.VERSION_CODES.LOLLIPOP) {
                    mScrollViewMainLayout.fullScroll(View.FOCUS_UP);
                }
            }
        });

        mSignUpConfirmPinEntry.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (currentApiVersion < Build.VERSION_CODES.LOLLIPOP) {
                    mScrollViewMainLayout.fullScroll(View.FOCUS_DOWN);
                }
                mSignUpConfirmPinEntry.requestFocus();
                return false;
            }
        });

        mMainParentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppConfig.getInstance().closeKeyboard(getActivity());
                if (currentApiVersion < Build.VERSION_CODES.LOLLIPOP) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScrollViewMainLayout.fullScroll(View.FOCUS_UP);
                        }
                    }, 100);
                }
                return false;
            }
        });

    }


    private boolean validatingRequired() {
        String message = "";
        String email = mSignUpUserEmail.getText().toString().trim();
        String fcmToken = AppConfig.getInstance().loadFCMToken();
        AppConfig.getInstance().setEmail(email);
        Log.e("check","validation");
/*  String enteredPin = enteredNewPin;

        String confirmPin = enteredConfirmPin;*/

        //validate the content
        if (email.isEmpty()) {
            message = getResources().getString(R.string.EmailErrorMessage);
            //utilObj.showError(this, message, textViewObj, emailObj);
            customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_up_enter_account_setup_heading), message, null, null, false, null);
        }
        else if (enteredNewPin.isEmpty() || enteredNewPin.length()<4)
        {
            message = getResources().getString(R.string.pin_error);
            //utilObj.showError(this, message, textViewObj, emailObj);
            customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_up_enter_account_setup_heading), message, null, null, false, null);
        }
        else if(!(enteredConfirmPin.equalsIgnoreCase(enteredNewPin)))
        {
            message = getResources().getString(R.string.confirm_pin_error);
            //utilObj.showError(this, message, textViewObj, emailObj);
            customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_up_enter_account_setup_heading), message, null, null, false, null);
        }

        else if (!checkEmail(email)) {
            message = getResources().getString(R.string.invalid_email);
            customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_up_enter_account_setup_heading), message, null, null, false, null);
        }
        else if (fcmToken.length() == 0) {
            message = getResources().getString(R.string.networkError);
            customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
        }
        if (message.equalsIgnoreCase("") || message == null) {
            AppConfig.getInstance().setEmail(email);
            Log.e("entered_pin",enteredNewPin);
            AppConfig.getInstance().setPin(enteredNewPin);
            // AppConfig.getInstance().
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
    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.signUpStepSixBackButton:
                fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                fragmentTransaction.replace(R.id.containerIntroFragments, new SignUpFragmentStepThree());
                fragmentTransaction.commit();
                // MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_get_started_email_back), getResources().getString(R.string.ga_event_action_get_started_email_back), getResources().getString(R.string.ga_event_label_get_started_email_back));
                break;

            case R.id.signUpStepSixFinishButton:

                Log.e("click","finish"+validatingRequired());
                if ( validatingRequired()==true)

                {
                    Log.e("chkpin",AppConfig.getInstance().getPin()+"mm");
                    requestSignUp(AppConfig.getInstance().getName(),
                            AppConfig.getInstance().getEmail(),
                            AppConfig.getInstance().getGender(),
                            AppConfig.getInstance().getPin(),
                            AppConfig.getInstance().mFCMToken,
                            AppConfig.getInstance().getOccupation(),
                            AppConfig.getInstance().getAge(),
                            AppConfig.getInstance().getReferralCode());


                }
                break;
            case R.id.mainParentLayout:

                AppConfig.getInstance().closeKeyboard(getActivity());
                break;
            case R.id.loginTermsAndConditions:
                Log.e("click","terms");


            case R.id.loginTermsAndConditions_Two:

                Log.e("click","terms");
                Bundle b = new Bundle();
                b.putString("page", AppConstt.LOGIN_RULES);
                navToTermsAndConditionsFragment(b);

                break;


        }}

    //    private void logFireBaseEvent() {
//        Bundle params = new Bundle();
//        params.putString("user_id", AppConfig.getInstance().mUser.getmUserId());
//        params.putString("device_type", "Android");
//        // Send the event
//        FirebaseAnalytics.getInstance(getActivity()).logEvent(AppConstt.FireBaseEvents.Successful_Signup, params);
//    }
//    private void logFaceBookEvent() {
//        AppEventsLogger.newLogger(getActivity()).logEvent(AppConstt.FireBaseEvents.Successful_Signup);
//    }
//
//    private void logMixPanelEvent() {
//        String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
//        MixpanelAPI mixpanel = MixpanelAPI.getInstance(getActivity(), MIXPANEL_TOKEN);
//        mixpanel.identify(AppConfig.getInstance().mUser.getmUserId());
//        mixpanel.getPeople().identify(AppConfig.getInstance().mUser.getmUserId());
//        mixpanel.getPeople().set("Email", AppConfig.getInstance().mUser.getmEmail());
//        mixpanel.getPeople().set("Gender", AppConfig.getInstance().mUser.getmGender());
//        mixpanel.getPeople().set("Created at", timeStamp);
//        mixpanel.getPeople().set("Last logged in at", timeStamp);
//    }
    private void requestSignUp(String _name, String _email, String _gender, String _pin, String _fcmToken,
                               String _occupation,String _age,String _referral_code) {

        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        SignUp_WebHit_Post_addUser signUp_webHit_post_addUser = new SignUp_WebHit_Post_addUser();
        signUp_webHit_post_addUser.requestSignUp(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                Log.e("register_res_boolean",isSuccess+","+strMsg);
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
//                logFireBaseEvent();
////                logFaceBookEvent();
//                logMixPanelEvent();
                    if (SignUp_WebHit_Post_addUser.responseObject != null) {
//                    if (SignUp_WebHit_Post_addUser.responseObject.getData().getPremierUser().equalsIgnoreCase("1")) {
                        final Bundle b = new Bundle();
                        b.putString(AppConstt.BundleStrings.userId, AppConfig.getInstance().mUser.getmUserId());
//                        b.putString(AppConstt.BundleStrings.premierUserPIN, SignUp_WebHit_Post_addUser.responseObject.getData().getVerificationCode());
                        //navToSignUpVerificationFragment(b);

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.custome_message_alert_box);

                        dialog.setCancelable(false);
                        // set the custom dialog components - text
                        TextView messageTitle = (TextView) dialog.findViewById(R.id.messageTitle);
                        TextView messageText = (TextView) dialog.findViewById(R.id.dialogMessageText);
                        Button cancelButton = (Button) dialog.findViewById(R.id.dialogButtonCancel);
                        Button okButton = (Button) dialog.findViewById(R.id.dialogButtonOK);


                        TextView dialogButtonSeparator = (TextView) dialog.findViewById(R.id.dialogButtonSeparator);
                        TextView HorizontalSeprator=(TextView)dialog.findViewById(R.id.horizontal_seprator);
                        HorizontalSeprator.setVisibility(View.GONE);
                        dialogButtonSeparator.setVisibility(View.GONE);
                        cancelButton.setVisibility(View.GONE);
                        okButton.setVisibility(View.GONE);
                        messageTitle.setText(getString(R.string.sign_up_enter_account_setup_heading));
                        messageText.setText(getString(R.string.register_successfully));
                        dialog.show();


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                showVerifyEmailDialog(b);
                            }
                        }, 3000);

//                    } else {
//                        AppConfig.getInstance().isCommingFromSplash = true;
//                        navToMainActivity();
//                    }
                    }
                } else {
                    customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), strMsg, null, null, false, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                Log.e("ex","ex",ex);
                progressDilogue.stopiOSLoader();
                customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_in_unsuccess_login_heading), ex.getMessage(), null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                Log.e("log","out");
                progressDilogue.stopiOSLoader();

            }
        }, _name, _email, _gender, _age,_occupation,_referral_code, _pin, _fcmToken);
    }
    private void showVerifyEmailDialog(final Bundle bundle)
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.custome_message_alert_box);

        dialog.setCancelable(false);
        // set the custom dialog components - text
        TextView messageTitle = (TextView) dialog.findViewById(R.id.messageTitle);
        TextView messageText = (TextView) dialog.findViewById(R.id.dialogMessageText);
        Button cancelButton = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        Button okButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        cancelButton.setText(getString(R.string._skip));

        TextView dialogButtonSeparator = (TextView) dialog.findViewById(R.id.dialogButtonSeparator);
        dialogButtonSeparator.setVisibility(View.VISIBLE);
//        cancelButton.setVisibility(View.GONE);
//        okButton.setVisibility(View.GONE);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                navToVerifyMemberFragment(bundle);
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                verifyEmail(bundle);
            }
        });
        messageTitle.setText(getString(R.string.verify_email));
        messageText.setText(getString(R.string.send_link_to_verify_email));
        dialog.show();
    }
    private void verifyEmail(final Bundle bundle) {

        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        SignUp_WebHit_Post_verifyEmail signUp_webHit_checkPhone = new SignUp_WebHit_Post_verifyEmail();
        signUp_webHit_checkPhone.verifyEmail(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                Log.e("register_res_boolean",isSuccess+","+strMsg);
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
//                    logFireBaseEvent();
//                    logFaceBookEvent();
//                    logMixPanelEvent();
                    if (SignUp_WebHit_Post_verifyEmail.responseObject != null)
                    {
//                        Bundle b = new Bundle();
//                        b.putString(AppConstt.BundleStrings.userId, AppConfig.getInstance().mUser.getmUserId());
//                        AppConfig.getInstance().isCommingFromSplash = true;
//                        AppConfig.getInstance().mUser.setLoggedIn(true);
                        navToVerifyMemberFragment(bundle);
//                        showVerifyEmailDialog();
                    }
                } else {

                    if (strMsg.equalsIgnoreCase("Conflict")) {
                        customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), getResources().getString(R.string.already_registered), null, null, false, null);
                    }
                    else
                    {
                        customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), strMsg, null, null, false, null);

                    }
                }
            }

            @Override
            public void onWebException(Exception ex) {
                Log.e("ex","ex",ex);
                progressDilogue.stopiOSLoader();
                customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_in_unsuccess_login_heading), ex.getMessage(),
                        null, null, false, null);
            }

            @Override
            public void onWebLogout() {
                Log.e("log","out");
                progressDilogue.stopiOSLoader();

            }
        }, AppConfig.getInstance().mUser.mUserId);

    }
    private void navToTermsAndConditionsFragment(Bundle bundle) {
        Log.e("click","terms111");
        Fragment fr = new WebViewFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fr.setArguments(bundle);
        ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
        ft.add(R.id.containerIntroFragments, fr, AppConstt.FRGTAG.WebViewFragment);
        ft.addToBackStack(AppConstt.FRGTAG.WebViewFragment);
        ft.hide(this);
        ft.commit();
    }

    private void navToVerifyMemberFragment(Bundle b) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment frg = new PhoneVerificationFragment();
        frg.setArguments(b);
        ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
        ft.replace(R.id.containerIntroFragments, frg);
        ft.commit();

    }




    private void navToMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }}

//    @Override
//    public void onSuccessRedirection(int taskID) {
//
//        if (taskID == Constants.TaskID.SIGN_UP_TASK_ID) {
//
//            if (AppInstance.signUpUser != null) {
//                SignUpUser signUpUser = AppInstance.signUpUser;
//                if (ResponseCodes.STATUS_ZERO.equalsIgnoreCase(signUpUser.getStatus())) {
//                    // utilObj.showToast(mContext, signUpUser.getResponseMessage(), Toast.LENGTH_LONG);
//                    utilObj.stopiOSLoader();
//                    utilObj.showCustomAlertDialog(mActivity, getString(R.string.header_setup), signUpUser.getResponseMessage(), null, null, false, null);
//                } else {
//                    AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.OPERATOR_TYPE_VODAFONE, "" + signUpUser.getVodafonecustomer());
//                    AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.OPERATOR_TYPE_OOREDOO, "" + signUpUser.getOoredoocustomer());
//
//                    AppPreference.setSetting(mContext, Constants.Request.EMAIL_ID, signUpUser.geteMailId());
//                    AppPreference.setSetting(mContext, Constants.Request.GENDER, "" + signUpUser.getGender());
//                    AppPreference.setSetting(mContext, Constants.Request.FIRST_NAME, signUpUser.getFirstName());
//                    AppPreference.setSetting(mContext, Constants.Request.CUSTOMER_ID, signUpUser.getUserData());
//                    AppPreference.setSetting(mContext, "key_new_offer_badge", "yes");
//                    AppPreference.setSetting(mContext, "key_never_ask_again_location", "");
//                    mSignUpManagerObj.doCMSSignUp(AppInstance.signUpUser, enteredConfirmPin);
//                    introActivityManager.doFetchHomeOffers2();
//                    introActivityManager.doCheckSubscribe(signUpUser.getUserData());
//
//                }
//            } else {
//                utilObj.stopiOSLoader();
//                AppInstance.signUpUser = AppInstance.tempLocalSignUpUser;
//                utilObj.showCustomAlertDialog(mActivity, getString(R.string.header_setup), getResources().getString(R.string.no_internet), null, null, false, null);
//            }
//
//        } else if (taskID == Constants.TaskID.GCM_SIGN_UP_TASK_ID) {
//
//// -- Don't show HowItWork when user is VODAFONE USER
//            utilObj.stopiOSLoader();
//            Intent intentObj = null;
//            AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 4);
//
//            if (AppPreference.getSetting(mContext, Constants.DEFAULT_VALUES.OPERATOR_TYPE_VODAFONE, "").equalsIgnoreCase("1") ||
//                    AppPreference.getSetting(mContext, Constants.DEFAULT_VALUES.OPERATOR_TYPE_OOREDOO, "").equalsIgnoreCase("1")) {
//
//                intentObj = new Intent(getActivity(), DashboardActivity.class);
//                //  intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                intentObj.putExtra(Constants.DEFAULT_VALUES.SIGN_UP, true);
//
//                startActivity(intentObj);
//
//                mActivity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
//
//                getActivity().finish();
//
//            } else {
//                //call the intent for the next activity
//                intentObj = new Intent(getActivity(), HowItWorksActivity.class);
//                intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intentObj.putExtra(Constants.DEFAULT_VALUES.SIGN_UP, true);
//
//                startActivity(intentObj);
//                mActivity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
//
//            }
//        }
//    }
//
//    @Override
//    public void onFailureRedirection(String errorMessage) {
//        utilObj.stopiOSLoader();
//        utilObj.showToast(mContext, errorMessage, Toast.LENGTH_LONG);
//    }
//
//    public void showResultEmailValidation(boolean b, String s) {
//        rlProgressBar.setVisibility(View.GONE);
//        if (b) {
//            if (NetworkUtils.isConnected(mContext)) {
//                String email = mSignUpUserEmail.getText().toString();
//                String enteredPin = enteredNewPin;
//                int pinCode = Integer.parseInt(enteredPin);
//                int num4 = pinCode % 10;
//                int num1 = pinCode / 1000 % 10;
//                AppPreference.setSetting(mContext, Constants.Request.PINCODE, num1 + "**" + num4);
//
//
//                if (AppInstance.signUpUser != null) {
//                    AppInstance.signUpUser.seteMailId(email);
//                    AppInstance.signUpUser.setCustomerPin(enteredPin);
//                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_get_started_email_finish), getResources().getString(R.string.ga_event_action_get_started_email_finish), getResources().getString(R.string.ga_event_label_get_started_email_finish));
////                        utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.sign_up_loading_message), false);
//                    mSignUpManagerObj.doSignUp(AppInstance.signUpUser);
//                }
//
//            } else {
//                utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
//            }
//
//        } else {
//            utilObj.stopiOSLoader();
//            utilObj.showCustomAlertDialog(mActivity, getString(R.string.header_setup), getResources().getString(R.string.invalid_email), null, null, false, null);
////            utilObj.showToast(mContext, getResources().getString(R.string.invalid_email), Toast.LENGTH_LONG);
//        }


