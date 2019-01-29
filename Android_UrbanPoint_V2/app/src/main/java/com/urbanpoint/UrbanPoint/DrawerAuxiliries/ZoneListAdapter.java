package com.urbanpoint.UrbanPoint.DrawerAuxiliries;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.R;

import java.util.List;


public class ZoneListAdapter extends BaseAdapter {
    Context mContext;
    List<String> lstNames;
    LayoutInflater inflater;
    ProfileFragment profileFragment;
    int mSelectedPosition;

    public ZoneListAdapter(Context _mContext, int _selectedPosition, List<String> _lstNames) {
        this.mContext = _mContext;
        this.lstNames = _lstNames;
        inflater = LayoutInflater.from(_mContext);
        this.mSelectedPosition = _selectedPosition;

    }

    @Override
    public int getCount() {
        return lstNames.size();
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.profile_listadapter_item_list, null);
            viewHolder = new ViewHolder();
            viewHolder.txvName = (TextView) convertView.findViewById(R.id.frg_profile_adapter_txv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txvName.setText(lstNames.get(position));
        if (mSelectedPosition > -1 &&
                position == mSelectedPosition) {
            viewHolder.txvName.setTextColor(Color.BLACK);
//            viewHolder.txvOfferName.setTextSize(mContext.getResources().getDimension(R.dimen.sp5));
        } else {
            viewHolder.txvName.setTextColor(mContext.getResources().getColor(R.color.gray_profile));
        }
        return convertView;

    }

    public void setPosition(int _position) {
        mSelectedPosition = _position;
    }

    public static class ViewHolder {
        TextView txvName;
    }
}
