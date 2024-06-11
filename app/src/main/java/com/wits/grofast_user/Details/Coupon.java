package com.wits.grofast_user.Details;

import static com.wits.grofast_user.CommonUtilities.handleApiError;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wits.grofast_user.Adapter.AllCouponAdapter;
import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.interfaces.CouponInterface;
import com.wits.grofast_user.Api.paginatedResponses.CouponPaginationRes;
import com.wits.grofast_user.Api.responseClasses.CouponResponse;
import com.wits.grofast_user.Api.responseModels.CouponModel;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.UserActivitySession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Coupon extends AppCompatActivity {
    RecyclerView recyclerView;
    AllCouponAdapter allCouponAdapter;
    private List<CouponModel> couponModelList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    private final String TAG = "CouponActivity";
    private UserActivitySession userActivitySession;
    LinearLayout load_data, nocouponlayout;
    NestedScrollView data;
    TextView nocoupontext1, nocoupontext2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.coupon_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_coupon);

        recyclerView = findViewById(R.id.all_coupon_recycleview);
        load_data = findViewById(R.id.coupon_load);
        nocouponlayout = findViewById(R.id.no_coupon_layout);
        nocoupontext1 = findViewById(R.id.no_coupon_text1);
        nocoupontext2 = findViewById(R.id.no_coupon_text2);
        data = findViewById(R.id.scroll_coupon_data);

        userActivitySession = new UserActivitySession(getApplicationContext());
        ShowPageLoader();
        loadCouponItems();

        //Coupon Item
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void loadCouponItems() {
        Call<CouponResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(CouponInterface.class).fetchCoupon(1);
        call.enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {
                HidePageLoader();
                if (response.isSuccessful()) {
                    CouponResponse couponResponse = response.body();
                    CouponPaginationRes couponPaginationRes = couponResponse.getCouponPaginationRes();
                    couponModelList = couponPaginationRes.getCouponList();
                    allCouponAdapter = new AllCouponAdapter(getApplicationContext(), couponModelList);
                    recyclerView.setAdapter(allCouponAdapter);
                    Log.i(TAG, "onResponse: total products " + couponPaginationRes.getTotal());
                    Log.i(TAG, "onResponse: fetched products " + couponPaginationRes.getTo());
                } else if (response.code() == 422) {
                    try {
                        String errorBodyString = response.errorBody().string();
                        Gson gson = new Gson();
                        JsonObject errorBodyJson = gson.fromJson(errorBodyString, JsonObject.class);

                        String errorMessage = errorBodyJson.has("errorMessage") ? errorBodyJson.get("errorMessage").getAsString() : "No errorMessage";
                        String message = errorBodyJson.has("message") ? errorBodyJson.get("message").getAsString() : "No message";

                        showNoCouponMessage(message, errorMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    handleApiError(TAG, response, getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<CouponResponse> call, Throwable t) {
                HidePageLoader();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void ShowPageLoader() {
        load_data.setVisibility(View.VISIBLE);
        data.setVisibility(View.GONE);
    }

    private void HidePageLoader() {
        load_data.setVisibility(View.GONE);
        data.setVisibility(View.VISIBLE);
    }

    private void showNoCouponMessage(String message, String errorMessage) {
        data.setVisibility(View.GONE);
        nocoupontext1.setText(message);
        nocoupontext2.setText(errorMessage);
        nocouponlayout.setVisibility(View.VISIBLE);
    }
}