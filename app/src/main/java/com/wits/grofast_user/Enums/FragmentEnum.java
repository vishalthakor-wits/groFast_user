package com.wits.grofast_user.Enums;

import com.wits.grofast_user.R;

public enum FragmentEnum {
    HOME("Home", R.id.navhome), PRODUCT("Product", R.id.navproduct), OFFER("Offer", R.id.navoffers), CART("Cart", R.id.navcart), HISTORY("History", R.id.navbhistory);

    private String tag;
    private int navId;

    FragmentEnum(String tag, int navId) {
        this.tag = tag;
        this.navId = navId;
    }

    public String getTag() {
        return tag;
    }

    public int getNavId() {
        return navId;
    }

    public static FragmentEnum fromTag(String tag) {
        for (FragmentEnum item : values()) {
            if (item.tag.equals(tag)) {
                return item;
            }
        }
        return null; // Or handle default case
    }
}
