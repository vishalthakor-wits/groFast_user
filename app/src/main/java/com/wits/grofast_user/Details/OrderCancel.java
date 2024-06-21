package com.wits.grofast_user.Details;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wits.grofast_user.R;

public class OrderCancel extends AppCompatActivity {

    TextView date, status, price, id, reason;
    int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.notification_setting_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_order_cancel);

        id = findViewById(R.id.cancel_order_id);
        date = findViewById(R.id.cancel_order_date);
        status = findViewById(R.id.cancel_order_status);
        price = findViewById(R.id.cancel_order_price);
        reason = findViewById(R.id.cancel_reason);

        // Retrieve the data from the Intent
        orderId = getIntent().getIntExtra("order_id", -1);
        float orderPrice = getIntent().getFloatExtra("order_price", 0.0f);
        String orderDate = getIntent().getStringExtra("order_date");
        String orderStatus = getIntent().getStringExtra("order_status");
        String orderStatusColor = getIntent().getStringExtra("order_status_color");
        Log.d("AllHistoryAdapter", "Total amount: " + orderPrice);

        // Set the data to the TextViews
        id.setText(String.valueOf(orderId));
        date.setText(orderDate);
        status.setText(orderStatus);
        status.setTextColor(Color.parseColor(orderStatusColor));
        price.setText(String.valueOf(orderPrice));

        reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowBottomSheet();
            }
        });
    }

    private void ShowBottomSheet() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cancel_reason_bottomsheet);


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}