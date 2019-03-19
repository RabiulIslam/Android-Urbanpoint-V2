package com.urbanpoint.UrbanPoint.RegistrationAuxiliries;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.uber.sdk.android.core.auth.LoginActivity;
import com.urbanpoint.UrbanPoint.DrawerAuxiliries.ProfileFragment;
import com.urbanpoint.UrbanPoint.IntroActivity;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.PhoneRegistrationApi;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.SignUp_WebHit_Post_checkPhoneEmail;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.SignUp_WebHit_Post_verifyEmail;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.SignupActivity;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

import static android.app.Activity.RESULT_OK;

public class PhoneVerificationFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    // private SMSBroadcastReceiver receiver;
    ProgressDilogue progressDilogue;
    private EditText phoneNumberCodeInput, phoneNumberInput;
    private Button confirmButton, confirmButtonSMSCode;
    private Button resendSms, changeNumber, skip;
    private ViewSwitcher viewSwitcher;
    private CountryCodePicker ccp;
    CustomAlert customAlert;
    private int RC_HINT = 2;
    private GoogleApiClient apiClient;
    String phoneNo;

    public PhoneVerificationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_verification, null);
        progressDilogue = new ProgressDilogue();
        customAlert = new CustomAlert();
        viewSwitcher = (ViewSwitcher) view.findViewById(R.id.viewSwitcher);
        changeNumber = (Button) view.findViewById(R.id.btnchangenumber);
        ccp = (CountryCodePicker) view.findViewById(R.id.ccp);
        skip = (Button) view.findViewById(R.id.skip);
        phoneNumberInput = (EditText) view.findViewById(R.id.editTextDialogPhoneInput);
        phoneNumberCodeInput = (EditText) view.findViewById(R.id.editTextDialogSMSCode);
        confirmButton = (Button) view.findViewById(R.id.btnphoneconfirm);
        confirmButtonSMSCode = (Button) view.findViewById(R.id.btnphoneconfirmSMSCode);
        resendSms = (Button) view.findViewById(R.id.btnresend);

        //Changes by Rashmi VPN
        /*Set phone number*/
        String phNo = AppConfig.getInstance().mUser.getmPhoneNumber();
        Log.e("phoneNo======", phNo);

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            // phone must begin with '+'
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse("+" + phNo, "");
            String countryCode = String.valueOf(numberProto.getCountryCode());
            Log.e("countryCode", countryCode + "");
            if (phNo.startsWith(countryCode)) {
//                Log.e("country===", phNo.indexOf(countryCode) + "");
                Log.e("country===", countryCode.length() + "");
                phoneNo = phNo.substring(countryCode.length());
                Log.e("phone===", phoneNo);
            }
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }

        if (phNo != null) {
            phoneNumberInput.setText(phoneNo);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(phoneNumberInput.getText().toString())) {

                    //Changes by Rashmi VPN
                    String phoneNumber = ccp.getSelectedCountryCode() + phoneNumberInput.getText().toString();
                    Log.e("phone number", "" + phoneNumber);

                    AppConfig.getInstance().mUser.setmPhoneNumber(phoneNumber);
                    AppConfig.getInstance().saveUserData();

                    sendPhoneNo();
                    viewSwitcher.showNext();
                }
            }
        });
        resendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(phoneNumberInput.getText().toString())) {
                    sendPhoneNo();
                }
            }
        });
        phoneNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirmButton.setEnabled(true);
                confirmButton.setFocusable(true);
                confirmButton.setClickable(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmButtonSMSCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sms_code = phoneNumberCodeInput.getText().toString();
                if (sms_code.isEmpty()) {
                    Toast.makeText(getActivity(),
                            R.string.phone_number_activity_code, Toast.LENGTH_LONG).show();
                } else {
                    confirmCode(sms_code);
                }
            }
        });
        changeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSwitcher.showPrevious();
                resendSms.setVisibility(View.GONE);
                changeNumber.setVisibility(View.GONE);
                skip.setVisibility(View.GONE);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navToMainActivity();
            }
        });
        if (apiClient != null) {
            apiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.CREDENTIALS_API)
                    .build();
        }
        return view;
    }

    // Obtain the phone number from the result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                phoneNumberInput.setText(credential.getId());
            }
        }
    }

    private void sendPhoneNo() {
        phoneNumberCodeInput.setText("");
        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        PhoneRegistrationApi phoneRegistrationApi = new PhoneRegistrationApi();
        phoneRegistrationApi.sendPhoneNumber(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                Log.e("register_res_boolean", isSuccess + "," + strMsg);
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    resendSms.setVisibility(View.VISIBLE);
                    changeNumber.setVisibility(View.VISIBLE);
                    skip.setVisibility(View.VISIBLE);
                } else {
                    resendSms.setVisibility(View.VISIBLE);
                    changeNumber.setVisibility(View.VISIBLE);
                    skip.setVisibility(View.VISIBLE);
                    customAlert.showCustomAlertDialog(getContext(), getString(R.string.sent_otp_code), strMsg,
                            null, null, false, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                Log.e("ex", "ex", ex);
                progressDilogue.stopiOSLoader();
            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();
            }
        }, getPhoneNumber());
    }

    private void confirmCode(String code) {

        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        PhoneRegistrationApi phoneRegistrationApi = new PhoneRegistrationApi();
        phoneRegistrationApi.confirmCode(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                Log.e("register_res_boolean", isSuccess + "," + strMsg);
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    updatePhoneRecords();
                } else {
                    customAlert.showCustomAlertDialog(getContext(), getString(R.string.otp_error_code), getString(R.string.invalid_otp_code),
                            null, null, false, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                Log.e("ex", "ex", ex);
                progressDilogue.stopiOSLoader();
            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();
            }
        }, getPhoneNumber(), code);
    }

    private void updatePhoneRecords() {

        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        SignUp_WebHit_Post_checkPhoneEmail signUp_webHit_checkPhone = new SignUp_WebHit_Post_checkPhoneEmail();
        signUp_webHit_checkPhone.requestcheckPhoneEmail(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                Log.e("register_res_boolean", isSuccess + "," + strMsg);
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
//                    logFireBaseEvent();
//                    logFaceBookEvent();
//                    logMixPanelEvent();
                    if (SignUp_WebHit_Post_checkPhoneEmail.responseObject != null) {
                        Bundle b = new Bundle();
                        b.putString(AppConstt.BundleStrings.userId, AppConfig.getInstance().mUser.getmUserId());
                        AppConfig.getInstance().isCommingFromSplash = true;
                        AppConfig.getInstance().mUser.setLoggedIn(true);
                        AppConfig.getInstance().mUser.setPhoneVerified("1");
                        AppConfig.getInstance().mUser.setPhnoVerified("1");
                        AppConfig.getInstance().saveUserData();
                        navToMainActivity();
                    } else {
                        navToMainActivity();
                    }
                } else {

                    if (strMsg.equalsIgnoreCase("Conflict")) {
                        customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), getResources().getString(R.string.already_registered), null, null, false, null);
                    } else {
                        customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), strMsg, null, null, false, null);
                    }
                }
            }

            @Override
            public void onWebException(Exception ex) {
                Log.e("ex", "ex", ex);
                progressDilogue.stopiOSLoader();
                customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_in_unsuccess_login_heading), ex.getMessage(), null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                Log.e("log", "out");
                progressDilogue.stopiOSLoader();

            }
        }, getPhoneNumber());
    }

    private void navToMainActivity() {
        if (getActivity() instanceof SignupActivity) {
            AppConfig.getInstance().isCommingFromSplash = true;
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
            getActivity().finish();
        } else {
            navToProfileFragment();
        }
    }

    public void navToProfileFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment frg = new ProfileFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.ProfileFragment);
        ft.commit();
    }

    private String getPhoneNumber() {
        String phoneNumber = ccp.getSelectedCountryCode() + phoneNumberInput.getText().toString();
        Log.e("phone number", "" + phoneNumber);

//        AppConfig.getInstance().mUser.setmPhoneNumber(phoneNumber);
//        AppConfig.getInstance().saveUserData();
        return phoneNumber;
    }

    private void smsRetriverListener() {
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        SmsRetrieverClient client = SmsRetriever.getClient(getActivity() /* context */);

        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();

        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
                Log.e("SMS", "Started");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
                Log.e("SMS", "Failed");
            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}