package com.wits.grofast_user.Details;

import static com.wits.grofast_user.CommonUtilities.handleApiError;
import static com.wits.grofast_user.CommonUtilities.validateAddress;
import static com.wits.grofast_user.CommonUtilities.validateCustomSpinner;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.material.textfield.TextInputEditText;
import com.wits.grofast_user.Adapter.CustomSpinnerAdapter;
import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.interfaces.AddressInterface;
import com.wits.grofast_user.Api.responseClasses.AddressAddResponse;
import com.wits.grofast_user.Api.responseModels.CustomSpinnerModel;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.UserActivitySession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddress extends AppCompatActivity {

    private AppCompatButton saveAddress;
    private TextInputEditText address;
    AppCompatSpinner countrySpinner, stateSpinner, citySpinner, pincodeSpinner;
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
        countrySpinner = findViewById(R.id.add_address_country);
        stateSpinner = findViewById(R.id.add_address_state);
        citySpinner = findViewById(R.id.add_address_city);
        pincodeSpinner = findViewById(R.id.add_address_pincode);
        progressBar=findViewById(R.id.loader_save_address);

        setCountrySpinnerValues();
        setStateSpinnerValues();
        setCitySpinnerValues();
        setPincodeSpinnerValues();

        saveAddress = findViewById(R.id.add_address_save_button);

        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                String userAddress = address.getText().toString().trim();
                CustomSpinnerModel userCountryModel = (CustomSpinnerModel) countrySpinner.getSelectedItem();
                CustomSpinnerModel userStateModel = (CustomSpinnerModel) stateSpinner.getSelectedItem();
                CustomSpinnerModel userCityModel = (CustomSpinnerModel) citySpinner.getSelectedItem();
                CustomSpinnerModel userPincodeModel = (CustomSpinnerModel) pincodeSpinner.getSelectedItem();

                if (validateAddress(userAddress, getApplicationContext()) && validateSpinners()) {
                    addAddress(userAddress, userCountryModel.getName(), userStateModel.getName(), userCityModel.getName(), userPincodeModel.getName());
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

    private void setCountrySpinnerValues() {
        List<CustomSpinnerModel> spinnerCountryList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            spinnerCountryList.add(new CustomSpinnerModel("India " + i, i));
        }
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), spinnerCountryList, R.string.spinner_select_country);
        countrySpinner.setAdapter(spinnerAdapter);
    }

    private void setStateSpinnerValues() {
        List<CustomSpinnerModel> spinnerStateList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            spinnerStateList.add(new CustomSpinnerModel("Gujarat " + i, i));
        }
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), spinnerStateList, R.string.spinner_select_state);
        stateSpinner.setAdapter(spinnerAdapter);
    }

    private void setCitySpinnerValues() {
        List<CustomSpinnerModel> spinnerCityList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            spinnerCityList.add(new CustomSpinnerModel("Bharuch " + i, i));
        }
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), spinnerCityList, R.string.spinner_select_city);
        citySpinner.setAdapter(spinnerAdapter);
    }

    private void setPincodeSpinnerValues() {
        List<CustomSpinnerModel> spinnerPincodeList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            spinnerPincodeList.add(new CustomSpinnerModel("39205" + i, i));
        }
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), spinnerPincodeList, R.string.spinner_select_pincode);
        pincodeSpinner.setAdapter(spinnerAdapter);
    }

    private boolean validateSpinners() {
        Context context = getApplicationContext();
        return (validateCustomSpinner(countrySpinner, context, R.string.spinner_select_country) && validateCustomSpinner(stateSpinner, context, R.string.spinner_select_state) && validateCustomSpinner(citySpinner, context, R.string.spinner_select_city) && validateCustomSpinner(pincodeSpinner, context, R.string.spinner_select_pincode));
    }
}