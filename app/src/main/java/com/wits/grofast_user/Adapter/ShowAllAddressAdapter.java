package com.wits.grofast_user.Adapter;

import static com.wits.grofast_user.CommonUtilities.handleApiError;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofast_user.Api.RetrofitService;
import com.wits.grofast_user.Api.interfaces.AddressInterface;
import com.wits.grofast_user.Api.responseClasses.LoginResponse;
import com.wits.grofast_user.Api.responseModels.AddressModel;
import com.wits.grofast_user.Details.EditAddress;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.UserActivitySession;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowAllAddressAdapter extends RecyclerView.Adapter<ShowAllAddressAdapter.ViewHolders> {
    private List<AddressModel> addressList;
    private Context context;
    private ShowAllAddressAdapter adapter;
    private final String TAG = "ShowAllAddressAdapter";

    public ShowAllAddressAdapter(Context context, List<AddressModel> addressList) {
        this.context = context;
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public ShowAllAddressAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_address_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShowAllAddressAdapter.ViewHolders holder, int position) {
        adapter = this;
        AddressModel item = addressList.get(position);
        String customerAddress = item.getAddress() + "," + item.getCity() + "," + item.getPin_code();
        holder.address.setText(customerAddress);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                if (!(context instanceof Activity) || ((Activity) context).isFinishing()) {
                    return;
                }
                showEditDeleteDialog(context, item, position);
            }
        });
    }

    private void showEditDeleteDialog(Context context, AddressModel item, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.Choose_an_action));
        builder.setItems(new CharSequence[]
                        {context.getString(R.string.edit_address), context.getString(R.string.delete_address)},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent in = new Intent(context, EditAddress.class);
                                in.putExtra("address", item);
                                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(in);
                                break;
                            case 1:
                                showDeleteConfirmationDialog(context, item, position);
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        TextView address;
        ImageView edit;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.all_address_address);
            edit = itemView.findViewById(R.id.all_address_edit);
        }
    }

    private void showDeleteConfirmationDialog(Context context, AddressModel item, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.delete_address_confirmation));
        builder.setMessage(context.getString(R.string.are_you_sure_delete));

        View dialogButtonsView = LayoutInflater.from(context).inflate(R.layout.confirmdeleteaddress, null);
        builder.setView(dialogButtonsView);

        // Find the buttons in the custom layout
        Button btnNo = dialogButtonsView.findViewById(R.id.btnNo);
        Button btnYes = dialogButtonsView.findViewById(R.id.btnYes);
        AlertDialog dialog = builder.create();
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAddress(item.getId(), position);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void deleteAddress(int addressId, int position) {
        UserActivitySession userActivitySession = new UserActivitySession(context);
        Call<LoginResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(AddressInterface.class).deleteCustomerAddress(addressId);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    addressList.remove(position);
                    adapter.notifyDataSetChanged();
                }
                handleApiError(TAG, response, context);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
