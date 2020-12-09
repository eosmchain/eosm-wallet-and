package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class test implements Parcelable {
    BigDecimal sss;

    protected test(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<test> CREATOR = new Creator<test>() {
        @Override
        public test createFromParcel(Parcel in) {
            return new test(in);
        }

        @Override
        public test[] newArray(int size) {
            return new test[size];
        }
    };
}
