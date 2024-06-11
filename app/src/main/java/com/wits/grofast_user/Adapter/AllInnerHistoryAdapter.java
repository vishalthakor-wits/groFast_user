package com.wits.grofast_user.Adapter;

import static com.wits.grofast_user.Api.RetrofitService.domain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wits.grofast_user.Api.responseModels.OrderItemModel;
import com.wits.grofast_user.Api.responseModels.ProductModel;
import com.wits.grofast_user.R;

import java.util.List;

public class AllInnerHistoryAdapter extends RecyclerView.Adapter<AllInnerHistoryAdapter.ViewHolders> {
    private List<OrderItemModel> orderItems;
    private Context context;

    public AllInnerHistoryAdapter(Context context, List<OrderItemModel> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public AllInnerHistoryAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_inner_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllInnerHistoryAdapter.ViewHolders holder, int position) {
        OrderItemModel item = orderItems.get(position);
        ProductModel product = item.getProduct();

        holder.quantity.setText("" + item.getQuantity());
        holder.productname.setText("" + product.getName());
        holder.price.setText("" + item.getSubtotal());
        Glide.with(context).load(domain + product.getImage()).placeholder(R.drawable.apple).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        TextView quantity, productname,price;
        ImageView image;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            quantity = itemView.findViewById(R.id.inner_product_quantity);
            productname = itemView.findViewById(R.id.inner_product_name);
            image = itemView.findViewById(R.id.inner_product_image);
            price = itemView.findViewById(R.id.inner_product_price);
        }
    }
}
