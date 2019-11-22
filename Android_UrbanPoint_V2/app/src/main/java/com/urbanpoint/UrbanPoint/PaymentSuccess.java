package com.urbanpoint.UrbanPoint;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.urbanpoint.UrbanPoint.SubscriptionAuxiliries.SubscribeTextAdapter2;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.ExpandableHeightGridView;

import java.util.ArrayList;
import java.util.List;

public class PaymentSuccess extends AppCompatActivity
{
    Button Letsgo;
    private List lstStrings;
    private ExpandableHeightGridView lsv2;
    private SubscribeTextAdapter2 subscribeTextAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_subscription_eligible_success);
        Log.e("testingggg","testingggg");
        Letsgo=(Button)findViewById(R.id.frg_subscription_eligible_success_btn_cnfrm);
        lsv2 = findViewById(R.id.frg_subscription_eligible_success_lsv_2);
        Letsgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentSuccess.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("subscribe","true");
                startActivity(i);
              finish();
            }
        });
//        AppConfig.getInstance().isCommingFromSplash = true;
        updateSubscriptionLists();
    }
    private void updateSubscriptionLists() {

        lstStrings = new ArrayList();
        if (getIntent().getStringExtra("type").equalsIgnoreCase("1"))
        {
            lstStrings.add("You are now subscribed for today to\nBiyog!");
        }
        else if (getIntent().getStringExtra("type").equalsIgnoreCase("2"))
        {
            lstStrings.add("You are now subscribed for 1 week to\nBiyog!");
        }
        else
        {
            lstStrings.add("You are now subscribed for 1 month to\nBiyog!");
        }

        lstStrings.add(getString(R.string.subscription_eligible_success_enjoy));
        subscribeTextAdapter = new SubscribeTextAdapter2(PaymentSuccess.this, lstStrings, false);
        lsv2.setExpanded(true);
        lsv2.setAdapter(subscribeTextAdapter);
    }

    private void initialize() {
        lstStrings = new ArrayList();
        AppConfig.getInstance().isSpaceRequiredToShow = false;

    }
}
