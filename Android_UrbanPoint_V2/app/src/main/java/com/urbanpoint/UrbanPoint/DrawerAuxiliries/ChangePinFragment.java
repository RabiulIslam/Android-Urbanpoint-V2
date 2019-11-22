package com.urbanpoint.UrbanPoint.DrawerAuxiliries;


import android.content.res.Resources;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.DrawerAuxiliries.WebServices.Profile_Webhit_Post_updateProfile;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.PinEntry;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePinFragment extends Fragment implements View.OnClickListener {

    private TextView mWelcomeText, mUserEmail, mChangePinButton;
    private String enteredCurrentPin = "", enteredNewPin = "", enteredConfirmPin = "";
    private PinEntry mChangePinOldPinEntry, mChangePinNewPinEntry, mChangePinConfirmNewPinEntry;
    private LinearLayout mMainParentLayout;
    private FragmentManager frgMngr;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    CustomAlert customAlert;
    ProgressDilogue progressDilogue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_pin, null);
        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle(getString(R.string.frg_change_pin));
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        initialize();
        bindViews(view);
        return view;
    }


    private void initialize() {
        frgMngr = getFragmentManager();
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();

    }

    private void bindViews(View frg) {
        mMainParentLayout = frg.findViewById(R.id.frg_change_pin_mainparentlayout);
        mMainParentLayout.setOnClickListener(this);


        mWelcomeText = frg.findViewById(R.id.frg_change_pin_txv_welcome);
        mUserEmail = frg.findViewById(R.id.frg_change_pin_txv_email);
        mChangePinButton = frg.findViewById(R.id.frg_change_pin_btn_change);
        mChangePinButton.setOnClickListener(this);

        mChangePinOldPinEntry = frg.findViewById(R.id.frg_change_pin_changePinOldPinEntry);
        mChangePinNewPinEntry = frg.findViewById(R.id.frg_change_pin_changePinNewPinEntry);
        mChangePinConfirmNewPinEntry = frg.findViewById(R.id.frg_change_pin_changePinConfirmNewPinEntry);

        mChangePinOldPinEntry.setOnPinEnteredListener(new PinEntry.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                if (pin.length() == 4) {
                    enteredCurrentPin = pin;
                    mChangePinNewPinEntry.requestFocus();
                } else {
                    enteredCurrentPin = "";
                }
            }
        });
        mChangePinNewPinEntry.setOnPinEnteredListener(new PinEntry.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                if (pin.length() == 4) {
                    enteredNewPin = pin;
                    mChangePinConfirmNewPinEntry.requestFocus();
                } else {
                    enteredNewPin = "";
                }
            }
        });
        mChangePinConfirmNewPinEntry.setOnPinEnteredListener(new PinEntry.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                if (pin.length() == 4) {
                    enteredConfirmPin = pin;
                    AppConfig.getInstance().closeKeyboard(getActivity());
                } else {
                    enteredConfirmPin = "";
                }
            }
        });
        setUserDetails();
    }

    private void setUserDetails() {

        String emailID = AppConfig.getInstance().mUser.getmEmail();
        String userName = AppConfig.getInstance().mUser.getmName();

        Resources resources = getResources();
        userName = String.format(resources.getString(R.string.change_pin_welcome), userName);
        mWelcomeText.setText(userName);
        mUserEmail.setText(emailID);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.frg_change_pin_btn_change:
                if (validatingRequired()) {
                    progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
                    requestUpdateProfile("",enteredNewPin , enteredCurrentPin, false);
                }
                break;
            case R.id.frg_change_pin_mainparentlayout:
                AppConfig.getInstance().closeKeyboard(getActivity());
                break;
        }

    }

    private void requestUpdateProfile(final String _nationality, String _password, String _oldPassword, boolean _isNaltionalityUpdate) {
        Profile_Webhit_Post_updateProfile profile_webhit_post_updateProfile = new Profile_Webhit_Post_updateProfile();
        profile_webhit_post_updateProfile.requestUpdateProfile(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    customAlert.showToast(getActivity(), strMsg, Toast.LENGTH_LONG);
                    frgMngr.popBackStackImmediate();
                 } else {
                    customAlert.showCustomAlertDialog(getActivity(), null, strMsg, null, null, false, null);
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
        }, _nationality, _password, _oldPassword, _isNaltionalityUpdate,AppConfig.getInstance().mUser.zone);
    }


    private boolean validatingRequired() {
        String message = "";


        //validate the content

        if (enteredCurrentPin.length() < 4) {
            message = getResources().getString(R.string.enter_current_pin_message);
            customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
        } else if (enteredNewPin.length() < 4) {
            message = getResources().getString(R.string.enter_new_pin_message);
            customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
        } else if (enteredConfirmPin.length() < 4) {
            message = getResources().getString(R.string.enter_new_confirm_pin_message);
            customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
        } else if (!enteredNewPin.equalsIgnoreCase(enteredConfirmPin)) {
            message = getResources().getString(R.string.enter_pin_not_match);
            customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
            mChangePinNewPinEntry.clearText();
            mChangePinConfirmNewPinEntry.clearText();
        }

        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            iNavBarUpdateUpdateListener.setNavBarTitle(getString(R.string.frg_change_pin));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }
    }
}
