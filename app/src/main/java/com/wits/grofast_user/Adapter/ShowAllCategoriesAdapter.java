package com.wits.grofast_user.Adapter;

import static com.wits.grofast_user.Api.RetrofitService.domain;

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
import com.wits.grofast_user.MainHomePage.ProductFragment;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.UserActivitySession;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowAllCategoriesAdapter extends RecyclerView.Adapter<ShowAllCategoriesAdapter.ViewHolders> {
    private final String TAG = "ShowAllCategoriesAdapter";
    private List<CategoryModel> categoryList;
    private Context context;
    private FragmentManager fragmentManager;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(CategoryModel category);
    }

    public ShowAllCategoriesAdapter(List<CategoryModel> categoryList, Context context, FragmentManager fragmentManager, OnCategoryClickListener listener) {
        this.categoryList = categoryList;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShowAllCategoriesAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_categories_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShowAllCategoriesAdapter.ViewHolders holder, int position) {
        UserActivitySession userActivitySession = new UserActivitySession(context);
        CategoryModel item = categoryList.get(position);
        holder.Name.setText(item.getCategory_name());
        Glide.with(context).load(domain + item.getImage()).placeholder(R.color.default_color).into(holder.Banner);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userActivitySession.setProductFetchIndicator(1);
                listener.onCategoryClick(item);
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
            Name = itemView.findViewById(R.id.all_categories_name);
            Banner = itemView.findViewById(R.id.all_categories_image);
        }
    }
}
