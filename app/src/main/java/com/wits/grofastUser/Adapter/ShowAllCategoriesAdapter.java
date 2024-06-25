package com.wits.grofastUser.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wits.grofastUser.Api.responseModels.CategoryModel;
import com.wits.grofastUser.Enums.ProductSearchEnum;
import com.wits.grofastUser.MainHomePage.HomePage;
import com.wits.grofastUser.R;
import com.wits.grofastUser.session.UserActivitySession;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowAllCategoriesAdapter extends RecyclerView.Adapter<ShowAllCategoriesAdapter.ViewHolders> {
    private final String TAG = "ShowAllCategoriesAdapter";
    private List<CategoryModel> categoryList;
    private Context context;
    private FragmentManager fragmentManager;

    public ShowAllCategoriesAdapter(List<CategoryModel> categoryList, Context context, FragmentManager fragmentManager) {
        this.categoryList = categoryList;
        this.context = context;
        this.fragmentManager = fragmentManager;
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
        Glide.with(context).load( item.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.Banner);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userActivitySession.setProductSearchIndicator(ProductSearchEnum.searchByCategory.getValue());
                userActivitySession.setSearchCategoryName(item.getCategory_name());
                Intent intent = new Intent(context, HomePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(context.getString(R.string.intent_key_category_name), item.getCategory_name());
                context.startActivity(intent);
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
