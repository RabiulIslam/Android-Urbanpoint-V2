package com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewOffersFragment extends Fragment implements View.OnClickListener {

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


        rlAlphabetically.setOnClickListener(this);
        rlLocation.setOnClickListener(this);

        if (!AppConfig.getInstance().checkPermission(getActivity()) || !(AppConfig.getInstance().isLocationEnabled(getActivity()))) {
            updatebtnAlpabetical();
            requestNewOffers(page, strSortBy, lat, lng, true);
        } else {
            lat = GPSTracker.getInstance(getContext()).getLatitude();
            lng = GPSTracker.getInstance(getContext()).getLongitude();
            if (lat != 0) {
                updateBtnLocation();
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
                    if (!AppConfig.getInstance().isLocationEnabled(getActivity()) || !AppConfig.getInstance().checkPermission(getActivity())) {
                        customAlert.showContextualAlertDialog(getActivity(), new CustomAlertConfirmationInterface() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void callConfirmationDialogPositive() {
                                requestSetLocationPermission(1);
                                if (AppConfig.getInstance().isLocationEnabled(getContext())) {
                                    requestPermission();
                                } else {
                                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), AppConstt.IntentPreference.SOURCE_LOCATION_INTENT_CODE);
                                }
                            }

                            @Override
                            public void callConfirmationDialogNegative() {
                                requestSetLocationPermission(0);
                            }
                        });
                    } else {
                        lat = GPSTracker.getInstance(getContext()).getLatitude();
                        lng = GPSTracker.getInstance(getContext()).getLongitude();
                        if (lat != 0) {
                            requestNewOffers(page, strSortBy, lat, lng, true);
                        } else {
                            customAlert.showCustomAlertDialog(getActivity(), getString(R.string.gps_connection_heading), getString(R.string.gps_connection_message), null, null, false, null);
                        }
                    }
                }
                break;
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
        if (strSortBy.equalsIgnoreCase(AppConstt.DEFAULT_VALUES.SORT_BY_LOCATION)) {
            updateBtnLocation();
        } else {
            updatebtnAlpabetical();
        }

        boolean isDistanceRequired;
        if (_lat>0) {
            isDistanceRequired = true;
        } else {
            isDistanceRequired = false;
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
                        festival,
                        (int) distance,
                        isDistanceRequired
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                //was crashing here
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Log.d("aserwqer", "onRequestPermissionsResult: " + requestCode);
                    lat = GPSTracker.getInstance(getContext()).getLatitude();
                    lng = GPSTracker.getInstance(getContext()).getLongitude();
                    if (lat != 0) {
                        requestNewOffers(page, strSortBy, lat, lng, true);
                    } else {
                        customAlert.showCustomAlertDialog(getActivity(), getString(R.string.gps_connection_heading), getString(R.string.gps_connection_message), null, null, false, null);
                    }
                } else {
                    String permission = Manifest.permission.ACCESS_FINE_LOCATION;
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        Log.d("aserwqer", "onRequestPermissionsResult----: " + requestCode);
                        if (!AppConfig.getInstance().mUser.isNeverAskActive) {
                            startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + BuildConfig.APPLICATION_ID)), AppConstt.IntentPreference.PACKAGE_LOCATION_INTENT_CODE);
                        } else {
                            AppConfig.getInstance().mUser.setNeverAskActive(true);
                        }
                    }
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstt.IntentPreference.PACKAGE_LOCATION_INTENT_CODE == requestCode) {
            if (AppConfig.getInstance().checkPermission(getContext())) {
                lat = GPSTracker.getInstance(getContext()).getLatitude();
                lng = GPSTracker.getInstance(getContext()).getLongitude();
                if (lat != 0) {
                    requestNewOffers(page, strSortBy, lat, lng, true);
                } else {
                    customAlert.showCustomAlertDialog(getActivity(), getString(R.string.gps_connection_heading), getString(R.string.gps_connection_message), null, null, false, null);
                }
            }
        } else if (AppConstt.IntentPreference.SOURCE_LOCATION_INTENT_CODE == requestCode) {

            if (AppConfig.getInstance().isLocationEnabled(getContext())) {
                if (!AppConfig.getInstance().checkPermission(getContext())) {
                    requestPermission();
                } else {
                    lat = GPSTracker.getInstance(getContext()).getLatitude();
                    lng = GPSTracker.getInstance(getContext()).getLongitude();
                    if (lat != 0) {
                        requestNewOffers(page, strSortBy, lat, lng, true);
                    } else {
                        customAlert.showCustomAlertDialog(getActivity(), getString(R.string.gps_connection_heading), getString(R.string.gps_connection_message), null, null, false, null);
                    }
                }
            } else {
                Log.d("sadSDSAASD", "onCreateView:5520 ");
            }

        }
    }
}
