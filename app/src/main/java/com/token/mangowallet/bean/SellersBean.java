package com.token.mangowallet.bean;

import java.util.List;

public class SellersBean {

    /**
     * rows : [{"owner":"guo222222222","available_quantity":"250.0000 MGP","accepted_payments":[],"processed_deals":0,"email":"","memo":""}]
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
         * owner : guo222222222
         * available_quantity : 250.0000 MGP
         * accepted_payments : []
         * processed_deals : 0
         * email :
         * memo :
         */

        private String owner;
        private String available_quantity;
        private int processed_deals;
        private String email;
        private String memo;
        private List<Integer> accepted_payments;

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getAvailable_quantity() {
            return available_quantity;
        }

        public void setAvailable_quantity(String available_quantity) {
            this.available_quantity = available_quantity;
        }

        public int getProcessed_deals() {
            return processed_deals;
        }

        public void setProcessed_deals(int processed_deals) {
            this.processed_deals = processed_deals;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public List<Integer> getAccepted_payments() {
            return accepted_payments;
        }

        public void setAccepted_payments(List<Integer> accepted_payments) {
            this.accepted_payments = accepted_payments;
        }
    }
}
