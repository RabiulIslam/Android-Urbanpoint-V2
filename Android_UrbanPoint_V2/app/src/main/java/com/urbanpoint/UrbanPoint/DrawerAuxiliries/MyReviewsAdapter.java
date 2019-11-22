package com.urbanpoint.UrbanPoint.DrawerAuxiliries;

import android.content.Context;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;

import java.util.ArrayList;

/**
 * Created by Danish on 3/14/2018.
 */

public class MyReviewsAdapter extends BaseAdapter {
    ArrayList<DModelMyReviewes> listReviewes = new ArrayList<>();
    Context context;

    public MyReviewsAdapter(Context context, ArrayList<DModelMyReviewes> listReviewes) {
        this.context = context;
        this.listReviewes = listReviewes;
    }

    @Override
    public int getCount() {
        return listReviewes.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        TextView txvMerchantName, txvOfferName;
        ImageView imvOffer;
        RelativeLayout rlNotHappy, rlOk, rlLoveIt;


        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.list_item_my_reveiwes, null);

        txvMerchantName =  convertView.findViewById(R.id.list_reviews_txvMerchantName);
        txvOfferName =  convertView.findViewById(R.id.txvOfferName);
        imvOffer =  convertView.findViewById(R.id.imvOffer);
        rlNotHappy =  convertView.findViewById(R.id.lis_item_rl_not_happy);
        rlOk =  convertView.findViewById(R.id.lis_item_rl_ok);
        rlLoveIt =  convertView.findViewById(R.id.lis_item_rl_love_it);

        if (listReviewes.get(position).getMerchantName() != null) {
            txvMerchantName.setText(listReviewes.get(position).getMerchantName());
        }

        if (listReviewes.get(position).getOfferName() != null) {
            txvOfferName.setText(listReviewes.get(position).getOfferName());
        }

        if (listReviewes.get(position).getImage() != null) {
            Picasso.get()
                    .load(AppConstt.BASE_URL_IMAGES + listReviewes.get(position).getImage())
                    .into(imvOffer);
        }

        rlNotHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = listReviewes.get(position).getId();
                String review = "Not Happy";
                navToRequestAddRevweive(id, review, position);

            }
        });

        rlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = listReviewes.get(position).getId();
                String review = "OK";
                navToRequestAddRevweive(id, review,position);
            }
        });

        rlLoveIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = listReviewes.get(position).getId();
                String review = "Love it";
                navToRequestAddRevweive(id, review, position);
            }
        });

        return convertView;
    }

    void navToRequestAddRevweive(String _orderId, String _review, int _position) {
        Log.d("safdsadf", "navToRequestAddRevweive: "+_orderId + "review: "+_review);
        FragmentManager fm = ((MainActivity)context).getSupportFragmentManager();
        ReviewOrdersFragment fragment = (ReviewOrdersFragment) fm.findFragmentByTag(AppConstt.FRGTAG.MyReviewsFragment);
        fragment.requestAddReview(_orderId, _review, _position);
    }
}
