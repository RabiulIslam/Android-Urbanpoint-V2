package com.urbanpoint.UrbanPoint.SubscriptionAuxiliries;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.Signup_WEbhit_Post_validateCode;
import com.urbanpoint.UrbanPoint.SubscriptionAuxiliries.WebServices.Subscribe_WebHit_Post_subscribe;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.SignUp_WebHit_Get_sendMT;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.PinEntry;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

/**
 * Created by Danish on 2/19/2018.
 */

public class SubscriptionConfirmFragment extends Fragment implements View.OnClickListener {
    private Button btnConfirm;
    private TextView txvTryAgain;
    private PinEntry mPinEntry;
    private String enteredPin, serverPin, subscriberPhone;
    private ProgressDilogue progressDilogue;
    private LinearLayout llParentLayout;
    private CustomAlert customAlert;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subscription_confirmation, container, false);
        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_subscription_for_15));
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        initialize();
        bindViews(v);

        return v;
    }

    private void initialize() {
        enteredPin = serverPin = subscriberPhone = "";
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();

        Bundle b = this.getArguments();
        if (b != null) {
            serverPin = b.getString(AppConstt.BundleStrings.cnfirmationPin);
            subscriberPhone = b.getString(AppConstt.BundleStrings.subscribeMsisdn);
        }
    }

    void bindViews(View v) {
        llParentLayout = v.findViewById(R.id.frg_sub_cnfrm_ll_parentlayout);
        txvTryAgain = v.findViewById(R.id.frg_sub_cnfrm_txv_try_again);
        mPinEntry = v.findViewById(R.id.frg_sub_cnfrm_pin_entry_code);
        btnConfirm = v.findViewById(R.id.frg_sub_cnfrm_btn_cnfrm);

        txvTryAgain.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        llParentLayout.setOnClickListener(this);


        mPinEntry.setOnPinEnteredListener(new PinEntry.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                if (pin.length() == 6) {
                    enteredPin = pin;
                    AppConfig.getInstance().closeKeyboard(getActivity());
                } else {
                    enteredPin = "";
                }
            }
        });

    }

    private void requestValidateCode(String _phone, String _code) {
        Signup_WEbhit_Post_validateCode signup_wEbhit_post_validateCode = new Signup_WEbhit_Post_validateCode();
        signup_wEbhit_post_validateCode.requestValidateCode(getActivity(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {

                    if (Signup_WEbhit_Post_validateCode.responseObject.getMessage().equals("matched")) {

                        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
                        requestSubscribe(subscriberPhone);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_sub_cnfrm_btn_cnfrm:
                if (validatingRequired()) {
                    progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);

                    requestValidateCode(subscriberPhone, enteredPin);

                }

                break;
            case R.id.frg_sub_cnfrm_txv_try_again:
                progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
                requestResendMT(subscriberPhone);
                break;

            case R.id.frg_sub_cnfrm_ll_parentlayout:
                AppConfig.getInstance().closeKeyboard(getActivity());
                break;
        }
    }

    private void navToSubscriptionSuccessFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new SubscriptionSuccessFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.SubscriptionSuccessFragment);
        ft.addToBackStack(AppConstt.FRGTAG.SubscriptionSuccessFragment);
        ft.hide(this);
        ft.commit();
    }

    private void navToSubscriptionEligibleSuccessFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new SubscriptionEligibleSuccessFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.SubscriptionEligibleSuccessFragment);
        ft.addToBackStack(AppConstt.FRGTAG.SubscriptionEligibleSuccessFragment);
        ft.hide(this);
        ft.commit();
    }

    private void requestSubscribe(String _phone) {
        Subscribe_WebHit_Post_subscribe subscribe_webHit_post_subscribe = new Subscribe_WebHit_Post_subscribe();
        subscribe_webHit_post_subscribe.requestsubscribeUser(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    if (AppConfig.getInstance().mUser.isEligible()) {
                        navToSubscriptionEligibleSuccessFragment();
                    } else {
                        navToSubscriptionSuccessFragment();
                    }
                } else {
                    customAlert.showCustomAlertDialog(getActivity(), null, strMsg, null, null, false, null);
                }

            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                customAlert.showCustomAlertDialog(getActivity(), null, ex.getMessage(), null, null, false, null);

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
    private void logFaceBookEvent() {
        AppEventsLogger.newLogger(getActivity()).logEvent(AppConstt.FireBaseEvents.OTP_Generation);
    }
    private void requestResendMT(String _phone) {
        SignUp_WebHit_Get_sendMT signUp_webHit_get_sendMT = new SignUp_WebHit_Get_sendMT();
        signUp_webHit_get_sendMT.requestResendCode(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    logFireBaseEvent();
                    logFaceBookEvent();
                    if (SignUp_WebHit_Get_sendMT.responseObject != null) {
                        serverPin = SignUp_WebHit_Get_sendMT.responseObject.getData();
                        Log.d("validateMsisdn", "onWebResult: " + serverPin);
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

    private boolean validatingRequired() {
        String message = "";

        //validate the content
        if (enteredPin.equalsIgnoreCase("")) {
            message = getResources().getString(R.string.enter_valid_pin_message);
            customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);

        } else if (enteredPin.length() < 4) {
            message = getResources().getString(R.string.enter_valid_pin_message);
            customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
        } else {
            /*if (!enteredPin.equalsIgnoreCase(serverPin)) {
            message = getResources().getString(R.string.enter_valid_pin_message);
            customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
            mPinEntry.clearText();*/
        }
        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_subscription_for_15));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }
    }
}
