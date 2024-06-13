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
import com.wits.grofast_user.Api.responseModels.ProductModel;
import com.wits.grofast_user.R;

import java.util.List;
import java.util.Map;

public class RelatedProductAdapter extends RecyclerView.Adapter<RelatedProductAdapter.ViewHolders> {
    private List<ProductModel> productList;
    private Context context;

    public RelatedProductAdapter(Context context, List<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public RelatedProductAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.related_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedProductAdapter.ViewHolders holder, int position) {
       ProductModel item = productList.get(position);
        holder.name.setText(item.getName());
        holder.price.setText(item.getFinal_price());
        Glide.with(context).load(domain + item.getImage()).placeholder(R.drawable.gobhi_image).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {

        TextView name, price;
        ImageView image;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.related_item_image);
            name = itemView.findViewById(R.id.related_item_name);
            price = itemView.findViewById(R.id.related_item_price);
        }
    }
}
