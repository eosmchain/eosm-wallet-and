package com.token.mangowallet.bean;

import com.blankj.utilcode.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;

public class SelordersBean {

    /**
     * rows : [{"id":0,"owner":"mgptest11111","price":"1000.00 CNY","quantity":"1000.0000 MGP","min_accept_quantity":"10.00 CNY","frozen_quantity":"0 ","fufilled_quantity":"0 ","closed":1,"created_at":"2020-12-31T03:05:50","closed_at":"2020-12-31T03:06:32"},{"id":1,"owner":"mgptest11111","price":"1000.00 CNY","quantity":"1000.0000 MGP","min_accept_quantity":"10.00 CNY","frozen_quantity":"0 ","fufilled_quantity":"0 ","closed":1,"created_at":"2020-12-31T03:06:45","closed_at":"2020-12-31T03:43:37"},{"id":2,"owner":"mgptest11111","price":"1000.00 CNY","quantity":"1000.0000 MGP","min_accept_quantity":"10.00 CNY","frozen_quantity":"0.0000 MGP","fufilled_quantity":"10.0000 MGP","closed":1,"created_at":"2020-12-31T03:43:44","closed_at":"2020-12-31T08:53:07"},{"id":3,"owner":"mgptest11111","price":"1000.00 CNY","quantity":"1000.0000 MGP","min_accept_quantity":"10.00 CNY","frozen_quantity":"0.0000 MGP","fufilled_quantity":"0.0000 MGP","closed":1,"created_at":"2020-12-31T07:34:23","closed_at":"2020-12-31T07:34:59"},{"id":4,"owner":"mgptest11111","price":"1000.00 CNY","quantity":"1000.0000 MGP","min_accept_quantity":"10.00 CNY","frozen_quantity":"0.0000 MGP","fufilled_quantity":"0.0000 MGP","closed":1,"created_at":"2020-12-31T07:34:31","closed_at":"2020-12-31T07:35:25"},{"id":5,"owner":"mgptest11111","price":"1.00 CNY","quantity":"50.0000 MGP","min_accept_quantity":"10.00 CNY","frozen_quantity":"42.0000 MGP","fufilled_quantity":"0.0000 MGP","closed":0,"created_at":"2021-01-04T03:09:27","closed_at":"1970-01-01T00:00:00"},{"id":6,"owner":"mgptest11111","price":"1.00 CNY","quantity":"100.0000 MGP","min_accept_quantity":"10.00 CNY","frozen_quantity":"30.0000 MGP","fufilled_quantity":"0.0000 MGP","closed":0,"created_at":"2021-01-04T10:26:25","closed_at":"1970-01-01T00:00:00"},{"id":7,"owner":"mgptest11111","price":"0.69 CNY","quantity":"30.0000 MGP","min_accept_quantity":"2.00 CNY","frozen_quantity":"3.6232 MGP","fufilled_quantity":"0.0000 MGP","closed":0,"created_at":"2021-01-05T06:03:54","closed_at":"1970-01-01T00:00:00"},{"id":8,"owner":"mgptest11111","price":"0.69 CNY","quantity":"10.0000 MGP","min_accept_quantity":"1.00 CNY","frozen_quantity":"0.0000 MGP","fufilled_quantity":"0.0000 MGP","closed":1,"created_at":"2021-01-05T10:34:58","closed_at":"2021-01-05T10:35:33"},{"id":9,"owner":"mgptest11111","price":"2.69 CNY","quantity":"10.0000 MGP","min_accept_quantity":"6.73 CNY","frozen_quantity":"0.0000 MGP","fufilled_quantity":"0.0000 MGP","closed":0,"created_at":"2021-01-06T03:38:42","closed_at":"1970-01-01T00:00:00"}]
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

    public static class RowsBean {
        /**
         * id : 0
         * owner : mgptest11111
         * price : 1000.00 CNY
         * quantity : 1000.0000 MGP
         * min_accept_quantity : 10.00 CNY
         * frozen_quantity : 0
         * fufilled_quantity : 0
         * closed : 1
         * created_at : 2020-12-31T03:05:50
         * closed_at : 2020-12-31T03:06:32
         */

        private int id;
        private String owner;
        private String price;
        private String quantity;
        private String min_accept_quantity;
        private String frozen_quantity;
        private String fufilled_quantity;
        private int closed;
        private String created_at;
        private String closed_at;

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

        public String getFufilled_quantity() {
            return fufilled_quantity;
        }

        public void setFufilled_quantity(String fufilled_quantity) {
            this.fufilled_quantity = fufilled_quantity;
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
    }
}
