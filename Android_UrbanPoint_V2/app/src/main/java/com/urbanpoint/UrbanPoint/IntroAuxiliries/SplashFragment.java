package com.urbanpoint.UrbanPoint.IntroAuxiliries;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.SignupActivity;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;

/**
 * Created by Danish on 1/24/2018.
 */

public class SplashFragment extends Fragment {
    private static int SPLASH_TIME_OUT = 4000;
    private boolean shouldNavigate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View frg = inflater.inflate(R.layout.fragment_splash, container, false);
        shouldNavigate = true;
        AppConfig.getInstance().loadUserData();
        getAppVersion();
        if (!AppConfig.getInstance().mUser.isLoggedIn) {
            AppConfig.getInstance().loadOldPrefrnceData();
            if (AppConfig.getInstance().mUser.getmUserId().length() > 0) {
                AppConfig.getInstance().mUser.setLoggedIn(true);
                AppConfig.getInstance().clearSharedPreferance();
                AppConfig.getInstance().saveUserData();
            }
        }
//                loadOldPrefrencLog();

        launchSelection();
        return frg;

    }

    private void loadOldPrefrencLog() {

        Log.d("OldDataIs", "UserId :" + AppConfig.getInstance().mUser.mUserId);
        Log.d("OldDataIs", "Name :" + AppConfig.getInstance().mUser.mName);
        Log.d("OldDataIs", "Email :" + AppConfig.getInstance().mUser.mEmail);
        Log.d("OldDataIs", "Age :" + AppConfig.getInstance().mUser.mAge);
        Log.d("OldDataIs", "Dob :" + AppConfig.getInstance().mUser.mDob);
        Log.d("OldDataIs", "Gender :" + AppConfig.getInstance().mUser.mGender);
        Log.d("OldDataIs", "Nationality :" + AppConfig.getInstance().mUser.mNationality);
        Log.d("OldDataIs", "PinCode :" + AppConfig.getInstance().mUser.mPinCode);
        Log.d("OldDataIs", "networktype :" + AppConfig.getInstance().mUser.mNetworkType);
        Log.d("OldDataIs", "Msisdn :" + AppConfig.getInstance().mUser.mPhoneNumber);
        Log.d("OldDataIs", "Subscribed :" + AppConfig.getInstance().mUser.isSubscribed);
        Log.d("OldDataIs", "AppVersion :" + AppConfig.getInstance().mUser.mAppVersion);
        Log.d("OldDataIs", "isLoggedIn :" + AppConfig.getInstance().mUser.isLoggedIn);
        Log.d("OldDataIs", "Token :" + AppConfig.getInstance().mUser.mAuthorizationToken);
        Log.d("OldDataIs", "Uber :" + AppConfig.getInstance().mUser.isUberRequired);
        Log.d("OldDataIs", "ForceUpdate :" + AppConfig.getInstance().mUser.isUberRequired);

    }

    private void getAppVersion() {
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            float version = Float.parseFloat(pInfo.versionName);
            Log.d("AppVersion", "showAppUpdateDialogue: " + version);
            AppConfig.getInstance().mUser.setmAppVersion(version);


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    void launchSelection() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("should","navigate"+shouldNavigate);
                if (shouldNavigate) {
                    if (AppConfig.getInstance().mUser.getmUserId().length() > 0) {

                        navToHomeActivity();
                    } else {
                        navToGetStartedFragment();
                    }
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void navToHomeActivity() {
        Log.e("home","home");
        Bundle b = this.getArguments();
        String id = "";
        String title = "";
        String msg = "";
        String date = "";
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if (b != null) {
            Log.e("home","home11");
            id = b.getString(AppConstt.Notifications.PUSH_NTIFCN_ID);
            title = b.getString(AppConstt.Notifications.PUSH_NTIFCN_TITLE);
            msg = b.getString(AppConstt.Notifications.PUSH_NTIFCN_MSG);
            date = b.getString(AppConstt.Notifications.PUSH_NTIFCN_DATE);
            intent.putExtra(AppConstt.Notifications.PUSH_NTIFCN_ID, id);
            intent.putExtra(AppConstt.Notifications.PUSH_NTIFCN_TITLE, title);
            intent.putExtra(AppConstt.Notifications.PUSH_NTIFCN_MSG, msg);
            intent.putExtra(AppConstt.Notifications.PUSH_NTIFCN_DATE, date);
        }

        startActivity(intent);
        getActivity().finish();
    }

    void navToGetStartedFragment()
    {
        Log.e("home","home23");
        Intent intent= new Intent(getActivity(), SignupActivity.class);
        startActivity(intent);
        getActivity().finish();
//        Fragment fragment = new SignUpFragment();
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.activity_intro_frm, fragment, AppConstt.FRGTAG.IntroMainFragment);
//        ft.commitAllowingStateLoss();
    }

    @Override
    public void onResume() {
        super.onResume();
        shouldNavigate = true;
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        shouldNavigate = false;
//    }
}
