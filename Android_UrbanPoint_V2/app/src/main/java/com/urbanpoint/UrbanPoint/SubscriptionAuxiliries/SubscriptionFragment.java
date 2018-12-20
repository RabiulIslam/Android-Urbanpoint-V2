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

import com.urbanpoint.UrbanPoint.DrawerAuxiliries.AccessCodeFragment;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.WebServices.Home_WebHit_Post_eligibilitychecker;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.WebServices.Home_WebHit_Post_homeApi;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.SignUpVerificationFragment;
import com.urbanpoint.UrbanPoint.SubscriptionAuxiliries.WebServices.Subscribe_WebHit_Post_validatemsisdn;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CellNoEntryView;
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

public class SubscriptionFragment extends Fragment implements View.OnClickListener {
    private Button  btnAccessCode;
   // private CellNoEntryView mMobileNumberEntry;
    private String enteredMobileNumber = "";
    private ExpandableHeightGridView lsv1, lsv2;
    private LinearLayout llParentLayout;
    private List lstStrings;
    private SubscribeTextAdapter subscribeTextAdapter;
    private SubscribeTextAdapter3 subscribeTextAdapter3;
    private ProgressDilogue progressDilogue;
    private CustomAlert customAlert;
    private HtmlTextView  txvSubsText;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subscription, container, false);
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
        updateSubscriptionLists();
        return v;
    }

    private void updateSubscriptionLists() {
        if (Home_WebHit_Post_homeApi.responseObject != null &&
                Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getSubscriptionText1() != null &&
                Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getSubscriptionText1().size() > 0) {
            lstStrings = new ArrayList();
            for (int i = 0; i < Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getSubscriptionText1().size(); i++) {
                lstStrings.add(Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getSubscriptionText1().get(i).getText());
             //   Spanned spanned = Html.fromHtml(Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getSubscriptionText1().get(0).getText());
                txvSubsText.setHtml(Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getSubscriptionText1().get(0).getText(), new HtmlResImageGetter(txvSubsText));
            }


            subscribeTextAdapter = new SubscribeTextAdapter(getContext(), lstStrings, true);
            lsv1.setExpanded(true);
            lsv1.setAdapter(subscribeTextAdapter);
        }
        if (Home_WebHit_Post_homeApi.responseObject != null &&
                Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getSubscriptionText2() != null &&
                Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getSubscriptionText2().size() > 0) {
            lstStrings = new ArrayList();
            for (int i = 0; i < Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getSubscriptionText2().size(); i++) {
                lstStrings.add(Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getSubscriptionText2().get(i).getText());
            }
            subscribeTextAdapter3 = new SubscribeTextAdapter3(getContext(), lstStrings, false);
            lsv2.setExpanded(true);
            lsv2.setAdapter(subscribeTextAdapter3);
        }
    }

    private void initialize() {
        lstStrings = new ArrayList();
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        AppConfig.getInstance().isSpaceRequiredToShow = false;
    }

    void bindViews(View v) {
        llParentLayout = v.findViewById(R.id.frg_subscription_ll_parentlayout);
        lsv1 = v.findViewById(R.id.frg_subscription_lsv_1);
        lsv2 = v.findViewById(R.id.frg_subscription_lsv_2);
        txvSubsText = (HtmlTextView) v.findViewById(R.id.txv_subs_text);
//        mMobileNumberEntry = v.findViewById(R.id.frg_subscription_pinentry_phone);
//        btnConfirm = v.findViewById(R.id.frg_subscription_btn_cnfrm);
        btnAccessCode = v.findViewById(R.id.frg_subscription_btn_access_code);

        llParentLayout.setOnClickListener(this);
//        btnConfirm.setOnClickListener(this);
        btnAccessCode.setOnClickListener(this);

//        mMobileNumberEntry.setOnPinEnteredListener(new CellNoEntryView.OnPinEnteredListener() {
//            @Override
//            public void onPinEntered(String pin) {
//                if (pin.length() == 8) {
//                    enteredMobileNumber = pin;
//                    AppConfig.getInstance().closeKeyboard(getActivity());
//                } else {
//                    enteredMobileNumber = "";
//                }
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.frg_subscription_btn_cnfrm:
//                if (enteredMobileNumber != null && enteredMobileNumber.length() == 8) {
//                    progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
////                    if (AppConfig.getInstance().mUser.getmPhoneNumber().length() > 10) {
////                        requestValidateMsisdn(AppConstt.DEFAULT_VALUES.COUNTRY_CODE + enteredMobileNumber);
////                    } else {
//                        requestCheckEligibility(AppConstt.DEFAULT_VALUES.COUNTRY_CODE + enteredMobileNumber);
//                   // }
//                } else {
//                    customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), getString(R.string.voda_do_unsubscribe_enter_mobile_number), null, null, false, null);
//                }
//                break;
            case R.id.frg_subscription_btn_access_code:
                AppConfig.getInstance().closeKeyboard(getActivity());
                Bundle b = new Bundle();
                b.putBoolean(AppConstt.BundleStrings.backBtnVisibility, true);
                navToAccessCodeFragment(b);
                break;
            case R.id.frg_subscription_ll_parentlayout:
                AppConfig.getInstance().closeKeyboard(getActivity());
                break;
        }
    }

    private void navToSubscriptionConfirmFragment(Bundle _b) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new SubscriptionConfirmFragment();
        frg.setArguments(_b);
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.SubscriptionConfirmFragment);
        ft.addToBackStack(AppConstt.FRGTAG.SubscriptionConfirmFragment);
        ft.hide(this);
        ft.commit();

    }

    public void navToAccessCodeFragment(Bundle _b) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new AccessCodeFragment();
        frg.setArguments(_b);
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.PromocodeFragment);
        ft.addToBackStack(AppConstt.FRGTAG.PromocodeFragment);
        ft.hide(this);
        ft.commit();
    }


    private void navToVerifyMemberFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new SignUpVerificationFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.FN_SignUpVerificationFragment);
        ft.addToBackStack(AppConstt.FRGTAG.FN_SignUpVerificationFragment);
        ft.hide(this);
        ft.commit();
    }

    private void navToSubscriptionEligibleFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new SubscriptionEligibleFragment();
//        Fragment frg = new SubscriptionEligibleSuccessFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.SubscriptionEligibleFragment);
        ft.addToBackStack(AppConstt.FRGTAG.SubscriptionEligibleFragment);
        ft.hide(this);
        ft.commit();

    }

    private void requestValidateMsisdn(final String _phone) {
        Subscribe_WebHit_Post_validatemsisdn subscribe_webHit_post_validatemsisdn = new Subscribe_WebHit_Post_validatemsisdn();
        subscribe_webHit_post_validatemsisdn.requestValidateMsisdn(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    if (Subscribe_WebHit_Post_validatemsisdn.responseObject != null &&
                            Subscribe_WebHit_Post_validatemsisdn.responseObject.getData() != null) {
                        Bundle b = new Bundle();
                        b.putString(AppConstt.BundleStrings.cnfirmationPin, Subscribe_WebHit_Post_validatemsisdn.responseObject.getData());
                        b.putString(AppConstt.BundleStrings.subscribeMsisdn, _phone);
                        Log.d("validateMsisdn", "onWebResult: " + Subscribe_WebHit_Post_validatemsisdn.responseObject.getData());
                        navToSubscriptionConfirmFragment(b);
                    } else {
                        customAlert.showCustomAlertDialog(getActivity(), null, strMsg, null, null, false, null);
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

    private void requestCheckEligibility(final String _phone) {
        Home_WebHit_Post_eligibilitychecker home_webHit_post_eligibilitychecker = new Home_WebHit_Post_eligibilitychecker();
        home_webHit_post_eligibilitychecker.checkEligibility(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                if (isSuccess) {
                    AppConfig.getInstance().mUser.setEligible(true);
                    requestValidateMsisdn(_phone);
                } else {
                    if (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.CONFLICT + "")) {
                        requestValidateMsisdn(_phone);
                    } else {
                        progressDilogue.stopiOSLoader();
                        customAlert.showCustomAlertDialog(getActivity(), null, strMsg, null, null, false, null);
                    }
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
            iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_subscription_for_15));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }
    }
}
