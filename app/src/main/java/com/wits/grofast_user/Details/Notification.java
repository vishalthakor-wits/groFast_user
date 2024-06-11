package com.wits.grofast_user.Details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.wits.grofast_user.Adapter.AddLocationSerachResultAdapter;
import com.wits.grofast_user.Adapter.NotificationAdapter;
import com.wits.grofast_user.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notification extends AppCompatActivity {

    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    List<Map<String, Object>> NotificationItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(getString(R.string.notification_page_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_24);
        recyclerView = findViewById(R.id.notification_recycle);
        NotificationItems = new ArrayList<>();

        loadnotificationItem();

        //Notification Item
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        notificationAdapter = new NotificationAdapter(this, NotificationItems);
        recyclerView.setAdapter(notificationAdapter);
    }

    private void loadnotificationItem() {
        Map<String, Object> item1 = new HashMap<>();
        item1.put("Header", "Ankleshwar");
        item1.put("Description", "Your order from Fresh Picks Mart has been delivered. Feel free to tio the delivery partner.");
        item1.put("Day", "3 day");

        Map<String, Object> item2 = new HashMap<>();
        item2.put("Header", "Ankleshwar");
        item2.put("Description", "Your order from Fresh Picks Mart has been delivered. Feel free to tio the delivery partner.");
        item2.put("Day", "3 day");

        Map<String, Object> item3 = new HashMap<>();
        item3.put("Header", "Ankleshwar");
        item3.put("Description", "Your order from Fresh Picks Mart has been delivered. Feel free to tio the delivery partner.");
        item3.put("Day", "3 day");

        NotificationItems.add(item1);
        NotificationItems.add(item2);
        NotificationItems.add(item3);
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