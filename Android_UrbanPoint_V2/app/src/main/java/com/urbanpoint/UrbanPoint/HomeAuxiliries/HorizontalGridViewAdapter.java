package com.urbanpoint.UrbanPoint.HomeAuxiliries;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.urbanpoint.UrbanPoint.CommonFragments.WebServices.OfferDetail_Webhit_Get_getOfferDetail;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;

import java.util.List;

public class HorizontalGridViewAdapter extends BaseAdapter {
    List<DModelHomeGrdVw> listOffers;
    Context mContext;
    LayoutInflater inflater;


    public HorizontalGridViewAdapter(Context _mContext, List<DModelHomeGrdVw> listOffers) {
        this.mContext = _mContext;
        this.listOffers = listOffers;
        inflater = LayoutInflater.from(_mContext);

    }

    @Override
    public int getCount() {
        return listOffers.size();
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
        // Log.i(TAG, "getView: " + selectedposition);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.horizontal_grid_view_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtTimmer = convertView.findViewById(R.id.grd_vw_txv_timmer);
            viewHolder.txvOffer = convertView.findViewById(R.id.grd_vw_txv_offer);
            viewHolder.imvOffer = convertView.findViewById(R.id.grd_vw_imv_bg);
            viewHolder.imvOverlay = convertView.findViewById(R.id.grd_vw_imv_overlay);
            viewHolder.rlTimmerContnr = convertView.findViewById(R.id.grd_vw_rl_timmer);
            viewHolder.imvFestival = convertView.findViewById(R.id.grd_vw_imv_festival);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (listOffers.get(position).getStrProductId().length() < 0) {
            viewHolder.txtTimmer.setText(listOffers.get(position).getStrProductId());
        } else {
            viewHolder.rlTimmerContnr.setVisibility(View.GONE);
            viewHolder.imvOverlay.setVisibility(View.GONE);
        }

        if (listOffers.get(position).getStrFestival().equalsIgnoreCase(mContext.getResources().getString(R.string.festival_ramadan))) {

            viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.ramadan));

        } else if (listOffers.get(position).getStrFestival().equalsIgnoreCase(mContext.getResources().getString(R.string.festival_burger))) {
            viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.burger_icon));

        } else if (listOffers.get(position).getStrFestival().equalsIgnoreCase(mContext.getResources().getString(R.string.festival_biryani))) {
            viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.biryani_icon));
        } else {
            viewHolder.imvFestival.setVisibility(View.GONE);
        }

        if (listOffers.get(position).getStrImgUrl().length() > 0) {
            Picasso.get()
                    .load(AppConstt.BASE_URL_IMAGES + listOffers.get(position).getStrImgUrl())
                    .into(viewHolder.imvOffer);

        }
        viewHolder.txvOffer.setText(listOffers.get(position).getStrOfferName() + " at " +
                listOffers.get(position).getStrMerchantName());


        return convertView;
    }

    static class ViewHolder {
        TextView txtTimmer, txvOffer;
        ImageView imvOffer, imvOverlay, imvFestival;
        RelativeLayout rlTimmerContnr;
    }

}
