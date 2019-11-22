package com.urbanpoint.UrbanPoint.DrawerAuxiliries;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.DrawerAuxiliries.WebServices.PurchaseHistory_Webhit_Get_getMyPurchaseHistory;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseHistoryFragment extends Fragment {

    private LayoutInflater mInflater;
    private TextView txvNotFound, txvTotalSavings;
    private ImageView imvDivider;
    private ExpandableListView expLsvPurchseHistory;
    private ArrayList<DModelPurchaseHistoryList> lstPurchaseHistory;
    private ArrayList<DModelPurchaseHistoryList.Child> lstChild;
    private ExpandablePurchaseHistoryListAdapter expandablePurchaseHistoryListAdapter;
    private ProgressDilogue progressDilogue;
    private CustomAlert customAlert;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_history, null);
        mInflater = inflater;
        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_purchaase_history));
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        initiate();
        bindViews(view);

        progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
        requestGetPurchaseHistory();
        return view;
    }

    private void initiate() {
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        lstPurchaseHistory = new ArrayList<>();
        lstChild = new ArrayList<>();
    }


    private void bindViews(View frg) {
        txvNotFound = frg.findViewById(R.id.frg_purchase_history_txv_ntfound);
        txvTotalSavings = frg.findViewById(R.id.frg_purchase_history_txv_total_saving);
        imvDivider = frg.findViewById(R.id.frg_purchase_history_imv_line);
        expLsvPurchseHistory = frg.findViewById(R.id.frg_purchase_history_explsv);
    }

    private void expandAllChilds(ExpandablePurchaseHistoryListAdapter _viewAdapted) {
        int count = _viewAdapted.getGroupCount();
        for (int position = 1; position <= count; position++)
            expLsvPurchseHistory.expandGroup(position - 1);
        _viewAdapted.notifyDataSetChanged();
    }

    private void requestGetPurchaseHistory() {
        PurchaseHistory_Webhit_Get_getMyPurchaseHistory purchaseHistory_webhit_get_getMyPurchaseHistory = new PurchaseHistory_Webhit_Get_getMyPurchaseHistory();
        purchaseHistory_webhit_get_getMyPurchaseHistory.getPurchaseHistory(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                if (isSuccess) {
                    updateList();
                } else {
                    expLsvPurchseHistory.setVisibility(View.GONE);
                    txvTotalSavings.setVisibility(View.GONE);
                    imvDivider.setVisibility(View.GONE);
                    txvNotFound.setVisibility(View.VISIBLE);
                    customAlert.showCustomAlertDialog(getActivity(), null, strMsg, null, null, false, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                expLsvPurchseHistory.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
                txvTotalSavings.setVisibility(View.GONE);
                imvDivider.setVisibility(View.GONE);
                customAlert.showCustomAlertDialog(getActivity(), null, ex.getMessage(), null, null, false, null);
            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();
                expLsvPurchseHistory.setVisibility(View.GONE);
                txvTotalSavings.setVisibility(View.GONE);
                imvDivider.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);

            }
        });

    }

    private void updateList() {
        if (PurchaseHistory_Webhit_Get_getMyPurchaseHistory.responseObject != null &&
                PurchaseHistory_Webhit_Get_getMyPurchaseHistory.responseObject.getData().getAllorders() != null &&
                PurchaseHistory_Webhit_Get_getMyPurchaseHistory.responseObject.getData().getAllorders().size() > 0) {

            for (int i = 0; i < PurchaseHistory_Webhit_Get_getMyPurchaseHistory.responseObject.getData().getAllorders().size(); i++) {
                if (PurchaseHistory_Webhit_Get_getMyPurchaseHistory.responseObject.getData().getAllorders().get(i).getOrders() != null &&
                        PurchaseHistory_Webhit_Get_getMyPurchaseHistory.responseObject.getData().getAllorders().get(i).getOrders().size() > 0) {
                    lstChild = new ArrayList<>();
                    String totalSaving = PurchaseHistory_Webhit_Get_getMyPurchaseHistory.responseObject.getData().getAllorders().get(i).getMonthlySaving();
                    float totalSavingsFloat = Float.parseFloat(totalSaving);
                    int totalSavingsRounded = (int) totalSavingsFloat;
                    String totalSavings = getString(R.string.frg_purchase_history_total_savings) + " "
                            + getResources().getString(R.string.txv_qatar_riyal) + " "
                            + totalSavingsRounded;


                    for (int j = 0; j < PurchaseHistory_Webhit_Get_getMyPurchaseHistory.responseObject.getData().getAllorders().get(i).getOrders().size(); j++) {

                        String strImageUrl = "";
                        if (PurchaseHistory_Webhit_Get_getMyPurchaseHistory.responseObject.getData().getAllorders().get(i).getOrders().get(j).getImage() != null) {
                            strImageUrl = PurchaseHistory_Webhit_Get_getMyPurchaseHistory.responseObject.getData().getAllorders().get(i).getOrders().get(j).getImage();
                        }
                        String aproxSavings;
                        if (PurchaseHistory_Webhit_Get_getMyPurchaseHistory.responseObject.getData().getAllorders().get(i).getOrders().get(j).getApproxSaving() != null) {
                            aproxSavings = String.valueOf(PurchaseHistory_Webhit_Get_getMyPurchaseHistory.responseObject.getData().getAllorders().get(i).getOrders().get(j).getApproxSaving());
                        } else {
                            aproxSavings = "00.00";
                        }
                        int approxSavingsRounded = 0;
                        Log.d("FLOASTIS", "updateList: " + PurchaseHistory_Webhit_Get_getMyPurchaseHistory.responseObject.getData().getAllorders().get(i).getOrders().get(j).getId());
                        Log.d("FLOASTIS", "updateList: " + aproxSavings);
                            float approxSavingsFloat = Float.parseFloat(aproxSavings);
                            approxSavingsRounded = (int) approxSavingsFloat;



                        lstChild.add(new DModelPurchaseHistoryList.Child(
                                strImageUrl,
                                PurchaseHistory_Webhit_Get_getMyPurchaseHistory.responseObject.getData().getAllorders().get(i).getOrders().get(j).getTitle(),
                                PurchaseHistory_Webhit_Get_getMyPurchaseHistory.responseObject.getData().getAllorders().get(i).getOrders().get(j).getOutletName(),
                                PurchaseHistory_Webhit_Get_getMyPurchaseHistory.responseObject.getData().getAllorders().get(i).getOrders().get(j).getOutletAddress(),
                                getResources().getString(R.string.txv_qatar_riyal) + " " + approxSavingsRounded));

                    }
                    lstChild.add(new DModelPurchaseHistoryList.Child("", "", "", "", ""));

                    lstPurchaseHistory.add(new DModelPurchaseHistoryList(
                            getCustomDateString(PurchaseHistory_Webhit_Get_getMyPurchaseHistory.responseObject.getData().getAllorders().get(i).getDate()),
                            lstChild,
                            totalSavings
                    ));

                }
            }
            txvTotalSavings.setVisibility(View.VISIBLE);
            expLsvPurchseHistory.setVisibility(View.VISIBLE);
            txvNotFound.setVisibility(View.GONE);

            expandablePurchaseHistoryListAdapter = new ExpandablePurchaseHistoryListAdapter(getContext(), lstPurchaseHistory);
            expLsvPurchseHistory.setAdapter(expandablePurchaseHistoryListAdapter);
            expandAllChilds(expandablePurchaseHistoryListAdapter);
        } else {
            txvTotalSavings.setVisibility(View.GONE);
            imvDivider.setVisibility(View.GONE);
            expLsvPurchseHistory.setVisibility(View.GONE);
            txvNotFound.setVisibility(View.VISIBLE);

        }
    }

    public static String getCustomDateString(String strDate) {
        Date date = new Date();
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM");
            date = format.parse(strDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SimpleDateFormat tmp = new SimpleDateFormat("MMMM yyyy");
        String str = tmp.format(date);
        return str;
    }


    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_purchaase_history));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        }

    }

}
