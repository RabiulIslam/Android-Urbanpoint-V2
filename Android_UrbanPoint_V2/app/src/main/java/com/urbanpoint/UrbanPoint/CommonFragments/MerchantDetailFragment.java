package com.urbanpoint.UrbanPoint.CommonFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.uber.sdk.android.core.Deeplink;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestDeeplink;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.core.client.SessionConfiguration;
import com.urbanpoint.UrbanPoint.BuildConfig;
import com.urbanpoint.UrbanPoint.CommonFragments.WebServices.MerchantDetail_Webhit_Get_Uber_Estimate_price;
import com.urbanpoint.UrbanPoint.CommonFragments.WebServices.MerchantDetail_Webhit_Get_Uber_Estimate_time;
import com.urbanpoint.UrbanPoint.CommonFragments.WebServices.MerchantDetail_Webhit_Get_getMerchantDetail;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.CustomAlertConfirmationInterface;
import com.urbanpoint.UrbanPoint.Utils.ExpandableHeightGridView;
import com.urbanpoint.UrbanPoint.Utils.GPSTracker;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

import java.util.ArrayList;
import java.util.Arrays;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class MerchantDetailFragment extends Fragment implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    double pickUp_latitude, pickUp_longitude, dropoff_lat, dropoff_lng;
    private ImageView imvProduct;
    private Button btnCall, btnDirection;
    private TextView txvOfferTitle, txvMerchantName, txvMerchantTimmings, txvDescription, txvTimeDuration, txvPriceEstimate;
    private CircularImageView imvMerchant;
    private final int REQUEST_CODE_ASK_PERMISSIONS_CALL = 0;
    private ArrayList<DModelOutletOffers> lstMerchantOffers;
    private OutletOffersAdapter merchantOffersAdapter;
    private ExpandableHeightGridView lsvMerchantOffers;
    private View lsvFooterView;
    private LinearLayout llBottomCntnr;
    private ScrollView scrollView;
    private String merchantPhone, directionlattitude, directionLongitute;
    private int outletId, page;
    private boolean shouldGetMoreOffers, isAlreadyfetchingOffers;
    private String navBarTitle, offerName, merchantName, merchantImage, merchantLogo, merchantAddress, merchantDescription, merchantTimmings, merchantPIN;
    private Bundle bundle;
    private LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;
    final static int REQUEST_LOCATION = 199;
    final static int MY_PERMISSIONS_REQUEST_LOCATION = 1999;
    android.location.Location mLastLocation;
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    private boolean isPriceLoaded, isTimeLoaded;
    private SessionConfiguration uberConfig;
    //    private RelativeLayout layout;
//    private RideRequestButton requestButton;
//    private RideRequestButtonCallback callback;
    private RideRequestDeeplink mDeepLink;
    private LinearLayout llUberbtnCntnr, llUberEstimationCntnr;
    private static final int PERMISSION_REQUEST_CODE = 1;


    CustomAlertConfirmationInterface callConfirmationInterfaces;
    CustomAlert customAlert;
    ProgressDilogue progressDilogue;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_merchant_detail, container, false);
        initiate();
        bindViews(v);

        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        if (merchantName.length() > 0) {
            iNavBarUpdateUpdateListener.setNavBarTitle(navBarTitle);
        } else {
            iNavBarUpdateUpdateListener.setNavBarTitle(getResources().getString(R.string.frg_merchant_detail));
        }
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);


        lsvMerchantOffers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int offerId = Integer.parseInt(lstMerchantOffers.get(position).getId());
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstt.BundleStrings.offerId, offerId);
                bundle.putString(AppConstt.BundleStrings.offerName, lstMerchantOffers.get(position).getOfferName());
                navToOfferDetailFragment(bundle);

            }
        });

        lsvMerchantOffers.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                View childView = scrollView.getChildAt(0);
                int diff = (childView.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

                // if diff is zero, then the bottom has been reached
                // do stuff
                Log.d("saddasd", "onScrollStateChanged1: " + diff);

                if (diff <= 340) {
                    Log.d("saddasd", "onScrollStateChanged2: ");
                    if (shouldGetMoreOffers && !isAlreadyfetchingOffers) {
//                              if ((lsvMerchantOffers.getLastVisiblePosition() == (merchantOffersAdapter.getCount() - 1)) ) {
                        page++;
                        lsvMerchantOffers.addFooterView(lsvFooterView);
                        merchantOffersAdapter.notifyDataSetChanged();
                        requestMerchantDetail(outletId, page, false);
//                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d("saddasd", "onScrollStateChanged3: ");

                if (view.getLastVisiblePosition() == (totalItemCount - 1)) {
                    Log.d("saddasd", "onScrollStateChanged4: ");
                }
            }
        });

        if (outletId > 0) {
            Log.d("sdafsadfdsa", "1offerId: " + outletId);
            requestMerchantDetail(outletId, page, true);
        } else {
            Log.d("sdafsadfdsa", "2offerId: " + outletId);
        }
        return v;
    }

    private void initiate() {
        outletId = 0;
        page = 1;
        offerName = "";
        merchantName = "";
        merchantAddress = "";
        merchantDescription = "";
        merchantImage = "";
        merchantLogo = "";
        merchantPhone = "";
        directionlattitude = "";
        directionLongitute = "";
        shouldGetMoreOffers = true;
        isAlreadyfetchingOffers = false;
        isTimeLoaded = false;
        isPriceLoaded = false;
        lstMerchantOffers = new ArrayList<>();
        bundle = new Bundle();

        Bundle b = this.getArguments();
        if (b != null) {
            outletId = b.getInt(AppConstt.BundleStrings.outletId);
            offerName = b.getString(AppConstt.BundleStrings.offerName);
            navBarTitle = b.getString(AppConstt.BundleStrings.offerName);
            merchantName = b.getString(AppConstt.BundleStrings.merchantName);
            merchantTimmings = b.getString(AppConstt.BundleStrings.merchantTimmings);
            merchantDescription = b.getString(AppConstt.BundleStrings.merchantDescription);
            merchantImage = b.getString(AppConstt.BundleStrings.merchantImage);
            merchantLogo = b.getString(AppConstt.BundleStrings.merchantLogo);
            merchantPhone = b.getString(AppConstt.BundleStrings.merchantPhone);
        }
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
    }

    private void bindViews(View v) {
        llBottomCntnr = v.findViewById(R.id.frg_merchant_detail_ll_cntnr_bottom);
        txvOfferTitle = v.findViewById(R.id.frg_merchant_detail_txv_merchant_title);
        imvProduct = v.findViewById(R.id.frg_merchant_detail_imv_product);
        imvMerchant = v.findViewById(R.id.frg_merchant_detail_imvMerchant);
        btnCall = v.findViewById(R.id.frg_merchant_detail_btn_call);
        btnDirection = v.findViewById(R.id.frg_merchant_detail_btnDirection);
        txvMerchantName = v.findViewById(R.id.frg_merchant_detail_txv_merchant_name);
        txvMerchantTimmings = v.findViewById(R.id.frg_merchant_detail_txv_timmings);
        txvDescription = v.findViewById(R.id.frg_merchant_detail_txv_description);
        lsvMerchantOffers = v.findViewById(R.id.frg_merchant_detial_lsv_offers);
        scrollView = v.findViewById(R.id.frg_merchant_detail_scrollview);
        lsvFooterView = ((LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.lsv_footer, null, false);

//        requestButton = new RideRequestButton(getActivity());
//        requestButton.setDeeplinkFallback(Deeplink.Fallback.MOBILE_WEB);
//        layout = v.findViewById(R.id.relative_layout);
//        layout.addView(requestButton);

        llUberbtnCntnr = v.findViewById(R.id.frg_merchant_detail_uber_btn_ll_container);
        llUberEstimationCntnr = v.findViewById(R.id.frg_merchant_detail_ll_cntnr_estimate);
        txvTimeDuration = v.findViewById(R.id.frg_merchant_detail_uber_time_duration_txv);
        txvPriceEstimate = v.findViewById(R.id.frg_merchant_detail_uber_price_estimate_txv);
        llUberbtnCntnr.setOnClickListener(this);

        txvOfferTitle.setText(offerName);
        txvMerchantName.setText(merchantName);
        txvMerchantTimmings.setText(merchantTimmings);
        txvDescription.setText(merchantDescription);

        if (merchantImage.length() > 0) {
            Picasso.get()
                    .load(AppConstt.BASE_URL_IMAGES + merchantImage)
                    .into(imvProduct);
        } else {
            /*imvProduct.setImageResource(R.mipmap.);*/
        }

        if (merchantLogo.length() > 0) {
            Picasso.get()
                    .load(AppConstt.BASE_URL_IMAGES + merchantLogo)
                    .into(imvMerchant);
        } else {
            imvMerchant.setImageResource(R.drawable.rmv_place_holder);
        }

        btnCall.setOnClickListener(this);
        btnDirection.setOnClickListener(this);
        imvMerchant.setOnClickListener(this);
        buildGoogleApiClient();
        createLocationRequest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_merchant_detail_btn_call:
                if (!merchantPhone.equalsIgnoreCase("")){
                    customAlert.showCustomAlertDialog(getActivity(), null, merchantPhone, getString(R.string.call), getString(R.string.cancel), true, new CustomAlertConfirmationInterface() {
                        @Override
                        public void callConfirmationDialogPositive() {
                            int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                            int hasCallPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
                            if (currentApiVersion > 22 && hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                        REQUEST_CODE_ASK_PERMISSIONS_CALL);
                            } else {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + merchantPhone));
                                startActivity(callIntent);
                            }
                        }

                        @Override
                        public void callConfirmationDialogNegative() {

                        }
                    });
                }else {
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
            case R.id.frg_merchant_detail_btnDirection:
                callMapView();
                break;

            case R.id.frg_merchant_detail_uber_btn_ll_container:
                boolean isAppInstalled = appInstalledOrNot("com.ubercab");
                if (isAppInstalled) {
                    //This intent will help you to launch if the package is already installed
//                    Intent LaunchIntent = getActivity().getPackageManager()
//                            .getLaunchIntentForPackage("com.ubercab");
//                    startActivity(LaunchIntent);
                    startActivity(new Intent(Intent.ACTION_VIEW, mDeepLink.getUri()));
                    Log.d("APPINSTALLED", "Application is already installed.");
                } else {
                    // Do whatever we want to do if application not installed
                    // Redirect to play store
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.ubercab"));
                    startActivity(i);
                    Log.d("APPINSTALLED", "Application is not currently installed.");
                }
                break;
        }
    }

    //region Navigation Function
    public void navToOfferDetailFragment(Bundle bundle) {
        Fragment fr = new OfferDetailFragment();
        fr.setArguments(bundle);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_frame, fr, AppConstt.FRGTAG.OfferDetailFragment);
        ft.addToBackStack(AppConstt.FRGTAG.OfferDetailFragment);
        ft.hide(this);
        ft.commit();
    }

    private void callMapView() {
        if (!directionlattitude.equals("")) {
            //mLatitude = Double.parseDouble(mEventDetailsModel.getmLatitude());
            try {
                String url = "http://maps.google.com/maps?f=d&daddr=" + directionlattitude
                        + "," + directionLongitute + "&dirflg=d";
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                intent.setClassName("com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity");

                //MyApplication.getInstance().printLogs("MAP", url);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?f=d&daddr=" + directionlattitude
                                + "," + directionLongitute));
                // MyApplication.getInstance().printLogs("MAP EXCEPTION", e.getMessage());
                startActivity(intent);
            }
        }
    }
    //endregion

    private void requestMerchantDetail(int _outletId, int _page, boolean _shouldClearLst) {
        if (_shouldClearLst) {
            progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        }
        isAlreadyfetchingOffers = true;
        MerchantDetail_Webhit_Get_getMerchantDetail merchantDetail_webhit_get_getMerchantDetail = new MerchantDetail_Webhit_Get_getMerchantDetail();
        merchantDetail_webhit_get_getMerchantDetail.requestOfferDetail(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                isAlreadyfetchingOffers = false;

                if (isSuccess) {

                    updateOfferDetailValues();
                } else if (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.DATABASE_NOT_FOUND + "")) {
                    shouldGetMoreOffers = false;
                    if (lstMerchantOffers.size() > 0) {
                        lsvMerchantOffers.removeFooterView(lsvFooterView);
                        merchantOffersAdapter.notifyDataSetChanged();
                    }

                } else {
                    customAlert.showCustomAlertDialog(getActivity(), null, strMsg, null, null, false, null);

                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                isAlreadyfetchingOffers = false;
                customAlert.showCustomAlertDialog(getActivity(), null, ex.getMessage(), null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();
                isAlreadyfetchingOffers = false;

            }
        }, _outletId, _page);
    }

    private void requestUberTime(double start_lat, double start_lng, double end_lat, double end_lng) {
        MerchantDetail_Webhit_Get_Uber_Estimate_time merchantDetail_webhit_get_uber_estimate_time = new MerchantDetail_Webhit_Get_Uber_Estimate_time();
        merchantDetail_webhit_get_uber_estimate_time.getUberTime(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                if (isSuccess) {
                    if (MerchantDetail_Webhit_Get_Uber_Estimate_time.responseObject != null &&
                            MerchantDetail_Webhit_Get_Uber_Estimate_time.responseObject.getTimes() != null &&
                            MerchantDetail_Webhit_Get_Uber_Estimate_time.responseObject.getTimes().size() > 0) {
                        isTimeLoaded = true;
                        Log.d("UBERSUCCESSS", "showResultTime: true ");
                        int mins = (int) (MerchantDetail_Webhit_Get_Uber_Estimate_time.responseObject.getTimes().get(0).getEstimate() / 60);
                        if (mins > 1) {
                            txvTimeDuration.setText(mins + " mins away");
                        } else {
                            txvTimeDuration.setText(mins + " min away");
                        }
                        txvTimeDuration.setVisibility(View.VISIBLE);
                    } else {
                        txvTimeDuration.setVisibility(View.GONE);
                    }
                    updateUberValues(true);
                } else {
                    Log.d("UBERSUCCESSS", "showResultTime: false ");
                    updateUberValues(false);
                }
            }

            @Override
            public void onWebException(Exception ex) {

            }

            @Override
            public void onWebLogout() {

            }
        }, start_lat, start_lng, end_lat, end_lng);
    }

    private void requestUberPrice(double start_lat, double start_lng, double end_lat, double end_lng) {
        MerchantDetail_Webhit_Get_Uber_Estimate_price merchantDetail_webhit_get_uber_estimate_price = new MerchantDetail_Webhit_Get_Uber_Estimate_price();
        merchantDetail_webhit_get_uber_estimate_price.getUberPrice(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                if (isSuccess) {
                    if (MerchantDetail_Webhit_Get_Uber_Estimate_price.responseObject != null &&
                            MerchantDetail_Webhit_Get_Uber_Estimate_price.responseObject.getPrices() != null &&
                            MerchantDetail_Webhit_Get_Uber_Estimate_price.responseObject.getPrices().size() > 0) {
                        isPriceLoaded = true;
                        String strPriceEstimate = MerchantDetail_Webhit_Get_Uber_Estimate_price.responseObject.getPrices().get(0).getEstimate() + " on " +
                                MerchantDetail_Webhit_Get_Uber_Estimate_price.responseObject.getPrices().get(0).getDisplayName();
                        Log.d("UBERSUCCESSS", "showResultPrice: true :" + strPriceEstimate);
                        txvPriceEstimate.setVisibility(View.VISIBLE);
                        txvPriceEstimate.setText(strPriceEstimate);
                    } else {
                        txvPriceEstimate.setVisibility(View.GONE);
                    }
                    updateUberValues(true);
                } else {
                    Log.d("UBERSUCCESSS", "showResultPrice: false ");
                    updateUberValues(false);
                }
            }

            @Override
            public void onWebException(Exception ex) {

            }

            @Override
            public void onWebLogout() {

            }
        }, start_lat, start_lng, end_lat, end_lng);
    }

    private void updateOfferDetailValues() {
        if (MerchantDetail_Webhit_Get_getMerchantDetail.responseObject.getData() != null &&
                MerchantDetail_Webhit_Get_getMerchantDetail.responseObject.getData().size() > 0) {

            // This one is for pagination
            if (MerchantDetail_Webhit_Get_getMerchantDetail.responseObject.getData().size() <= 20) {
                shouldGetMoreOffers = false;
            }

            txvOfferTitle.setVisibility(View.VISIBLE);
            txvMerchantName.setVisibility(View.VISIBLE);
            txvMerchantTimmings.setVisibility(View.VISIBLE);
            txvDescription.setVisibility(View.VISIBLE);
            imvMerchant.setVisibility(View.VISIBLE);
            imvProduct.setVisibility(View.VISIBLE);

            if (MerchantDetail_Webhit_Get_getMerchantDetail.responseObject.getData().get(0).getLatitude() != null) {
                directionlattitude = MerchantDetail_Webhit_Get_getMerchantDetail.responseObject.getData().get(0).getLatitude();
            }

            if (MerchantDetail_Webhit_Get_getMerchantDetail.responseObject.getData().get(0).getLongitude() != null) {
                directionLongitute = MerchantDetail_Webhit_Get_getMerchantDetail.responseObject.getData().get(0).getLongitude();
            }


            if (AppConfig.getInstance().mUser.isUberRequired()) {


                if (!AppConfig.getInstance().checkPermission(getActivity()) ||
                        !(AppConfig.getInstance().isLocationEnabled(getActivity()))) {
                    Log.d("PERMISSS", "Not allowed ");
                    customAlert.showUberAlertDialog(getActivity(), new CustomAlertConfirmationInterface() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void callConfirmationDialogPositive() {

                            if(AppConfig.getInstance().checkPermission(getActivity()))
                            {
                                turnGPSOn();
                            }
                            else
                            {
                               requestPermission();
                            }


                        }

                        @Override
                        public void callConfirmationDialogNegative() {

                        }
                    });
                } else {
                    if (directionlattitude.length() > 0) {
                        Log.d("PERMISSS", "Allowed:" + AppConfig.getInstance().mUser.isUberRequired());
                        updateUberSDK();
                    } else {
                        Log.d("PERMISSS", "Not allowed bcoz no offers");
                    }
                }
            }
            Log.e("merchant_offerssize",MerchantDetail_Webhit_Get_getMerchantDetail.responseObject.getData().size()+"");


            if (MerchantDetail_Webhit_Get_getMerchantDetail.responseObject.getData() != null &&
                    MerchantDetail_Webhit_Get_getMerchantDetail.responseObject.getData().size() > 0) {

                Log.e("merchant_offers",MerchantDetail_Webhit_Get_getMerchantDetail.responseObject.getData().size()+"");
                for (int i = 0; i < MerchantDetail_Webhit_Get_getMerchantDetail.responseObject.getData().size(); i++) {

                    String strImageUrl = "";
                    if (MerchantDetail_Webhit_Get_getMerchantDetail.responseObject.getData().get(i).getImage() != null) {
                        strImageUrl = MerchantDetail_Webhit_Get_getMerchantDetail.responseObject.getData().get(i).getImage();
                    }

                    lstMerchantOffers.add(new DModelOutletOffers(
                            MerchantDetail_Webhit_Get_getMerchantDetail.responseObject.getData().get(i).getId(),
                            strImageUrl,
                            MerchantDetail_Webhit_Get_getMerchantDetail.responseObject.getData().get(i).getTitle(),
                            merchantName,
                            0,
                            MerchantDetail_Webhit_Get_getMerchantDetail.responseObject.getData().get(i).getSpecialType(),
                            Float.parseFloat(MerchantDetail_Webhit_Get_getMerchantDetail.
                                    responseObject.getData().get(i).getApproxSaving())));
                }
                lsvMerchantOffers.setFocusable(false);
                lsvMerchantOffers.setExpanded(true);

                if (merchantOffersAdapter != null) {
                    lsvMerchantOffers.removeFooterView(lsvFooterView);
                    merchantOffersAdapter.notifyDataSetChanged();
                } else {
                    merchantOffersAdapter = new OutletOffersAdapter(getActivity(), lstMerchantOffers);
                    lsvMerchantOffers.setAdapter(merchantOffersAdapter);
                }
            }
            llBottomCntnr.setVisibility(View.VISIBLE);
        }
    }



    public void turnGPSOn()
    {
        Log.e("checkgps","1");
        mGoogleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>()
        {
            @Override
            public void onResult(LocationSettingsResult result)
            {
                final Status status = result.getStatus();
                switch (status.getStatusCode())
                {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try
                        {
                            startIntentSenderForResult(status.getResolution().getIntentSender(), REQUEST_LOCATION, new Intent(), 0, 0, 0, null);

//                            status.startResolutionForResult(getActivity(), REQUEST_LOCATION);
                        }
                        catch (@SuppressLint("NewApi") Exception e)
                        {
                            Log.e("exc","e",e);
                        }
                        break;
                }
            }
        });
    }
    private void updateUberSDK() {
        uberConfig = new SessionConfiguration.Builder()
                // mandatory
                .setClientId(AppConstt.UberRideEstimate.CLIENT_ID)
                // required for enhanced button features
                .setServerToken(AppConstt.UberRideEstimate.SERVER_TOKEN)
                // required for implicit grant authentication
                //                .setRedirectUri("<REDIRECT_URI>")
                .setScopes(Arrays.asList(Scope.RIDE_WIDGETS))
                // optional: set sandbox as operating environment
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .build();
        UberSdk.initialize(uberConfig);
        Log.d("PERMISSS", "latitude:" + directionlattitude);

//        double pickUp_latitude, pickUp_longitude, dropoff_lat, dropoff_lng;
        pickUp_latitude = GPSTracker.lat;
        pickUp_longitude = GPSTracker.lng;
//        pickUp_latitude = 33.651962;
//        pickUp_longitude = 73.046379;
        dropoff_lat = Double.parseDouble(directionlattitude);
        dropoff_lng = Double.parseDouble(directionLongitute);
//        dropoff_lat = 33.713487;
//        dropoff_lng = 73.063656;

        RideParameters rideParams = new RideParameters.Builder()
                // Optional product_id from /v1/products endpoint (e.g. UberX). If not provided, most cost-efficient product will be used
                //                .setOfferName("a1111c8c-c720-46c3-8534-2fcdd730040d")
                // Required for price estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of dropoff location
                .setDropoffLocation(dropoff_lat,
                        dropoff_lng,
                        merchantName,
                        merchantAddress)
                // Required for pickup estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of pickup location
                .setPickupLocation(pickUp_latitude,
                        pickUp_longitude,
                        merchantName, merchantName)
                .build();

        mDeepLink = new RideRequestDeeplink.Builder(getContext())
                .setSessionConfiguration(uberConfig)
                .setFallback(Deeplink.Fallback.APP_INSTALL)
                .setRideParameters(rideParams)
                .build();
        // set parameters for the RideRequestButton instance
//        requestButton.setRideParameters(rideParams);
//
//        ServerTokenSession session = new ServerTokenSession(uberConfig);
//        requestButton.setSession(session);
//
//        callback = new RideRequestButtonCallback() {
//
//            @Override
//            public void onRideInformationLoaded() {
//                // react to the displayed estimates
//                isUberLoaded = true;
//                layout.setVisibility(View.VISIBLE);
//                rlUberEstimate.setVisibility(View.GONE);
//                Log.d("PERMISSS", "onRideInformationLoaded: ");
//            }
//
//            @Override
//            public void onError(ApiError apiError) {
//                // API error details: /docs/riders/references/api#section-errors
//                isUberLoaded = true;
//                rlUberEstimate.setVisibility(View.VISIBLE);
//                Log.d("PERMISSS", "onErrorAPI: " + apiError.getClientErrors().get(0).getCode());
////                layout.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                // Unexpected error, very likely an IOException
//                isUberLoaded = true;
//                Log.d("PERMISSS", "onError: ");
//            }
//        };
//        requestButton.setCallback(callback);
//        requestButton.loadRideInformation();
        requestUberPrice(pickUp_latitude, pickUp_longitude, dropoff_lat, dropoff_lng);
        requestUberTime(pickUp_latitude, pickUp_longitude, dropoff_lat, dropoff_lng);

    }
    private void displayLocation() {

        if (checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
//            double latitude = mLastLocation.getLatitude();
//            double longitude = mLastLocation.getLongitude();

            pickUp_latitude = mLastLocation.getLatitude();
            pickUp_longitude = mLastLocation.getLongitude();
            updateUberSDK();
            //Loge("Location_gps",latitude + ", " + longitude);
//            strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
//            requestCategoryOffers(page, strCategoryId, strSortBy, strOfferType, lat, lng, true);
//            findGirlfriendbydefaultlocation(lat,lng);
        } else {
            //Loge("check","location");
        }
    }
    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }
    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                //was crashing here
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission Granted
                    Log.e("aserwqer", "onRequestPermissionsResult: " + requestCode);
                    pickUp_latitude = GPSTracker.lat;
                    pickUp_longitude = GPSTracker.lng;
                    if (pickUp_latitude != 0) {
                        updateUberSDK();
//                        strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
//                        requestCategoryOffers(page, strCategoryId, strSortBy, strOfferType, lat, lng, true);
                    }
                    else if (!(AppConfig.getInstance().isLocationEnabled(getActivity())))
                    {
                        turnGPSOn();
                    }
                    else {
                        customAlert.showCustomAlertDialog(getActivity(), getString(R.string.gps_connection_heading), getString(R.string.gps_connection_message), null, null, false, null);
                    }

                } else {
                    String permission = Manifest.permission.ACCESS_FINE_LOCATION;
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        Log.e("aserwqer11", "onRequestPermissionsResult----: " + requestCode);
//                        if (!AppConfig.getInstance().mUser.isNeverAskActive) {
                        startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.parse("package:" + BuildConfig.APPLICATION_ID)),
                                AppConstt.IntentPreference.PACKAGE_LOCATION_INTENT_CODE);
//                        } else {
//                            AppConfig.getInstance().mUser.setNeverAskActive(true);
//                        }
                    }
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Log.e("onactivityresult",requestCode+","+resultCode);
//        if (requestCode==REQUEST_LOCATION && resultCode==getActivity().RESULT_OK)
//        {
//            updateUberSDK();
////            strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
////            requestCategoryOffers(page, strCategoryId, strSortBy, strOfferType, lat, lng, true);
//        }
        if (requestCode == REQUEST_LOCATION)
        {
            switch (resultCode)

            {
                case -1:
                    displayLocation();

                    break;

            }

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.e("loc","changed");
        pickUp_latitude=location.getLatitude();
        pickUp_longitude=location.getLongitude();
    updateUberSDK();
        //        strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
//        requestCategoryOffers(page, strCategoryId, strSortBy, strOfferType, lat, lng, true);


    }





    private void updateUberValues(boolean _isSuccess) {
        if (_isSuccess) {
            if (isPriceLoaded && isTimeLoaded) {
                llUberEstimationCntnr.setVisibility(View.VISIBLE);
                llUberbtnCntnr.setVisibility(View.VISIBLE);
            }
        } else {
            llUberbtnCntnr.setVisibility(View.VISIBLE);
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }


    public void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            if (merchantName.length() > 0) {
                iNavBarUpdateUpdateListener.setNavBarTitle(navBarTitle);
            } else {
                iNavBarUpdateUpdateListener.setNavBarTitle(getResources().getString(R.string.frg_merchant_detail));
            }
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSION_REQUEST_CODE:
//                //was crashing here
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission Granted
//                    Log.d("aserwqer", "onRequestPermissionsResult: " + requestCode);
//                    updateUberSDK();
//
//                } else {
//                    String permission = Manifest.permission.ACCESS_FINE_LOCATION;
//                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
//                    if (!showRationale) {
//                        Log.d("aserwqer", "onRequestPermissionsResult----: " + requestCode);
//                        if (!AppConfig.getInstance().mUser.isNeverAskActive) {
//                            startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                                    Uri.parse("package:" + BuildConfig.APPLICATION_ID)), AppConstt.IntentPreference.PACKAGE_LOCATION_INTENT_CODE);
//                        } else {
//                            AppConfig.getInstance().mUser.setNeverAskActive(true);
//                        }
//                    }
//                }
//
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (AppConstt.IntentPreference.PACKAGE_LOCATION_INTENT_CODE == requestCode) {
//            if (AppConfig.getInstance().checkPermission(getContext())) {
//                updateUberSDK();
//            }
//        } else if (AppConstt.IntentPreference.SOURCE_LOCATION_INTENT_CODE == requestCode) {
//
//            if (AppConfig.getInstance().isLocationEnabled(getContext())) {
//                if (!AppConfig.getInstance().checkPermission(getContext())) {
//                    requestPermission();
//                } else {
//                    updateUberSDK();
//                }
//            } else {
//                Log.d("sadSDSAASD", "onCreateView:5520 ");
//            }
//
//        }
//    }
}
