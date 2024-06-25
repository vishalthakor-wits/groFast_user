package com.wits.grofastUser.Details;

import static com.wits.grofastUser.CommonUtilities.handleApiError;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.wits.grofastUser.Api.RetrofitService;
import com.wits.grofastUser.Api.responseClasses.NotificationResponse;
import com.wits.grofastUser.Api.responseModels.NotificationModel;
import com.wits.grofastUser.Notification.NotificationInterface;
import com.wits.grofastUser.R;
import com.wits.grofastUser.session.UserActivitySession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationSetting extends AppCompatActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch enableAll, push, newsletter, promoemail, promopush, socialemail, socialpush;
    UserActivitySession userActivitySession;
    private static String TAG = "NotificationSetting";
    private boolean isInitialSetup = true;
    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.notification_setting_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_notification_setting);

        userActivitySession = new UserActivitySession(getApplicationContext());
        enableAll = findViewById(R.id.switchEnableAll);
        push = findViewById(R.id.switchPushNotifications);
        newsletter = findViewById(R.id.switchEmailNewsletter);
        promoemail = findViewById(R.id.switchEmailPromos);
        promopush = findViewById(R.id.switchPushPromos);
        socialemail = findViewById(R.id.socialemailnotification);
        socialpush = findViewById(R.id.socialpushnotification);
        shimmerFrameLayout = findViewById(R.id.shimmer_layout_notification_setting);
        view = findViewById(R.id.view_all_notification_setting);
        ShowPageLoader();
        setUpListeners();
        notificationFetch();

    }

    private void notificationFetch() {
        Call<NotificationResponse> call1 = RetrofitService.getClient(userActivitySession.getToken()).create(NotificationInterface.class).notificationFetch();
        call1.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                HidePageLoader();
                if (response.isSuccessful()) {
                    NotificationResponse notification = response.body();
                    NotificationModel notificationModel = notification.getNotificationModel();
                    Log.e(TAG, "onResponse: Notification message" + notification.getMessage());
                    Log.e(TAG, "onResponse: Notification status" + notification.getStatus());
                    Log.e(TAG, "onResponse: enable all " + notificationModel.getEnable_all());

                    enableAll.setChecked(notificationModel.getEnable_all() == 1);
                    push.setChecked(notificationModel.getPush_notification() == 1);
                    newsletter.setChecked(notificationModel.getNewsletter_email() == 1);
                    promoemail.setChecked(notificationModel.getPromo_offer_email() == 1);
                    promopush.setChecked(notificationModel.getPromo_offer_push() == 1);
                    socialemail.setChecked(notificationModel.getSocial_notification_email() == 1);
                    socialpush.setChecked(notificationModel.getSocial_notification_push() == 1);

                    isInitialSetup = false; // Initial setup complete
                } else {
                    handleApiError(TAG, response, getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setUpListeners() {
        enableAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isInitialSetup) sendNotificationUpdate("enable_all", isChecked ? 1 : 0);
        });
        push.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isInitialSetup) sendNotificationUpdate("push_notification", isChecked ? 1 : 0);
        });
        newsletter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isInitialSetup) sendNotificationUpdate("newsletter_email", isChecked ? 1 : 0);
        });
        promoemail.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isInitialSetup) sendNotificationUpdate("promo_offer_email", isChecked ? 1 : 0);
        });
        promopush.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isInitialSetup) sendNotificationUpdate("promo_offer_push", isChecked ? 1 : 0);
        });
        socialemail.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isInitialSetup)
                sendNotificationUpdate("social_notification_email", isChecked ? 1 : 0);
        });
        socialpush.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isInitialSetup)
                sendNotificationUpdate("social_notification_push", isChecked ? 1 : 0);
        });
    }

    private void sendNotificationUpdate(String setting, int value) {
        Integer enable_all = null;
        Integer push_notification = null;
        Integer newsletter_email = null;
        Integer promo_offer_email = null;
        Integer promo_offer_push = null;
        Integer social_notification_email = null;
        Integer social_notification_push = null;

        switch (setting) {
            case "enable_all":
                enable_all = value;
                break;
            case "push_notification":
                push_notification = value;
                break;
            case "newsletter_email":
                newsletter_email = value;
                break;
            case "promo_offer_email":
                promo_offer_email = value;
                break;
            case "promo_offer_push":
                promo_offer_push = value;
                break;
            case "social_notification_email":
                social_notification_email = value;
                break;
            case "social_notification_push":
                social_notification_push = value;
                break;
        }

        Call<NotificationResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(NotificationInterface.class).notificationsetting(enable_all, push_notification, newsletter_email, promo_offer_email, promo_offer_push, social_notification_email, social_notification_push);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                HidePageLoader();
                if (response.isSuccessful()) {
                    NotificationResponse notificationResponse = response.body();
                    Log.e(TAG, "message " + notificationResponse.getMessage());
                    Toast.makeText(NotificationSetting.this, "" + notificationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                } else {
                    handleApiError(TAG, response, getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void ShowPageLoader() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }

    private void HidePageLoader() {
        shimmerFrameLayout.setVisibility(View.GONE);
        shimmerFrameLayout.stopShimmer();
        view.setVisibility(View.VISIBLE);
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