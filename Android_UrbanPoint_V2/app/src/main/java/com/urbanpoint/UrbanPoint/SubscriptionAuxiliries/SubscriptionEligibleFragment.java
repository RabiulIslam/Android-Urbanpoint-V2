package com.urbanpoint.UrbanPoint.SubscriptionAuxiliries;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.HomeAuxiliries.WebServices.Home_WebHit_Post_homeApi;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.ExpandableHeightGridView;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danish on 2/19/2018.
 */

public class SubscriptionEligibleFragment extends Fragment implements View.OnClickListener {
    private Button btnConfirm, btnAccessCode;
    private String enteredMobileNumber = "";
    private ExpandableHeightGridView lsv1;
    private ExpandableHeightGridView lsv2;
    private LinearLayout llParentLayout;
    private List lstStrings;
    private boolean shouldCheckSubscription;
    private SubscribeTextAdapter subscribeTextAdapter;
    private ProgressDilogue progressDilogue;
    private CustomAlert customAlert;
    private HtmlTextView txvSubsText, txvSubsText2;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subscription_eligible, container, false);
        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_subscription_eligible));
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        initialize();
        bindViews(v);
        updateSubscriptionLists();
        return v;
    }

    private void updateSubscriptionLists() {
        lstStrings = new ArrayList();
        lstStrings.add(getString(R.string.subscription_eligible_screen_para_1_string_1));
        lstStrings.add(getString(R.string.subscription_eligible_screen_para_1_string_2));
        lstStrings.add(getString(R.string.subscription_eligible_screen_para_1_string_3));
        subscribeTextAdapter = new SubscribeTextAdapter(getContext(), lstStrings, true);
        lsv1.setExpanded(true);
        lsv1.setAdapter(subscribeTextAdapter);

        txvSubsText.setHtml("<ul style=\"color:#722B4C; font-size: 20px\">\n" +
                "<li>Access 1500 Offers in Qatar!</li>\n" +
                "<li>Use every offer, every month!</li>\n" +
                "<li>Save Money, Live Better!</li>\n" +
                "</ul>", new HtmlResImageGetter(txvSubsText));

//        lsv1.getLayoutParams().width =(int)(getWidestView(getActivity(), subscribeTextAdapter)*1.5);

        lstStrings = new ArrayList();
        lstStrings.add(getString(R.string.subscription_eligible_screen_para_2_string_1));
        lstStrings.add(getString(R.string.subscription_eligible_screen_para_2_string_2));
        lstStrings.add(getString(R.string.subscription_eligible_screen_para_2_string_3));
        lstStrings.add(getString(R.string.subscription_eligible_screen_para_2_string_4));
        subscribeTextAdapter = new SubscribeTextAdapter(getContext(), lstStrings, false);
        lsv2.setExpanded(true);
        lsv2.setAdapter(subscribeTextAdapter);

        txvSubsText2.setHtml("Subscribe for FREE in 3 easy steps!<br>\n" +
                "1)  Log into your Ooredoo App<br>\n" +
                "2) Click on Deals<br>\n" +
                "3) Get your FREE subscription!", new HtmlResImageGetter(txvSubsText));
    }

    /**
     * Computes the widest view in an adapter, best used when you need to wrap_content on a ListView, please be careful
     * and don't use it on an adapter that is extremely numerous in items or it will take a long time.
     *
     * @param context Some context
     * @param adapter The adapter to process
     * @return The pixel width of the widest View
     */
    public static int getWidestView(Context context, Adapter adapter) {
        int maxWidth = 0;
        View view = null;
        FrameLayout fakeParent = new FrameLayout(context);
        for (int i = 0, count = adapter.getCount(); i < count; i++) {
            view = adapter.getView(i, view, fakeParent);
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int width = view.getMeasuredWidth();
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        return maxWidth;
    }

    private void initialize() {
        lstStrings = new ArrayList();
        shouldCheckSubscription = false;
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        AppConfig.getInstance().isEligible = false;
//        AppConfig.getInstance().isSpaceRequiredToShow = true;

    }

    void bindViews(View v) {
        llParentLayout = v.findViewById(R.id.frg_subscription_eligible_ll_parentlayout);
        lsv1 = v.findViewById(R.id.frg_subscription_eligible_lsv_1);
        lsv2 = v.findViewById(R.id.frg_subscription_eligible_lsv_2);
        btnConfirm = v.findViewById(R.id.frg_subscription_eligible_btn_cnfrm);
        btnAccessCode = v.findViewById(R.id.frg_subscription_eligible_btn_access_code);
        txvSubsText = (HtmlTextView) v.findViewById(R.id.txv_subs_text);
        txvSubsText2 =(HtmlTextView) v.findViewById(R.id.txv_subs_text2);

        llParentLayout.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnAccessCode.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_subscription_eligible_btn_cnfrm:
//                navToSubscriptionEligibleSuccessFragment();

                boolean isAppInstalled = appInstalledOrNot("qa.ooredoo.android");
                if (isAppInstalled) {
                    //This intent will help you to launch if the package is already installed
                    shouldCheckSubscription = true;
                    Intent LaunchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("qa.ooredoo.android");
                    startActivity(LaunchIntent);
//                    startActivity(new Intent(Intent.ACTION_VIEW, mDeepLink.getUri()));
                    Log.d("APPINSTALLED", "Application is already installed.");
                } else {
                    // Do whatever we want to do if application not installed
                    // Redirect to play store
                    shouldCheckSubscription = true;
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=qa.ooredoo.android"));
                    startActivity(i);
                    Log.d("APPINSTALLED", "Application is not currently installed.");
                }
                break;

            case R.id.frg_subscription_eligible_btn_access_code:
                shouldCheckSubscription = false;
                navToSubscriptionFragment();
                break;

            case R.id.frg_subscription_eligible_ll_parentlayout:
                AppConfig.getInstance().closeKeyboard(getActivity());
                break;
        }
    }

    private void navToSubscriptionFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new SubscriptionFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.SubscriptionFragment);
        ft.addToBackStack(AppConstt.FRGTAG.SubscriptionFragment);
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

    private void requestHomeApi() {
        Home_WebHit_Post_homeApi home_webHit_post_homeApi = new Home_WebHit_Post_homeApi();
        home_webHit_post_homeApi.requestHomeApi(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    if (AppConfig.getInstance().mUser.isSubscribed()) {
                        navToSubscriptionEligibleSuccessFragment();
                    }
                } else {
                    if (customAlert != null)
                        customAlert.showCustomAlertDialog(getActivity(), null, strMsg, null, null, false, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                if (customAlert != null)
                    customAlert.showCustomAlertDialog(getActivity(), null, ex.getMessage(), null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();
                AppConfig.getInstance().deleteUserData(AppConfig.getInstance().loadFCMToken());
//                customAlert.showCustomAlertDialog(getActivity(), null, getString(R.string.frg_access_code), null, null, false, null);
                if (customAlert != null)
                    customAlert.showToast(getActivity(), getString(R.string.loginError), Toast.LENGTH_SHORT);
                iNavBarUpdateUpdateListener.navToLogin();
            }
        });
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_subscription_eligible));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ONACTIVITYCALLED", "onActivity: " + shouldCheckSubscription);
        if (requestCode == 100) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("ONACTIVITYCALLED", "onResume: " + shouldCheckSubscription);
        if (shouldCheckSubscription) {
            progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
            requestHomeApi();
        } else {
            shouldCheckSubscription = true;
        }
    }
}
