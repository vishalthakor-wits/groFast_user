package com.wits.grofast_user.Details;

import static android.view.View.GONE;
import static com.wits.grofast_user.CommonUtilities.handleApiError;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_user.Adapter.WallethistoryAdapter;
import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.interfaces.WalletInterface;
import com.wits.grofast_user.Api.paginatedResponses.WalletPaginatedRes;
import com.wits.grofast_user.Api.responseClasses.WalletResponse;
import com.wits.grofast_user.Api.responseModels.WalletModel;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.UserActivitySession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Wallethistory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private WallethistoryAdapter wallethistoryAdapter;
    private final String TAG = "WalletHistoryActivity";
    private List<WalletModel> walletModelslist = new ArrayList<>();
    private UserActivitySession userActivitySession;
    private LinearLayoutManager linearLayoutManager;
    private TextView noWalletHistory;
    LinearLayout nowalletlayout, walletlayout;
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.wallet_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_wallethistory);

        recyclerView = findViewById(R.id.wallet_history_recycleview);
        noWalletHistory = findViewById(R.id.no_wallet_hostory);
        nowalletlayout = findViewById(R.id.no_wallet_layout);
        walletlayout = findViewById(R.id.wallet_history_layout);
        loader = findViewById(R.id.loader_wallet_history);

        //Wallet history Item
        userActivitySession = new UserActivitySession(getApplicationContext());
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ShowPageLoader();
        loadWalletDetails();
    }

    private void loadWalletDetails() {
        Call<WalletResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(WalletInterface.class).fetchWallet();
        call.enqueue(new Callback<WalletResponse>() {
            @Override
            public void onResponse(Call<WalletResponse> call, Response<WalletResponse> response) {
                if (response.isSuccessful()) {
                    WalletResponse walletResponse = response.body();
                    WalletPaginatedRes walletPaginatedRes = walletResponse.getWalletPaginatedRes();
                    walletModelslist = walletPaginatedRes.getWalletList();
                    wallethistoryAdapter = new WallethistoryAdapter(walletModelslist, getApplicationContext());
                    recyclerView.setAdapter(wallethistoryAdapter);
                    HidePageLoader();
                    if (walletPaginatedRes.getWalletList().isEmpty()) {
                        showNoWalletMessage(walletResponse.getMessage());
                    }
                    Log.i(TAG, "onResponse: fetched " + walletPaginatedRes.getTo() + " wallets");
                    Log.i(TAG, "onResponse: total wallets " + walletPaginatedRes.getTotal());
                } else handleApiError(TAG, response, getApplicationContext());
            }

            @Override
            public void onFailure(Call<WalletResponse> call, Throwable t) {

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

    private void ShowPageLoader() {
        walletlayout.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }

    private void HidePageLoader() {
        walletlayout.setVisibility(View.VISIBLE);
        loader.setVisibility(GONE);
    }

    private void showNoWalletMessage(String message) {
        walletlayout.setVisibility(View.GONE);
        noWalletHistory.setText(message);
        nowalletlayout.setVisibility(View.VISIBLE);
    }

}