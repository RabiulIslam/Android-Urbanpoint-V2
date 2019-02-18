package com.urbanpoint.UrbanPoint.RegistrationAuxiliries;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.hbb20.CountryCodePicker;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.PhoneRegistrationApi;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.SignUp_WebHit_Post_checkPhoneEmail;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.SignUp_WebHit_Post_verifyEmail;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

public class PhoneVerificationFragment extends Fragment implements View.OnClickListener {


    private SMSBroadcastReceiver receiver;
    ProgressDilogue progressDilogue;
    private CountryCodePicker ccp;
    private EditText phoneNumberInput, phoneNumberCodeInput;
    private Button confirmButton, confirmButtonSMSCode;
    private Button resendSms, changeNumber;
    private ViewSwitcher viewSwitcher;
    TextView codeMessage;
    CustomAlert customAlert;
    public PhoneVerificationFragment() {
        // Required empty public constructor
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get SMS message contents
            String code = (String) intent.getExtras().get(AppConstt.EXTRAS.OTP_CODE);
            phoneNumberCodeInput.setText(code);
        }
    };


    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        getActivity().registerReceiver(receiver, intentFilter);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(AppConstt.ACTIONS.SMS_RECEIVED));
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
        phoneNumberInput = (EditText) view.findViewById(R.id.editTextDialogPhoneInput);
        phoneNumberCodeInput = (EditText) view.findViewById(R.id.editTextDialogSMSCode);
        confirmButton = (Button) view.findViewById(R.id.btnphoneconfirm);
        confirmButtonSMSCode = (Button) view.findViewById(R.id.btnphoneconfirmSMSCode);
        resendSms = (Button) view.findViewById(R.id.btnresend);
        codeMessage = view.findViewById(R.id.info_dialog_summary);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(phoneNumberInput.getText().toString())){
                    sendPhoneNo();
                    viewSwitcher.showNext();
                }
            }
        });
        resendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(phoneNumberInput.getText().toString())){
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
            }
        });

        receiver = new SMSBroadcastReceiver();
        smsRetriverListener();
        return view;
    }
    private void sendPhoneNo() {

        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        PhoneRegistrationApi phoneRegistrationApi = new PhoneRegistrationApi();
        phoneRegistrationApi.sendPhoneNumber(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                Log.e("register_res_boolean",isSuccess+","+strMsg);
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    resendSms.setVisibility(View.VISIBLE);
                    changeNumber.setVisibility(View.VISIBLE);
                }else{
                    viewSwitcher.showNext();
                    resendSms.setVisibility(View.VISIBLE);
                    changeNumber.setVisibility(View.VISIBLE);
                    customAlert.showCustomAlertDialog(getContext(), getString(R.string.sent_otp_code), strMsg,
                            null, null, false, null);
                }
            }
            @Override
            public void onWebException(Exception ex) {
                Log.e("ex","ex",ex);
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
                Log.e("register_res_boolean",isSuccess+","+strMsg);
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    updatePhoneRecords();
                }else{
                    customAlert.showCustomAlertDialog(getContext(), getString(R.string.otp_error_code), getString(R.string.invalid_otp_code),
                            null, null, false, null);
                }
            }
            @Override
            public void onWebException(Exception ex) {
                Log.e("ex","ex",ex);
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
                Log.e("register_res_boolean",isSuccess+","+strMsg);
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
//                    logFireBaseEvent();
//                    logFaceBookEvent();
//                    logMixPanelEvent();
                    if (SignUp_WebHit_Post_checkPhoneEmail.responseObject != null)
                    {
                        Bundle b = new Bundle();
                        b.putString(AppConstt.BundleStrings.userId, AppConfig.getInstance().mUser.getmUserId());
                        AppConfig.getInstance().isCommingFromSplash = true;
                        AppConfig.getInstance().mUser.setLoggedIn(true);
                        //  navToMainActivity();
                        showVerifyEmailDialog();
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
                customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_in_unsuccess_login_heading), ex.getMessage(), null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                Log.e("log","out");
                progressDilogue.stopiOSLoader();

            }
        }, getPhoneNumber());
    }
    private void showVerifyEmailDialog()
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
                navToMainActivity();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyEmail();
            }
        });
        messageTitle.setText(getString(R.string.verify_email));
        messageText.setText(getString(R.string.send_link_to_verify_email));
        dialog.show();

    }
    private void verifyEmail() {

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
                        navToMainActivity();
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
    private void navToMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }
    private String getPhoneNumber() {
        String phoneNumber = ccp.getSelectedCountryCodeAsInt() + phoneNumberInput.getText().toString();
        return phoneNumber;
    }
    private void smsRetriverListener(){
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
}