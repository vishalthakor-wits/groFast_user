package com.wits.grofast_user.Adapter;

import static com.wits.grofast_user.CommonUtilities.getDateFromTimestamp;
import static com.wits.grofast_user.CommonUtilities.handleApiError;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.interfaces.OrderInterface;
import com.wits.grofast_user.Api.responseClasses.OrderHistoryResponse;
import com.wits.grofast_user.Api.responseClasses.OrderPlaceResponse;
import com.wits.grofast_user.Api.responseModels.OrderItemModel;
import com.wits.grofast_user.Api.responseModels.OrderModel;
import com.wits.grofast_user.Api.responseModels.OrderStatusModel;
import com.wits.grofast_user.Api.responseModels.TaxAndCharge;
import com.wits.grofast_user.MainHomePage.HomePage;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.UserActivitySession;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.ProductDate.setText(getDateFromTimestamp(item.getCreated_at()));
        } else holder.ProductDate.setText(item.getCreated_at());

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
                reorder(item.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        TextView ProductOrderId, ProductStatus, ProductInvoice, ProductDate, ProductPrice;
        LinearLayout ProductReorder;
        RecyclerView recyclerView, chargesRecyclerView;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            ProductOrderId = itemView.findViewById(R.id.history_product_order_id);
            ProductStatus = itemView.findViewById(R.id.history_product_status);
            ProductInvoice = itemView.findViewById(R.id.history_product_invoice);
            ProductDate = itemView.findViewById(R.id.history_product_date);
            ProductPrice = itemView.findViewById(R.id.history_product_price);
            ProductReorder = itemView.findViewById(R.id.history_product_reorder_layout);
            recyclerView = itemView.findViewById(R.id.history_product_inner_recycleview);
            chargesRecyclerView = itemView.findViewById(R.id.order_history_taxes_and_charges_recyclerview);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    private void reorder(int orderId) {
        Call<OrderPlaceResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(OrderInterface.class).reOrder(orderId);

        call.enqueue(new Callback<OrderPlaceResponse>() {
            @Override
            public void onResponse(Call<OrderPlaceResponse> call, Response<OrderPlaceResponse> response) {
                if (response.isSuccessful()) {
                    OrderPlaceDialog();
                    Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT);
                    loadOrderHistory();
                }
                handleApiError(TAG, response, context);
            }

            @Override
            public void onFailure(Call<OrderPlaceResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadOrderHistory() {
        Call<OrderHistoryResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(OrderInterface.class).fetchOrderHistory();

        call.enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                if (response.isSuccessful()) {
                    OrderHistoryResponse orderHistoryResponse = response.body();
                    orderList = orderHistoryResponse.getOrderList();

                    adapter.notifyDataSetChanged();
                } else handleApiError(TAG, response, context);
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void OrderPlaceDialog() {
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
}
