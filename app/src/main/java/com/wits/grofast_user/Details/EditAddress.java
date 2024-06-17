package com.wits.grofast_user.Details;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.material.textfield.TextInputEditText;
import com.wits.grofast_user.Api.responseModels.AddressModel;
import com.wits.grofast_user.R;

import java.util.ArrayList;
import java.util.List;

public class EditAddress extends AppCompatActivity {
    private AddressModel addressModel;
    private final String TAG = "EditAddress";

    private TextInputEditText address ;
    AppCompatSpinner state, country, spinCity, spinPincode;
    private List<String> cityList = new ArrayList<>();
    private List<String> pincodeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.update_address_button));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_edit_address);

        Intent intent = getIntent();
        address = findViewById(R.id.edit_address_address);
        country = findViewById(R.id.edit_address_country);
        state = findViewById(R.id.edit_address_state);

        spinCity = findViewById(R.id.edit_address_city);
        spinPincode = findViewById(R.id.edit_address_pincode);

        if (intent.hasExtra("address")) {
            addressModel = intent.getParcelableExtra("address");
            {
                Log.e(TAG, "onCreate: Address : " + addressModel.getAddress());
                Log.e(TAG, "onCreate: Country : " + addressModel.getCountry());
                Log.e(TAG, "onCreate: State   : " + addressModel.getState());
                Log.e(TAG, "onCreate: City    : " + addressModel.getCity());
                Log.e(TAG, "onCreate: Pincode : " + addressModel.getPin_code());
            }

            address.setText(addressModel.getAddress());

            cityList.add(addressModel.getCity());
            pincodeList.add(addressModel.getPin_code());

            ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, cityList);
            ArrayAdapter<String> pincodeAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, pincodeList);

            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            pincodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinCity.setAdapter(cityAdapter);
            spinPincode.setAdapter(pincodeAdapter);
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

}