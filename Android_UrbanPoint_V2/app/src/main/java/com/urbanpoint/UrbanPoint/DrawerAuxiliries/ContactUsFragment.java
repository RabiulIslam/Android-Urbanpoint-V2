package com.urbanpoint.UrbanPoint.DrawerAuxiliries;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.urbanpoint.UrbanPoint.DrawerAuxiliries.WebServices.ContactUs_Webhit_Post_contactUs;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragment;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.CustomAlertConfirmationInterface;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

/**
 * Created by Danish on 3/14/2018.
 */

public class ContactUsFragment extends Fragment implements View.OnClickListener {
    private ImageView mSlideButton;
    private Spinner mContactUsCategorySpinner;
    private Button mContactUsSubmit;
    private EditText mContactUsMessage;
    private LinearLayout mMainParentLayout;
    CustomAlert customAlert;
    private ProgressDilogue progressDilogue;
    private FragmentManager mFrgmgr;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact_us, container, false);
        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_my_contact_us));
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        mFrgmgr = getActivity().getSupportFragmentManager();
        bindViews(v);
        return v;
    }

    void bindViews(View v) {
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        mMainParentLayout = (LinearLayout) v.findViewById(R.id.frg_change_pin_mainparentlayout);
        mMainParentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppConfig.getInstance().closeKeyboard(getActivity());
                return false;
            }
        });

        mContactUsCategorySpinner = v.findViewById(R.id.contactUsCategorySpinner);
        mContactUsMessage = v.findViewById(R.id.contactUsMessage);
        mContactUsSubmit = v.findViewById(R.id.contactUsSubmit);
        mContactUsSubmit.setOnClickListener(this);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.contact_us_reason_array, R.layout.contact_us_custom_spinner);

        adapter.setDropDownViewResource(R.layout.contact_us_custom_spinner);
        mContactUsCategorySpinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contactUsSubmit:

                String message = "";

                if (mContactUsCategorySpinner.getSelectedItemPosition() == 0) {
                    message = getResources().getString(R.string.select_reason);
                    //utilObj.showError(this, message, textViewObj, emailObj);
                    customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
                } else if (mContactUsMessage.getText().toString().trim().length() == 0) {
                    message = getResources().getString(R.string.enter_into_comment_box);
                    //utilObj.showError(this, message, textViewObj, emailObj);
                    customAlert.showCustomAlertDialog(getActivity(), null, message, null, null, false, null);
                } else {
                    String reason = (String) mContactUsCategorySpinner.getSelectedItem();
                    String body = mContactUsMessage.getText().toString();
//                    progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
                   requestContactUs(reason, body);

//                    String URI = "mailto:?subject=" + reason + "&body=" + body + "&to=" + AppConstt.ContactUS.EMAIL;
//
//
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    Uri data = Uri.parse(URI);
//                    intent.setData(data);
//                    startActivity(intent);
                    break;
                }
        }
    }

    private void requestContactUs(String _reason, String _body) {
        ContactUs_Webhit_Post_contactUs contactUs_webhit_post_contactUs = new ContactUs_Webhit_Post_contactUs();
        contactUs_webhit_post_contactUs.requestContactUs(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    customAlert.showCustomAlertDialog(getActivity(),
                            null, "Thanks for contacting us." +
                            " Our team will contact you shortly",
                            null, null,
                            false,
                            new CustomAlertConfirmationInterface() {
                        @Override
                        public void callConfirmationDialogPositive() {
                            navToHomeFragment();

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
        }, _reason, _body);
    }

    public void navToHomeFragment() {
        clearMyBackStack();
        FragmentTransaction ft = mFrgmgr.beginTransaction();
        Fragment frg = new HomeFragment();
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.HomeFragment);
        ft.addToBackStack(AppConstt.FRGTAG.HomeFragment);
        ft.commit();
    }

    public void clearMyBackStack() {
        int count = mFrgmgr.getBackStackEntryCount();
        for (int i = 0; i < count; ++i) {
            mFrgmgr.popBackStackImmediate();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isHidden()) {
            iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_my_contact_us));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        }
    }
}
