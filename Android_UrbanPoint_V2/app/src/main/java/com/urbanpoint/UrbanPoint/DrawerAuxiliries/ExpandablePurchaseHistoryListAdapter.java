package com.urbanpoint.UrbanPoint.DrawerAuxiliries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;

import java.util.ArrayList;

/**
 * Created by Ibrar on 1/26/2017.
 */

public class ExpandablePurchaseHistoryListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<DModelPurchaseHistoryList> lstOffers = new ArrayList<>();

    public ExpandablePurchaseHistoryListAdapter(Context context, ArrayList<DModelPurchaseHistoryList> _lstOffers) {
        this.mContext = context;
        this.lstOffers = _lstOffers;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.lstOffers.get(groupPosition).getChild();//npt array so pos not required
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolderChild viewHolderChild = null;
        LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            viewHolderChild = new ViewHolderChild();
            convertView = infalInflater.inflate(R.layout.adptr_expendible_purchase_history_child, null);
            viewHolderChild.imvOffer = convertView.findViewById(R.id.adptr_expendible_purchase_history_child_imv_offer);
            viewHolderChild.txvOfferName = convertView.findViewById(R.id.adptr_expendible_purchase_history_child_txv_offer_name);
            viewHolderChild.txvMerchantName = convertView.findViewById(R.id.adptr_expendible_purchase_history_child_txv_merchant_name);
            viewHolderChild.txvMerchantAddr = convertView.findViewById(R.id.adptr_expendible_purchase_history_child_txv_merchant_address);
            viewHolderChild.txvApproxSavings = convertView.findViewById(R.id.adptr_expendible_purchase_history_child_txv_approx_saving);
            viewHolderChild.txvTotalSavings = convertView.findViewById(R.id.adptr_expendible_purchase_history_child_txv_total_saving);
            viewHolderChild.rlInfoCntnr = convertView.findViewById(R.id.adptr_expendible_purchase_history_child_rl_info_cntnr);
            viewHolderChild.rlTotalSavingCntnr = convertView.findViewById(R.id.adptr_expendible_purchase_history_child_rl_total_savings_cntnr);
            convertView.setTag(viewHolderChild);

        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }

        if (childPosition == getChildrenCount(groupPosition) - 1) {
            viewHolderChild.rlTotalSavingCntnr.setVisibility(View.VISIBLE);
            viewHolderChild.rlInfoCntnr.setVisibility(View.GONE);
            viewHolderChild.txvTotalSavings.setText(lstOffers.get(groupPosition).getTotalSavings());
        }

        if (childPosition < getChildrenCount(groupPosition) - 1) {
            viewHolderChild.rlInfoCntnr.setVisibility(View.VISIBLE);
            viewHolderChild.rlTotalSavingCntnr.setVisibility(View.GONE);
            if (lstOffers.get(groupPosition).getChild().get(childPosition).getImgUrl() != null) {
                Picasso.get()
                        .load(AppConstt.BASE_URL_IMAGES + lstOffers.get(groupPosition).getChild().get(childPosition).getImgUrl())
                        .into(viewHolderChild.imvOffer);
            }

             viewHolderChild.txvOfferName.setText(lstOffers.get(groupPosition).getChild().get(childPosition).getOfferName());
            viewHolderChild.txvMerchantName.setText(lstOffers.get(groupPosition).getChild().get(childPosition).getMerchantName());
            viewHolderChild.txvMerchantAddr.setText(lstOffers.get(groupPosition).getChild().get(childPosition).getMerchantAddr());
            viewHolderChild.txvApproxSavings.setText(lstOffers.get(groupPosition).getChild().get(childPosition).getApproxSavings());
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (this.lstOffers.get(groupPosition).getChild().size());
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.lstOffers.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.lstOffers.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adptr_expendible_purchase_history_group, null);
            viewHolder.date_title = convertView.findViewById(R.id.adptr_expendible_purchase_history_group_txv_date);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (lstOffers.get(groupPosition).getStrDate() != null) {
            viewHolder.date_title.setText("" + lstOffers.get(groupPosition).getStrDate());
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder {
        TextView date_title;
    }

    class ViewHolderChild {
        TextView txvOfferName, txvMerchantName, txvMerchantAddr, txvApproxSavings, txvTotalSavings;
        ImageView imvOffer;
        RelativeLayout rlTotalSavingCntnr, rlInfoCntnr;
    }
}
