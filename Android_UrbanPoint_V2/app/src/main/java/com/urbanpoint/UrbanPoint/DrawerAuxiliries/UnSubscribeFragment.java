package com.urbanpoint.UrbanPoint.DrawerAuxiliries;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.urbanpoint.UrbanPoint.DrawerAuxiliries.WebServices.UnSubscribe_Webhit_Post_unsubscribe;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CellNoEntryView;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.CustomAlertConfirmationInterface;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

import org.json.JSONException;
import org.json.JSONObject;

import static com.urbanpoint.UrbanPoint.Utils.AppConstt.MIXPANEL_TOKEN;

/**
 * Created by Danish on 2/19/2018.
 */

public class UnSubscribeFragment extends Fragment implements View.OnClickListener {
    private Button btnUnSub;
    private CellNoEntryView mPhone;
    private String enteredPhone = "";
    private ProgressDilogue progressDilogue;
    private CustomAlert customAlert;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_unsubscribe, container, false);
        bindViews(v);
        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_un_subscription));
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        mPhone.setOnPinEnteredListener(new CellNoEntryView.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                if (pin.length() == 8) {
                    enteredPhone = AppConstt.DEFAULT_VALUES.COUNTRY_CODE + pin;
                } else {
                    enteredPhone = "";
                }
            }
        });

        return v;
    }

    void bindViews(View v) {
        btnUnSub = v.findViewById(R.id.frg_unsub_btn_unsub);
        mPhone = v.findViewById(R.id.frg_unsub_pinentry_phone);

        btnUnSub.setOnClickListener(this);
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();

    }

    private boolean validatingRequired() {
        String message = "";

        //validate the content
        if (enteredPhone.length() < 8) {
            message = getResources().getString(R.string.enter_mobile_number);
            customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
        } else if (!enteredPhone.equalsIgnoreCase(AppConfig.getInstance().mUser.getmPhoneNumber())) {
            message = getResources().getString(R.string.enter_subscribed_mobile_number);
            customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
        }
        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_unsub_btn_unsub:
                if (validatingRequired()) {
                    progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
                    requestUnSubscribe(enteredPhone);
                }
                break;
        }
    }

    private void logFireBaseEvent() {
        Bundle params = new Bundle();
        params.putString("user_id", AppConfig.getInstance().mUser.getmUserId());
        params.putString("device_type", "Android");
        // Send the event
        FirebaseAnalytics.getInstance(getActivity())
                .logEvent(AppConstt.FireBaseEvents.Manual_Unsubscription, params);
    }

    private void logFaceBookEvent() {
        AppEventsLogger.newLogger(getActivity()).logEvent(AppConstt.FireBaseEvents.Manual_Unsubscription);
    }

    private void requestUnSubscribe(String _phone) {
        UnSubscribe_Webhit_Post_unsubscribe unSubscribe_webhit_post_unsubscribe = new UnSubscribe_Webhit_Post_unsubscribe();
        unSubscribe_webhit_post_unsubscribe.requestUnSubsribe(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    AppConfig.getInstance().mUser.setmCanUnSubscribe(false);
                    MixpanelAPI mixpanel = MixpanelAPI.getInstance(getContext(), MIXPANEL_TOKEN);

                    JSONObject props = null;
                    try {
                        props = new JSONObject();
                        props.put("", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mixpanel.track("Unsubscribe confirmation", props);

                    logFireBaseEvent();
                    logFaceBookEvent();
                    customAlert.showCustomAlertDialog(getActivity(), null, getString(R.string.unsub_successfully), null, null, false, new CustomAlertConfirmationInterface() {
                        @Override
                        public void callConfirmationDialogPositive() {
                            getFragmentManager().popBackStackImmediate();
                        }

                        @Override
                        public void callConfirmationDialogNegative() {

                        }
                    });
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

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_un_subscription));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }
    }
}
