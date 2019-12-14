package com.urbanpoint.UrbanPoint.SubscriptionAuxiliries;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.GatewayCallbackActivity;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.walletmix.walletmixopglibrary.WalletmixOnlinePaymentGateway;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivityOrderDetail extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    RelativeLayout rlBack;
    TextView TermsofSale;
    EditText et_Address;
    TextView Name, Cell, Address, Email, Package, OrderDate, OrderId, Total, Subtotal, Vat;
    Button ProceedPayment;
    CustomAlert customAlert;
    CheckBox Agree;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderdetail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bindViews();
    }


    private void bindViews() {
        customAlert = new CustomAlert();
        rlBack = (RelativeLayout) findViewById(R.id.app_bar_rl_back);
        TermsofSale = (TextView) findViewById(R.id.tv_terms);
        Name = (TextView) findViewById(R.id.tv_name);
        Cell = (TextView) findViewById(R.id.tv_cell);
        Address = (TextView) findViewById(R.id.tv_address);
        Email = (TextView) findViewById(R.id.tv_email);
        Package = (TextView) findViewById(R.id.tv_package);
        OrderId = (TextView) findViewById(R.id.tv_orderid);
        OrderDate = (TextView) findViewById(R.id.tv_orderdate);
        Total = (TextView) findViewById(R.id.tv_total);
        Subtotal = (TextView) findViewById(R.id.tv_subtotal);
        //Vat = (TextView) findViewById(R.id.tv_vat);
        Agree = (CheckBox) findViewById(R.id.agree_checkbox);
        et_Address = (EditText) findViewById(R.id.et_address);
        ProceedPayment = (Button) findViewById(R.id.btn_payment);
        TermsofSale.setOnClickListener(this);
        ProceedPayment.setOnClickListener(this);
        rlBack.setOnClickListener(this);
        Name.setText(AppConfig.getInstance().mUser.getmName());
        Email.setText(AppConfig.getInstance().mUser.getmEmail());
//        Log.e("locationn",AppConfig.getInstance().mUser.getZone());
        if (AppConfig.getInstance().mUser.getmNationality().equalsIgnoreCase("Others")) {
            Address.setVisibility(View.GONE);
            et_Address.setVisibility(View.VISIBLE);
        } else if (AppConfig.getInstance().mUser.getZone() != null &&
                !(AppConfig.getInstance().mUser.getZone().equalsIgnoreCase("null"))
                && AppConfig.getInstance().mUser.getZone() != "null" && !(TextUtils.isEmpty(AppConfig.getInstance().mUser.getZone()))) {
            Address.setText(AppConfig.getInstance().mUser.getZone() + ", " +
                    AppConfig.getInstance().mUser.getmNationality() + ", Dhaka");
            Address.setVisibility(View.VISIBLE);
            et_Address.setVisibility(View.GONE);
        } else {
            Address.setVisibility(View.GONE);
            et_Address.setVisibility(View.VISIBLE);
//            Address.setText("Dhaka");
        }
        Cell.setText(AppConfig.getInstance().mUser.getmPhoneNumber());
        Package.setText(getIntent().getStringExtra("package"));
        //Vat.setText(getIntent().getStringExtra("vat"));
        Subtotal.setText(getIntent().getStringExtra("subtotal"));
        Total.setText(getIntent().getStringExtra("total"));


        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(Calendar.getInstance().getTime());
        OrderId.setText("Order #" + formattedDate);
        SimpleDateFormat df1 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        String formattedDate1 = df1.format(Calendar.getInstance().getTime());
        OrderDate.setText(formattedDate1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_terms:
                startActivity(new Intent(ActivityOrderDetail.this, TermsofSale.class));
                break;
            case R.id.app_bar_rl_back:
//                Intent i=new Intent(ActivityOrderDetail.this, MainActivity.class);
//                startActivity(i);
                finish();
                break;
            case R.id.btn_payment:
                if (Agree.isChecked()) {
                    if (et_Address.getVisibility() == View.VISIBLE) {
                        if (TextUtils.isEmpty(et_Address.getText().toString())) {
                            Toast.makeText(ActivityOrderDetail.this, "Please enter address", Toast.LENGTH_SHORT).show();
                        } else {
                            proceedToPayment();
                        }
                    } else {
                        proceedToPayment();
                    }

                } else {
//                  customAlert.showCustomAlertDialog();
                    customAlert.showCustomAlertDialog(ActivityOrderDetail.this, null, "You will have to agree to Biyog's Terms of Sale to proceed to checkout.", null, null, false, null);
                }
                break;
        }
    }

    static String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();

    }

    private void proceedToPayment() {

        String totalprice = getIntent().getStringExtra("total");
        totalprice = totalprice.replace("Tk.", "");
        Log.e("total", totalprice);
        WalletmixOnlinePaymentGateway payment = new WalletmixOnlinePaymentGateway(ActivityOrderDetail.this);
        payment.setTransactionInformation(getString(R.string.merchant_id), getString(R.string.merchant_username),
                getString(R.string.merchant_password), getString(R.string.walletmixapp_key), System.currentTimeMillis() + "",
                getAlphaNumericString(15), AppConfig.getInstance().mUser.mName,
                AppConfig.getInstance().mUser.getmPhoneNumber(),
                AppConfig.getInstance().mUser.mEmail,
                "Dhaka", "", "", "",
                "Subscription", totalprice, "BDT", "", "", "", "", "",
                getString(R.string.walletmix_appname), "");
        payment.startTransactions(true, GatewayCallbackActivity.class);
        //finish();
//      Intent in=new Intent(ActivityOrderDetail.this,GatewayCallbackActivity.class);
//      startActivity(in);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent i=new Intent(ActivityOrderDetail.this, MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(i);
    }
}
