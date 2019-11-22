package com.urbanpoint.UrbanPoint.DrawerAuxiliries;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.urbanpoint.UrbanPoint.DrawerAuxiliries.WebServices.AccessCode_Webhit_Post_usePromoCode;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.CustomAlertConfirmationInterface;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

/**
 * Created by Danish on 2/19/2018.
 */

public class AccessCodeFragment extends Fragment implements View.OnClickListener {
    private Button btnSubmit;
    private EditText edtAccessCode;
    private String promoCode = "";
    private boolean backBtnVisibility;
    private ProgressDilogue progressDilogue;
    private CustomAlert customAlert;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_access_code, container, false);
        initiate();
        bindViews(v);
        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        if (backBtnVisibility) {
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        } else {
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
        }
        iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_access_code));
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        return v;
    }

    private void initiate() {
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        backBtnVisibility = false;
        Bundle b = this.getArguments();
        if (b != null) {
            backBtnVisibility = b.getBoolean(AppConstt.BundleStrings.backBtnVisibility);
        }
    }

    void bindViews(View v) {
        btnSubmit = v.findViewById(R.id.frg_access_code_btn_submit);
        edtAccessCode = v.findViewById(R.id.frg_access_code_edt_code);

        btnSubmit.setOnClickListener(this);
    }

    private boolean validatingRequired() {
        String message = "";
        promoCode = edtAccessCode.getText().toString();

        //validate the content
        if (promoCode.equalsIgnoreCase("")) {
            message = getResources().getString(R.string.enter_access_code_empty_2);
            customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
        }

        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_access_code_btn_submit:
                if (validatingRequired()) {
                    progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
                    requestFreeAccess(edtAccessCode.getText().toString());
                }
                break;

        }
    }

    private void requestFreeAccess(String _code) {
        AccessCode_Webhit_Post_usePromoCode accessCode_webhit_post_usePromoCode = new AccessCode_Webhit_Post_usePromoCode();
        accessCode_webhit_post_usePromoCode.requestFreeAccess(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    AppConfig.getInstance().mUser.setSubscribed(true);
                    AppConfig.getInstance().mUser.setmCanUnSubscribe(false);
                    customAlert.showCustomAlertDialog(getActivity(), null, getString(R.string.congratulations_access), null, null, false, new CustomAlertConfirmationInterface() {
                        @Override
                        public void callConfirmationDialogPositive() {
                            ((MainActivity) getActivity()).navToHomeFragment();
                        }

                        @Override
                        public void callConfirmationDialogNegative() {

                        }
                    });
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
        }, _code);
    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_access_code));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }
    }
}
