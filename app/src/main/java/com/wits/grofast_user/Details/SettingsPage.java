package com.wits.grofast_user.Details;

import static com.wits.grofast_user.CommonUtilities.handleApiError;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.interfaces.UserInterface;
import com.wits.grofast_user.Api.responseClasses.LoginResponse;
import com.wits.grofast_user.MainActivity;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.CartDetailSession;
import com.wits.grofast_user.session.UserActivitySession;
import com.wits.grofast_user.session.UserDetailSession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsPage extends AppCompatActivity {
    RadioButton hindi_rd, englosh_rd;
    ImageView delete_account;
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
        hindi_rd = findViewById(R.id.language_hindi);
        englosh_rd = findViewById(R.id.language_english);
        englosh_rd.setChecked(true);
        delete_account = findViewById(R.id.delete_account);

        userActivitySession = new UserActivitySession(this);
        userDetailSession = new UserDetailSession(this);
        cartDetailSession = new CartDetailSession(this);

        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAccountConfirmation();
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