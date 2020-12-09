package com.token.mangowallet.bean;

import java.math.BigDecimal;
import java.util.List;

public class AccountInfo {

    /**
     * account_name : monneymakers
     * head_block_num : 22984822
     * head_block_time : 2020-05-07T02:49:55.500
     * privileged : false
     * last_code_update : 1970-01-01T00:00:00.000
     * created : 2019-12-19T10:09:05.500
     * core_liquid_balance : 3.0856 MGP
     * ram_quota : 34232871
     * net_weight : 599435000
     * cpu_weight : 599435000
     * net_limit : {"used":221,"available":"344800334743","max":"344800334964"}
     * cpu_limit : {"used":5670,"available":"32882151033","max":"32882156703"}
     * ram_usage : 4566
     * permissions : [{"perm_name":"active","parent":"owner","required_auth":{"threshold":1,"keys":[{"key":"EOS8AidVCXkstsx7PbjiuhTwhqj8FJfvcfimp5wpAcWSp1cP4WsYW","weight":1}],"accounts":[],"waits":[]}},{"perm_name":"owner","parent":"","required_auth":{"threshold":1,"keys":[{"key":"EOS8AidVCXkstsx7PbjiuhTwhqj8FJfvcfimp5wpAcWSp1cP4WsYW","weight":1}],"accounts":[],"waits":[]}}]
     * total_resources : {"owner":"monneymakers","net_weight":"59943.5000 MGP","cpu_weight":"59943.5000 MGP","ram_bytes":34231471}
     * self_delegated_bandwidth : {"from":"monneymakers","to":"monneymakers","net_weight":"59943.5000 MGP","cpu_weight":"59943.5000 MGP"}
     * refund_request : {"owner":"monneymakers","request_time":"2020-05-07T02:49:56","net_amount":"0.5000 MGP","cpu_amount":"0.5000 MGP"}
     * voter_info : {"owner":"monneymakers","proxy":"","producers":["mgproducer11","mgproducer12","mgproducer13","mgproducer14","mgproducer15","mgproducer21","mgproducer22","mgproducer23","mgproducer24","mgproducer25","mgproducer31","mgproducer32","mgproducer33","mgproducer34","mgproducer35","mgproducer41","mgproducer42","mgproducer43","mgproducer44","mgproducer45","mgproducer51"],"staked":1198870000,"last_vote_weight":"1663189796419222.75000000000000000","proxied_vote_weight":"0.00000000000000000","is_proxy":0,"flags1":0,"reserved2":0,"reserved3":"0 "}
     */

    private String account_name;
    private BigDecimal head_block_num;
    private String head_block_time;
    private boolean privileged;
    private String last_code_update;
    private String created;
    private String core_liquid_balance;
    private BigDecimal ram_quota;
    private BigDecimal net_weight;
    private BigDecimal cpu_weight;
    private NetLimitBean net_limit;
    private CpuLimitBean cpu_limit;
    private BigDecimal ram_usage;
    private TotalResourcesBean total_resources;
    private SelfDelegatedBandwidthBean self_delegated_bandwidth;
    private RefundRequestBean refund_request;
    private VoterInfoBean voter_info;
    private List<PermissionsBean> permissions;

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public BigDecimal getHead_block_num() {
        return head_block_num;
    }

    public void setHead_block_num(BigDecimal head_block_num) {
        this.head_block_num = head_block_num;
    }

    public String getHead_block_time() {
        return head_block_time;
    }

    public void setHead_block_time(String head_block_time) {
        this.head_block_time = head_block_time;
    }

    public boolean isPrivileged() {
        return privileged;
    }

    public void setPrivileged(boolean privileged) {
        this.privileged = privileged;
    }

    public String getLast_code_update() {
        return last_code_update;
    }

    public void setLast_code_update(String last_code_update) {
        this.last_code_update = last_code_update;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCore_liquid_balance() {
        return core_liquid_balance;
    }

    public void setCore_liquid_balance(String core_liquid_balance) {
        this.core_liquid_balance = core_liquid_balance;
    }

    public BigDecimal getRam_quota() {
        return ram_quota;
    }

    public void setRam_quota(BigDecimal ram_quota) {
        this.ram_quota = ram_quota;
    }

    public BigDecimal getNet_weight() {
        return net_weight;
    }

    public void setNet_weight(BigDecimal net_weight) {
        this.net_weight = net_weight;
    }

    public BigDecimal getCpu_weight() {
        return cpu_weight;
    }

    public void setCpu_weight(BigDecimal cpu_weight) {
        this.cpu_weight = cpu_weight;
    }

    public NetLimitBean getNet_limit() {
        return net_limit;
    }

    public void setNet_limit(NetLimitBean net_limit) {
        this.net_limit = net_limit;
    }

    public CpuLimitBean getCpu_limit() {
        return cpu_limit;
    }

    public void setCpu_limit(CpuLimitBean cpu_limit) {
        this.cpu_limit = cpu_limit;
    }

    public BigDecimal getRam_usage() {
        return ram_usage;
    }

    public void setRam_usage(BigDecimal ram_usage) {
        this.ram_usage = ram_usage;
    }

    public TotalResourcesBean getTotal_resources() {
        return total_resources;
    }

    public void setTotal_resources(TotalResourcesBean total_resources) {
        this.total_resources = total_resources;
    }

    public SelfDelegatedBandwidthBean getSelf_delegated_bandwidth() {
        return self_delegated_bandwidth;
    }

    public void setSelf_delegated_bandwidth(SelfDelegatedBandwidthBean self_delegated_bandwidth) {
        this.self_delegated_bandwidth = self_delegated_bandwidth;
    }

    public RefundRequestBean getRefund_request() {
        return refund_request;
    }

    public void setRefund_request(RefundRequestBean refund_request) {
        this.refund_request = refund_request;
    }

    public VoterInfoBean getVoter_info() {
        return voter_info;
    }

    public void setVoter_info(VoterInfoBean voter_info) {
        this.voter_info = voter_info;
    }

    public List<PermissionsBean> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionsBean> permissions) {
        this.permissions = permissions;
    }

    public static class NetLimitBean {
        /**
         * used : 221
         * available : 344800334743
         * max : 344800334964
         */

        private BigDecimal used;
        private BigDecimal available;
        private BigDecimal max;

        public BigDecimal getUsed() {
            return used;
        }

        public void setUsed(BigDecimal used) {
            this.used = used;
        }

        public BigDecimal getAvailable() {
            return available;
        }

        public void setAvailable(BigDecimal available) {
            this.available = available;
        }

        public BigDecimal getMax() {
            return max;
        }

        public void setMax(BigDecimal max) {
            this.max = max;
        }
    }

    public static class CpuLimitBean {
        /**
         * used : 5670
         * available : 32882151033
         * max : 32882156703
         */

        private BigDecimal used;
        private BigDecimal available;
        private BigDecimal max;

        public BigDecimal getUsed() {
            return used;
        }

        public void setUsed(BigDecimal used) {
            this.used = used;
        }

        public BigDecimal getAvailable() {
            return available;
        }

        public void setAvailable(BigDecimal available) {
            this.available = available;
        }

        public BigDecimal getMax() {
            return max;
        }

        public void setMax(BigDecimal max) {
            this.max = max;
        }
    }

    public static class TotalResourcesBean {
        /**
         * owner : monneymakers
         * net_weight : 59943.5000 MGP
         * cpu_weight : 59943.5000 MGP
         * ram_bytes : 34231471
         */

        private String owner;
        private String net_weight;
        private String cpu_weight;
        private BigDecimal ram_bytes;

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getNet_weight() {
            return net_weight;
        }

        public void setNet_weight(String net_weight) {
            this.net_weight = net_weight;
        }

        public String getCpu_weight() {
            return cpu_weight;
        }

        public void setCpu_weight(String cpu_weight) {
            this.cpu_weight = cpu_weight;
        }

        public BigDecimal getRam_bytes() {
            return ram_bytes;
        }

        public void setRam_bytes(BigDecimal ram_bytes) {
            this.ram_bytes = ram_bytes;
        }
    }

    public static class SelfDelegatedBandwidthBean {
        /**
         * from : monneymakers
         * to : monneymakers
         * net_weight : 59943.5000 MGP
         * cpu_weight : 59943.5000 MGP
         */

        private String from;
        private String to;
        private String net_weight;
        private String cpu_weight;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getNet_weight() {
            return net_weight;
        }

        public void setNet_weight(String net_weight) {
            this.net_weight = net_weight;
        }

        public String getCpu_weight() {
            return cpu_weight;
        }

        public void setCpu_weight(String cpu_weight) {
            this.cpu_weight = cpu_weight;
        }
    }

    public static class RefundRequestBean {
        /**
         * owner : monneymakers
         * request_time : 2020-05-07T02:49:56
         * net_amount : 0.5000 MGP
         * cpu_amount : 0.5000 MGP
         */

        private String owner;
        private String request_time;
        private String net_amount;
        private String cpu_amount;

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getRequest_time() {
            return request_time;
        }

        public void setRequest_time(String request_time) {
            this.request_time = request_time;
        }

        public String getNet_amount() {
            return net_amount;
        }

        public void setNet_amount(String net_amount) {
            this.net_amount = net_amount;
        }

        public String getCpu_amount() {
            return cpu_amount;
        }

        public void setCpu_amount(String cpu_amount) {
            this.cpu_amount = cpu_amount;
        }
    }

    public static class VoterInfoBean {
        /**
         * owner : monneymakers
         * proxy :
         * producers : ["mgproducer11","mgproducer12","mgproducer13","mgproducer14","mgproducer15","mgproducer21","mgproducer22","mgproducer23","mgproducer24","mgproducer25","mgproducer31","mgproducer32","mgproducer33","mgproducer34","mgproducer35","mgproducer41","mgproducer42","mgproducer43","mgproducer44","mgproducer45","mgproducer51"]
         * staked : 1198870000
         * last_vote_weight : 1663189796419222.75000000000000000
         * proxied_vote_weight : 0.00000000000000000
         * is_proxy : 0
         * flags1 : 0
         * reserved2 : 0
         * reserved3 : 0
         */

        private String owner;
        private String proxy;
        private BigDecimal staked;
        private String last_vote_weight;
        private String proxied_vote_weight;
        private BigDecimal is_proxy;
        private BigDecimal flags1;
        private BigDecimal reserved2;
        private String reserved3;
        private List<String> producers;

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getProxy() {
            return proxy;
        }

        public void setProxy(String proxy) {
            this.proxy = proxy;
        }

        public BigDecimal getStaked() {
            return staked;
        }

        public void setStaked(BigDecimal staked) {
            this.staked = staked;
        }

        public String getLast_vote_weight() {
            return last_vote_weight;
        }

        public void setLast_vote_weight(String last_vote_weight) {
            this.last_vote_weight = last_vote_weight;
        }

        public String getProxied_vote_weight() {
            return proxied_vote_weight;
        }

        public void setProxied_vote_weight(String proxied_vote_weight) {
            this.proxied_vote_weight = proxied_vote_weight;
        }

        public BigDecimal getIs_proxy() {
            return is_proxy;
        }

        public void setIs_proxy(BigDecimal is_proxy) {
            this.is_proxy = is_proxy;
        }

        public BigDecimal getFlags1() {
            return flags1;
        }

        public void setFlags1(BigDecimal flags1) {
            this.flags1 = flags1;
        }

        public BigDecimal getReserved2() {
            return reserved2;
        }

        public void setReserved2(BigDecimal reserved2) {
            this.reserved2 = reserved2;
        }

        public String getReserved3() {
            return reserved3;
        }

        public void setReserved3(String reserved3) {
            this.reserved3 = reserved3;
        }

        public List<String> getProducers() {
            return producers;
        }

        public void setProducers(List<String> producers) {
            this.producers = producers;
        }
    }

    public static class PermissionsBean {
        /**
         * perm_name : active
         * parent : owner
         * required_auth : {"threshold":1,"keys":[{"key":"EOS8AidVCXkstsx7PbjiuhTwhqj8FJfvcfimp5wpAcWSp1cP4WsYW","weight":1}],"accounts":[],"waits":[]}
         */

        private String perm_name;
        private String parent;
        private RequiredAuthBean required_auth;

        public String getPerm_name() {
            return perm_name;
        }

        public void setPerm_name(String perm_name) {
            this.perm_name = perm_name;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public RequiredAuthBean getRequired_auth() {
            return required_auth;
        }

        public void setRequired_auth(RequiredAuthBean required_auth) {
            this.required_auth = required_auth;
        }

        public static class RequiredAuthBean {
            /**
             * threshold : 1
             * keys : [{"key":"EOS8AidVCXkstsx7PbjiuhTwhqj8FJfvcfimp5wpAcWSp1cP4WsYW","weight":1}]
             * accounts : []
             * waits : []
             */

            private BigDecimal threshold;
            private List<KeysBean> keys;
            private List<?> accounts;
            private List<?> waits;

            public BigDecimal getThreshold() {
                return threshold;
            }

            public void setThreshold(BigDecimal threshold) {
                this.threshold = threshold;
            }

            public List<KeysBean> getKeys() {
                return keys;
            }

            public void setKeys(List<KeysBean> keys) {
                this.keys = keys;
            }

            public List<?> getAccounts() {
                return accounts;
            }

            public void setAccounts(List<?> accounts) {
                this.accounts = accounts;
            }

            public List<?> getWaits() {
                return waits;
            }

            public void setWaits(List<?> waits) {
                this.waits = waits;
            }

            public static class KeysBean {
                /**
                 * key : EOS8AidVCXkstsx7PbjiuhTwhqj8FJfvcfimp5wpAcWSp1cP4WsYW
                 * weight : 1
                 */

                private String key;
                private BigDecimal weight;

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public BigDecimal getWeight() {
                    return weight;
                }

                public void setWeight(BigDecimal weight) {
                    this.weight = weight;
                }
            }
        }
    }
}
