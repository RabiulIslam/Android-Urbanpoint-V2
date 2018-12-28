package com.urbanpoint.UrbanPoint.CommonFragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;

import java.util.ArrayList;

/**
 * Created by Danish on 3/5/2018.
 */

public class OutletOffersAdapter extends BaseAdapter {
    ArrayList<DModelOutletOffers> categorieslist;
    Context context;

    public OutletOffersAdapter(Context context, ArrayList<DModelOutletOffers> categorieslist) {
        this.context = context;
        this.categorieslist = categorieslist;
    }

    @Override
    public int getCount() {
        return categorieslist.size();
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
        context = parent.getContext();
        ImageView imvCategory, imvLocked, imvSpecial;
        TextView txvOfferName;
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.list_item_outlet_offers, null);

        imvCategory = convertView.findViewById(R.id.imv_offer_item);
        txvOfferName = convertView.findViewById(R.id.lst_item_new_offer_name_offer);
        imvLocked = convertView.findViewById(R.id.frg_outlet_offers_list_imvLocked);
        imvSpecial = convertView.findViewById(R.id.frg_outlet_oofers_imvSpecial);


        txvOfferName.setText(categorieslist.get(position).getOfferName());
        Picasso.get()
                .load(AppConstt.BASE_URL_IMAGES + categorieslist.get(position).getImage())
                .into(imvCategory);

        if (AppConfig.getInstance().mUser.isSubscribed()||
                AppConfig.getInstance().mUser.getWallet()>=categorieslist.get(position).getPrice()) {

            imvLocked.setVisibility(View.GONE);
            if (categorieslist.get(position).getSpecial() == "1") {
                imvSpecial.setVisibility(View.VISIBLE);
            } else {
                imvSpecial.setVisibility(View.GONE);
            }
        } else {
            if (AppConfig.getInstance().isArabic) {
                imvLocked.setImageResource(R.mipmap.lock_arabic_3x);
            } else {
                imvLocked.setImageResource(R.mipmap.lock_badge_3x);
            }
            imvLocked.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}
