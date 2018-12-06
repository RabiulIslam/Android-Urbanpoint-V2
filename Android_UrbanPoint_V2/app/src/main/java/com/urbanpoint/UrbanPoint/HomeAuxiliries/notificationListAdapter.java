package com.urbanpoint.UrbanPoint.HomeAuxiliries;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragments.NotificationFragment;
import com.urbanpoint.UrbanPoint.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Lenovo on 6/20/2017.
 */

public class notificationListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<DModel_NotificationList> lstNotification;
    LayoutInflater inflater;
    NotificationFragment notificationFragment;
    int mSelectedPosition;


    public notificationListAdapter(Context _mContext, NotificationFragment _notificationFragment, ArrayList<DModel_NotificationList> _lstNotification) {
        this.mContext = _mContext;
        this.notificationFragment = _notificationFragment;
        this.lstNotification = _lstNotification;
        inflater = LayoutInflater.from(_mContext);
        mSelectedPosition = -1;
    }

    @Override
    public int getCount() {
        return lstNotification.size();
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
            convertView = inflater.inflate(R.layout.notification_listadapter_item_list, null);
            viewHolder = new ViewHolder();
            viewHolder.txvDateDay =  convertView.findViewById(R.id.frg_notification_adapter_txv_date_day);
            viewHolder.txvDateDay2 =  convertView.findViewById(R.id.frg_notification_adapter_txv_date_day2);
            viewHolder.txvDateYear =  convertView.findViewById(R.id.frg_notification_adapter_txv_date_year);
            viewHolder.txvMessage =  convertView.findViewById(R.id.frg_notification_adapter_txv_messages);
            viewHolder.txvNew =  convertView.findViewById(R.id.frg_notification_list_view_txv_new);
            viewHolder.rlNew =  convertView.findViewById(R.id.frg_notification_list_view_rl_new);
            viewHolder.imvNextArrow =  convertView.findViewById(R.id.frg_notification_list_view_imv_arrow);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.d("EEE", "getView: " + lstNotification.size());


        String string = getCustomDateString(lstNotification.get(position).getDate());
        String[] parts = string.split(",");
        String part1 = parts[0]; // 004
        String part2 = parts[1];
        String part3 = parts[2];
        viewHolder.txvDateDay.setText(part1);
        viewHolder.txvDateDay2.setText(part2);
         viewHolder.txvDateYear.setText(part3);

        viewHolder.txvMessage.setText(lstNotification.get(position).getMerchantMessage());

        if (lstNotification.get(position).isRead()) {
            viewHolder.rlNew.setVisibility(View.GONE);
            viewHolder.imvNextArrow.setBackground(mContext.getResources().getDrawable(R.mipmap.fwd_icn_gray));
            viewHolder.txvMessage.setTextColor(mContext.getResources().getColor(R.color.gray_light));
            viewHolder.txvDateDay.setTextColor(mContext.getResources().getColor(R.color.gray_light));
            viewHolder.txvDateDay2.setTextColor(mContext.getResources().getColor(R.color.gray_light));
            viewHolder.txvDateYear.setTextColor(mContext.getResources().getColor(R.color.gray_light));
        } else {
            viewHolder.rlNew.setVisibility(View.VISIBLE);
            viewHolder.imvNextArrow.setBackground(mContext.getResources().getDrawable(R.mipmap.fwd_icn_blck));
            viewHolder.txvMessage.setTextColor(Color.BLACK);
            viewHolder.txvDateDay.setTextColor(Color.BLACK);
            viewHolder.txvDateDay2.setTextColor(Color.BLACK);
            viewHolder.txvDateYear.setTextColor(Color.BLACK);
        }
//        if (position == mSelectedPosition &&
//                mSelectedPosition != -1) {
//            viewHolder.rlNew.setVisibility(View.GONE);
//             }

        return convertView;
    }

    public static class ViewHolder {
        TextView txvDateDay, txvDateDay2, txvDateYear, txvMessage, txvNew;
        RelativeLayout rlNew;
        ImageView imvNextArrow;
    }

    public static String getCustomDateString(String strDate) {
        Date date = new Date();
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(strDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SimpleDateFormat tmp = new SimpleDateFormat("d");
        String a = "";
        String str = tmp.format(date);
        str = str.substring(0, 1).toUpperCase() + str.substring(1);

        if (date.getDate() > 10 && date.getDate() < 14)
            a = "th ";
        else {
            if (str.endsWith("1")) a = "st ";
            else if (str.endsWith("2")) a = "nd ";
            else if (str.endsWith("3")) a = "rd ";
            else a = "th ";
        }
        tmp = new SimpleDateFormat("MMMM yyyy");
        str = str + "," + a + "," + tmp.format(date);
        return str;
    }

    public void setSelectedPosition(int _position) {
        mSelectedPosition = _position;
    }
}
