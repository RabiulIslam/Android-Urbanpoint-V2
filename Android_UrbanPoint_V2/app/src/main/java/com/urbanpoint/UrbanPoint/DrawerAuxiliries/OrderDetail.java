package com.urbanpoint.UrbanPoint.DrawerAuxiliries;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.urbanpoint.UrbanPoint.HomeAuxiliries.WebViewFragment;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;

public class OrderDetail  extends Fragment implements View.OnClickListener {
    Toolbar toolbar;
    TextView txvTitle;
    ImageView imvBackIcn;
    RelativeLayout rlBack;
    TextView TermsofSale;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.orderdetail,container,false);
        initialization(view);

        return view;

    }


    private void initialization(View view)
    {
        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle("Order Detail");
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        bindViews(view);
    }

    private void bindViews(View view)
    {
        TermsofSale=(TextView)view.findViewById(R.id.tv_terms);
        TermsofSale.setOnClickListener(this);

    }
//    @Override
//    public void onHiddenChanged(boolean isHidden) {
//        super.onHiddenChanged(isHidden);
//        if (!isHidden) {
//            iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_access_code));
//            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
//            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
//            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_terms:
                Bundle bundle=new Bundle();
                bundle.putString("page", AppConstt.REDEEM_RULES);
                navToTermsofSaleFragment(bundle);
               // finish();
                break;
        }
    }

    private void navToTermsofSaleFragment(Bundle bundle)
    {
        Fragment fr = new WebViewFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fr.setArguments(bundle);
        ft.add(R.id.content_frame, fr, AppConstt.FRGTAG.WebViewFragment);
        ft.addToBackStack(AppConstt.FRGTAG.WebViewFragment);
        ft.hide(this);
        ft.commit();
    }
}
