package com.wits.grofastUser.Adapter;

import static com.wits.grofastUser.CommonUtilities.getDateFromTimestamp;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofastUser.Api.responseModels.WalletModel;
import com.wits.grofastUser.R;

import java.util.List;

public class WallethistoryAdapter extends RecyclerView.Adapter<WallethistoryAdapter.ViewHolders> {

    private List<WalletModel> walletList;
    private Context context;
    private static String TAG = "Wallethistory adapter";

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
        holder.point.setText(model.getPoints().toString());
        holder.orderid.setText(model.getOrder_id().toString());
        Log.e(TAG, "onBindViewHolder: status :  " + model.getStatus());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.created.setText(getDateFromTimestamp(model.getCreated_at()));
        } else holder.created.setText(model.getCreated_at());

        if (model.getStatus().equals("1")) {
            holder.SubtraIcon.setVisibility(View.VISIBLE);
            holder.point.setTextColor(ContextCompat.getColor(context, R.color.expired));
            holder.status.setText(R.string.debited);
        } else {
            holder.addIcon.setVisibility(View.VISIBLE);
            holder.point.setTextColor(ContextCompat.getColor(context, R.color.Login_theme));
            holder.status.setText(R.string.creadited);
        }
    }

    @Override
    public int getItemCount() {
        return walletList.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        TextView orderid, status, created, point;
        ImageView addIcon, SubtraIcon;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            orderid = itemView.findViewById(R.id.wallet_history_order_id);
            status = itemView.findViewById(R.id.wallet_history_status);
            created = itemView.findViewById(R.id.wallet_history_created_at);
            point = itemView.findViewById(R.id.wallet_history_points);
            addIcon = itemView.findViewById(R.id.creadited);
            SubtraIcon = itemView.findViewById(R.id.debited);
        }
    }
}
