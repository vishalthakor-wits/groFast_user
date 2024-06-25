package com.wits.grofastUser.MainHomePage;

import static com.wits.grofastUser.CommonUtilities.handleApiError;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wits.grofastUser.Adapter.CartResentAddProductAdapter;
import com.wits.grofastUser.Adapter.TaxesChargesAdapter;
import com.wits.grofastUser.Api.RetrofitService;
import com.wits.grofastUser.Api.interfaces.CartInterface;
import com.wits.grofastUser.Api.responseClasses.CartFetchResponse;
import com.wits.grofastUser.Api.responseModels.CartModel;
import com.wits.grofastUser.Api.responseModels.TaxAndCharge;
import com.wits.grofastUser.Details.Coupon;
import com.wits.grofastUser.Details.PaymentDetails;
import com.wits.grofastUser.R;
import com.wits.grofastUser.session.CartDetailSession;
import com.wits.grofastUser.session.UserActivitySession;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {
    RecyclerView recyclerView_cart_resent_product, taxes_charges_cart_recycleview;
    CartResentAddProductAdapter cartItemsAdapter;
    TaxesChargesAdapter taxesChargesAdapter;
    TextView additem, couponLink, tip20, tip30, tipother;
    LinearLayout additemlayout, showeditItemtextlayout, addcoponlayout, showeditCouponlayout, Taxeslayout, tiplayout;
    EditText additemedittext, coupontext, tipamount;
    AppCompatButton additembutton, addCouponbutton, checkout,startshopping;
    private List<CartModel> cartModelList = new ArrayList<>();
    private List<TaxAndCharge> taxAndCharges = new ArrayList<>();
    private static TextView grandTotal, subTotal, cart_empty_text1, cart_empty_text2;
    private static ProgressBar progressBar;
    private static LinearLayout cartLinearLayout, cart_empty_layout;
    private UserActivitySession userActivitySession;
    private CartDetailSession cartDetailSession;
    private String selectedTip = "0";
    ImageView additemimage, couponimagechange, Taxesimage;
    LinearLayoutManager linearLayoutManager;
    private final String TAG = "CartFragment";
    private NestedScrollView datashow;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        if (getActivity() instanceof HomePage) {
            ((HomePage) getActivity()).updateActionBar(getString(R.string.bottom_menu_cart), 0, 0);
        }

        //Textview
        additem = root.findViewById(R.id.cart_add_more_item);
        couponLink = root.findViewById(R.id.cart_coupon_link);
        tip20 = root.findViewById(R.id.tip_20);
        tip30 = root.findViewById(R.id.tip_30);
        tipother = root.findViewById(R.id.tip_other);
        subTotal = root.findViewById(R.id.subtotal);
        grandTotal = root.findViewById(R.id.grand_total);

        shimmerFrameLayout = root.findViewById(R.id.shimmer_layout_cart);

        userActivitySession = new UserActivitySession(getContext());
        cartDetailSession = new CartDetailSession(getContext());

        cart_empty_text1 = root.findViewById(R.id.cart_empty_text1);
        cart_empty_text2 = root.findViewById(R.id.cart_empty_text2);

        //Linear layout
        additemlayout = root.findViewById(R.id.cart_add_item_linearlayout);
        showeditItemtextlayout = root.findViewById(R.id.cart_add_item_show_detail_layout);
        addcoponlayout = root.findViewById(R.id.cart_add_coupon_layout);
        showeditCouponlayout = root.findViewById(R.id.cart_add_coupon_show_detail_layout);
        Taxeslayout = root.findViewById(R.id.cart_add_taxes_layout);
        tiplayout = root.findViewById(R.id.tip_layout);
        datashow = root.findViewById(R.id.cart_data_show);
        cart_empty_layout = root.findViewById(R.id.cart_empty_layout);

        //Edittext
        additemedittext = root.findViewById(R.id.cart_add_item_edittext);
        coupontext = root.findViewById(R.id.cart_add_coupon_edittext);
        tipamount = root.findViewById(R.id.tip_amount_enter);

        //Button
        additembutton = root.findViewById(R.id.cart_add_item_button);
        addCouponbutton = root.findViewById(R.id.cart_add_coupon_button);
        checkout = root.findViewById(R.id.checkout);
        startshopping=root.findViewById(R.id.cart_empty_start_shopping);

        //Image view
        additemimage = root.findViewById(R.id.cart_add_item_image);
        couponimagechange = root.findViewById(R.id.cart_add_coupon_image);
        Taxesimage = root.findViewById(R.id.cart_add_taxes_image);

        progressBar = root.findViewById(R.id.progress_bar_cart_fragment);
        cartLinearLayout = root.findViewById(R.id.cart_linear_layout);

        startshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentnav, new ProductFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), PaymentDetails.class);
                startActivity(in);
            }
        });

        addCouponbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCartItems(coupontext.getText().toString(), null, false);
            }
        });

        View.OnClickListener tipClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTipSelection();
                updateTipSelection((TextView) v);

                cartDetailSession.setTip("0");
                selectedTip = ((TextView) v).getText().toString();

                if (selectedTip.equals(getString(R.string.other))) {
                    tipamount.setVisibility(View.VISIBLE);
                } else {
                    cartDetailSession.setTip(selectedTip);
                    loadCartItems(cartDetailSession.getCoupon(), null, false);
                    tipamount.setText("");
                    tipamount.setVisibility(View.GONE);
                }
            }
        };

        coupontext.setText(cartDetailSession.getCoupon());
        additemedittext.setText(cartDetailSession.getAditionalNote());

        tipamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, "onTextChanged: tip text " + s.toString());
                if (tipamount.getVisibility() == View.VISIBLE) {
                    cartDetailSession.setTip(s.toString());
                    loadCartItems(cartDetailSession.getCoupon(), null, false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        additembutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = additemedittext.getText().toString().trim();
                if (!note.isEmpty())
                    Toast.makeText(getActivity(), getString(R.string.toast_aditional_note_saved), Toast.LENGTH_SHORT).show();
                cartDetailSession.storeAditionalNote(note);
            }
        });

        final String sessionTip = cartDetailSession.getTip();
        if (!sessionTip.equals("0")) {
            Log.e(TAG, "onCreateView: session tip " + sessionTip);
            if (sessionTip.equals(tip20.getText().toString())) {
                resetTipSelection();
                updateTipSelection(tip20);
            } else if (sessionTip.equals(tip30.getText().toString())) {
                resetTipSelection();
                updateTipSelection(tip30);
            } else {
                resetTipSelection();
                updateTipSelection(tipother);
                tipamount.setText(sessionTip);
                tipamount.setVisibility(View.VISIBLE);
            }
        }

        tip20.setOnClickListener(tipClickListener);
        tip30.setOnClickListener(tipClickListener);
        tipother.setOnClickListener(tipClickListener);


        Taxeslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taxes_charges_cart_recycleview.getVisibility() == View.VISIBLE) {
                    taxes_charges_cart_recycleview.setVisibility(View.GONE);
                    Taxesimage.setImageResource(R.drawable.hide_arrow);
                } else {
                    taxes_charges_cart_recycleview.setVisibility(View.VISIBLE);
                    Taxesimage.setImageResource(R.drawable.arrow_up);
                }
            }
        });

        additemlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showeditItemtextlayout.getVisibility() == View.VISIBLE) {
                    showeditItemtextlayout.setVisibility(View.GONE);
                    additemimage.setImageResource(R.drawable.hide_arrow);
                } else {
                    showeditItemtextlayout.setVisibility(View.VISIBLE);
                    additemimage.setImageResource(R.drawable.arrow_up);
                }
            }
        });

        addcoponlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showeditCouponlayout.getVisibility() == View.VISIBLE) {
                    showeditCouponlayout.setVisibility(View.GONE);
                    couponLink.setVisibility(View.GONE);
                    couponimagechange.setImageResource(R.drawable.hide_arrow);
                } else {
                    showeditCouponlayout.setVisibility(View.VISIBLE);
                    couponLink.setVisibility(View.VISIBLE);
                    couponimagechange.setImageResource(R.drawable.arrow_up);
                }
            }
        });

        couponLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), Coupon.class);
                startActivity(in);
            }
        });

        //Resent Cart Item
        recyclerView_cart_resent_product = root.findViewById(R.id.fragment_cart_resent_product);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView_cart_resent_product.setLayoutManager(linearLayoutManager);

        loadCartItems(cartDetailSession.getCoupon(), null, true);

        //Taxes Charges cart item
        taxes_charges_cart_recycleview = root.findViewById(R.id.taxes_charges_cart_recycleview);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        taxes_charges_cart_recycleview.setLayoutManager(linearLayoutManager);

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentnav, new ProductFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return root;
    }

    private void loadCartItems(String couponCode, String aditionalNote, boolean isFirstLoad) {
        Log.e(TAG, "loadCartItems: tip " + cartDetailSession.getTip());
        Call<CartFetchResponse> call = RetrofitService.getClient(userActivitySession.getToken()).create(CartInterface.class).fetchCartDetails(Integer.parseInt(cartDetailSession.getTip()), couponCode, aditionalNote);
        if (isFirstLoad) startShimmereffect();
        else startProgrtessBar();
        call.enqueue(new Callback<CartFetchResponse>() {
            @Override
            public void onResponse(Call<CartFetchResponse> call, Response<CartFetchResponse> response) {
                stopProgrtessBar();
                stopShimmereffect();
                if (response.isSuccessful()) {
                    CartFetchResponse cartFetchResponse = response.body();
                    cartModelList = cartFetchResponse.getCartModelList();

                    taxAndCharges = cartFetchResponse.getTaxAndCharges();

                    subTotal.setText(cartFetchResponse.getSubtotal().toString());
                    grandTotal.setText(cartFetchResponse.getTotal().toString());
                    taxesChargesAdapter = new TaxesChargesAdapter(getContext(), taxAndCharges);
                    cartItemsAdapter = new CartResentAddProductAdapter(cartModelList, getContext(), taxes_charges_cart_recycleview);

                    recyclerView_cart_resent_product.setAdapter(cartItemsAdapter);
                    taxes_charges_cart_recycleview.setAdapter(taxesChargesAdapter);

                    cartDetailSession.setCoupon(couponCode);
                    cartDetailSession.setTotalAmount(cartFetchResponse.getTotal());
                } else if (response.code() == 422) {
                    try {
                        String errorBodyString = response.errorBody().string();
                        Gson gson = new Gson();
                        JsonObject errorBodyJson = gson.fromJson(errorBodyString, JsonObject.class);

                        String errorMessage = errorBodyJson.has("errorMessage") ? errorBodyJson.get("errorMessage").getAsString() : "No errorMessage";
                        String message = errorBodyJson.has("message") ? errorBodyJson.get("message").getAsString() : "No message";

                        showNoCartMessage(message, errorMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else handleApiError(TAG, response, getContext());
            }

            @Override
            public void onFailure(Call<CartFetchResponse> call, Throwable t) {
                stopShimmereffect();
                stopProgrtessBar();
                t.printStackTrace();
            }
        });
    }

    private void showNoCartMessage(String message, String messaage2) {
        datashow.setVisibility(View.GONE);
        cart_empty_layout.setVisibility(View.VISIBLE);
        cart_empty_text1.setText(message);
        cart_empty_text2.setText(messaage2);
    }

    private void resetTipSelection() {
        resetTip(tip20);
        resetTip(tip30);
        resetTip(tipother);
    }

    private void resetTip(TextView tipView) {
        tipView.setBackground(getResources().getDrawable(R.drawable.button_line));
        tipView.setTextColor(getResources().getColor(R.color.Login_theme));
        tipView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_currency_rupee_24, 0, 0, 0);
        tipView.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.Login_theme)));
    }

    private void updateTipSelection(TextView selectedTip) {
        selectedTip.setBackground(getResources().getDrawable(R.drawable.selected_text));
        selectedTip.setTextColor(getResources().getColor(R.color.white));
        selectedTip.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_currency_rupee_24, 0, 0, 0);
        selectedTip.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

        // Ensure the padding remains 20dp on left and right
        selectedTip.setPadding(getResources().getDimensionPixelSize(R.dimen.padding_left_right), selectedTip.getPaddingTop(), getResources().getDimensionPixelSize(R.dimen.padding_left_right), selectedTip.getPaddingBottom());
    }

    public static TextView getSubTotalTextView() {
        return subTotal;
    }

    public static TextView getGrandTotalTotalTextView() {
        return grandTotal;
    }

    public static void startProgrtessBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void stopProgrtessBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void startShimmereffect() {
        datashow.setVisibility(View.GONE);
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
    }

    private void stopShimmereffect() {
        datashow.setVisibility(View.VISIBLE);
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
    }
}