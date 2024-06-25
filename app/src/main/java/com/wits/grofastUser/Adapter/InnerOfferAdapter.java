package com.wits.grofastUser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofastUser.Details.OfferDetails;
import com.wits.grofastUser.R;

import java.util.List;
import java.util.Map;

public class InnerOfferAdapter extends RecyclerView.Adapter<InnerOfferAdapter.ViewHolders> {
    private List<Map<String, Object>> allinneroffersItem;
    private Context context;

    public InnerOfferAdapter(Context context, List<Map<String, Object>> allinneroffersItem) {
        this.context = context;
        this.allinneroffersItem = allinneroffersItem;
    }


    @NonNull
    @Override
    public InnerOfferAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_inner_offers_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerOfferAdapter.ViewHolders holder, int position) {
        Map<String, Object> item = allinneroffersItem.get(position);
        holder.image.setImageResource((int) item.get("image"));

        holder.image.setOnClickListener(new View.OnClickListener() {
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
        return allinneroffersItem.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.inner_offer_image);
        }
    }

}
