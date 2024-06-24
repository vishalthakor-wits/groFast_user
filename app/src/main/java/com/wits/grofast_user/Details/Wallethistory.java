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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
    private TextView noWalletHistory,noWalletHistory1;
    LinearLayout nowalletlayout, walletlayout;
    ProgressBar loader;
    private int currentPage = 1;
    private int lastPage = 1;
    private int visibleThreshold = 4;
    private Call<WalletResponse> call;
    private boolean isLoading = false;

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
        noWalletHistory1 = findViewById(R.id.no_wallet_hostory1);
        nowalletlayout = findViewById(R.id.no_wallet_layout);
        walletlayout = findViewById(R.id.wallet_history_layout);
        loader = findViewById(R.id.loader_wallet_history);

        //Wallet history Item
        userActivitySession = new UserActivitySession(getApplicationContext());
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ShowPageLoader();
        call = RetrofitService.getClient(userActivitySession.getToken()).create(WalletInterface.class).fetchWallet(currentPage);
        loadWalletDetails(call);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount < lastVisibleItem + visibleThreshold) {
                    Log.e("TAG", "onScrolled: firstVisibleItem : " + firstVisibleItemPosition);
                    Log.e("TAG", "onScrolled: lastVisibleItem : " + lastVisibleItem);
                    Log.e("TAG", "onScrolled:  totalItemCount : " + totalItemCount);
                    Log.e("TAG", "onScrolled: lastVisibleItem + visibleThreshold : " + (lastVisibleItem + visibleThreshold));
                    Log.e("TAG", "onScrolled: current page " + currentPage);

                    isLoading = true;
                    call = RetrofitService.getClient(userActivitySession.getToken()).create(WalletInterface.class).fetchWallet(currentPage);
                    loadWalletDetails(call);
                }
            }
        });
    }

    private void loadWalletDetails(Call<WalletResponse> call) {
        Log.e("TAG", "getProducts:     last page  " + lastPage);
        Log.e("TAG", "getProducts: curremnt page  " + currentPage);
        if (lastPage >= currentPage) {
            call.enqueue(new Callback<WalletResponse>() {
                @Override
                public void onResponse(Call<WalletResponse> call, Response<WalletResponse> response) {
                    HidePageLoader();
                    isLoading = false;
                    if (response.isSuccessful()) {
                        WalletResponse walletResponse = response.body();
                        WalletPaginatedRes walletPaginatedRes = walletResponse.getWalletPaginatedRes();
                        if (currentPage == 1) {
                            walletModelslist = walletPaginatedRes.getWalletList();
                            wallethistoryAdapter = new WallethistoryAdapter(walletModelslist, getApplicationContext());
                            recyclerView.setAdapter(wallethistoryAdapter);
//                            if (walletPaginatedRes.getWalletList().isEmpty()) {
//                                showNoWalletMessage(walletResponse.getMessage());
//                            }
                        } else {
                            List<WalletModel> list = walletPaginatedRes.getWalletList();
                            for (WalletModel model : list) {
                                walletModelslist.add(model);
                                wallethistoryAdapter.notifyItemInserted(walletModelslist.size());
                            }
                        }
                        currentPage++;
                        lastPage = walletPaginatedRes.getLast_page();
                    } else if (response.code() == 422) {
                        try {
                            String errorBodyString = response.errorBody().string();
                            Gson gson = new Gson();
                            JsonObject errorBodyJson = gson.fromJson(errorBodyString, JsonObject.class);

                            String errorMessage = errorBodyJson.has("errorMessage") ? errorBodyJson.get("errorMessage").getAsString() : "No errorMessage";
                            String message = errorBodyJson.has("message") ? errorBodyJson.get("message").getAsString() : "No message";

                            showNoWalletMessage(message,errorMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else handleApiError(TAG, response, getApplicationContext());
                }

                @Override
                public void onFailure(Call<WalletResponse> call, Throwable t) {

                }
            });
        }
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

    private void showNoWalletMessage(String message,String errorMessage) {
        walletlayout.setVisibility(View.GONE);
        noWalletHistory.setText(message);
        noWalletHistory1.setText(errorMessage);
        nowalletlayout.setVisibility(View.VISIBLE);

    }

}