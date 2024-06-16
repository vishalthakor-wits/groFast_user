package com.wits.grofast_user.MainHomePage;

import static com.wits.grofast_user.CommonUtilities.handleApiError;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.wits.grofast_user.Adapter.HomeViewProductAdapter;
import com.wits.grofast_user.Adapter.TopCategoriesAdapter;
import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.interfaces.CategoryInterface;
import com.wits.grofast_user.Api.interfaces.ProductInerface;
import com.wits.grofast_user.Api.responseClasses.HomeCategoryResponse;
import com.wits.grofast_user.Api.responseClasses.HomeProductResponse;
import com.wits.grofast_user.Api.responseModels.HomeCategoryModel;
import com.wits.grofast_user.Api.responseModels.HomeProductModel;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.UserActivitySession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    RecyclerView top_stores_recycleview, product_recycleview;
    TopCategoriesAdapter topStoreAdapter;
    HomeViewProductAdapter productAdapter;
    private List<HomeCategoryModel> categoryList = new ArrayList<>();
    private List<HomeProductModel> productList = new ArrayList<>();
    private GridLayoutManager layoutManager;
    private ShimmerFrameLayout shimmerFrameLayout;
    TextView view_all_categories, view_all_product;
    NestedScrollView layoutcategories;
    private boolean isCategoriesLoaded = false;
    private boolean isProductsLoaded = false;
    private final String TAG = "HomeFragment";
    private UserActivitySession userActivitySession;

    private ImageView searchIcon;
    private String searchQuery;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        userActivitySession = new UserActivitySession(getContext());
        top_stores_recycleview = root.findViewById(R.id.recycleview_top_stores);
        product_recycleview = root.findViewById(R.id.product_recycleview);

        view_all_categories = root.findViewById(R.id.view_all_categories_homepage);
        view_all_product = root.findViewById(R.id.view_all_product);

        layoutcategories = root.findViewById(R.id.layout_top_categories);
        shimmerFrameLayout = root.findViewById(R.id.shimmer_layout_home);

        searchView = root.findViewById(R.id.home_product_search_view);
        searchIcon = root.findViewById(R.id.home_product_search_icon);

        ShowPageLoader();

        //Top Stores Item
        layoutManager = new GridLayoutManager(getContext(), 4);
        top_stores_recycleview.setLayoutManager(layoutManager);
        getHomeCategories();

        //Product Item
        layoutManager = new GridLayoutManager(getContext(), 2);
        product_recycleview.setLayoutManager(layoutManager);
        getHomeProducts();

        view_all_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), ShowAllCategories.class);
                startActivity(in);
            }
        });

        view_all_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ProductFragment());
            }
        });

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProductsOnProductFragment(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                return false;
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProductsOnProductFragment(searchQuery);
            }
        });

        return root;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentnav, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getHomeCategories() {
        Call<HomeCategoryResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(CategoryInterface.class).fetchHomeCategories();

        call.enqueue(new Callback<HomeCategoryResponse>() {
            @Override
            public void onResponse(Call<HomeCategoryResponse> call, Response<HomeCategoryResponse> response) {
                if (response.isSuccessful()) {
                    HomeCategoryResponse homeCategoryResponse = response.body();
                    categoryList = homeCategoryResponse.getCategoryList();
                    topStoreAdapter = new TopCategoriesAdapter(categoryList, getContext(), getFragmentManager());
                    top_stores_recycleview.setAdapter(topStoreAdapter);

                    Log.i(TAG, "onResponse: total categories " + categoryList.size());
                } else {
                    if (isAdded()) handleApiError(TAG, response, getContext());
                }
                isCategoriesLoaded = true;
                checkIfDataLoaded();
            }

            @Override
            public void onFailure(Call<HomeCategoryResponse> call, Throwable t) {
                t.printStackTrace();
                isCategoriesLoaded = true;
            }
        });
    }

    private void getHomeProducts() {
        Call<HomeProductResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(ProductInerface.class).fetchHomeProducts();

        call.enqueue(new Callback<HomeProductResponse>() {
            @Override
            public void onResponse(Call<HomeProductResponse> call, Response<HomeProductResponse> response) {
                if (response.isSuccessful()) {
                    HomeProductResponse homeProductResponse = response.body();
                    productList = homeProductResponse.getProductList();

                    productAdapter = new HomeViewProductAdapter(productList, getContext());
                    product_recycleview.setAdapter(productAdapter);

                } else {
                    if (isAdded()) handleApiError(TAG, response, getContext());
                }
                isProductsLoaded = true;
                checkIfDataLoaded();
            }

            @Override
            public void onFailure(Call<HomeProductResponse> call, Throwable t) {
                t.printStackTrace();
                isProductsLoaded = true;
            }
        });
    }

    private void ShowPageLoader() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        layoutcategories.setVisibility(View.GONE);
    }

    private void HidePageLoader() {
        shimmerFrameLayout.setVisibility(View.GONE);
        shimmerFrameLayout.stopShimmer();
        layoutcategories.setVisibility(View.VISIBLE);
    }

    private void checkIfDataLoaded() {
        if (isCategoriesLoaded && isProductsLoaded) {
            HidePageLoader();
        }
    }

    private void searchProductsOnProductFragment(String searchParameter) {
        ProductFragment productFragment = new ProductFragment();

        Bundle bundle = new Bundle();
        bundle.putString("searchParameter", searchParameter);
        productFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction().replace(R.id.fragmentnav, productFragment).addToBackStack(null).commit();
    }

    @Override
    public void onStop() {
        super.onStop();
        searchView.setQuery("",false);
    }
}