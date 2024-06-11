package com.wits.grofast_user.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_user.R;

import java.util.List;
import java.util.Map;

public class AddLocationSerachResultAdapter extends RecyclerView.Adapter<AddLocationSerachResultAdapter.ViewHolder> {

    private List<Map<String, Object>> locationitem;

    public AddLocationSerachResultAdapter(List<Map<String, Object>> locationitem) {
        this.locationitem = locationitem;
    }

    @NonNull
    @Override
    public AddLocationSerachResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.add_location_search_result_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddLocationSerachResultAdapter.ViewHolder holder, int position) {
        Map<String, Object> item = locationitem.get(position);
        holder.location_name.setText((String) item.get("LocationName"));
        holder.sub_name.setText((String) item.get("SubName"));
    }

    @Override
    public int getItemCount() {
        return locationitem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView location_name, sub_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            location_name = itemView.findViewById(R.id.add_location_name);
            sub_name = itemView.findViewById(R.id.add_location_sub_name);
        }
    }
}
