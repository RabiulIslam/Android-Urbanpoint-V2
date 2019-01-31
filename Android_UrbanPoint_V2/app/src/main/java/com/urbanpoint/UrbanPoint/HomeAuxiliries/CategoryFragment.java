package com.urbanpoint.UrbanPoint.HomeAuxiliries;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
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
//import com.google.firebase.analytics.FirebaseAnalytics;
import com.uber.sdk.rides.client.model.Vehicle;
import com.urbanpoint.UrbanPoint.BuildConfig;
import com.urbanpoint.UrbanPoint.CommonFragments.MerchantDetailFragment;
import com.urbanpoint.UrbanPoint.CommonFragments.OfferDetailFragment;
import com.urbanpoint.UrbanPoint.CommonFragments.WebServices.OfferDetail_Webhit_Get_getOfferDetail;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.WebServices.CategoryOffers_Webhit_Get_getOutlet;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.WebServices.LocationPermission_WebHit_Post_updatePermission;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.CustomAlertConfirmationInterface;
//import com.urbanpoint.UrbanPoint.Utils.GPSTracker;
//import com.urbanpoint.UrbanPoint.Utils.Findmygirlfriend;
import com.urbanpoint.UrbanPoint.Utils.GPSTracker;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private ExpandableListView lsvOutlets;
    private ArrayList<DModelMerchintList> lstOutlets;
    private ArrayList<DModelMerchintList.Child> lstChild;
    private ExpandableMerchintListAdapter expandableMerchintListAdapter;
    private RelativeLayout rlAlphabetically, rlLocation, rlGenderCntnr, rlGenderSpecific, rlAllOffers;
    private TextView txvLocation, txvAlphabetically, txvGenderSpecific, txvAllOffers, txvNotFound;
    private View lsvFooterView;
    private double lat, lng;
    private String strSortBy, strOfferType, strCategoryId, strCategoryName;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private int page, listSizeOffset;
    private boolean isGenderRequired;
    private boolean shouldGetMoreOffers, isAlreadyfetchingOffers;
    private LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;
    final static int REQUEST_LOCATION = 199;
    final static int MY_PERMISSIONS_REQUEST_LOCATION = 1999;
    android.location.Location mLastLocation;
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    CustomAlert customAlert;
    ProgressDilogue progressDilogue;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_outlets, null);
        initialize();
        bindViews(view);

        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle(strCategoryName);
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        if (strCategoryId.equalsIgnoreCase("64")) {
            rlGenderCntnr.setVisibility(View.VISIBLE);
            isGenderRequired = true;
            if (AppConfig.getInstance().mUser.getmGender().equalsIgnoreCase(AppConstt.Gender.FEMALE)) {
                strOfferType = AppConstt.DEFAULT_VALUES.Female;
            } else {
                strOfferType = AppConstt.DEFAULT_VALUES.Male;
            }
            updateBtnGenderSpecific();
        } else {
            rlGenderCntnr.setVisibility(View.GONE);
            isGenderRequired = false;
        }


        if (!AppConfig.getInstance().checkPermission(getActivity()) ||
                !(AppConfig.getInstance().isLocationEnabled(getActivity()))) {
            updatebtnAlpabetical();
            requestCategoryOffers(page, strCategoryId, strSortBy, strOfferType, lat, lng, true);
        } else {
            updateBtnLocation();
            lat = GPSTracker.lat;
            lng = GPSTracker.lng;
            if (lat != 0) {
                requestCategoryOffers(page, strCategoryId, strSortBy, strOfferType, lat, lng, true);
            } else {
                updatebtnAlpabetical();
                rlAlphabetically.performClick();
            }
        }
        lsvOutlets.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (shouldGetMoreOffers && !isAlreadyfetchingOffers) {
                    if (expandableMerchintListAdapter != null) {
                        if ((lsvOutlets.getLastVisiblePosition() - listSizeOffset == (expandableMerchintListAdapter.getGroupCount() - 1))) {
                            page++;
                            lsvOutlets.addFooterView(lsvFooterView);
                            expandableMerchintListAdapter.notifyDataSetChanged();
                            lsvOutlets.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
                            requestCategoryOffers(page, strCategoryId, strSortBy, strOfferType, lat, lng, false);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        //Following Both listener is for adjusting the list size
        lsvOutlets.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                listSizeOffset = listSizeOffset + lstOutlets.get(groupPosition).getChild().size();
                Log.d("OUTLETSSSSSS", "onGroupExpand: " + listSizeOffset);
            }
        });
        lsvOutlets.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                listSizeOffset = listSizeOffset - lstOutlets.get(groupPosition).getChild().size();
                Log.d("OUTLETSSSSSS", "onGroupCollapse: " + listSizeOffset);

            }
        });
        return view;
    }


    private void initialize() {
        page = 1;
        listSizeOffset = 0;
        strOfferType = AppConstt.DEFAULT_VALUES.Both;
        isGenderRequired = false;
        strSortBy = "";
        strCategoryId = "";
        strCategoryName = getActivity().getResources().getString(R.string.frg_category_fragment);
        lat = AppConstt.DEFAULT_VALUES.DEFAULT_LAT;
        lng = AppConstt.DEFAULT_VALUES.DEFAULT_LNG;
        shouldGetMoreOffers = true;
        isAlreadyfetchingOffers = false;
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        lstOutlets = new ArrayList<>();


        Bundle b = this.getArguments();
        if (b != null) {
            strCategoryId = b.getString(AppConstt.BundleStrings.categoryId);
            strCategoryName = b.getString(AppConstt.BundleStrings.categoryName);
        }
    }

    private void bindViews(View frg) {
        txvNotFound = frg.findViewById(R.id.frg_category_outlets_txv_nt_found);
        lsvOutlets = frg.findViewById(R.id.frg_category_outlets_lsv);
        txvAlphabetically = frg.findViewById(R.id.frg_category_outlets_txv_alphabetically);
        txvLocation = frg.findViewById(R.id.frg_category_outlets_txv_location);
        txvGenderSpecific = frg.findViewById(R.id.frg_category_outlets_txv_male);
        txvAllOffers = frg.findViewById(R.id.frg_category_outlets_txv_female);
        rlGenderCntnr = frg.findViewById(R.id.frg_category_outlets_rl_btns_gender_cntnr);
        rlGenderSpecific = frg.findViewById(R.id.frg_category_outlets_rl_gender_specific);
        rlAllOffers = frg.findViewById(R.id.frg_category_outlets_rl_gender_alloffers);
        rlAlphabetically = frg.findViewById(R.id.frg_category_outlets_rl_alphabetically);
        rlLocation = frg.findViewById(R.id.frg_category_outlets_rl_location);
        lsvFooterView = ((LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.lsv_footer, null, false);
        buildGoogleApiClient();
        createLocationRequest();
        rlAlphabetically.setOnClickListener(this);
        rlLocation.setOnClickListener(this);
        rlGenderSpecific.setOnClickListener(this);
        rlAllOffers.setOnClickListener(this);


        lsvOutlets.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                int offerId = Integer.parseInt(lstOutlets.get(groupPosition).getChild().get(childPosition).getProductId());
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstt.BundleStrings.offerId, offerId);
                bundle.putString(AppConstt.BundleStrings.offerName, lstOutlets.get(groupPosition).getChild().get(childPosition).getName());
                navToOfferDetailFragment(bundle);
                return false;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_category_outlets_rl_alphabetically:
                if (!isAlreadyfetchingOffers) {
                    shouldGetMoreOffers = true;
                    page = 1;
                    listSizeOffset = 0;
                    strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_ALPHABETICALLY;
                    requestCategoryOffers(page, strCategoryId, strSortBy, strOfferType, lat, lng, true);
                }
                break;

            case R.id.frg_category_outlets_rl_location:
                if (!isAlreadyfetchingOffers) {
                    shouldGetMoreOffers = true;
                    listSizeOffset = 0;
                    page = 1;

                   if( AppConfig.getInstance().checkPermission(getActivity()))
                    {
                        Log.e("checkloc","1");
                        if (AppConfig.getInstance().isLocationEnabled(getActivity())) {
                            Log.e("checkloc","2");
                            lat = GPSTracker.lat;
                            lng = GPSTracker.lng;
                            if (lat != 0) {
                                Log.e("checkloc","3");
                                strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
                                requestCategoryOffers(page, strCategoryId, strSortBy, strOfferType, lat, lng, true);
                            } else {
                                Log.e("checkloc","4");

                                displayLocation();
                            }
                        }
                        else
                        {
                            turnGPSOn();
                            customAlert.showCustomAlertDialog(getActivity(), getString(R.string.gps_connection_heading), getString(R.string.gps_connection_message), null, null, false, null);
//
//                            GPSTracker.enqueueWork(getActivity(),new Intent());
                        }
                    }
                    else
                    {
                        requestPermission();
                    }

                }
                break;

            case R.id.frg_category_outlets_rl_gender_specific:
                if (AppConfig.getInstance().mUser.getmGender().equalsIgnoreCase(AppConstt.Gender.FEMALE)) {
                    strOfferType = AppConstt.DEFAULT_VALUES.Female;
                } else {
                    strOfferType = AppConstt.DEFAULT_VALUES.Male;
                }
                page = 1;
                listSizeOffset = 0;
                shouldGetMoreOffers = true;
                requestCategoryOffers(page, strCategoryId, strSortBy, strOfferType, lat, lng, true);

                break;

            case R.id.frg_category_outlets_rl_gender_alloffers:
                strOfferType = AppConstt.DEFAULT_VALUES.Both;
                page = 1;
                listSizeOffset = 0;
                shouldGetMoreOffers = true;
                requestCategoryOffers(page, strCategoryId, strSortBy, strOfferType, lat, lng, true);

                break;
        }
    }

    private void displayLocation() {

        if (checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mGoogleApiClient.isConnected()) {
            if (mLastLocation == null) {
                mLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                        mLocationRequest, (LocationListener) this);

            } else {

                Log.e("location_resulttttt", mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());
                lat = mLastLocation.getLatitude();
                lng = mLastLocation.getLongitude();

                strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
                requestCategoryOffers(page, strCategoryId, strSortBy, strOfferType, lat, lng, true);

            }
        }
        else
        {
            mGoogleApiClient.connect();

        }

    }
    private void navToOfferDetailFragment(Bundle _bundle) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new OfferDetailFragment();
        frg.setArguments(_bundle);
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.OfferDetailFragment);
        ft.addToBackStack(AppConstt.FRGTAG.OfferDetailFragment);
        ft.hide(this);
        ft.commit();

    }

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

    private void requestSetLocationPermission(int _permission) {
        LocationPermission_WebHit_Post_updatePermission locationPermission_webHit_post_updatePermission = new LocationPermission_WebHit_Post_updatePermission();
        locationPermission_webHit_post_updatePermission.setLocationPermission(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {

            }

            @Override
            public void onWebException(Exception ex) {

            }

            @Override
            public void onWebLogout() {

            }
        }, _permission);
    }

    private void requestCategoryOffers(int _page, final String _categoryId, String _sortBy,
                                       String _genderType, final double lat, double lng, final boolean _shouldClearLst) {
        Log.e("request","offers");
        if (_shouldClearLst) {
            progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        }
        isAlreadyfetchingOffers = true;
        CategoryOffers_Webhit_Get_getOutlet categoryOffers_webhit_get_getOutlet = new CategoryOffers_Webhit_Get_getOutlet();
        categoryOffers_webhit_get_getOutlet.requestCategoryOffers(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                isAlreadyfetchingOffers = false;
                if (isSuccess) {
                    if (_shouldClearLst) {
                        lstOutlets.clear();
                        expandableMerchintListAdapter = null;
                    }
                    logFireBaseAndFaceBookEvent(_categoryId);
                    updateList(lat);
                } else if (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.DATABASE_NOT_FOUND + "")) {
                    Log.d("dsafasdfasdfdsa", "onWebResult: 404");
                    shouldGetMoreOffers = false;
                    if (lstOutlets.size() == 0) {
                        lsvOutlets.setVisibility(View.GONE);
                        txvNotFound.setVisibility(View.VISIBLE);
                    } else {
                        lsvOutlets.removeFooterView(lsvFooterView);
                        expandableMerchintListAdapter.notifyDataSetChanged();
                    }
                } else {
                    lsvOutlets.setVisibility(View.GONE);
                    txvNotFound.setVisibility(View.VISIBLE);
                    if (customAlert != null)
                        customAlert.showCustomAlertDialog(getActivity(), null, strMsg, null, null, false, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                isAlreadyfetchingOffers = false;
                shouldGetMoreOffers = true;
                lsvOutlets.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
                if (customAlert != null)
                    customAlert.showCustomAlertDialog(getActivity(), null, ex.getMessage(), null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();
                lsvOutlets.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
                isAlreadyfetchingOffers = false;
                shouldGetMoreOffers = true;

            }
        }, _page, _categoryId, _sortBy, _genderType, lat, lng, isGenderRequired);
    }

    private void updateList(double _lat) {
        boolean isDistanceRequired;
        if (strSortBy.equalsIgnoreCase(AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION)) {
            updateBtnLocation();
            if (_lat > 0) {
                isDistanceRequired = true;
            } else {
                isDistanceRequired = false;
            }

        } else {
            isDistanceRequired=false;
            updatebtnAlpabetical();
        }


//        if (_lat > 0) {
//            isDistanceRequired = true;
//        } else {
//            isDistanceRequired = false;
//        }

        if ((strOfferType.equalsIgnoreCase(AppConstt.DEFAULT_VALUES.Male)) ||
                (strOfferType.equalsIgnoreCase(AppConstt.DEFAULT_VALUES.Female))) {
            updateBtnGenderSpecific();
        } else if (strOfferType.equalsIgnoreCase(AppConstt.DEFAULT_VALUES.Both)) {
            updateBtnAllOffers();
        }

        if (CategoryOffers_Webhit_Get_getOutlet.responseObject != null &&
                CategoryOffers_Webhit_Get_getOutlet.responseObject.getData() != null &&
                CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().size() > 0) {

            if (CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().size() < 20) {
                Log.d("dsafasdfasdfdsa", "onUpdateList: " + CategoryOffers_Webhit_Get_getOutlet.
                        responseObject.getData().size());
                shouldGetMoreOffers = false;
            }

            for (int i = 0; i < CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().size(); i++) {
                if (CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getOffers() != null) {
                    String Gfestival = "";
                    lstChild = new ArrayList<>();
                    for (int j = 0; j < CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getOffers().size(); j++) {
                        String Cfestival;
                        String special = "";
                        if (CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecial() != null
                                && CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecial().length() > 0) {
                            special = CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecial();
                        } else {
                            special = "0";
                        }


                        if (CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecialType() != null &&
                                CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecialType().length() > 0) {
                            Cfestival = CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecialType();
                            Gfestival = CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getSpecialType();
                        } else {
                            Cfestival = "";
                        }

                        String strImageUrl = "";
                        String isRedeem="";
                        if (CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getImage() != null) {
                            strImageUrl = CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getImage();
                        }
                        if (AppConfig.getInstance().mUser.isSubscribed()
                                || (Float.parseFloat(CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getApproxSaving())
                                <= AppConfig.getInstance().mUser.getWallet()))
                        {
                           isRedeem="1";
                        }

                        else
                        {
                            isRedeem="0";
                        }
                      //  CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getPrice()
                        lstChild.add(new DModelMerchintList.Child(
                                CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getId(),
                                strImageUrl,
                                CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getOffers().get(j).getTitle(),
                                special,
                                Cfestival,
                                isRedeem,
                                "", ""));
                    }
                    int distance;
                    if (CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getDistance() >= 0) {
                        distance = (int) CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getDistance();
                    } else {
                        distance = 0;
                    }

                    String strImageUrl = "";
                    if (CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getImage() != null) {
                        strImageUrl = CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getImage();
                    }
                    //CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).get
                    lstOutlets.add(new DModelMerchintList(
                            CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getId(),
                            CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getName(),
                            CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getAddress(),
                            CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getTimings(),
                            CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getDescription(),
                            distance,
                            isDistanceRequired,
                            CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getSpecial(),
                            Gfestival,
                            strImageUrl,
                            CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getLogo(),
                            CategoryOffers_Webhit_Get_getOutlet.responseObject.getData().get(i).getPhone(),
                            lstChild

                    ));

                    if (strSortBy.equalsIgnoreCase(AppConstt.DEFAULT_VALUES.SORT_BY_ALPHABETICALLY)) {
                        Collections.sort(lstOutlets, new Comparator<DModelMerchintList>() {
                            public int compare(DModelMerchintList v1, DModelMerchintList v2) {
                                return v1.getMerchantName().compareTo(v2.getMerchantName());
                            }
                        });
                    }
                    else
                    {
                        Collections.sort(lstOutlets, new Comparator<DModelMerchintList>() {
                            public int compare(DModelMerchintList v1, DModelMerchintList v2) {
                                return v1.getMerchantDistance()- v2.getMerchantDistance();
                            }
                        });
                    }

                }
            }

            lsvOutlets.setVisibility(View.VISIBLE);
            txvNotFound.setVisibility(View.GONE);
            lsvOutlets.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);

            if (expandableMerchintListAdapter != null) {
                lsvOutlets.removeFooterView(lsvFooterView);
                expandableMerchintListAdapter.notifyDataSetChanged();
            } else {
                expandableMerchintListAdapter = new ExpandableMerchintListAdapter(getContext(), this, lstOutlets);
                lsvOutlets.setAdapter(expandableMerchintListAdapter);
            }
        }
    }

    private void updateBtnGenderSpecific() {
        if (strOfferType.equalsIgnoreCase(AppConstt.DEFAULT_VALUES.Female)) {
            txvGenderSpecific.setText(getString(R.string.gender_ladies));
        } else {
            txvGenderSpecific.setText(getString(R.string.gender_gents));
        }
        rlAllOffers.setBackground(getResources().getDrawable(R.color.white));
        rlGenderSpecific.setBackground(getResources().getDrawable(R.drawable.orange_gadient_rectngle_square));
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
//      displayLocation();
    }

    private void updateBtnAllOffers() {
        rlGenderSpecific.setBackground(getResources().getDrawable(R.color.white));
        rlAllOffers.setBackground(getResources().getDrawable(R.drawable.orange_gadient_rectngle_square));
    }

    private void updatebtnAlpabetical() {
        strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_ALPHABETICALLY;
        rlLocation.setBackground(getResources().getDrawable(R.drawable.gray_background_border));
        txvLocation.setTextColor(Color.BLACK);
        rlAlphabetically.setBackground(getResources().getDrawable(R.drawable.orange_gadient_rectngle_rounded));
        txvAlphabetically.setTextColor(Color.WHITE);
    }

    private void updateBtnLocation() {
        strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
        rlAlphabetically.setBackground(getResources().getDrawable(R.drawable.gray_background_border));
        txvAlphabetically.setTextColor(Color.BLACK);
        rlLocation.setBackground(getResources().getDrawable(R.drawable.orange_gadient_rectngle_rounded));
        txvLocation.setTextColor(Color.WHITE);
    }

    private void logFireBaseAndFaceBookEvent(String _caegoryId) {
        Bundle params = new Bundle();
        params.putString("user_id", AppConfig.getInstance().mUser.getmUserId());
        params.putString("device_type", "Android");
        // Send the event

        switch (_caegoryId) {
            case "17":
//                FirebaseAnalytics.getInstance(getActivity())    // FireBaseEvent Logger
//                        .logEvent(AppConstt.FireBaseEvents.Food_And_Drink_Views, params);
//                AppEventsLogger.newLogger(getActivity())        // FacebookEvent Logger
//                        .logEvent(AppConstt.FireBaseEvents.Food_And_Drink_Views);
//                break;
//            case "64":
//                FirebaseAnalytics.getInstance(getActivity())
//                        .logEvent(AppConstt.FireBaseEvents.Beauty_And_Health_Views, params);
//                AppEventsLogger.newLogger(getActivity())
//                        .logEvent(AppConstt.FireBaseEvents.Beauty_And_Health_Views);
//                break;
//            case "15":
//                FirebaseAnalytics.getInstance(getActivity())
//                        .logEvent(AppConstt.FireBaseEvents.Fun_And_Leisure_Views, params);
//                AppEventsLogger.newLogger(getActivity())
//                        .logEvent(AppConstt.FireBaseEvents.Fun_And_Leisure_Views);
//                break;
//            case "65":
//                FirebaseAnalytics.getInstance(getActivity())
//                        .logEvent(AppConstt.FireBaseEvents.Retail_And_Services_Views, params);
//                AppEventsLogger.newLogger(getActivity())
//                        .logEvent(AppConstt.FireBaseEvents.Retail_And_Services_Views);
//                break;
        }
    }

    public void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            iNavBarUpdateUpdateListener.setNavBarTitle(strCategoryName);
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
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

                    if (AppConfig.getInstance().isLocationEnabled(getActivity())) {


                        if (mGoogleApiClient.isConnected()) {


                            @SuppressLint("MissingPermission") Location mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                            if (mLocation == null) {
                                mLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                                        mLocationRequest, (LocationListener) this);
                            }
                            else {
                                lat = mLocation.getLatitude();
                                lng = mLocation.getLongitude();
                                Log.e("location_onconnected", lat + "," + lng);
                                strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
                                updateBtnLocation();
                                requestCategoryOffers(page, strCategoryId, strSortBy, strOfferType, lat, lng, true);
                            }

                        }
                        else
                        {
                            mGoogleApiClient.connect();

                        }
                    }
                    else

                    {
                        turnGPSOn();
                    }

                } else {
                    String permission = Manifest.permission.ACCESS_FINE_LOCATION;
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        Log.e("aserwqer11", "onRequestPermissionsResult----: " + requestCode);

                            startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + BuildConfig.APPLICATION_ID)),
                                    AppConstt.IntentPreference.PACKAGE_LOCATION_INTENT_CODE);

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

     Location   mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

         if(mLocation == null)
         {
             mLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
             LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                     mLocationRequest, (LocationListener) this);
         }
    else
     {

         lat=mLocation.getLatitude();
         lng=mLocation.getLongitude();
         Log.e("location_onconnected",lat+","+lng);
         strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
         updateBtnLocation();
         requestCategoryOffers(page, strCategoryId, strSortBy, strOfferType, lat, lng, true);


     }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

     Log.e("location1122344","changed");
        lat=location.getLatitude();
        lng=location.getLongitude();
        strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
        requestCategoryOffers(page, strCategoryId, strSortBy, strOfferType, lat, lng, true);


    }







}
