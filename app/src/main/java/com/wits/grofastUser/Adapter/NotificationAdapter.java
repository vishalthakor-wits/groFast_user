package com.wits.grofastUser.Adapter;

import static com.wits.grofastUser.CommonUtilities.getDateFromTimestamp;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofastUser.Notification.InAppNotificationModel;
import com.wits.grofastUser.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolders> {
    private List<InAppNotificationModel> NotificationItems;
    private Context context;

    public NotificationAdapter(Context context, List<InAppNotificationModel> NotificationItems) {
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
        InAppNotificationModel item = NotificationItems.get(position);
        holder.header.setText(item.getTitle());
        holder.description.setText(item.getBody());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.day.setText(getDateFromTimestamp(item.getCreated_at()));
        } else holder.day.setText(item.getCreated_at());

    }

    @Override
    public int getItemCount() {
        return NotificationItems.size();
    }

    public void addNotification(List<InAppNotificationModel> notification) {
        NotificationItems.addAll(notification);
        notifyDataSetChanged();
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
