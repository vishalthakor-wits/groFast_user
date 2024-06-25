package com.wits.grofast_user;

import static com.wits.grofast_user.CommonUtilities.handleApiError;
import static com.wits.grofast_user.CommonUtilities.setEditTextListeners;
import static com.wits.grofast_user.CommonUtilities.startCountdown;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.interfaces.OtpInterface;
import com.wits.grofast_user.Api.responseClasses.LoginResponse;
import com.wits.grofast_user.Api.responseClasses.NotificationResponse;
import com.wits.grofast_user.Api.responseClasses.OtpVerifyResponse;
import com.wits.grofast_user.Api.responseModels.NotificationModel;
import com.wits.grofast_user.Api.responseModels.UserModel;
import com.wits.grofast_user.MainHomePage.HomePage;
import com.wits.grofast_user.Notification.NotificationInterface;
import com.wits.grofast_user.session.UserActivitySession;
import com.wits.grofast_user.session.UserDetailSession;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpPage extends AppCompatActivity {
    AppCompatButton Continue, resend;
    TextView phone, countDownTimer;
    String receivedPhone, enteredOtp = "";
    EditText digit1, digit2, digit3, digit4;
    long COUNTDOWN_TIME_MILLIS = 30000;
    String TAG = "OtpPage";
    ProgressBar loadingOverlay;
    private UserActivitySession userActivitySession;
    UserDetailSession userDetailSession;
    ImageView login_back_ground,login_image;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.otp_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_otp_page);

        userActivitySession = new UserActivitySession(getApplicationContext());
        loadingOverlay = findViewById(R.id.loading_overlay);
        cardView = findViewById(R.id.login_card);
        login_back_ground = findViewById(R.id.login_back_ground);
        login_image = findViewById(R.id.login_image);
        userDetailSession = new UserDetailSession(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            receivedPhone = intent.getStringExtra("mobileNo");

            Log.e(TAG, "onCreate: receivedPhone " + receivedPhone);
        }

        digit1 = findViewById(R.id.otp_digit1);
        digit2 = findViewById(R.id.otp_digit2);
        digit3 = findViewById(R.id.otp_digit3);
        digit4 = findViewById(R.id.otp_digit4);

        setEditTextListeners(digit1, digit2, digit3, digit4);

        Continue = findViewById(R.id.Continue_otp_page);
        resend = findViewById(R.id.resend_otp_button);
        phone = findViewById(R.id.otp_phone_no);
        countDownTimer = findViewById(R.id.countdown_timer);
        phone.setText(receivedPhone);

        resend.setVisibility(View.GONE);
        countDownTimer.setVisibility(View.VISIBLE);

        startCountdown(resend, countDownTimer, getApplicationContext(), COUNTDOWN_TIME_MILLIS);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpVerify();
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer.getText().toString().equals("00:00")) {
                   Showloder();
                    Call<LoginResponse> call = RetrofitService.getUnAuthorizedClient().create(OtpInterface.class).login(receivedPhone);

                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                startCountdown(resend, countDownTimer, getApplicationContext(), COUNTDOWN_TIME_MILLIS);
                                LoginResponse loginResponse = response.body();
                                if (loginResponse != null) {
                                    Toast.makeText(getApplicationContext(), "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    Hideloader();
                                    resend.setVisibility(View.GONE);
                                    countDownTimer.setVisibility(View.VISIBLE);
                                }
                            } else {
                                handleApiError(TAG, response, getApplicationContext());
                                Hideloader();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            t.printStackTrace();
                           Hideloader();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_message_resend_otp), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void startCountdown(AppCompatButton resend, TextView countDownTimer, Context applicationContext, long countdownTimeMillis) {
        new CountDownTimer(countdownTimeMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                countDownTimer.setText(timeLeftFormatted);

                resend.setClickable(false);
                resend.setBackgroundDrawable(applicationContext.getDrawable(R.drawable.textview_design));
                resend.setTextColor(applicationContext.getColor(R.color.default_color));
            }

            @Override
            public void onFinish() {
                resend.setClickable(true);
                countDownTimer.setText("00:00");
                resend.setVisibility(View.VISIBLE);
                countDownTimer.setVisibility(View.GONE);
                resend.setBackgroundDrawable(applicationContext.getDrawable(R.drawable.color_button));
                resend.setTextColor(getApplicationContext().getColor(R.color.button_text_color));
            }
        }.start();
    }

    private void otpVerify() {
        Intent i = new Intent(getApplicationContext(), HomePage.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        enteredOtp = digit1.getText().toString().trim() + digit2.getText().toString().trim() + digit3.getText().toString().trim() + digit4.getText().toString().trim();
        Log.e(TAG, "onCreate: enteredOtp " + enteredOtp);

        if (isOtpValid()) {
            Showloder();
            Integer userOtp = Integer.parseInt(enteredOtp);
            Call<OtpVerifyResponse> call = RetrofitService.getUnAuthorizedClient().create(OtpInterface.class).verifyOtp(receivedPhone, userOtp);
            call.enqueue(new Callback<OtpVerifyResponse>() {
                @Override
                public void onResponse(Call<OtpVerifyResponse> call, Response<OtpVerifyResponse> response) {
                   Hideloader();
                    if (response.isSuccessful()) {
                        OtpVerifyResponse otpVerifyResponse = response.body();
                        UserModel userModel = otpVerifyResponse.getUser();

                        Log.e(TAG, "id " + userModel.getId());
                        Log.e(TAG, "phone no " + userModel.getPhone_no());
                        userActivitySession.setLoginStaus(true);
                        userActivitySession.setToken(otpVerifyResponse.getAccessToken());
                        userDetailSession.setUserId(userModel.getId());
                        userDetailSession.setName(userModel.getName());
                        userDetailSession.setEmail(userModel.getEmail());
                        userDetailSession.setPhoneNo(userModel.getPhone_no());
                        userDetailSession.setGender(userModel.getGender());
                        userDetailSession.setImage(userModel.getImage());
                        userDetailSession.setUuid(userModel.getUuid());
                        userDetailSession.setWalletStatus(userModel.getWalletStatus());

                        saveFcmToServer();
                        startActivity(i);
                    } else {
                        handleApiError(TAG, response, getApplicationContext());
                    }
                }

                @Override
                public void onFailure(Call<OtpVerifyResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            showToastAndFocus(getString(R.string.toast_message_enter_otp), digit1);
        }
    }


    private boolean isOtpValid() {
        boolean valid = true;
        if (digit1.getText().toString().trim().isEmpty()) {
            valid = false;
        }

        if (digit2.getText().toString().trim().isEmpty()) {
            valid = false;
        }
        if (digit3.getText().toString().trim().isEmpty()) {
            valid = false;
        }

        if (digit4.getText().toString().trim().isEmpty()) {
            valid = false;
        }

        return valid;
    }


    private void showToastAndFocus(String message, EditText editText) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        editText.requestFocus();
        showKeyboard(editText);
    }

    private void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    private void saveFcmToServer() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                return;
            }

            // Get the FCM token
            Call<Void> call = RetrofitService.getClient(userActivitySession.getToken()).create(NotificationInterface.class).storeFcmtoServer(task.getResult());
            Log.e(TAG, "onResponse: fcm token " + task.getResult());

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (!response.isSuccessful()) {
                        handleApiError(TAG, response, getApplicationContext());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });
    }

    private void Hideloader(){
        loadingOverlay.setVisibility(View.GONE);
        cardView.setVisibility(View.VISIBLE);
        login_image.setVisibility(View.VISIBLE);
        login_back_ground.setVisibility(View.VISIBLE);
    }

    private void Showloder(){
        loadingOverlay.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);
        login_image.setVisibility(View.GONE);
        login_back_ground.setVisibility(View.GONE);
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