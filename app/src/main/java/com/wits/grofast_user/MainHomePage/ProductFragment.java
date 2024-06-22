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
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
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
import com.wits.grofast_user.Enums.ProductSearchEnum;
import com.wits.grofast_user.KeyboardUtil;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.UserActivitySession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {

    RecyclerView recyclerView, bannerrecycleview;
    AllProductAdapter allProductAdapter;
    private List<ProductModel> productList = new ArrayList<>();
    private List<ProductModel> productListByName = new ArrayList<>();
    private List<ProductModel> productListByCategory = new ArrayList<>();
    private GridLayoutManager layoutManager;
    ScrollView show_data;
    private final String TAG = "ProductFragment";
    private UserActivitySession userActivitySession;
    AppCompatButton completeorderbtn;
    private ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout no_product_layout;
    TextView no_product_text, no_product_text2;
    private SearchView searchView;
    private ImageView searchIcon;
    private int currentBannerPosition = 0;
    private Handler handler = new Handler();

    BannerAdapter bannerAdapter;

    private boolean isLoading = false;
    private int currentPageSearchAll = 1;
    private int lastPageSearchAll = 1;

    private int currentPageSearchByName = 1;
    private int lastPageSearchByName = 1;

    private int currentPageSearchByCategory = 1;
    private int lastPageSearchByCategory = 1;

    private int visibleThreshold = 4;
    private final int apiDelay = 2000;

    private int searchIndicator;
    private String searchName, searchCategory;
    private ProgressBar recyclerProgressBar;

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
        recyclerProgressBar = root.findViewById(R.id.centerProgressBar);

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

        allProductAdapter = new AllProductAdapter(getContext(), productList);
        recyclerView.setAdapter(allProductAdapter);

        Bundle bundle = getArguments();
        searchIndicator = userActivitySession.getProductSearchIndicator();

        if (searchIndicator == ProductSearchEnum.searchByName.getValue()) {
            searchName = userActivitySession.getetSearchProductName();
            searchProducts(searchName, currentPageSearchByName);
//            searchName = bundle != null ? bundle.getString(getString(R.string.intent_key_product_name)) : null;
        } else if (searchIndicator == ProductSearchEnum.searchByCategory.getValue()) {
            searchCategory = userActivitySession.getSearchCategoryName();
            getProductByCategory(searchCategory, currentPageSearchByCategory);
//            searchCategory = bundle != null ? bundle.getString(getString(R.string.intent_key_category_name)) : null;
        } else {
            getProducts(currentPageSearchAll);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                userActivitySession.setProductSearchIndicator(ProductSearchEnum.searchByName.getValue());
                userActivitySession.setSearchProductName(query);
                searchIndicator = userActivitySession.getProductSearchIndicator();
                searchName = query;
                searchProducts(query, currentPageSearchByName);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    showAllProducts();
                }
                searchName = newText;
                return false;
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userActivitySession.setProductSearchIndicator(ProductSearchEnum.searchByName.getValue());
                userActivitySession.setSearchProductName(searchName);
                searchIndicator = userActivitySession.getProductSearchIndicator();
                searchProducts(searchName, currentPageSearchByName);
            }
        });

        final View rootLayout = root.findViewById(R.id.fram_product);
        KeyboardUtil.setKeyboardVisibilityListener(rootLayout, new KeyboardUtil.KeyboardVisibilityListener() {
            @Override
            public void onKeyboardVisibilityChanged(boolean isVisible) {
                if (isVisible) {
                    completeorderbtn.setVisibility(GONE);
                } else {
                    completeorderbtn.setVisibility(View.VISIBLE);
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();


                if (!isLoading && totalItemCount < lastVisibleItem + visibleThreshold) {
                    Log.e("TAG", "onScrolled: firstVisibleItem : " + firstVisibleItemPosition);
                    Log.e("TAG", "onScrolled: lastVisibleItem : " + lastVisibleItem);
                    Log.e("TAG", "onScrolled:  totalItemCount : " + totalItemCount);
                    Log.e("TAG", "onScrolled: lastVisibleItem + visibleThreshold : " + (lastVisibleItem + visibleThreshold));
//                    Log.e("TAG", "onScrolled: current page " + currentPage);

                    isLoading = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isLoading = false;
                        }
                    }, apiDelay);

                    if (searchIndicator == ProductSearchEnum.searchByName.getValue()) {
                        searchProducts(searchName, currentPageSearchByName);
                        Log.e(TAG, "onScrolled: fetching products by name");
                    } else if (searchIndicator == ProductSearchEnum.searchByCategory.getValue()) {
                        getProductByCategory(searchCategory, currentPageSearchByCategory);
                        Log.e(TAG, "onScrolled: fetching products by category");
                    } else {
                        getProducts(currentPageSearchAll);
                        Log.e(TAG, "onScrolled: fetching all products");
                    }

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
        Log.e("TAG", "getProducts:     last page  " + lastPageSearchAll);
        Log.e("TAG", "getProducts: current page  " + currentPageSearchAll);
        Call<ProductResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(ProductInerface.class).fetchProducts(currentPageSearchAll);

        if (lastPageSearchAll >= page) {
        call.enqueue(new Callback<ProductResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                HidePageLoader();
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    ProductPaginatedRes paginatedResponse = productResponse.getPaginatedProducts();
                    if (page == 1) {
                        productList = paginatedResponse.getProductList();
                        allProductAdapter = new AllProductAdapter(getContext(), productList);
                        recyclerView.setAdapter(allProductAdapter);
                    } else {
                        List<ProductModel> list = paginatedResponse.getProductList();
                        for (ProductModel model : list) {
                            productList.add(model);
                            allProductAdapter.notifyItemInserted(productList.size());
                        }
                    }
                    currentPageSearchAll++;
                    lastPageSearchAll = paginatedResponse.getLast_page();
                } else {
                    if (page <= 1) handleApiError(TAG, response, getContext());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                isLoading = false;
                HidePageLoader();
                t.printStackTrace();
            }
        });
        }
    }

    private void getProductByCategory(String category, int page) {
        Call<ProductResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(ProductInerface.class).fetchProductsByCategory(page, category);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                HidePageLoader();
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    ProductPaginatedRes paginatedResponse = productResponse.getPaginatedProducts();
                    productListByCategory = paginatedResponse.getProductList();

                    allProductAdapter = new AllProductAdapter(getContext(), productListByCategory);
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
                    if (page <= 1) handleApiError(TAG, response, getContext());
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
        show_data.setVisibility(GONE);
        completeorderbtn.setVisibility(GONE);
        no_product_layout.setVisibility(View.VISIBLE);
        no_product_text2.setText(errorMessage);
        no_product_text.setText(message);
    }

    private void ShowPageLoader() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        show_data.setVisibility(GONE);
        completeorderbtn.setVisibility(GONE);
    }

    private void HidePageLoader() {
        shimmerFrameLayout.setVisibility(GONE);
        shimmerFrameLayout.stopShimmer();
        show_data.setVisibility(View.VISIBLE);
        completeorderbtn.setVisibility(View.VISIBLE);
    }

    private void searchProducts(String searchParameter, int page) {
        Call<ProductResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(ProductInerface.class).searchProduct(searchParameter, page);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                HidePageLoader();
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();
                    ProductPaginatedRes paginatedResponse = productResponse.getPaginatedProducts();
                    productListByName = paginatedResponse.getProductList();

                    allProductAdapter = new AllProductAdapter(getContext(), productListByName);
                    recyclerView.setAdapter(allProductAdapter);
                } else {
                    if (page <= 1) handleApiError(TAG, response, getContext());
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

    private void startProgress() {
        recyclerProgressBar.setVisibility(View.VISIBLE);
    }

    private void stopProgress() {
        recyclerProgressBar.setVisibility(GONE);
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
        userActivitySession.setProductSearchIndicator(ProductSearchEnum.searchAll.getValue());
        userActivitySession.setSearchProductName(null);
        userActivitySession.setSearchCategoryName(null);
    }
}