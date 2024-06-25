package com.wits.grofastUser.Details;

import static com.wits.grofastUser.CommonUtilities.handleApiError;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wits.grofastUser.Adapter.AllCouponAdapter;
import com.wits.grofastUser.Api.RetrofitService;
import com.wits.grofastUser.Api.interfaces.CouponInterface;
import com.wits.grofastUser.Api.paginatedResponses.CouponPaginationRes;
import com.wits.grofastUser.Api.responseClasses.CouponResponse;
import com.wits.grofastUser.Api.responseModels.CouponModel;
import com.wits.grofastUser.R;
import com.wits.grofastUser.session.UserActivitySession;

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
    ScrollView data;
    TextView nocoupontext1, nocoupontext2;

    private boolean isLoading = false;
    private int currentPage = 1;
    private int lastPage = 1;
    private int visibleThreshold = 4;
    private final int apiDelay = 2000;

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
        loadCouponItems(currentPage);

        //Coupon Item
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();


                if (!isLoading && totalItemCount < lastVisibleItem + visibleThreshold) {
                    Log.e("TAG", "onScrolled: firstVisibleItem : " + firstVisibleItemPosition);
                    Log.e("TAG", "onScrolled: lastVisibleItem : " + lastVisibleItem);
                    Log.e("TAG", "onScrolled:  totalItemCount : " + totalItemCount);
                    Log.e("TAG", "onScrolled: lastVisibleItem + visibleThreshold : " + (lastVisibleItem + visibleThreshold));

                    isLoading = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isLoading = false;
                        }
                    }, apiDelay);

                    loadCouponItems(currentPage);
                }
            }
        });
    }

    private void loadCouponItems(int page) {
        Log.e(TAG, "loadCouponItems: current page : " + page);
        Log.e(TAG, "loadCouponItems: last page    : " + lastPage);
        Call<CouponResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(CouponInterface.class).fetchCoupon(page);
        if (lastPage >= currentPage) {
        call.enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {
                HidePageLoader();
                if (response.isSuccessful()) {
                    CouponResponse couponResponse = response.body();
                    CouponPaginationRes couponPaginationRes = couponResponse.getCouponPaginationRes();
                    List<CouponModel> list = couponPaginationRes.getCouponList();

                    if (page == 1) {
                        couponModelList = couponPaginationRes.getCouponList();
                        allCouponAdapter = new AllCouponAdapter(getApplicationContext(), couponModelList);
                        recyclerView.setAdapter(allCouponAdapter);
                    } else {
                        for (CouponModel model : list) {
                            couponModelList.add(model);
                            allCouponAdapter.notifyItemInserted(couponModelList.size());
                        }
                    }
                    currentPage++;
                    lastPage = couponPaginationRes.getLast_page();
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
        } else Log.e(TAG, "loadCouponItems: coupon list end");
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