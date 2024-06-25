package com.wits.grofastUser.Details;

import static com.wits.grofastUser.CommonUtilities.handleApiError;
import static com.wits.grofastUser.CommonUtilities.validateAddress;
import static com.wits.grofastUser.CommonUtilities.validateCustomSpinner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.wits.grofastUser.Adapter.CustomSpinnerAdapter;
import com.wits.grofastUser.Api.RetrofitService;
import com.wits.grofastUser.Api.interfaces.AddressInterface;
import com.wits.grofastUser.Api.responseClasses.LoginResponse;
import com.wits.grofastUser.Api.responseModels.AddressModel;
import com.wits.grofastUser.Api.responseModels.CustomSpinnerModel;
import com.wits.grofastUser.Api.responseModels.SpinnerItemModel;
import com.wits.grofastUser.R;
import com.wits.grofastUser.session.UserActivitySession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAddress extends AppCompatActivity {
    private AddressModel addressModel;
    private final String TAG = "EditAddress";
    private UserActivitySession userActivitySession;
    private TextInputEditText address ;
    AppCompatSpinner stateSpinner, countrySpinner, citySpinner, pincodeSpinner;

    private CustomSpinnerAdapter countryAdapter, stateAdapter, cityAdapter, pincodeAdapter;
    private final List<CustomSpinnerModel> countrySpinnerList = new ArrayList<>();
    private final List<CustomSpinnerModel> stateSpinnerList = new ArrayList<>();
    private final List<CustomSpinnerModel> citySpinnerList = new ArrayList<>();
    private final List<CustomSpinnerModel> pincodeSpinnerList = new ArrayList<>();

    private List<SpinnerItemModel> countryList, stateList, cityList, pincodeList;
    private AppCompatButton updateAddress;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.update_address_button));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_edit_address);

        linearLayout = findViewById(R.id.address_update_linear_layout);
        shimmerFrameLayout = findViewById(R.id.shimmer_layout_update_address);

        userActivitySession = new UserActivitySession(getApplicationContext());
        Intent intent = getIntent();
        address = findViewById(R.id.edit_address_address);
        countrySpinner = findViewById(R.id.edit_address_country);
        stateSpinner = findViewById(R.id.edit_address_state);
        updateAddress = findViewById(R.id.all_address_edit);

        citySpinner = findViewById(R.id.edit_address_city);
        pincodeSpinner = findViewById(R.id.edit_address_pincode);
        progressBar=findViewById(R.id.loader_update_address);

        startProgressBar();

        if (intent.hasExtra("address")) {
            addressModel = intent.getParcelableExtra("address");
            address.setText(addressModel.getAddress());

            setSpinnerAdapters();
            fetchListsOnSpinnerSelection();
            fetchCountries();
        }
        updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userAddress = address.getText().toString().trim();
                CustomSpinnerModel userCountryModel = (CustomSpinnerModel) countrySpinner.getSelectedItem();
                CustomSpinnerModel userStateModel = (CustomSpinnerModel) stateSpinner.getSelectedItem();
                CustomSpinnerModel userCityModel = (CustomSpinnerModel) citySpinner.getSelectedItem();
                CustomSpinnerModel userPincodeModel = (CustomSpinnerModel) pincodeSpinner.getSelectedItem();
                if (validateAddress(address.getText().toString().trim(), getApplicationContext()) && validateSpinners()) {
                    updateCustomerAddress(addressModel.getId(), userAddress, userCountryModel.getName(), userStateModel.getName(), userCityModel.getName(), userPincodeModel.getName());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void fetchCountries() {

        Call<List<SpinnerItemModel>> call = RetrofitService.getClient(userActivitySession.getToken()).create(AddressInterface.class).getCountries();
        call.enqueue(new Callback<List<SpinnerItemModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerItemModel>> call, Response<List<SpinnerItemModel>> response) {
                if (response.isSuccessful()) {
                    countryList = response.body();
                    countrySpinnerList.clear();
                    for (SpinnerItemModel model : countryList) {
                        countrySpinnerList.add(new CustomSpinnerModel(model.getName(), model.getId()));
                    }
                    countryAdapter.notifyDataSetChanged();

//                    SET SELECTED SPINNER ITEM IN CUSTOM SPINNER
                    {
                        int position = 1;
                        for (CustomSpinnerModel model : countrySpinnerList) {
                            if (model.getName().equals(addressModel.getCountry())) {
                                countrySpinner.setSelection(position);
                                break;
                            }
                            position++;
                        }
                    }

//                    CLEAR LOWER SPINNER LIST IF UPPER LIST IS EMPTY
                    if (countryList.isEmpty()) {
                        stopProgressBar();
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
        startProgressBar();
        Call<List<SpinnerItemModel>> call = RetrofitService.getClient(userActivitySession.getToken()).create(AddressInterface.class).getStates(countryId);
        call.enqueue(new Callback<List<SpinnerItemModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerItemModel>> call, Response<List<SpinnerItemModel>> response) {
                if (response.isSuccessful()) {
                    stateList = response.body();
                    stateSpinnerList.clear();
                    for (SpinnerItemModel model : stateList) {
                        stateSpinnerList.add(new CustomSpinnerModel(model.getName(), model.getId()));
                    }
                    stateAdapter.notifyDataSetChanged();

//                    SET SELECTED SPINNER ITEM IN CUSTOM SPINNER
                    {
                        int position = 1;
                        for (CustomSpinnerModel model : stateSpinnerList) {
                            if (model.getName().equals(addressModel.getState())) {
                                stateSpinner.setSelection(position);
                                break;
                            }
                            position++;
                        }
                    }

//                    CLEAR LOWER SPINNER LIST IF UPPER LIST IS EMPTY
                    if (stateList.isEmpty()) {
                        stopProgressBar();
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
        startProgressBar();
        Call<List<SpinnerItemModel>> call = RetrofitService.getClient(userActivitySession.getToken()).create(AddressInterface.class).getCities(stateId);
        call.enqueue(new Callback<List<SpinnerItemModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerItemModel>> call, Response<List<SpinnerItemModel>> response) {
                if (response.isSuccessful()) {
                    cityList = response.body();
                    citySpinnerList.clear();
                    for (SpinnerItemModel model : cityList) {
                        citySpinnerList.add(new CustomSpinnerModel(model.getName(), model.getId()));
                    }
                    cityAdapter.notifyDataSetChanged();

//                    SET SELECTED SPINNER ITEM IN CUSTOM SPINNER
                    {
                        int position = 1;
                        for (CustomSpinnerModel model : citySpinnerList) {
                            if (model.getName().equals(addressModel.getCity())) {
                                citySpinner.setSelection(position);
                                break;
                            }
                            position++;
                        }
                    }

//                    CLEAR LOWER SPINNER LIST IF UPPER LIST IS EMPTY
                    if (cityList.isEmpty()) {
                        stopProgressBar();

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
        startProgressBar();
        Call<List<SpinnerItemModel>> call = RetrofitService.getClient(userActivitySession.getToken()).create(AddressInterface.class).getPincodes(cityId);
        call.enqueue(new Callback<List<SpinnerItemModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerItemModel>> call, Response<List<SpinnerItemModel>> response) {
                stopProgressBar();
                if (response.isSuccessful()) {
                    pincodeList = response.body();
                    pincodeSpinnerList.clear();
                    for (SpinnerItemModel model : pincodeList) {
                        pincodeSpinnerList.add(new CustomSpinnerModel(model.getCode(), model.getId()));
                    }
                    pincodeAdapter.notifyDataSetChanged();

//                    SET SELECTED SPINNER ITEM IN CUSTOM SPINNER
                    {
                        int position = 1;
                        for (CustomSpinnerModel model : pincodeSpinnerList) {
                            if (model.getName().equals(addressModel.getPin_code())) {
                                pincodeSpinner.setSelection(position);
                                break;
                            }
                            position++;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerItemModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private boolean validateSpinners() {
        Context context = getApplicationContext();
        return (validateCustomSpinner(countrySpinner, context, R.string.spinner_select_country) && validateCustomSpinner(stateSpinner, context, R.string.spinner_select_state) && validateCustomSpinner(citySpinner, context, R.string.spinner_select_city) && validateCustomSpinner(pincodeSpinner, context, R.string.spinner_select_pincode));
    }

    private void updateCustomerAddress(int addressId, String address, String country, String state, String city, String pincode) {
        startProgressBar();
        Call<LoginResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(AddressInterface.class).updateCustomreAddress(addressId, address, country, state, city, pincode);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                stopProgressBar();
                if (response.isSuccessful()) {
                    LoginResponse addressUpdateResponse = response.body();
                    Toast.makeText(getApplicationContext(), "" + addressUpdateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else handleApiError(TAG, response, getApplicationContext());
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                stopProgressBar();
                t.printStackTrace();
            }
        });
    }

    private void startProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        updateAddress.setVisibility(View.GONE);
    }

    private void stopProgressBar() {
        progressBar.setVisibility(View.GONE);
        updateAddress.setVisibility(View.VISIBLE);
    }

}