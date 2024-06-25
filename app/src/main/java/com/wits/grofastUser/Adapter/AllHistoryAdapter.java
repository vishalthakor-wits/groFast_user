package com.wits.grofastUser.Adapter;

import static com.wits.grofastUser.CommonUtilities.getDateFromTimestamp;
import static com.wits.grofastUser.CommonUtilities.handleApiError;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wits.grofastUser.Api.RetrofitService;
import com.wits.grofastUser.Api.interfaces.OrderInterface;
import com.wits.grofastUser.Api.responseClasses.OrderHistoryResponse;
import com.wits.grofastUser.Api.responseClasses.OrderPlaceResponse;
import com.wits.grofastUser.Api.responseModels.OrderItemModel;
import com.wits.grofastUser.Api.responseModels.OrderModel;
import com.wits.grofastUser.Api.responseModels.OrderStatusModel;
import com.wits.grofastUser.Api.responseModels.TaxAndCharge;
import com.wits.grofastUser.Details.OrderCancel;
import com.wits.grofastUser.MainHomePage.HomePage;
import com.wits.grofastUser.R;
import com.wits.grofastUser.session.UserActivitySession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllHistoryAdapter extends RecyclerView.Adapter<AllHistoryAdapter.ViewHolders> {
    private List<OrderModel> orderList;
    private Context context;
    private final String TAG = "AllHistoryAdapter";
    private AllHistoryAdapter adapter;
    private UserActivitySession userActivitySession;

    public AllHistoryAdapter(Context context, List<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public AllHistoryAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_history_fragment_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolders holder, @SuppressLint("RecyclerView") int position) {
        userActivitySession = new UserActivitySession(context);
        adapter = this;
        OrderModel item = orderList.get(position);
        holder.ProductOrderId.setText("" + item.getId());
        List<TaxAndCharge> taxesnCgargesList = new ArrayList<>();

        if (item.getCgst() != 0.0 || item.getCgst() != 0) {
            TaxAndCharge charge = new TaxAndCharge(context.getString(R.string.order_cgst), item.getCgst());
            taxesnCgargesList.add(charge);
        }
        if (item.getSgst() != 0.0 || item.getSgst() != 0) {
            TaxAndCharge charge = new TaxAndCharge(context.getString(R.string.order_sgst), item.getSgst());
            taxesnCgargesList.add(charge);
        }
        if (item.getDelivery_charges() != 0.0 || item.getDelivery_charges() != 0) {
            TaxAndCharge charge = new TaxAndCharge(context.getString(R.string.order_delevery_charges), item.getDelivery_charges());
            taxesnCgargesList.add(charge);
        }

        if (item.getDiscount() != 0.0 || item.getDiscount() != 0) {
            TaxAndCharge charge = new TaxAndCharge(context.getString(R.string.order_discount), item.getDiscount());
            taxesnCgargesList.add(charge);
        }
        if (item.getTip() != 0.0 || item.getTip() != 0) {
            TaxAndCharge charge = new TaxAndCharge(context.getString(R.string.order_tip), item.getTip());
            taxesnCgargesList.add(charge);
        }

        String orderDate = item.getCreated_at();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            orderDate = getDateFromTimestamp(item.getCreated_at());
        } else {
            orderDate = item.getCreated_at().split(" ")[0];
        }
        holder.ProductDate.setText(orderDate);

        OrderStatusModel orderStatus = item.getOrderStatus();
        holder.ProductPrice.setText("" + item.getTotal_amount());

        holder.ProductStatus.setText(orderStatus.getStatus());
        holder.ProductStatus.setTextColor(Color.parseColor(orderStatus.getColor()));

        List<OrderItemModel> orderItems = item.getOrderItems();
        AllInnerHistoryAdapter allInnerHistoryAdapter = new AllInnerHistoryAdapter(context, orderItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(allInnerHistoryAdapter);
        holder.recyclerView.setTag(position);

        TaxesChargesAdapter adapter = new TaxesChargesAdapter(context, taxesnCgargesList);
        holder.chargesRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.chargesRecyclerView.setAdapter(adapter);

        holder.ProductReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress(holder);
                reorder(item.getId(), holder);
            }
        });

        if (item.getIsCancelAllow() == 0) {
            holder.ProductCancel.setVisibility(View.GONE);
        } else {
            holder.ProductCancel.setVisibility(View.VISIBLE);
        }
//        Log.e(TAG, "onBindViewHolder: cancel button " + item.getIsCancelAllow());
//        Log.e(TAG, "onBindViewHolder: Reorder button " + item.getIsReorderAllow());
//        Log.e(TAG, "onBindViewHolder: Return button " + item.getIsReturnAllow());

        if (item.getIsReturnAllow() == 0) {
            holder.ProductReturn.setVisibility(View.GONE);
        } else {
            holder.ProductReturn.setVisibility(View.VISIBLE);
        }

        if (item.getIsReorderAllow() == 0) {
            holder.ProductReorder.setVisibility(View.GONE);
        } else {
            holder.ProductReorder.setVisibility(View.VISIBLE);
        }

        String finalOrderDate = orderDate;
        holder.ProductCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context.getApplicationContext(), OrderCancel.class);
                in.putExtra("order_id", item.getId());
                in.putExtra("order_price", item.getTotal_amount());
                in.putExtra("order_date", finalOrderDate);
                in.putExtra("order_status", orderStatus.getStatus());
                in.putExtra("order_status_color", orderStatus.getColor());
                Log.d("AllHistoryAdapter", "Total amount: " + item.getTotal_amount());
                context.startActivity(in);
            }
        });

        holder.ProductInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadInvoice(item.getInvoice());
                Log.e(TAG, "onClick: Invoice : " + item.getInvoice());
            }
        });
    }

    private void downloadInvoice(String downloadLink) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadLink));
        context.startActivity(browserIntent);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        TextView ProductOrderId, ProductStatus, ProductInvoice, ProductDate, ProductPrice;
        LinearLayout ProductReorder, ProductCancel, ProductReturn;
        RecyclerView recyclerView, chargesRecyclerView;
        ProgressBar reorderProgress;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            ProductOrderId = itemView.findViewById(R.id.history_product_order_id);
            ProductStatus = itemView.findViewById(R.id.history_product_status);
            ProductInvoice = itemView.findViewById(R.id.history_product_invoice);
            ProductDate = itemView.findViewById(R.id.history_product_date);
            ProductPrice = itemView.findViewById(R.id.history_product_price);
            ProductReorder = itemView.findViewById(R.id.history_product_reorder_layout);
            ProductReturn = itemView.findViewById(R.id.history_product_return_layout);
            ProductCancel = itemView.findViewById(R.id.history_product_cancel_layout);
            recyclerView = itemView.findViewById(R.id.history_product_inner_recycleview);
            chargesRecyclerView = itemView.findViewById(R.id.order_history_taxes_and_charges_recyclerview);
            reorderProgress = itemView.findViewById(R.id.reorder_progress_bar);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);

        }
    }

    private void reorder(int orderId, ViewHolders holder) {
        Call<OrderPlaceResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(OrderInterface.class).reOrder(orderId);

        call.enqueue(new Callback<OrderPlaceResponse>() {
            @Override
            public void onResponse(Call<OrderPlaceResponse> call, Response<OrderPlaceResponse> response) {
                stopProgress(holder);
                if (response.isSuccessful()) {
                    orderPlaceDialog();
                    Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT);
                }
                try {
                    String errorBodyString = response.errorBody().string();
                    Gson gson = new Gson();
                    JsonObject errorBodyJson = gson.fromJson(errorBodyString, JsonObject.class);

                    String message = errorBodyJson.has("message") ? errorBodyJson.get("message").getAsString() : "";
                    String errorMessage = errorBodyJson.has("errorMessage") ? errorBodyJson.get("errorMessage").getAsString() : "";

                    orderFailDialog(message);
                } catch (Exception e) {
                    Log.e(TAG, "handleApiError: error " + e.getMessage());
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<OrderPlaceResponse> call, Throwable t) {
                stopProgress(holder);
                t.printStackTrace();
            }
        });
    }

    private void orderPlaceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.orderplacelayout, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        AppCompatButton continue_shopping = dialogView.findViewById(R.id.continue_shopping);
        continue_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(context, HomePage.class);
                intent.putExtra("openHomeFragment", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dailogbox_background);
        }
        dialog.show();
    }

    private void orderFailDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.order_fail_layout, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        TextView messageTextView, errorMessageTextView;
        messageTextView = dialogView.findViewById(R.id.order_failure_message);
        errorMessageTextView = dialogView.findViewById(R.id.order_failure_error_message);

        AppCompatButton continue_shopping = dialogView.findViewById(R.id.continue_shopping);

        messageTextView.setText(message);
        continue_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(context, HomePage.class);
                intent.putExtra("openHomeFragment", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dailogbox_background);
        }
        dialog.show();
    }

    private void startProgress(ViewHolders holder) {
        holder.reorderProgress.setVisibility(View.VISIBLE);
        holder.ProductReorder.setVisibility(View.GONE);
    }

    private void stopProgress(ViewHolders holder) {
        holder.ProductReorder.setVisibility(View.VISIBLE);
        holder.reorderProgress.setVisibility(View.GONE);
    }
}
