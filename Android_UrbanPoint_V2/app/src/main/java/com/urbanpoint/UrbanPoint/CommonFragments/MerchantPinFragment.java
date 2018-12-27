package com.urbanpoint.UrbanPoint.CommonFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.CommonFragments.WebServices.MerchantPIN_Webhit_POST_redeemOffer;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.PinEntry;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

/**
 * Created by Danish on 3/7/2018.
 */

public class MerchantPinFragment extends Fragment implements View.OnClickListener {
    TextView txvOfferName, txvMerchantName, txvAddress;
    PinEntry pinEntry;
    Button btnConfirmPurchase;
    private String enteredLoginPin = "";
    private LinearLayout llParentLayout;
    String offerId, offerName, merchantName, merchantAddress, merchantPIN,merchantId, orderId,approxsavings;
    CustomAlert customAlert;
    private ProgressDilogue progressDilogue;
    FragmentManager fragmentManager;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_merchant_pin, container, false);
        initiate();
        bindViews(v);

        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle(getResources().getString(R.string.frg_merchant_pin));
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);


        return v;
    }

    private void initiate() {
        offerId = "";
        offerName = "";
        merchantName = "";
        merchantAddress = "";
        merchantPIN = "";
        merchantId = "";
        orderId = "";

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            offerId = bundle.getString(AppConstt.BundleStrings.offerId);
            offerName = bundle.getString(AppConstt.BundleStrings.offerName);
            merchantName = bundle.getString(AppConstt.BundleStrings.merchantName);
            merchantAddress = bundle.getString(AppConstt.BundleStrings.merchantAddress);
            merchantPIN = bundle.getString(AppConstt.BundleStrings.merchantPIN);
            merchantId = bundle.getString(AppConstt.BundleStrings.merchantId);
            orderId = bundle.getString(AppConstt.BundleStrings.orderId);
        }
    }

    void bindViews(View v) {
        llParentLayout = v.findViewById(R.id.frg_merchant_pin_ll_parentlayout);
        txvOfferName = v.findViewById(R.id.txvOfferName);
        txvMerchantName = v.findViewById(R.id.txvMerchantName);
        txvAddress = v.findViewById(R.id.txvMerchantAddress);
        pinEntry = v.findViewById(R.id.pinEntry);
        btnConfirmPurchase = v.findViewById(R.id.btnConfirmPurchase);
        btnConfirmPurchase.setOnClickListener(this);
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        fragmentManager = getActivity().getSupportFragmentManager();

        txvOfferName.setText(offerName);
        txvMerchantName.setText(merchantName);
        txvAddress.setText(merchantAddress);


        llParentLayout.setOnClickListener(this);

        Log.d("MerchintPIN", "Merchant PIN: " + merchantPIN);
        pinEntry.setOnPinEnteredListener(new PinEntry.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                if (pin.length() < 4) {
                    enteredLoginPin = "";
                } else {
                    enteredLoginPin = pin;
                    AppConfig.getInstance().closeKeyboard(getActivity());
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConfirmPurchase:
                if (validatingRequired()) {
                    if (AppConfig.getInstance().mUser.isSubscribed()) {
                        requestRedeemOffer(Integer.parseInt(offerId), Integer.parseInt(enteredLoginPin), 0);
                    }
                    else
                    {
                        requestRedeemOffer(Integer.parseInt(offerId), Integer.parseInt(enteredLoginPin), 1);
                    }
                }
                break;

            case R.id.frg_merchant_pin_ll_parentlayout:
                AppConfig.getInstance().closeKeyboard(getActivity());
                break;

        }
    }

    void navToPurchaseSuccessFragment(Bundle b) {
        Fragment fr = new PurchaseSuccessFragment();
        fr.setArguments(b);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_frame, fr, AppConstt.FRGTAG.FN_PurchaseSuccessFragment);
        ft.addToBackStack(AppConstt.FRGTAG.FN_PurchaseSuccessFragment);
        ft.hide(this);
        ft.commit();
    }

    private void requestRedeemOffer(int _offerId, int _pin,int useWallet) {
        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        MerchantPIN_Webhit_POST_redeemOffer merchantPIN_webhit_post_redeemOffer = new MerchantPIN_Webhit_POST_redeemOffer();
        merchantPIN_webhit_post_redeemOffer.requestRedeem(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    String purchaseSuccessPIN = MerchantPIN_Webhit_POST_redeemOffer.responseObject.getData() + "";
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstt.BundleStrings.offerName, offerName);
                    bundle.putString(AppConstt.BundleStrings.merchantName, merchantName);
                    bundle.putString(AppConstt.BundleStrings.merchantAddress, merchantAddress);
                    bundle.putString(AppConstt.BundleStrings.purchaseSuccessPIN, purchaseSuccessPIN);
                    bundle.putString(AppConstt.BundleStrings.offerId, offerId);
                    bundle.putString(AppConstt.BundleStrings.merchantPIN, merchantPIN);
                    bundle.putString(AppConstt.BundleStrings.merchantId, merchantId);
                    bundle.putString(AppConstt.BundleStrings.orderId, orderId);

                    navToPurchaseSuccessFragment(bundle);
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
        }, _offerId, _pin,useWallet);
    }

    private boolean validatingRequired() {
        String message = "";

        //validate the content
        if (enteredLoginPin.equalsIgnoreCase(AppConfig.getInstance().mUser.getMasterMerchant())) {
            message = "";
        } else {
            if (enteredLoginPin.equalsIgnoreCase("")) {
                message = getResources().getString(R.string.MerchantErrorMessage);
                customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);

            } else if (enteredLoginPin.length() < 4) {
                message = getResources().getString(R.string.MerchantErrorMessage);
                customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
            } else if (!enteredLoginPin.equalsIgnoreCase(merchantPIN)) {
                message = getResources().getString(R.string.enter_valid_pin_message);
                customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
                pinEntry.clearText();
            } else if (offerId.length() < 1) {
                message = getResources().getString(R.string.OfferErrorMessage);
                customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
            }
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
            iNavBarUpdateUpdateListener.setNavBarTitle(getResources().getString(R.string.frg_merchant_pin));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }
    }
}
