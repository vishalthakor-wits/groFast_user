package com.wits.grofastUser.Adapter;

import static com.wits.grofastUser.CommonUtilities.handleApiError;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wits.grofastUser.Api.RetrofitService;
import com.wits.grofastUser.Api.interfaces.CartInterface;
import com.wits.grofastUser.Api.responseClasses.AddToCartResponse;
import com.wits.grofastUser.Api.responseClasses.CartFetchResponse;
import com.wits.grofastUser.Api.responseModels.CartModel;
import com.wits.grofastUser.Api.responseModels.ProductModel;
import com.wits.grofastUser.Api.responseModels.TaxAndCharge;
import com.wits.grofastUser.Details.ProductDetailActivity;
import com.wits.grofastUser.MainHomePage.CartFragment;
import com.wits.grofastUser.R;
import com.wits.grofastUser.session.CartDetailSession;
import com.wits.grofastUser.session.UserActivitySession;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartResentAddProductAdapter extends RecyclerView.Adapter<CartResentAddProductAdapter.ViewHolders> {
    private List<CartModel> cartItems;
    private Context context;
    private RecyclerView recyclerView;
    private final String TAG = "CartResentAddProductAdapter";
    private UserActivitySession userActivitySession;
    private CartDetailSession cartDetailSession;
    private CartResentAddProductAdapter adapter;

    public CartResentAddProductAdapter(List<CartModel> cartItems, Context context, RecyclerView recyclerView) {
        this.cartItems = cartItems;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public CartResentAddProductAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_cart_product_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartResentAddProductAdapter.ViewHolders holder, int position) {
        userActivitySession = new UserActivitySession(context);
        cartDetailSession = new CartDetailSession(context);
        View.OnClickListener productDetailClicklistner;
        adapter = this;
        CartModel item = cartItems.get(position);
        ProductModel product = item.getProduct();

        if (product != null) {
            holder.product_name.setText(product.getName());
            Glide.with(context).load(product.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.product_image);
        }

        holder.product_price.setText(item.getAmount().toString());
        holder.totalquantity.setText(item.getQuantity().toString());

        productDetailClicklistner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                if (product != null)
                    intent.putExtra(context.getString(R.string.intent_key_product_model), product);
                context.startActivity(intent);
            }
        };

        holder.product_image.setOnClickListener(productDetailClicklistner);
        holder.product_name.setOnClickListener(productDetailClicklistner);
        holder.product_price.setOnClickListener(productDetailClicklistner);

        holder.addquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<AddToCartResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(CartInterface.class).addToCart(product.getId(), 1);
                CartFragment.startProgrtessBar();
                call.enqueue(new Callback<AddToCartResponse>() {
                    @Override
                    public void onResponse(Call<AddToCartResponse> call, Response<AddToCartResponse> response) {
                        if (response.isSuccessful()) {
                            loadCartItems(null);
                            Log.e(TAG, "onResponse: add to cart : message " + response.body().getMessage());
                        } else handleApiError(TAG, response, context);
                    }

                    @Override
                    public void onFailure(Call<AddToCartResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

        holder.removeqyantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCartItem(product.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        ImageView product_image, removeqyantity, addquantity;
        TextView product_name, product_price, totalquantity;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.cart_product_image);
            product_name = itemView.findViewById(R.id.cart_product_name);
            product_price = itemView.findViewById(R.id.cart_product_price);
            removeqyantity = itemView.findViewById(R.id.cart_remove_product_quantity);
            addquantity = itemView.findViewById(R.id.cart_add_product_quantity);
            totalquantity = itemView.findViewById(R.id.cart_total_product_quantity);
        }
    }

    private void loadCartItems(String aditionalNote) {
        Call<CartFetchResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(CartInterface.class).fetchCartDetails(Integer.parseInt(cartDetailSession.getTip()), cartDetailSession.getCoupon(), aditionalNote);
        call.enqueue(new Callback<CartFetchResponse>() {
            @Override
            public void onResponse(Call<CartFetchResponse> call, Response<CartFetchResponse> response) {
                CartFragment.stopProgrtessBar();
                if (response.isSuccessful()) {
                    CartFetchResponse cartFetchResponse = response.body();
                    cartItems = cartFetchResponse.getCartModelList();
                    List<TaxAndCharge> taxAndCharges = cartFetchResponse.getTaxAndCharges();
                    TaxesChargesAdapter taxesChargesAdapter = new TaxesChargesAdapter(context, taxAndCharges);

                    recyclerView.setAdapter(taxesChargesAdapter);
                    adapter.notifyDataSetChanged();

                    CartFragment.getSubTotalTextView().setText(cartFetchResponse.getSubtotal().toString());
                    CartFragment.getGrandTotalTotalTextView().setText(cartFetchResponse.getTotal().toString());

                    Log.e(TAG, "onResponse: loadCartItems message : " + cartFetchResponse.getMessage());

                    cartDetailSession.setTotalAmount(cartFetchResponse.getTotal());

                } else handleApiError(TAG, response, context);
            }

            @Override
            public void onFailure(Call<CartFetchResponse> call, Throwable t) {
                t.printStackTrace();
                CartFragment.stopProgrtessBar();
            }
        });
    }

    private void removeCartItem(int productId) {
        Call<AddToCartResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(CartInterface.class).removeCartItem(productId, 1);
        CartFragment.startProgrtessBar();
        call.enqueue(new Callback<AddToCartResponse>() {
            @Override
            public void onResponse(Call<AddToCartResponse> call, Response<AddToCartResponse> response) {
                if (response.isSuccessful()) {
                    loadCartItems(null);
                    Log.e(TAG, "onResponse: add to cart : message " + response.body().getMessage());
                }
                handleApiError(TAG, response, context);
            }

            @Override
            public void onFailure(Call<AddToCartResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
