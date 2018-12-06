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

public class OffersAdapter extends BaseAdapter {
    Context mContext;
    List<DModelHomeGrdVw> lstOffers;
    LayoutInflater inflater;
    private String festivalRamdan;
    private String festivalBurger;
    private String festivalBiryani;
    private boolean isSubscribed;

    public OffersAdapter(Context _mContext, List<DModelHomeGrdVw> _lstFavorites, boolean isSubscribed) {
        this.mContext = _mContext;
        this.lstOffers = _lstFavorites;
        this.isSubscribed = isSubscribed;
//        this.festivalRamdan = "ramadan";
        this.festivalRamdan = "ramadan";
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
            convertView = inflater.inflate(R.layout.adptr_new_offers, null);
            viewHolder = new ViewHolder();
            viewHolder.imvOffer = convertView.findViewById(R.id.fav_offer_Image);
            viewHolder.imvLockOffer = convertView.findViewById(R.id.foodShowLockOffersIcon);
            viewHolder.imvFestival = convertView.findViewById(R.id.newOffersFestivalIcon);
            viewHolder.txvName = convertView.findViewById(R.id.fav_offr_name);
            viewHolder.txvMerchantName = convertView.findViewById(R.id.fav_merchant_name);
            viewHolder.txvMerchantAddress = convertView.findViewById(R.id.fav_distance_and_merchant_address);
            viewHolder.txvDistanceUnit = convertView.findViewById(R.id.fav_distance_unit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (lstOffers.get(position).getStrImgUrl().length() > 0) {
            Picasso.get()
                    .load(AppConstt.BASE_URL_IMAGES + lstOffers.get(position).getStrImgUrl())
                    .into(viewHolder.imvOffer);
        }

        if (isSubscribed) {
            viewHolder.imvLockOffer.setVisibility(View.GONE);
        } else {
            viewHolder.imvLockOffer.setVisibility(View.VISIBLE);
        }


        if (lstOffers.get(position).getStrSpecial().equalsIgnoreCase("1")) {
            viewHolder.imvFestival.setVisibility(View.VISIBLE);
            if (lstOffers.get(position).getStrFestival().length() > 0) {
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
        } else {
            viewHolder.imvFestival.setVisibility(View.GONE);
        }


        viewHolder.txvName.setText(lstOffers.get(position).getStrOfferName());
        viewHolder.txvMerchantName.setText(lstOffers.get(position).getStrMerchantName());

        if (lstOffers.get(position).isDistanceRequired()) {
            int distance = lstOffers.get(position).getDistance();
            if (distance > 1000) {
                float distanceInKm = distance / 1000;
                int newDistance = (int) distanceInKm;
                viewHolder.txvMerchantAddress.setText(newDistance + "");
                viewHolder.txvDistanceUnit.setText(" " + "km");
            } else {
                viewHolder.txvMerchantAddress.setText(distance + "");
                viewHolder.txvDistanceUnit.setText(" " + "m");
            }
            viewHolder.txvDistanceUnit.setVisibility(View.VISIBLE);
        } else {
            viewHolder.txvMerchantAddress.setText("");
            viewHolder.txvDistanceUnit.setVisibility(View.GONE);
        }


        return convertView;
    }

    public static class ViewHolder {
        ImageView imvOffer, imvLockOffer, imvFestival;
        TextView txvName, txvMerchantName, txvMerchantAddress, txvDistanceUnit;
    }
}
