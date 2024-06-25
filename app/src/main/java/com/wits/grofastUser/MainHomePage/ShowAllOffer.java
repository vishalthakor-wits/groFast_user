package com.wits.grofastUser.MainHomePage;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wits.grofastUser.Adapter.ShowAllOffersAdapter;
import com.wits.grofastUser.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowAllOffer extends AppCompatActivity {
    RecyclerView recyclerView;
    ShowAllOffersAdapter showAllOffersAdapter;
    List<Map<String, Object>> offerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.All_offer_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        setContentView(R.layout.activity_show_all_offer);
        //offer Item
        recyclerView = findViewById(R.id.show_all_offer_data);
        offerItems = new ArrayList<>();

        loadOffersItem();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        showAllOffersAdapter = new ShowAllOffersAdapter(getApplicationContext(), offerItems);
        recyclerView.setAdapter(showAllOffersAdapter);
    }

    private void loadOffersItem() {
        Map<String, Object> item1 = new HashMap<>();
        item1.put("Name", "Ankleshwar");
        item1.put("image", R.drawable.of1);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("Name", "Ankleshwar");
        item2.put("image", R.drawable.of2);

        Map<String, Object> item3 = new HashMap<>();
        item3.put("Name", "Ankleshwar");
        item3.put("image", R.drawable.of1);

        Map<String, Object> item4 = new HashMap<>();
        item4.put("Name", "Ankleshwar");
        item4.put("image", R.drawable.of1);

        offerItems.add(item1);
        offerItems.add(item2);
        offerItems.add(item3);
        offerItems.add(item4);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}