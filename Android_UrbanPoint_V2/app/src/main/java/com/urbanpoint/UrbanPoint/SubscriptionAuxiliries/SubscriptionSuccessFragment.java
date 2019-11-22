package com.urbanpoint.UrbanPoint.SubscriptionAuxiliries;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

//import com.facebook.appevents.AppEventsLogger;
//import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Danish on 2/19/2018.
 */

public class SubscriptionSuccessFragment extends Fragment implements View.OnClickListener {
    private Button btnLetsGo;
    private ImageView imvCross;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subscription_success, container, false);
        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_subscription_success));
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        AppConfig.getInstance().mUser.setSubscribed(true);
        AppConfig.getInstance().saveUserData();

        bindViews(v);

//        logFireBaseEvent();
//        logFaceBookEvent();
//
//        MixpanelAPI mixpanel = MixpanelAPI.getInstance(getContext(), MIXPANEL_TOKEN);
        JSONObject props = null;
        try {
            props = new JSONObject();
            props.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        mixpanel.track("Subscribe Confirmation screen", props);

        return v;
    }

    void bindViews(View v) {
        btnLetsGo = v.findViewById(R.id.frg_sub_success_btn_letsgo);
        imvCross = v.findViewById(R.id.frg_sub_success_btn_cross);

        imvCross.setOnClickListener(this);
        btnLetsGo.setOnClickListener(this);
     }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_sub_success_btn_letsgo:
                ((MainActivity)getActivity()).navToHomeFragment();
                break;
            case R.id.frg_sub_success_btn_cross:
                ((MainActivity)getActivity()).navToHomeFragment();
                break;
        }
    }

//    private void logFireBaseEvent() {
//        Bundle params = new Bundle();
//        params.putString("user_id", AppConfig.getInstance().mUser.getmUserId());
//        params.putString("device_type", "Android");
//        // Send the event
//        FirebaseAnalytics.getInstance(getActivity())
//                .logEvent(AppConstt.FireBaseEvents.Successful_Regular_Subscription, params);
//    }

//    private void logFaceBookEvent() {
//        AppEventsLogger.newLogger(getActivity()).logEvent(AppConstt.FireBaseEvents.Successful_Regular_Subscription);
//    }
    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_subscription_success));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }
    }
}
