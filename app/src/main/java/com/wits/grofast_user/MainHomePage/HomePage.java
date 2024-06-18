package com.wits.grofast_user.MainHomePage;

import static com.wits.grofast_user.Api.RetrofitService.domain;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.wits.grofast_user.Adapter.AddLocationSerachResultAdapter;
import com.wits.grofast_user.Details.Coupon;
import com.wits.grofast_user.Details.EditProfile;
import com.wits.grofast_user.Details.MyAddress;
import com.wits.grofast_user.Details.Notification;
import com.wits.grofast_user.Details.NotificationSetting;
import com.wits.grofast_user.Details.SettingsPage;
import com.wits.grofast_user.Details.Support;
import com.wits.grofast_user.Details.Wallet;
import com.wits.grofast_user.Details.Wallethistory;
import com.wits.grofast_user.Enums.FragmentEnum;
import com.wits.grofast_user.KeyboardUtil;
import com.wits.grofast_user.MainActivity;
import com.wits.grofast_user.R;
import com.wits.grofast_user.session.CartDetailSession;
import com.wits.grofast_user.session.UserActivitySession;
import com.wits.grofast_user.session.UserDetailSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePage extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private ImageView menuBar, notification;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView textview_set_location;
    AddLocationSerachResultAdapter addLocationSerachResultAdapter;
    List<Map<String, Object>> LocationItems;
    private TextView userName, userPhoneNo;
    private CircleImageView userProfile;
    private View headerView;
    private UserDetailSession userDetailSession;
    private final String TAG = "HomePage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home_page);

        UserActivitySession session = new UserActivitySession(getApplicationContext());
        CartDetailSession cartDetailSession = new CartDetailSession(getApplicationContext());
        userDetailSession = new UserDetailSession(getApplicationContext());

        drawerLayout = findViewById(R.id.drawerlayout1);
        menuBar = findViewById(R.id.menu_bar);
        navigationView = findViewById(R.id.menu_navigation);
        textview_set_location = findViewById(R.id.textview_set_location);
        notification = findViewById(R.id.notification_home);
        headerView = navigationView.getHeaderView(0);

        userName = headerView.findViewById(R.id.user_name);
        userPhoneNo = headerView.findViewById(R.id.user_phone_no);
        userProfile = headerView.findViewById(R.id.user_profile);


        if (getIntent().getBooleanExtra("openHomeFragment", false)) {
            openProductFragment();
        }

        menuBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, Notification.class));
            }
        });

//        textview_set_location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openlocationDialogbox();
//            }
//        });
        final View rootLayout = findViewById(R.id.drawerlayout1);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        if (savedInstanceState == null) {
            loadfragment(new HomeFragment(), FragmentEnum.HOME.getTag());
            bottomNavigationView.setSelectedItemId(R.id.navhome);
        }


        frameLayout = findViewById(R.id.fragmentnav);
        bottomNavigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navproduct) {
                    loadfragment(new ProductFragment(), FragmentEnum.PRODUCT.getTag());
                } else if (id == R.id.navoffers) {
                    loadfragment(new OffersFragment(), FragmentEnum.OFFER.getTag());
                } else if (id == R.id.navhome) {
                    loadfragment(new HomeFragment(), FragmentEnum.HOME.getTag());
                } else if (id == R.id.navcart) {
                    loadfragment(new CartFragment(), FragmentEnum.CART.getTag());
                } else {
                    loadfragment(new HistoryFragment(), FragmentEnum.HISTORY.getTag());
                }
                return true;
            }
        });

        KeyboardUtil.setKeyboardVisibilityListener(rootLayout, new KeyboardUtil.KeyboardVisibilityListener() {
            @Override
            public void onKeyboardVisibilityChanged(boolean isVisible) {
                if (isVisible) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_home) {
                    loadfragment(new HomeFragment(), FragmentEnum.HOME.getTag());
                } else if (id == R.id.menu_coupon) {
                    startActivity(new Intent(HomePage.this, Coupon.class));
                } else if (id == R.id.menu_offers) {
                    loadfragment(new OffersFragment(), FragmentEnum.OFFER.getTag());
                } else if (id == R.id.menu_wallet) {
                    if (userDetailSession.isWalletActivated())
                        startActivity(new Intent(HomePage.this, Wallethistory.class));
                    else {
                        startActivity(new Intent(HomePage.this, Wallet.class));
                    }
                } else if (id == R.id.menu_notification_setting) {
                    startActivity(new Intent(HomePage.this, NotificationSetting.class));
                } else if (id == R.id.menu_notification) {
                    startActivity(new Intent(HomePage.this, Notification.class));
                } else if (id == R.id.menu_setting) {
                    startActivity(new Intent(HomePage.this, SettingsPage.class));
                } else if (id == R.id.menu_cart) {
                    loadfragment(new CartFragment(), FragmentEnum.CART.getTag());
                } else if (id == R.id.menu_edit_profile) {
                    startActivity(new Intent(HomePage.this, EditProfile.class));
                } else if (id == R.id.menu_my_address) {
                    startActivity(new Intent(HomePage.this, MyAddress.class));
                } else if (id == R.id.menu_support) {
                    startActivity(new Intent(HomePage.this, Support.class));
                } else if (id == R.id.menu_logout) {
                    session.setLoginStaus(false);
                    session.clearSession();
                    userDetailSession.clearSession();
                    cartDetailSession.clearSession();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        View.OnClickListener profileClickListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, EditProfile.class));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                }, 500);

            }
        };

        userName.setOnClickListener(profileClickListner);
        userProfile.setOnClickListener(profileClickListner);
    }


    private void loadfragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentnav, fragment, tag);
        fragmentTransaction.addToBackStack(null); // Add to back stack for subsequent transactions
        fragmentTransaction.commit();

        if (fragment instanceof OffersFragment) {
            updateActionBar(getString(R.string.bottom_menu_offers), 0, 0);
        } else if (fragment instanceof ProductFragment) {
            updateActionBar(getString(R.string.bottom_menu_product), 0, 0);
        } else if (fragment instanceof CartFragment) {
            updateActionBar(getString(R.string.bottom_menu_cart), 0, 0);
        } else if (fragment instanceof HistoryFragment) {
            updateActionBar(getString(R.string.bottom_menu_history), 0, 0);
        } else {
            updateActionBar(getString(R.string.bottom_menu_home), 0, 0);
        }
    }

    public void updateActionBar(String text, int leftDrawable, int rightDrawable) {
        textview_set_location.setText(text);
        textview_set_location.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, rightDrawable, 0);
    }

    private void openlocationDialogbox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.add_location_design, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        ImageView close = dialogView.findViewById(R.id.close_add_location);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        RecyclerView recyclerView = dialogView.findViewById(R.id.recycleview_search_result_add_location);
        LocationItems = new ArrayList<>();

        loadlocationItem();

        //Top Stores Item
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        addLocationSerachResultAdapter = new AddLocationSerachResultAdapter(LocationItems);
        recyclerView.setAdapter(addLocationSerachResultAdapter);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dailogbox_background);
        }
        dialog.show();
    }

    private void loadlocationItem() {
        Map<String, Object> item1 = new HashMap<>();
        item1.put("LocationName", "Ankleshwar");
        item1.put("SubName", "Bharuch");

        Map<String, Object> item2 = new HashMap<>();
        item2.put("LocationName", "Ankleshwar");
        item2.put("SubName", "Bharuch");

        Map<String, Object> item3 = new HashMap<>();
        item3.put("LocationName", "Ankleshwar");
        item3.put("SubName", "Bharuch");

        LocationItems.add(item1);
        LocationItems.add(item2);
        LocationItems.add(item3);
    }

    @Override
    protected void onStart() {
        super.onStart();

        userPhoneNo.setText(userDetailSession.getPhoneNo());
        String name = userDetailSession.getName();
        if (name == null || name.isEmpty()) {
            name = getString(R.string.user_name);
            userName.setTextColor(getResources().getColor(R.color.default_color));
        } else {
            userName.setTextColor(getResources().getColor(R.color.white));
        }
        userName.setText(name);
        Glide.with(getApplicationContext()).load(domain + userDetailSession.getImage()).placeholder(R.drawable.account).into(userProfile);

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();

        Log.e(TAG, "onBackPressed: BackStackEntryCount " + fragmentManager.getBackStackEntryCount());
        if (backStackEntryCount <= 1) {
            super.onBackPressed();
            finish();
        } else {
            String previousFragmentTag = fragmentManager.findFragmentById(R.id.fragmentnav).getTag();
            fragmentManager.popBackStack();
        }
    }

    public void openProductFragment() {
        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentnav, homeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}