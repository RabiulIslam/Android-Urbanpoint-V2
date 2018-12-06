package com.urbanpoint.UrbanPoint.HomeAuxiliries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.DModelGetCategories;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;

import java.util.ArrayList;

/**
 * Created by Danish on 2/13/2018.
 */

public class HomeCategoriesAdapter extends BaseAdapter {
    ArrayList<DModelGetCategories> lstCategory;
    Context context;

    public HomeCategoriesAdapter(Context context, ArrayList<DModelGetCategories> lstCategory) {
        this.context = context;
        this.lstCategory = lstCategory;
    }

    @Override
    public int getCount() {
        return lstCategory.size();
    }

    @Override
    public Object getItem(int position) {
        return lstCategory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        ImageView imvCategory;
        TextView txvOfferName;
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.adptr_home_categories, null);

        imvCategory = convertView.findViewById(R.id.adptr_home_categories_imv_category);
        txvOfferName = convertView.findViewById(R.id.adptr_home_category_txv_name);

        txvOfferName.setText(lstCategory.get(position).getName());
        if (lstCategory.get(position).getImage() != null &&
                lstCategory.get(position).getImage().length() > 0) {
            Picasso.get()
                    .load(AppConstt.BASE_URL_IMAGES + lstCategory.get(position).getImage())
                    .placeholder(R.drawable.icn_category)
                    .into(imvCategory);
        } else {

        }
        return convertView;
    }
}
