package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.blankj.utilcode.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;


public class SelordersBean {
    /**
     * rows : [{"id":0,"owner":"mgptest11111","accepted_payments":[],"price":"0.69 CNY","price_usd":"0.1064 USD","quantity":"1000.0000 MGP","min_accept_quantity":"10.00 CNY","frozen_quantity":"1000.0000 MGP","fulfilled_quantity":"0.0000 MGP","closed":0,"created_at":"2021-01-29T03:29:00","closed_at":"1970-01-01T00:00:00"},{"id":1,"owner":"mgptest11111","accepted_payments":[],"price":"0.69 CNY","price_usd":"0.1064 USD","quantity":"30.0000 MGP","min_accept_quantity":"11.00 CNY","frozen_quantity":"16.0000 MGP","fulfilled_quantity":"0.0000 MGP","closed":0,"created_at":"2021-01-29T03:30:54","closed_at":"1970-01-01T00:00:00"},{"id":2,"owner":"mgptest11111","accepted_payments":[2,5],"price":"0.69 CNY","price_usd":"0.1064 USD","quantity":"50.0000 MGP","min_accept_quantity":"11.00 CNY","frozen_quantity":"34.0000 MGP","fulfilled_quantity":"16.0000 MGP","closed":0,"created_at":"2021-01-29T06:02:11","closed_at":"1970-01-01T00:00:00"},{"id":3,"owner":"mgptest11111","accepted_payments":[5],"price":"0.69 CNY","price_usd":"0.1064 USD","quantity":"200.0000 MGP","min_accept_quantity":"1.00 CNY","frozen_quantity":"200.0000 MGP","fulfilled_quantity":"0.0000 MGP","closed":0,"created_at":"2021-02-03T08:48:32","closed_at":"1970-01-01T00:00:00"},{"id":4,"owner":"mgptest11111","accepted_payments":[5],"price":"0.23 CNY","price_usd":"0.0354 USD","quantity":"636.4584 MGP","min_accept_quantity":"10.00 CNY","frozen_quantity":"636.4347 MGP","fulfilled_quantity":"0.0000 MGP","closed":0,"created_at":"2021-02-03T09:09:41","closed_at":"1970-01-01T00:00:00"},{"id":5,"owner":"mgptest11111","accepted_payments":[5],"price":"0.69 CNY","price_usd":"0.1064 USD","quantity":"100.0000 MGP","min_accept_quantity":"1.00 CNY","frozen_quantity":"0.0000 MGP","fulfilled_quantity":"0.0000 MGP","closed":0,"created_at":"2021-02-04T10:40:33","closed_at":"1970-01-01T00:00:00"}]
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

    public static class RowsBean implements Parcelable, Comparator<RowsBean> {
        /**
         * id : 0
         * owner : mgptest11111
         * accepted_payments : []
         * price : 0.69 CNY
         * price_usd : 0.1064 USD
         * quantity : 1000.0000 MGP
         * min_accept_quantity : 10.00 CNY
         * frozen_quantity : 1000.0000 MGP
         * fulfilled_quantity : 0.0000 MGP
         * closed : 0
         * created_at : 2021-01-29T03:29:00
         * closed_at : 1970-01-01T00:00:00
         */

        private int id;
        private String owner;
        private String price;
        private String price_usd;
        private String quantity;
        private String min_accept_quantity;
        private String frozen_quantity;
        private String fulfilled_quantity;
        private int closed;
        private String created_at;
        private String closed_at;
        private List<Integer> accepted_payments;

        protected RowsBean(Parcel in) {
            id = in.readInt();
            owner = in.readString();
            price = in.readString();
            price_usd = in.readString();
            quantity = in.readString();
            min_accept_quantity = in.readString();
            frozen_quantity = in.readString();
            fulfilled_quantity = in.readString();
            closed = in.readInt();
            created_at = in.readString();
            closed_at = in.readString();
            in.readList(accepted_payments, Integer.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(owner);
            dest.writeString(price);
            dest.writeString(price_usd);
            dest.writeString(quantity);
            dest.writeString(min_accept_quantity);
            dest.writeString(frozen_quantity);
            dest.writeString(fulfilled_quantity);
            dest.writeInt(closed);
            dest.writeString(created_at);
            dest.writeString(closed_at);
            dest.writeList(accepted_payments);
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

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPrice_usd() {
            return price_usd;
        }

        public void setPrice_usd(String price_usd) {
            this.price_usd = price_usd;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getMin_accept_quantity() {
            return min_accept_quantity;
        }

        public void setMin_accept_quantity(String min_accept_quantity) {
            this.min_accept_quantity = min_accept_quantity;
        }

        public String getFrozen_quantity() {
            return frozen_quantity;
        }

        public void setFrozen_quantity(String frozen_quantity) {
            this.frozen_quantity = frozen_quantity;
        }

        public String getFulfilled_quantity() {
            return fulfilled_quantity;
        }

        public void setFulfilled_quantity(String fulfilled_quantity) {
            this.fulfilled_quantity = fulfilled_quantity;
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

        public List<Integer> getAccepted_payments() {
            return accepted_payments;
        }

        public void setAccepted_payments(List<Integer> accepted_payments) {
            this.accepted_payments = accepted_payments;
        }

        @Override
        public int compare(RowsBean o1, RowsBean o2) {
            int compare;
            String price1 = ObjectUtils.isEmpty(o1.getPrice()) ? "0.00 CNY" : o1.getPrice();
            String price2 = ObjectUtils.isEmpty(o2.getPrice()) ? "0.00 CNY" : o2.getPrice();
            price1 = price1.split(" ")[0];
            price2 = price2.split(" ")[0];
            BigDecimal priceDecimal1 = new BigDecimal(ObjectUtils.isEmpty(price1) ? "0" : price1);
            BigDecimal priceDecimal2 = new BigDecimal(ObjectUtils.isEmpty(price2) ? "0" : price2);
            if (priceDecimal1.compareTo(priceDecimal2) > 0) {//-1表示小于，0是等于，1是大于。
                compare = 1;
            } else if (priceDecimal1.compareTo(priceDecimal2) < 0) {
                compare = -1;
            } else {
                compare = 0;
            }
            return compare;
        }
    }

//
//    /**
//     * rows : [{"id":0,"owner":"mgptest11111","price":"1000.00 CNY","quantity":"1000.0000 MGP","min_accept_quantity":"10.00 CNY","frozen_quantity":"0 ","fulfilled_quantity":"0 ","closed":1,"created_at":"2020-12-31T03:05:50","closed_at":"2020-12-31T03:06:32"}
//     * ,{"id":1,"owner":"mgptest11111","price":"1000.00 CNY","quantity":"1000.0000 MGP","min_accept_quantity":"10.00 CNY","frozen_quantity":"0 ","fulfilled_quantity":"0 ","closed":1,"created_at":"2020-12-31T03:06:45","closed_at":"2020-12-31T03:43:37"},{"id":2,"owner":"mgptest11111","price":"1000.00 CNY","quantity":"1000.0000 MGP","min_accept_quantity":"10.00 CNY","frozen_quantity":"0.0000 MGP","fulfilled_quantity":"10.0000 MGP","closed":1,"created_at":"2020-12-31T03:43:44","closed_at":"2020-12-31T08:53:07"},{"id":3,"owner":"mgptest11111","price":"1000.00 CNY","quantity":"1000.0000 MGP","min_accept_quantity":"10.00 CNY","frozen_quantity":"0.0000 MGP","fulfilled_quantity":"0.0000 MGP","closed":1,"created_at":"2020-12-31T07:34:23","closed_at":"2020-12-31T07:34:59"},{"id":4,"owner":"mgptest11111","price":"1000.00 CNY","quantity":"1000.0000 MGP","min_accept_quantity":"10.00 CNY","frozen_quantity":"0.0000 MGP","fulfilled_quantity":"0.0000 MGP","closed":1,"created_at":"2020-12-31T07:34:31","closed_at":"2020-12-31T07:35:25"},{"id":5,"owner":"mgptest11111","price":"1.00 CNY","quantity":"50.0000 MGP","min_accept_quantity":"10.00 CNY","frozen_quantity":"42.0000 MGP","fulfilled_quantity":"0.0000 MGP","closed":0,"created_at":"2021-01-04T03:09:27","closed_at":"1970-01-01T00:00:00"},{"id":6,"owner":"mgptest11111","price":"1.00 CNY","quantity":"100.0000 MGP","min_accept_quantity":"10.00 CNY","frozen_quantity":"30.0000 MGP","fufilled_quantity":"0.0000 MGP","closed":0,"created_at":"2021-01-04T10:26:25","closed_at":"1970-01-01T00:00:00"},{"id":7,"owner":"mgptest11111","price":"0.69 CNY","quantity":"30.0000 MGP","min_accept_quantity":"2.00 CNY","frozen_quantity":"3.6232 MGP","fufilled_quantity":"0.0000 MGP","closed":0,"created_at":"2021-01-05T06:03:54","closed_at":"1970-01-01T00:00:00"},{"id":8,"owner":"mgptest11111","price":"0.69 CNY","quantity":"10.0000 MGP","min_accept_quantity":"1.00 CNY","frozen_quantity":"0.0000 MGP","fufilled_quantity":"0.0000 MGP","closed":1,"created_at":"2021-01-05T10:34:58","closed_at":"2021-01-05T10:35:33"},{"id":9,"owner":"mgptest11111","price":"2.69 CNY","quantity":"10.0000 MGP","min_accept_quantity":"6.73 CNY","frozen_quantity":"0.0000 MGP","fufilled_quantity":"0.0000 MGP","closed":0,"created_at":"2021-01-06T03:38:42","closed_at":"1970-01-01T00:00:00"}]
//     * more : false
//     * next_key :
//     */
//
//    private boolean more;
//    private String next_key;
//    private List<RowsBean> rows;
//
//    public boolean isMore() {
//        return more;
//    }
//
//    public void setMore(boolean more) {
//        this.more = more;
//    }
//
//    public String getNext_key() {
//        return next_key;
//    }
//
//    public void setNext_key(String next_key) {
//        this.next_key = next_key;
//    }
//
//    public List<RowsBean> getRows() {
//        return rows;
//    }
//
//    public void setRows(List<RowsBean> rows) {
//        this.rows = rows;
//    }
//
//    public static class RowsBean implements Parcelable, Comparator<SelordersBean.RowsBean> {
//        /**
//         * id : 0
//         * owner : mgptest11111
//         * price : 1000.00 CNY
//         * quantity : 1000.0000 MGP
//         * min_accept_quantity : 10.00 CNY
//         * frozen_quantity : 0
//         * fulfilled_quantity : 0
//         * closed : 1
//         * created_at : 2020-12-31T03:05:50
//         * closed_at : 2020-12-31T03:06:32
//         */
//
//        private int id;
//        private String owner;
//        private String price;
//        private String quantity;
//        private String min_accept_quantity;
//        private String frozen_quantity;
//        private String fulfilled_quantity;
//        private int closed;
//        private String created_at;
//        private String closed_at;
//
//        protected RowsBean(Parcel in) {
//            id = in.readInt();
//            owner = in.readString();
//            price = in.readString();
//            quantity = in.readString();
//            min_accept_quantity = in.readString();
//            frozen_quantity = in.readString();
//            fulfilled_quantity = in.readString();
//            closed = in.readInt();
//            created_at = in.readString();
//            closed_at = in.readString();
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            dest.writeInt(id);
//            dest.writeString(owner);
//            dest.writeString(price);
//            dest.writeString(quantity);
//            dest.writeString(min_accept_quantity);
//            dest.writeString(frozen_quantity);
//            dest.writeString(fulfilled_quantity);
//            dest.writeInt(closed);
//            dest.writeString(created_at);
//            dest.writeString(closed_at);
//        }
//
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        public static final Creator<RowsBean> CREATOR = new Creator<RowsBean>() {
//            @Override
//            public RowsBean createFromParcel(Parcel in) {
//                return new RowsBean(in);
//            }
//
//            @Override
//            public RowsBean[] newArray(int size) {
//                return new RowsBean[size];
//            }
//        };
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getOwner() {
//            return owner;
//        }
//
//        public void setOwner(String owner) {
//            this.owner = owner;
//        }
//
//        public String getPrice() {
//            return price;
//        }
//
//        public void setPrice(String price) {
//            this.price = price;
//        }
//
//        public String getQuantity() {
//            return quantity;
//        }
//
//        public void setQuantity(String quantity) {
//            this.quantity = quantity;
//        }
//
//        public String getMin_accept_quantity() {
//            return min_accept_quantity;
//        }
//
//        public void setMin_accept_quantity(String min_accept_quantity) {
//            this.min_accept_quantity = min_accept_quantity;
//        }
//
//        public String getFrozen_quantity() {
//            return frozen_quantity;
//        }
//
//        public void setFrozen_quantity(String frozen_quantity) {
//            this.frozen_quantity = frozen_quantity;
//        }
//
//        public String getFufilled_quantity() {
//            return fulfilled_quantity;
//        }
//
//        public void setFufilled_quantity(String fulfilled_quantity) {
//            this.fulfilled_quantity = fulfilled_quantity;
//        }
//
//        public int getClosed() {
//            return closed;
//        }
//
//        public void setClosed(int closed) {
//            this.closed = closed;
//        }
//
//        public String getCreated_at() {
//            return created_at;
//        }
//
//        public void setCreated_at(String created_at) {
//            this.created_at = created_at;
//        }
//
//        public String getClosed_at() {
//            return closed_at;
//        }
//
//        public void setClosed_at(String closed_at) {
//            this.closed_at = closed_at;
//        }
//
//        @Override
//        public int compare(RowsBean o1, RowsBean o2) {
//            int compare;
//            String price1 = ObjectUtils.isEmpty(o1.getPrice()) ? "0.00 CNY" : o1.getPrice();
//            String price2 = ObjectUtils.isEmpty(o2.getPrice()) ? "0.00 CNY" : o2.getPrice();
//            price1 = price1.split(" ")[0];
//            price2 = price2.split(" ")[0];
//            BigDecimal priceDecimal1 = new BigDecimal(ObjectUtils.isEmpty(price1) ? "0" : price1);
//            BigDecimal priceDecimal2 = new BigDecimal(ObjectUtils.isEmpty(price2) ? "0" : price2);
//            if (priceDecimal1.compareTo(priceDecimal2) > 0) {//-1表示小于，0是等于，1是大于。
//                compare = 1;
//            } else if (priceDecimal1.compareTo(priceDecimal2) < 0) {
//                compare = -1;
//            } else {
//                compare = 0;
//            }
//            return compare;
//        }
//    }
}
