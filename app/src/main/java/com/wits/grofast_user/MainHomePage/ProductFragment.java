package com.wits.grofast_user.MainHomePage;

import static android.view.View.GONE;
import static com.wits.grofast_user.CommonUtilities.handleApiError;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wits.grofast_user.Adapter.AllProductAdapter;
import com.wits.grofast_user.Adapter.BannerAdapter;
import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.interfaces.BannerInterface;
import com.wits.grofast_user.Api.interfaces.ProductInerface;
import com.wits.grofast_user.Api.paginatedResponses.ProductPaginatedRes;
import com.wits.grofast_user.Api.responseClasses.BannerResponse;
import com.wits.grofast_user.Api.responseClasses.ProductResponse;
import com.wits.grofast_user.Api.responseModels.BannerModel;
import com.wits.grofast_user.Api.responseModels.ProductModel;
import com.wits.grofast_user.KeyboardUtil;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.UserActivitySession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {

    private boolean isCategoriesProductLoaded = false;
    private boolean isProductsLoaded = false;
    RecyclerView recyclerView, bannerrecycleview;
    AllProductAdapter allProductAdapter;
    private List<ProductModel> productList = new ArrayList<>();
    private List<ProductModel> searchProductList = new ArrayList<>();
    private GridLayoutManager layoutManager;
    NestedScrollView show_data;
    private final String TAG = "ProductFragment";
    private UserActivitySession userActivitySession;
    AppCompatButton completeorderbtn;
    private ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout no_product_layout;
    TextView no_product_text, no_product_text2;
    private SearchView searchView;
    private ImageView searchIcon;
    private String searchQuery;
    private int currentBannerPosition = 0;
    private Handler handler = new Handler();
    BannerAdapter bannerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_product, container, false);

        if (getActivity() instanceof HomePage) {
            ((HomePage) getActivity()).updateActionBar(getString(R.string.bottom_menu_product), 0, 0);
        }

        userActivitySession = new UserActivitySession(getContext());
        recyclerView = root.findViewById(R.id.all_product_recycleview);
        shimmerFrameLayout = root.findViewById(R.id.shimmer_layout_product);
        show_data = root.findViewById(R.id.show_product_data);
        completeorderbtn = root.findViewById(R.id.complete_order_btn);
        no_product_layout = root.findViewById(R.id.no_product_layout);
        no_product_text = root.findViewById(R.id.no_product_text1);
        no_product_text2 = root.findViewById(R.id.no_product_text2);

        //Banner Recycleview
        bannerrecycleview = root.findViewById(R.id.product_page_banner_recycleview);
        bannerrecycleview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        getbanner();

        searchView = root.findViewById(R.id.product_search_view);
        searchIcon = root.findViewById(R.id.product_search_icon);

        ShowPageLoader();

        //Product Item
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        if (getArguments() != null) {
            searchQuery = getArguments().getString("searchParameter");
            String category = getArguments().getString(getString(R.string.intent_key_category_name));

            if (category != null) {
                getProductByCategory(category);
            } else {
                searchView.setQuery(searchQuery, false);
                searchProducts(searchQuery, 1);
            }
        } else {
            getProducts(1);
        }

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProducts(query, 1);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    showAllProducts();
                }
                searchQuery = newText;
                return false;
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProducts(searchQuery, 1);
            }
        });

        final View rootLayout = root.findViewById(R.id.fram_product);
        KeyboardUtil.setKeyboardVisibilityListener(rootLayout, new KeyboardUtil.KeyboardVisibilityListener() {
            @Override
            public void onKeyboardVisibilityChanged(boolean isVisible) {
                if (isVisible) {
                    completeorderbtn.setVisibility(View.GONE);
                } else {
                    completeorderbtn.setVisibility(View.VISIBLE);
                }
            }
        });


        return root;
    }

    private void getbanner() {
        Call<BannerResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(BannerInterface.class).fetchbanner();
        call.enqueue(new Callback<BannerResponse>() {
            @Override
            public void onResponse(Call<BannerResponse> call, Response<BannerResponse> response) {
                if (response.isSuccessful()) {
                    List<BannerModel> banners = response.body().getBanners();
                    bannerAdapter = new BannerAdapter(getContext(), banners);
                    bannerrecycleview.setAdapter(bannerAdapter);
                    bannerAdapter.notifyDataSetChanged();
                    startAutoScroll();
                } else {
                    handleApiError(TAG, response, getContext());
                }
            }

            @Override
            public void onFailure(Call<BannerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void startAutoScroll() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentBannerPosition == bannerAdapter.getItemCount()) {
                    currentBannerPosition = 0;
                }
                bannerrecycleview.smoothScrollToPosition(currentBannerPosition++);
                handler.postDelayed(this, 2000);
            }
        }, 2000);
    }

    private void getProducts(int page) {
        Call<ProductResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(ProductInerface.class).fetchProducts(page);
        call.enqueue(new Callback<ProductResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    ProductPaginatedRes paginatedResponse = productResponse.getPaginatedProducts();
                    productList = paginatedResponse.getProductList();

                    allProductAdapter = new AllProductAdapter(getContext(), productList);
                    recyclerView.setAdapter(allProductAdapter);
                    HidePageLoader();
                } else {
                    if (isAdded()) handleApiError(TAG, response, getContext());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getProductByCategory(String category) {
        Call<ProductResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(ProductInerface.class).fetchProductsByCategory(1, category);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                HidePageLoader();
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    ProductPaginatedRes paginatedResponse = productResponse.getPaginatedProducts();
                    productList = paginatedResponse.getProductList();

                    allProductAdapter = new AllProductAdapter(getContext(), productList);
                    recyclerView.setAdapter(allProductAdapter);
                } else if (response.code() == 422) {
                    try {
                        String errorBodyString = response.errorBody().string();
                        Gson gson = new Gson();
                        JsonObject errorBodyJson = gson.fromJson(errorBodyString, JsonObject.class);

                        String errorMessage = errorBodyJson.has("errorMessage") ? errorBodyJson.get("errorMessage").getAsString() : "No errorMessage";
                        String message = errorBodyJson.has("message") ? errorBodyJson.get("message").getAsString() : "No message";

                        showNoProductMessage(message, errorMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    handleApiError(TAG, response, getContext());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                HidePageLoader();
                t.printStackTrace();
            }
        });
    }

    private void showNoProductMessage(String message, String errorMessage) {
        show_data.setVisibility(View.GONE);
        completeorderbtn.setVisibility(GONE);
        no_product_layout.setVisibility(View.VISIBLE);
        no_product_text2.setText(errorMessage);
        no_product_text.setText(message);
    }

    private void ShowPageLoader() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        show_data.setVisibility(View.GONE);
        completeorderbtn.setVisibility(GONE);
    }

    private void HidePageLoader() {
        shimmerFrameLayout.setVisibility(View.GONE);
        shimmerFrameLayout.stopShimmer();
        show_data.setVisibility(View.VISIBLE);
        completeorderbtn.setVisibility(View.VISIBLE);
    }

    private void searchProducts(String searchParameter, int page) {
        ShowPageLoader();
        Call<ProductResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(ProductInerface.class).searchProduct(searchParameter, page);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                HidePageLoader();
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    ProductPaginatedRes paginatedResponse = productResponse.getPaginatedProducts();
                    searchProductList = paginatedResponse.getProductList();

                    allProductAdapter = new AllProductAdapter(getContext(), searchProductList);
                    recyclerView.setAdapter(allProductAdapter);
                } else {
                    handleApiError(TAG, response, getContext());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                HidePageLoader();
            }
        });
    }

    private void showAllProducts() {
        if (!productList.isEmpty()) {
            allProductAdapter = new AllProductAdapter(getContext(), productList);
            recyclerView.setAdapter(allProductAdapter);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        completeorderbtn = view.findViewById(R.id.complete_order_btn);
        completeorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FragmentActivity activity = getActivity();
                    if (activity != null) {
                        View fragmentNavView = activity.findViewById(R.id.fragmentnav);
                        if (fragmentNavView != null) {
                            Log.d(TAG, "fragmentnav view found: " + fragmentNavView.toString());
                            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentnav, new CartFragment());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else {
                            Log.e(TAG, "fragmentnav view not found");
                        }
                    } else {
                        Log.e(TAG, "Activity is null, cannot perform fragment transaction");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onClick: completeorderbtn " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        searchView.setQuery("", false);
        userActivitySession.setProductFetchIndicator(0);
    }
}