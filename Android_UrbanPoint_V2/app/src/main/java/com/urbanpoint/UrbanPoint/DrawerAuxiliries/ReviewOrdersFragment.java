package com.urbanpoint.UrbanPoint.DrawerAuxiliries;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.DrawerAuxiliries.WebServices.ReviewOrders_Webhit_Get_addReview;
import com.urbanpoint.UrbanPoint.DrawerAuxiliries.WebServices.ReviewOrders_Webhit_Get_getMyOrdersWithoutReview;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

import java.util.ArrayList;

/**
 * Created by Danish on 3/14/2018.
 */

public class ReviewOrdersFragment extends Fragment implements AbsListView.OnScrollListener {
    TextView txvNoDataMessage;
    GridView gvReveives;
    MyReviewsAdapter myReviewsAdapter;
    ArrayList<DModelMyReviewes> listReveiews;
    CustomAlert customAlert;
    private ProgressDilogue progressDilogue;
    private int page;
    private boolean shouldGetMoreOffers, isAlreadyfetchingOffers;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_review_orders, container, false);
        initiate();
        bindViews(v);
        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_my_reviews));
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        AppConfig.getInstance().shouldNavToReview = false;
        requestGetReviews(page);
//        if(NetworkUtils.isConnected(getActivity())){
//            requestMyReveiwes(AppConfig.getInstance().mUser.token, 1);
//        }else {
//            customAlert.showCustomAlertDialog(getActivity(), null, getString(R.string.no_internet), null, null, false, null);
//
//        }
        return v;

    }

    private void initiate() {
        page = 1;
        shouldGetMoreOffers = true;
        isAlreadyfetchingOffers = false;
        listReveiews = new ArrayList<>();
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
    }


    void bindViews(View frg) {
        gvReveives = frg.findViewById(R.id.gv_reviews_list);
        txvNoDataMessage = frg.findViewById(R.id.txv_you_do_not);
        gvReveives.setOnScrollListener(this);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (shouldGetMoreOffers && !isAlreadyfetchingOffers) {
            if ((gvReveives.getLastVisiblePosition() == (myReviewsAdapter.getCount() - 1))) {
                page++;
                requestGetReviews(page);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private void requestGetReviews(int _page) {
        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        isAlreadyfetchingOffers = true;
        ReviewOrders_Webhit_Get_getMyOrdersWithoutReview review_Orders_webhit_get_getMyOrdersWithoutReview = new ReviewOrders_Webhit_Get_getMyOrdersWithoutReview();
        review_Orders_webhit_get_getMyOrdersWithoutReview.getReviews(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                isAlreadyfetchingOffers = false;
                if (isSuccess) {
                    updateList();
                } else if (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.DATABASE_NOT_FOUND + "")) {
                    shouldGetMoreOffers = false;
                    if (listReveiews.size() == 0) {
                        gvReveives.setVisibility(View.GONE);
                        txvNoDataMessage.setVisibility(View.VISIBLE);
                    }
                } else {
                    gvReveives.setVisibility(View.GONE);
                    txvNoDataMessage.setVisibility(View.VISIBLE);
                    customAlert.showCustomAlertDialog(getActivity(), null, strMsg, null, null, false, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                isAlreadyfetchingOffers = false;
                gvReveives.setVisibility(View.GONE);
                txvNoDataMessage.setVisibility(View.VISIBLE);
                customAlert.showCustomAlertDialog(getActivity(), null, ex.getMessage(), null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();
                gvReveives.setVisibility(View.GONE);
                txvNoDataMessage.setVisibility(View.VISIBLE);
                isAlreadyfetchingOffers = false;

            }
        }, _page);
    }

    public void requestAddReview(String _orderId, String _review, final int _position) {
        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        ReviewOrders_Webhit_Get_addReview reviewOrders_webhit_get_addReview = new ReviewOrders_Webhit_Get_addReview();
        reviewOrders_webhit_get_addReview.addReviews(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    AppConfig.getInstance().mUserBadges.reviewCount--;
                    ((MainActivity) getContext()).setReviewCount(AppConfig.getInstance().mUserBadges.getReviewCount());
                    listReveiews.remove(_position);
                    myReviewsAdapter.notifyDataSetChanged();
                    if (listReveiews.size() == 0) {
                        String nationality = "";
                        if (AppConfig.getInstance().mUser.getmNationality() != null &&
                                AppConfig.getInstance().mUser.getmNationality().length() > 0) {
                            nationality = AppConfig.getInstance().mUser.getmNationality();
                        }
                        if (nationality.length() > 0 &&
                                AppConfig.getInstance().mUser.getEmailVerified().equalsIgnoreCase("1")) {
                            ((MainActivity) getContext()).setMenuBadgeVisibility(false);
                        }
                        gvReveives.setVisibility(View.GONE);
                        txvNoDataMessage.setVisibility(View.VISIBLE);
                    }
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
        }, _orderId, _review);
    }

    private void updateList() {
        if (ReviewOrders_Webhit_Get_getMyOrdersWithoutReview.responseObject != null &&
                ReviewOrders_Webhit_Get_getMyOrdersWithoutReview.responseObject.getData() != null &&
                ReviewOrders_Webhit_Get_getMyOrdersWithoutReview.responseObject.getData().size() > 0) {
            if (ReviewOrders_Webhit_Get_getMyOrdersWithoutReview.responseObject.getData().size() < 20) {
                shouldGetMoreOffers = false;
            }

            for (int i = 0; i < ReviewOrders_Webhit_Get_getMyOrdersWithoutReview.responseObject.getData().size(); i++) {
                String strImageUrl = "";
                if (ReviewOrders_Webhit_Get_getMyOrdersWithoutReview.responseObject.getData().get(i).getImage() != null) {
                    strImageUrl = ReviewOrders_Webhit_Get_getMyOrdersWithoutReview.responseObject.getData().get(i).getImage();
                }


                listReveiews.add(new DModelMyReviewes(
                        ReviewOrders_Webhit_Get_getMyOrdersWithoutReview.responseObject.getData().get(i).getOrderId(),
                        ReviewOrders_Webhit_Get_getMyOrdersWithoutReview.responseObject.getData().get(i).getOutletName(),
                        ReviewOrders_Webhit_Get_getMyOrdersWithoutReview.responseObject.getData().get(i).getTitle(),
                        strImageUrl,
                        ""));
            }
            gvReveives.setVisibility(View.VISIBLE);
            txvNoDataMessage.setVisibility(View.GONE);

            if (myReviewsAdapter != null) {
                myReviewsAdapter.notifyDataSetChanged();
            } else {
                myReviewsAdapter = new MyReviewsAdapter(getContext(), listReveiews);
                gvReveives.setAdapter(myReviewsAdapter);

            }
        } else {
            gvReveives.setVisibility(View.GONE);
            txvNoDataMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isHidden()) {
            iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_my_reviews));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        }
    }
}


