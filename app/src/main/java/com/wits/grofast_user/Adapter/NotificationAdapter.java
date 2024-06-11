package com.wits.grofast_user.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_user.R;

import java.util.List;
import java.util.Map;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolders> {
    private List<Map<String, Object>> NotificationItems;
    private Context context;

    public NotificationAdapter(Context context, List<Map<String, Object>> NotificationItems) {
        this.context = context;
        this.NotificationItems = NotificationItems;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_notification_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolders holder, int position) {
        Map<String, Object> item = NotificationItems.get(position);
        holder.header.setText((String) item.get("Header"));
        holder.description.setText((String) item.get("Description"));
        holder.day.setText((String) item.get("Day"));
    }

    @Override
    public int getItemCount() {
        return NotificationItems.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        TextView header, description, day;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.notification_header);
            description = itemView.findViewById(R.id.notification_description);
            day = itemView.findViewById(R.id.notification_day);
        }
    }
}
