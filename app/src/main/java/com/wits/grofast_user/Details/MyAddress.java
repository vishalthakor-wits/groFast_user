package com.wits.grofast_user.Details;

import static com.wits.grofast_user.CommonUtilities.handleApiError;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wits.grofast_user.Adapter.ShowAllAddressAdapter;
import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.interfaces.AddressInterface;
import com.wits.grofast_user.Api.responseClasses.AddressFetchResponse;
import com.wits.grofast_user.Api.responseModels.AddressModel;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.UserActivitySession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAddress extends AppCompatActivity {
    RecyclerView recyclerView;
    ShowAllAddressAdapter showAllAddressAdapter;
    private List<AddressModel> addressList = new ArrayList<>();
    private UserActivitySession userActivitySession;
    AppCompatButton add_address;
    private final String TAG = "MyAddress";
    ProgressBar progressBar;
    LinearLayout noaddresslayout;
    TextView noaddresstext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.my_address_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_my_address);

        recyclerView = findViewById(R.id.show_all_address_recycleview);
        add_address = findViewById(R.id.Add_address);
        progressBar = findViewById(R.id.loader_address);
        noaddresslayout = findViewById(R.id.no_address_layout);
        noaddresstext = findViewById(R.id.no_address_text1);
        userActivitySession = new UserActivitySession(getApplicationContext());
        ShowPageLoader();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddAddress.class);
                startActivity(i);
            }
        });

    }

    private void loadAddress() {
        Call<AddressFetchResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(AddressInterface.class).fetchCustmerAddress();
        call.enqueue(new Callback<AddressFetchResponse>() {
            @Override
            public void onResponse(Call<AddressFetchResponse> call, Response<AddressFetchResponse> response) {
                HidePageLoader();
                if (response.isSuccessful()) {
                    AddressFetchResponse addressFetchResponse = response.body();
                    addressList = addressFetchResponse.getAddressList();
                    showAllAddressAdapter = new ShowAllAddressAdapter(getApplicationContext(), addressList);
                    recyclerView.setAdapter(showAllAddressAdapter);
                    Log.e(TAG, "onResponse: message : " + addressFetchResponse.getMessage());
                }
                else if (response.code() == 422) {
                    try {
                        String errorBodyString = response.errorBody().string();
                        Gson gson = new Gson();
                        JsonObject errorBodyJson = gson.fromJson(errorBodyString, JsonObject.class);

                        String errorMessage = errorBodyJson.has("errorMessage") ? errorBodyJson.get("errorMessage").getAsString() : "No errorMessage";
                        String message = errorBodyJson.has("message") ? errorBodyJson.get("message").getAsString() : "No message";

                        showNoAddressMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                handleApiError(TAG, response, getApplicationContext());
            }

            @Override
            public void onFailure(Call<AddressFetchResponse> call, Throwable t) {
                HidePageLoader();
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

    private void ShowPageLoader() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        add_address.setVisibility(View.GONE);
    }

    private void HidePageLoader() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        add_address.setVisibility(View.VISIBLE);
    }

    private void showNoAddressMessage(String message) {
        recyclerView.setVisibility(View.GONE);
        noaddresslayout.setVisibility(View.VISIBLE);
        add_address.setVisibility(View.VISIBLE);
        noaddresstext.setText(message);
    }

    private void hideNoAddressMessage() {
        recyclerView.setVisibility(View.VISIBLE);
        noaddresslayout.setVisibility(View.GONE);
        add_address.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideNoAddressMessage();
        ShowPageLoader();
        loadAddress();
    }
}