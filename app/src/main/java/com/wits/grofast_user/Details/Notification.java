package com.wits.grofast_user.Details;

import static com.wits.grofast_user.CommonUtilities.handleApiError;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wits.grofast_user.Adapter.AddLocationSerachResultAdapter;
import com.wits.grofast_user.Adapter.NotificationAdapter;
import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.paginatedResponses.InAppNotificationPaginatedRes;
import com.wits.grofast_user.Api.responseClasses.InAppNotificationResponse;
import com.wits.grofast_user.Api.responseClasses.NotificationResponse;
import com.wits.grofast_user.Api.responseModels.InAppNotificationModel;
import com.wits.grofast_user.Notification.NotificationInterface;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.UserActivitySession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notification extends AppCompatActivity {

    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    List<InAppNotificationModel> notificationModels = new ArrayList<>();
    UserActivitySession userActivitySession;
    private static String TAG = "Notification";
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.notification_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_notification);

        userActivitySession = new UserActivitySession(getApplicationContext());

        fetchNotifications(currentPage);

        //Notification Item
        recyclerView = findViewById(R.id.notification_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        notificationAdapter = new NotificationAdapter(getApplicationContext(), notificationModels);
        recyclerView.setAdapter(notificationAdapter);

    }

    private void fetchNotifications(int page) {
        Call<InAppNotificationResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(NotificationInterface.class).inappnotification(page);
        call.enqueue(new Callback<InAppNotificationResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<InAppNotificationResponse> call, Response<InAppNotificationResponse> response) {
                if (response.isSuccessful()) {
                    InAppNotificationResponse notificationResponse = response.body();
                    if (notificationResponse != null) {
                        InAppNotificationPaginatedRes paginatedResponse = notificationResponse.getInAppNotificationPaginatedRes();
                        if (paginatedResponse != null) {
                            List<InAppNotificationModel> notification = paginatedResponse.getInAppNotificationModels();
                            if (notification != null && !notification.isEmpty()) {
                                notificationAdapter.addNotification(notification);
                            }
                            Log.d(TAG, "onResponse: getProducts message " + notificationResponse.getMessage());
                            Log.d(TAG, "onResponse: total products " + paginatedResponse.getTotal());
                            Log.d(TAG, "onResponse: fetched products " + paginatedResponse.getTo());
                        }
                    }
                } else {
                    handleApiError(TAG, response, getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<InAppNotificationResponse> call, Throwable t) {
                t.printStackTrace();
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

}