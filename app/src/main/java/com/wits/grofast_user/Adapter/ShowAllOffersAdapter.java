package com.wits.grofast_user.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_user.Details.OfferDetails;
import com.wits.grofast_user.R;

import java.util.List;
import java.util.Map;

public class ShowAllOffersAdapter extends RecyclerView.Adapter<ShowAllOffersAdapter.ViewHolders> {
    private List<Map<String, Object>> alloffersItem;
    private Context context;

    public ShowAllOffersAdapter(Context context, List<Map<String, Object>> alloffersItem) {
        this.context = context;
        this.alloffersItem = alloffersItem;
    }

    @NonNull
    @Override
    public ShowAllOffersAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.show_all_offers_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShowAllOffersAdapter.ViewHolders holder, int position) {
        Map<String, Object> item = alloffersItem.get(position);
        holder.imageView.setImageResource((int) item.get("image"));

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context.getApplicationContext(), OfferDetails.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alloffersItem.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.show_all_offer_image);
        }
    }
}
