package com.urbanpoint.UrbanPoint.SubscriptionAuxiliries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;

import java.util.List;

/**
 * Created by Lenovo on 22/10/2018.
 */

public class SubscribeTextAdapter2 extends BaseAdapter {
    Context context;
    List<String> listData;
    LayoutInflater inflter;
    boolean showCircle;

    public SubscribeTextAdapter2(Context _context, List<String> _listData, boolean _showCircle) {
        this.context = _context;
        this.listData = _listData;
        showCircle = _showCircle;
        inflter = (LayoutInflater.from(_context));
    }

    @Override
    public int getCount() {
        return listData.size();
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
        final SubscribeTextAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflter.inflate(R.layout.adpter_subscribe_text_list_items_2, null);
            viewHolder = new SubscribeTextAdapter.ViewHolder();
            viewHolder.txtName = convertView.findViewById(R.id.adptr_subscribe_text_txv);
            viewHolder.imvCircle = convertView.findViewById(R.id.adptr_subscribe_text_imv);
//            viewHolder.rlSpace = convertView.findViewById(R.id.adptr_subscribe_rl_space);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SubscribeTextAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.txtName.setText(listData.get(position));
        if (showCircle) {
            viewHolder.imvCircle.setVisibility(View.VISIBLE);
            viewHolder.txtName.setTextColor(context.getResources().getColor(R.color.main_color));
        } else {
            viewHolder.imvCircle.setVisibility(View.GONE);
            viewHolder.txtName.setTextColor(context.getResources().getColor(R.color.black));
//            if (AppConfig.getInstance().isSpaceRequiredToShow) {
//                viewHolder.rlSpace.setVisibility(View.VISIBLE);
//            } else {
//                viewHolder.rlSpace.setVisibility(View.GONE);
//            }
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView txtName;
        ImageView imvCircle;
//        RelativeLayout rlSpace;
    }
}