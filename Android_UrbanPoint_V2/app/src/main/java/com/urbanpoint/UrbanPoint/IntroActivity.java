package com.urbanpoint.UrbanPoint;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.SignUpFragment;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.SplashFragment;
import com.urbanpoint.UrbanPoint.MyApplication;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;

import java.util.Locale;

/**
 * Created by Danish on 1/23/2018.
 */

public class IntroActivity extends AppCompatActivity implements INavBarUpdateUpdateListener{
    private ImageView imvLoader;
    private LinearLayout llContainer;
    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppConfig.getInstance().loadFCMToken();

        if (!AppConfig.getInstance().loadDefLanguage().equals("")) {
            Log.i("checkdef", "yes in if");
            if (AppConfig.getInstance().loadDefLanguage().equalsIgnoreCase(AppConstt.ENGLISH)) {
                AppConfig.getInstance().isArabic = false;
            } else if (AppConfig.getInstance().loadDefLanguage().equalsIgnoreCase(AppConstt.ARABIC)) {
                Log.i("checkdef", "yes in if");
                AppConfig.getInstance().isArabic = true;
            }
            setDefLang("en");
        } else {
            AppConfig.getInstance().saveDefLanguage(AppConstt.ENGLISH);
            AppConfig.getInstance().isArabic = false;
        }
        // setupDefaultFont();

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity

            Log.d("ExtraEntery", "Extra entry is handled");
            finish();
            return;
        }

        setContentView(R.layout.activity_intro);
        //playVideo();
        AppConfig.getInstance().clearSignupData();
        AppConfig.getInstance().isCommingFromSplash = true;

        AppConfig.getInstance().isComingFromHome =false;
        if (AppConfig.getInstance().isComingFromLogout) {
            navToSignUpFragment();
        } else {
            navToSplash();
        }
        String projectToken = AppConstt.mixPanel.MIXPANEL_TOKEN; // e.g.: "1ef7e30d2a58d27f4b90c42e31d6d7ad"
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

//    private void playVideo() {
//        videoView = findViewById(R.id.splash_video);
//        llContainer = findViewById(R.id.splash_cntnr_ll);
//        imvLoader = findViewById(R.id.activity_intro_imv_loader);
//
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels;
//        ViewGroup.LayoutParams params = videoView.getLayoutParams();
//        params.width = width;
//        params.height = (int) (width * 1.6);
//        videoView.setLayoutParams(params);
//
//        String path = "android.resource://" + getPackageName() + "/" + R.raw.video720;
//
//        videoView.setVideoPath(path);
//        videoView.start();
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                videoView.seekTo(100);
//                videoView.setBackgroundColor(Color.TRANSPARENT);
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        imvLoader.setVisibility(View.GONE);
//                    }
//                },500);
//            }
//        });
//        llContainer.setBackgroundColor(getResources().getColor(R.color.splash_bg));
//    }

    void navToSplash() {
        Intent intent = getIntent();
        String Id = "";
        String title = "";
        String msg = "";
        String date = "";
        if (intent != null) {
            Id = intent.getStringExtra(AppConstt.Notifications.PUSH_NTIFCN_ID);
            title = intent.getStringExtra(AppConstt.Notifications.PUSH_NTIFCN_TITLE);
            msg = intent.getStringExtra(AppConstt.Notifications.PUSH_NTIFCN_MSG);
            date = intent.getStringExtra(AppConstt.Notifications.PUSH_NTIFCN_DATE);
        }
        Bundle b = new Bundle();
        b.putString(AppConstt.Notifications.PUSH_NTIFCN_ID, Id);
        b.putString(AppConstt.Notifications.PUSH_NTIFCN_TITLE, title);
        b.putString(AppConstt.Notifications.PUSH_NTIFCN_MSG, msg);
        b.putString(AppConstt.Notifications.PUSH_NTIFCN_DATE, date);

        Fragment fragment = new SplashFragment();
        fragment.setArguments(b);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.activity_intro_frm, fragment, AppConstt.FRGTAG.SplashFragment);
        ft.commit();

    }

    void navToSignUpFragment() {
        Intent intent=new Intent(IntroActivity.this,SignupActivity.class);
        startActivity(intent);
        finish();
        Fragment fragment = new SignUpFragment();
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.activity_intro_frm, fragment, AppConstt.FRGTAG.FN_SignUpFragment);
//        ft.commit();

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        ExitMessageDialog();
    }

    private void ExitMessageDialog() {
        AlertDialog alertDialog;
        alertDialog =   new AlertDialog.Builder(IntroActivity.this)
                .setMessage(getResources().getString(R.string.exit_app))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }})
                .setNegativeButton(getResources().getString(R.string.No), null)
                .show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);

    }
    public void setDefLang(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        ((MyApplication) getApplication()).setLanguageSpecificFonts(true);
    }

    @Override
    public void setNavBarTitle(String strTitle) {

    }

    @Override
    public void setMenuBadgeVisibility(boolean _shouldBageVisible) {

    }

    @Override
    public void setBackBtnVisibility(int _visibility) {

    }

    @Override
    public void setCancelBtnVisibility(int _visibility) {

    }

    @Override
    public void setToolBarbackgroudVisibility(int _visibility) {

    }

    @Override
    public void setReviewCount(int _count) {

    }

    @Override
    public void setProfileCountVisibility(int _visibility) {

    }

    @Override
    public void setUnSubscribeVisibility(int _visibility) {

    }

    @Override
    public void navToLogin() {

    }

    @Override
    public void setProfileCount(String Count) {

    }
}
