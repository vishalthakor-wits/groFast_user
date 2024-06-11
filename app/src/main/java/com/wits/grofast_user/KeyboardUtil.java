package com.wits.grofast_user;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

public class KeyboardUtil {
    public interface KeyboardVisibilityListener {
        void onKeyboardVisibilityChanged(boolean isVisible);
    }
    public static void setKeyboardVisibilityListener(View rootLayout, final KeyboardVisibilityListener listener) {
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private boolean wasOpened;

            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootLayout.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                boolean isOpen = keypadHeight > screenHeight * 0.15;

                if (isOpen != wasOpened) {
                    wasOpened = isOpen;
                    listener.onKeyboardVisibilityChanged(isOpen);
                }
            }
        });
    }
}
