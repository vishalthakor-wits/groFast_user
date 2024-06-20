package com.wits.grofast_user.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wits.grofast_user.Api.responseModels.CustomSpinnerModel;
import com.wits.grofast_user.R;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<CustomSpinnerModel> {
    private final LayoutInflater mInflater;
    List<CustomSpinnerModel> spinnerItemList = new ArrayList<>();
    private final Context mContext;
    private final int mResource;
    private final int hintId;

    public CustomSpinnerAdapter(@NonNull Context context, @NonNull List<CustomSpinnerModel> spinnerItemList, int hintStringId) {
        super(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerItemList);
        this.mInflater = LayoutInflater.from(context);
        this.spinnerItemList = spinnerItemList;
        this.mContext = context;
        this.mResource = R.layout.spinner_layout;
        hintId = hintStringId;
    }

    @Override
    public int getCount() {
        return spinnerItemList.size() + 1;
    }

    @Override
    public CustomSpinnerModel getItem(int position) {
        // Return null if the position is the first item (hint)
        return position == 0 ? null : spinnerItemList.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            // Hint for the main spinner view
            return getHintView(parent);
        } else {
            return getCustomView(position, convertView, parent);
        }
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            // Hint for the dropdown view
            return new View(mContext);
        } else {
            return getCustomView(position, convertView, parent);
        }
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);
        TextView textView = view.findViewById(R.id.text1);

        if (!spinnerItemList.isEmpty()) {
            textView.setText(spinnerItemList.get(position - 1).getName());
        }

        return view;
    }

    private View getHintView(ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);
        TextView textView = view.findViewById(R.id.text1);
        textView.setText(hintId);
        textView.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
        return view;
    }
}

