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
import android.widget.AdapterView;
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
import com.wits.grofast_user.Api.responseModels.SpinnerItemModel;
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

    private CustomSpinnerAdapter countryAdapter, stateAdapter, cityAdapter, pincodeAdapter;
    private final List<CustomSpinnerModel> countrySpinnerList = new ArrayList<>();
    private final List<CustomSpinnerModel> stateSpinnerList = new ArrayList<>();
    private final List<CustomSpinnerModel> citySpinnerList = new ArrayList<>();
    private final List<CustomSpinnerModel> pincodeSpinnerList = new ArrayList<>();

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

        setSpinnerAdapters();

        fetchListsOnSpinnerSelection();

        fetchCountries();

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

    private void setSpinnerAdapters() {
        countryAdapter = new CustomSpinnerAdapter(getApplicationContext(), countrySpinnerList, R.string.spinner_select_country);
        stateAdapter = new CustomSpinnerAdapter(getApplicationContext(), stateSpinnerList, R.string.spinner_select_state);
        cityAdapter = new CustomSpinnerAdapter(getApplicationContext(), citySpinnerList, R.string.spinner_select_city);
        pincodeAdapter = new CustomSpinnerAdapter(getApplicationContext(), pincodeSpinnerList, R.string.spinner_select_pincode);

        countrySpinner.setAdapter(countryAdapter);
        stateSpinner.setAdapter(stateAdapter);
        citySpinner.setAdapter(cityAdapter);
        pincodeSpinner.setAdapter(pincodeAdapter);
    }

    private boolean validateSpinners() {
        Context context = getApplicationContext();
        return (validateCustomSpinner(countrySpinner, context, R.string.spinner_select_country) && validateCustomSpinner(stateSpinner, context, R.string.spinner_select_state) && validateCustomSpinner(citySpinner, context, R.string.spinner_select_city) && validateCustomSpinner(pincodeSpinner, context, R.string.spinner_select_pincode));
    }

    private void fetchCountries() {

        Call<List<SpinnerItemModel>> call = RetrofitService.getClient(userActivitySession.getToken()).create(AddressInterface.class).getCountries();

        call.enqueue(new Callback<List<SpinnerItemModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerItemModel>> call, Response<List<SpinnerItemModel>> response) {
                if (response.isSuccessful()) {
                    List<SpinnerItemModel> countryList = response.body();
                    countrySpinnerList.clear();
                    for (SpinnerItemModel model : countryList) {
                        countrySpinnerList.add(new CustomSpinnerModel(model.getName(), model.getId()));
                    }
                    countryAdapter.notifyDataSetChanged();

//                    CLEAR LOWER SPINNER LIST IF UPPER LIST IS EMPTY
                    if (countryList.isEmpty()) {
                        stateSpinnerList.clear();
                        citySpinnerList.clear();
                        pincodeSpinnerList.clear();

                        stateAdapter.notifyDataSetChanged();
                        cityAdapter.notifyDataSetChanged();
                        pincodeAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerItemModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void fetchStates(int countryId) {

        Call<List<SpinnerItemModel>> call = RetrofitService.getClient(userActivitySession.getToken()).create(AddressInterface.class).getStates(countryId);

        call.enqueue(new Callback<List<SpinnerItemModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerItemModel>> call, Response<List<SpinnerItemModel>> response) {
                if (response.isSuccessful()) {
                    List<SpinnerItemModel> stateList = response.body();
                    stateSpinnerList.clear();
                    for (SpinnerItemModel model : stateList) {
                        stateSpinnerList.add(new CustomSpinnerModel(model.getName(), model.getId()));
                    }
                    stateAdapter.notifyDataSetChanged();

//                    CLEAR LOWER SPINNER LIST IF UPPER LIST IS EMPTY
                    if (stateList.isEmpty()) {
                        citySpinnerList.clear();
                        pincodeSpinnerList.clear();

                        cityAdapter.notifyDataSetChanged();
                        pincodeAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerItemModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void fetchCities(int stateId) {

        Call<List<SpinnerItemModel>> call = RetrofitService.getClient(userActivitySession.getToken()).create(AddressInterface.class).getCities(stateId);

        call.enqueue(new Callback<List<SpinnerItemModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerItemModel>> call, Response<List<SpinnerItemModel>> response) {
                if (response.isSuccessful()) {
                    List<SpinnerItemModel> cityList = response.body();
                    citySpinnerList.clear();
                    for (SpinnerItemModel model : cityList) {
                        citySpinnerList.add(new CustomSpinnerModel(model.getName(), model.getId()));
                    }
                    cityAdapter.notifyDataSetChanged();

//                    CLEAR LOWER SPINNER LIST IF UPPER LIST IS EMPTY
                    if (cityList.isEmpty()) {
                        pincodeSpinnerList.clear();
                        pincodeAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerItemModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void fetchPincodes(int cityId) {

        Call<List<SpinnerItemModel>> call = RetrofitService.getClient(userActivitySession.getToken()).create(AddressInterface.class).getPincodes(cityId);

        call.enqueue(new Callback<List<SpinnerItemModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerItemModel>> call, Response<List<SpinnerItemModel>> response) {
                if (response.isSuccessful()) {
                    List<SpinnerItemModel> pincodeList = response.body();
                    pincodeSpinnerList.clear();
                    for (SpinnerItemModel model : pincodeList) {
                        pincodeSpinnerList.add(new CustomSpinnerModel(model.getCode(), model.getId()));
                    }
                    pincodeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerItemModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void fetchListsOnSpinnerSelection() {
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!countrySpinnerList.isEmpty()) {
                    CustomSpinnerModel model = countrySpinnerList.get(position - 1);
                    fetchStates(model.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!stateSpinnerList.isEmpty()) {
                    CustomSpinnerModel model = stateSpinnerList.get(position - 1);
                    fetchCities(model.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!citySpinnerList.isEmpty()) {
                    CustomSpinnerModel model = citySpinnerList.get(position - 1);
                    fetchPincodes(model.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}