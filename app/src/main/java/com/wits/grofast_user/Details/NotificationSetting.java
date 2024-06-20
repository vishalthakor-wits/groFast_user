package com.wits.grofast_user.Details;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Switch;

import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.responseClasses.NotificationResponse;
import com.wits.grofast_user.Api.responseModels.NotificationModel;
import com.wits.grofast_user.Notification.NotificationInterface;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.NotificationSession;
import com.wits.grofast_user.session.UserActivitySession;
import com.wits.grofast_user.session.UserDetailSession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationSetting extends AppCompatActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch enableAll, push, newsletter, promoemail, promopush, socialemail, socialpush;
    NotificationSession notificationSession;
    UserActivitySession userActivitySession;
    private static String TAG = "NotificationSetting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.notification_setting_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_notification_setting);

        notificationSession = new NotificationSession(getApplicationContext());
        userActivitySession = new UserActivitySession(getApplicationContext());
        enableAll = findViewById(R.id.switchEnableAll);
        push = findViewById(R.id.switchPushNotifications);
        newsletter = findViewById(R.id.switchEmailNewsletter);
        promoemail = findViewById(R.id.switchEmailPromos);
        promopush = findViewById(R.id.switchPushPromos);
        socialemail = findViewById(R.id.socialemailnotification);
        socialpush = findViewById(R.id.socialpushnotification);


        // Set switch states based on session values
        enableAll.setChecked(notificationSession.getEnableAll() == 1);
        push.setChecked(notificationSession.getPushNotification() == 1);
        newsletter.setChecked(notificationSession.getNewsLetterEmailNotification() == 1);
        promoemail.setChecked(notificationSession.getPromoOfferEmail() == 1);
        promopush.setChecked(notificationSession.getpromoOfferPush() == 1);
        socialemail.setChecked(notificationSession.getsocialNotificationEmail() == 1);
        socialpush.setChecked(notificationSession.getsocialNotificationPush() == 1);

        setSwitchListeners();

    }

    private void setSwitchListeners() {
        enableAll.setOnCheckedChangeListener((buttonView, isChecked) -> updateNotificationSetting("enable_all", isChecked));
        push.setOnCheckedChangeListener((buttonView, isChecked) -> updateNotificationSetting("push_notification", isChecked));
        newsletter.setOnCheckedChangeListener((buttonView, isChecked) -> updateNotificationSetting("newsletter_email", isChecked));
        promoemail.setOnCheckedChangeListener((buttonView, isChecked) -> updateNotificationSetting("promo_offer_email", isChecked));
        promopush.setOnCheckedChangeListener((buttonView, isChecked) -> updateNotificationSetting("promo_offer_push", isChecked));
        socialemail.setOnCheckedChangeListener((buttonView, isChecked) -> updateNotificationSetting("social_notification_email", isChecked));
        socialpush.setOnCheckedChangeListener((buttonView, isChecked) -> updateNotificationSetting("social_notification_push", isChecked));
    }

    private void updateNotificationSetting(String setting, boolean isChecked) {
        switch (setting) {
            case "enable_all":
                notificationSession.setEnableAll(isChecked ? 1 : 0);
                break;
            case "push_notification":
                notificationSession.setPushNotification(isChecked ? 1 : 0);
                break;
            case "newsletter_email":
                notificationSession.setNewsLetterEmailNotification(isChecked ? 1 : 0);
                break;
            case "promo_offer_email":
                notificationSession.setPromoOfferEmail(isChecked ? 1 : 0);
                break;
            case "promo_offer_push":
                notificationSession.setPromoOfferPush(isChecked ? 1 : 0);
                break;
            case "social_notification_email":
                notificationSession.setsocialNotificationEmail(isChecked ? 1 : 0);
                break;
            case "social_notification_push":
                notificationSession.setsocialNotificationPush(isChecked ? 1 : 0);
                break;
        }

        // Make the API call to update the settings
        Call<NotificationResponse> call = RetrofitService.getClient(userActivitySession.getToken())
                .create(NotificationInterface.class)
                .notificationsetting();
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.isSuccessful()) {
                    NotificationResponse notificationResponse = response.body();
                    NotificationModel notificationModel = notificationResponse.getNotificationModel();
                    Log.e(TAG, "onResponse: Update Notification message " + notificationResponse.getMessage());
                    Log.e(TAG, "onResponse: Update enable all  " + notificationModel.getEnable_all());
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Log.e(TAG, "Error updating notification setting: " + t.getMessage());
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