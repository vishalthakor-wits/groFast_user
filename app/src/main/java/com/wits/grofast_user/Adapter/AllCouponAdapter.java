package com.wits.grofast_user.Adapter;

import static com.wits.grofast_user.Api.RetrofitService.domain;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wits.grofast_user.Api.responseModels.CouponModel;
import com.wits.grofast_user.Details.CouponDetails;
import com.wits.grofast_user.R;

import java.util.List;

public class AllCouponAdapter extends RecyclerView.Adapter<AllCouponAdapter.ViewHolders> {
    private List<CouponModel> AllCouponItems;
    private Context context;

    public AllCouponAdapter(Context context, List<CouponModel> AllCouponItems) {
        this.context = context;
        this.AllCouponItems = AllCouponItems;
    }

    @NonNull
    @Override
    public AllCouponAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_coupon_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllCouponAdapter.ViewHolders holder, int position) {
        CouponModel item = AllCouponItems.get(position);
        Glide.with(context).load(domain + item.getImage()).placeholder(R.color.default_color).into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), CouponDetails.class);
                intent.putExtra("Code", item.getCode());
                intent.putExtra("Description", item.getDescription());
                intent.putExtra("image", domain + item.getImage());
                intent.putExtra("Status",  item.getStatus());
                setStatusColor(item.getStatus());
                intent.putExtra("CouponId", item.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    private void setStatusColor(String status) {
//        switch (status) {
//            case "Active":
//                textView.setTextColor(context.getResources().getColor(R.color.active));
//                break;
//            case "Inactive":
//                textView.setTextColor(context.getResources().getColor(R.color.inactive));
//                break;
//            case "Expired":
//                textView.setTextColor(context.getResources().getColor(R.color.expired));
//                break;
//            default:
//                textView.setTextColor(context.getResources().getColor(android.R.color.black));
//                break;
//        }
    }

    @Override
    public int getItemCount() {
        return AllCouponItems.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.coupon_card);
            imageView = itemView.findViewById(R.id.coupon_image);
        }
    }
}
