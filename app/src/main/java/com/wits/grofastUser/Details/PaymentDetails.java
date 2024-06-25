package com.wits.grofastUser.Details;

import static com.wits.grofastUser.CommonUtilities.handleApiError;
import static com.wits.grofastUser.CommonUtilities.validatePhone;
import static com.wits.grofastUser.CommonUtilities.validateReceiverName;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofastUser.Adapter.AddresslistAdapter;
import com.wits.grofastUser.Api.RetrofitService;
import com.wits.grofastUser.Api.interfaces.AddressInterface;
import com.wits.grofastUser.Api.interfaces.OrderInterface;
import com.wits.grofastUser.Api.responseClasses.AddressFetchResponse;
import com.wits.grofastUser.Api.responseClasses.OrderPlaceResponse;
import com.wits.grofastUser.Api.responseModels.AddressModel;
import com.wits.grofastUser.Api.responseModels.OrderModel;
import com.wits.grofastUser.MainHomePage.HomePage;
import com.wits.grofastUser.R;
import com.wits.grofastUser.session.CartDetailSession;
import com.wits.grofastUser.session.UserActivitySession;
import com.wits.grofastUser.session.UserDetailSession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetails extends AppCompatActivity {
    RadioButton radioButton;
    AppCompatButton select_address, changeaddress, placeOrder;
    TextView newaddress, selectedaddressshow;
    RecyclerView address_list_recyclerView;
    AddresslistAdapter addresslistAdapter;
    ImageView close;
    Dialog dialog;
    private TextView customerName, customerNumber;
    private List<AddressModel> addressList = new ArrayList<>();
    private UserDetailSession userDetailSession;
    private UserActivitySession userActivitySession;
    private CartDetailSession cartDetailSession;
    private Integer selectedAddressId;
    private EditText receiverName, receiverNumber;
    private final String TAG = "PaymentDetails";
    ProgressBar progressBar, place_order_loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.payment_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_payment_details);
        radioButton = findViewById(R.id.cash_on_Delivery_radiobtn);
        radioButton.setChecked(true);

        userDetailSession = new UserDetailSession(getApplicationContext());
        userActivitySession = new UserActivitySession(getApplicationContext());
        cartDetailSession = new CartDetailSession(getApplicationContext());

        select_address = findViewById(R.id.selected_delivery_address_btn);
        changeaddress = findViewById(R.id.change_selected_address);
        selectedaddressshow = findViewById(R.id.selected_address_textview);
        placeOrder = findViewById(R.id.place_order);
        place_order_loader = findViewById(R.id.place_ordre_loader);

        customerName = findViewById(R.id.payment_customer_name);
        customerNumber = findViewById(R.id.payment_customer_number);
        receiverName = findViewById(R.id.receiver_name);
        receiverNumber = findViewById(R.id.receiver_number);

        customerName.setText("Name : " + userDetailSession.getName());
        customerNumber.setText("Number : " + userDetailSession.getPhoneNo());

        select_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressBottomSheet();
            }
        });

        changeaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressBottomSheet();
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String receiver = receiverName.getText().toString().trim();
                if (validateReceiverName(receiver, getApplicationContext()) && validatePhone(receiverNumber, getApplicationContext())) {
                    if (selectedAddressId != null) {
                        placeOrder.setVisibility(View.GONE);
                        place_order_loader.setVisibility(View.VISIBLE);
                        placeOrder(cartDetailSession.getCoupon(), Integer.parseInt(cartDetailSession.getTip()), cartDetailSession.getAditionalNote(), selectedAddressId, receiver, Long.parseLong(receiverNumber.getText().toString()), 1);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_select_delevery_address), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void OrderPlaceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.orderplacelayout, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        AppCompatButton continue_shopping = dialogView.findViewById(R.id.continue_shopping);
        continue_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(PaymentDetails.this, HomePage.class);
                intent.putExtra("openHomeFragment", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dailogbox_background);
        }
        dialog.show();
    }

    private void showAddressBottomSheet() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addressbottomsheet);

        newaddress = dialog.findViewById(R.id.bottomsheet_add_new_Address);
        close = dialog.findViewById(R.id.bottomsheet_close);
        progressBar = dialog.findViewById(R.id.loader_address_select);

        //Item
        address_list_recyclerView = dialog.findViewById(R.id.bottom_sheet_address_list_recycleview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        address_list_recyclerView.setLayoutManager(linearLayoutManager);
        getCustomerAddress();

        newaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                Intent in = new Intent(getApplicationContext(), AddAddress.class);
                startActivity(in);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void getCustomerAddress() {
        progressBar.setVisibility(View.VISIBLE);
        address_list_recyclerView.setVisibility(View.GONE);
        Call<AddressFetchResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(AddressInterface.class).fetchCustmerAddress();
        call.enqueue(new Callback<AddressFetchResponse>() {
            @Override
            public void onResponse(Call<AddressFetchResponse> call, Response<AddressFetchResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    address_list_recyclerView.setVisibility(View.VISIBLE);
                    AddressFetchResponse addressFetchResponse = response.body();
                    addressList = addressFetchResponse.getAddressList();
                    addresslistAdapter = new AddresslistAdapter(getApplicationContext(), addressList, new AddresslistAdapter.OnDeliveryButtonClickListener() {
                        @Override
                        public void onDeliveryButtonClick(String address, int addressId) {
                            selectedaddressshow.setText(address);
                            selectedaddressshow.setVisibility(View.VISIBLE);
                            changeaddress.setVisibility(View.VISIBLE);
                            select_address.setVisibility(View.GONE);
                            selectedAddressId = addressId;
                            dismissDialog();
                        }
                    });
                    address_list_recyclerView.setAdapter(addresslistAdapter);
                    Log.e(TAG, "onResponse: message : " + addressFetchResponse.getMessage());
                } else handleApiError(TAG, response, getApplicationContext());
            }

            @Override
            public void onFailure(Call<AddressFetchResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private void placeOrder(String couponCode, int tip, String aditionalNote, int addressId, String receiverName, Long receiverPhone, int paymentMethod) {
        Call<OrderPlaceResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(OrderInterface.class).placeOrder(couponCode, tip, aditionalNote, addressId, receiverName, receiverPhone, paymentMethod);
        call.enqueue(new Callback<OrderPlaceResponse>() {
            @Override
            public void onResponse(Call<OrderPlaceResponse> call, Response<OrderPlaceResponse> response) {
                if (response.isSuccessful()) {
                    placeOrder.setVisibility(View.VISIBLE);
                    place_order_loader.setVisibility(View.GONE);
                    OrderPlaceResponse orderPlaceResponse = response.body();
                    OrderModel orderDetails = orderPlaceResponse.getOrderDetails();
                    cartDetailSession.clearSession();
                    OrderPlaceDialog();

//                    {
//                        Log.i(TAG, "onResponse placeOrder: message " + orderPlaceResponse.getMessage());
//                        Log.i(TAG, "onResponse placeOrder: total " + orderDetails.getTotal_amount());
//                        Log.i(TAG, "onResponse placeOrder: coupon " + orderDetails.getCoupon());
//                        Log.i(TAG, "onResponse placeOrder: discount " + orderDetails.getDiscount());
//                        Log.i(TAG, "onResponse placeOrder: cgst " + orderDetails.getCgst());
//                        Log.i(TAG, "onResponse placeOrder: sgst " + orderDetails.getSgst());
//                        Log.i(TAG, "onResponse placeOrder: Delevery Charges " + orderDetails.getDelivery_charges());
//                        Log.i(TAG, "onResponse placeOrder: tip " + orderDetails.getTip());
//                        Log.i(TAG, "onResponse placeOrder: address " + orderDetails.getAddress());
//                        Log.i(TAG, "onResponse placeOrder: note " + orderDetails.getAdditional_note());
//                        Log.i(TAG, "onResponse placeOrder: receiver name  " + orderDetails.getReceiver_name());
//                        Log.i(TAG, "onResponse placeOrder: receiver number " + orderDetails.getReceiver_phone_no());
//                        Log.i(TAG, "onResponse placeOrder: payment method " + orderDetails.getPayment_metod());
//                        Log.i(TAG, "onResponse placeOrder: order status " + orderDetails.getOrderStatus().getStatus());
//                    }

                } else {
                    placeOrder.setVisibility(View.VISIBLE);
                    place_order_loader.setVisibility(View.GONE);
                    handleApiError(TAG, response, getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<OrderPlaceResponse> call, Throwable t) {
                placeOrder.setVisibility(View.VISIBLE);
                place_order_loader.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }
}