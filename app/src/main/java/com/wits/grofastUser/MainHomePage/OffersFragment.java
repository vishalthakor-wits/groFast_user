package com.wits.grofastUser.MainHomePage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wits.grofastUser.Adapter.OffersAdapter;
import com.wits.grofastUser.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OffersFragment extends Fragment {
    RecyclerView recyclerView;
    OffersAdapter allOffersAdapter;
    List<Map<String, Object>> offerItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_offers, container, false);
        if (getActivity() instanceof HomePage) {
            ((HomePage) getActivity()).updateActionBar(getString(R.string.bottom_menu_offers), 0, 0);
        }

        //offer Item
        recyclerView = root.findViewById(R.id.all_offers_recycleview);
        offerItems = new ArrayList<>();

        loadOffersItem();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        allOffersAdapter = new OffersAdapter(getContext(), offerItems);
        recyclerView.setAdapter(allOffersAdapter);
        return root;
    }

    private void loadOffersItem() {
        Map<String, Object> item1 = new HashMap<>();
        item1.put("Name", "Ankleshwar");
        item1.put("SubName", "Bharuch");

        List<Map<String, Object>> innerData1 = new ArrayList<>();
        Map<String, Object> innerItem1 = new HashMap<>();
        innerItem1.put("image", R.drawable.of1);
        innerData1.add(innerItem1);

        Map<String, Object> innerItem2 = new HashMap<>();
        innerItem2.put("image", R.drawable.of2);
        innerData1.add(innerItem2);

        Map<String, Object> innerItem3 = new HashMap<>();
        innerItem3.put("image", R.drawable.of1);
        innerData1.add(innerItem3);

        item1.put("InnerData", innerData1);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("Name", "Ankleshwar");
        item2.put("SubName", "Bharuch");

        List<Map<String, Object>> innerData2 = new ArrayList<>();
        Map<String, Object> innerItem5 = new HashMap<>();
        innerItem5.put("image", R.drawable.of2);
        innerData2.add(innerItem2);
        item2.put("InnerData", innerData2);

        Map<String, Object> item3 = new HashMap<>();
        item3.put("Name", "Ankleshwar");
        item3.put("SubName", "Bharuch");

        List<Map<String, Object>> innerData3 = new ArrayList<>();
        Map<String, Object> innerItem10 = new HashMap<>();
        innerItem10.put("image", R.drawable.of2);
        innerData3.add(innerItem3);
        item3.put("InnerData", innerData3);

        offerItems.add(item1);
        offerItems.add(item2);
        offerItems.add(item3);
    }
}