package com.wits.grofast_user.Adapter;

import static com.wits.grofast_user.CommonUtilities.getDateFromTimestamp;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_user.Api.responseModels.OrderItemModel;
import com.wits.grofast_user.Api.responseModels.OrderModel;
import com.wits.grofast_user.Api.responseModels.OrderStatusModel;
import com.wits.grofast_user.R;

import java.util.List;

public class AllHistoryAdapter extends RecyclerView.Adapter<AllHistoryAdapter.ViewHolders> {
    private List<OrderModel> orderList;
    private Context context;
    private final String TAG = "AllHistoryAdapter";

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
    public void onBindViewHolder(@NonNull ViewHolders holder, int position) {
        OrderModel item = orderList.get(position);
        holder.ProductOrderId.setText("" + item.getId());
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
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        TextView ProductOrderId, ProductStatus, ProductInvoice, ProductDate, ProductPrice;
        LinearLayout ProductReorder;
        RecyclerView recyclerView;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            ProductOrderId = itemView.findViewById(R.id.history_product_order_id);
            ProductStatus = itemView.findViewById(R.id.history_product_status);
            ProductInvoice = itemView.findViewById(R.id.history_product_invoice);
            ProductDate = itemView.findViewById(R.id.history_product_date);
            ProductPrice = itemView.findViewById(R.id.history_product_price);
            ProductReorder = itemView.findViewById(R.id.history_product_reorder_layout);
            recyclerView = itemView.findViewById(R.id.history_product_inner_recycleview);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
        }
    }
}