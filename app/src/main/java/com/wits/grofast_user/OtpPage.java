package com.wits.grofast_user;

import static com.wits.grofast_user.CommonUtilities.handleApiError;
import static com.wits.grofast_user.CommonUtilities.setEditTextListeners;
import static com.wits.grofast_user.CommonUtilities.startCountdown;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.interfaces.OtpInterface;
import com.wits.grofast_user.Api.interfaces.UserInterface;
import com.wits.grofast_user.Api.responseClasses.LoginResponse;
import com.wits.grofast_user.Api.responseClasses.OtpVerifyResponse;
import com.wits.grofast_user.Api.responseModels.UserModel;
import com.wits.grofast_user.MainHomePage.HomePage;
import com.wits.grofast_user.session.UserActivitySession;
import com.wits.grofast_user.session.UserDetailSession;

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
    LinearLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_otp_page);

        loadingOverlay = findViewById(R.id.loading_overlay);

        UserActivitySession session = new UserActivitySession(getApplicationContext());
        UserDetailSession userDetailSession = new UserDetailSession(getApplicationContext());

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
        startCountdown(resend, countDownTimer, getApplicationContext(), COUNTDOWN_TIME_MILLIS);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomePage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                enteredOtp = digit1.getText().toString().trim() + digit2.getText().toString().trim() + digit3.getText().toString().trim() + digit4.getText().toString().trim();
                Log.e(TAG, "onCreate: enteredOtp " + enteredOtp);

                if (isOtpValid()) {
                    loadingOverlay.setVisibility(View.VISIBLE);
                    Integer userOtp = Integer.parseInt(enteredOtp);
                    Call<OtpVerifyResponse> call = RetrofitService.getUnAuthorizedClient().create(OtpInterface.class).verifyOtp(receivedPhone, userOtp);
                    call.enqueue(new Callback<OtpVerifyResponse>() {
                        @Override
                        public void onResponse(Call<OtpVerifyResponse> call, Response<OtpVerifyResponse> response) {
                            loadingOverlay.setVisibility(View.GONE);
                            if (response.isSuccessful()) {
                                OtpVerifyResponse otpVerifyResponse = response.body();
                                UserModel userModel = otpVerifyResponse.getUser();

                                Log.e(TAG, "id " + userModel.getId());
                                Log.e(TAG, "phone no " + userModel.getPhone_no());
                                session.setLoginStaus(true);
                                session.setToken(otpVerifyResponse.getAccessToken());


                                userDetailSession.setUserId(userModel.getId());
                                userDetailSession.setName(userModel.getName());
                                userDetailSession.setEmail(userModel.getEmail());
                                userDetailSession.setPhoneNo(userModel.getPhone_no());
                                userDetailSession.setGender(userModel.getGender());
                                userDetailSession.setImage(userModel.getImage());
                                userDetailSession.setUuid(userModel.getUuid());
                                userDetailSession.setWalletStatus(userModel.getWalletStatus());
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
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer.getText().toString().equals("00:00")) {
                    loadingOverlay.setVisibility(View.VISIBLE);
                    Call<LoginResponse> call = RetrofitService.getUnAuthorizedClient().create(OtpInterface.class).login(receivedPhone);

                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                startCountdown(resend, countDownTimer, getApplicationContext(), COUNTDOWN_TIME_MILLIS);
                                LoginResponse loginResponse = response.body();
                                if (loginResponse != null) {
                                    Toast.makeText(getApplicationContext(), "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    loadingOverlay.setVisibility(View.GONE);
                                }
                            } else {
                                handleApiError(TAG, response, getApplicationContext());
                                loadingOverlay.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            t.printStackTrace();
                            loadingOverlay.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_message_resend_otp), Toast.LENGTH_SHORT).show();
                }
            }
        });

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
}