package com.token.mangowallet.listener;

import android.graphics.Bitmap;

public interface ControlListener {
    void onShare(Bitmap bitmap);

    void onSelectWallet(String address);
}
