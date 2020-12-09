package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class CreateAccountMode implements Parcelable {

    /**
     * active : EOS5Q26ftdpGPvcf2A3FJm342jjTmjRjvTv2QsYNch2TLp96btm4P
     * owner : EOS5Q26ftdpGPvcf2A3FJm342jjTmjRjvTv2QsYNch2TLp96btm4P
     * account : aaabbbcccddd
     * type : 0
     * blockchain : EOS
     */

    private String active;
    private String owner;
    private String account;
    private int type;
    private String blockchain;

    public CreateAccountMode() {

    }

    protected CreateAccountMode(Parcel in) {
        active = in.readString();
        owner = in.readString();
        account = in.readString();
        type = in.readInt();
        blockchain = in.readString();
    }

    public static final Creator<CreateAccountMode> CREATOR = new Creator<CreateAccountMode>() {
        @Override
        public CreateAccountMode createFromParcel(Parcel in) {
            return new CreateAccountMode(in);
        }

        @Override
        public CreateAccountMode[] newArray(int size) {
            return new CreateAccountMode[size];
        }
    };

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(String blockchain) {
        this.blockchain = blockchain;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(active);
        dest.writeString(owner);
        dest.writeString(account);
        dest.writeInt(type);
        dest.writeString(blockchain);
    }
}
