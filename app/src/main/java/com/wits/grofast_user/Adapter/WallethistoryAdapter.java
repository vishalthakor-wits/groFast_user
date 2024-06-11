package com.wits.grofast_user.Adapter;

import static com.wits.grofast_user.CommonUtilities.getDateFromTimestamp;
import static com.wits.grofast_user.CommonUtilities.getTimeFromTimestamp;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_user.Api.responseModels.WalletModel;
import com.wits.grofast_user.R;

import java.util.List;

public class WallethistoryAdapter extends RecyclerView.Adapter<WallethistoryAdapter.ViewHolders> {

    private List<WalletModel> walletList;
    private Context context;

    public WallethistoryAdapter(List<WalletModel> walletList, Context context) {
        this.walletList = walletList;
        this.context = context;
    }

    @NonNull
    @Override
    public WallethistoryAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_wallet_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolders holder, int position) {
        WalletModel model = walletList.get(position);

        holder.status.setText(model.getStatus());
        holder.point.setText(model.getPoints().toString());
        holder.orderid.setText(model.getOrder_id().toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String date = getDateFromTimestamp(model.getCreated_at());
            String time = getTimeFromTimestamp(model.getCreated_at());
            holder.created.setText(date + " " + time);
        } else holder.created.setText("" + model.getCreated_at());
    }

    @Override
    public int getItemCount() {
        return walletList.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        TextView orderid, status, created, point;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            orderid = itemView.findViewById(R.id.wallet_history_order_id);
            status = itemView.findViewById(R.id.wallet_history_status);
            created = itemView.findViewById(R.id.wallet_history_created_at);
            point = itemView.findViewById(R.id.wallet_history_points);
        }
    }
}
