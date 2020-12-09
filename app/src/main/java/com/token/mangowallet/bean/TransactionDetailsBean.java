package com.token.mangowallet.bean;

import java.util.List;

public class TransactionDetailsBean {

    /**
     * block_num : 91836719
     * block_time : 2019-11-26T06:19:51.000
     * head_block_num : 117870661
     * id : 6873990b48055bd073219f9f6e162bf741293cf23a5c5aacd53746478d91dd22
     * irreversible : true
     * last_irreversible_block : 117870299
     * traces : [{"account_ram_deltas":[{"account":"pieinstantoo","delta":240}],"act":{"account":"eosio.token","authorization":[{"actor":"pieinstantoo","permission":"active"}],"data":{"from":"pieinstantoo","memo":"","quantity":"5.6559 EOS","to":"algorguo1234"},"hex_data":"40699e26e3e994ab40860854b34b5934efdc00000000000004454f530000000000","name":"transfer"},"action_ordinal":1,"block_num":91836719,"block_time":"2019-11-26T06:19:51.000","closest_unnotified_ancestor_action_ordinal":0,"console":"","context_free":false,"creator_action_ordinal":0,"elapsed":81,"error_code":null,"except":null,"producer_block_id":"0579512f75fd2a0e0aeef17ccab53bd8a17d8ca5b66df86e0da03179dd2c0c3a","receipt":{"abi_sequence":4,"act_digest":"7172e82ea6627b7e79df5d9665000cba3df87d062877a4a49a680aa3610e4a0f","auth_sequence":[["pieinstantoo",46341]],"code_sequence":4,"global_sequence":"16577199348","receiver":"eosio.token","recv_sequence":2976343266},"receiver":"eosio.token","trx_id":"6873990b48055bd073219f9f6e162bf741293cf23a5c5aacd53746478d91dd22"},{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"pieinstantoo","permission":"active"}],"data":{"from":"pieinstantoo","memo":"","quantity":"5.6559 EOS","to":"algorguo1234"},"hex_data":"40699e26e3e994ab40860854b34b5934efdc00000000000004454f530000000000","name":"transfer"},"action_ordinal":2,"block_num":91836719,"block_time":"2019-11-26T06:19:51.000","closest_unnotified_ancestor_action_ordinal":1,"console":"","context_free":false,"creator_action_ordinal":1,"elapsed":1,"error_code":null,"except":null,"producer_block_id":"0579512f75fd2a0e0aeef17ccab53bd8a17d8ca5b66df86e0da03179dd2c0c3a","receipt":{"abi_sequence":4,"act_digest":"7172e82ea6627b7e79df5d9665000cba3df87d062877a4a49a680aa3610e4a0f","auth_sequence":[["pieinstantoo",46342]],"code_sequence":4,"global_sequence":"16577199349","receiver":"pieinstantoo","recv_sequence":22927},"receiver":"pieinstantoo","trx_id":"6873990b48055bd073219f9f6e162bf741293cf23a5c5aacd53746478d91dd22"},{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"pieinstantoo","permission":"active"}],"data":{"from":"pieinstantoo","memo":"","quantity":"5.6559 EOS","to":"algorguo1234"},"hex_data":"40699e26e3e994ab40860854b34b5934efdc00000000000004454f530000000000","name":"transfer"},"action_ordinal":3,"block_num":91836719,"block_time":"2019-11-26T06:19:51.000","closest_unnotified_ancestor_action_ordinal":1,"console":"","context_free":false,"creator_action_ordinal":1,"elapsed":7,"error_code":null,"except":null,"producer_block_id":"0579512f75fd2a0e0aeef17ccab53bd8a17d8ca5b66df86e0da03179dd2c0c3a","receipt":{"abi_sequence":4,"act_digest":"7172e82ea6627b7e79df5d9665000cba3df87d062877a4a49a680aa3610e4a0f","auth_sequence":[["pieinstantoo",46343]],"code_sequence":4,"global_sequence":"16577199350","receiver":"algorguo1234","recv_sequence":1},"receiver":"algorguo1234","trx_id":"6873990b48055bd073219f9f6e162bf741293cf23a5c5aacd53746478d91dd22"}]
     * transaction_num : 9
     * trx : {"receipt":{"cpu_usage_us":206,"net_usage_words":16,"status":"executed","trx":[1,{"compression":"none","packed_context_free_data":"","packed_trx":"7cc4dc5de64f244d3ab2000000000100a6823403ea3055000000572d3ccdcd0140699e26e3e994ab00000000a8ed32322140699e26e3e994ab40860854b34b5934efdc00000000000004454f53000000000000","signatures":["SIG_K1_KA6JdUsSd6fYKaF4VdWixidHUKcc1nuCsMbtjjhHYnUsu8he94APzWaPj3pUhzMzqstt4e334pEWBEN3oNnWaiunCHR8UX"]}]},"trx":{"actions":[{"account":"eosio.token","authorization":[{"actor":"pieinstantoo","permission":"active"}],"data":{"from":"pieinstantoo","memo":"","quantity":"5.6559 EOS","to":"algorguo1234"},"hex_data":"40699e26e3e994ab40860854b34b5934efdc00000000000004454f530000000000","name":"transfer"}],"context_free_actions":[],"context_free_data":[],"delay_sec":0,"expiration":"2019-11-26T06:21:48","max_cpu_usage_ms":0,"max_net_usage_words":0,"ref_block_num":20454,"ref_block_prefix":2990165284,"signatures":["SIG_K1_KA6JdUsSd6fYKaF4VdWixidHUKcc1nuCsMbtjjhHYnUsu8he94APzWaPj3pUhzMzqstt4e334pEWBEN3oNnWaiunCHR8UX"]}}
     */

    private String block_num;
    private String block_time;
    private String head_block_num;
    private String id;
    private boolean irreversible;
    private String last_irreversible_block;
    private int transaction_num;
    private TrxBeanX trx;
    private List<TracesBean> traces;

    public String getBlock_num() {
        return block_num;
    }

    public void setBlock_num(String block_num) {
        this.block_num = block_num;
    }

    public String getBlock_time() {
        return block_time;
    }

    public void setBlock_time(String block_time) {
        this.block_time = block_time;
    }

    public String getHead_block_num() {
        return head_block_num;
    }

    public void setHead_block_num(String head_block_num) {
        this.head_block_num = head_block_num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIrreversible() {
        return irreversible;
    }

    public void setIrreversible(boolean irreversible) {
        this.irreversible = irreversible;
    }

    public String getLast_irreversible_block() {
        return last_irreversible_block;
    }

    public void setLast_irreversible_block(String last_irreversible_block) {
        this.last_irreversible_block = last_irreversible_block;
    }

    public int getTransaction_num() {
        return transaction_num;
    }

    public void setTransaction_num(int transaction_num) {
        this.transaction_num = transaction_num;
    }

    public TrxBeanX getTrx() {
        return trx;
    }

    public void setTrx(TrxBeanX trx) {
        this.trx = trx;
    }

    public List<TracesBean> getTraces() {
        return traces;
    }

    public void setTraces(List<TracesBean> traces) {
        this.traces = traces;
    }

    public static class TrxBeanX {
        /**
         * receipt : {"cpu_usage_us":206,"net_usage_words":16,"status":"executed","trx":[1,{"compression":"none","packed_context_free_data":"","packed_trx":"7cc4dc5de64f244d3ab2000000000100a6823403ea3055000000572d3ccdcd0140699e26e3e994ab00000000a8ed32322140699e26e3e994ab40860854b34b5934efdc00000000000004454f53000000000000","signatures":["SIG_K1_KA6JdUsSd6fYKaF4VdWixidHUKcc1nuCsMbtjjhHYnUsu8he94APzWaPj3pUhzMzqstt4e334pEWBEN3oNnWaiunCHR8UX"]}]}
         * trx : {"actions":[{"account":"eosio.token","authorization":[{"actor":"pieinstantoo","permission":"active"}],"data":{"from":"pieinstantoo","memo":"","quantity":"5.6559 EOS","to":"algorguo1234"},"hex_data":"40699e26e3e994ab40860854b34b5934efdc00000000000004454f530000000000","name":"transfer"}],"context_free_actions":[],"context_free_data":[],"delay_sec":0,"expiration":"2019-11-26T06:21:48","max_cpu_usage_ms":0,"max_net_usage_words":0,"ref_block_num":20454,"ref_block_prefix":2990165284,"signatures":["SIG_K1_KA6JdUsSd6fYKaF4VdWixidHUKcc1nuCsMbtjjhHYnUsu8he94APzWaPj3pUhzMzqstt4e334pEWBEN3oNnWaiunCHR8UX"]}
         */

        private ReceiptBean receipt;
        private TrxBean trx;

        public ReceiptBean getReceipt() {
            return receipt;
        }

        public void setReceipt(ReceiptBean receipt) {
            this.receipt = receipt;
        }

        public TrxBean getTrx() {
            return trx;
        }

        public void setTrx(TrxBean trx) {
            this.trx = trx;
        }

        public static class ReceiptBean {
            /**
             * cpu_usage_us : 206
             * net_usage_words : 16
             * status : executed
             * trx : [1,{"compression":"none","packed_context_free_data":"","packed_trx":"7cc4dc5de64f244d3ab2000000000100a6823403ea3055000000572d3ccdcd0140699e26e3e994ab00000000a8ed32322140699e26e3e994ab40860854b34b5934efdc00000000000004454f53000000000000","signatures":["SIG_K1_KA6JdUsSd6fYKaF4VdWixidHUKcc1nuCsMbtjjhHYnUsu8he94APzWaPj3pUhzMzqstt4e334pEWBEN3oNnWaiunCHR8UX"]}]
             */

            private String cpu_usage_us;
            private String net_usage_words;
            private String status;

            public String getCpu_usage_us() {
                return cpu_usage_us;
            }

            public void setCpu_usage_us(String cpu_usage_us) {
                this.cpu_usage_us = cpu_usage_us;
            }

            public String getNet_usage_words() {
                return net_usage_words;
            }

            public void setNet_usage_words(String net_usage_words) {
                this.net_usage_words = net_usage_words;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }

        public static class TrxBean {
            /**
             * actions : [{"account":"eosio.token","authorization":[{"actor":"pieinstantoo","permission":"active"}],"data":{"from":"pieinstantoo","memo":"","quantity":"5.6559 EOS","to":"algorguo1234"},"hex_data":"40699e26e3e994ab40860854b34b5934efdc00000000000004454f530000000000","name":"transfer"}]
             * context_free_actions : []
             * context_free_data : []
             * delay_sec : 0
             * expiration : 2019-11-26T06:21:48
             * max_cpu_usage_ms : 0
             * max_net_usage_words : 0
             * ref_block_num : 20454
             * ref_block_prefix : 2990165284
             * signatures : ["SIG_K1_KA6JdUsSd6fYKaF4VdWixidHUKcc1nuCsMbtjjhHYnUsu8he94APzWaPj3pUhzMzqstt4e334pEWBEN3oNnWaiunCHR8UX"]
             */

            private int delay_sec;
            private String expiration;
            private int max_cpu_usage_ms;
            private int max_net_usage_words;
            private int ref_block_num;
            private long ref_block_prefix;
            private List<ActionsBean> actions;
            private List<?> context_free_actions;
            private List<?> context_free_data;
            private List<String> signatures;

            public int getDelay_sec() {
                return delay_sec;
            }

            public void setDelay_sec(int delay_sec) {
                this.delay_sec = delay_sec;
            }

            public String getExpiration() {
                return expiration;
            }

            public void setExpiration(String expiration) {
                this.expiration = expiration;
            }

            public int getMax_cpu_usage_ms() {
                return max_cpu_usage_ms;
            }

            public void setMax_cpu_usage_ms(int max_cpu_usage_ms) {
                this.max_cpu_usage_ms = max_cpu_usage_ms;
            }

            public int getMax_net_usage_words() {
                return max_net_usage_words;
            }

            public void setMax_net_usage_words(int max_net_usage_words) {
                this.max_net_usage_words = max_net_usage_words;
            }

            public int getRef_block_num() {
                return ref_block_num;
            }

            public void setRef_block_num(int ref_block_num) {
                this.ref_block_num = ref_block_num;
            }

            public long getRef_block_prefix() {
                return ref_block_prefix;
            }

            public void setRef_block_prefix(long ref_block_prefix) {
                this.ref_block_prefix = ref_block_prefix;
            }

            public List<ActionsBean> getActions() {
                return actions;
            }

            public void setActions(List<ActionsBean> actions) {
                this.actions = actions;
            }

            public List<?> getContext_free_actions() {
                return context_free_actions;
            }

            public void setContext_free_actions(List<?> context_free_actions) {
                this.context_free_actions = context_free_actions;
            }

            public List<?> getContext_free_data() {
                return context_free_data;
            }

            public void setContext_free_data(List<?> context_free_data) {
                this.context_free_data = context_free_data;
            }

            public List<String> getSignatures() {
                return signatures;
            }

            public void setSignatures(List<String> signatures) {
                this.signatures = signatures;
            }

            public static class ActionsBean {
                /**
                 * account : eosio.token
                 * authorization : [{"actor":"pieinstantoo","permission":"active"}]
                 * data : {"from":"pieinstantoo","memo":"","quantity":"5.6559 EOS","to":"algorguo1234"}
                 * hex_data : 40699e26e3e994ab40860854b34b5934efdc00000000000004454f530000000000
                 * name : transfer
                 */

                private String account;
                private DataBean data;
                private String hex_data;
                private String name;
                private List<AuthorizationBean> authorization;

                public String getAccount() {
                    return account;
                }

                public void setAccount(String account) {
                    this.account = account;
                }

                public DataBean getData() {
                    return data;
                }

                public void setData(DataBean data) {
                    this.data = data;
                }

                public String getHex_data() {
                    return hex_data;
                }

                public void setHex_data(String hex_data) {
                    this.hex_data = hex_data;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<AuthorizationBean> getAuthorization() {
                    return authorization;
                }

                public void setAuthorization(List<AuthorizationBean> authorization) {
                    this.authorization = authorization;
                }

                public static class DataBean {
                    /**
                     * from : pieinstantoo
                     * memo :
                     * quantity : 5.6559 EOS
                     * to : algorguo1234
                     */

                    private String from;
                    private String memo;
                    private String quantity;
                    private String to;

                    public String getFrom() {
                        return from;
                    }

                    public void setFrom(String from) {
                        this.from = from;
                    }

                    public String getMemo() {
                        return memo;
                    }

                    public void setMemo(String memo) {
                        this.memo = memo;
                    }

                    public String getQuantity() {
                        return quantity;
                    }

                    public void setQuantity(String quantity) {
                        this.quantity = quantity;
                    }

                    public String getTo() {
                        return to;
                    }

                    public void setTo(String to) {
                        this.to = to;
                    }
                }

                public static class AuthorizationBean {
                    /**
                     * actor : pieinstantoo
                     * permission : active
                     */

                    private String actor;
                    private String permission;

                    public String getActor() {
                        return actor;
                    }

                    public void setActor(String actor) {
                        this.actor = actor;
                    }

                    public String getPermission() {
                        return permission;
                    }

                    public void setPermission(String permission) {
                        this.permission = permission;
                    }
                }
            }
        }
    }

    public static class TracesBean {
        /**
         * account_ram_deltas : [{"account":"pieinstantoo","delta":240}]
         * act : {"account":"eosio.token","authorization":[{"actor":"pieinstantoo","permission":"active"}],"data":{"from":"pieinstantoo","memo":"","quantity":"5.6559 EOS","to":"algorguo1234"},"hex_data":"40699e26e3e994ab40860854b34b5934efdc00000000000004454f530000000000","name":"transfer"}
         * action_ordinal : 1
         * block_num : 91836719
         * block_time : 2019-11-26T06:19:51.000
         * closest_unnotified_ancestor_action_ordinal : 0
         * console :
         * context_free : false
         * creator_action_ordinal : 0
         * elapsed : 81
         * error_code : null
         * except : null
         * producer_block_id : 0579512f75fd2a0e0aeef17ccab53bd8a17d8ca5b66df86e0da03179dd2c0c3a
         * receipt : {"abi_sequence":4,"act_digest":"7172e82ea6627b7e79df5d9665000cba3df87d062877a4a49a680aa3610e4a0f","auth_sequence":[["pieinstantoo",46341]],"code_sequence":4,"global_sequence":"16577199348","receiver":"eosio.token","recv_sequence":2976343266}
         * receiver : eosio.token
         * trx_id : 6873990b48055bd073219f9f6e162bf741293cf23a5c5aacd53746478d91dd22
         */

        private ActBean act;
        private int action_ordinal;
        private int block_num;
        private String block_time;
        private int closest_unnotified_ancestor_action_ordinal;
        private String console;
        private boolean context_free;
        private int creator_action_ordinal;
        private int elapsed;
        private Object error_code;
        private Object except;
        private String producer_block_id;
        private ReceiptBeanX receipt;
        private String receiver;
        private String trx_id;
        private List<AccountRamDeltasBean> account_ram_deltas;

        public ActBean getAct() {
            return act;
        }

        public void setAct(ActBean act) {
            this.act = act;
        }

        public int getAction_ordinal() {
            return action_ordinal;
        }

        public void setAction_ordinal(int action_ordinal) {
            this.action_ordinal = action_ordinal;
        }

        public int getBlock_num() {
            return block_num;
        }

        public void setBlock_num(int block_num) {
            this.block_num = block_num;
        }

        public String getBlock_time() {
            return block_time;
        }

        public void setBlock_time(String block_time) {
            this.block_time = block_time;
        }

        public int getClosest_unnotified_ancestor_action_ordinal() {
            return closest_unnotified_ancestor_action_ordinal;
        }

        public void setClosest_unnotified_ancestor_action_ordinal(int closest_unnotified_ancestor_action_ordinal) {
            this.closest_unnotified_ancestor_action_ordinal = closest_unnotified_ancestor_action_ordinal;
        }

        public String getConsole() {
            return console;
        }

        public void setConsole(String console) {
            this.console = console;
        }

        public boolean isContext_free() {
            return context_free;
        }

        public void setContext_free(boolean context_free) {
            this.context_free = context_free;
        }

        public int getCreator_action_ordinal() {
            return creator_action_ordinal;
        }

        public void setCreator_action_ordinal(int creator_action_ordinal) {
            this.creator_action_ordinal = creator_action_ordinal;
        }

        public int getElapsed() {
            return elapsed;
        }

        public void setElapsed(int elapsed) {
            this.elapsed = elapsed;
        }

        public Object getError_code() {
            return error_code;
        }

        public void setError_code(Object error_code) {
            this.error_code = error_code;
        }

        public Object getExcept() {
            return except;
        }

        public void setExcept(Object except) {
            this.except = except;
        }

        public String getProducer_block_id() {
            return producer_block_id;
        }

        public void setProducer_block_id(String producer_block_id) {
            this.producer_block_id = producer_block_id;
        }

        public ReceiptBeanX getReceipt() {
            return receipt;
        }

        public void setReceipt(ReceiptBeanX receipt) {
            this.receipt = receipt;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getTrx_id() {
            return trx_id;
        }

        public void setTrx_id(String trx_id) {
            this.trx_id = trx_id;
        }

        public List<AccountRamDeltasBean> getAccount_ram_deltas() {
            return account_ram_deltas;
        }

        public void setAccount_ram_deltas(List<AccountRamDeltasBean> account_ram_deltas) {
            this.account_ram_deltas = account_ram_deltas;
        }

        public static class ActBean {
            /**
             * account : eosio.token
             * authorization : [{"actor":"pieinstantoo","permission":"active"}]
             * data : {"from":"pieinstantoo","memo":"","quantity":"5.6559 EOS","to":"algorguo1234"}
             * hex_data : 40699e26e3e994ab40860854b34b5934efdc00000000000004454f530000000000
             * name : transfer
             */

            private String account;
            private DataBeanX data;
            private String hex_data;
            private String name;
            private List<AuthorizationBeanX> authorization;

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public DataBeanX getData() {
                return data;
            }

            public void setData(DataBeanX data) {
                this.data = data;
            }

            public String getHex_data() {
                return hex_data;
            }

            public void setHex_data(String hex_data) {
                this.hex_data = hex_data;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<AuthorizationBeanX> getAuthorization() {
                return authorization;
            }

            public void setAuthorization(List<AuthorizationBeanX> authorization) {
                this.authorization = authorization;
            }

            public static class DataBeanX {
                /**
                 * from : pieinstantoo
                 * memo :
                 * quantity : 5.6559 EOS
                 * to : algorguo1234
                 */

                private String from;
                private String memo;
                private String quantity;
                private String to;

                public String getFrom() {
                    return from;
                }

                public void setFrom(String from) {
                    this.from = from;
                }

                public String getMemo() {
                    return memo;
                }

                public void setMemo(String memo) {
                    this.memo = memo;
                }

                public String getQuantity() {
                    return quantity;
                }

                public void setQuantity(String quantity) {
                    this.quantity = quantity;
                }

                public String getTo() {
                    return to;
                }

                public void setTo(String to) {
                    this.to = to;
                }
            }

            public static class AuthorizationBeanX {
                /**
                 * actor : pieinstantoo
                 * permission : active
                 */

                private String actor;
                private String permission;

                public String getActor() {
                    return actor;
                }

                public void setActor(String actor) {
                    this.actor = actor;
                }

                public String getPermission() {
                    return permission;
                }

                public void setPermission(String permission) {
                    this.permission = permission;
                }
            }
        }

        public static class ReceiptBeanX {
            /**
             * abi_sequence : 4
             * act_digest : 7172e82ea6627b7e79df5d9665000cba3df87d062877a4a49a680aa3610e4a0f
             * auth_sequence : [["pieinstantoo",46341]]
             * code_sequence : 4
             * global_sequence : 16577199348
             * receiver : eosio.token
             * recv_sequence : 2976343266
             */

            private int abi_sequence;
            private String act_digest;
            private int code_sequence;
            private String global_sequence;
            private String receiver;
            private long recv_sequence;
            private List<List<String>> auth_sequence;

            public int getAbi_sequence() {
                return abi_sequence;
            }

            public void setAbi_sequence(int abi_sequence) {
                this.abi_sequence = abi_sequence;
            }

            public String getAct_digest() {
                return act_digest;
            }

            public void setAct_digest(String act_digest) {
                this.act_digest = act_digest;
            }

            public int getCode_sequence() {
                return code_sequence;
            }

            public void setCode_sequence(int code_sequence) {
                this.code_sequence = code_sequence;
            }

            public String getGlobal_sequence() {
                return global_sequence;
            }

            public void setGlobal_sequence(String global_sequence) {
                this.global_sequence = global_sequence;
            }

            public String getReceiver() {
                return receiver;
            }

            public void setReceiver(String receiver) {
                this.receiver = receiver;
            }

            public long getRecv_sequence() {
                return recv_sequence;
            }

            public void setRecv_sequence(long recv_sequence) {
                this.recv_sequence = recv_sequence;
            }

            public List<List<String>> getAuth_sequence() {
                return auth_sequence;
            }

            public void setAuth_sequence(List<List<String>> auth_sequence) {
                this.auth_sequence = auth_sequence;
            }
        }

        public static class AccountRamDeltasBean {
            /**
             * account : pieinstantoo
             * delta : 240
             */

            private String account;
            private int delta;

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public int getDelta() {
                return delta;
            }

            public void setDelta(int delta) {
                this.delta = delta;
            }
        }
    }
}
