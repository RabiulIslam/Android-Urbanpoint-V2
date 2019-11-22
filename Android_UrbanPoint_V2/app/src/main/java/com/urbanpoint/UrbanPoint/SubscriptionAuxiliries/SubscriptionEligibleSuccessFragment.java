package com.urbanpoint.UrbanPoint.SubscriptionAuxiliries;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.ExpandableHeightGridView;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danish on 2/19/2018.
 */

public class SubscriptionEligibleSuccessFragment extends Fragment implements View.OnClickListener {
    private Button btnConfirm;
    private ExpandableHeightGridView lsv2;
    private LinearLayout llParentLayout;
    private List lstStrings;
    private SubscribeTextAdapter2 subscribeTextAdapter;
  
    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subscription_eligible_success, container, false);
        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_subscription_success));
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        initialize();
        bindViews(v);
        logFireBaseEvent();
        updateSubscriptionLists();
        return v;
    }

    private void updateSubscriptionLists() {
       
        lstStrings = new ArrayList();
        lstStrings.add(getString(R.string.subscription_eligible_success_subscribed));
        lstStrings.add(getString(R.string.subscription_eligible_success_enjoy));
        subscribeTextAdapter = new SubscribeTextAdapter2(getContext(), lstStrings, false);
        lsv2.setExpanded(true);
        lsv2.setAdapter(subscribeTextAdapter);
    }

    private void initialize() {
        lstStrings = new ArrayList();
         AppConfig.getInstance().isSpaceRequiredToShow = false;

    }

    void bindViews(View v) {
        llParentLayout = v.findViewById(R.id.frg_subscription_eligible_success_ll_parentlayout);
        lsv2 = v.findViewById(R.id.frg_subscription_eligible_success_lsv_2);
        btnConfirm = v.findViewById(R.id.frg_subscription_eligible_success_btn_cnfrm);
       
        llParentLayout.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_subscription_eligible_success_btn_cnfrm:
                ((MainActivity)getActivity()).navToHomeFragment();
                break;


            case R.id.frg_subscription_eligible_success_ll_parentlayout:
                AppConfig.getInstance().closeKeyboard(getActivity());
                break;
        }
    }

    private void logFireBaseEvent() {
        Bundle params = new Bundle();
        params.putString("user_id", AppConfig.getInstance().mUser.getmUserId());
        params.putString("device_type", "Android");
        // Send the event
        FirebaseAnalytics.getInstance(getActivity())
                .logEvent(AppConstt.FireBaseEvents.Successful_Bundled_Subscription, params);
    }

    private void navToSubscriptionFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new SubscriptionFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.SubscriptionFragment);
        ft.addToBackStack(AppConstt.FRGTAG.SubscriptionFragment);
        ft.hide(this);
        ft.commit();

    }

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
