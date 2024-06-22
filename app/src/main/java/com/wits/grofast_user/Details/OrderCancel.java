package com.wits.grofast_user.Details;

import static com.wits.grofast_user.CommonUtilities.handleApiError;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;

import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.interfaces.OrderInterface;
import com.wits.grofast_user.Api.responseClasses.LoginResponse;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.UserActivitySession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderCancel extends AppCompatActivity {

    TextView date, status, price, id;
    AppCompatEditText reason;
    int orderId;
    CardView reason_card;
    AppCompatButton cancel_order;
    String reason_cancel;
    UserActivitySession userActivitySession;
    EditText edittxt_other_reason;
    private static String TAG = "OrderCancel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.Order_cancel_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_order_cancel);

        id = findViewById(R.id.cancel_order_id);
        date = findViewById(R.id.cancel_order_date);
        status = findViewById(R.id.cancel_order_status);
        price = findViewById(R.id.cancel_order_price);
        reason = findViewById(R.id.cancel_reason);
        reason_card = findViewById(R.id.write_reason_card);
        cancel_order = findViewById(R.id.cancel_order_btn);
        edittxt_other_reason = findViewById(R.id.edittext_other_reason);
        userActivitySession = new UserActivitySession(getApplicationContext());

        // Retrieve the data from the Intent
        orderId = getIntent().getIntExtra("order_id", -1);
        float orderPrice = getIntent().getFloatExtra("order_price", 0.0f);
        String orderDate = getIntent().getStringExtra("order_date");
        String orderStatus = getIntent().getStringExtra("order_status");
        String orderStatusColor = getIntent().getStringExtra("order_status_color");
        Log.d("AllHistoryAdapter", "Total amount: " + orderPrice);

        // Set the data to the TextViews
        id.setText(String.valueOf(orderId));
        date.setText(orderDate);
        status.setText(orderStatus);
        status.setTextColor(Color.parseColor(orderStatusColor));
        price.setText(String.valueOf(orderPrice));

        reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowBottomSheet();
            }
        });

        cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reason.getText().toString().isEmpty()) {
                    Toast.makeText(OrderCancel.this, R.string.toast_select_reason, Toast.LENGTH_SHORT).show();
                } else if (reason_card.getVisibility() == View.VISIBLE && edittxt_other_reason.getText().toString().isEmpty()) {
                    showToastAndFocus(getString(R.string.toast_enter_reason));
                } else {
                    reason_cancel = reason_card.getVisibility() == View.VISIBLE ? edittxt_other_reason.getText().toString() : reason.getText().toString();
                    showConfirmationDialog();
                }
            }
        });
    }

    private void showToastAndFocus(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        edittxt_other_reason.requestFocus();
        showKeyboard(edittxt_other_reason);
    }

    private void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogButtonsView = LayoutInflater.from(this).inflate(R.layout.confirmdeleteaddress, null);
        builder.setView(dialogButtonsView);

        // Find the buttons in the custom layout
        TextView title = dialogButtonsView.findViewById(R.id.delete_confirmation_title);
        TextView msg = dialogButtonsView.findViewById(R.id.delete_confirmation_msg);
        Button btnNo = dialogButtonsView.findViewById(R.id.btnNo);
        Button btnYes = dialogButtonsView.findViewById(R.id.btnYes);
        AlertDialog dialog = builder.create();

        title.setText(R.string.cancel_oredr_confirmation);
        msg.setText(R.string.cancel_oredr_msg);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Canceloredr(orderId, reason_cancel);
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }

    ImageView close;
    RadioButton r1, r2, r3, r4, r5, r6, r7, r8_other;

    private void ShowBottomSheet() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cancel_reason_bottomsheet);
        close = dialog.findViewById(R.id.cancel_bottomsheet_close);
        r1 = dialog.findViewById(R.id.r1);
        r2 = dialog.findViewById(R.id.r2);
        r3 = dialog.findViewById(R.id.r3);
        r4 = dialog.findViewById(R.id.r4);
        r5 = dialog.findViewById(R.id.r5);
        r6 = dialog.findViewById(R.id.r6);
        r7 = dialog.findViewById(R.id.r7);
        r8_other = dialog.findViewById(R.id.r8_other);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        View.OnClickListener radioListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = (RadioButton) v;
                String selectedReason = rb.getText().toString();
                reason.setText(selectedReason);
                if (r8_other.isChecked()) {
                    reason_card.setVisibility(View.VISIBLE);
                } else {
                    reason_card.setVisibility(View.GONE);
                }
                dialog.dismiss();
            }
        };

        r1.setOnClickListener(radioListener);
        r2.setOnClickListener(radioListener);
        r3.setOnClickListener(radioListener);
        r4.setOnClickListener(radioListener);
        r5.setOnClickListener(radioListener);
        r6.setOnClickListener(radioListener);
        r7.setOnClickListener(radioListener);
        r8_other.setOnClickListener(radioListener);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void Canceloredr(int orderId, String reasonCancel) {
        Call<LoginResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(OrderInterface.class).cancelOrder(orderId, reasonCancel);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    Log.e(TAG, "onResponse: Order Id " + orderId);
                    Log.e(TAG, "onResponse: reason " + reasonCancel);
                    Log.e(TAG, "onResponse: Message " + loginResponse.getMessage());
                    Toast.makeText(OrderCancel.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
                handleApiError(TAG, response, getApplicationContext());
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
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
}