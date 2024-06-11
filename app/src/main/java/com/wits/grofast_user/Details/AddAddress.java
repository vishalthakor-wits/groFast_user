package com.wits.grofast_user.Details;

import static com.wits.grofast_user.CommonUtilities.handleApiError;
import static com.wits.grofast_user.CommonUtilities.validateAddress;
import static com.wits.grofast_user.CommonUtilities.validateCity;
import static com.wits.grofast_user.CommonUtilities.validateCountry;
import static com.wits.grofast_user.CommonUtilities.validatePincode;
import static com.wits.grofast_user.CommonUtilities.validateState;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.interfaces.AddressInterface;
import com.wits.grofast_user.Api.responseClasses.AddressAddResponse;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.UserActivitySession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddress extends AppCompatActivity {

    private AppCompatButton saveAddress;
    private TextInputEditText address, country, state, city, pincode;
    private final String TAG = "AddAddress";
    private UserActivitySession userActivitySession;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.add_address_button));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_add_address);

        userActivitySession = new UserActivitySession(getApplicationContext());
        address = findViewById(R.id.add_address_address);
        country = findViewById(R.id.add_address_country);
        state = findViewById(R.id.add_address_state);
        city = findViewById(R.id.add_address_city);
        pincode = findViewById(R.id.add_address_pincode);
        progressBar=findViewById(R.id.loader_save_address);

        saveAddress = findViewById(R.id.add_address_save_button);

        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                String userAddress = address.getText().toString().trim();
                String userCountry = country.getText().toString().trim();
                String userState = state.getText().toString().trim();
                String userCity = city.getText().toString().trim();
                String userPincode = pincode.getText().toString().trim();

                if (validateAddress(userAddress, context) && validateCountry(userCountry, context) && validateState(userState, context) && validateCity(userCity, context) && validatePincode(userPincode, context)) {
                    addAddress(userAddress, userCountry, userState, userCity, userPincode);
                }
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

    private void addAddress(String address, String country, String state, String city, String pincode) {
        progressBar.setVisibility(View.VISIBLE);
        saveAddress.setVisibility(View.GONE);
        Log.e(TAG, "addAddress(): Here address will be added");
        Call<AddressAddResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(AddressInterface.class).addCustomreAddress(address, country, state, city, pincode);
        call.enqueue(new Callback<AddressAddResponse>() {
            @Override
            public void onResponse(Call<AddressAddResponse> call, Response<AddressAddResponse> response) {
                progressBar.setVisibility(View.GONE);
                saveAddress.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    AddressAddResponse addressAddResponse = response.body();
                    Toast.makeText(AddAddress.this, "" + addressAddResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else handleApiError(TAG, response, getApplicationContext());
            }

            @Override
            public void onFailure(Call<AddressAddResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}