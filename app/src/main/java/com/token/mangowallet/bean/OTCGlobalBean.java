package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

public class OTCGlobalBean {

    /**
     * rows : [{"min_buy_order_quantity":"0.0010 MGP","min_sell_order_quantity":"0.0010 MGP","min_pos_stake_quantity":"0.0010 MGP","pos_staking_contract":"addressbookt","withhold_expire_sec":900,"transaction_fee_receiver":"mwalletadmin","transaction_fee_ratio":0,"otc_arbiters":["mwalletadmin","testchenhanl","testzyuting1"],"cs_contact_title":"Custom Service Contact","cs_contact":"cs_contact_mango"}]
     * more : false
     * next_key :
     */

    private boolean more;
    private String next_key;
    private List<RowsBean> rows;

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public String getNext_key() {
        return next_key;
    }

    public void setNext_key(String next_key) {
        this.next_key = next_key;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean implements Parcelable {
        /**
         * min_buy_order_quantity : 0.0010 MGP
         * min_sell_order_quantity : 0.0010 MGP
         * min_pos_stake_quantity : 0.0010 MGP
         * pos_staking_contract : addressbookt
         * withhold_expire_sec : 900
         * transaction_fee_receiver : mwalletadmin
         * transaction_fee_ratio : 0
         * otc_arbiters : ["mwalletadmin","testchenhanl","testzyuting1"]
         * cs_contact_title : Custom Service Contact
         * cs_contact : cs_contact_mango
         */

        private String min_buy_order_quantity;
        private String min_sell_order_quantity;
        private String min_pos_stake_quantity;
        private String pos_staking_contract;
        private BigDecimal withhold_expire_sec;
        private String transaction_fee_receiver;
        private int transaction_fee_ratio;
        private String cs_contact_title;
        private String cs_contact;
        private List<String> otc_arbiters;

        protected RowsBean(Parcel in) {
            min_buy_order_quantity = in.readString();
            min_sell_order_quantity = in.readString();
            min_pos_stake_quantity = in.readString();
            pos_staking_contract = in.readString();
            withhold_expire_sec = new BigDecimal(in.readString());
            transaction_fee_receiver = in.readString();
            transaction_fee_ratio = in.readInt();
            cs_contact_title = in.readString();
            cs_contact = in.readString();
            otc_arbiters = in.createStringArrayList();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(min_buy_order_quantity);
            dest.writeString(min_sell_order_quantity);
            dest.writeString(min_pos_stake_quantity);
            dest.writeString(pos_staking_contract);
            dest.writeString(withhold_expire_sec.toPlainString());
            dest.writeString(transaction_fee_receiver);
            dest.writeInt(transaction_fee_ratio);
            dest.writeString(cs_contact_title);
            dest.writeString(cs_contact);
            dest.writeStringList(otc_arbiters);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<RowsBean> CREATOR = new Creator<RowsBean>() {
            @Override
            public RowsBean createFromParcel(Parcel in) {
                return new RowsBean(in);
            }

            @Override
            public RowsBean[] newArray(int size) {
                return new RowsBean[size];
            }
        };

        public String getMin_buy_order_quantity() {
            return min_buy_order_quantity;
        }

        public void setMin_buy_order_quantity(String min_buy_order_quantity) {
            this.min_buy_order_quantity = min_buy_order_quantity;
        }

        public String getMin_sell_order_quantity() {
            return min_sell_order_quantity;
        }

        public void setMin_sell_order_quantity(String min_sell_order_quantity) {
            this.min_sell_order_quantity = min_sell_order_quantity;
        }

        public String getMin_pos_stake_quantity() {
            return min_pos_stake_quantity;
        }

        public void setMin_pos_stake_quantity(String min_pos_stake_quantity) {
            this.min_pos_stake_quantity = min_pos_stake_quantity;
        }

        public String getPos_staking_contract() {
            return pos_staking_contract;
        }

        public void setPos_staking_contract(String pos_staking_contract) {
            this.pos_staking_contract = pos_staking_contract;
        }

        public BigDecimal getWithhold_expire_sec() {
            return withhold_expire_sec;
        }

        public void setWithhold_expire_sec(BigDecimal withhold_expire_sec) {
            this.withhold_expire_sec = withhold_expire_sec;
        }

        public String getTransaction_fee_receiver() {
            return transaction_fee_receiver;
        }

        public void setTransaction_fee_receiver(String transaction_fee_receiver) {
            this.transaction_fee_receiver = transaction_fee_receiver;
        }

        public int getTransaction_fee_ratio() {
            return transaction_fee_ratio;
        }

        public void setTransaction_fee_ratio(int transaction_fee_ratio) {
            this.transaction_fee_ratio = transaction_fee_ratio;
        }

        public String getCs_contact_title() {
            return cs_contact_title;
        }

        public void setCs_contact_title(String cs_contact_title) {
            this.cs_contact_title = cs_contact_title;
        }

        public String getCs_contact() {
            return cs_contact;
        }

        public void setCs_contact(String cs_contact) {
            this.cs_contact = cs_contact;
        }

        public List<String> getOtc_arbiters() {
            return otc_arbiters;
        }

        public void setOtc_arbiters(List<String> otc_arbiters) {
            this.otc_arbiters = otc_arbiters;
        }
    }
}
