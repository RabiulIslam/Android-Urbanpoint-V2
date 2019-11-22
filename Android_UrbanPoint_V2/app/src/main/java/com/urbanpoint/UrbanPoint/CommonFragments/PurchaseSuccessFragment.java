package com.urbanpoint.UrbanPoint.CommonFragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
//
//import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
//import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Danish on 3/7/2018.
 */

public class PurchaseSuccessFragment extends Fragment implements View.OnClickListener {
    TextView txvOfferName, txvMerchantName, txvAddress, txvDate, txvPurcahsePin, txvGoToHome;
    Button btnRateExperience;
    String strOfferName, strMerchantName, strMerchantAddress, strConfirmationPIN, strDate,strOfferId,strMerchantPIN,strMerchantId,strOrderId;
    Bundle bundle;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_purchase_success, container, false);
        initiate();

        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        if (strOfferName.length() > 0) {
            iNavBarUpdateUpdateListener.setNavBarTitle(strOfferName);
        } else {
            iNavBarUpdateUpdateListener.setNavBarTitle(getResources().getString(R.string.frg_purchase_success));
        }
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        bindViews(v);

//        MixpanelAPI mixpanel = MixpanelAPI.getInstance(getContext(), MIXPANEL_TOKEN);

        JSONObject props = null;
        try {
            props = new JSONObject();
            props.put("Order ID", strOrderId);
            props.put("User ID", AppConfig.getInstance().mUser.getmUserId());
            props.put("User Name", AppConfig.getInstance().mUser.getmName());
            props.put("User Email", AppConfig.getInstance().mUser.getmEmail());
            props.put("Merchant ID", strMerchantId);
            props.put("Merchant Name", strMerchantName);
            props.put("Merchant Address", strMerchantAddress);
            props.put("Merchant Pin", strMerchantPIN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        mixpanel.track("Offer Used Confirmation screen", props);

        logFireBaseEvent();
//        logFaceBookEvent();

        return v;
    }

    private void initiate() {
        strOfferName = "";
        strMerchantName = "";
        strMerchantAddress = "";
        strConfirmationPIN = "";
        strDate = "";
        strOfferId = "";
        strMerchantPIN = "";
        strMerchantId = "";
        strOrderId = "";
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            strOfferName = bundle.getString(AppConstt.BundleStrings.offerName);
            strMerchantName = bundle.getString(AppConstt.BundleStrings.merchantName);
            strMerchantAddress = bundle.getString(AppConstt.BundleStrings.merchantAddress);
            strConfirmationPIN = bundle.getString(AppConstt.BundleStrings.purchaseSuccessPIN);
            strOfferId=bundle.getString(AppConstt.BundleStrings.offerId);
            strMerchantPIN= bundle.getString(AppConstt.BundleStrings.merchantPIN);
            strMerchantId= bundle.getString(AppConstt.BundleStrings.merchantId);
            strOrderId= bundle.getString(AppConstt.BundleStrings.orderId);
        }
    }

    void bindViews(View v) {
        txvOfferName = v.findViewById(R.id.frg_purchase_sucess_txv_offr);
        txvMerchantName = v.findViewById(R.id.frg_purchase_sucess_txv_merchnt);
        txvAddress = v.findViewById(R.id.frg_purchase_sucess_txv_outlet);
        txvPurcahsePin = v.findViewById(R.id.frg_purchase_sucess_txv_confirmation_code);
        txvDate = v.findViewById(R.id.frg_purchase_sucess_txv_date);
        txvGoToHome = v.findViewById(R.id.frg_purchase_sucess_txv_goto_home);
        btnRateExperience = v.findViewById(R.id.frg_purchase_sucess_btn_rate);


        txvOfferName.setText(strOfferName);
        txvMerchantName.setText(strMerchantName);
        txvAddress.setText(strMerchantAddress);
        txvPurcahsePin.setText(strConfirmationPIN);
        txvDate.setText(getCurrentDateToDisplay());

        btnRateExperience.setOnClickListener(this);
        txvGoToHome.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_purchase_sucess_txv_goto_home:
                navToHomeFragment(false);
                break;
            case R.id.frg_purchase_sucess_btn_rate:
                navToHomeFragment(true);
                break;

        }
    }

    void navToHomeFragment(boolean _shouldNavToReview) {
        AppConfig.getInstance().shouldNavToReview = _shouldNavToReview;
        ((MainActivity) getActivity()).navToHomeFragment();
    }

    private String getCurrentDateToDisplay() {
        String data = getString(R.string.offer_redeem_success_time);
        DateFormat df = new SimpleDateFormat("MMM yyyy");
        DateFormat df2 = new SimpleDateFormat("hh:mm aa");
        Date dateobj = new Date();
        Calendar instance = Calendar.getInstance();
        int day = instance.get(Calendar.DATE);
        String ordinalSuffix = getOrdinalSuffix(day);
        String resposne = String.format(day + ordinalSuffix + ", " + df.format(dateobj) + " at " + df2.format(dateobj));

        return resposne;

    }

    public static String getOrdinalSuffix(int value) {
        int hunRem = value % 100;
        int tenRem = value % 10;

        if (hunRem - tenRem == 10) {
            return "th";
        }
        switch (tenRem) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    private void logFireBaseEvent() {
        Bundle params = new Bundle();
        params.putString("user_id", AppConfig.getInstance().mUser.getmUserId());
        params.putString("device_type", "Android");
        // Send the event
        FirebaseAnalytics.getInstance(getActivity())
                .logEvent(AppConstt.FireBaseEvents.Successful_Redemptions, params);
    }

//    private void logFaceBookEvent() {
//        AppEventsLogger.newLogger(getActivity()).logEvent(AppConstt.FireBaseEvents.Successful_Redemptions);
//    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            if (strOfferName.length() > 0) {
                iNavBarUpdateUpdateListener.setNavBarTitle(strOfferName);
            } else {
                iNavBarUpdateUpdateListener.setNavBarTitle(getResources().getString(R.string.frg_purchase_success));
            }
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }
    }
}
