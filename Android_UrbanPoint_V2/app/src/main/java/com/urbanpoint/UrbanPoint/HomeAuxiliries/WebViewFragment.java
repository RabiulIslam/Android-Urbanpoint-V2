package com.urbanpoint.UrbanPoint.HomeAuxiliries;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.CommonFragments.OfferDetailFragment;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.RegistrationAuxiliries.SignUpFragmentStepSix;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;

/**
 * Created by Danish on 3/7/2018.
 */

public class WebViewFragment extends Fragment implements View.OnClickListener {
    WebView webView;
    RelativeLayout rlBackbtn;
    ImageView imvBack,imvBack1;
    FragmentManager fragmentManager;
    TextView txvHeader;
    String strTitle;
    RelativeLayout header;
    Dialog progressDialog;
    WebViewClient mClient;
    String strExtra = "";
    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    public static final String REDEEM_RULES = "Rules_of_Purchase.html";
    public static final String TERMS_OF_SALES="Terms_of_Sale.html";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_web_view, container, false);
//        mClient = new WebViewClient() {
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                if (progressDialog != null)
//                    progressDialog.dismiss();
//            }
//        };
        bindViews(v);

        return v;
    }

    void bindViews(View v) {
        rlBackbtn = (RelativeLayout) v.findViewById(R.id.frg_web_view_back_button);
        imvBack = (ImageView) v.findViewById(R.id.imvBack);
        imvBack1 = (ImageView) v.findViewById(R.id.imvBack1);
        webView = (WebView) v.findViewById(R.id.webview);
        txvHeader = (TextView) v.findViewById(R.id.frg_offer_detail_txv_header);
        header=(RelativeLayout)v.findViewById(R.id.frg_web_view_rl_top_header);
        webView.getSettings().setJavaScriptEnabled(true);

        strTitle = "Web View";

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            strExtra = bundle.getString("page");
        }



        if (strExtra.equalsIgnoreCase(AppConstt.LOGIN_RULES)) {
            webView.loadUrl("file:///android_asset/" + AppConstt.LOGIN_RULES);
//            showProgDialog();
            webView.setWebViewClient(mClient);
//            webView.loadUrl("http://urbanpoint.com/Terms_of_service.html");
            strTitle = (getActivity().getResources().getString(R.string.frg_privacy_statement));
            //header.setVisibility(View.VISIBLE);
            imvBack1.setVisibility(View.VISIBLE);
//            try {
//                iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        } else if (strExtra.equalsIgnoreCase(AppConstt.REDEEM_RULES)) {
            webView.loadUrl("file:///android_asset/" + REDEEM_RULES);
            strTitle = (getActivity().getResources().getString(R.string.frg_rules_of_purchase));
            imvBack1.setVisibility(View.GONE);
            try {
                iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();

            } catch (Exception e) {
                e.printStackTrace();
            }
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setNavBarTitle(strTitle);
          //  iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }

        else if (strExtra.equalsIgnoreCase(AppConstt.TERMS_OF_SALES)) {
            webView.loadUrl("file:///android_asset/" + TERMS_OF_SALES);
            strTitle = (getActivity().getResources().getString(R.string.terms_of_sale));
            imvBack1.setVisibility(View.GONE);
            try {
                iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();

            } catch (Exception e) {
                e.printStackTrace();
            }
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setNavBarTitle(strTitle);
            //  iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }

        else if (strExtra.equalsIgnoreCase(AppConstt.REFER_AND_EARN))
        {
            webView.loadUrl("file:///android_asset/refer_earn.html");
            strTitle = "Refer & Earn";
            imvBack1.setVisibility(View.GONE);
            try
            {
                iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();

            } catch (Exception e) {
                e.printStackTrace();
            }
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setNavBarTitle(strTitle);
            //  iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }

        webView.setBackgroundColor(Color.TRANSPARENT);
        rlBackbtn.setOnClickListener(this);
        imvBack1.setOnClickListener(this);
        fragmentManager = getActivity().getSupportFragmentManager();
        if (AppConfig.getInstance().isArabic) {
            imvBack.setImageResource(R.mipmap.arrowbackright);
            imvBack1.setImageResource(R.mipmap.arrowbackright);
        } else {
            imvBack.setImageResource(R.mipmap.arrow_back);
            imvBack1.setImageResource(R.mipmap.arrow_back);
        }


    }

    private void showProgDialog() {
        progressDialog = new Dialog(getActivity(), R.style.AppTheme);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
//        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onClick(View v) {
        Fragment fr = null;
        switch (v.getId()) {
            case R.id.frg_web_view_back_button:
//                if (strExtra.equalsIgnoreCase(AppConstt.LOGIN_RULES)) {
//
//                }
//                else if (strExtra.equalsIgnoreCase(AppConstt.REDEEM_RULES)) {
                    fr = new OfferDetailFragment();

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                    ft.replace(R.id.containerIntroFragments, fr);
                    ft.commit();
                //}
                //  fragmentManager.popBackStack();
                break;
            case R.id.imvBack1:
                fr = new SignUpFragmentStepSix();

                FragmentManager fm1 = getActivity().getSupportFragmentManager();
                FragmentTransaction ft1 = fm1.beginTransaction();
                ft1.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                ft1.replace(R.id.containerIntroFragments, fr);
                ft1.commit();
                break;
        }
    }
}

