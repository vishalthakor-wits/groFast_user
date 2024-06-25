package com.wits.grofastUser.Details;

import static com.wits.grofastUser.CommonUtilities.handleApiError;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.wits.grofastUser.Api.RetrofitService;
import com.wits.grofastUser.Api.interfaces.WalletInterface;
import com.wits.grofastUser.Api.responseClasses.LoginResponse;
import com.wits.grofastUser.R;
import com.wits.grofastUser.session.UserActivitySession;
import com.wits.grofastUser.session.UserDetailSession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Wallet extends AppCompatActivity {

    private AppCompatButton activate;
    private UserActivitySession userActivitySession;
    private UserDetailSession userDetailSession;
    private final String TAG = "Wallet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.wallet_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_wallet);

        userActivitySession = new UserActivitySession(getApplicationContext());
        userDetailSession = new UserDetailSession(getApplicationContext());
        activate = findViewById(R.id.activate_wallet);
        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateUserWallet();
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

    private void activateUserWallet() {
        Call<LoginResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(WalletInterface.class).activateWallet();

        Intent in = new Intent(getApplicationContext(), Wallethistory.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse walletResponse = response.body();

                    userDetailSession.setWalletStatus(1);
                    Log.i(TAG, "onResponse: status " + walletResponse.getStatus());
                    Log.i(TAG, "onResponse: message " + walletResponse.getMessage());
                    Toast.makeText(Wallet.this, "" + walletResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(in);
                    finish();
                } else handleApiError(TAG, response, getApplicationContext());
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }
}