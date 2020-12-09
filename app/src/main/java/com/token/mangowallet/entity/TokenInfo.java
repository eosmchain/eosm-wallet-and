package com.token.mangowallet.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.token.mangowallet.utils.Constants;

public class TokenInfo implements Parcelable {
    public final String address;
    public final String name;
    public final String symbol;
    public final int decimals;
    public final Constants.WalletType walletType;

    public TokenInfo(String address, String name, String symbol, int decimals, Constants.WalletType walletType) {
        this.address = address;
        this.name = name;
        this.symbol = symbol;
        this.decimals = decimals;
        this.walletType = walletType;
    }

    private TokenInfo(Parcel in) {
        address = in.readString();
        name = in.readString();
        symbol = in.readString();
        decimals = in.readInt();
        walletType = Constants.WalletType.getPagerFromPositon(in.readInt());
    }

    public static final Creator<TokenInfo> CREATOR = new Creator<TokenInfo>() {
        @Override
        public TokenInfo createFromParcel(Parcel in) {
            return new TokenInfo(in);
        }

        @Override
        public TokenInfo[] newArray(int size) {
            return new TokenInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(name);
        dest.writeString(symbol);
        dest.writeInt(decimals);
        dest.writeInt(walletType.getType());
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", decimals=" + decimals +
                ", walletType=" + walletType +
                '}';
    }
}
