package com.wits.grofast_user.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_user.Api.responseModels.AddressModel;
import com.wits.grofast_user.Details.EditAddress;
import com.wits.grofast_user.R;

import java.util.List;

public class AddresslistAdapter extends RecyclerView.Adapter<AddresslistAdapter.ViewHolders> {
    private List<AddressModel> addressList;
    private Context context;
    private int selectedPosition = -1;
    private OnDeliveryButtonClickListener deliveryButtonClickListener;

    public AddresslistAdapter(Context context, List<AddressModel> addressList, OnDeliveryButtonClickListener listener) {
        this.context = context;
        this.addressList = addressList;
        this.deliveryButtonClickListener = listener;
    }

    @NonNull
    @Override
    public AddresslistAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_address_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddresslistAdapter.ViewHolders holder, @SuppressLint("RecyclerView") int position) {
        AddressModel item = addressList.get(position);
        String customerAddress = item.getAddress() + "," + item.getCity() + "," + item.getPin_code();
        holder.address.setText(customerAddress);

        holder.radioButton.setChecked(position == selectedPosition);
        holder.layout.setVisibility(position == selectedPosition ? View.VISIBLE : View.GONE);

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousPosition = selectedPosition;
                selectedPosition = position;
                notifyItemChanged(previousPosition);
                notifyItemChanged(selectedPosition);
            }
        });

        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.radioButton.performClick();
            }
        });

        holder.delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deliveryButtonClickListener != null) {
                    deliveryButtonClickListener.onDeliveryButtonClick(customerAddress, item.getId());
                }
            }
        });

        holder.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, EditAddress.class);
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        TextView address, editbtn;
        AppCompatButton delivery;
        RadioButton radioButton;
        LinearLayout layout;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
            radioButton = itemView.findViewById(R.id.selected_address);
            editbtn = itemView.findViewById(R.id.edit_address_btn);
            delivery = itemView.findViewById(R.id.delivery_btn);
            layout = itemView.findViewById(R.id.details_show_layout);
        }
    }
    public interface OnDeliveryButtonClickListener {
        void onDeliveryButtonClick(String address, int id);
    }
}
