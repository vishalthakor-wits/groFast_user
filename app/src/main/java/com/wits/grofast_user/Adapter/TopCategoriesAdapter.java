package com.wits.grofast_user.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wits.grofast_user.Api.responseModels.CategoryModel;
import com.wits.grofast_user.Api.responseModels.HomeCategoryModel;
import com.wits.grofast_user.Enums.ProductSearchEnum;
import com.wits.grofast_user.MainHomePage.ProductFragment;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.UserActivitySession;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopCategoriesAdapter extends RecyclerView.Adapter<TopCategoriesAdapter.ViewHolders> {
    private final String TAG = "TopCategoriesAdapter";
    private List<HomeCategoryModel> categoryList;
    private Context context;
    private FragmentManager fragmentManager;

    public TopCategoriesAdapter(List<HomeCategoryModel> categoryList, Context context, FragmentManager fragmentManager) {
        this.categoryList = categoryList;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public TopCategoriesAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.top_categories_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopCategoriesAdapter.ViewHolders holder, int position) {
        UserActivitySession userActivitySession = new UserActivitySession(context);
        CategoryModel item = categoryList.get(position).getHomeCategory();
        holder.Name.setText(item.getCategory_name());
        Glide.with(context).load(item.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.Banner);

        holder.Banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userActivitySession.setProductSearchIndicator(ProductSearchEnum.searchByCategory.getValue());
                userActivitySession.setSearchCategoryName(item.getCategory_name());
                openProducatFragment(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        TextView Name;
        CircleImageView Banner;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.top_name);
            Banner = itemView.findViewById(R.id.top_image);
        }
    }

    private void openProducatFragment(CategoryModel item) {
        Bundle bundle = new Bundle();
        bundle.putString(context.getString(R.string.intent_key_category_name), item.getCategory_name());

        // Create an instance of the second fragment
        ProductFragment productFragment = new ProductFragment();
        productFragment.setArguments(bundle);

        // Replace the current fragment with the second fragment
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentnav, productFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
