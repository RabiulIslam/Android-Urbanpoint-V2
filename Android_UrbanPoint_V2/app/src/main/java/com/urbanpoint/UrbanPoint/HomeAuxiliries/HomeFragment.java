package com.urbanpoint.UrbanPoint.HomeAuxiliries;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.CommonFragments.OfferDetailFragment;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragments.FavoriteOffersFragment;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragments.NewOffersFragment;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragments.NotificationFragment;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragments.SearchOffersFragment;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.WebServices.Home_WebHit_Post_eligibilitychecker;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.WebServices.Home_WebHit_Post_getAuthorization;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.WebServices.Home_WebHit_Post_homeApi;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.SignUpVerificationFragment;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.SubscriptionAuxiliries.SubscriptionEligibleFragment;
import com.urbanpoint.UrbanPoint.SubscriptionAuxiliries.SubscriptionEligibleSuccessFragment;
import com.urbanpoint.UrbanPoint.SubscriptionAuxiliries.SubscriptionFragment;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.CustomAlertConfirmationInterface;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//import com.facebook.appevents.AppEventsLogger;
//import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {


    private ImageView imvFestival, imvCategoryLine;
    private Button btnGainAccess;
    private LinearLayout llIstOffer, llGainAcess;
    private TextView txvOfferDays, txvRedIconBtn, txvHomeScreenMsg, txvMemberMsg, txvBubbleCount;
    private HorizontalScrollView mHorizontalScrollView1, mHorizontalScrollView2;
    private RelativeLayout rlHomeMenu, rlHomeSearch;
    //Bottom Tab Items
    private final byte NUM_BOTTOM_TABS = 3;
    private final byte BOTTOM_TAB_FAVORITES = 0;
    private final byte BOTTOM_TAB_NOTIFICATIONS = 1;
    private final byte BOTTOM_TAB_NEW_OFFERS = 2;

    private LinearLayout[] llBottomTab = new LinearLayout[NUM_BOTTOM_TABS];
    private ImageView[] imvBottomTab = new ImageView[NUM_BOTTOM_TABS * 2];
    private TextView[] txvBottomTab = new TextView[NUM_BOTTOM_TABS];
    private HorizontalGridViewAdapter horizontalGridViewAdapter1, horizontalGridViewAdapter2;
    private HomeCategoriesAdapter homeCategoriesAdapter;
    private ArrayList<DModelGetCategories> lstCategories;
    private List<DModelHomeGrdVw> lstGrids1, lstGrids2;
    private GridView grdvwCategories, grdvw1, grdvw2;
    private ImageView imvFavorite, imvNotification, imvNewOffer;
    private CheckBox chkMenu;
    private RelativeLayout rlGrd1Next, rlGrd2Next, imvSearch, rlNotifctnBubble, rlMembermsg, rlProgress1, rlProgress2;
    private int grdvwWidth, grdvwWidth2, grdvwItemWidth, increment1, increment2;

    private boolean isAppUpdateDialogueDisplaying;
    private CustomAlert customAlert;
    private ProgressDilogue progressDilogue;
    private LocalBroadcastManager localBroadcastManager;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        OurOnCreate(view);

        if (AppConfig.getInstance().mUser.getmAuthorizationToken().length() > 0) {
            progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
            requestHomeApi();
        } else {
            String fcmToken = AppConfig.getInstance().loadFCMToken();
            if (fcmToken.length() > 0) {
                progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
                requestAuthorization(fcmToken);
            } else {
                String message = getResources().getString(R.string.MSG_ERROR_NETWORK);
                customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
            }
        }
        if (AppConfig.getInstance().shouldNavToReview) {
            ((MainActivity) getContext()).navToMyReviewsFragment(this);
        }
        Bundle b = this.getArguments();
        if (b != null) {
            String id = b.getString(AppConstt.Notifications.PUSH_NTIFCN_ID);
            String title = b.getString(AppConstt.Notifications.PUSH_NTIFCN_TITLE);
            String msg = b.getString(AppConstt.Notifications.PUSH_NTIFCN_MSG);
            String date = b.getString(AppConstt.Notifications.PUSH_NTIFCN_DATE);

            Bundle c = new Bundle();
            c.putString(AppConstt.Notifications.PUSH_NTIFCN_ID, id);
            c.putString(AppConstt.Notifications.PUSH_NTIFCN_TITLE, title);
            c.putString(AppConstt.Notifications.PUSH_NTIFCN_MSG, msg);
            c.putString(AppConstt.Notifications.PUSH_NTIFCN_DATE, date);
            navToNotificationFragmentWithArguments(c);
        }


        return view;
    }

    private void initialize() {
        printKeyHash();
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        lstCategories = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());

        lstCategories.add(new DModelGetCategories("", "", ""));
        lstCategories.add(new DModelGetCategories("", "", ""));
        lstCategories.add(new DModelGetCategories("", "", ""));
        lstCategories.add(new DModelGetCategories("", "", ""));

        isAppUpdateDialogueDisplaying = false;
        increment1 = 0;
        increment2 = 0;
    }

    private void bindViews(View frg) {
        rlHomeMenu = frg.findViewById(R.id.frg_home_tool_bar_rl_menu);
        rlHomeSearch = frg.findViewById(R.id.frg_home_tool_bar_rl_search);

        grdvwCategories = frg.findViewById(R.id.listviewCategories);

        txvBubbleCount = frg.findViewById(R.id.frg_home_txv_notification_bubble);
        txvHomeScreenMsg = frg.findViewById(R.id.frg_home_screen_msg);
        txvMemberMsg = frg.findViewById(R.id.frg_home_txv_member_msg);
        imvCategoryLine = frg.findViewById(R.id.frg_home_imv_category_line);
        llIstOffer = frg.findViewById(R.id.frg_home_ll_ist_offer);
        llGainAcess = frg.findViewById(R.id.mainLayoutGainAcess);

        imvFestival = frg.findViewById(R.id.frg_home_imv_festival);
        rlMembermsg = frg.findViewById(R.id.frg_home_rl_member_msg);
        rlProgress1 = frg.findViewById(R.id.frg_home_rl_progrssbar_1);
        rlProgress2 = frg.findViewById(R.id.frg_home_rl_progrssbar_2);
        rlMembermsg.setOnClickListener(this);
        rlMembermsg.setVisibility(View.GONE);

        chkMenu = frg.findViewById(R.id.frg_home_chkbx_menu);
        imvNewOffer = frg.findViewById(R.id.tab_imv_round_offers);
        imvFavorite = frg.findViewById(R.id.tab_imv_round_favourites);
        imvNotification = frg.findViewById(R.id.tab_imv_round_notification);
        rlNotifctnBubble = frg.findViewById(R.id.frg_home_rl_notificatn_bubble);
        mHorizontalScrollView1 = frg.findViewById(R.id.frg_home_horizontal_scroll_view);
        mHorizontalScrollView2 = frg.findViewById(R.id.frg_home_horizontal_scroll_view_2);
        rlGrd1Next = frg.findViewById(R.id.frg_home_grd_1_next);
        rlGrd1Next.setOnClickListener(this);
        rlGrd2Next = frg.findViewById(R.id.frg_home_grd_2_next);
        rlGrd2Next.setOnClickListener(this);
        grdvw1 = frg.findViewById(R.id.frg_home_lsv_1);
        grdvw2 = frg.findViewById(R.id.frg_home_lsv_2);
        txvOfferDays = frg.findViewById(R.id.frg_home_txv_offer_days);
        btnGainAccess = frg.findViewById(R.id.frg_home_btn_gain_access);

        rlHomeMenu.setOnClickListener(this);
        rlHomeSearch.setOnClickListener(this);
        rlNotifctnBubble.setOnClickListener(this);
        btnGainAccess.setOnClickListener(this);

        homeCategoriesAdapter = new HomeCategoriesAdapter(getContext(), lstCategories);
        grdvwCategories.setAdapter(homeCategoriesAdapter);


    }

    private void sendNotification(String _title, String _message, String _event, String _orderId, String _date) {


        NotificationManager notificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String tempChannelId = "";
        int tempCategory = 0;

        //Set channles for Oreo+ (Pre-Req for NotificationCompat)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = null;

            //Create the channel object with a unique ID
            channel = new NotificationChannel(
                    AppConstt.Notifications.CHNL_ID_ANNOUNCEMENT,
                    AppConstt.Notifications.CHNL_NAME_ANNOUNCEMENT,
                    NotificationManager.IMPORTANCE_LOW);//No sound
            channel.setLightColor(Color.GREEN);
            tempCategory = AppConstt.Notifications.PUSH_CATG_ANNOUNCEMENTS;


            //Configure Channel's initial settings
            channel.setDescription(_message);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            //Submit notification channel object to the notificaiton manager
            notificationManager.createNotificationChannel(channel);

            tempChannelId = channel.getId();
        } else {
            //Group Notificaitons for older devices
            tempCategory = AppConstt.Notifications.PUSH_CATG_ANNOUNCEMENTS;
        }


        //Set Intent (Pre-Req for NotificationCompat)
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setClass(getActivity(), MainActivity.class);
        intent.putExtra(AppConstt.Notifications.PUSH_NTIFCN_ID, _orderId);
        intent.putExtra(AppConstt.Notifications.PUSH_NTIFCN_TITLE, _event);
        intent.putExtra(AppConstt.Notifications.PUSH_NTIFCN_MSG, _message);
        intent.putExtra(AppConstt.Notifications.PUSH_NTIFCN_DATE, _date);
        PendingIntent notifyPIntent = PendingIntent.getActivity(
                getActivity(), tempCategory /* Request code */,
                intent, PendingIntent.FLAG_ONE_SHOT);//


        //Build push Notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getActivity(), tempChannelId)
                        .setSmallIcon(getNotificationIcon())
                        .setContentTitle(_title)
                        .setTicker(_title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(Html.fromHtml(_message)))
                        .setContentText(Html.fromHtml(_message))
                        .setSound(defaultSoundUri)
                        .setContentIntent(notifyPIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            notificationBuilder.setColor(
                    getResources().
                            getColor(R.color.blue));

        //Show notification to user
        notificationManager.notify(tempCategory, notificationBuilder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_launcher : R.drawable.ic_launcher;
    }

    private void OurOnCreate(View view) {
        initialize();
        bindViews(view);
        setBottomTabs(view);
        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle(getResources().getString(R.string.frg_Home));
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.GONE);

        grdvw1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int offerId = Integer.parseInt(lstGrids1.get(position).getStrProductId());
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstt.BundleStrings.offerId, offerId);
                bundle.putString(AppConstt.BundleStrings.offerName, lstGrids1.get(position).getStrOfferName());
                navToOfferDetailFragment(bundle);
            }
        });
        grdvw2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int offerId = Integer.parseInt(lstGrids2.get(position).getStrProductId());
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstt.BundleStrings.offerId, offerId);
                bundle.putString(AppConstt.BundleStrings.offerName, lstGrids2.get(position).getStrOfferName());
                navToOfferDetailFragment(bundle);
            }
        });

        grdvwCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lstCategories.get(position).getId().length() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstt.BundleStrings.categoryId, lstCategories.get(position).getId());
                    bundle.putString(AppConstt.BundleStrings.categoryName, lstCategories.get(position).getName());
                    navToCategoryFragment(bundle);
                }
            }
        });

    }

    private void setBottomTabs(View frg) {
        llBottomTab[BOTTOM_TAB_FAVORITES] = frg.findViewById(R.id.tab_ll_favorites);
        llBottomTab[BOTTOM_TAB_NOTIFICATIONS] = frg.findViewById(R.id.tab_ll_notifications);
        llBottomTab[BOTTOM_TAB_NEW_OFFERS] = frg.findViewById(R.id.tab_ll_offers);

        imvBottomTab[BOTTOM_TAB_FAVORITES] = frg.findViewById(R.id.tab_img_favorites);
        imvBottomTab[BOTTOM_TAB_NOTIFICATIONS] = frg.findViewById(R.id.tab_img_notifications);
        imvBottomTab[BOTTOM_TAB_NEW_OFFERS] = frg.findViewById(R.id.tab_img_offers);

        txvBottomTab[BOTTOM_TAB_FAVORITES] = frg.findViewById(R.id.tab_txv_favorites);
        txvBottomTab[BOTTOM_TAB_NOTIFICATIONS] = frg.findViewById(R.id.tab_txv_notifications);
        txvBottomTab[BOTTOM_TAB_NEW_OFFERS] = frg.findViewById(R.id.tab_txv_offers);

        llBottomTab[BOTTOM_TAB_FAVORITES].setOnClickListener(this);
        llBottomTab[BOTTOM_TAB_NOTIFICATIONS].setOnClickListener(this);
        llBottomTab[BOTTOM_TAB_NEW_OFFERS].setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_home_tool_bar_rl_menu:
                ((MainActivity) getActivity()).OpenDrawer();
//                sendNotification("Urban Point", "abcmessage", "abc", "51", "2018-08-09 10:23:51");
                break;

            case R.id.frg_home_tool_bar_rl_search:
                navToSearchOffersFragment();
                break;

            case R.id.frg_home_btn_gain_access:
                String phone = AppConfig.getInstance().mUser.getmPhoneNumber();
                Log.d("PHONECHECKER", "onClick: " + phone);
//                logFireBaseEvent();
//                logFaceBookEvent();
//
//                if (phone.length() > 10) {
//                    progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
//                    requestCheckEligibility(phone);
//                } else {
                AppConfig.getInstance().mUser.setEligible(false);
                navToSubscriptionFragment();
                //  }
                // navToSubscriptionEligibleFragment();
                break;

            case R.id.frg_home_grd_1_next:
                if (grdvwWidth - (2.5 * grdvwItemWidth) > increment1) {
                    increment1 = increment1 + grdvwItemWidth;
                    mHorizontalScrollView1.smoothScrollTo(increment1, 0);
                } else {
                    increment1 = 0;
                    mHorizontalScrollView1.smoothScrollTo(increment1, 0);
                }
                break;

            case R.id.frg_home_grd_2_next:
                if (grdvwWidth2 - (2.5 * grdvwItemWidth) > increment2) {
                    increment2 = increment2 + grdvwItemWidth;
                    mHorizontalScrollView2.smoothScrollTo(increment2, 0);
                } else {
                    increment2 = 0;
                    mHorizontalScrollView2.smoothScrollTo(increment2, 0);
                }
                break;

            case R.id.frg_home_rl_member_msg:
                navToVerifyMemberFragment();
                break;
            case R.id.frg_home_rl_notificatn_bubble:
                navToNotificationFragment();
                break;
            case R.id.tab_ll_notifications:
                navToNotificationFragment();
                break;
            case R.id.tab_ll_favorites:
                navToFavoriteOffersFragment();
                break;
            case R.id.tab_ll_offers:
                navToNewOffersFragment();
                break;
        }
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

//    private void logFireBaseEvent() {
//        Bundle params = new Bundle();
//        params.putString("user_id", AppConfig.getInstance().mUser.getmUserId());
//        params.putString("device_type", "Android");
//        // Send the event
//        FirebaseAnalytics.getInstance(getActivity())
//                .logEvent(AppConstt.FireBaseEvents.Gain_Access, params);
//    }

//    private void logFaceBookEvent() {
//        AppEventsLogger.newLogger(getActivity()).logEvent(AppConstt.FireBaseEvents.Gain_Access);
//    }

    //region Navigation Function
    private void navToNotificationFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new NotificationFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.NotificationsFragment);
        ft.addToBackStack(AppConstt.FRGTAG.NotificationsFragment);
        ft.hide(this);
        ft.commit();
    }

    private void navToNotificationFragmentWithArguments(Bundle _b) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new NotificationFragment();
        frg.setArguments(_b);
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.NotificationsFragment);
        ft.addToBackStack(AppConstt.FRGTAG.NotificationsFragment);
        ft.hide(this);
        ft.commit();
    }

    private void navToNewOffersFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new NewOffersFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.NewOffersFragment);
        ft.addToBackStack(AppConstt.FRGTAG.NewOffersFragment);
        ft.hide(this);
        ft.commit();

    }

    private void navToSearchOffersFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new SearchOffersFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.SearchOffersFragment);
        ft.addToBackStack(AppConstt.FRGTAG.SearchOffersFragment);
        ft.hide(this);
        ft.commit();

    }

    private void navToFavoriteOffersFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new FavoriteOffersFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.FavouritesFragment);
        ft.addToBackStack(AppConstt.FRGTAG.FavouritesFragment);
        ft.hide(this);
        ft.commit();

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

    private void navToCategoryFragment(Bundle _bundle) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new CategoryFragment();
        frg.setArguments(_bundle);
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.CategoryFragment);
        ft.addToBackStack(AppConstt.FRGTAG.CategoryFragment);
        ft.hide(this);
        ft.commit();

    }

    private void navToSubscriptionFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new SubscriptionFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.SubscriptionFragment);
        ft.addToBackStack(AppConstt.FRGTAG.SubscriptionFragment);
        ft.hide(this);
        ft.commit();

    }

    private void navToSubscriptionEligibleFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        // Fragment frg = new SubscriptionFragment();
        Fragment frg = new SubscriptionEligibleFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.SubscriptionEligibleFragment);
        ft.addToBackStack(AppConstt.FRGTAG.SubscriptionEligibleFragment);
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

    //endregion

    private void requestAuthorization(String _fcmToken) {
        Home_WebHit_Post_getAuthorization home_webHit_post_getAuthorization = new Home_WebHit_Post_getAuthorization();
        home_webHit_post_getAuthorization.requestGetAuthorization(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                if (isSuccess) {
                    requestHomeApi();
                } else {
                    progressDilogue.stopiOSLoader();
                    if (customAlert != null)
                        customAlert.showCustomAlertDialog(getActivity(), null, strMsg, null, null, false, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                if (customAlert != null)
                    customAlert.showCustomAlertDialog(getActivity(), null, ex.getMessage(), null, null, false, null);
            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();

            }
        }, _fcmToken);
    }

    private void requestHomeApi() {
        Home_WebHit_Post_homeApi home_webHit_post_homeApi = new Home_WebHit_Post_homeApi();
        home_webHit_post_homeApi.requestHomeApi(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    upDateBothList();
                    updateBadges();


//                    if ((AppConfig.getInstance().isEligible) &&
//                            !(AppConfig.getInstance().mUser.isSubscribed())) {
//                        navToSubscriptionEligibleFragment();
//                    }
                } else {
                    if (customAlert != null)
                        customAlert.showCustomAlertDialog(getActivity(), null, strMsg, null, null, false, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                Log.e("home_exceptn", "ex", ex);

                progressDilogue.stopiOSLoader();
                if (customAlert != null)
                    customAlert.showCustomAlertDialog(getActivity(), null, ex.getMessage(), null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();
                AppConfig.getInstance().deleteUserData(AppConfig.getInstance().loadFCMToken());
//                customAlert.showCustomAlertDialog(getActivity(), null, getString(R.string.frg_access_code), null, null, false, null);
                if (customAlert != null)
                    customAlert.showToast(getActivity(), getString(R.string.loginError), Toast.LENGTH_SHORT);
                iNavBarUpdateUpdateListener.navToLogin();
            }
        });
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

    //region Value Update Functions
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private void updateGridViewWidth0() {
        ViewGroup.LayoutParams params = grdvwCategories.getLayoutParams();
        int screenwidth = getScreenWidth();
        Log.d("WWWWIIIIIDDTHHH", "screenwidth: " + screenwidth);
        int itemWidth = (int) (screenwidth / 4); // 0.78 is the image ratio
        Log.d("WWWWIIIIIDDTHHH", "itemWidth: " + itemWidth);

        double width = lstCategories.size() * itemWidth;
        Log.d("WWWWIIIIIDDTHHH", "widthshould be: " + width);

        int wi = (int) width;
        params.width = (wi);
        Log.d("WWWWIIIIIDDTHHH", "updateGridViewWidth: " + params.width);

        grdvwCategories.setLayoutParams(params);
        grdvwCategories.setNumColumns(lstCategories.size());
        grdvwCategories.requestLayout();
    }

    private void updateGridViewWidth1() {
        ViewGroup.LayoutParams params = grdvw1.getLayoutParams();
        Log.d("Heght", "orignalGridViewWidth1: " + params.width);
        double width = lstGrids1.size() * 160;
        int wi = (int) width;
        params.width = (dpToPx(wi));
        Log.d("Heght", "updateGridViewWidth1: " + params.width);
        grdvwItemWidth = (dpToPx(158));
        grdvwWidth = params.width;
        Log.d("Heght", "updateGridItemWidth1: " + grdvwItemWidth);
        grdvw1.setLayoutParams(params);
        grdvw1.requestLayout();
    }

    private void updateGridViewWidth2() {
        ViewGroup.LayoutParams params = grdvw2.getLayoutParams();
        Log.d("Heght", "orignalGridViewWidth2: " + params.width);
        double width = lstGrids2.size() * 160;
        int wi = (int) width;
        params.width = (dpToPx(wi));
        Log.d("Heght", "updateGridViewWidth1: " + grdvwItemWidth);
        grdvwWidth2 = params.width;
        grdvw2.setLayoutParams(params);
        grdvw2.requestLayout();
    }

    private void upDateBothList() {
        lstGrids1 = new ArrayList<>();
        lstGrids2 = new ArrayList<>();
        lstCategories = new ArrayList<>();

        if (Home_WebHit_Post_homeApi.responseObject != null) {

            updateGridVwData();
            updateGridViewWidth0();
            updateGridViewWidth1();
            updateGridViewWidth2();

            grdvwCategories.setAdapter(homeCategoriesAdapter);
            grdvw1.setNumColumns(lstGrids1.size());
            grdvw1.setAdapter(horizontalGridViewAdapter1);
            grdvw2.setNumColumns(lstGrids2.size());
            grdvw2.setAdapter(horizontalGridViewAdapter2);
        }
    }


    public void printKeyHash() {
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    "com.s.bikeandtaxi",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void updateGridVwData() {

        //Updating Home Screen Msg
        if (Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getHomePage().length() > 0) {
            txvHomeScreenMsg.setText(Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getHomePage());
        }


        //Checking if Update Dialog is required
        if (Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getVersion().getVersion().length() > 0) {
            if (Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getVersion().getForcefullyUpdated() != null &&
                    Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getVersion().getForcefullyUpdated().equalsIgnoreCase("1")) {
                AppConfig.getInstance().mUser.setForcefullyUpdateActive(true);
            } else {
                AppConfig.getInstance().mUser.setForcefullyUpdateActive(false);
            }
            float playStoreAppVersion = Float.parseFloat(Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getVersion().getVersion());
            if (playStoreAppVersion > AppConfig.getInstance().mUser.getmAppVersion()) {
                Log.e("AppUpdateVersion111", "App Version: " + AppConfig.getInstance().mUser.getmAppVersion());
                Log.e("AppUpdateVersion11", "Server Version: " + playStoreAppVersion);
                showAppUpdateDialogue(AppConfig.getInstance().mUser.isForcefullyUpdateActive());
            }
        }

        // Updating Home Categories list
        if (Home_WebHit_Post_homeApi.responseObject.getData().getCategories() != null &&
                Home_WebHit_Post_homeApi.responseObject.getData().getCategories().size() > 0) {
            for (int i = 0; i < Home_WebHit_Post_homeApi.responseObject.getData().getCategories().size(); i++) {
                lstCategories.add(new DModelGetCategories(
                        Home_WebHit_Post_homeApi.responseObject.getData().getCategories().get(i).getId(),
                        Home_WebHit_Post_homeApi.responseObject.getData().getCategories().get(i).getName(),
                        Home_WebHit_Post_homeApi.responseObject.getData().getCategories().get(i).getImage()
                ));
            }
            homeCategoriesAdapter = new HomeCategoriesAdapter(getContext(), lstCategories);
            imvCategoryLine.setBackground(getResources().getDrawable(R.drawable.orange_gadient_rectngle));
        }

        // Updating Home Special Offers list
        if (Home_WebHit_Post_homeApi.responseObject.getData().getSpecailOffers() != null &&
                Home_WebHit_Post_homeApi.responseObject.getData().getSpecailOffers().size() > 0) {
            imvFestival.setBackground(getContext().getResources().getDrawable(R.drawable.biryani_icon));
            imvFestival.setVisibility(View.VISIBLE);
            String festival = getContext().getResources().getString(R.string.festival_biryani);

            for (int i = 0; i < Home_WebHit_Post_homeApi.responseObject.getData().getSpecailOffers().size(); i++) {
                int dist = 0;
                String strImageUrl = "";
                if (Home_WebHit_Post_homeApi.responseObject.getData().getSpecailOffers().get(i).getImage() != null) {
                    strImageUrl = Home_WebHit_Post_homeApi.responseObject.getData().getSpecailOffers().get(i).getImage();
                }

                lstGrids1.add(new DModelHomeGrdVw(
                        strImageUrl,
                        Home_WebHit_Post_homeApi.responseObject.getData().getSpecailOffers().get(i).getTitle(),
                        Home_WebHit_Post_homeApi.responseObject.getData().getSpecailOffers().get(i).getId(),
                        Home_WebHit_Post_homeApi.responseObject.getData().getSpecailOffers().get(i).getName(),
                        Home_WebHit_Post_homeApi.responseObject.getData().getSpecailOffers().get(i).getSpecial(),
                        festival,
                        dist, false,
                        Float.parseFloat(Home_WebHit_Post_homeApi.responseObject.getData()
                                .getSpecailOffers().get(i).getApproxSaving())

                ));
            }
            rlProgress1.setVisibility(View.GONE);
            horizontalGridViewAdapter1 = new HorizontalGridViewAdapter(getContext(), lstGrids1);
        }

        // Updating Home MostLoved Offers list

        Log.e("most_loved_offers_size", Home_WebHit_Post_homeApi.responseObject.getData().getMostLovedOffers().size() + "");
        if (Home_WebHit_Post_homeApi.responseObject.getData().getMostLovedOffers() != null &&
                Home_WebHit_Post_homeApi.responseObject.getData().getMostLovedOffers().size() > 0) {
            for (int i = 0; i < Home_WebHit_Post_homeApi.responseObject.getData().getMostLovedOffers().size(); i++) {
                int dist = 0;
                String strImageUrl = "";
                if (Home_WebHit_Post_homeApi.responseObject.getData().getMostLovedOffers().get(i).getImage() != null) {
                    strImageUrl = Home_WebHit_Post_homeApi.responseObject.getData().getMostLovedOffers().get(i).getImage();
                }
                lstGrids2.add(new DModelHomeGrdVw(
                        strImageUrl,
                        Home_WebHit_Post_homeApi.responseObject.getData().getMostLovedOffers().get(i).getTitle(),
                        Home_WebHit_Post_homeApi.responseObject.getData().getMostLovedOffers().get(i).getId(),
                        Home_WebHit_Post_homeApi.responseObject.getData().getMostLovedOffers().get(i).getName(),
                        //"0",
                        Home_WebHit_Post_homeApi.responseObject.getData().getMostLovedOffers().get(i).getSpecial(),
                        "",
                        dist,
                        false, Float.parseFloat(Home_WebHit_Post_homeApi.responseObject.getData().
                        getMostLovedOffers().get(i).getApproxSaving())
                ));
            }
            rlProgress2.setVisibility(View.GONE);
            horizontalGridViewAdapter2 = new HorizontalGridViewAdapter(getContext(), lstGrids2);
        }


    }

    public void updateBadges() {
        AppConfig.getInstance().mProfileBadgeCount = 70;
        Log.e("email_verified_status", AppConfig.getInstance().mUser.EmailVerified + "");

        Log.e("subscription1", AppConfig.getInstance().mUser.isSubscribed +
                "&" + AppConfig.getInstance().mUser.isPremierUser);

        if (btnGainAccess != null) {
            if (AppConfig.getInstance().mUser.isSubscribed) {
                Log.e("test", "1");
                btnGainAccess.setVisibility(View.GONE);
            } else {
                Log.e("test", "2");
                btnGainAccess.setVisibility(View.VISIBLE);
            }
        }

        //Changes by Rashmi VPN
        /*Manage visibility of GAIN ACCESS button*/
        String expirydate = Home_WebHit_Post_homeApi.responseObject.getData().getSubscription().getExpiry_datetime();

        Date current_date = Calendar.getInstance().getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Log.e("date===", dateFormat.format(current_date));

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            Date currDate = dateFormat.parse(dateFormat.format(current_date));

            if (expirydate != null) {
                Date expiry_date = dateFormat1.parse(expirydate);
                //Date expiryDate = dateFormat.parse(dateFormat.format(expiry_date));
                //Date expiryDate = new Date("13/02/2019");

                Log.e("expiryDate", expiry_date.toString());

                if (currDate.before(expiry_date) || currDate.equals(expiry_date)) {
                    btnGainAccess.setVisibility(View.GONE);
                } else {
                    btnGainAccess.setVisibility(View.VISIBLE);
                }
            } /*else {

                String dtStart = "2019-03-12 20:54:21";
                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date expireDate = dateFormat1.parse(dtStart);

                if (currDate.before(expireDate) || currDate.equals(expireDate)) {
                    btnGainAccess.setVisibility(View.GONE);
                } else {
                    btnGainAccess.setVisibility(View.VISIBLE);
                }
            }*/
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Updating NewOffers Badge
        if (AppConfig.getInstance().mUserBadges.getNewOfferCount() > 0) {
            imvNewOffer.setVisibility(View.VISIBLE);
        } else {
            imvNewOffer.setVisibility(View.GONE);
        }

        //Updating Favorites Badge
        if (AppConfig.getInstance().mUserBadges.getFavoriteCount() > 0) {
            imvFavorite.setVisibility(View.VISIBLE);
        } else {
            imvFavorite.setVisibility(View.GONE);
        }

        //Updatisng UnRead Notification Badge
        int count = AppConfig.getInstance().mUserBadges.getNotificationCount();
        if (count > 0) {
            rlNotifctnBubble.setVisibility(View.VISIBLE);
            if (count > 5) {
                txvBubbleCount.setText("5+");
            } else {
                txvBubbleCount.setText(count + "");
            }
        } else {
            rlNotifctnBubble.setVisibility(View.GONE);
        }

        //Updating MenuIcon Badge
        String nationality = "";
        String EmailVerified = "";
        String PhoneVerified = "";
        if (AppConfig.getInstance().mUser.getmNationality() != null &&
                AppConfig.getInstance().mUser.getmNationality().length() > 0) {
            nationality = AppConfig.getInstance().mUser.getmNationality();
        }
        if (AppConfig.getInstance().mUser.getEmailVerified() != null &&
                AppConfig.getInstance().mUser.getEmailVerified().length() > 0) {
            EmailVerified = AppConfig.getInstance().mUser.getEmailVerified();
        }
        if (AppConfig.getInstance().mUser.getPhoneVerified() != null &&
                AppConfig.getInstance().mUser.getPhoneVerified().length() > 0) {
            PhoneVerified = AppConfig.getInstance().mUser.getPhoneVerified();
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


        //Updating Review Count Badge
        ((MainActivity) getContext()).setReviewCount(AppConfig.getInstance().mUserBadges.getReviewCount());

        //Updating Profile Completion Badge
        if (nationality.length() > 0 && EmailVerified.length() > 0 &&
                EmailVerified.equalsIgnoreCase("1")) {
            ((MainActivity) getContext()).setProfileCountVisibility(View.GONE);
        } else if ((EmailVerified.equalsIgnoreCase("1") && nationality.length() <= 0) ||
                (nationality.length() > 0 && EmailVerified.equalsIgnoreCase("0"))) {
            ((MainActivity) getContext()).setProfileCountVisibility(View.VISIBLE);
            AppConfig.getInstance().mProfileBadgeCount += 10;
        } else if (EmailVerified.equalsIgnoreCase("0") && nationality.length() == 0) {
            ((MainActivity) getContext()).setProfileCountVisibility(View.VISIBLE);
        }
        if (nationality.length() > 0 && PhoneVerified.length() > 0 &&
                PhoneVerified.equalsIgnoreCase("1")) {
            ((MainActivity) getContext()).setProfileCountVisibility(View.GONE);
        } else if ((PhoneVerified.equalsIgnoreCase("1") && nationality.length() <= 0) ||
                (nationality.length() > 0 && PhoneVerified.equalsIgnoreCase("0"))) {
            ((MainActivity) getContext()).setProfileCountVisibility(View.VISIBLE);
            AppConfig.getInstance().mProfileBadgeCount += 10;
        } else if (PhoneVerified.equalsIgnoreCase("0") && nationality.length() == 0) {
            ((MainActivity) getContext()).setProfileCountVisibility(View.VISIBLE);
        }
        ((MainActivity) getContext()).setProfileCount(AppConfig.getInstance().mProfileBadgeCount + "%");
        //Updating if User can Unsubscribe or not
        if (AppConfig.getInstance().mUser.ismCanUnSubscribe()) {
            ((MainActivity) getContext()).setUnSubscribeVisibility(View.VISIBLE);
        } else {
            ((MainActivity) getContext()).setUnSubscribeVisibility(View.GONE);
        }
    }

    private void showAppUpdateDialogue(boolean _isForcefullyUpdateActive) {
        if (!isAppUpdateDialogueDisplaying) {
            isAppUpdateDialogueDisplaying = true;
            customAlert.showAppUpdateAlertDialog(getContext(), _isForcefullyUpdateActive, new CustomAlertConfirmationInterface() {
                @Override
                public void callConfirmationDialogPositive() {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.urbanpoint.UrbanPoint"));
                    startActivity(i);
                    isAppUpdateDialogueDisplaying = false;
                }

                @Override
                public void callConfirmationDialogNegative() {
                    Log.d("dsfasdfdsaf", "callConfirmationDialogNegative: ");
                    isAppUpdateDialogueDisplaying = false;
                }
            });
        }
    }
    //endregion

    private BroadcastReceiver locationUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BroadcastISReceivng", "onReceive: ");
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                boolean isPushReceived = bundle.getBoolean(AppConstt.Notifications.PUSH_NTIFCN_BAGDE);
                if (isPushReceived) {
                    updateBadges();
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
//        loadOldPrefrencLog();
//        Checking if Update Dialog is required

        Log.e("resume", "resume");

        String tag = ((MainActivity) getActivity()).returnStackFragment();
        if (tag.equalsIgnoreCase(AppConstt.FRGTAG.HomeFragment)) {
            if (Home_WebHit_Post_homeApi.responseObject != null &&
                    Home_WebHit_Post_homeApi.responseObject.getData() != null) {
                if (Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getVersion().getVersion().length() > 0) {
                    if (Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getVersion().getForcefullyUpdated() != null &&
                            Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getVersion().getForcefullyUpdated().equalsIgnoreCase("1")) {
                        AppConfig.getInstance().mUser.setForcefullyUpdateActive(true);
                    } else {
                        AppConfig.getInstance().mUser.setForcefullyUpdateActive(false);
                    }
                    float playStoreAppVersion = Float.parseFloat(Home_WebHit_Post_homeApi.responseObject.getData().getDefaults().getVersion().getVersion());
                    if (playStoreAppVersion > AppConfig.getInstance().mUser.getmAppVersion()) {
                        Log.e("AppUpdateVersion", "App Version: " + AppConfig.getInstance().mUser.getmAppVersion());
                        Log.e("AppUpdateVersion", "Server Version: " + playStoreAppVersion);
                        showAppUpdateDialogue(AppConfig.getInstance().mUser.isForcefullyUpdateActive());
                    }
                }
            }


        }
    }

    @Override
    public void onStart() {
        super.onStart();
        localBroadcastManager.registerReceiver(locationUpdateReceiver, new IntentFilter(AppConstt.Notifications.LBCT_INTENT_NOTFICTN_UPDATED));
    }

    @Override
    public void onStop() {
        super.onStop();
        localBroadcastManager.unregisterReceiver(locationUpdateReceiver);
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
        Log.d("OldDataIs", "FCM :" + AppConfig.getInstance().loadFCMToken());

    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            updateBadges();
            Log.d("Notification", "onHiddenChanged: " + AppConfig.getInstance().mUserBadges.getNotificationCount());
            iNavBarUpdateUpdateListener.setNavBarTitle(getResources().getString(R.string.frg_Home));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.GONE);
        }
    }

}