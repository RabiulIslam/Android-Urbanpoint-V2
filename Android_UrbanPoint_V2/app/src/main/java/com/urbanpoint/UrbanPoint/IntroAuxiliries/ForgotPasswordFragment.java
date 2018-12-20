package com.urbanpoint.UrbanPoint.IntroAuxiliries;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.ForgotPassword_WebHit_Post_forgotPassword;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.RegistrationAuxiliries.LoginFragment;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.CustomAlertConfirmationInterface;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {
    private EditText edtEmail;
    private Button btnSend;
    private RelativeLayout rlBack;
    private boolean isMailSent;
    String emailID;
    CustomAlert customAlert;
    RelativeLayout Back;
    ProgressDilogue progressDilogue;
    CustomAlertConfirmationInterface customAlertConfirmationInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, null);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initialize();
        bindViews(view);
        return view;
    }

    private void initialize() {
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        customAlertConfirmationInterface = new CustomAlertConfirmationInterface() {
            @Override
            public void callConfirmationDialogPositive() {
                if (isMailSent) {
                   // getFragmentManager().popBackStackImmediate();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                    fragmentTransaction.replace(R.id.containerIntroFragments, new LoginFragment());
                    fragmentTransaction.commit();
                } else {
                    edtEmail.setText("");
                }
            }

            @Override
            public void callConfirmationDialogNegative() {
            }
        };
        isMailSent = false;
    }


    private void bindViews(View frg) {
        rlBack = frg.findViewById(R.id.frg_forgot_paswrd_rl_back);
        edtEmail = frg.findViewById(R.id.frg_forgot_password_edt_email);
        btnSend = frg.findViewById(R.id.frg_forgot_password_btn_send);

        btnSend.setOnClickListener(this);
        rlBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_forgot_password_btn_send:
                if (validatingRequired()) {
                    progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
                    requestForgotPassword(edtEmail.getText().toString());
                }
                break;
            case R.id.frg_forgot_paswrd_rl_back:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                fragmentTransaction.replace(R.id.containerIntroFragments, new LoginFragment());
                fragmentTransaction.commit();
                //getFragmentManager().popBackStackImmediate();
                break;
        }
    }

    private void requestForgotPassword(String _email) {
        ForgotPassword_WebHit_Post_forgotPassword forgotPassword_webHit_post_forgotPassword = new ForgotPassword_WebHit_Post_forgotPassword();
        forgotPassword_webHit_post_forgotPassword.requestForgotPassword(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                customAlert.showCustomAlertDialog(getContext(), null, strMsg, null, null, false, customAlertConfirmationInterface);
                if (isSuccess) {
                    isMailSent = true;
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                customAlert.showCustomAlertDialog(getContext(), null, ex.getMessage(), null, null, false, customAlertConfirmationInterface);

            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();

            }
        }, _email);
    }

    private boolean validatingRequired() {
        String message = "";
        String email = edtEmail.getText().toString();
       /*  String enteredPin = enteredNewPin;

        String confirmPin = enteredConfirmPin;*/

        //validate the content
        if (email.isEmpty()) {
            message = getResources().getString(R.string.EmailErrorMessage);
            //utilObj.showError(this, message, textViewObj, emailObj);
            customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_up_enter_account_setup_heading), message, null, null, false, null);
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


}
