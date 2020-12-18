package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class A implements Parcelable {
    private boolean isBackup = false;

    protected A(Parcel in) {
        isBackup = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isBackup ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<A> CREATOR = new Creator<A>() {
        @Override
        public A createFromParcel(Parcel in) {
            return new A(in);
        }

        @Override
        public A[] newArray(int size) {
            return new A[size];
        }
    };
}
