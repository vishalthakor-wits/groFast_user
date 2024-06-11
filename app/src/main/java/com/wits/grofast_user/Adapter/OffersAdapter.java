package com.wits.grofast_user.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_user.MainHomePage.ShowAllOffer;
import com.wits.grofast_user.R;

import java.util.List;
import java.util.Map;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolders> {
    private List<Map<String, Object>> alloffersItem;
    private Context context;

    public OffersAdapter(Context context, List<Map<String, Object>> alloffersItem) {
        this.context = context;
        this.alloffersItem = alloffersItem;
    }

    @NonNull
    @Override
    public OffersAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_offers_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OffersAdapter.ViewHolders holder, int position) {
        Map<String, Object> item = alloffersItem.get(position);
        holder.name.setText((String) item.get("Name"));
        holder.subname.setText((String) item.get("SubName"));

        List<Map<String, Object>> innerItems = (List<Map<String, Object>>) item.get("InnerData");
        InnerOfferAdapter allInnerOfferAdapter = new InnerOfferAdapter(context, innerItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(allInnerOfferAdapter);
        holder.recyclerView.setTag(position);

        holder.viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context.getApplicationContext(), ShowAllOffer.class);
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alloffersItem.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        TextView name, subname, viewall;
        ImageView image;
        RecyclerView recyclerView;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.all_offers_name);
            subname = itemView.findViewById(R.id.all_offers_subname);
            viewall = itemView.findViewById(R.id.all_offers_view_all);
            image = itemView.findViewById(R.id.all_offers_image);
            recyclerView = itemView.findViewById(R.id.show_offers_recycleview);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
        }
    }
}
