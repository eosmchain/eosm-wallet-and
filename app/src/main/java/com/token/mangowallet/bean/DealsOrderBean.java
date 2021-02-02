package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class DealsOrderBean {

    /**
     * rows : [{"id":10,"order_id":1,"order_price":"0.69 CNY","order_price_usd":"0.1064 USD","deal_quantity":"16.0000 MGP","order_maker":"mgptest11111","maker_passed":0,"maker_passed_at":"1970-01-01T00:00:00","order_taker":"mgptest11111","taker_passed":0,"taker_passed_at":"1970-01-01T00:00:00","arbiter":"","arbiter_passed":0,"arbiter_passed_at":"1970-01-01T00:00:00","closed":0,"created_at":"2021-02-02T03:00:18","closed_at":"1970-01-01T00:00:00","order_sn":"1612234819889","pay_type":0,"expired_at":"2021-02-02T03:15:18","maker_expired_at":"1970-01-01T00:00:00","restart_taker_num":0,"restart_maker_num":0}]
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
         * id : 10
         * order_id : 1
         * order_price : 0.69 CNY
         * order_price_usd : 0.1064 USD
         * deal_quantity : 16.0000 MGP
         * order_maker : mgptest11111
         * maker_passed : 0
         * maker_passed_at : 1970-01-01T00:00:00
         * order_taker : mgptest11111
         * taker_passed : 0
         * taker_passed_at : 1970-01-01T00:00:00
         * arbiter :
         * arbiter_passed : 0
         * arbiter_passed_at : 1970-01-01T00:00:00
         * closed : 0
         * created_at : 2021-02-02T03:00:18
         * closed_at : 1970-01-01T00:00:00
         * order_sn : 1612234819889
         * pay_type : 0
         * expired_at : 2021-02-02T03:15:18
         * maker_expired_at : 1970-01-01T00:00:00
         * restart_taker_num : 0
         * restart_maker_num : 0
         */

        private int id;
        private int order_id;
        private String order_price;
        private String order_price_usd;
        private String deal_quantity;
        private String order_maker;
        private int maker_passed;
        private String maker_passed_at;
        private String order_taker;
        private int taker_passed;
        private String taker_passed_at;
        private String arbiter;
        private int arbiter_passed;
        private String arbiter_passed_at;
        private int closed;
        private String created_at;
        private String closed_at;
        private String order_sn;
        private int pay_type;
        private String expired_at;
        private String maker_expired_at;
        private int restart_taker_num;
        private int restart_maker_num;

        protected RowsBean(Parcel in) {
            id = in.readInt();
            order_id = in.readInt();
            order_price = in.readString();
            order_price_usd = in.readString();
            deal_quantity = in.readString();
            order_maker = in.readString();
            maker_passed = in.readInt();
            maker_passed_at = in.readString();
            order_taker = in.readString();
            taker_passed = in.readInt();
            taker_passed_at = in.readString();
            arbiter = in.readString();
            arbiter_passed = in.readInt();
            arbiter_passed_at = in.readString();
            closed = in.readInt();
            created_at = in.readString();
            closed_at = in.readString();
            order_sn = in.readString();
            pay_type = in.readInt();
            expired_at = in.readString();
            maker_expired_at = in.readString();
            restart_taker_num = in.readInt();
            restart_maker_num = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeInt(order_id);
            dest.writeString(order_price);
            dest.writeString(order_price_usd);
            dest.writeString(deal_quantity);
            dest.writeString(order_maker);
            dest.writeInt(maker_passed);
            dest.writeString(maker_passed_at);
            dest.writeString(order_taker);
            dest.writeInt(taker_passed);
            dest.writeString(taker_passed_at);
            dest.writeString(arbiter);
            dest.writeInt(arbiter_passed);
            dest.writeString(arbiter_passed_at);
            dest.writeInt(closed);
            dest.writeString(created_at);
            dest.writeString(closed_at);
            dest.writeString(order_sn);
            dest.writeInt(pay_type);
            dest.writeString(expired_at);
            dest.writeString(maker_expired_at);
            dest.writeInt(restart_taker_num);
            dest.writeInt(restart_maker_num);
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public String getOrder_price() {
            return order_price;
        }

        public void setOrder_price(String order_price) {
            this.order_price = order_price;
        }

        public String getOrder_price_usd() {
            return order_price_usd;
        }

        public void setOrder_price_usd(String order_price_usd) {
            this.order_price_usd = order_price_usd;
        }

        public String getDeal_quantity() {
            return deal_quantity;
        }

        public void setDeal_quantity(String deal_quantity) {
            this.deal_quantity = deal_quantity;
        }

        public String getOrder_maker() {
            return order_maker;
        }

        public void setOrder_maker(String order_maker) {
            this.order_maker = order_maker;
        }

        public int getMaker_passed() {
            return maker_passed;
        }

        public void setMaker_passed(int maker_passed) {
            this.maker_passed = maker_passed;
        }

        public String getMaker_passed_at() {
            return maker_passed_at;
        }

        public void setMaker_passed_at(String maker_passed_at) {
            this.maker_passed_at = maker_passed_at;
        }

        public String getOrder_taker() {
            return order_taker;
        }

        public void setOrder_taker(String order_taker) {
            this.order_taker = order_taker;
        }

        public int getTaker_passed() {
            return taker_passed;
        }

        public void setTaker_passed(int taker_passed) {
            this.taker_passed = taker_passed;
        }

        public String getTaker_passed_at() {
            return taker_passed_at;
        }

        public void setTaker_passed_at(String taker_passed_at) {
            this.taker_passed_at = taker_passed_at;
        }

        public String getArbiter() {
            return arbiter;
        }

        public void setArbiter(String arbiter) {
            this.arbiter = arbiter;
        }

        public int getArbiter_passed() {
            return arbiter_passed;
        }

        public void setArbiter_passed(int arbiter_passed) {
            this.arbiter_passed = arbiter_passed;
        }

        public String getArbiter_passed_at() {
            return arbiter_passed_at;
        }

        public void setArbiter_passed_at(String arbiter_passed_at) {
            this.arbiter_passed_at = arbiter_passed_at;
        }

        public int getClosed() {
            return closed;
        }

        public void setClosed(int closed) {
            this.closed = closed;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getClosed_at() {
            return closed_at;
        }

        public void setClosed_at(String closed_at) {
            this.closed_at = closed_at;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public int getPay_type() {
            return pay_type;
        }

        public void setPay_type(int pay_type) {
            this.pay_type = pay_type;
        }

        public String getExpired_at() {
            return expired_at;
        }

        public void setExpired_at(String expired_at) {
            this.expired_at = expired_at;
        }

        public String getMaker_expired_at() {
            return maker_expired_at;
        }

        public void setMaker_expired_at(String maker_expired_at) {
            this.maker_expired_at = maker_expired_at;
        }

        public int getRestart_taker_num() {
            return restart_taker_num;
        }

        public void setRestart_taker_num(int restart_taker_num) {
            this.restart_taker_num = restart_taker_num;
        }

        public int getRestart_maker_num() {
            return restart_maker_num;
        }

        public void setRestart_maker_num(int restart_maker_num) {
            this.restart_maker_num = restart_maker_num;
        }
    }
}
