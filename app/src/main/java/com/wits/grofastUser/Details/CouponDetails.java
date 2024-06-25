package com.wits.grofastUser.Details;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.wits.grofastUser.R;

public class CouponDetails extends AppCompatActivity {
    ImageView couponimage;
    TextView code, status, description;
    AppCompatButton copy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.coupondetails_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_coupon_details);

        couponimage = findViewById(R.id.coupon_detail_image);
        code = findViewById(R.id.all_coupondeatil_code);
        status = findViewById(R.id.all_coupondeatil_status);
        description = findViewById(R.id.all_coupondeatil_description);
        copy = findViewById(R.id.all_coupondeatil_copy);
        setStatusColor(status);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String couponCode = code.getText().toString();
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("text", couponCode);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), getString(R.string.toast_coupon_code_copied), Toast.LENGTH_SHORT).show();
            }
        });

        int CouponId = getIntent().getIntExtra("CouponId", 0);

        if (CouponId != 0) {
            Log.e("TAG", "Coupon ID: " + CouponId);
        } else {
            Log.e("TAG", "No Coupon ID passed in the intent");
        }

        if (getIntent() != null) {
            code.setText(getIntent().getStringExtra("Code"));
            status.setText(getIntent().getStringExtra("Status"));
            description.setText(getIntent().getStringExtra("Description"));
            Glide.with(getApplicationContext()).load(getIntent().getStringExtra("image")).placeholder(R.drawable.gobhi_image).into(couponimage);
        }
    }

    private void setStatusColor(TextView textView) {
        String statusText = status.getText().toString();
        if (statusText.equals("Active")) {
            textView.setTextColor(getResources().getColor(R.color.active));
        } else if (statusText.equals("Inactive")) {
            textView.setTextColor(getResources().getColor(R.color.inactive));
        } else if (statusText.equals("Expired")) {
            textView.setTextColor(getResources().getColor(R.color.expired));
        } else {
            textView.setTextColor(getResources().getColor(android.R.color.black));
        }
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