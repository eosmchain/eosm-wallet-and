package com.token.mangowallet.bean;

import androidx.annotation.DrawableRes;


public class AppItemModel {
    public final int id;
    @DrawableRes
    public final int thumbnail;
    public final String title;
    @DrawableRes
    public final int large;
    public final String theme;
    public final String details;

    public AppItemModel(int id, @DrawableRes int thumbnail, String title, @DrawableRes int large, String theme, String details) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.title = title;
        this.large = large;
        this.theme = theme;
        this.details = details;
    }
}
