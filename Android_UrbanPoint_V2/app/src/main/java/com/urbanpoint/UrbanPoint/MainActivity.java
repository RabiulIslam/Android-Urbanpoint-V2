package com.urbanpoint.UrbanPoint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.DrawerAuxiliries.AccessCodeFragment;
import com.urbanpoint.UrbanPoint.DrawerAuxiliries.ContactUsFragment;
import com.urbanpoint.UrbanPoint.DrawerAuxiliries.HowToUseFragment;
import com.urbanpoint.UrbanPoint.DrawerAuxiliries.ProfileFragment;
import com.urbanpoint.UrbanPoint.DrawerAuxiliries.PurchaseHistoryFragment;
import com.urbanpoint.UrbanPoint.DrawerAuxiliries.ReferAndEarnFragment;
import com.urbanpoint.UrbanPoint.DrawerAuxiliries.ReviewOrdersFragment;
import com.urbanpoint.UrbanPoint.DrawerAuxiliries.UnSubscribeFragment;
import com.urbanpoint.UrbanPoint.DrawerAuxiliries.WebServices.Logout_Webhit_Get_logout;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragment;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.CustomAlertConfirmationInterface;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements INavBarUpdateUpdateListener, View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private TextView txvTitle, txvCancel, txvProfile, txvReview, txvUserName;
    private RelativeLayout rlCancel, rlMenu, rlBack;
    private CheckBox chkMenu;
    private Toolbar toolbar;
    private ImageView imvBackIcn;
    private LinearLayout llDrawerMenu;
    private FragmentManager mFrgmgr;
    private LinearLayout llToolBarContainer, llHome, llInviteFriends, llProfile, llHowToUse, llMyReviews, llAccessCode, llUnSubscribe, llPurchaseHistory, llContactUs, llLogout;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

    CustomAlert customAlert;
    ProgressDilogue progressDilogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("PUSHNOTIFICATN", "onCreateMainActivity: " + AppConfig.getInstance().isCommingFromSplash);

//         if (getIntent().hasExtra("subscribe"))
//         {
//             Log.e("home","home");
////             navToHomeFragment();
//             FragmentTransaction ft = mFrgmgr.beginTransaction();
//             Fragment frg = new HomeFragment();
//             ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.HomeFragment);
//             ft.addToBackStack(AppConstt.FRGTAG.HomeFragment);
//             ft.commit();
//         }
        initiate();
        bindViews();
        setupDrawerToggle();
        //Log.e("app_version","");

        if (!AppConfig.getInstance().isCommingFromSplash) {
            Log.e("check", "1");
            AppConfig.getInstance().isCommingFromSplash = true;
            //If not coming from splash, redirect to splash
            Intent intent = new Intent(this, IntroActivity.class);
            //Assign extras from FCM based pending intent by MyHandler.java
            if (getIntent().getExtras() != null)
                intent.putExtras(getIntent().getExtras());
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            this.overridePendingTransition(0, 0);
            this.finish();//Not required in the backstack
        } else {
            Log.e("check", "2");
            setDefLang("en");
            mDrawerLayout = findViewById(R.id.drawer_layout);
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            Intent intent = getIntent();
            String Id = "";
            String title = "";
            String msg = "";
            String date = "";
            if (intent != null) {
                Log.e("check", "3");
                Id = intent.getStringExtra(AppConstt.Notifications.PUSH_NTIFCN_ID);
                title = intent.getStringExtra(AppConstt.Notifications.PUSH_NTIFCN_TITLE);
                msg = intent.getStringExtra(AppConstt.Notifications.PUSH_NTIFCN_MSG);
                date = intent.getStringExtra(AppConstt.Notifications.PUSH_NTIFCN_DATE);
            }
            if (Id != null && Id.length() > 0) {
                Log.e("check", "4");
                Bundle b = new Bundle();
                b.putString(AppConstt.Notifications.PUSH_NTIFCN_ID, Id);
                b.putString(AppConstt.Notifications.PUSH_NTIFCN_TITLE, title);
                b.putString(AppConstt.Notifications.PUSH_NTIFCN_MSG, msg);
                b.putString(AppConstt.Notifications.PUSH_NTIFCN_DATE, date);
                navToHomeFragmentWithArguments(b);
            } else {
                Log.e("check", "5");
                AppConfig.getInstance().isComingFromHome = true;
                navToHomeFragment();
            }
//            logLengthyToken(AppConfig.getInstance().mUser.getToken());
        }
        printhashkey();
    }

    public void printhashkey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:", e.getMessage());

        } catch (NoSuchAlgorithmException e) {

            Log.d("KeyHash:", e.getMessage());
        }

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

    private void logLengthyToken(String grandString) {
        if (grandString.length() > 4000) {
            int chunkCount = grandString.length() / 4000;
            for (int i = 0; i <= chunkCount; i++) {
                int max = 4000 * (i + 1);
                if (max >= grandString.length()) {
                    Log.d("AuthTokenLOG", "part " + i + " of " + chunkCount + ":" + grandString.substring(4000 * i));
                } else {
                    Log.d("AuthTokenLOG", "part " + i + " of " + chunkCount + ":" + grandString.substring(4000 * i, max));
                }
            }
        } else {
            Log.d("AuthTokenLOG", grandString.toString());
        }
    }

    private void initiate() {
        mFrgmgr = getSupportFragmentManager();
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
    }

    private void bindViews() {
        llDrawerMenu = findViewById(R.id.Drawer_menu);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        llToolBarContainer = findViewById(R.id.container_toolbar);
        txvUserName = findViewById(R.id.drawer_txv_user_name);
        txvProfile = findViewById(R.id.drawer_txv_profile);
        txvReview = findViewById(R.id.drawer_txv_review);

        llHome = findViewById(R.id.drawer_ll_home);
        llInviteFriends = findViewById(R.id.drawer_ll_invite_friends);
        llProfile = findViewById(R.id.drawer_ll_profile);
        llHowToUse = findViewById(R.id.drawer_ll_how_to_use);
        llMyReviews = findViewById(R.id.drawer_ll_my_reviews);
        llPurchaseHistory = findViewById(R.id.drawer_ll_purchase_history);
        llAccessCode = findViewById(R.id.drawer_ll_access_code);
        llUnSubscribe = findViewById(R.id.drawer_ll_unsub);
        llContactUs = findViewById(R.id.drawer_ll_contact_us);
        llLogout = findViewById(R.id.drawer_ll_logout);

        String split[] = AppConfig.getInstance().mUser.getmName().split(" ");
        txvUserName.setText("Hi, " + split[0]);

        llHome.setOnClickListener(this);
        llInviteFriends.setOnClickListener(this);
        llProfile.setOnClickListener(this);
        llHowToUse.setOnClickListener(this);
        llMyReviews.setOnClickListener(this);
        llPurchaseHistory.setOnClickListener(this);
        llAccessCode.setOnClickListener(this);
        llUnSubscribe.setOnClickListener(this);
        llContactUs.setOnClickListener(this);
        llLogout.setOnClickListener(this);
        setupToolbar();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.app_br_toolbar);
        txvTitle = findViewById(R.id.app_br_toolbar_title);
        txvCancel = findViewById(R.id.app_br_toolbar_txv_cancel);
        imvBackIcn = findViewById(R.id.app_bar_imv_back);
        rlCancel = findViewById(R.id.app_bar_rl_cancel);
        rlBack = findViewById(R.id.app_bar_rl_back);
        rlMenu = findViewById(R.id.app_bar_rl_menu);
        chkMenu = findViewById(R.id.app_bar_chkbx_menu);

        rlBack.setOnClickListener(this);
        rlMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String tag = returnStackFragment();
        Fragment previousFrg = mFrgmgr.findFragmentByTag(tag);
        switch (v.getId()) {
            case R.id.app_bar_rl_menu:
                mDrawerLayout.openDrawer(llDrawerMenu);
                break;

            case R.id.app_bar_rl_back:
                if ((tag.equalsIgnoreCase(AppConstt.FRGTAG.FN_PurchaseSuccessFragment)) ||
                        (tag.equalsIgnoreCase(AppConstt.FRGTAG.SubscriptionEligibleSuccessFragment))) {
                    navToHomeFragment();
                } else if (tag.equalsIgnoreCase(AppConstt.FRGTAG.SubscriptionSuccessFragment)) {
                    if (AppConfig.getInstance().isCommingFromOfferDetail) {
                        mFrgmgr.popBackStackImmediate();
                        mFrgmgr.popBackStackImmediate();
                        mFrgmgr.popBackStackImmediate();
                    } else {
                        navToHomeFragment();
                    }
                } else {
                    mFrgmgr.popBackStackImmediate();
                    AppConfig.getInstance().closeKeyboard(MainActivity.this);
                }
                break;


            case R.id.drawer_ll_home:
                navToHomeFragment();
                mDrawerLayout.closeDrawer(llDrawerMenu);
                break;

            case R.id.drawer_ll_invite_friends:
                navToReferAndEarnFragment(previousFrg);
                mDrawerLayout.closeDrawer(llDrawerMenu);
                break;

            case R.id.drawer_ll_profile:
                navToProfileFragment(previousFrg);
                mDrawerLayout.closeDrawer(llDrawerMenu);
                break;

            case R.id.drawer_ll_how_to_use:
                navToHowToUseFragment(previousFrg);
                mDrawerLayout.closeDrawer(llDrawerMenu);
                break;

            case R.id.drawer_ll_my_reviews:
                navToMyReviewsFragment(previousFrg);
                mDrawerLayout.closeDrawer(llDrawerMenu);
                break;

            case R.id.drawer_ll_purchase_history:
                navToPurchaseHistoryFragment(previousFrg);
                mDrawerLayout.closeDrawer(llDrawerMenu);
                break;

            case R.id.drawer_ll_access_code:
                navToAccessCodeFragment(previousFrg);
                mDrawerLayout.closeDrawer(llDrawerMenu);
                break;

            case R.id.drawer_ll_unsub:
                navToUnSubscribeFragment(previousFrg);
                mDrawerLayout.closeDrawer(llDrawerMenu);
                break;

            case R.id.drawer_ll_contact_us:
                navToContactUsFragment(previousFrg);
                mDrawerLayout.closeDrawer(llDrawerMenu);
                break;
            case R.id.drawer_ll_logout:
                progressDilogue.startiOSLoader(MainActivity.this, R.drawable.image_for_rotation, getString(R.string.please_wait), false);
//                AppConfig.getInstance().deleteUserData();
                requestLogout();
                mDrawerLayout.closeDrawer(llDrawerMenu);
                break;
        }
    }

    //region Navbar Interface Functions
    @Override
    public void setNavBarTitle(String strTitle) {
        txvTitle.setText(strTitle);
    }

    @Override
    public void setMenuBadgeVisibility(boolean _shouldBageVisible) {
        boolean chkis = _shouldBageVisible;
        chkMenu.setChecked(_shouldBageVisible);
    }

    @Override
    public void setBackBtnVisibility(int _visibility) {
        if (_visibility == View.VISIBLE) {
            rlMenu.setVisibility(View.GONE);
            rlBack.setVisibility(View.VISIBLE);
            // this will lock the drawer toggle's swiping listener
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            rlMenu.setVisibility(View.VISIBLE);
            rlBack.setVisibility(View.GONE);
            // this will unlock the drawer toggle's swiping listener
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            //Updating MenuIcon Badge
            String nationality = "";
            if (AppConfig.getInstance().mUser.getmNationality() != null &&
                    AppConfig.getInstance().mUser.getmNationality().length() > 0) {
                nationality = AppConfig.getInstance().mUser.getmNationality();
            }
            if ((AppConfig.getInstance().mUserBadges.getReviewCount() == 0)) {
                if ((nationality.length() > 0)) {
                    chkMenu.setChecked(false);
                } else {
                    chkMenu.setChecked(true);
                }
            } else {
                chkMenu.setChecked(true);
            }
        }
    }

    @Override
    public void setCancelBtnVisibility(int _visibility) {
        if (_visibility == View.VISIBLE) {
            rlCancel.setVisibility(View.VISIBLE);
        } else {
            rlCancel.setVisibility(View.GONE);
        }
    }

    @Override
    public void setToolBarbackgroudVisibility(int _visibility) {
        llToolBarContainer.setVisibility(_visibility);
    }

    @Override
    public void setReviewCount(int _count) {
        if (_count > 0) {
            txvReview.setVisibility(View.VISIBLE);
            txvReview.setText(_count + "");
        } else {
            txvReview.setVisibility(View.GONE);
        }
    }


    @Override
    public void setProfileCount(String Count) {
        txvProfile.setText(Count);
    }

    @Override
    public void setProfileCountVisibility(int _visibility) {
        txvProfile.setVisibility(_visibility);
    }

    @Override
    public void setUnSubscribeVisibility(int _visibility) {
        llUnSubscribe.setVisibility(_visibility);
    }

    @Override
    public void navToLogin() {
        clearFCMToken();
    }
    //endregion

    //region Navigation Functions
    public String returnStackFragment() {
        int index = mFrgmgr.getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntry = null;
        String tag = "";
        if (index >= 0) {
            backEntry = mFrgmgr.getBackStackEntryAt(index);
            tag = backEntry.getName();
        }
        return tag;
    }

    public void clearMyBackStack() {
        int count = 0;
        if (mFrgmgr != null) {
            if (mFrgmgr.getBackStackEntryCount() > 0) {
                count = mFrgmgr.getBackStackEntryCount();
            }
            for (int i = 0; i < count; ++i) {
                mFrgmgr.popBackStackImmediate();
            }
        }
    }

    public void navToMyReviewsFragment(Fragment _previousFrg) {
        FragmentTransaction ft = mFrgmgr.beginTransaction();
        Fragment frg = new ReviewOrdersFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.MyReviewsFragment);
        ft.addToBackStack(AppConstt.FRGTAG.MyReviewsFragment);
        ft.hide(_previousFrg);
        ft.commit();
    }

    public void navToAccessCodeFragment(Fragment _previousFrg) {
        FragmentTransaction ft = mFrgmgr.beginTransaction();
        Fragment frg = new AccessCodeFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.PromocodeFragment);
        ft.addToBackStack(AppConstt.FRGTAG.PromocodeFragment);
        ft.hide(_previousFrg);
        ft.commit();
    }

    public void navToUnSubscribeFragment(Fragment _previousFrg) {
        FragmentTransaction ft = mFrgmgr.beginTransaction();
        Fragment frg = new UnSubscribeFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.UnSubscribeFragment);
        ft.addToBackStack(AppConstt.FRGTAG.UnSubscribeFragment);
        ft.hide(_previousFrg);
        ft.commit();
    }

    public void navToPurchaseHistoryFragment(Fragment _previousFrg) {
        FragmentTransaction ft = mFrgmgr.beginTransaction();
        Fragment frg = new PurchaseHistoryFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.PurchaseHistoryFragment);
        ft.addToBackStack(AppConstt.FRGTAG.PurchaseHistoryFragment);
        ft.hide(_previousFrg);
        ft.commit();
    }

    public void navToContactUsFragment(Fragment _previousFrg) {
        FragmentTransaction ft = mFrgmgr.beginTransaction();
        Fragment frg = new ContactUsFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.ContactUsFragment);
        ft.addToBackStack(AppConstt.FRGTAG.ContactUsFragment);
        ft.hide(_previousFrg);
        ft.commit();
    }

    public void navToProfileFragment(Fragment _previousFrg) {
        FragmentTransaction ft = mFrgmgr.beginTransaction();
        Fragment frg = new ProfileFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.ProfileFragment);
        ft.addToBackStack(AppConstt.FRGTAG.ProfileFragment);
        ft.hide(_previousFrg);
        ft.commit();
    }

    public void navToReferAndEarnFragment(Fragment _previousFrg) {
        FragmentTransaction ft = mFrgmgr.beginTransaction();
        Fragment frg = new ReferAndEarnFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.ReferAndEarnFragment);
        ft.addToBackStack(AppConstt.FRGTAG.ReferAndEarnFragment);
        ft.hide(_previousFrg);
        ft.commit();
    }

    public void navToHowToUseFragment(Fragment _previousFrg) {
        FragmentTransaction ft = mFrgmgr.beginTransaction();
        Fragment frg = new HowToUseFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.HowToUseFragment);
        ft.addToBackStack(AppConstt.FRGTAG.HowToUseFragment);
        ft.hide(_previousFrg);
        ft.commit();
    }

    public void navToHomeFragment() {
        clearMyBackStack();
        FragmentTransaction ft = mFrgmgr.beginTransaction();
        Fragment frg = new HomeFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.HomeFragment);
        ft.addToBackStack(AppConstt.FRGTAG.HomeFragment);
        ft.commit();
    }

    public void navToHomeFragmentWithArguments(Bundle _b) {
        clearMyBackStack();
        FragmentTransaction ft = mFrgmgr.beginTransaction();
        Fragment frg = new HomeFragment();
        frg.setArguments(_b);
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.HomeFragment);
        ft.addToBackStack(AppConstt.FRGTAG.HomeFragment);
        ft.commit();
    }


    private void navToIntroActivity() {
//        AppConfig.getInstance().deleteUserData(AppConfig.getInstance().loadDefLanguage(),
//                AppConfig.getInstance().loadFCMToken());
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
        this.finish();
    }

    private void clearFCMToken() {
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                {
//                    try {
//                        FirebaseInstanceId.getInstance().deleteInstanceId();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void result) {
        //call your activity where you want to land after log out
        navToIntroActivity();
//            }
//        }.execute();
    }
    //endregion


//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(event.getAction() == KeyEvent.ACTION_DOWN){
//            switch(keyCode)
//            {
//                case KeyEvent.KEYCODE_BACK:
//
//                    ExitMessageDialog();
//
////                    String tag = returnStackFragment();
////
//////                    TimelineFragment home_frag = (TimelineFragment) getSupportFragmentManager().findFragmentByTag("home");
////                    if (tag == null || TextUtils.isEmpty(tag)) {
////                        // add your code here
////                        ExitMessageDialog();
////                    }
////
////
////
////                    if(getSupportFragmentManager().getBackStackEntryCount()<=0)
////                    {
////                        ExitMessageDialog();
////                        //finish();
////                    }
////                    else
////                    {
////                        getSupportFragmentManager().popBackStack();
////                    }
//            }
//
//
//            return true;
//        }


//        return super.onKeyDown(keyCode, event);
//    }

    private void requestLogout() {
        Logout_Webhit_Get_logout logout_webhit_get_logout = new Logout_Webhit_Get_logout();
        logout_webhit_get_logout.requestLogOut(this, new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    AppConfig.getInstance().deleteUserData(AppConfig.getInstance().loadFCMToken());
                    AppConfig.getInstance().mUser.clearUserModel();
                    AppConfig.getInstance().isComingFromLogout = true;
                    navToLogin();
                } else {
                    customAlert.showCustomAlertDialog(MainActivity.this, null, strMsg, null, null, false, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                customAlert.showCustomAlertDialog(MainActivity.this, null, ex.getMessage(), null, null, false, null);
            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();
                navToLogin();
            }
        });
    }

    public void updatePushNotifctnBadge() {
        HomeFragment homeFragment;
        String tag = returnStackFragment();
        Fragment frg = mFrgmgr.findFragmentByTag(tag);
        if (frg instanceof HomeFragment) {
            homeFragment = (HomeFragment) frg;
            homeFragment.updateBadges();
        }
    }

    void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {

                Log.d("dfasdfasdfds", "setupDrawerToggle: " + mDrawerLayout.isDrawerOpen(llDrawerMenu));

                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {

                Log.d("dfasdfasdfds", "setupDrawerToggle: " + mDrawerLayout.isDrawerOpen(llDrawerMenu));

                invalidateOptionsMenu();
            }

            // YOUR CODE GOES HERE?

        };
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();

    }

    public void OpenDrawer() {
        if (mDrawerLayout.isDrawerOpen(llDrawerMenu)) {
            mDrawerLayout.closeDrawer(llDrawerMenu);
        } else {
            mDrawerLayout.openDrawer(llDrawerMenu);
        }
    }

    private void performActionAgainstFCM() {
        try {
            Intent intent = getIntent();
            String pushNotificationId = intent.getStringExtra(AppConstt.Notifications.PUSH_NTIFCN_ID);
            String pushNotificationTitle = intent.getStringExtra(AppConstt.Notifications.PUSH_NTIFCN_TITLE);
            String pushNotificationMsg = intent.getStringExtra(AppConstt.Notifications.PUSH_NTIFCN_MSG);
            String pushNotificationDate = intent.getStringExtra(AppConstt.Notifications.PUSH_NTIFCN_DATE);

            if (pushNotificationId.length() > 0 && AppConfig.getInstance().isCommingFromSplash) {

            } else {
                Log.d("pushIntent", "Notification intent is null");
            }
            getIntent().removeExtra(AppConstt.Notifications.PUSH_NTIFCN_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        String tag = returnStackFragment();
        if (mDrawerLayout.isDrawerOpen(llDrawerMenu)) {
            mDrawerLayout.closeDrawer(llDrawerMenu);

        } else if (tag.equalsIgnoreCase(AppConstt.FRGTAG.HomeFragment)) {
            new CustomAlert().showCustomDialog(this, getResources().getString(R.string.No), getResources().getString(R.string.yes)
                    , getResources().getString(R.string.exit_app), new CustomAlertConfirmationInterface() {
                        @Override
                        public void callConfirmationDialogPositive() {
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory(Intent.CATEGORY_HOME);
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(homeIntent);
                            finish();
                        }

                        @Override
                        public void callConfirmationDialogNegative() {

                        }
                    });
        } else if ((tag.equalsIgnoreCase(AppConstt.FRGTAG.FN_PurchaseSuccessFragment)) ||
                (tag.equalsIgnoreCase(AppConstt.FRGTAG.SubscriptionEligibleSuccessFragment))) {
            navToHomeFragment();
        } else if (tag.equalsIgnoreCase(AppConstt.FRGTAG.SubscriptionSuccessFragment)) {
            if (AppConfig.getInstance().isCommingFromOfferDetail) {
                mFrgmgr.popBackStackImmediate();
                mFrgmgr.popBackStackImmediate();
                mFrgmgr.popBackStackImmediate();
            } else {
                navToHomeFragment();
            }
        } else {
            mFrgmgr.popBackStackImmediate();
            AppConfig.getInstance().closeKeyboard(MainActivity.this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        AppConfig.getInstance().isAppRunning = false;
//    }


}