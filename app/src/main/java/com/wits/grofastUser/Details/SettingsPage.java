package com.wits.grofastUser.Details;

import static com.wits.grofastUser.CommonUtilities.handleApiError;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wits.grofastUser.Api.RetrofitService;
import com.wits.grofastUser.Api.interfaces.UserInterface;
import com.wits.grofastUser.Api.responseClasses.LoginResponse;
import com.wits.grofastUser.MainActivity;
import com.wits.grofastUser.Policy.CancellationPolicy;
import com.wits.grofastUser.Policy.DeleteAccountPolicy;
import com.wits.grofastUser.Policy.DeleteDataPolicy;
import com.wits.grofastUser.Policy.PrivacyPolicyActivity;
import com.wits.grofastUser.Policy.RefundPolicy;
import com.wits.grofastUser.Policy.ReportPolicy;
import com.wits.grofastUser.Policy.TermsConditionPolicy;
import com.wits.grofastUser.R;
import com.wits.grofastUser.session.CartDetailSession;
import com.wits.grofastUser.session.UserActivitySession;
import com.wits.grofastUser.session.UserDetailSession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsPage extends AppCompatActivity {
    RadioButton englosh_rd;
    LinearLayout delete_account, privacy_policy, terms_condition_policy, delete_data_policy, delete_account_policy, refund_policy, cancellation_policy, report_policy;
    UserActivitySession userActivitySession;
    UserDetailSession userDetailSession;
    CartDetailSession cartDetailSession;
    private final String TAG = "SettingPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.menu_setting));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_settings_page);

        englosh_rd = findViewById(R.id.language_english);
        englosh_rd.setChecked(true);
        delete_account = findViewById(R.id.delete_account);
        privacy_policy = findViewById(R.id.privacy_policy);
        terms_condition_policy = findViewById(R.id.terms_condition__policy);
        delete_data_policy = findViewById(R.id.delete_data_policy);
        delete_account_policy = findViewById(R.id.delete_account_policy);
        refund_policy = findViewById(R.id.refund_policy);
        cancellation_policy = findViewById(R.id.cancellation_policy);
        report_policy = findViewById(R.id.report_policy);

        userActivitySession = new UserActivitySession(this);
        userDetailSession = new UserDetailSession(this);
        cartDetailSession = new CartDetailSession(this);

        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAccountConfirmation();
            }
        });

        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsPage.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });

        terms_condition_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), TermsConditionPolicy.class);
                startActivity(in);
            }
        });

        delete_data_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), DeleteDataPolicy.class);
                startActivity(in);
            }
        });

        delete_account_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), DeleteAccountPolicy.class);
                startActivity(in);
            }
        });

        refund_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), RefundPolicy.class);
                startActivity(in);
            }
        });

        cancellation_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), CancellationPolicy.class);
                startActivity(in);
            }
        });

        report_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), ReportPolicy.class);
                startActivity(in);
            }
        });
    }

    private void DeleteAccountConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete_account_confirmation));
        builder.setMessage(getString(R.string.are_you_sure_delete_account));

        View dialogButtonsView = LayoutInflater.from(this).inflate(R.layout.confirmdeleteaddress, null);
        builder.setView(dialogButtonsView);

        // Find the buttons in the custom layout
        Button btnNo = dialogButtonsView.findViewById(R.id.btnNo);
        Button btnYes = dialogButtonsView.findViewById(R.id.btnYes);
        AlertDialog dialog = builder.create();
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAccount();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void DeleteAccount() {
        Call<LoginResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(UserInterface.class).deleteaccount();
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        String message = loginResponse.getMessage();
                        userActivitySession.setLoginStaus(false);
                        userActivitySession.clearSession();
                        userDetailSession.clearSession();
                        cartDetailSession.clearSession();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        Log.e(TAG, "onResponse: delete account " + message);
                        Toast.makeText(SettingsPage.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
                handleApiError(TAG, response, getApplicationContext());
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
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