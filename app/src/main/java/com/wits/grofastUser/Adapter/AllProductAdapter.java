package com.wits.grofastUser.Adapter;

import static com.wits.grofastUser.CommonUtilities.handleApiError;
import static com.wits.grofastUser.CommonUtilities.showToast;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wits.grofastUser.Api.RetrofitService;
import com.wits.grofastUser.Api.interfaces.CartInterface;
import com.wits.grofastUser.Api.responseClasses.AddToCartResponse;
import com.wits.grofastUser.Api.responseModels.ProductModel;
import com.wits.grofastUser.Details.ProductDetailActivity;
import com.wits.grofastUser.R;
import com.wits.grofastUser.session.UserActivitySession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.ViewHolders> {
    private List<ProductModel> productList = new ArrayList<>();
    private Context context;
    private final String TAG = "AllProductAdapter";

    public AllProductAdapter(Context context, List<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public AllProductAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_product_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllProductAdapter.ViewHolders holder, int position) {
        ProductModel item = productList.get(position);
        holder.name.setText(item.getName());
        holder.weight.setText(item.getPer() + " " + item.getUnitName());
        holder.price.setText(item.getFinal_price().toString());
        Glide.with(context).load( item.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra(context.getString(R.string.intent_key_product_model), item);
                context.startActivity(intent);
            }
        });

        holder.add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(holder.total_product_quantity.getText().toString());
                holder.total_product_quantity.setText(String.valueOf(currentQuantity + 1));
            }
        });

        holder.remove_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(holder.total_product_quantity.getText().toString());
                if (currentQuantity > 1) {
                    holder.total_product_quantity.setText(String.valueOf(currentQuantity - 1));
                }
            }
        });

        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.total_product_quantity.getText().toString());
                int amount = item.getPrice();
                Addtocart(holder, item.getId(), amount, quantity);
                holder.btn_add_to_cart.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {

        ImageView image, add_product, remove_product;
        TextView name, weight, price, total_product_quantity;
        AppCompatButton btn_add_to_cart;
        private ProgressBar progressBar;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.product_image);
            name = itemView.findViewById(R.id.product_name);
            weight = itemView.findViewById(R.id.product_weight);
            price = itemView.findViewById(R.id.product_price);
            add_product = itemView.findViewById(R.id.add_product_quantity);
            remove_product = itemView.findViewById(R.id.remove_product_quantity);
            total_product_quantity = itemView.findViewById(R.id.total_product_quantity);
            btn_add_to_cart = itemView.findViewById(R.id.btn_add_to_cart);
            progressBar = itemView.findViewById(R.id.loader_cart_item);
        }
    }

    private void Addtocart(ViewHolders holder, int id, int amount, int quantity) {
        UserActivitySession userActivitySession = new UserActivitySession(context);
        Call<AddToCartResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(CartInterface.class).addToCart(id, quantity);
        call.enqueue(new Callback<AddToCartResponse>() {
            @Override
            public void onResponse(Call<AddToCartResponse> call, Response<AddToCartResponse> response) {
                if (response.isSuccessful()) {
                    holder.total_product_quantity.setText(context.getString(R.string.min_product_quantity));
                    holder.btn_add_to_cart.setVisibility(View.VISIBLE);
                    holder.progressBar.setVisibility(View.GONE);
                    AddToCartResponse addToCartResponse = response.body();
                    showToast(context, addToCartResponse.getMessage());
                } else {
                    handleApiError(TAG, response, context);
                }
            }

            @Override
            public void onFailure(Call<AddToCartResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
