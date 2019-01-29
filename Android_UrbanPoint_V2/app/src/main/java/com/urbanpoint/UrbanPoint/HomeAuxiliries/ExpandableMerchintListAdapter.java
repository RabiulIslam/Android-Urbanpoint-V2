package com.urbanpoint.UrbanPoint.HomeAuxiliries;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;

import java.util.ArrayList;

/**
 * Created by Ibrar on 1/26/2017.
 */

public class ExpandableMerchintListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    ArrayList<DModelMerchintList> lstOutletOffers = new ArrayList<>();
    private String festivalRamdan;
    private String festivalBurger;
    private String festivalBiryani;
    private boolean isSubscribed;
    private CategoryFragment categoryFragment;

    public ExpandableMerchintListAdapter(Context context, Fragment _frg, ArrayList<DModelMerchintList> _lstOutletOffers) {
        this.mContext = context;
        this.lstOutletOffers = _lstOutletOffers;
        this.isSubscribed = AppConfig.getInstance().mUser.isSubscribed();
        this.festivalRamdan = mContext.getResources().getString(R.string.festival_ramadan);
        this.festivalBurger = mContext.getResources().getString(R.string.festival_burger);
        this.festivalBiryani = mContext.getResources().getString(R.string.festival_biryani);
        this.categoryFragment = (CategoryFragment) _frg;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.lstOutletOffers.get(groupPosition).getChild();//npt array so pos not required
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolderChild viewHolderChild = null;
        if (convertView == null) {
            viewHolderChild = new ViewHolderChild();
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adptr_expendible_merchintlst_child, null);
            viewHolderChild.imvOffer = convertView.findViewById(R.id.adptr_expendible_merchintlst_offerImage);
            viewHolderChild.imvLockOffer = convertView.findViewById(R.id.adptr_expendible_merchintlst_expfoodShowLockOffersIcon);
            viewHolderChild.imvFestival = convertView.findViewById(R.id.adptr_expendible_merchintlst_festivalImage);
            viewHolderChild.txvName = convertView.findViewById(R.id.adptr_expendible_merchintlst_name);
            viewHolderChild.txvMerchantName = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_merchant_name);
            viewHolderChild.txvMerchantAddr = convertView.findViewById(R.id.adptr_expendible_merchintlst_distanceAndMerchantAddress);
            convertView.setTag(viewHolderChild);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }


        Log.e("img_urlll",lstOutletOffers.get(groupPosition).getChild().get(childPosition).getImgUrl()+" offer image");
        if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getImgUrl() != null) {
            Picasso.get()
                    .load(AppConstt.BASE_URL_IMAGES + lstOutletOffers.get(groupPosition).getChild().get(childPosition).getImgUrl())
                    .into(viewHolderChild.imvOffer);
        }
        if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getStatus().equalsIgnoreCase("1")) {
            viewHolderChild.imvLockOffer.setVisibility(View.GONE);
        } else {
            viewHolderChild.imvLockOffer.setVisibility(View.VISIBLE);
        }

        if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getSpecial().equalsIgnoreCase("1")) {
            viewHolderChild.imvFestival.setVisibility(View.VISIBLE);
            if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getFestival().length() > 0) {
                if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getFestival().equalsIgnoreCase(festivalRamdan)) {
                    viewHolderChild.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.ramadan));
                } else if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getFestival().equalsIgnoreCase(festivalBiryani)) {
                    viewHolderChild.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.biryani_icon));
                } else if (lstOutletOffers.get(groupPosition).getChild().get(childPosition).getFestival().equalsIgnoreCase(festivalBurger)) {
                    viewHolderChild.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.burger_icon));
                } else {
                    viewHolderChild.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.biryani_icon));
                }
            } else {
                viewHolderChild.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.biryani_icon));
            }

        } else {
            viewHolderChild.imvFestival.setVisibility(View.GONE);
        }

        viewHolderChild.txvName.setText(lstOutletOffers.get(groupPosition).getChild().get(childPosition).getName());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.lstOutletOffers.get(groupPosition).getChild().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.lstOutletOffers.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.lstOutletOffers.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
//        String headerTitle = (String) getGroup(groupPosition);
//        DModelMerchintList dModelMerchintList = lstOutletOffers.get(groupPosition);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adptr_expendible_merchintlst_group, null);

            viewHolder.merchant_name = convertView
                    .findViewById(R.id.adptr_expendible_merchintlst_group_merchant_name);
            viewHolder.merchantAddress = convertView
                    .findViewById(R.id.adptr_expendible_merchintlst_group_merchantAddress);

            viewHolder.merchantDistance = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_merchantDistance);
            viewHolder.llMerchantName = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_expllMerchntName);
            viewHolder.llIndicatorDown = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_ll_down);
            viewHolder.llIndicatorUp = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_ll_up);
            viewHolder.merchantLogoImage = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_merchantLogoImage);
            viewHolder.imvGFestival = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_groupFestivalImage);
            viewHolder.imvLock = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_expfoodShowGroupLockOffersIcon);
            viewHolder.rlMerchintDetail = convertView.findViewById(R.id.adptr_expendible_merchintlst_group_rlMerchintDetail);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (isExpanded) {
            viewHolder.llIndicatorDown.setVisibility(View.GONE);
            viewHolder.llIndicatorUp.setVisibility(View.VISIBLE);
        } else {
            viewHolder.llIndicatorDown.setVisibility(View.VISIBLE);
            viewHolder.llIndicatorUp.setVisibility(View.GONE);
        }

        if (lstOutletOffers.get(groupPosition).getMerchantName() != null) {
            viewHolder.merchant_name.setText("" + lstOutletOffers.get(groupPosition).getMerchantName());
        }
        if (lstOutletOffers.get(groupPosition).getMerchantAddress() != null) {
            viewHolder.merchantAddress.setText("" + lstOutletOffers.get(groupPosition).getMerchantAddress());
        }

        if (lstOutletOffers.get(groupPosition).isDistanceRequired()) {
            int distance = lstOutletOffers.get(groupPosition).getMerchantDistance();
            if (distance > 1000) {
                float distanceInKm = distance / 1000;
                int newDistance = (int) distanceInKm;
                viewHolder.merchantDistance.setText(newDistance + " km");
            } else {
                viewHolder.merchantDistance.setText(distance + " m");
            }
        } else {
            viewHolder.merchantDistance.setText("");
        }

        if (isSubscribed ||  AppConfig.getInstance().mUser.getWallet()>0)
        {
            viewHolder.imvLock.setVisibility(View.GONE);
        }
        else {

            viewHolder.imvLock.setVisibility(View.VISIBLE);
        }

        if (lstOutletOffers.get(groupPosition).getStrSpecial().equalsIgnoreCase("1")) {
            viewHolder.imvGFestival.setVisibility(View.VISIBLE);
            if (lstOutletOffers.get(groupPosition).getFestival().length() > 0) {
                if (lstOutletOffers.get(groupPosition).getFestival().equalsIgnoreCase(festivalRamdan)) {
                    viewHolder.imvGFestival.setBackground(mContext.getResources().getDrawable(R.drawable.ramadan));

                } else if (lstOutletOffers.get(groupPosition).getFestival().equalsIgnoreCase(festivalBiryani)) {
                    viewHolder.imvGFestival.setBackground(mContext.getResources().getDrawable(R.drawable.biryani_icon));

                } else if (lstOutletOffers.get(groupPosition).getFestival().equalsIgnoreCase(festivalBurger)) {
                    viewHolder.imvGFestival.setBackground(mContext.getResources().getDrawable(R.drawable.burger_icon));
                } else {
                    viewHolder.imvGFestival.setBackground(mContext.getResources().getDrawable(R.drawable.biryani_icon));
                }
            } else {
                viewHolder.imvGFestival.setBackground(mContext.getResources().getDrawable(R.drawable.biryani_icon));
            }
        } else {
            viewHolder.imvGFestival.setVisibility(View.GONE);
        }


        if (lstOutletOffers.get(groupPosition).getMerchantsLogoUrl() != null) {
            Picasso.get()
                    .load(AppConstt.BASE_URL_IMAGES + lstOutletOffers.get(groupPosition).getMerchantsLogoUrl())
                    .into(viewHolder.merchantLogoImage);
        } else {
            viewHolder.merchantLogoImage.setImageResource(R.drawable.rmv_place_holder);

        }

        viewHolder.rlMerchintDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String outletId = lstOutletOffers.get(groupPosition).getId();
                String merchantImage = lstOutletOffers.get(groupPosition).getMerchantsImageUrl();
                String merchantLogo = lstOutletOffers.get(groupPosition).getMerchantsLogoUrl();
                String offerName = lstOutletOffers.get(groupPosition).getMerchantName();
                String merchantName = lstOutletOffers.get(groupPosition).getMerchantAddress();
                String merchantTimmings = lstOutletOffers.get(groupPosition).getMerchantTimmings();
                String merchantDescription = lstOutletOffers.get(groupPosition).getMerchantDescription();
                String merchantPhone = lstOutletOffers.get(groupPosition).getMerchantsPhone();

                Bundle b = new Bundle();
                b.putInt(AppConstt.BundleStrings.outletId, Integer.parseInt(outletId));
                b.putString(AppConstt.BundleStrings.merchantImage, merchantImage);
                b.putString(AppConstt.BundleStrings.merchantLogo, merchantLogo);
                b.putString(AppConstt.BundleStrings.offerName, offerName);
                b.putString(AppConstt.BundleStrings.merchantName, merchantName);
                b.putString(AppConstt.BundleStrings.merchantTimmings, merchantTimmings);
                b.putString(AppConstt.BundleStrings.merchantDescription, merchantDescription);
                b.putString(AppConstt.BundleStrings.merchantPhone, merchantPhone);
                categoryFragment.navToMerchantDetailFragment(b);
            }
        });
        viewHolder.merchantLogoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String outletId = lstOutletOffers.get(groupPosition).getId();
                String merchantImage = lstOutletOffers.get(groupPosition).getMerchantsImageUrl();
                String merchantLogo = lstOutletOffers.get(groupPosition).getMerchantsLogoUrl();
                String offerName = lstOutletOffers.get(groupPosition).getMerchantName();
                String merchantName = lstOutletOffers.get(groupPosition).getMerchantAddress();
                String merchantTimmings = lstOutletOffers.get(groupPosition).getMerchantTimmings();
                String merchantDescription = lstOutletOffers.get(groupPosition).getMerchantDescription();
                String merchantPhone = lstOutletOffers.get(groupPosition).getMerchantsPhone();

                Bundle b = new Bundle();
                b.putInt(AppConstt.BundleStrings.outletId, Integer.parseInt(outletId));
                b.putString(AppConstt.BundleStrings.merchantImage, merchantImage);
                b.putString(AppConstt.BundleStrings.merchantLogo, merchantLogo);
                b.putString(AppConstt.BundleStrings.offerName, offerName);
                b.putString(AppConstt.BundleStrings.merchantName, merchantName);
                b.putString(AppConstt.BundleStrings.merchantTimmings, merchantTimmings);
                b.putString(AppConstt.BundleStrings.merchantDescription, merchantDescription);
                b.putString(AppConstt.BundleStrings.merchantPhone, merchantPhone);
                categoryFragment.navToMerchantDetailFragment(b);
            }
        });
        viewHolder.llMerchantName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String outletId = lstOutletOffers.get(groupPosition).getId();
                String merchantImage = lstOutletOffers.get(groupPosition).getMerchantsImageUrl();
                String merchantLogo = lstOutletOffers.get(groupPosition).getMerchantsLogoUrl();
                String offerName = lstOutletOffers.get(groupPosition).getMerchantName();
                String merchantName = lstOutletOffers.get(groupPosition).getMerchantAddress();
                String merchantTimmings = lstOutletOffers.get(groupPosition).getMerchantTimmings();
                String merchantDescription = lstOutletOffers.get(groupPosition).getMerchantDescription();
                String merchantPhone = lstOutletOffers.get(groupPosition).getMerchantsPhone();

                Bundle b = new Bundle();
                b.putInt(AppConstt.BundleStrings.outletId, Integer.parseInt(outletId));
                b.putString(AppConstt.BundleStrings.merchantImage, merchantImage);
                b.putString(AppConstt.BundleStrings.merchantLogo, merchantLogo);
                b.putString(AppConstt.BundleStrings.offerName, offerName);
                b.putString(AppConstt.BundleStrings.merchantName, merchantName);
                b.putString(AppConstt.BundleStrings.merchantTimmings, merchantTimmings);
                b.putString(AppConstt.BundleStrings.merchantDescription, merchantDescription);
                b.putString(AppConstt.BundleStrings.merchantPhone, merchantPhone);
                categoryFragment.navToMerchantDetailFragment(b);
            }
        });

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
        CircularImageView merchantLogoImage;
        ImageView imvGFestival, imvLock;
        TextView merchant_name, merchantAddress, merchantDistance;
        LinearLayout llIndicatorDown, llIndicatorUp;
        RelativeLayout rlMerchintDetail, llMerchantName;
    }

    class ViewHolderChild {
        TextView txvName, txvMerchantName, txvMerchantAddr;
        ImageView imvOffer, imvLockOffer, imvFestival;
    }

    public void updateLockedValues() {
        isSubscribed = AppConfig.getInstance().mUser.isSubscribed();
        Log.d("asdadsadqweq", "onResumeaD: ");
    }
}
