package com.urbanpoint.UrbanPoint.CommonFragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.StatsSnapshot;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.WebServices.Home_WebHit_Post_eligibilitychecker;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.SignUpVerificationFragment;
import com.urbanpoint.UrbanPoint.SubscriptionAuxiliries.SubscriptionEligibleFragment;
import com.urbanpoint.UrbanPoint.SubscriptionAuxiliries.SubscriptionEligibleSuccessFragment;
import com.urbanpoint.UrbanPoint.SubscriptionAuxiliries.SubscriptionFragment;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.WebViewFragment;
import com.urbanpoint.UrbanPoint.CommonFragments.WebServices.OfferDetail_Webhit_Get_getOfferDetail;
import com.urbanpoint.UrbanPoint.CommonFragments.WebServices.OfferDetail_Webhit_POST_addFavouriteOffer;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.CustomAlertConfirmationInterface;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Danish on 2/26/2018.
 */

public class OfferDetailFragment extends Fragment implements View.OnClickListener {

    private ImageView imvProduct, imvGetItLock;
    private Button btnCall, btnDirection, btnShare, btnGetIt, btnAddToFav, btnSavedtoFavs;
    TextView txvOfferTitle, txvProductName, txvMerchantAddress, txvDescriptionTitle, txvDescription, txvTitleWhatYouNeed,
            txvApproxSaving, txvApproxSavingPrice, txvValidFor, txvAllOfferSubject, txvRulesOfPurchase, txvTimingTitle, txvTiming, txvExpiryTime;
    private CircularImageView imvMerchant;
    private final int REQUEST_CODE_ASK_PERMISSIONS_CALL = 0;
    private int offerId;
    private String callNumber, outletId;
    private String directionlattitude, directionLongitute;
    private FragmentManager fragmentManager;
    private String navBarTitle, offerName, merchantName, merchantImage, merchantLogo, merchantPhone, merchantAddress, merchantDescription, merchantTimmings, startDate, merchantPIN,merchantId,orderId, totalSaving;
    private Bundle bundle;
    private int roundedSavings;
    CustomAlert customAlert;
    ProgressDilogue progressDilogue;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_offer_detail, container, false);
        initiate();
        bindViews(v);

        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        if (offerName.length() > 0) {
            iNavBarUpdateUpdateListener.setNavBarTitle(navBarTitle);
        } else {
            iNavBarUpdateUpdateListener.setNavBarTitle(getResources().getString(R.string.frg_offer_detail));
        }
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);


        if (offerId > 0) {
            Log.d("sdafsadfdsa", "1offerId: " + offerId);
            requestOfferDetail(offerId);
        } else {
            Log.d("sdafsadfdsa", "2offerId: " + offerId);
        }

        getNextMonth();
        return v;
    }

    private void initiate() {
        offerId = 0;
        roundedSavings = 0;
        outletId = "0";
        offerName = "";
        merchantName = "";
        merchantAddress = "";
        merchantDescription = "";
        merchantImage = "";
        merchantLogo = "";
        callNumber = "";
        merchantPhone = "";
        directionlattitude = "";
        directionLongitute = "";
        merchantPIN = "";
        merchantId = "";
        orderId = "";
        bundle = new Bundle();

        Bundle b = this.getArguments();
        if (b != null) {
            offerId = b.getInt(AppConstt.BundleStrings.offerId);
            offerName = b.getString(AppConstt.BundleStrings.offerName);
            navBarTitle = b.getString(AppConstt.BundleStrings.offerName);
        }

        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        fragmentManager = getActivity().getSupportFragmentManager();


        this.mImageLoader = ImageLoader.getInstance();
        this.mImageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(android.R.color.transparent)
                .showImageForEmptyUri(android.R.color.transparent)
                .showImageOnFail(android.R.color.transparent)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();


    }

    private void bindViews(View v) {
        txvOfferTitle = v.findViewById(R.id.frg_offer_detail_txv_merchant_title);
        imvProduct = v.findViewById(R.id.frg_offer_detail_imv_product);
        imvMerchant = v.findViewById(R.id.frg_offer_detail_imvMerchant);
        btnCall = v.findViewById(R.id.frg_offer_detail_btn_call);
        btnDirection = v.findViewById(R.id.frg_offer_detail_btnDirection);
        btnShare = v.findViewById(R.id.frg_offer_detail_btn_share);
        txvProductName = v.findViewById(R.id.frg_offer_detail_txv_merchant_name);
        txvMerchantAddress = v.findViewById(R.id.frg_offer_detail_txv_address);
        imvGetItLock = v.findViewById(R.id.frg_offer_detail_imv_lock_btngetit);
        btnGetIt = v.findViewById(R.id.frg_offer_detail_btnGetIt);
        btnAddToFav = v.findViewById(R.id.frg_offer_btn_favourite);
        txvDescriptionTitle = v.findViewById(R.id.frg_offer_detail_txv_title_description);
        txvDescription = v.findViewById(R.id.frg_offer_detail_txv_description);
        txvTitleWhatYouNeed = v.findViewById(R.id.frg_offer_detail_txv_title_what_need_to_know);
        txvApproxSaving = v.findViewById(R.id.frg_offer_detail_txv_approx_saving);
        txvValidFor = v.findViewById(R.id.frg_offer_detail_txv_validfor);
        txvApproxSavingPrice = v.findViewById(R.id.frg_offer_detail_txv_approx_saving_price);
        txvAllOfferSubject = v.findViewById(R.id.frg_offer_detail_txv_offers_subject_to);
        txvRulesOfPurchase = v.findViewById(R.id.frg_offer_detail_txv_RulesOfPurchase);
        txvTimingTitle = v.findViewById(R.id.frg_offer_detail_txv_timing_text);
        txvTiming = v.findViewById(R.id.frg_offer_detail_txv_timing_value);
        txvExpiryTime = v.findViewById(R.id.frg_offer_detail_txv_expiry);


        btnCall.setOnClickListener(this);
        btnDirection.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnAddToFav.setOnClickListener(this);
        btnGetIt.setOnClickListener(this);
        txvRulesOfPurchase.setOnClickListener(this);
        imvMerchant.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_offer_detail_imvMerchant:
                Bundle b = new Bundle();
                b.putInt(AppConstt.BundleStrings.outletId, Integer.parseInt(outletId));
                b.putString(AppConstt.BundleStrings.merchantImage, merchantImage);
                b.putString(AppConstt.BundleStrings.merchantLogo, merchantLogo);
                b.putString(AppConstt.BundleStrings.offerName, merchantName);
                b.putString(AppConstt.BundleStrings.merchantName, offerName);
                b.putString(AppConstt.BundleStrings.merchantTimmings, merchantTimmings);
                b.putString(AppConstt.BundleStrings.merchantDescription, merchantDescription);
                b.putString(AppConstt.BundleStrings.merchantPhone, merchantPhone);
                navToMerchantDetailFragment(b);
                break;

            case R.id.frg_offer_detail_btn_call:
                if (!callNumber.equalsIgnoreCase("")) {
                    customAlert.showCustomAlertDialog(getActivity(), null, callNumber, getString(R.string.call), getString(R.string.cancel), true, new CustomAlertConfirmationInterface() {
                        @Override
                        public void callConfirmationDialogPositive() {
                            int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                            int hasCallPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
                            if (currentApiVersion > 22 && hasCallPermission != PackageManager.PERMISSION_GRANTED) {

                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                        REQUEST_CODE_ASK_PERMISSIONS_CALL);
                            } else {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + callNumber));
                                startActivity(callIntent);
                            }
                        }

                        @Override
                        public void callConfirmationDialogNegative() {

                        }
                    });
                } else {

                    customAlert.showCustomAlertDialog(getActivity(), null, "Invalid Number", getString(R.string.btn_ok), null, false, new CustomAlertConfirmationInterface() {
                        @Override
                        public void callConfirmationDialogPositive() {

                        }

                        @Override
                        public void callConfirmationDialogNegative() {

                        }
                    });

                }

                break;
            case R.id.frg_offer_detail_btnDirection:
                callMapView();
                break;
            case R.id.frg_offer_detail_btn_share:
                calShare();
                break;
            case R.id.frg_offer_btn_favourite:
                requestAddToFav(offerId);
                break;
            case R.id.frg_offer_detail_btnGetIt:

                if (!AppConfig.getInstance().mUser.isSubscribed()) {
                    AppConfig.getInstance().isCommingFromOfferDetail = true;
                    String phone = AppConfig.getInstance().mUser.getmPhoneNumber();
                    Log.d("PHONECHECKER", "onClick: " + phone);
                    if (phone.length() > 10) {
                        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
                        requestCheckEligibility(phone);
                    } else {
                        AppConfig.getInstance().mUser.setEligible(false);
                        navToSubscriptionFragment();
                    }
                } else {
                    bundle = new Bundle();
                    bundle.putString(AppConstt.BundleStrings.offerId, offerId + "");
                    bundle.putString(AppConstt.BundleStrings.offerName, offerName);
                    bundle.putString(AppConstt.BundleStrings.merchantName, merchantName);
                    bundle.putString(AppConstt.BundleStrings.merchantAddress, merchantAddress);
                    bundle.putString(AppConstt.BundleStrings.merchantPIN, merchantPIN);
                    bundle.putString(AppConstt.BundleStrings.merchantId, merchantId);
                    bundle.putString(AppConstt.BundleStrings.orderId, orderId);
                    navToMerchantPinFragment(bundle);
                }
                break;

            case R.id.frg_offer_detail_txv_RulesOfPurchase:
                bundle.putString("page", AppConstt.REDEEM_RULES);
                navToRulesOfPurchaseFragment(bundle);
                break;
        }
    }

    //region Navigation Function
    public void navToMerchantDetailFragment(Bundle bundle) {
        Fragment fr = new MerchantDetailFragment();
        fr.setArguments(bundle);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_frame, fr, AppConstt.FRGTAG.OutletDetailFramgnet);
        ft.addToBackStack(AppConstt.FRGTAG.OfferDetailFragment);
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

    private void navToVerifyMemberFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new SignUpVerificationFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.FN_SignUpVerificationFragment);
        ft.addToBackStack(AppConstt.FRGTAG.FN_SignUpVerificationFragment);
        ft.hide(this);
        ft.commit();
    }

    private void navToMerchantPinFragment(Bundle bundle) {
        Fragment fr = new MerchantPinFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fr.setArguments(bundle);
        ft.add(R.id.content_frame, fr, AppConstt.FRGTAG.MerchantPinFragment);
        ft.addToBackStack(AppConstt.FRGTAG.MerchantPinFragment);
        ft.hide(this);
        ft.commit();
    }

    private void navToSubscriptionFragment() {
        Fragment fr = new SubscriptionFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_frame, fr, AppConstt.FRGTAG.SubscriptionFragment);
        ft.addToBackStack(AppConstt.FRGTAG.SubscriptionFragment);
        ft.hide(this);
        ft.commit();
    }

    private void navToRulesOfPurchaseFragment(Bundle bundle) {
        Fragment fr = new WebViewFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fr.setArguments(bundle);
        ft.add(R.id.content_frame, fr, AppConstt.FRGTAG.WebViewFragment);
        ft.addToBackStack(AppConstt.FRGTAG.WebViewFragment);
        ft.hide(this);
        ft.commit();
    }

    private void navToSubscriptionEligibleFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new SubscriptionEligibleFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.SubscriptionEligibleFragment);
        ft.addToBackStack(AppConstt.FRGTAG.SubscriptionEligibleFragment);
        ft.hide(this);
        ft.commit();

    }

    private void callMapView() {
        if (!directionlattitude.equals("")) {
            //mLatitude = Double.parseDouble(mEventDetailsModel.getmLatitude());
            try {
                String url = "http://maps.google.com/maps?f=d&daddr=" + directionlattitude
                        + "," + directionLongitute + "&dirflg=d";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(url));
                intent.setClassName("com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity");

                //MyApplication.getInstance().printLogs("MAP", url);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?f=d&daddr=" + directionlattitude
                                + "," + directionLongitute));
                // MyApplication.getInstance().printLogs("MAP EXCEPTION", e.getMessage());
                startActivity(intent);
            }
        }
    }

    private void calShare() {
        if (OfferDetail_Webhit_Get_getOfferDetail.responseObject != null) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getDescription());
            String formattedString = String.format(getString(R.string.offer_share_message_text), OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getTitle(), OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getName(), roundedSavings + " QR", AppConstt.DEFAULT_VALUES.SHARE_URL);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, formattedString);
            Log.d("sadsadsdsad", "onClick: " + formattedString);
            startActivity(Intent.createChooser(sharingIntent, "Select"));
        }
    }
    //endregion

    public void requestOfferDetail(int id) {
        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        OfferDetail_Webhit_Get_getOfferDetail offerDetail_webhit_get_getOfferDetail = new OfferDetail_Webhit_Get_getOfferDetail();
        offerDetail_webhit_get_getOfferDetail.requestOfferDetail(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    logFireBaseEvent();
                    logFaceBookEvent();
                    updateOfferDetailValues();
                } else {
                    btnGetIt.setVisibility(View.GONE);
                    btnAddToFav.setVisibility(View.GONE);
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
        }, id);
    }

    private void requestCheckEligibility(String _phone) {
        Home_WebHit_Post_eligibilitychecker home_webHit_post_eligibilitychecker = new Home_WebHit_Post_eligibilitychecker();
        home_webHit_post_eligibilitychecker.checkEligibility(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    if (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.OK + "")) {
                        if (Home_WebHit_Post_eligibilitychecker.responseObject.getData() != null) {
                            if (Home_WebHit_Post_eligibilitychecker.responseObject.getData().getPremier_subscription().equalsIgnoreCase("1")) {
                                navToSubscriptionEligibleSuccessFragment();
                            } else if (Home_WebHit_Post_eligibilitychecker.responseObject.getData().getPremierUser().equalsIgnoreCase("1")) {
                                navToVerifyMemberFragment();
                            } else {
                                navToSubscriptionEligibleFragment();
                            }
                        } else {
                            navToSubscriptionFragment();
                        }
                    } else {
                        navToSubscriptionEligibleFragment();
                    }
                } else {
                    if (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.CONFLICT + "")) {
                        navToSubscriptionFragment();
                    } else {
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

    private void requestAddToFav(int id) {
        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        OfferDetail_Webhit_POST_addFavouriteOffer offerDetail_webhit_post_addFavouriteOffer = new OfferDetail_Webhit_POST_addFavouriteOffer();
        offerDetail_webhit_post_addFavouriteOffer.requestAddToFav(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    customAlert.showCustomiOSMessageBox(getActivity(), getString(R.string.added_to_ur_wish_list), 2000);
                    btnAddToFav.setBackground(getResources().getDrawable(R.drawable.btn_redeem_slc));
                    btnAddToFav.setEnabled(false);
                    btnAddToFav.setClickable(false);
                    btnAddToFav.setText(getResources().getString(R.string.added_to_ur_wish_list));
                    AppConfig.getInstance().mUserBadges.setFavoriteCount(1);
                    AppConfig.getInstance().saveUserData();
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
        }, id);
    }

    private void updateOfferDetailValues() {
        if (OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData() != null &&
                OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().size() > 0) {
            if (OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getImage() != null && !OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getImage().equalsIgnoreCase("")) {
                imvProduct.setVisibility(View.VISIBLE);

//                imageLoader.displayImage(AppConstt.BASE_URL_IMAGES + OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getImage(), imvMerchant);

//                mImageLoader.displayImage(AppConstt.BASE_URL_IMAGES + OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getImage(), imvProduct
//                        , options, new SimpleImageLoadingListener() {
//                            @Override
//                            public void onLoadingStarted(String imageUri, View view) {
//                            }
//
//                            @Override
//                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                                Log.d("Picass2", "onLoadingFailed ImageLoader: "+failReason.toString());
//                            }
//
//                            @Override
//                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                                Log.d("Picass2", "onLoadingComplete ImageLoader: "+imageUri);
//                            }
//                        });

                Picasso.get()
                        .load(AppConstt.BASE_URL_IMAGES + OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getImage())
                        .into(imvProduct);
            }

            if (OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getLogo() != null) {
//                mImageLoader.displayImage(AppConstt.BASE_URL_IMAGES + OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getLogo(), imvMerchant
//                        , options, new SimpleImageLoadingListener() {
//                            @Override
//                            public void onLoadingStarted(String imageUri, View view) {
//                            }
//
//                            @Override
//                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                            }
//
//                            @Override
//                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                            }
//                        });

                Picasso.get()
                        .load(AppConstt.BASE_URL_IMAGES + OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getLogo())
                        .into(imvMerchant, new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.d("Picass2", "onSuccess Picasso: ");
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.d("Picass2", "onError Picasso: "+e.getMessage());
                            }
                        });


            } else {
                imvMerchant.setImageResource(R.drawable.rmv_place_holder);
            }
            merchantPIN = OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getPin();
            merchantId = OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getMerchantId();
            orderId = OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getId();
            txvOfferTitle.setVisibility(View.VISIBLE);
            txvOfferTitle.setText(OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getTitle());
            offerName = OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getAddress();
            txvProductName.setVisibility(View.VISIBLE);
            txvProductName.setText(OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getName());
            merchantName = OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getName();
            txvMerchantAddress.setVisibility(View.VISIBLE);
            txvMerchantAddress.setText(OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getAddress());
            merchantAddress = OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getAddress();
            merchantDescription = OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getOutletDescription();
            merchantImage = OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getOutletImage();
            merchantLogo = OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getLogo();
            merchantTimmings = OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getOutletTiming();
            merchantPhone = OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getPhone();
            txvDescriptionTitle.setVisibility(View.VISIBLE);
            txvDescription.setVisibility(View.VISIBLE);
            txvDescription.setText(OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getDescription());
            txvTitleWhatYouNeed.setVisibility(View.VISIBLE);
            txvApproxSaving.setVisibility(View.VISIBLE);
            float savings = Float.parseFloat(String.valueOf(OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getApproxSaving()));
            roundedSavings = (int) savings;
            txvApproxSavingPrice.setText(getResources().getString(R.string.txv_qatar_riyal) + " " + roundedSavings);
            txvApproxSavingPrice.setVisibility(View.VISIBLE);
            txvValidFor.setText(OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getValidFor());
            txvValidFor.setVisibility(View.VISIBLE);
            txvAllOfferSubject.setVisibility(View.VISIBLE);
            txvRulesOfPurchase.setVisibility(View.VISIBLE);
            txvTimingTitle.setVisibility(View.VISIBLE);
            txvTiming.setVisibility(View.VISIBLE);
            btnAddToFav.setVisibility(View.VISIBLE);
            btnGetIt.setVisibility(View.VISIBLE);

            if (AppConfig.getInstance().mUser.isSubscribed()) {
                imvGetItLock.setVisibility(View.GONE);
            } else {
                imvGetItLock.setVisibility(View.VISIBLE);
            }
            if (OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getIsRedeeme() == 0) {
                btnGetIt.setText(getResources().getString(R.string.btn_used));
                btnGetIt.setEnabled(false);
                btnGetIt.setClickable(false);
                btnGetIt.setBackground(getResources().getDrawable(R.drawable.btn_redeem_slc));
                txvExpiryTime.setText(getResources().getString(R.string.offer_detail_expires_on_2) + " " + getNextMonth());
                txvExpiryTime.setVisibility(View.VISIBLE);
            } else {
                String newDate = convertDate(OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getEndDatetime());
                txvExpiryTime.setText(getResources().getString(R.string.offer_detail_expires_on_1) + " " + newDate);
                txvExpiryTime.setVisibility(View.VISIBLE);
            }
            if (OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getRenew().equalsIgnoreCase("0")) {
                txvExpiryTime.setText(getResources().getString(R.string.offer_detail_expires_on_3));
                txvExpiryTime.setVisibility(View.VISIBLE);
            }

            txvTiming.setText(OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getOutletTiming());
            startDate = OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getCreatedAt();


            if (OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getPhone() != null) {
                callNumber = OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getPhone();
            }

            if (OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getApproxSaving() != null) {
                totalSaving = OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getApproxSaving();
            }


            if (OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getLatitude() != null) {
                directionlattitude = OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getLatitude();
            }

            if (OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getLongitude() != null) {
                directionLongitute = OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getLongitude();
            }

            if ((OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getIsfavourite().equalsIgnoreCase("1"))) {
                btnAddToFav.setText(getResources().getString(R.string.add_to_favs));
                btnAddToFav.setEnabled(false);
                btnAddToFav.setClickable(false);
                btnAddToFav.setBackground(getResources().getDrawable(R.drawable.btn_redeem_slc));
            } else {
                btnAddToFav.setText(getResources().getString(R.string.favourite));
                btnAddToFav.setBackground(getResources().getDrawable(R.drawable.btn_redeem));
            }
            outletId = OfferDetail_Webhit_Get_getOfferDetail.responseObject.getData().get(0).getOutletId();
        }
    }

    public String getNextMonth() {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MONTH, 1);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM, yyyy");
        String strDate = "01 " + formatter.format(date.getTime());
        Log.d("sdfsdfsdf", "getNextMonth: " + strDate);
        return strDate;
    }

    public String convertDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
            Log.d("serverDate", testDate.toString() + date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        SimpleDateFormat formatter = new SimpleDateFormat("dd", Locale.ENGLISH);
        String newDate = formatter.format(testDate);
//        if (newDate.endsWith("1") && !newDate.endsWith("11"))
//            formatter = new SimpleDateFormat("dd'st' MMM, yyyy");
//        else if (newDate.endsWith("2") && !newDate.endsWith("12")) {
//            formatter = new SimpleDateFormat("dd'nd' MMM, yyyy");
//        } else if (newDate.endsWith("3") && !newDate.endsWith("13")) {
//            formatter = new SimpleDateFormat("dd'rd' MMM, yyyy");
//        } else if (newDate.endsWith("31")) {
//            formatter = new SimpleDateFormat("dd'st' MMM, yyyy");
//        } else {
        formatter = new SimpleDateFormat("dd MMM, yyyy");
        Log.d("convertedDate", testDate.toString() + date);
//        }

        String newFormat = formatter.format(testDate);
        return newFormat;
    }

    public String convertTime(String time) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM:SS", Locale.ENGLISH);
        Date testDate = null;
        try {
            testDate = sdf.parse(time);
            Log.d("time", testDate.toString() + time);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        String newFormat = formatter.format(testDate);
        return newFormat;
    }

    private void logFireBaseEvent() {
        Bundle params = new Bundle();
        params.putString("user_id", AppConfig.getInstance().mUser.getmUserId());
        params.putString("device_type", "Android");
        // Send the event
        FirebaseAnalytics.getInstance(getActivity())
                .logEvent(AppConstt.FireBaseEvents.Number_Of_Offers_Viewed, params);
    }
    private void logFaceBookEvent() {
        AppEventsLogger.newLogger(getActivity()).logEvent(AppConstt.FireBaseEvents.Number_Of_Offers_Viewed);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS_CALL:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + callNumber));
                    startActivity(callIntent);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            if (offerName.length() > 0) {
                iNavBarUpdateUpdateListener.setNavBarTitle(navBarTitle);
            } else {
                iNavBarUpdateUpdateListener.setNavBarTitle(getResources().getString(R.string.frg_offer_detail));
            }
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

            // if User subscribe from OfferDetail and comes back to offerDetail, update the locked values
            if (AppConfig.getInstance().isCommingFromOfferDetail) {
                AppConfig.getInstance().isCommingFromOfferDetail = false;
                if (AppConfig.getInstance().mUser.isSubscribed()) {
                    imvGetItLock.setVisibility(View.GONE);
                } else {
                    imvGetItLock.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
