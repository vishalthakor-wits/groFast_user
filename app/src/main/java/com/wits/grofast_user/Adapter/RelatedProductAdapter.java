package com.wits.grofast_user.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_user.R;

import java.util.List;
import java.util.Map;

public class RelatedProductAdapter extends RecyclerView.Adapter<RelatedProductAdapter.ViewHolders> {
    private List<Map<String, Object>> relatedItem;
    private Context context;

    public RelatedProductAdapter(Context context, List<Map<String, Object>> relatedItem) {
        this.context = context;
        this.relatedItem = relatedItem;
    }

    @NonNull
    @Override
    public RelatedProductAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.related_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedProductAdapter.ViewHolders holder, int position) {
        Map<String, Object> item = relatedItem.get(position);
        holder.name.setText((String) item.get("Name"));
        holder.price.setText((String) item.get("Price"));
        holder.image.setImageResource((int) item.get("image"));
    }

    @Override
    public int getItemCount() {
        return relatedItem.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {

        TextView name, price;
        ImageView image;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.related_item_image);
            name = itemView.findViewById(R.id.related_item_name);
            price = itemView.findViewById(R.id.related_item_price);
        }
    }
}
