package com.urbanpoint.UrbanPoint.RegistrationAuxiliries;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//
//import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
//import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.FCMAuxiliries.FirebaseInstanceId;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.ForgotPasswordFragment;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.SignIn_WebHit_Post_signIn;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.PinEntry;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;
import com.urbanpoint.UrbanPoint.Utils.Utility;
import com.urbanpoint.UrbanPoint.customViews.pinEntry.PinEntryView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.urbanpoint.UrbanPoint.Utils.AppConstt.MIXPANEL_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener
        //implements ServiceRedirection, View.OnClickListener, CustomDialogConfirmationInterfaces {//, TextWatcher, View.OnFocusChangeListener {

{
    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private Button mBackView;
    private Button mLoginToAppView;
    //    private LoginManager loginManagerObj;
    private Utility utilObj;
    //    private InputUser inputUserObj;
    private EditText mUserEmail;
    private TextView mForgotPassword;

    private PinEntryView mLoginPinEntry;
    private String enteredLoginPin = "";
    //    private MyReviewsManager mMyReviewManager;
    private LinearLayout mMainLoginLayout;
    private String mStoredDeviceID;
    private String gcmID;
    //    private IntroActivityManager introActivityManager;
    private String strPIN;
    CustomAlert customAlert;
    ProgressDilogue progressDilogue;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initialize();

        return view;
    }

    private void initialize() {
        utilObj = new Utility(mActivity);
        bindViews();
    }

    private void bindViews() {
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        mMainLoginLayout = (LinearLayout) mRootView.findViewById(R.id.mainLoginLayout);
        mMainLoginLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppConfig.getInstance().closeKeyboard(getActivity());
                return false;
            }
        });

        mBackView = (Button) mRootView.findViewById(R.id.loginBackButton);
        mBackView.setOnClickListener(this);

        mForgotPassword = (TextView) mRootView.findViewById(R.id.forgotPassword);
        mForgotPassword.setOnClickListener(this);

        mLoginToAppView = (Button) mRootView.findViewById(R.id.loginToApp);
        mLoginToAppView.setOnClickListener(this);

        mUserEmail = (EditText) mRootView.findViewById(R.id.userEmail);

        mLoginPinEntry = (PinEntryView) mRootView.findViewById(R.id.loginPinEntry);

        mLoginPinEntry.setOnPinEnteredListener(new PinEntryView.OnPinEnteredListener() {
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


    }
    private void navToForgotPasswordFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
        fragmentTransaction.replace(R.id.containerIntroFragments, new ForgotPasswordFragment());
        fragmentTransaction.commit();
    }


    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.loginBackButton:
                fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                fragmentTransaction.replace(R.id.containerIntroFragments, new GetStartedFragment());
                fragmentTransaction.commit();

                break;

            case R.id.forgotPassword:
//                Intent intent = new Intent(getActivity(), ForgotPasswordActivity.class);
//                startActivity(intent);
                navToForgotPasswordFragment();
                break;

            case R.id.loginToApp:
                String fcmToken = AppConfig.getInstance().loadFCMToken();
                if (fcmToken.equalsIgnoreCase("")){
                    fcmToken = com.google.firebase.iid.FirebaseInstanceId.getInstance().getToken();
                    AppConfig.getInstance().saveFCMToken(fcmToken);
                }
                if (validatingRequired()) {
                    //utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.logging_your_account),false) }
                    progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.progress_dialog_logging_your_account), false);
                    requestSignIn(mUserEmail.getText().toString().trim(), strPIN, fcmToken);
                }
                break;
        }

    }
    private void navToMainActivity() {
        AppConfig.getInstance().isCommingFromSplash = true;
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }
    private void requestSignIn(String _email, String _pin, String _fcmToken) {
        Log.e("check","login11");
        SignIn_WebHit_Post_signIn signIn_webHit_post_signIn = new SignIn_WebHit_Post_signIn();
        signIn_webHit_post_signIn.requestSignIn(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                Log.e("login_success",isSuccess+"");
                progressDilogue.stopiOSLoader();
                Log.e("strmsg",strMsg);
                if (isSuccess) {


                 if (strMsg.equalsIgnoreCase("Phone no. verification pending.")){
                        navToVerifyMemberFragment();
                    }else if (strMsg.equalsIgnoreCase("User authenticated Successfully.")) {
//                     logFireBaseEvent();
//                     logFaceBookEvent();
//                     logMixPanelEvent();
                        AppConfig.getInstance().mUser.setLoggedIn(true);
                        navToMainActivity();
                    }
                }
                else {
                    Log.e("check_login_res","ressss");
                    customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_in_unsuccess_login_heading), strMsg,
                            null, null, false, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                Log.e("exxc","ex",ex);
                customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_in_unsuccess_login_heading), ex.getMessage(), null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();
            }
        }, _email, _pin, _fcmToken);
    }


    private void navToVerifyMemberFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment frg = new VerifyMemberFragment();
        ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
        ft.replace(R.id.containerIntroFragments, frg);
        ft.commit();
    }

    private boolean validatingRequired() {
        String message = "";
        String email = mUserEmail.getText().toString().trim();
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







