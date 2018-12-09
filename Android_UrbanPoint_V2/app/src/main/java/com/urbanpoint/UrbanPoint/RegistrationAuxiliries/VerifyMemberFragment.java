package com.urbanpoint.UrbanPoint.RegistrationAuxiliries;


import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.Utility;
//import com.urbanpoint.UrbanPoint.adapters.homeAdapter.favoritesAdapter.FavoritesAdapter;
//import com.urbanpoint.UrbanPoint.dataobject.main.DModelHomeGrdVw;
//import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
//import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
//import com.urbanpoint.UrbanPoint.managers.HomeManager;
//import com.urbanpoint.UrbanPoint.managers.categoryScreens.MerchantManager;
//import com.urbanpoint.UrbanPoint.utils.AppPreference;
//import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.Utils.Utility;
import com.urbanpoint.UrbanPoint.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyMemberFragment extends Fragment implements View.OnClickListener {


    private FragmentActivity mActivity;
    private FragmentManager frgMngr;
    private Context mContext;
    private View mRootView;
    private Utility utilObj;
    private ImageView mBackButton;
    private ListView lsvFavorites;
//    private List<DModelHomeGrdVw> lstFavoritesByLocation, lstFavoritesByAlphabetically;
//    private FavoritesAdapter favoritesAdapter;
    private RelativeLayout rlAlphabetically, rlLocation;
    private TextView txvLocation, txvAlphabetically;
//    private HomeManager homeManager;
//    private MerchantManager mMerchantManager;
    private boolean isLocationSort;
    private int mSelectedPosition;
    private boolean isSubscribed;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Typeface novaThin, novaRegular;
//    private CustomDialogConfirmationInterfaces contextualDialogConfirmationInterfacesLocation;

    public VerifyMemberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify_member, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
//        mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initialize();


//        if (NetworkUtils.isConnected(mContext)) {
//            utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
////            homeManager.doFetchFavOffers();
//        } else {
//            utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
//        }


        return view;
    }


    private void initialize() {
      //  MyApplication.getInstance().trackScreenView(getString(R.string.contact_us));
        utilObj = new Utility(getActivity());
       // homeManager = new HomeManager(mContext, this);
        frgMngr = getFragmentManager();
        novaThin = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_thin.ttf");
        novaRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_regular.ttf");
        setActionBar("Verify", false);
        isLocationSort = true;
//        isSubscribed = AppPreference.getSettingResturnsBoolean(mActivity, Constants.DEFAULT_VALUES.IS_USER_SUBSCRIBE, false);
//        lstFavoritesByLocation = new ArrayList<>();
//        lstFavoritesByAlphabetically = new ArrayList<>();
        bindViews();
    }

    private void bindViews() {


    }

    public void setActionBar(String title, boolean showNavButton) {
//        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        Animation animation = AnimationUtils.loadAnimation(getContext(),
                R.anim.right_in);
//        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getActivity().getLayoutInflater().inflate(R.layout.action_bar_offer_main, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);
        customView.startAnimation(animation);
        mBackButton = (ImageView) customView.findViewById(R.id.backButton);
        mBackButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                frgMngr.popBackStack();
                return false;
            }
        });

        title1.setText(title);
        title1.setTypeface(novaRegular);
//        getActivity().getActionBar().setCustomView(customView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_fav_rl_location:

                break;
        }
    }

//    @Override
//    public void onSuccessRedirection(int taskID) {
//        utilObj.stopiOSLoader();
//        if (taskID == Constants.TaskID.FETCH_FAV_OFFERS_TASK_ID) {
//
//        }
//
//    }

//    @Override
//    public void onFailureRedirection(String errorMessage) {
//        utilObj.stopiOSLoader();
////        utilObj.showToast(mContext, errorMessage, Toast.LENGTH_LONG);
//        utilObj.showCustomAlertDialog(mActivity, null, getResources().getString(R.string.no_fav_marked), null, null, false, null);
//
//    }
}
