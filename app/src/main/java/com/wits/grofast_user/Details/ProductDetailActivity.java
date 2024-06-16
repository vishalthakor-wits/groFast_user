package com.wits.grofast_user.Details;

import static com.wits.grofast_user.CommonUtilities.handleApiError;
import static com.wits.grofast_user.CommonUtilities.showToast;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.wits.grofast_user.Adapter.RelatedProductAdapter;
import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.interfaces.CartInterface;
import com.wits.grofast_user.Api.interfaces.ProductInerface;
import com.wits.grofast_user.Api.responseClasses.AddToCartResponse;
import com.wits.grofast_user.Api.responseClasses.RelatedProductsResponse;
import com.wits.grofast_user.Api.responseModels.ProductModel;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.UserActivitySession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView productImage, removeproductquantity, addproductquantity;
    private TextView productName, productWeight, productPrice, productdescription, totalqunatity;
    RecyclerView recyclerView;
    RelatedProductAdapter relatedProductAdapter;
    private List<ProductModel> relatedProducts=new ArrayList<>();
    AppCompatButton add_to_cart;
    ProgressBar progressBar;
    private final String TAG = "ProductDetailActivity";
    private UserActivitySession userActivitySession;
    private int categoryId,productId;
    private ShimmerFrameLayout shimmerRelatedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.product_details_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_product_detail);

        userActivitySession=new UserActivitySession(getApplicationContext());
        productImage = findViewById(R.id.product_detail_image);
        productName = findViewById(R.id.product_detail_name);
        productWeight = findViewById(R.id.product_detail_weight);
        productPrice = findViewById(R.id.product_detail_price);
        productdescription = findViewById(R.id.product_detail_description);
        recyclerView = findViewById(R.id.product_related_item_recycleview);
        removeproductquantity = findViewById(R.id.remove_product_detail_quantity);
        addproductquantity = findViewById(R.id.add_product_detail_quantity);
        totalqunatity = findViewById(R.id.total_product_detail_quantity);
        add_to_cart = findViewById(R.id.product_detail_add_to_cart);
        progressBar = findViewById(R.id.loader_product_cart_item);
        shimmerRelatedProducts = findViewById(R.id.related_products_shimmer_layout);


        productId = getIntent().getIntExtra("ProductId", 0);

        if (productId != 0) {
            Log.e("TAG", "Product ID: " + productId);
        } else {
            Log.e("TAG", "No Product ID passed in the intent");
        }

        if (getIntent() != null) {
            categoryId=getIntent().getIntExtra("categoryId",0);
            productName.setText(getIntent().getStringExtra("Name"));
            productWeight.setText(getIntent().getStringExtra("Weight"));
            productPrice.setText(getIntent().getStringExtra("Price"));
            productdescription.setText(getIntent().getStringExtra("Description"));
            totalqunatity.setText(getIntent().getStringExtra("quantity"));
            Glide.with(getApplicationContext()).load(getIntent().getStringExtra("image")).placeholder(R.drawable.gobhi_image).into(productImage);
        }

        //Related Product Item
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        loadRelatedProducts(categoryId,productId);

        addproductquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(totalqunatity.getText().toString());
                totalqunatity.setText(String.valueOf(currentQuantity + 1));
            }
        });

        removeproductquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(totalqunatity.getText().toString());
                if (currentQuantity > 1) {
                    totalqunatity.setText(String.valueOf(currentQuantity - 1));
                }
            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(totalqunatity.getText().toString());
                int amount = Integer.parseInt(productPrice.getText().toString());
                Addtocart(productId, amount, quantity);
                add_to_cart.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void Addtocart(int id, int amount, int quantity) {
        UserActivitySession userActivitySession = new UserActivitySession(getApplicationContext());
        Call<AddToCartResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(CartInterface.class).addToCart(id,quantity);
        call.enqueue(new Callback<AddToCartResponse>() {
            @Override
            public void onResponse(Call<AddToCartResponse> call, Response<AddToCartResponse> response) {
                if (response.isSuccessful()) {
                    add_to_cart.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    AddToCartResponse cartResponse = response.body();
                    Log.e("Addtocart", "Product added to cart message :" + cartResponse.getMessage());
                    Log.e("Addtocart", "Product added to cart id : " + id);
                    Log.e("Addtocart", "Product added to cart amount : " + amount);
                    Log.e("Addtocart", "Product added to cart quantity :" + quantity);
                    showToast(getApplicationContext(), cartResponse.getMessage());
                } else {
                    handleApiError(TAG, response, getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<AddToCartResponse> call, Throwable t) {

            }
        });
    }


    private void loadRelatedProducts(int categoryId,int productId){
        Call<RelatedProductsResponse> call=RetrofitService.getClient(userActivitySession.getToken()).create(ProductInerface.class).fetchRelatedProducts(categoryId,productId);

        call.enqueue(new Callback<RelatedProductsResponse>() {
            @Override
            public void onResponse(Call<RelatedProductsResponse> call, Response<RelatedProductsResponse> response) {
                stopShimmer();
                if(response.isSuccessful()){
                    RelatedProductsResponse relatedProductsResponse=response.body();
                    relatedProducts=relatedProductsResponse.getProductList();

                    relatedProductAdapter = new RelatedProductAdapter(getApplicationContext(), relatedProducts);
                    recyclerView.setAdapter(relatedProductAdapter);
                }else handleApiError(TAG,response,getApplicationContext());
            }

            @Override
            public void onFailure(Call<RelatedProductsResponse> call, Throwable t) {
                t.printStackTrace();
                stopShimmer();
            }
        });
    }

    private void stopShimmer() {
        shimmerRelatedProducts.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
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