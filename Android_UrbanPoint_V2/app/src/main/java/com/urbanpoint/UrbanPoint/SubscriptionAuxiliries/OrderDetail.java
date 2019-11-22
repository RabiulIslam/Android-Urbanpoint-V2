package com.urbanpoint.UrbanPoint.SubscriptionAuxiliries;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.urbanpoint.UrbanPoint.GatewayCallbackActivity;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.WebViewFragment;
import com.urbanpoint.UrbanPoint.MainActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OrderDetail extends Fragment implements View.OnClickListener {
    Toolbar toolbar;
    TextView txvTitle;
    ImageView imvBackIcn;
    RelativeLayout rlBack;
    TextView TermsofSale;
    EditText et_Address;
    TextView Name, Cell, Address,Email,Package,OrderDate,OrderId, Total, Subtotal, Vat;
    Button ProceedPayment;
    CustomAlert customAlert;
    CheckBox Agree;
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

        iNavBarUpdateUpdateListener.setNavBarTitle("Order Details");
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        customAlert=new CustomAlert();
        bindViews(view);
    }

    private void bindViews(View view)
    {
        TermsofSale=(TextView)view.findViewById(R.id.tv_terms);
        Name=(TextView)view.findViewById(R.id.tv_name);
        Cell=(TextView)view.findViewById(R.id.tv_cell);
        Address=(TextView)view.findViewById(R.id.tv_address);
        Email=(TextView)view.findViewById(R.id.tv_email);
        Package=(TextView)view.findViewById(R.id.tv_package);
        OrderId=(TextView)view.findViewById(R.id.tv_orderid);
        OrderDate=(TextView)view.findViewById(R.id.tv_orderdate);
        Total=(TextView)view.findViewById(R.id.tv_total);
        Subtotal=(TextView)view.findViewById(R.id.tv_subtotal);
        Vat=(TextView)view.findViewById(R.id.tv_vat);
        Agree=(CheckBox)view.findViewById(R.id.agree_checkbox);
        et_Address=(EditText)view.findViewById(R.id.et_address);
        ProceedPayment=(Button)view.findViewById(R.id.btn_payment);
        TermsofSale.setOnClickListener(this);
        ProceedPayment.setOnClickListener(this);

        Name.setText(AppConfig.getInstance().mUser.getmName());
        Email.setText(AppConfig.getInstance().mUser.getmEmail());

        Log.e("locationn",AppConfig.getInstance().mUser.getZone());
        if (AppConfig.getInstance().mUser.getmNationality().equalsIgnoreCase("Others"))
        {
            Address.setVisibility(View.GONE);
            et_Address.setVisibility(View.VISIBLE);
        }
        else if (AppConfig.getInstance().mUser.getZone()!=null &&
                !(AppConfig.getInstance().mUser.getZone().equalsIgnoreCase("null"))
               && AppConfig.getInstance().mUser.getZone()!="null" && !(TextUtils.isEmpty(AppConfig.getInstance().mUser.getZone())))
        {
            Address.setText(AppConfig.getInstance().mUser.getZone() + ", " +
                    AppConfig.getInstance().mUser.getmNationality() + ", Dhaka");
            Address.setVisibility(View.VISIBLE);
            et_Address.setVisibility(View.GONE);
        }
        else
        {
          Address.setText("Dhaka");
        }
        Cell.setText(AppConfig.getInstance().mUser.getmPhoneNumber());
        Package.setText(getArguments().getString("package"));
        Vat.setText(getArguments().getString("vat"));
        Subtotal.setText(getArguments().getString("subtotal"));
        Total.setText(getArguments().getString("total"));


        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(Calendar.getInstance().getTime());
        OrderId.setText("Order #"+formattedDate);
        SimpleDateFormat df1 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        String formattedDate1 = df1.format(Calendar.getInstance().getTime());
        OrderDate.setText(formattedDate1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_terms:
                Bundle bundle=new Bundle();
                bundle.putString("page", AppConstt.TERMS_OF_SALES);
                navToTermsofSaleFragment(bundle);

                break;
            case R.id.btn_payment:
                if (Agree.isChecked()) {
                    proceedToPayment();
                }
                else
                {
//                  customAlert.showCustomAlertDialog();
                    customAlert.showCustomAlertDialog(getActivity(), null, "You will have to agree to Biyog's Terms of Sale to proceed to checkout.", null, null, false, null);

                }
                break;
        }
    }


        private void proceedToPayment()
    {

        String totalprice=getArguments().getString("total");
        totalprice=totalprice.replace("Tk ","");
        Log.e("total",totalprice);
        Intent in=new Intent(getActivity(),GatewayCallbackActivity.class);
        in.putExtra("payment","true");
        startActivity(in);
        ((MainActivity)getActivity()).finish();
//       requestSubscribe("1","2019-01-18 16:46:22");

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



    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            iNavBarUpdateUpdateListener.setNavBarTitle("Order Details");
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }
    }
}
