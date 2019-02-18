package com.urbanpoint.UrbanPoint.RegistrationAuxiliries;


import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.UIManager;
//import com.facebook.appevents.AppEventsLogger;
//import com.google.firebase.analytics.FirebaseAnalytics;
//import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.SignUpVerificationFragment;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.SignUp_WebHit_Post_addUser;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.SignUp_WebHit_Post_checkPhoneEmail;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.SignUp_WebHit_Post_verifyEmail;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static com.urbanpoint.UrbanPoint.Utils.AppConstt.MIXPANEL_TOKEN;

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
    ProgressDilogue progressDilogue;
    private ListView lsvFavorites;
    CustomAlert customAlert;
//    private List<DModelHomeGrdVw> lstFavoritesByLocation, lstFavoritesByAlphabetically;
//    private FavoritesAdapter favoritesAdapter;
    private RelativeLayout rlAlphabetically, rlLocation;
    private TextView Back, Continue;
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
        initialize(view);

        if (checkSelfPermission(getActivity(), Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(getActivity(),
                        Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 0);
        }
        return view;
    }


    private void initialize(View view) {
      //  MyApplication.getInstance().trackScreenView(getString(R.string.contact_us));
        utilObj = new Utility(getActivity());
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        frgMngr = getFragmentManager();
        novaThin = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_thin.ttf");
        novaRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_regular.ttf");
        setActionBar("Verify", false);
        isLocationSort = true;
//        isSubscribed = AppPreference.getSettingResturnsBoolean(mActivity, Constants.DEFAULT_VALUES.IS_USER_SUBSCRIBE, false);
//        lstFavoritesByLocation = new ArrayList<>();
//        lstFavoritesByAlphabetically = new ArrayList<>();
        bindViews(view);
    }

    private void bindViews(View view) {
     Back=(Button)view.findViewById(R.id.back);
     Continue=(Button)view.findViewById(R.id.btn_continue);
     Continue.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Log.e("on","click");
           //phoneLogin();
             phoneLoginFragment();
         }
     });
    }

    private void phoneLoginFragment(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment frg = new PhoneVerificationFragment();
        ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
        ft.replace(R.id.containerIntroFragments, frg);
        ft.commit();
    }

    int APP_REQUEST_CODE =99;
    @Override
    public void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            if (data != null) {
                if (data.hasExtra(AccountKitLoginResult.RESULT_KEY)) {
                    AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
                    String toastMessage = "";
                    if (loginResult.getError() != null) {
                        toastMessage = loginResult.getError().getErrorType().getMessage();

                    } else if (loginResult.wasCancelled()) {
                        toastMessage = "Login Cancelled";
                    } else {

                        getaccount_info();
                    }
                    Log.e("result", toastMessage);
                    // Surface the result to your user in an appropriate way.
//            Toast.makeText(getContext(),
//                    toastMessage,
//                    Toast.LENGTH_LONG)
//                    .show();
                }
            }
        }
    }

    private void getaccount_info()
    {
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account)
            {
                Log.e("mobileno",account.getPhoneNumber()+"");

                String phoneno= String.valueOf(account.getPhoneNumber());
                phoneno=phoneno.replace("+91","");
                verifyPhoneNo(phoneno);
            }

            @Override
            public void onError(AccountKitError accountKitError) {
                Log.e("account_kit_error",accountKitError+"");

            }
        });
    }
//    private void logFireBaseEvent() {
//        Bundle params = new Bundle();
//        params.putString("user_id", AppConfig.getInstance().mUser.getmUserId());
//        params.putString("device_type", "Android");
//        // Send the event
//        FirebaseAnalytics.getInstance(getActivity()).logEvent(AppConstt.FireBaseEvents.Successful_Signup, params);
//    }
//    private void logFaceBookEvent() {
//        AppEventsLogger.newLogger(getActivity()).logEvent(AppConstt.FireBaseEvents.Successful_Signup);
//    }
//
//    private void logMixPanelEvent() {
//        String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
//        MixpanelAPI mixpanel = MixpanelAPI.getInstance(getActivity(), MIXPANEL_TOKEN);
//        mixpanel.identify(AppConfig.getInstance().mUser.getmUserId());
//        mixpanel.getPeople().identify(AppConfig.getInstance().mUser.getmUserId());
//        mixpanel.getPeople().set("Email", AppConfig.getInstance().mUser.getmEmail());
//        mixpanel.getPeople().set("Gender", AppConfig.getInstance().mUser.getmGender());
//        mixpanel.getPeople().set("Created at", timeStamp);
//        mixpanel.getPeople().set("Last logged in at", timeStamp);
//    }
    private void navToMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }
//    private void navToSignUpVerificationFragment(Bundle b) {
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        Fragment frg = new SignUpVerificationFragment();
//        frg.setArguments(b);
//        ft.add(R.id.activity_intro_frm, frg, AppConstt.FRGTAG.FN_SignUpVerificationFragment);
//        ft.addToBackStack(AppConstt.FRGTAG.FN_SignUpVerificationFragment);
//        ft.hide(this);
//        ft.commit();
//    }
    private void verifyPhoneNo(String _phone) {

        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        SignUp_WebHit_Post_checkPhoneEmail signUp_webHit_checkPhone = new SignUp_WebHit_Post_checkPhoneEmail();
        signUp_webHit_checkPhone.requestcheckPhoneEmail(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                Log.e("register_res_boolean",isSuccess+","+strMsg);
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
//                    logFireBaseEvent();
//                    logFaceBookEvent();
//                    logMixPanelEvent();
                    if (SignUp_WebHit_Post_checkPhoneEmail.responseObject != null)
                    {
                        Bundle b = new Bundle();
                        b.putString(AppConstt.BundleStrings.userId, AppConfig.getInstance().mUser.getmUserId());
                        AppConfig.getInstance().isCommingFromSplash = true;
                        AppConfig.getInstance().mUser.setLoggedIn(true);
                      //  navToMainActivity();
                        showVerifyEmailDialog();
                    }
                } else {

                    if (strMsg.equalsIgnoreCase("Conflict")) {
                        customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), getResources().getString(R.string.already_registered), null, null, false, null);
                    }
                    else
                    {
                        customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), strMsg, null, null, false, null);

                    }
                }
            }

            @Override
            public void onWebException(Exception ex) {
                Log.e("ex","ex",ex);
                progressDilogue.stopiOSLoader();
                customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_in_unsuccess_login_heading), ex.getMessage(), null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                Log.e("log","out");
                progressDilogue.stopiOSLoader();

            }
        }, _phone);

    }



    private void verifyEmail() {

        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        SignUp_WebHit_Post_verifyEmail signUp_webHit_checkPhone = new SignUp_WebHit_Post_verifyEmail();
        signUp_webHit_checkPhone.verifyEmail(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                Log.e("register_res_boolean",isSuccess+","+strMsg);
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
//                    logFireBaseEvent();
//                    logFaceBookEvent();
//                    logMixPanelEvent();
                    if (SignUp_WebHit_Post_verifyEmail.responseObject != null)
                    {
//                        Bundle b = new Bundle();
//                        b.putString(AppConstt.BundleStrings.userId, AppConfig.getInstance().mUser.getmUserId());
//                        AppConfig.getInstance().isCommingFromSplash = true;
//                        AppConfig.getInstance().mUser.setLoggedIn(true);
                        navToMainActivity();
//                        showVerifyEmailDialog();
                    }
                } else {

                    if (strMsg.equalsIgnoreCase("Conflict")) {
                        customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), getResources().getString(R.string.already_registered), null, null, false, null);
                    }
                    else
                    {
                        customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), strMsg, null, null, false, null);

                    }
                }
            }

            @Override
            public void onWebException(Exception ex) {
                Log.e("ex","ex",ex);
                progressDilogue.stopiOSLoader();
                customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_in_unsuccess_login_heading), ex.getMessage(),
                        null, null, false, null);
            }

            @Override
            public void onWebLogout() {
                Log.e("log","out");
                progressDilogue.stopiOSLoader();

            }
        }, AppConfig.getInstance().mUser.mUserId);

    }

    private void showVerifyEmailDialog()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.custome_message_alert_box);

        dialog.setCancelable(false);
        // set the custom dialog components - text
        TextView messageTitle = (TextView) dialog.findViewById(R.id.messageTitle);
        TextView messageText = (TextView) dialog.findViewById(R.id.dialogMessageText);
        Button cancelButton = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        Button okButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        cancelButton.setText(getString(R.string._skip));

        TextView dialogButtonSeparator = (TextView) dialog.findViewById(R.id.dialogButtonSeparator);
      dialogButtonSeparator.setVisibility(View.VISIBLE);
//        cancelButton.setVisibility(View.GONE);
//        okButton.setVisibility(View.GONE);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               navToMainActivity();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyEmail();
            }
        });
        messageTitle.setText(getString(R.string.verify_email));
        messageText.setText(getString(R.string.send_link_to_verify_email));
        dialog.show();

    }


    public void phoneLogin() {
    //    AccountKit.logOut();
        final Intent intent = new Intent(getActivity(), AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        UIManager uiManager=new SkinManager(SkinManager.Skin.CLASSIC,Color.parseColor("#a83664"));
//        UIManager uiManager=new SkinManager(SkinManager.Skin.CONTEMPORARY,Color.parseColor(getActivity().
//                getResources().getColor(R.color.expndble_lst_color),SkinManager.Tint.WHITE))
//       U uiManager = new SkinManager(SkinManager.Skin.CONTEMPORARY,Color.parseColor("#6200EE"),
//               SkinManager.Tint.WHITE,60);

        configurationBuilder.setUIManager(uiManager);
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
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
//        mBackButton.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                frgMngr.popBackStack();
//                return false;
//            }
//        });

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
