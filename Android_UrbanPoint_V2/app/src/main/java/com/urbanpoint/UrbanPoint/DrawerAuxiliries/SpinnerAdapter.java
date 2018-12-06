package com.urbanpoint.UrbanPoint.DrawerAuxiliries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.R;

import java.util.List;

/**
 * Created by Ibrar on 01/04/2017.
 */
public class SpinnerAdapter extends BaseAdapter {
    Context context;

    List<String> listData;
    LayoutInflater inflter;

    public SpinnerAdapter(Context context, List<String> listData) {
        this.context = context;
        this.listData = listData;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflter.inflate(R.layout.fragment_profile_spinner_item_list, null);
            viewHolder = new ViewHolder();
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.frg_profile_spin_itm_txt_item);
//            viewHolder.txtName.setBackgroundColor(context.getColor(R.color.lst_item_bg));
            viewHolder.txtName.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.spinner_bg));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtName.setText(listData.get(position));
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflter.inflate(R.layout.fragment_profile_spinner_item_list, null);

            viewHolder = new ViewHolder();
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.frg_profile_spin_itm_txt_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.txtName.setText(listData.get(position));



        return convertView;
    }

    public static class ViewHolder {
        TextView txtName;
    }
}