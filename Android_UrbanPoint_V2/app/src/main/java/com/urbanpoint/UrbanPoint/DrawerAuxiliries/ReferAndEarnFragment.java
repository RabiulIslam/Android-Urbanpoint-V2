package com.urbanpoint.UrbanPoint.DrawerAuxiliries;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;

import java.text.DecimalFormat;

import static com.mixpanel.android.mpmetrics.ExceptionHandler.init;

public class ReferAndEarnFragment extends Fragment implements View.OnClickListener {
    TextView ReferralCode,WalletBalance;
    Button Share;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_refer_and_earn,container,false);
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

        iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.drawer_invite_friends));
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        bindViews(view);
    }

    private void bindViews(View view) {
        WalletBalance=(TextView)view.findViewById(R.id.tv_wallet_balance);
        ReferralCode=(TextView)view.findViewById(R.id.tv_referral_code);
        Share=(Button)view.findViewById(R.id.btn_share);
        Share.setOnClickListener(this);
        setData();
    }

    private void setData()
    {

        if (AppConfig.getInstance().mUser.getWallet()==0.0)
        {
            WalletBalance.setText(getActivity().getResources().getString(R.string.wallet_balance) + " 00.00 Tk");
        }
        else {
            DecimalFormat df = new DecimalFormat("#######.00");
            WalletBalance.setText(getActivity().getResources().getString(R.string.wallet_balance) + " " + df.format(AppConfig.getInstance().mUser.getWallet()) + " Tk");
        }
        Log.e("ref_code",AppConfig.getInstance().mUser.getmReferralCode()+"ref_code");
        ReferralCode.setText(AppConfig.getInstance().mUser.getmReferralCode());
    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_access_code));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String referral_msg= getResources().getString(R.string.app_share_message_text)
                        +" "+ AppConfig.getInstance().mUser.getmReferralCode()+"\n\n"+
                        getResources().getString(R.string.earn_free_subscription);


                String formattedString = String.format(referral_msg, AppConstt.DEFAULT_VALUES.SHARE_URL, "");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, formattedString);
                startActivity(Intent.createChooser(sharingIntent, "Select"));
                break;
        }
    }
}
