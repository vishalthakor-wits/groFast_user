package com.wits.grofast_user.MainHomePage;

import static android.view.View.GONE;
import static com.wits.grofast_user.CommonUtilities.handleApiError;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wits.grofast_user.Adapter.AllProductAdapter;
import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.interfaces.ProductInerface;
import com.wits.grofast_user.Api.paginatedResponses.ProductPaginatedRes;
import com.wits.grofast_user.Api.responseClasses.ProductResponse;
import com.wits.grofast_user.Api.responseModels.ProductModel;
import com.wits.grofast_user.KeyboardUtil;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.UserActivitySession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {
    private static final String ARG_CATEGORY_NAME = "categoryName";

    public static ProductFragment newInstance(String categoryName) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_NAME, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    private boolean isCategoriesProductLoaded = false;
    private boolean isProductsLoaded = false;
    RecyclerView recyclerView;
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

        searchView = root.findViewById(R.id.product_search_view);
        searchIcon = root.findViewById(R.id.product_search_icon);

        ShowPageLoader();

        //Product Item
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        if (getArguments() != null) {
            searchQuery = getArguments().getString("searchParameter");
            String category = getArguments().getString(ARG_CATEGORY_NAME);

            if (category != null) {
                getProductByCategory(category);
            } else {
                searchView.setQuery(searchQuery, false);
                searchProducts(searchQuery, 1);
            }

            Log.e(TAG, "onCreateView: category " + category);
            Log.e(TAG, "onCreateView: searchQuery " + searchQuery);

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

        completeorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentnav, new CartFragment());
                transaction.addToBackStack(null);
                transaction.commit();
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

                    Log.i(TAG, "onResponse: getProducts message " + productResponse.getMessage());
                    Log.i(TAG, "onResponse: total products " + paginatedResponse.getTotal());
                    Log.i(TAG, "onResponse: fetched products " + paginatedResponse.getTo());
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

                    Log.i(TAG, "onResponse: getProductByCategory message " + productResponse.getMessage());
                    Log.i(TAG, "onResponse: total products " + paginatedResponse.getTotal());
                    Log.i(TAG, "onResponse: fetched products " + paginatedResponse.getTo());
                    Log.e(TAG, "onResponse: fragment Show all Product");
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

                    Log.i(TAG, "onResponse: getProductByCategory message " + productResponse.getMessage());
                    Log.i(TAG, "onResponse: total products " + paginatedResponse.getTotal());
                    Log.i(TAG, "onResponse: fetched products " + paginatedResponse.getTo());
                    Log.e(TAG, "onResponse: fragment Show all Product");
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
    public void onStop() {
        super.onStop();
        userActivitySession.setProductFetchIndicator(0);
    }
}