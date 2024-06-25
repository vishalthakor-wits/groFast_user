package com.wits.grofastUser;

import static com.google.android.material.internal.ViewUtils.showKeyboard;

import static com.wits.grofastUser.CommonUtilities.handleApiError;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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
import android.widget.Toast;

import com.wits.grofastUser.Api.RetrofitService;
import com.wits.grofastUser.Api.interfaces.OtpInterface;
import com.wits.grofastUser.Api.responseClasses.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    AppCompatButton Continue;
    EditText phoneNo;
    String TAG = "MainActivity";
    LinearLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        Continue = findViewById(R.id.Continue);
        phoneNo = findViewById(R.id.phone_no);
        loadingOverlay = findViewById(R.id.loading_overlay);

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneNo.getText().toString().trim();
                if (phone.isEmpty()) {
                    showToastAndFocus(getString(R.string.toast_message_enter_number));
                } else if (!isValidPhoneNumber(phone)) {
                    showToastAndFocus(getString(R.string.toast_message_valid_number));
                } else {
                    Log.e(TAG, "onClick: phone no " + phone);
                    OtpInterface otpInterface = RetrofitService.getUnAuthorizedClient().create(OtpInterface.class);
                    Call<LoginResponse> call = otpInterface.login(phone);
                    loadingOverlay.setVisibility(View.VISIBLE);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            loadingOverlay.setVisibility(View.GONE);
                            if (response.isSuccessful()) {
                                LoginResponse loginResponse = response.body();
                                Toast.makeText(getApplicationContext(), "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "onResponse: message " + loginResponse.getMessage());
                                Log.i(TAG, "onResponse: phoneNo " + loginResponse.getPhone_no());

                                Intent in = new Intent(getApplicationContext(), OtpPage.class);
                                in.putExtra("mobileNo", loginResponse.getPhone_no());
                                startActivity(in);
                            } else {
                                handleApiError(TAG, response, getApplicationContext());
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            t.printStackTrace();
                            loadingOverlay.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.length() == 10 && phone.matches("\\d+");
    }

    private void showToastAndFocus(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        phoneNo.requestFocus();
        showKeyboard(phoneNo);
    }

    private void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingOverlay.setVisibility(View.GONE);
    }
}