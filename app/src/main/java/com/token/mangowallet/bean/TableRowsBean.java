package com.token.mangowallet.bean;

import java.util.List;

public class TableRowsBean {


    /**
     * rows : [{"supply":"10000000000.0000 RAMCORE","base":{"balance":"67580740409 RAM","weight":"0.50000000000000000"},"quote":{"balance":"203370.0031 MGP","weight":"0.50000000000000000"}}]
     * more : false
     */

    private boolean more;
    private List<RowsBean> rows;

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * supply : 10000000000.0000 RAMCORE
         * base : {"balance":"67580740409 RAM","weight":"0.50000000000000000"}
         * quote : {"balance":"203370.0031 MGP","weight":"0.50000000000000000"}
         */

        private String account;
        private String remaining;
        private String supply;
        private BaseBean base;
        private QuoteBean quote;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getRemaining() {
            return remaining;
        }

        public void setRemaining(String remaining) {
            this.remaining = remaining;
        }

        public String getSupply() {
            return supply;
        }

        public void setSupply(String supply) {
            this.supply = supply;
        }

        public BaseBean getBase() {
            return base;
        }

        public void setBase(BaseBean base) {
            this.base = base;
        }

        public QuoteBean getQuote() {
            return quote;
        }

        public void setQuote(QuoteBean quote) {
            this.quote = quote;
        }

        public static class BaseBean {
            /**
             * balance : 67580740409 RAM
             * weight : 0.50000000000000000
             */

            private String balance;
            private String weight;

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public String getWeight() {
                return weight;
            }

            public void setWeight(String weight) {
                this.weight = weight;
            }
        }

        public static class QuoteBean {
            /**
             * balance : 203370.0031 MGP
             * weight : 0.50000000000000000
             */

            private String balance;
            private String weight;

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public String getWeight() {
                return weight;
            }

            public void setWeight(String weight) {
                this.weight = weight;
            }
        }
    }
}
