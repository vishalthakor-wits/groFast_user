package com.wits.grofastUser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofastUser.Api.responseModels.TaxAndCharge;
import com.wits.grofastUser.R;
import com.wits.grofastUser.session.CartDetailSession;

import java.util.List;

public class TaxesChargesAdapter extends RecyclerView.Adapter<TaxesChargesAdapter.ViewHolders> {
    private List<TaxAndCharge> taxAndCharges;
    private Context context;
    private CartDetailSession cartDetailSession;
    private final String TAG = "TaxesChargesAdapter";

    public TaxesChargesAdapter(Context context, List<TaxAndCharge> taxAndCharges) {
        this.context = context;
        this.taxAndCharges = taxAndCharges;
    }

    @NonNull
    @Override
    public TaxesChargesAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_taxes_charges_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaxesChargesAdapter.ViewHolders holder, int position) {
        cartDetailSession = new CartDetailSession(context);
        TaxAndCharge item = taxAndCharges.get(position);

        holder.name.setText(item.getText());
        holder.subname.setText("" + item.getValue());
    }

    @Override
    public int getItemCount() {
        return taxAndCharges.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        TextView name,subname;
        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.taxes_name);
            subname = itemView.findViewById(R.id.taxes_sub_name);
        }
    }
}
