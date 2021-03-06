package com.urbanpoint.UrbanPoint.DrawerAuxiliries;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.urbanpoint.UrbanPoint.DrawerAuxiliries.WebServices.Profile_Webhit_Post_updateProfile;
import com.urbanpoint.UrbanPoint.IntroAuxiliries.WebServices.SignUp_WebHit_Post_verifyEmail;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.RegistrationAuxiliries.PhoneVerificationFragment;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;
import com.urbanpoint.UrbanPoint.Utils.RoundedImageView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private RoundedImageView rmvImg;
    private List<String> lstNationality, lstZone;
    private TextView edtEmail, edtPhone;
    private TextView txvNationality, txvName, txvPercentage, txvOldPin, txvGender, txvNetwork, txvZone;
    private ListView lsvNationality, lsvZone;
    private NationalityListAdapter nationalityListAdapter;
    private ZoneListAdapter zoneListAdapter;
    private ImageView imvNationltiyFlag, imvFlagInRounded, imvCross;
    private RelativeLayout rlChangePin, rlVerifyemail, rlVerifynumber;
    private LinearLayout llListContainer, llProfileContainer;
    private Button btnNationalitySave, btnUpdateProfile;
    private TextView ListTitle;
    private int mSelectedPosition, zoneselectedposition;
    Drawable drawable;
    CustomAlert customAlert;
    ProgressDilogue progressDilogue;
    boolean zone;
    RelativeLayout ZoneLayout, ChangeZoneLayout, ChangenationalityLayout;
    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;
    String phoneNo, countryCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null);
        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle("Hi, " + AppConfig.getInstance().mUser.getmName());
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        AppConfig.getInstance().mProfileBadgeCount = 70;
        initialize();
        bindViews(view);
        txvName.setText(AppConfig.getInstance().mUser.getmName());
        edtEmail.setText(AppConfig.getInstance().mUser.getmEmail());

        //Changes by Rashmi VPN
        /*Set phone number*/
        String phNo = AppConfig.getInstance().mUser.getmPhoneNumber();
        Log.e("phoneNo======", phNo);

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            // phone must begin with '+'
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse("+" + phNo, "");
            countryCode = String.valueOf(numberProto.getCountryCode());
            Log.e("countryCode", countryCode + "");
            if (phNo.startsWith(countryCode)) {
//                Log.e("country===", phNo.indexOf(countryCode) + "");
                Log.e("country===", countryCode.length() + "");
                phoneNo = phNo.substring(countryCode.length());
                Log.e("phone===", phoneNo);
            }
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }

        if (phoneNo != null && countryCode != null && !phoneNo.isEmpty()) {
            edtPhone.setText("+" + countryCode + phoneNo);
        }

        //edtPhone.setText(AppConfig.getInstance().mUser.getmPhoneNumber());

        txvNetwork.setText(AppConfig.getInstance().mUser.getmNetworkType());
        txvGender.setText(AppConfig.getInstance().mUser.getmGender());
        txvOldPin.setText(AppConfig.getInstance().mUser.getmPinCode());
        if (AppConfig.getInstance().mUser.getEmailVerified() != null) {
            if (AppConfig.getInstance().mUser.getEmailVerified().equalsIgnoreCase("1")) {
                AppConfig.getInstance().mProfileBadgeCount += 10;
            }
        }
        if (AppConfig.getInstance().mUser.getPhoneVerified() != null) {
            if (AppConfig.getInstance().mUser.getPhoneVerified().equalsIgnoreCase("1")) {
                AppConfig.getInstance().mProfileBadgeCount += 10;
            }
        }

        txvPercentage.setText(AppConfig.getInstance().mProfileBadgeCount + "%");
        ((MainActivity) getContext()).setProfileCount(AppConfig.getInstance().mProfileBadgeCount + "%");

        try {
//                Log.d("IMGESLST", "list value: " + AppInstance.profileData.getNationality() + ".png");
            InputStream inputstream = getContext().getAssets().open("flags/Bangladesh.png");
            drawable = Drawable.createFromStream(inputstream, null);
            imvFlagInRounded.setImageDrawable(drawable);
            imvNationltiyFlag.setImageDrawable(drawable);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (AppConfig.getInstance().mUser.getmNationality().length() > 0) {

            ChangenationalityLayout.setVisibility(View.VISIBLE);
            ChangeZoneLayout.setVisibility(View.VISIBLE);
            txvNationality.setText(AppConfig.getInstance().mUser.getmNationality() + "");
            // btnUpdateProfile.setVisibility(View.GONE);
            txvPercentage.setVisibility(View.GONE);
            imvNationltiyFlag.setVisibility(View.GONE);
            imvCross.setVisibility(View.GONE);

            if (AppConfig.getInstance().mUser.getmNationality().equalsIgnoreCase("Others")) {
                ZoneLayout.setVisibility(View.GONE);

            } else {
                ZoneLayout.setVisibility(View.VISIBLE);
                txvZone.setText(AppConfig.getInstance().mUser.getZone());
            }


        } else {
            ChangenationalityLayout.setVisibility(View.GONE);
            ChangeZoneLayout.setVisibility(View.GONE);
            if (AppConfig.getInstance().mUser.isNationalityLstDisplyd()) {
                txvPercentage.setVisibility(View.VISIBLE);
                imvCross.setVisibility(View.GONE);
            } else {
                imvCross.setVisibility(View.VISIBLE);
                llProfileContainer.setVisibility(View.VISIBLE);
                btnNationalitySave.setVisibility(View.GONE);
                AppConfig.getInstance().mUser.setNationalityLstDisplyd(true);
                AppConfig.getInstance().saveUserData();
            }
        }


        lsvNationality.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        lsvZone.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        lsvNationality.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                imvNationltiyFlag.setVisibility(View.VISIBLE);
//                lsvNationality.setVisibility(View.GONE);
//                utilObj.keyboardClose(mContext, view);

                Log.e("item click", "true");
                mSelectedPosition = position;

                nationalityListAdapter.setPosition(position);
                nationalityListAdapter.notifyDataSetInvalidated();
                btnNationalitySave.setVisibility(View.VISIBLE);
            }
        });


        lsvZone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                zoneselectedposition = position;
                zoneListAdapter.setPosition(position);
                zoneListAdapter.notifyDataSetInvalidated();
                btnNationalitySave.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }


    private void initialize() {
        lstZone = new ArrayList<>();
        lstNationality = new ArrayList<>();
        mSelectedPosition = -1;
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
    }

    private void bindViews(View frg) {
        ListTitle = frg.findViewById(R.id.frg_profile_list_view_nationality_msg);
        ZoneLayout = frg.findViewById(R.id.zone_layout);
        rlVerifyemail = frg.findViewById(R.id.frg_profile_verify_email);
        rlVerifynumber = frg.findViewById(R.id.frg_profile_verify_number);
        llListContainer = frg.findViewById(R.id.frg_profile_ll_list_container);
        llProfileContainer = frg.findViewById(R.id.frg_profile_ll);
        imvCross = frg.findViewById(R.id.frg_profile_imv_close);
        imvCross.setOnClickListener(this);
        txvPercentage = frg.findViewById(R.id.frg_profile_txv_profile_percentage);
        txvOldPin = frg.findViewById(R.id.frg_profile_txv_old_pin);
        txvGender = frg.findViewById(R.id.frg_my_profile_txv_gender);
        txvNetwork = frg.findViewById(R.id.frg_profile_txv_network);
        btnUpdateProfile = frg.findViewById(R.id.frg_profile_update);
        btnUpdateProfile.setOnClickListener(this);
        btnNationalitySave = frg.findViewById(R.id.frg_profile_nationality_save);
        btnNationalitySave.setOnClickListener(this);
        imvFlagInRounded = frg.findViewById(R.id.frg_profile_imv_flag);
        imvFlagInRounded.setImageResource(R.mipmap.nationality_0);
        rmvImg = frg.findViewById(R.id.frg_profile_round__imv_pic);
        rmvImg.setImageResource(R.mipmap.nationality_2);
        txvName = frg.findViewById(R.id.frg_profile_edt_name);
        edtEmail = frg.findViewById(R.id.frg_profile_edt_email);
        edtPhone = frg.findViewById(R.id.frg_profile_edt_number);
        txvZone = frg.findViewById(R.id.frg_profile_edt_zone);
        ChangenationalityLayout = frg.findViewById(R.id.frg_profile_rl_change_nationality);
        ChangeZoneLayout = frg.findViewById(R.id.frg_profile_rl_change_zone);
        txvNationality = frg.findViewById(R.id.frg_profile_edt_nationality);
        ChangeZoneLayout.setOnClickListener(this);
        ChangenationalityLayout.setOnClickListener(this);
        txvNationality.setOnClickListener(this);
        txvZone.setOnClickListener(this);
        lsvNationality = frg.findViewById(R.id.frg_profile_list_view_nationality_2);
        lsvZone = frg.findViewById(R.id.frg_profile_list_view_zone);
        imvNationltiyFlag = frg.findViewById(R.id.frg_profile_imv_nationality);
        rlChangePin = frg.findViewById(R.id.frg_profile_rl_change_pin);
        rlChangePin.setOnClickListener(this);
        rlVerifyemail.setOnClickListener(this);
        rlVerifynumber.setOnClickListener(this);
        lstNationality = Arrays.asList(AppConstt.arrFlags);
        Log.d("IMGESLST", "Unsorted: " + lstNationality);
        nationalityListAdapter = new NationalityListAdapter(getContext(), mSelectedPosition, lstNationality);
        lsvNationality.setAdapter(nationalityListAdapter);
        if (AppConfig.getInstance().mUser.getEmailVerified() != null) {
            if (AppConfig.getInstance().mUser.getEmailVerified().equalsIgnoreCase("1")) {
                rlVerifyemail.setVisibility(View.GONE);
            } else {
                rlVerifyemail.setVisibility(View.VISIBLE);
            }
        } else {
            rlVerifyemail.setVisibility(View.VISIBLE);
        }

        /*Changes by Rashmi VPN*/
        /*if (AppConfig.getInstance().mUser.getPhnoVerified() != null) {
            if (AppConfig.getInstance().mUser.getPhnoVerified().equalsIgnoreCase("1")) {
                rlVerifynumber.setVisibility(View.GONE);
            } else {
                rlVerifynumber.setVisibility(View.VISIBLE);
            }
        } else {
            rlVerifynumber.setVisibility(View.VISIBLE);
        }*/

        if (AppConfig.getInstance().mUser.getPhoneVerified() != null) {
            if (AppConfig.getInstance().mUser.getPhoneVerified().equalsIgnoreCase("1")) {
                rlVerifynumber.setVisibility(View.GONE);
            } else {
                rlVerifynumber.setVisibility(View.VISIBLE);
            }
        } else {
            rlVerifynumber.setVisibility(View.VISIBLE);
        }

    }

    public void navToPhoneVerification() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment frg = new PhoneVerificationFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.ReferAndEarnFragment);
        ft.addToBackStack(AppConstt.FRGTAG.ReferAndEarnFragment);
        ft.commit();
    }

    private void verifyEmail() {

        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        SignUp_WebHit_Post_verifyEmail signUp_webHit_checkPhone = new SignUp_WebHit_Post_verifyEmail();
        signUp_webHit_checkPhone.verifyEmail(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                Log.e("register_res_boolean", isSuccess + "," + strMsg);
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
//                    logFireBaseEvent();
//                    logFaceBookEvent();
//                    logMixPanelEvent();
                    if (SignUp_WebHit_Post_verifyEmail.responseObject != null) {
                        customAlert.showCustomAlertDialog(getContext(), getString(R.string.verify_email), getResources().getString(R.string.link_sent), null, null, false, null);
//                        Bundle b = new Bundle();
//                        b.putString(AppConstt.BundleStrings.userId, AppConfig.getInstance().mUser.getmUserId());
//                        AppConfig.getInstance().isCommingFromSplash = true;
//                        AppConfig.getInstance().mUser.setLoggedIn(true);
                        //navToMainActivity();
//                        showVerifyEmailDialog();
                    }
                } else {

                    if (strMsg.equalsIgnoreCase("Conflict")) {
                        customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), getResources().getString(R.string.already_registered), null, null, false, null);
                    } else {
                        customAlert.showCustomAlertDialog(getContext(), getString(R.string.sign_up_enter_account_setup_heading), strMsg, null, null, false, null);

                    }
                }
            }

            @Override
            public void onWebException(Exception ex) {
                Log.e("ex", "ex", ex);
                progressDilogue.stopiOSLoader();
                customAlert.showCustomAlertDialog(getActivity(), getString(R.string.sign_in_unsuccess_login_heading), ex.getMessage(), null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                Log.e("log", "out");
                progressDilogue.stopiOSLoader();

            }
        }, AppConfig.getInstance().mUser.mUserId);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.frg_profile_rl_change_zone:
                Log.e("change", "zone");
                if (txvNationality.getText().toString().length() > 0) {
                    zone = true;
                    btnNationalitySave.setVisibility(View.GONE);
                    imvCross.setVisibility(View.VISIBLE);
                    llListContainer.setVisibility(View.VISIBLE);
                    llProfileContainer.setVisibility(View.GONE);
                    lsvZone.setVisibility(View.VISIBLE);
                    lsvNationality.setVisibility(View.GONE);
                    ListTitle.setText(getString(R.string.select_zone));


                    if (txvNationality.getText().toString().equalsIgnoreCase("Dhaka North")) {
                        lstZone = Arrays.asList(AppConstt.arrNorthZone);
                        zoneListAdapter = new ZoneListAdapter(getContext(), zoneselectedposition, lstZone);
                        lsvZone.setAdapter(zoneListAdapter);
                    } else {
                        lstZone = Arrays.asList(AppConstt.arrSouthZone);
                        zoneListAdapter = new ZoneListAdapter(getContext(), zoneselectedposition, lstZone);
                        lsvZone.setAdapter(zoneListAdapter);
                    }
                } else {
                    Toast.makeText(getActivity(), "Please select location", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.frg_profile_rl_change_nationality:
                Log.e("change", "nationality");
                zone = false;
                ListTitle.setText(getString(R.string.natinality_message));
                btnNationalitySave.setVisibility(View.GONE);
                imvCross.setVisibility(View.VISIBLE);
                llListContainer.setVisibility(View.VISIBLE);
                llProfileContainer.setVisibility(View.GONE);
                lsvZone.setVisibility(View.GONE);
                lsvNationality.setVisibility(View.VISIBLE);

                break;
            case R.id.frg_profile_rl_change_pin:
                navToChangePinFragment();
                break;
            case R.id.frg_profile_verify_email:
                verifyEmail();
                break;
            case R.id.frg_profile_verify_number:
                navToPhoneVerification();
                break;
            case R.id.frg_profile_imv_close:
                llProfileContainer.setVisibility(View.VISIBLE);
                llListContainer.setVisibility(View.GONE);
                imvCross.setVisibility(View.GONE);
                mSelectedPosition = -1;
                nationalityListAdapter.setPosition(mSelectedPosition);
                nationalityListAdapter.notifyDataSetInvalidated();
                break;

            case R.id.frg_profile_nationality_save:
                llProfileContainer.setVisibility(View.VISIBLE);
                llListContainer.setVisibility(View.GONE);
                imvCross.setVisibility(View.GONE);
                if (!zone) {
                    if (mSelectedPosition > -1) {
//                    AppConfig.getInstance().mUser.setmNationality(lstNationality.get(mSelectedPosition));
                        txvNationality.setText(lstNationality.get(mSelectedPosition));
                        txvZone.setText("");
                        if (mSelectedPosition == 0) {
                            ZoneLayout.setVisibility(View.VISIBLE);
                            lstZone = Arrays.asList(AppConstt.arrNorthZone);
                            Log.e("check", "11");
                            zoneListAdapter = new ZoneListAdapter(getContext(), zoneselectedposition, lstZone);
                            lsvZone.setAdapter(zoneListAdapter);
//                            zoneListAdapter.notifyDataSetChanged();
                        } else if (mSelectedPosition == 1) {
                            Log.e("check", "22");
                            ZoneLayout.setVisibility(View.VISIBLE);
                            lstZone = Arrays.asList(AppConstt.arrSouthZone);
                            zoneListAdapter = new ZoneListAdapter(getContext(), zoneselectedposition, lstZone);
                            lsvZone.setAdapter(zoneListAdapter);
                        } else {
                            ZoneLayout.setVisibility(View.GONE);
                        }

                    }


                    nationalityListAdapter.setPosition(-1);
                    nationalityListAdapter.notifyDataSetInvalidated();
                } else {
                    if (zoneselectedposition > -1) {
//                    AppConfig.getInstance().mUser.setmNationality(lstNationality.get(mSelectedPosition));
                        txvZone.setText(lstZone.get(zoneselectedposition));
//

                    }
                    zoneListAdapter.setPosition(-1);
                    zoneListAdapter.notifyDataSetInvalidated();
                }

                break;
            case R.id.frg_profile_edt_zone:
                zone = true;
                btnNationalitySave.setVisibility(View.GONE);
                imvCross.setVisibility(View.VISIBLE);
                llListContainer.setVisibility(View.VISIBLE);
                llProfileContainer.setVisibility(View.GONE);
                lsvZone.setVisibility(View.VISIBLE);
                lsvNationality.setVisibility(View.GONE);
                ListTitle.setText(getString(R.string.select_zone));
                break;
            case R.id.frg_profile_edt_nationality:
                zone = false;
                ListTitle.setText(getString(R.string.natinality_message));
                if (AppConfig.getInstance().mUser.getmNationality().length() < 1) {
                    btnNationalitySave.setVisibility(View.GONE);
                    imvCross.setVisibility(View.VISIBLE);
                    llListContainer.setVisibility(View.VISIBLE);
                    llProfileContainer.setVisibility(View.GONE);
                    lsvZone.setVisibility(View.GONE);
                    lsvNationality.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.frg_profile_update:
                if (txvNationality.getText().length() <= 0) {
                    customAlert.showCustomAlertDialog(getActivity(), null, getString(R.string.nationality_hint), null, null, false, null);

                } else if (!(txvNationality.getText().toString().equalsIgnoreCase("Others"))
                        && txvZone.getText().length() <= 0) {
                    customAlert.showCustomAlertDialog(getActivity(), null, getString(R.string.select_zone), null, null, false, null);

                } else {
                    progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
                    requestUpdateProfile(txvNationality.getText().toString(), "", "", true,
                            txvZone.getText().toString());
                    //customAlert.showCustomAlertDialog(getActivity(), null, getString(R.string.nationality_hint), null, null, false, null);
                }
                break;
        }
    }

    private void navToChangePinFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new ChangePinFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.ChangePinFragment);
        ft.addToBackStack(AppConstt.FRGTAG.ChangePinFragment);
        ft.hide(this);
        ft.commit();
    }

    private void requestUpdateProfile(final String _nationality, String _password, String _oldPassword,
                                      boolean _isNaltionalityUpdate, final String _zone) {
        Profile_Webhit_Post_updateProfile profile_webhit_post_updateProfile = new Profile_Webhit_Post_updateProfile();
        profile_webhit_post_updateProfile.requestUpdateProfile(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    AppConfig.getInstance().mUser.setmNationality(_nationality);
                    AppConfig.getInstance().mUser.setZone(_zone);
                    AppConfig.getInstance().saveUserData();
                    ((MainActivity) getContext()).setProfileCountVisibility(View.GONE);

                    if (AppConfig.getInstance().mUserBadges.getReviewCount() == 0 &
                            AppConfig.getInstance().mUser.getEmailVerified().equalsIgnoreCase("1")) {
                        ((MainActivity) getContext()).setMenuBadgeVisibility(false);
                    }

                    imvNationltiyFlag.setVisibility(View.GONE);
                    txvPercentage.setVisibility(View.GONE);
                    customAlert.showToast(getContext(), strMsg, Toast.LENGTH_LONG);
                } else {
                    customAlert.showCustomAlertDialog(getActivity(), null, strMsg,
                            null, null, false,
                            null);
                }

            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                customAlert.showCustomAlertDialog(getActivity(), null, ex.getMessage(),
                        null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();

            }
        }, _nationality, _password, _oldPassword, _isNaltionalityUpdate, _zone);
    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            txvOldPin.setText(AppConfig.getInstance().mUser.getmPinCode());
            iNavBarUpdateUpdateListener.setNavBarTitle("Hi, " + AppConfig.getInstance().mUser.getmName());
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }
    }
}
