package com.urbanpoint.UrbanPoint.HomeAuxiliries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;

import java.util.List;

/**
 * Created by Lenovo on 6/20/2017.
 */

public class SearchOffersAdapter extends BaseAdapter {
    Context mContext;
    List<DModelHomeGrdVw> lstOffers;
    LayoutInflater inflater;
    private String festivalRamdan;
    private String festivalBurger;
    private String festivalBiryani;
   private boolean isSubscribed;

    public SearchOffersAdapter(Context _mContext, List<DModelHomeGrdVw> _lstFavorites, boolean isSubscribed) {
        this.mContext = _mContext;
        this.lstOffers = _lstFavorites;
        this.isSubscribed = isSubscribed;
        this.festivalRamdan = mContext.getResources().getString(R.string.festival_ramadan);
        this.festivalBurger = mContext.getResources().getString(R.string.festival_burger);
        this.festivalBiryani = mContext.getResources().getString(R.string.festival_biryani);
        inflater = LayoutInflater.from(_mContext);
    }

    @Override
    public int getCount() {
        return lstOffers.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adptr_search_offers, null);
            viewHolder = new ViewHolder();
            viewHolder.imvOffer = convertView.findViewById(R.id.search_offers_imv_offer);
            viewHolder.imvLockOffer = convertView.findViewById(R.id.search_offers_imv_lock);
            viewHolder.imvFestival = convertView.findViewById(R.id.search_offers_imv_festival);
            viewHolder.imvCategory = convertView.findViewById(R.id.search_offers_imv_category);
            viewHolder.txvName = convertView.findViewById(R.id.search_offers_txv_offer_name);
            viewHolder.txvMerchantNameAddress = convertView.findViewById(R.id.search_offers_txv_outlet_name_address);
            viewHolder.txvMerchantAddress = convertView.findViewById(R.id.search_offers_txv_outlet_address);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (lstOffers.get(position).getStrImgUrl().length() > 0) {
            Picasso.get()
                    .load(AppConstt.BASE_URL_IMAGES + lstOffers.get(position).getStrImgUrl())
                    .into(viewHolder.imvOffer);
        }
        if (lstOffers.get(position).getStrCategoryImage().length() > 0) {
            Picasso.get()
                    .load(AppConstt.BASE_URL_IMAGES + lstOffers.get(position).getStrCategoryImage())
                    .into(viewHolder.imvCategory);
        }


        if (isSubscribed) {
            viewHolder.imvLockOffer.setVisibility(View.GONE);
        } else {
            viewHolder.imvLockOffer.setVisibility(View.VISIBLE);
        }

        if (lstOffers.get(position).getStrFestival().length() > 0) {
            viewHolder.imvFestival.setVisibility(View.VISIBLE);
            if (lstOffers.get(position).getStrFestival().equalsIgnoreCase(festivalRamdan)) {
                viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.ramadan));
            } else if (lstOffers.get(position).getStrFestival().equalsIgnoreCase(festivalBiryani)) {
                viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.biryani_icon));
            } else if (lstOffers.get(position).getStrFestival().equalsIgnoreCase(festivalBurger)) {
                viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.burger_icon));
            } else {
                viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.biryani_icon));
            }
        } else {
            viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.biryani_icon));
        }

        viewHolder.txvName.setText(lstOffers.get(position).getStrOfferName());
        viewHolder.txvMerchantNameAddress.setText(lstOffers.get(position).getStrMerchantName());
        viewHolder.txvMerchantAddress.setText(lstOffers.get(position).getStrMerchantAddress());

        return convertView;
    }

    public static class ViewHolder {
        ImageView imvOffer, imvCategory, imvLockOffer, imvFestival;
        TextView txvName, txvMerchantNameAddress, txvMerchantAddress;
    }
}
