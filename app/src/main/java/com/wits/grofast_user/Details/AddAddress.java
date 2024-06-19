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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.material.textfield.TextInputEditText;
import com.wits.grofast_user.Adapter.CustomSpinnerAdapter;
import com.wits.grofast_user.Api.Address.SpinnerModel;
import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.interfaces.AddressInterface;
import com.wits.grofast_user.Api.responseClasses.AddressAddResponse;
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
                SpinnerModel userCountryModel = (SpinnerModel) countrySpinner.getSelectedItem();
                SpinnerModel userStateModel = (SpinnerModel) stateSpinner.getSelectedItem();
                SpinnerModel userCityModel = (SpinnerModel) citySpinner.getSelectedItem();
                SpinnerModel userPincodeModel = (SpinnerModel) pincodeSpinner.getSelectedItem();

                {
                    Log.e(TAG, "onClick: saveAddress userAddress : " + userAddress);
                    Log.e(TAG, "onClick: saveAddress     country : " + userCountryModel.getName());
                    Log.e(TAG, "onClick: saveAddress       state : " + userStateModel.getName());
                    Log.e(TAG, "onClick: saveAddress        city : " + userCityModel.getName());
                    Log.e(TAG, "onClick: saveAddress     pincode : " + userPincodeModel.getName());
                }
                if (validateAddress(userAddress, context) && validateCountry(userCountryModel.getName(), context) && validateState(userStateModel.getName(), context) && validateCity(userCityModel.getName(), context) && validatePincode(userPincodeModel.getName(), context)) {
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
        List<SpinnerModel> spinnerCountryList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            spinnerCountryList.add(new SpinnerModel("India " + i, i));
        }
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), spinnerCountryList, R.string.spinner_select_country);
        countrySpinner.setAdapter(spinnerAdapter);
    }

    private void setStateSpinnerValues() {
        List<SpinnerModel> spinnerStateList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            spinnerStateList.add(new SpinnerModel("Gujarat " + i, i));
        }
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), spinnerStateList, R.string.spinner_select_state);
        stateSpinner.setAdapter(spinnerAdapter);
    }

    private void setCitySpinnerValues() {
        List<SpinnerModel> spinnerCityList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            spinnerCityList.add(new SpinnerModel("Bharuch " + i, i));
        }
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), spinnerCityList, R.string.spinner_select_city);
        citySpinner.setAdapter(spinnerAdapter);
    }

    private void setPincodeSpinnerValues() {
        List<SpinnerModel> spinnerPincodeList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            spinnerPincodeList.add(new SpinnerModel("39205" + i, i));
        }
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), spinnerPincodeList, R.string.spinner_select_pincode);
        pincodeSpinner.setAdapter(spinnerAdapter);
    }
}