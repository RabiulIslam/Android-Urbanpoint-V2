package com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragments.WebServices.Notification_Webhit_POST_readNotification;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationDetailFragment extends Fragment implements View.OnClickListener {
    private ImageView imvCross;
    private Button btnHome;
    private ImageView mBackButton;
    private FragmentManager mFragMgr;
    private TextView txvDay, txvDayUnit, txvYear, txv1, txv2, txv3;
    private Bundle mBundle;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_detail, null);
        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_notification_detail));
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        initialize();
        bindViews(view);
        return view;
    }


    private void initialize() {
        mBundle = getArguments();
        mFragMgr = getActivity().getSupportFragmentManager();
    }

    private void bindViews(View frg) {
        txvDay = frg.findViewById(R.id.frg_notification_detail_txv_day);
        txvDayUnit = frg.findViewById(R.id.frg_notification_detail_txv_day_unit);
        txvYear = frg.findViewById(R.id.frg_notification_detail_txv_year);
        txv1 = frg.findViewById(R.id.frg_notification_detail_txv_1);
        txv2 = frg.findViewById(R.id.frg_notification_detail_txv_2);
        txv3 = frg.findViewById(R.id.frg_notification_detail_txv_3);
        imvCross = frg.findViewById(R.id.frg_notification_detail_imv_close);
        btnHome = frg.findViewById(R.id.frg_notification_detail_btn_home);


        if (mBundle != null) {
            String id = mBundle.getString(AppConstt.Notifications.PUSH_NTIFCN_ID);
            String string = getCustomDateString(mBundle.getString(AppConstt.Notifications.PUSH_NTIFCN_DATE));
            String strMessage = mBundle.getString(AppConstt.Notifications.PUSH_NTIFCN_MSG);
            String strTitle = mBundle.getString(AppConstt.Notifications.PUSH_NTIFCN_TITLE);
            boolean shouldReadNotification = mBundle.getBoolean(AppConstt.Notifications.PUSH_NTIFCN_READ);
            if (shouldReadNotification) {
                requestReadNotificaion(id);
            }

            String[] parts = string.split(",");
            String part1 = parts[0]; // 004
            String part2 = parts[1];
            String part3 = parts[2];
            txvDay.setText(part1);
            txvDayUnit.setText(part2);
            txvYear.setText(part3);
            txv2.setVisibility(View.VISIBLE);
            txv2.setText(strMessage);
            txv1.setVisibility(View.VISIBLE);
            txv1.setText(strTitle);

        }
        imvCross.setOnClickListener(this);
        btnHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.frg_notification_detail_btn_home:
                ((MainActivity) getActivity()).navToHomeFragment();
                break;
            case R.id.frg_notification_detail_imv_close:
                mFragMgr.popBackStack();
                break;
        }
    }

    public static String getCustomDateString(String strDate) {
        Date date = new Date();
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(strDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SimpleDateFormat tmp = new SimpleDateFormat("d");
        String a = "";
        String str = tmp.format(date);
        str = str.substring(0, 1).toUpperCase() + str.substring(1);

        if (date.getDate() > 10 && date.getDate() < 14)
            a = "th ";
        else {
            if (str.endsWith("1")) a = "st ";
            else if (str.endsWith("2")) a = "nd ";
            else if (str.endsWith("3")) a = "rd ";
            else a = "th ";
        }
        tmp = new SimpleDateFormat("MMMM yyyy");
        str = str + "," + a + "," + tmp.format(date);
        return str;
    }

    private void requestReadNotificaion(String _notificationId) {
        Notification_Webhit_POST_readNotification notification_webhit_post_readNotification = new Notification_Webhit_POST_readNotification();
        notification_webhit_post_readNotification.readNotification(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                if (isSuccess) {
                    AppConfig.getInstance().mUserBadges.setNotificationCount(AppConfig.getInstance().mUserBadges.getNotificationCount() - 1);
                }
            }

            @Override
            public void onWebException(Exception ex) {
            }

            @Override
            public void onWebLogout() {
            }
        }, _notificationId);
    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_notification_detail));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }
    }
}
