package com.token.mangowallet.bean;

public class CurrencyStatsBean {

    /**
     * MGP : {"supply":"150000000.0000 MGP","max_supply":"500000000.0000 MGP","issuer":"eosio"}
     */

    private MGPBean MGP;

    public MGPBean getMGP() {
        return MGP;
    }

    public void setMGP(MGPBean MGP) {
        this.MGP = MGP;
    }

    public static class MGPBean {
        /**
         * supply : 150000000.0000 MGP
         * max_supply : 500000000.0000 MGP
         * issuer : eosio
         */

        private String supply;
        private String max_supply;
        private String issuer;

        public String getSupply() {
            return supply;
        }

        public void setSupply(String supply) {
            this.supply = supply;
        }

        public String getMax_supply() {
            return max_supply;
        }

        public void setMax_supply(String max_supply) {
            this.max_supply = max_supply;
        }

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }
    }
}
