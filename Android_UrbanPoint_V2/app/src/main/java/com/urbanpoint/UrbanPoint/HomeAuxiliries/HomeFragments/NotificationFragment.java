package com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragments;


import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.HomeAuxiliries.DModel_NotificationList;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragments.WebServices.Notification_Webhit_Get_getMyNotifications;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragments.WebServices.Notification_Webhit_POST_readNotification;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.notificationListAdapter;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    private ArrayList<DModel_NotificationList> lstNotification;
    private ListView lsvNotification;
    private com.urbanpoint.UrbanPoint.HomeAuxiliries.notificationListAdapter notificationListAdapter;
    private Bundle mBundle;
    private TextView txvNotFound;
    private FragmentManager frgMngr;
    private View lsvFooterView;
    private int mSelctedPosition;
    private String date2;
    private int page;
    private boolean shouldGetMoreOffers, isAlreadyfetchingOffers, isViewNull;
    private CustomAlert customAlert;
    private ProgressDilogue progressDilogue;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, null);
        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_notification));
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        initialize();
        bindViews(view);
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
            c.putBoolean(AppConstt.Notifications.PUSH_NTIFCN_READ, true);
            mSelctedPosition = 0;
            navToNotificationDetailFragment(c);
        } else {
            requestUnreadNotification(page, true);
        }
        lsvNotification.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (shouldGetMoreOffers && !isAlreadyfetchingOffers) {
                    if ((lsvNotification.getLastVisiblePosition() == (notificationListAdapter.getCount() - 1))) {
                        page++;
                        lsvNotification.addFooterView(lsvFooterView);
                        lsvNotification.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
                        notificationListAdapter.notifyDataSetChanged();
                        requestUnreadNotification(page, false);
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        lsvNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelctedPosition = position;
                Bundle b = new Bundle();
                b.putString(AppConstt.Notifications.PUSH_NTIFCN_ID, lstNotification.get(position).getNotificationId());
                b.putString(AppConstt.Notifications.PUSH_NTIFCN_DATE, lstNotification.get(position).getDate());
                b.putString(AppConstt.Notifications.PUSH_NTIFCN_TITLE, lstNotification.get(position).getMerchantName());
                b.putString(AppConstt.Notifications.PUSH_NTIFCN_MSG, lstNotification.get(position).getMerchantMessage());
                b.putBoolean(AppConstt.Notifications.PUSH_NTIFCN_READ, false);

                if (lstNotification.get(position).isRead()) {
                    navToNotificationDetailFragment(b);
                } else {
                    requestReadNotificaion(lstNotification.get(position).getNotificationId(), b, true);
                }
            }
        });

        return view;
    }

    private void initialize() {
        frgMngr = getFragmentManager();
        mSelctedPosition = -1;
        page = 1;
        shouldGetMoreOffers = true;
        isAlreadyfetchingOffers = false;
        isViewNull = false;
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        mBundle = new Bundle();
        lstNotification = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("sadsdewqe", "onResume: ");
    }

    private void bindViews(View frg) {
        txvNotFound = frg.findViewById(R.id.frg_notification_txv_no_notification);
        lsvNotification = frg.findViewById(R.id.frg_notification_list_view);
        lsvFooterView = ((LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.lsv_footer, null, false);

    }


    private void navToNotificationDetailFragment(Bundle _b) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new NotificationDetailFragment();
        frg.setArguments(_b);
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.NotificationDetailFragment);
        ft.addToBackStack(AppConstt.FRGTAG.NotificationDetailFragment);
        ft.hide(this);
        ft.commit();
    }

    private void requestUnreadNotification(int _page, boolean _shouldClearLst) {
        if (_shouldClearLst) {
            progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
            lstNotification.clear();
            notificationListAdapter = null;
        }
        isAlreadyfetchingOffers = true;
        Notification_Webhit_Get_getMyNotifications notificationWebhitGetgetMyNotifications = new Notification_Webhit_Get_getMyNotifications();
        notificationWebhitGetgetMyNotifications.getNotifications(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                isAlreadyfetchingOffers = false;
                if (isSuccess) {
                    updateList();
                } else if (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.DATABASE_NOT_FOUND + "")) {
                    shouldGetMoreOffers = false;
                    if (lstNotification.size() == 0) {
                        lsvNotification.setVisibility(View.GONE);
                        txvNotFound.setVisibility(View.VISIBLE);
                    }else {
                        lsvNotification.removeFooterView(lsvFooterView);
                        notificationListAdapter.notifyDataSetChanged();
                    }
                } else {
                    lsvNotification.setVisibility(View.GONE);
                    txvNotFound.setVisibility(View.VISIBLE);
                   if (customAlert !=null)
                    customAlert.showCustomAlertDialog(getActivity(), null, strMsg, null, null, false, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                isAlreadyfetchingOffers = false;
                lsvNotification.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
                if (customAlert !=null)
                    customAlert.showCustomAlertDialog(getActivity(), null, ex.getMessage(), null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();
                lsvNotification.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
                isAlreadyfetchingOffers = false;

            }
        }, _page);
    }

    private void requestReadNotificaion(String _notificationId, final Bundle _b, final boolean _shouldNavigate) {
        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        Notification_Webhit_POST_readNotification notification_webhit_post_readNotification = new Notification_Webhit_POST_readNotification();
        notification_webhit_post_readNotification.readNotification(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    lstNotification.set(mSelctedPosition, new DModel_NotificationList(
                            lstNotification.get(mSelctedPosition).getNotificationId(),
                            lstNotification.get(mSelctedPosition).getMerchantName(),
                            lstNotification.get(mSelctedPosition).getMerchantMessage(),
                            lstNotification.get(mSelctedPosition).getDate(),
                            true
                    ));
                    AppConfig.getInstance().mUserBadges.setNotificationCount(AppConfig.getInstance().mUserBadges.getNotificationCount() - 1);
                    notificationListAdapter.notifyDataSetChanged();
                    if (_shouldNavigate) {
                        navToNotificationDetailFragment(_b);
                    }
                } else {
                    if (customAlert !=null)
                        customAlert.showCustomAlertDialog(getActivity(), null, strMsg, null, null, false, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                if (customAlert !=null)
                    customAlert.showCustomAlertDialog(getActivity(), null, ex.getMessage(), null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();

            }
        }, _notificationId);
    }

    private void updateList() {
        if (Notification_Webhit_Get_getMyNotifications.responseObject != null &&
                Notification_Webhit_Get_getMyNotifications.responseObject.getData() != null &&
                Notification_Webhit_Get_getMyNotifications.responseObject.getData().size() > 0) {

            if (Notification_Webhit_Get_getMyNotifications.responseObject.getData().size() < 20) {
                shouldGetMoreOffers = false;
            }

            for (int i = 0; i < Notification_Webhit_Get_getMyNotifications.responseObject.getData().size(); i++) {
                boolean isRead = Notification_Webhit_Get_getMyNotifications.responseObject.getData().get(i).getReaded() == 1 ? true : false;

                lstNotification.add(new DModel_NotificationList(
                        Notification_Webhit_Get_getMyNotifications.responseObject.getData().get(i).getId(),
                        Notification_Webhit_Get_getMyNotifications.responseObject.getData().get(i).getTitle(),
                        Notification_Webhit_Get_getMyNotifications.responseObject.getData().get(i).getMessage(),
                        Notification_Webhit_Get_getMyNotifications.responseObject.getData().get(i).getCreatedAt(),
                        isRead
                ));


            }

            lsvNotification.setVisibility(View.VISIBLE);
            txvNotFound.setVisibility(View.GONE);
            lsvNotification.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);

            if (notificationListAdapter != null) {
                lsvNotification.removeFooterView(lsvFooterView);
                notificationListAdapter.notifyDataSetChanged();
            } else {
                notificationListAdapter = new notificationListAdapter(getContext(), this, lstNotification);
                lsvNotification.setAdapter(notificationListAdapter);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (lstNotification.size() == 0 && !isViewNull) {
                        requestUnreadNotification(page, true);
                    }
                }
            }, 300);

            iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_notification));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isViewNull = true;
    }
}

