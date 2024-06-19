package com.wits.grofast_user.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_user.R;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolders> {
    private List<Integer> bannerImages;
    private Context context;

    public BannerAdapter(Context context, List<Integer> bannerImages) {
        this.context = context;
        this.bannerImages = bannerImages;
    }

    @NonNull
    @Override
    public BannerAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_banner_layout, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull BannerAdapter.ViewHolders holder, int position) {
        int bannerImage = bannerImages.get(position);
        holder.imageView.setImageResource(bannerImage);
    }

    @Override
    public int getItemCount() {
        return bannerImages.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.banner_image);
        }
    }
}
