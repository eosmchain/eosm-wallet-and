package com.token.mangowallet.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.token.mangowallet.R;

public class TextViewDrawableTarget extends CustomViewTarget<TextView, Drawable> {

    private Context mContext;
    private Orientation orientation;

    /**
     * Constructor that defaults {@code waitForLayout} to {@code false}.
     *
     * @param view
     */
    public TextViewDrawableTarget(Context context, @NonNull TextView view, Orientation orientation) {
        super(view);
        this.mContext = context;
        this.orientation = orientation;
    }

    @Override
    protected void onResourceCleared(@Nullable Drawable placeholder) {

    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {
        //加载失败例如url=null，此时使用 fallback不生效
        view.setCompoundDrawablesWithIntrinsicBounds(null, mContext.getDrawable(R.mipmap.icon_scan_02), null, null);
    }

    @Override
    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
        switch (orientation) {
            case TOP:
                view.setCompoundDrawablesWithIntrinsicBounds(null, resource, null, null);
                break;
            case LEFT:
                view.setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null);
                break;
            case RIGHT:
                view.setCompoundDrawablesWithIntrinsicBounds(null, null, resource, null);
                break;
            case BOTTOM:
                view.setCompoundDrawablesWithIntrinsicBounds(null, null, null, resource);
                break;
            default:
                view.setCompoundDrawablesWithIntrinsicBounds(null, resource, null, null);
        }

    }

    public enum Orientation {
        TOP, BOTTOM, LEFT, RIGHT;
    }
}
