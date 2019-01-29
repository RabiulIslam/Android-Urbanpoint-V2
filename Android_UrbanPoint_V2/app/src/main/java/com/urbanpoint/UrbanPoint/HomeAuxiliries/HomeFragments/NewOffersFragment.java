package com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.BuildConfig;
import com.urbanpoint.UrbanPoint.CommonFragments.OfferDetailFragment;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.DModelHomeGrdVw;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragments.WebServices.NewOffers_Webhit_Get_getOffers;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.OffersAdapter;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.WebServices.LocationPermission_WebHit_Post_updatePermission;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.CustomAlertConfirmationInterface;
import com.urbanpoint.UrbanPoint.Utils.GPSTracker;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;
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
import java.util.ArrayList;
import java.util.List;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewOffersFragment extends Fragment implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {


    private ListView lsvNewOffers;
    private List<DModelHomeGrdVw> lstNewOffers;
    private OffersAdapter offersAdapter;
    private RelativeLayout rlAlphabetically, rlLocation;
    private TextView txvLocation, txvAlphabetically, txvNotFound;
    private View lsvFooterView;
    private double lat, lng;
    private boolean isLocationSort;
    private boolean isSubscribed;
    private String strSortBy;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private int page;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_offers, null);
        initialize();
        bindViews(view);

        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_new_offers));
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        lsvNewOffers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int offerId = Integer.parseInt(lstNewOffers.get(position).getStrProductId());
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstt.BundleStrings.offerId, offerId);
                bundle.putString(AppConstt.BundleStrings.offerName, lstNewOffers.get(position).getStrOfferName());
                navToOfferDetailFragment(bundle);
            }
        });

        lsvNewOffers.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (shouldGetMoreOffers && !isAlreadyfetchingOffers) {
                    if (offersAdapter != null) {
                        if ((lsvNewOffers.getLastVisiblePosition() == (offersAdapter.getCount() - 1))) {
                            page++;
                            lsvNewOffers.addFooterView(lsvFooterView);
                            offersAdapter.notifyDataSetChanged();
                            lsvNewOffers.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
                            requestNewOffers(page, strSortBy, lat, lng, false);
                        }
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        return view;
    }


    private void initialize() {
        page = 1;
        strSortBy = "";
        lat = AppConstt.DEFAULT_VALUES.DEFAULT_LAT;
        lng = AppConstt.DEFAULT_VALUES.DEFAULT_LNG;
        isLocationSort = true;
        shouldGetMoreOffers = true;
        isAlreadyfetchingOffers = false;
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        lstNewOffers = new ArrayList<>();
    }

    private void bindViews(View frg) {
        txvNotFound = frg.findViewById(R.id.frg_new_offers_txv_nt_found);
        lsvNewOffers = frg.findViewById(R.id.frg_new_offers_lst_view);
        txvAlphabetically = frg.findViewById(R.id.frg_fav_txv_alphabetically);
        txvLocation = frg.findViewById(R.id.frg_fav_txv_location);
        rlAlphabetically = frg.findViewById(R.id.frg_fav_rl_alphabetically);
        rlLocation = frg.findViewById(R.id.frg_fav_rl_location);
        lsvFooterView = ((LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.lsv_footer, null, false);

        buildGoogleApiClient();
        createLocationRequest();
        rlAlphabetically.setOnClickListener(this);
        rlLocation.setOnClickListener(this);

        if (!AppConfig.getInstance().checkPermission(getActivity()) ||
                !(AppConfig.getInstance().isLocationEnabled(getActivity()))) {
            updatebtnAlpabetical();
            requestNewOffers(page, strSortBy, lat, lng, true);
        } else {
            updateBtnLocation();
            lat = GPSTracker.lat;
            lng = GPSTracker.lng;
            if (lat != 0) {
                requestNewOffers(page, strSortBy, lat, lng, true);
            } else {
                updatebtnAlpabetical();
                rlAlphabetically.performClick();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_fav_rl_alphabetically:
                if (!isAlreadyfetchingOffers) {
                    shouldGetMoreOffers = true;
                    page = 1;
                    strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_ALPHABETICALLY;
                    requestNewOffers(page, strSortBy, lat, lng, true);
                }
                break;

            case R.id.frg_fav_rl_location:
                if (!isAlreadyfetchingOffers) {
                    shouldGetMoreOffers = true;
                    page = 1;
                    strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
//
                    if (AppConfig.getInstance().checkPermission(getActivity())) {
                        Log.e("checkloc", "1");
                        if (AppConfig.getInstance().isLocationEnabled(getActivity())) {
                            Log.e("checkloc", "2");
                            lat = GPSTracker.lat;
                            lng = GPSTracker.lng;
                            if (lat != 0) {
                                Log.e("checkloc", "3");
                                strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
                                requestNewOffers(page, strSortBy, lat, lng, true);
                            } else {
                                Log.e("checkloc", "4");

//                                customAlert.showCustomAlertDialog(getActivity(), getString(R.string.gps_connection_heading), getString(R.string.gps_connection_message), null, null, false, null);
//
                                displayLocation();
                            }
                        } else {
                            turnGPSOn();
//                            customAlert.showCustomAlertDialog(getActivity(), getString(R.string.gps_connection_heading), getString(R.string.gps_connection_message), null, null, false, null);
//
//                            GPSTracker.enqueueWork(getActivity(),new Intent());
                        }
                    } else {
                        requestPermission();
                    }
                    break;
                }
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
//      displayLocation();
    }

    
    
    private void displayLocation()
    {

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
                requestNewOffers(page, strSortBy, lat, lng, false);
            }
        }
        else
        {
            mGoogleApiClient.connect();

        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                //was crashing here
//

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (AppConfig.getInstance().isLocationEnabled(getActivity())) {


                        if (mGoogleApiClient.isConnected()) {


                            Location mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

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
                                requestNewOffers(page, strSortBy, lat, lng, false);           }

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

    public void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
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

    private void requestNewOffers(int _page, String _sortBy, final double lat, double lng, boolean _shouldClearLst) {
        if (_shouldClearLst) {
            progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
            lstNewOffers.clear();
            offersAdapter = null;
        }
        isAlreadyfetchingOffers = true;
        NewOffers_Webhit_Get_getOffers newOffers_webhit_get_getOffers = new NewOffers_Webhit_Get_getOffers();
        newOffers_webhit_get_getOffers.requestNewOffers(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                isAlreadyfetchingOffers = false;
                if (isSuccess) {
                    updateList(lat);
                } else if (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.DATABASE_NOT_FOUND + "")) {
                    shouldGetMoreOffers = false;
                    if (lstNewOffers.size() == 0) {
                        lsvNewOffers.setVisibility(View.GONE);
                        txvNotFound.setVisibility(View.VISIBLE);
                    }else {
                        lsvNewOffers.removeFooterView(lsvFooterView);
                        offersAdapter.notifyDataSetChanged();
                    }
                } else {
                    lsvNewOffers.setVisibility(View.GONE);
                    txvNotFound.setVisibility(View.VISIBLE);
                    customAlert.showCustomAlertDialog(getActivity(), null, strMsg, null, null, false, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                isAlreadyfetchingOffers = false;
                lsvNewOffers.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
                customAlert.showCustomAlertDialog(getActivity(), null, ex.getMessage(), null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();
                lsvNewOffers.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
                isAlreadyfetchingOffers = false;

            }
        }, _page, _sortBy, lat, lng, false);
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

    private void updateList(double _lat) {

        boolean isDistanceRequired;

        if (strSortBy.equalsIgnoreCase(AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION)) {
            updateBtnLocation();
            if (_lat>0) {
                isDistanceRequired = true;
            } else {
                isDistanceRequired = false;
            }
        } else {
            updatebtnAlpabetical();
            isDistanceRequired=false;
        }


        if (NewOffers_Webhit_Get_getOffers.responseObject != null &&
                NewOffers_Webhit_Get_getOffers.responseObject.getData() != null &&
                NewOffers_Webhit_Get_getOffers.responseObject.getData().size() > 0) {
            AppConfig.getInstance().mUserBadges.setNewOfferCount(0);
            if (NewOffers_Webhit_Get_getOffers.responseObject.getData().size() < 20) {
                shouldGetMoreOffers = false;
            }


            for (int i = 0; i < NewOffers_Webhit_Get_getOffers.responseObject.getData().size(); i++) {
                String festival;
                if (NewOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getSpecialType() != null) {
                    festival = NewOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getSpecialType();
                } else {
                    festival = "";
                }
                double distance;
                if (NewOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getDistance() >0) {
                    distance = (NewOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getDistance());
                } else {
                    distance = -1;
                }

                String strImageUrl = "";
                if (NewOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getImage() != null) {
                    strImageUrl = NewOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getImage();
                }
                lstNewOffers.add(new DModelHomeGrdVw(
                        strImageUrl,
                        NewOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getTitle(),
                        NewOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getId(),
                        NewOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getName(),
                        NewOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getSpecial(),
                        festival, (int) distance, isDistanceRequired,
                        Float.parseFloat(NewOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getApproxSaving())
                ));
            }

            lsvNewOffers.setVisibility(View.VISIBLE);
            txvNotFound.setVisibility(View.GONE);
            lsvNewOffers.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);

            if (offersAdapter != null) {
                lsvNewOffers.removeFooterView(lsvFooterView);
                 offersAdapter.notifyDataSetChanged();
            } else {
                offersAdapter = new OffersAdapter(getContext(), lstNewOffers, AppConfig.getInstance().mUser.isSubscribed());
                lsvNewOffers.setAdapter(offersAdapter);
            }
        }
    }

    private void updatebtnAlpabetical() {
        strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_ALPHABETICALLY;
        rlLocation.setBackground(getResources().getDrawable(R.drawable.gray_background_border));
        txvLocation.setTextColor(Color.BLACK);
        rlAlphabetically.setBackground(getResources().getDrawable(R.drawable.orange_gadient_rectngle_rounded));
        txvAlphabetically.setTextColor(Color.WHITE);
        isLocationSort = false;
    }

    private void updateBtnLocation() {
        strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
        rlAlphabetically.setBackground(getResources().getDrawable(R.drawable.gray_background_border));
        txvAlphabetically.setTextColor(Color.BLACK);
        rlLocation.setBackground(getResources().getDrawable(R.drawable.orange_gadient_rectngle_rounded));
        txvLocation.setTextColor(Color.WHITE);
        isLocationSort = true;
    }



    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            iNavBarUpdateUpdateListener.setNavBarTitle(getResources().getString(R.string.frg_new_offers));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
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
            requestNewOffers(page, strSortBy, lat, lng, true);


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

        Log.e("loc","changed");
        lat=location.getLatitude();
        lng=location.getLongitude();
        strSortBy = AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION;
        requestNewOffers(page, strSortBy, lat, lng, false);

    }

}
