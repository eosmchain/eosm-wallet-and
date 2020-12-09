package com.token.mangowallet.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;

import com.token.mangowallet.utils.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

@Entity
public class MangoWallet implements Parcelable {

    @Id(autoincrement = true)
    private Long id;
    @DrawableRes
    public int image;
    @NotNull
    private int walletType;
    @NotNull
    private String walletPassword;
    private String hint;
    private String WalletAddress;
    @NotNull
    private String privateKey;
    private String publicKey;
    private String keystore;
    @Convert(columnType = String.class, converter = StringConverter.class)
    List<String> mnemonicCode;
    @Convert(columnType = String.class, converter = StringConverter.class)
    List<String> tokens;

    protected MangoWallet(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        image = in.readInt();
        walletType = in.readInt();
        walletPassword = in.readString();
        hint = in.readString();
        WalletAddress = in.readString();
        privateKey = in.readString();
        publicKey = in.readString();
        keystore = in.readString();
        mnemonicCode = in.createStringArrayList();
        tokens = in.createStringArrayList();
    }

    @Generated(hash = 1843577074)
    public MangoWallet(Long id, int image, int walletType,
            @NotNull String walletPassword, String hint, String WalletAddress,
            @NotNull String privateKey, String publicKey, String keystore,
            List<String> mnemonicCode, List<String> tokens) {
        this.id = id;
        this.image = image;
        this.walletType = walletType;
        this.walletPassword = walletPassword;
        this.hint = hint;
        this.WalletAddress = WalletAddress;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.keystore = keystore;
        this.mnemonicCode = mnemonicCode;
        this.tokens = tokens;
    }

    @Generated(hash = 2114901284)
    public MangoWallet() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeInt(image);
        dest.writeInt(walletType);
        dest.writeString(walletPassword);
        dest.writeString(hint);
        dest.writeString(WalletAddress);
        dest.writeString(privateKey);
        dest.writeString(publicKey);
        dest.writeString(keystore);
        dest.writeStringList(mnemonicCode);
        dest.writeStringList(tokens);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MangoWallet> CREATOR = new Creator<MangoWallet>() {
        @Override
        public MangoWallet createFromParcel(Parcel in) {
            return new MangoWallet(in);
        }

        @Override
        public MangoWallet[] newArray(int size) {
            return new MangoWallet[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getWalletType() {
        return walletType;
    }

    public void setWalletType(int walletType) {
        this.walletType = walletType;
    }

    public String getWalletPassword() {
        return walletPassword;
    }

    public void setWalletPassword(String walletPassword) {
        this.walletPassword = walletPassword;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public List<String> getMnemonicCode() {
        return mnemonicCode;
    }

    public void setMnemonicCode(List<String> mnemonicCode) {
        this.mnemonicCode = mnemonicCode;
    }

    public String getWalletAddress() {
        return this.WalletAddress;
    }

    public void setWalletAddress(String WalletAddress) {
        this.WalletAddress = WalletAddress;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    @Override
    public String toString() {
        return "MangoWallet{" +
                "id=" + id +
                ", image=" + image +
                ", walletType=" + walletType +
                ", walletPassword='" + walletPassword + '\'' +
                ", hint='" + hint + '\'' +
                ", WalletAddress='" + WalletAddress + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", keystore='" + keystore + '\'' +
                ", mnemonicCode=" + mnemonicCode +
                ", tokens=" + tokens +
                '}';
    }
}
