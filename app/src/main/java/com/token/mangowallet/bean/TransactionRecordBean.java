package com.token.mangowallet.bean;

import java.util.List;

public class TransactionRecordBean {


    /**
     * actions : [{"account_action_seq":0,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"pieinstantoo","permission":"active"}],"data":{"from":"pieinstantoo","memo":"","quantity":"5.6559 EOS","to":"algorguo1234"},"hex_data":"40699e26e3e994ab40860854b34b5934efdc00000000000004454f530000000000","name":"transfer"},"action_ordinal":3,"block_num":91836719,"block_time":"2019-11-26T06:19:51.000","closest_unnotified_ancestor_action_ordinal":1,"console":"","context_free":false,"creator_action_ordinal":1,"elapsed":7,"error_code":null,"except":null,"producer_block_id":"0579512f75fd2a0e0aeef17ccab53bd8a17d8ca5b66df86e0da03179dd2c0c3a","receipt":{"abi_sequence":4,"act_digest":"7172e82ea6627b7e79df5d9665000cba3df87d062877a4a49a680aa3610e4a0f","auth_sequence":[["pieinstantoo",46343]],"code_sequence":4,"global_sequence":"16577199350","receiver":"algorguo1234","recv_sequence":1},"receiver":"algorguo1234","trx_id":"6873990b48055bd073219f9f6e162bf741293cf23a5c5aacd53746478d91dd22"},"block_num":91836719,"block_time":"2019-11-26T06:19:51.000","global_action_seq":16577199350,"irreversible":true},{"account_action_seq":1,"action_trace":{"account_ram_deltas":[{"account":"algorguo1234","delta":128},{"account":"pieinstantoo","delta":-128}],"act":{"account":"eosio.token","authorization":[{"actor":"1stbill.tp","permission":"active"},{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"15000,1,,","quantity":"0.1200 EOS","to":"resource.tp"},"hex_data":"40860854b34b5934006a060a5d4db1bab00400000000000004454f53000000000931353030302c312c2c","name":"transfer"},"action_ordinal":1,"block_num":115776876,"block_time":"2020-04-16T08:33:50.500","closest_unnotified_ancestor_action_ordinal":0,"console":"","context_free":false,"creator_action_ordinal":0,"elapsed":59,"error_code":null,"except":null,"producer_block_id":"06e69d6c321cd370e56d173f2d9691e5e8ef6797db486946838637ac9929bf54","receipt":{"abi_sequence":4,"act_digest":"dba58dd02192a398bcea4946a95d1a1d095c26854d4d876d725aaa1bf4be9d28","auth_sequence":[["1stbill.tp",9219229],["algorguo1234",1]],"code_sequence":4,"global_sequence":"95319601104","receiver":"eosio.token","recv_sequence":"22536190613"},"receiver":"eosio.token","trx_id":"376914a6c347b3703821faab3f03f678b6872ab9311050dc237217cdfbafe517"},"block_num":115776876,"block_time":"2020-04-16T08:33:50.500","global_action_seq":95319601104,"irreversible":true},{"account_action_seq":2,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"1stbill.tp","permission":"active"},{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"15000,1,,","quantity":"0.1200 EOS","to":"resource.tp"},"hex_data":"40860854b34b5934006a060a5d4db1bab00400000000000004454f53000000000931353030302c312c2c","name":"transfer"},"action_ordinal":2,"block_num":115776876,"block_time":"2020-04-16T08:33:50.500","closest_unnotified_ancestor_action_ordinal":1,"console":"","context_free":false,"creator_action_ordinal":1,"elapsed":2,"error_code":null,"except":null,"producer_block_id":"06e69d6c321cd370e56d173f2d9691e5e8ef6797db486946838637ac9929bf54","receipt":{"abi_sequence":4,"act_digest":"dba58dd02192a398bcea4946a95d1a1d095c26854d4d876d725aaa1bf4be9d28","auth_sequence":[["1stbill.tp",9219230],["algorguo1234",2]],"code_sequence":4,"global_sequence":"95319601105","receiver":"algorguo1234","recv_sequence":2},"receiver":"algorguo1234","trx_id":"376914a6c347b3703821faab3f03f678b6872ab9311050dc237217cdfbafe517"},"block_num":115776876,"block_time":"2020-04-16T08:33:50.500","global_action_seq":95319601105,"irreversible":true},{"account_action_seq":3,"action_trace":{"account_ram_deltas":[{"account":"resource.tp","delta":304}],"act":{"account":"eosio.token","authorization":[{"actor":"1stbill.tp","permission":"active"},{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"15000,1,,","quantity":"0.1200 EOS","to":"resource.tp"},"hex_data":"40860854b34b5934006a060a5d4db1bab00400000000000004454f53000000000931353030302c312c2c","name":"transfer"},"action_ordinal":3,"block_num":115776876,"block_time":"2020-04-16T08:33:50.500","closest_unnotified_ancestor_action_ordinal":1,"console":"","context_free":false,"creator_action_ordinal":1,"elapsed":66,"error_code":null,"except":null,"producer_block_id":"06e69d6c321cd370e56d173f2d9691e5e8ef6797db486946838637ac9929bf54","receipt":{"abi_sequence":4,"act_digest":"dba58dd02192a398bcea4946a95d1a1d095c26854d4d876d725aaa1bf4be9d28","auth_sequence":[["1stbill.tp",9219231],["algorguo1234",3]],"code_sequence":4,"global_sequence":"95319601106","receiver":"resource.tp","recv_sequence":90639},"receiver":"resource.tp","trx_id":"376914a6c347b3703821faab3f03f678b6872ab9311050dc237217cdfbafe517"},"block_num":115776876,"block_time":"2020-04-16T08:33:50.500","global_action_seq":95319601106,"irreversible":true},{"account_action_seq":4,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"testray","quantity":"0.0001 EOS","to":"lxheos1accou"},"hex_data":"40860854b34b5934a029422660aa5a8f010000000000000004454f53000000000774657374726179","name":"transfer"},"action_ordinal":1,"block_num":115781979,"block_time":"2020-04-16T09:16:22.500","closest_unnotified_ancestor_action_ordinal":0,"console":"","context_free":false,"creator_action_ordinal":0,"elapsed":48,"error_code":null,"except":null,"producer_block_id":"06e6b15b5b5dfc28067a558c289e2008ed28157389ee5215252b7b21c7dddd57","receipt":{"abi_sequence":4,"act_digest":"f5f026a859c2e53c51a6217ad6b470d28b82f2e5d0fcf32695400ac4bebac05f","auth_sequence":[["algorguo1234",4]],"code_sequence":4,"global_sequence":"95346957064","receiver":"eosio.token","recv_sequence":"22542981360"},"receiver":"eosio.token","trx_id":"ac0b520a81d045fbaa6bd132fe514f7b2172249735f10ae8216df78e4dcbf430"},"block_num":115781979,"block_time":"2020-04-16T09:16:22.500","global_action_seq":95346957064,"irreversible":true},{"account_action_seq":5,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"testray","quantity":"0.0001 EOS","to":"lxheos1accou"},"hex_data":"40860854b34b5934a029422660aa5a8f010000000000000004454f53000000000774657374726179","name":"transfer"},"action_ordinal":2,"block_num":115781979,"block_time":"2020-04-16T09:16:22.500","closest_unnotified_ancestor_action_ordinal":1,"console":"","context_free":false,"creator_action_ordinal":1,"elapsed":2,"error_code":null,"except":null,"producer_block_id":"06e6b15b5b5dfc28067a558c289e2008ed28157389ee5215252b7b21c7dddd57","receipt":{"abi_sequence":4,"act_digest":"f5f026a859c2e53c51a6217ad6b470d28b82f2e5d0fcf32695400ac4bebac05f","auth_sequence":[["algorguo1234",5]],"code_sequence":4,"global_sequence":"95346957065","receiver":"algorguo1234","recv_sequence":3},"receiver":"algorguo1234","trx_id":"ac0b520a81d045fbaa6bd132fe514f7b2172249735f10ae8216df78e4dcbf430"},"block_num":115781979,"block_time":"2020-04-16T09:16:22.500","global_action_seq":95346957065,"irreversible":true},{"account_action_seq":6,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"testray","quantity":"0.0001 EOS","to":"lxheos1accou"},"hex_data":"40860854b34b5934a029422660aa5a8f010000000000000004454f53000000000774657374726179","name":"transfer"},"action_ordinal":3,"block_num":115781979,"block_time":"2020-04-16T09:16:22.500","closest_unnotified_ancestor_action_ordinal":1,"console":"","context_free":false,"creator_action_ordinal":1,"elapsed":6,"error_code":null,"except":null,"producer_block_id":"06e6b15b5b5dfc28067a558c289e2008ed28157389ee5215252b7b21c7dddd57","receipt":{"abi_sequence":4,"act_digest":"f5f026a859c2e53c51a6217ad6b470d28b82f2e5d0fcf32695400ac4bebac05f","auth_sequence":[["algorguo1234",6]],"code_sequence":4,"global_sequence":"95346957066","receiver":"lxheos1accou","recv_sequence":16},"receiver":"lxheos1accou","trx_id":"ac0b520a81d045fbaa6bd132fe514f7b2172249735f10ae8216df78e4dcbf430"},"block_num":115781979,"block_time":"2020-04-16T09:16:22.500","global_action_seq":95346957066,"irreversible":true},{"account_action_seq":7,"action_trace":{"account_ram_deltas":[{"account":"algorguotest","delta":2996}],"act":{"account":"eosio","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"active":{"accounts":[],"keys":[{"key":"EOS77i6sktLJEKgPnpzHf7EtEdrNdYQ5vLGz41dih73G2dDWnoQQq","weight":1}],"threshold":1,"waits":[]},"creator":"algorguo1234","name":"algorguotest","owner":{"accounts":[],"keys":[{"key":"EOS77i6sktLJEKgPnpzHf7EtEdrNdYQ5vLGz41dih73G2dDWnoQQq","weight":1}],"threshold":1,"waits":[]}},"hex_data":"40860854b34b593490b1ca54b34b5934010000000100032568cc3f33448777b51335e952c61ea5d73ae9bea9293c1222a263f85a218a6101000000010000000100032568cc3f33448777b51335e952c61ea5d73ae9bea9293c1222a263f85a218a6101000000","name":"newaccount"},"action_ordinal":1,"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","closest_unnotified_ancestor_action_ordinal":0,"console":"","context_free":false,"creator_action_ordinal":0,"elapsed":203,"error_code":null,"except":null,"producer_block_id":"06e6b1ba9168dd08aed2609d3968406251764b01360bf35fd7884c3f23329f48","receipt":{"abi_sequence":17,"act_digest":"b562f7b60451037f4961e801a38e7727e9511f71f19ee5bf24bfc274135e14ef","auth_sequence":[["algorguo1234",7]],"code_sequence":15,"global_sequence":"95347543682","receiver":"eosio","recv_sequence":134254752},"receiver":"eosio","trx_id":"ae559872968fdc9145d98c84b5e1445193b7d97d74e903905e4ec9f0d9803512"},"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","global_action_seq":95347543682,"irreversible":true},{"account_action_seq":8,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"bytes":8000,"payer":"algorguo1234","receiver":"algorguotest"},"hex_data":"40860854b34b593490b1ca54b34b5934401f0000","name":"buyrambytes"},"action_ordinal":2,"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","closest_unnotified_ancestor_action_ordinal":0,"console":"","context_free":false,"creator_action_ordinal":0,"elapsed":86,"error_code":null,"except":null,"producer_block_id":"06e6b1ba9168dd08aed2609d3968406251764b01360bf35fd7884c3f23329f48","receipt":{"abi_sequence":17,"act_digest":"c555dd5069426e2e6cbeadd644a809757c81bb6427cd318cc2ab377cd406f720","auth_sequence":[["algorguo1234",8]],"code_sequence":15,"global_sequence":"95347543683","receiver":"eosio","recv_sequence":134254753},"receiver":"eosio","trx_id":"ae559872968fdc9145d98c84b5e1445193b7d97d74e903905e4ec9f0d9803512"},"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","global_action_seq":95347543683,"irreversible":true},{"account_action_seq":9,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"},{"actor":"eosio.ram","permission":"active"}],"data":{"from":"algorguo1234","memo":"buy ram","quantity":"0.4206 EOS","to":"eosio.ram"},"hex_data":"40860854b34b5934000090e602ea30556e1000000000000004454f5300000000076275792072616d","name":"transfer"},"action_ordinal":4,"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","closest_unnotified_ancestor_action_ordinal":2,"console":"","context_free":false,"creator_action_ordinal":2,"elapsed":25,"error_code":null,"except":null,"producer_block_id":"06e6b1ba9168dd08aed2609d3968406251764b01360bf35fd7884c3f23329f48","receipt":{"abi_sequence":4,"act_digest":"1de12a449248a0a06b1145cc52a81fed4076d47e61a2ebf7cf37ba6d57709b73","auth_sequence":[["algorguo1234",9],["eosio.ram",8696821]],"code_sequence":4,"global_sequence":"95347543684","receiver":"eosio.token","recv_sequence":"22543127159"},"receiver":"eosio.token","trx_id":"ae559872968fdc9145d98c84b5e1445193b7d97d74e903905e4ec9f0d9803512"},"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","global_action_seq":95347543684,"irreversible":true},{"account_action_seq":10,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"},{"actor":"eosio.ram","permission":"active"}],"data":{"from":"algorguo1234","memo":"buy ram","quantity":"0.4206 EOS","to":"eosio.ram"},"hex_data":"40860854b34b5934000090e602ea30556e1000000000000004454f5300000000076275792072616d","name":"transfer"},"action_ordinal":7,"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","closest_unnotified_ancestor_action_ordinal":4,"console":"","context_free":false,"creator_action_ordinal":4,"elapsed":2,"error_code":null,"except":null,"producer_block_id":"06e6b1ba9168dd08aed2609d3968406251764b01360bf35fd7884c3f23329f48","receipt":{"abi_sequence":4,"act_digest":"1de12a449248a0a06b1145cc52a81fed4076d47e61a2ebf7cf37ba6d57709b73","auth_sequence":[["algorguo1234",10],["eosio.ram",8696822]],"code_sequence":4,"global_sequence":"95347543685","receiver":"algorguo1234","recv_sequence":4},"receiver":"algorguo1234","trx_id":"ae559872968fdc9145d98c84b5e1445193b7d97d74e903905e4ec9f0d9803512"},"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","global_action_seq":95347543685,"irreversible":true},{"account_action_seq":11,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"},{"actor":"eosio.ram","permission":"active"}],"data":{"from":"algorguo1234","memo":"buy ram","quantity":"0.4206 EOS","to":"eosio.ram"},"hex_data":"40860854b34b5934000090e602ea30556e1000000000000004454f5300000000076275792072616d","name":"transfer"},"action_ordinal":8,"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","closest_unnotified_ancestor_action_ordinal":4,"console":"","context_free":false,"creator_action_ordinal":4,"elapsed":1,"error_code":null,"except":null,"producer_block_id":"06e6b1ba9168dd08aed2609d3968406251764b01360bf35fd7884c3f23329f48","receipt":{"abi_sequence":4,"act_digest":"1de12a449248a0a06b1145cc52a81fed4076d47e61a2ebf7cf37ba6d57709b73","auth_sequence":[["algorguo1234",11],["eosio.ram",8696823]],"code_sequence":4,"global_sequence":"95347543686","receiver":"eosio.ram","recv_sequence":3388326},"receiver":"eosio.ram","trx_id":"ae559872968fdc9145d98c84b5e1445193b7d97d74e903905e4ec9f0d9803512"},"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","global_action_seq":95347543686,"irreversible":true},{"account_action_seq":12,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"ram fee","quantity":"0.0022 EOS","to":"eosio.ramfee"},"hex_data":"40860854b34b5934a0d492e602ea3055160000000000000004454f53000000000772616d20666565","name":"transfer"},"action_ordinal":5,"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","closest_unnotified_ancestor_action_ordinal":2,"console":"","context_free":false,"creator_action_ordinal":2,"elapsed":15,"error_code":null,"except":null,"producer_block_id":"06e6b1ba9168dd08aed2609d3968406251764b01360bf35fd7884c3f23329f48","receipt":{"abi_sequence":4,"act_digest":"6cc3226aabc89544d9770ab2ddea64ca4dd3f5b72fb8f82315e1ad51f4f98ef7","auth_sequence":[["algorguo1234",12]],"code_sequence":4,"global_sequence":"95347543687","receiver":"eosio.token","recv_sequence":"22543127160"},"receiver":"eosio.token","trx_id":"ae559872968fdc9145d98c84b5e1445193b7d97d74e903905e4ec9f0d9803512"},"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","global_action_seq":95347543687,"irreversible":true},{"account_action_seq":13,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"ram fee","quantity":"0.0022 EOS","to":"eosio.ramfee"},"hex_data":"40860854b34b5934a0d492e602ea3055160000000000000004454f53000000000772616d20666565","name":"transfer"},"action_ordinal":9,"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","closest_unnotified_ancestor_action_ordinal":5,"console":"","context_free":false,"creator_action_ordinal":5,"elapsed":2,"error_code":null,"except":null,"producer_block_id":"06e6b1ba9168dd08aed2609d3968406251764b01360bf35fd7884c3f23329f48","receipt":{"abi_sequence":4,"act_digest":"6cc3226aabc89544d9770ab2ddea64ca4dd3f5b72fb8f82315e1ad51f4f98ef7","auth_sequence":[["algorguo1234",13]],"code_sequence":4,"global_sequence":"95347543688","receiver":"algorguo1234","recv_sequence":5},"receiver":"algorguo1234","trx_id":"ae559872968fdc9145d98c84b5e1445193b7d97d74e903905e4ec9f0d9803512"},"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","global_action_seq":95347543688,"irreversible":true},{"account_action_seq":14,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"ram fee","quantity":"0.0022 EOS","to":"eosio.ramfee"},"hex_data":"40860854b34b5934a0d492e602ea3055160000000000000004454f53000000000772616d20666565","name":"transfer"},"action_ordinal":10,"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","closest_unnotified_ancestor_action_ordinal":5,"console":"","context_free":false,"creator_action_ordinal":5,"elapsed":2,"error_code":null,"except":null,"producer_block_id":"06e6b1ba9168dd08aed2609d3968406251764b01360bf35fd7884c3f23329f48","receipt":{"abi_sequence":4,"act_digest":"6cc3226aabc89544d9770ab2ddea64ca4dd3f5b72fb8f82315e1ad51f4f98ef7","auth_sequence":[["algorguo1234",14]],"code_sequence":4,"global_sequence":"95347543689","receiver":"eosio.ramfee","recv_sequence":4694348},"receiver":"eosio.ramfee","trx_id":"ae559872968fdc9145d98c84b5e1445193b7d97d74e903905e4ec9f0d9803512"},"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","global_action_seq":95347543689,"irreversible":true},{"account_action_seq":15,"action_trace":{"account_ram_deltas":[{"account":"algorguo1234","delta":160}],"act":{"account":"eosio","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","receiver":"algorguotest","stake_cpu_quantity":"0.0200 EOS","stake_net_quantity":"0.0200 EOS","transfer":0},"hex_data":"40860854b34b593490b1ca54b34b5934c80000000000000004454f5300000000c80000000000000004454f530000000000","name":"delegatebw"},"action_ordinal":3,"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","closest_unnotified_ancestor_action_ordinal":0,"console":"","context_free":false,"creator_action_ordinal":0,"elapsed":46,"error_code":null,"except":null,"producer_block_id":"06e6b1ba9168dd08aed2609d3968406251764b01360bf35fd7884c3f23329f48","receipt":{"abi_sequence":17,"act_digest":"6652ac3291f7aa0f831f8ab27e760746080697e60553ec42123f3dfa2ee34d90","auth_sequence":[["algorguo1234",15]],"code_sequence":15,"global_sequence":"95347543693","receiver":"eosio","recv_sequence":134254754},"receiver":"eosio","trx_id":"ae559872968fdc9145d98c84b5e1445193b7d97d74e903905e4ec9f0d9803512"},"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","global_action_seq":95347543693,"irreversible":true},{"account_action_seq":16,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"stake bandwidth","quantity":"0.0400 EOS","to":"eosio.stake"},"hex_data":"40860854b34b59340014341903ea3055900100000000000004454f53000000000f7374616b652062616e647769647468","name":"transfer"},"action_ordinal":13,"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","closest_unnotified_ancestor_action_ordinal":3,"console":"","context_free":false,"creator_action_ordinal":3,"elapsed":13,"error_code":null,"except":null,"producer_block_id":"06e6b1ba9168dd08aed2609d3968406251764b01360bf35fd7884c3f23329f48","receipt":{"abi_sequence":4,"act_digest":"e2d631961b2fadae9ce7814f954d2c7c761f32b7debc42e4b750b9668cd027d1","auth_sequence":[["algorguo1234",16]],"code_sequence":4,"global_sequence":"95347543694","receiver":"eosio.token","recv_sequence":"22543127162"},"receiver":"eosio.token","trx_id":"ae559872968fdc9145d98c84b5e1445193b7d97d74e903905e4ec9f0d9803512"},"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","global_action_seq":95347543694,"irreversible":true},{"account_action_seq":17,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"stake bandwidth","quantity":"0.0400 EOS","to":"eosio.stake"},"hex_data":"40860854b34b59340014341903ea3055900100000000000004454f53000000000f7374616b652062616e647769647468","name":"transfer"},"action_ordinal":14,"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","closest_unnotified_ancestor_action_ordinal":13,"console":"","context_free":false,"creator_action_ordinal":13,"elapsed":2,"error_code":null,"except":null,"producer_block_id":"06e6b1ba9168dd08aed2609d3968406251764b01360bf35fd7884c3f23329f48","receipt":{"abi_sequence":4,"act_digest":"e2d631961b2fadae9ce7814f954d2c7c761f32b7debc42e4b750b9668cd027d1","auth_sequence":[["algorguo1234",17]],"code_sequence":4,"global_sequence":"95347543695","receiver":"algorguo1234","recv_sequence":6},"receiver":"algorguo1234","trx_id":"ae559872968fdc9145d98c84b5e1445193b7d97d74e903905e4ec9f0d9803512"},"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","global_action_seq":95347543695,"irreversible":true},{"account_action_seq":18,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"stake bandwidth","quantity":"0.0400 EOS","to":"eosio.stake"},"hex_data":"40860854b34b59340014341903ea3055900100000000000004454f53000000000f7374616b652062616e647769647468","name":"transfer"},"action_ordinal":15,"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","closest_unnotified_ancestor_action_ordinal":13,"console":"","context_free":false,"creator_action_ordinal":13,"elapsed":2,"error_code":null,"except":null,"producer_block_id":"06e6b1ba9168dd08aed2609d3968406251764b01360bf35fd7884c3f23329f48","receipt":{"abi_sequence":4,"act_digest":"e2d631961b2fadae9ce7814f954d2c7c761f32b7debc42e4b750b9668cd027d1","auth_sequence":[["algorguo1234",18]],"code_sequence":4,"global_sequence":"95347543696","receiver":"eosio.stake","recv_sequence":4916606},"receiver":"eosio.stake","trx_id":"ae559872968fdc9145d98c84b5e1445193b7d97d74e903905e4ec9f0d9803512"},"block_num":115782074,"block_time":"2020-04-16T09:17:10.000","global_action_seq":95347543696,"irreversible":true},{"account_action_seq":19,"action_trace":{"account_ram_deltas":[{"account":"algorguo1111","delta":2996}],"act":{"account":"eosio","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"active":{"accounts":[],"keys":[{"key":"EOS7g7U13sEC6BjAdFchavqfEuwjdbASKdmGDQjckF3YNpxcXBH26","weight":1}],"threshold":1,"waits":[]},"creator":"algorguo1234","name":"algorguo1111","owner":{"accounts":[],"keys":[{"key":"EOS7g7U13sEC6BjAdFchavqfEuwjdbASKdmGDQjckF3YNpxcXBH26","weight":1}],"threshold":1,"waits":[]}},"hex_data":"40860854b34b593410420854b34b5934010000000100036efbe4ca27fad971b3690cd01d1789ef53095dafcea8f9dd1bacb61af14fa91401000000010000000100036efbe4ca27fad971b3690cd01d1789ef53095dafcea8f9dd1bacb61af14fa91401000000","name":"newaccount"},"action_ordinal":1,"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","closest_unnotified_ancestor_action_ordinal":0,"console":"","context_free":false,"creator_action_ordinal":0,"elapsed":258,"error_code":null,"except":null,"producer_block_id":"06e6bb8911ebe89a9fee6aad86b3b2e208c42e52da508f2c02f60a0907dd72b8","receipt":{"abi_sequence":17,"act_digest":"3730aab41c062aca174076f283c691bae6f2da61f46bca708c5ba49a42f6a217","auth_sequence":[["algorguo1234",19]],"code_sequence":15,"global_sequence":"95361225225","receiver":"eosio","recv_sequence":134257390},"receiver":"eosio","trx_id":"3303c2fda0455cbe5410adbc7d48c16911f702473ab42e006fb2fd41d6490b8b"},"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","global_action_seq":95361225225,"irreversible":true},{"account_action_seq":20,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"bytes":4300,"payer":"algorguo1234","receiver":"algorguo1111"},"hex_data":"40860854b34b593410420854b34b5934cc100000","name":"buyrambytes"},"action_ordinal":2,"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","closest_unnotified_ancestor_action_ordinal":0,"console":"","context_free":false,"creator_action_ordinal":0,"elapsed":95,"error_code":null,"except":null,"producer_block_id":"06e6bb8911ebe89a9fee6aad86b3b2e208c42e52da508f2c02f60a0907dd72b8","receipt":{"abi_sequence":17,"act_digest":"7043e3eb58695cd1696e338c31dc6b4f7b7de2588e1bb616273cf3ce269730b7","auth_sequence":[["algorguo1234",20]],"code_sequence":15,"global_sequence":"95361225226","receiver":"eosio","recv_sequence":134257391},"receiver":"eosio","trx_id":"3303c2fda0455cbe5410adbc7d48c16911f702473ab42e006fb2fd41d6490b8b"},"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","global_action_seq":95361225226,"irreversible":true},{"account_action_seq":21,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"},{"actor":"eosio.ram","permission":"active"}],"data":{"from":"algorguo1234","memo":"buy ram","quantity":"0.2260 EOS","to":"eosio.ram"},"hex_data":"40860854b34b5934000090e602ea3055d40800000000000004454f5300000000076275792072616d","name":"transfer"},"action_ordinal":4,"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","closest_unnotified_ancestor_action_ordinal":2,"console":"","context_free":false,"creator_action_ordinal":2,"elapsed":48,"error_code":null,"except":null,"producer_block_id":"06e6bb8911ebe89a9fee6aad86b3b2e208c42e52da508f2c02f60a0907dd72b8","receipt":{"abi_sequence":4,"act_digest":"6ae28bf4b5266e16e98693ea1288532af79765a7fdf1cc6c09c2979a681bb6a7","auth_sequence":[["algorguo1234",21],["eosio.ram",8696884]],"code_sequence":4,"global_sequence":"95361225227","receiver":"eosio.token","recv_sequence":"22546526229"},"receiver":"eosio.token","trx_id":"3303c2fda0455cbe5410adbc7d48c16911f702473ab42e006fb2fd41d6490b8b"},"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","global_action_seq":95361225227,"irreversible":true},{"account_action_seq":22,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"},{"actor":"eosio.ram","permission":"active"}],"data":{"from":"algorguo1234","memo":"buy ram","quantity":"0.2260 EOS","to":"eosio.ram"},"hex_data":"40860854b34b5934000090e602ea3055d40800000000000004454f5300000000076275792072616d","name":"transfer"},"action_ordinal":7,"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","closest_unnotified_ancestor_action_ordinal":4,"console":"","context_free":false,"creator_action_ordinal":4,"elapsed":2,"error_code":null,"except":null,"producer_block_id":"06e6bb8911ebe89a9fee6aad86b3b2e208c42e52da508f2c02f60a0907dd72b8","receipt":{"abi_sequence":4,"act_digest":"6ae28bf4b5266e16e98693ea1288532af79765a7fdf1cc6c09c2979a681bb6a7","auth_sequence":[["algorguo1234",22],["eosio.ram",8696885]],"code_sequence":4,"global_sequence":"95361225228","receiver":"algorguo1234","recv_sequence":7},"receiver":"algorguo1234","trx_id":"3303c2fda0455cbe5410adbc7d48c16911f702473ab42e006fb2fd41d6490b8b"},"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","global_action_seq":95361225228,"irreversible":true},{"account_action_seq":23,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"},{"actor":"eosio.ram","permission":"active"}],"data":{"from":"algorguo1234","memo":"buy ram","quantity":"0.2260 EOS","to":"eosio.ram"},"hex_data":"40860854b34b5934000090e602ea3055d40800000000000004454f5300000000076275792072616d","name":"transfer"},"action_ordinal":8,"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","closest_unnotified_ancestor_action_ordinal":4,"console":"","context_free":false,"creator_action_ordinal":4,"elapsed":2,"error_code":null,"except":null,"producer_block_id":"06e6bb8911ebe89a9fee6aad86b3b2e208c42e52da508f2c02f60a0907dd72b8","receipt":{"abi_sequence":4,"act_digest":"6ae28bf4b5266e16e98693ea1288532af79765a7fdf1cc6c09c2979a681bb6a7","auth_sequence":[["algorguo1234",23],["eosio.ram",8696886]],"code_sequence":4,"global_sequence":"95361225229","receiver":"eosio.ram","recv_sequence":3388347},"receiver":"eosio.ram","trx_id":"3303c2fda0455cbe5410adbc7d48c16911f702473ab42e006fb2fd41d6490b8b"},"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","global_action_seq":95361225229,"irreversible":true},{"account_action_seq":24,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"ram fee","quantity":"0.0012 EOS","to":"eosio.ramfee"},"hex_data":"40860854b34b5934a0d492e602ea30550c0000000000000004454f53000000000772616d20666565","name":"transfer"},"action_ordinal":5,"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","closest_unnotified_ancestor_action_ordinal":2,"console":"","context_free":false,"creator_action_ordinal":2,"elapsed":20,"error_code":null,"except":null,"producer_block_id":"06e6bb8911ebe89a9fee6aad86b3b2e208c42e52da508f2c02f60a0907dd72b8","receipt":{"abi_sequence":4,"act_digest":"6eed19ce46414f75a1c4f34dff492107cb1755fd573fba457e274ba4a614ee16","auth_sequence":[["algorguo1234",24]],"code_sequence":4,"global_sequence":"95361225230","receiver":"eosio.token","recv_sequence":"22546526230"},"receiver":"eosio.token","trx_id":"3303c2fda0455cbe5410adbc7d48c16911f702473ab42e006fb2fd41d6490b8b"},"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","global_action_seq":95361225230,"irreversible":true},{"account_action_seq":25,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"ram fee","quantity":"0.0012 EOS","to":"eosio.ramfee"},"hex_data":"40860854b34b5934a0d492e602ea30550c0000000000000004454f53000000000772616d20666565","name":"transfer"},"action_ordinal":9,"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","closest_unnotified_ancestor_action_ordinal":5,"console":"","context_free":false,"creator_action_ordinal":5,"elapsed":5,"error_code":null,"except":null,"producer_block_id":"06e6bb8911ebe89a9fee6aad86b3b2e208c42e52da508f2c02f60a0907dd72b8","receipt":{"abi_sequence":4,"act_digest":"6eed19ce46414f75a1c4f34dff492107cb1755fd573fba457e274ba4a614ee16","auth_sequence":[["algorguo1234",25]],"code_sequence":4,"global_sequence":"95361225231","receiver":"algorguo1234","recv_sequence":8},"receiver":"algorguo1234","trx_id":"3303c2fda0455cbe5410adbc7d48c16911f702473ab42e006fb2fd41d6490b8b"},"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","global_action_seq":95361225231,"irreversible":true},{"account_action_seq":26,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"ram fee","quantity":"0.0012 EOS","to":"eosio.ramfee"},"hex_data":"40860854b34b5934a0d492e602ea30550c0000000000000004454f53000000000772616d20666565","name":"transfer"},"action_ordinal":10,"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","closest_unnotified_ancestor_action_ordinal":5,"console":"","context_free":false,"creator_action_ordinal":5,"elapsed":3,"error_code":null,"except":null,"producer_block_id":"06e6bb8911ebe89a9fee6aad86b3b2e208c42e52da508f2c02f60a0907dd72b8","receipt":{"abi_sequence":4,"act_digest":"6eed19ce46414f75a1c4f34dff492107cb1755fd573fba457e274ba4a614ee16","auth_sequence":[["algorguo1234",26]],"code_sequence":4,"global_sequence":"95361225232","receiver":"eosio.ramfee","recv_sequence":4694390},"receiver":"eosio.ramfee","trx_id":"3303c2fda0455cbe5410adbc7d48c16911f702473ab42e006fb2fd41d6490b8b"},"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","global_action_seq":95361225232,"irreversible":true},{"account_action_seq":27,"action_trace":{"account_ram_deltas":[{"account":"algorguo1234","delta":160}],"act":{"account":"eosio","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","receiver":"algorguo1111","stake_cpu_quantity":"0.4000 EOS","stake_net_quantity":"0.2500 EOS","transfer":0},"hex_data":"40860854b34b593410420854b34b5934c40900000000000004454f5300000000a00f00000000000004454f530000000000","name":"delegatebw"},"action_ordinal":3,"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","closest_unnotified_ancestor_action_ordinal":0,"console":"","context_free":false,"creator_action_ordinal":0,"elapsed":52,"error_code":null,"except":null,"producer_block_id":"06e6bb8911ebe89a9fee6aad86b3b2e208c42e52da508f2c02f60a0907dd72b8","receipt":{"abi_sequence":17,"act_digest":"1e40b5ec1d9ac06d0aabec2064893525bec97da327057903e4e476e4b6f2a987","auth_sequence":[["algorguo1234",27]],"code_sequence":15,"global_sequence":"95361225236","receiver":"eosio","recv_sequence":134257392},"receiver":"eosio","trx_id":"3303c2fda0455cbe5410adbc7d48c16911f702473ab42e006fb2fd41d6490b8b"},"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","global_action_seq":95361225236,"irreversible":true},{"account_action_seq":28,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"stake bandwidth","quantity":"0.6500 EOS","to":"eosio.stake"},"hex_data":"40860854b34b59340014341903ea3055641900000000000004454f53000000000f7374616b652062616e647769647468","name":"transfer"},"action_ordinal":13,"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","closest_unnotified_ancestor_action_ordinal":3,"console":"","context_free":false,"creator_action_ordinal":3,"elapsed":14,"error_code":null,"except":null,"producer_block_id":"06e6bb8911ebe89a9fee6aad86b3b2e208c42e52da508f2c02f60a0907dd72b8","receipt":{"abi_sequence":4,"act_digest":"a07c7ae6b8e69a2f37ff9106ae214e4fe4711151741e6e104e0cba491af79e98","auth_sequence":[["algorguo1234",28]],"code_sequence":4,"global_sequence":"95361225237","receiver":"eosio.token","recv_sequence":"22546526232"},"receiver":"eosio.token","trx_id":"3303c2fda0455cbe5410adbc7d48c16911f702473ab42e006fb2fd41d6490b8b"},"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","global_action_seq":95361225237,"irreversible":true},{"account_action_seq":29,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"stake bandwidth","quantity":"0.6500 EOS","to":"eosio.stake"},"hex_data":"40860854b34b59340014341903ea3055641900000000000004454f53000000000f7374616b652062616e647769647468","name":"transfer"},"action_ordinal":14,"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","closest_unnotified_ancestor_action_ordinal":13,"console":"","context_free":false,"creator_action_ordinal":13,"elapsed":1,"error_code":null,"except":null,"producer_block_id":"06e6bb8911ebe89a9fee6aad86b3b2e208c42e52da508f2c02f60a0907dd72b8","receipt":{"abi_sequence":4,"act_digest":"a07c7ae6b8e69a2f37ff9106ae214e4fe4711151741e6e104e0cba491af79e98","auth_sequence":[["algorguo1234",29]],"code_sequence":4,"global_sequence":"95361225238","receiver":"algorguo1234","recv_sequence":9},"receiver":"algorguo1234","trx_id":"3303c2fda0455cbe5410adbc7d48c16911f702473ab42e006fb2fd41d6490b8b"},"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","global_action_seq":95361225238,"irreversible":true},{"account_action_seq":30,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"algorguo1234","permission":"active"}],"data":{"from":"algorguo1234","memo":"stake bandwidth","quantity":"0.6500 EOS","to":"eosio.stake"},"hex_data":"40860854b34b59340014341903ea3055641900000000000004454f53000000000f7374616b652062616e647769647468","name":"transfer"},"action_ordinal":15,"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","closest_unnotified_ancestor_action_ordinal":13,"console":"","context_free":false,"creator_action_ordinal":13,"elapsed":2,"error_code":null,"except":null,"producer_block_id":"06e6bb8911ebe89a9fee6aad86b3b2e208c42e52da508f2c02f60a0907dd72b8","receipt":{"abi_sequence":4,"act_digest":"a07c7ae6b8e69a2f37ff9106ae214e4fe4711151741e6e104e0cba491af79e98","auth_sequence":[["algorguo1234",30]],"code_sequence":4,"global_sequence":"95361225239","receiver":"eosio.stake","recv_sequence":4916639},"receiver":"eosio.stake","trx_id":"3303c2fda0455cbe5410adbc7d48c16911f702473ab42e006fb2fd41d6490b8b"},"block_num":115784585,"block_time":"2020-04-16T09:38:05.500","global_action_seq":95361225239,"irreversible":true},{"account_action_seq":31,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"eosalgor1234","permission":"active"}],"data":{"from":"eosalgor1234","memo":"gg","quantity":"1.0000 EOS","to":"algorguo1234"},"hex_data":"40860897b268305540860854b34b5934102700000000000004454f5300000000026767","name":"transfer"},"action_ordinal":3,"block_num":116599809,"block_time":"2020-04-21T02:53:31.000","closest_unnotified_ancestor_action_ordinal":1,"console":"","context_free":false,"creator_action_ordinal":1,"elapsed":15,"error_code":null,"except":null,"producer_block_id":"06f32c014d4c33a47944a6d17c3be2699642e162b94990151a843d0bbe6f0c50","receipt":{"abi_sequence":4,"act_digest":"8f45c16e6a450fb04b666e60b6c1a983004df68b3e63ceae24c69b46b2571245","auth_sequence":[["eosalgor1234",11]],"code_sequence":4,"global_sequence":"99558165377","receiver":"algorguo1234","recv_sequence":10},"receiver":"algorguo1234","trx_id":"96bf40354ab86f4e0b356d8dab91affb5211d92ebcf4d6191bac78bcbd257dd1"},"block_num":116599809,"block_time":"2020-04-21T02:53:31.000","global_action_seq":99558165377,"irreversible":true},{"account_action_seq":32,"action_trace":{"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"eosalgor1234","permission":"active"}],"data":{"from":"eosalgor1234","memo":"hi","quantity":"0.1200 EOS","to":"algorguo1234"},"hex_data":"40860897b268305540860854b34b5934b00400000000000004454f5300000000026869","name":"transfer"},"action_ordinal":3,"block_num":116602908,"block_time":"2020-04-21T03:19:26.500","closest_unnotified_ancestor_action_ordinal":1,"console":"","context_free":false,"creator_action_ordinal":1,"elapsed":12,"error_code":null,"except":null,"producer_block_id":"06f3381c22b7a7e8682e6f8637ca7b93edcde3325b109576c9aac749f10e5d6f","receipt":{"abi_sequence":4,"act_digest":"5a17f798acb3927decdcdceb56b97f794952556db79f6c2724f5ecd302dc6133","auth_sequence":[["eosalgor1234",14]],"code_sequence":4,"global_sequence":"99574821665","receiver":"algorguo1234","recv_sequence":11},"receiver":"algorguo1234","trx_id":"5d4c2f71d0ec6b9f2d571238da1b31722a0fb346094bc00e9d0de1e599048ff9"},"block_num":116602908,"block_time":"2020-04-21T03:19:26.500","global_action_seq":99574821665,"irreversible":true}]
     * head_block_num : 117807806
     * last_irreversible_block : 117807469
     */

    private int head_block_num;
    private int last_irreversible_block;
    private List<ActionsBean> actions;

    public int getHead_block_num() {
        return head_block_num;
    }

    public void setHead_block_num(int head_block_num) {
        this.head_block_num = head_block_num;
    }

    public int getLast_irreversible_block() {
        return last_irreversible_block;
    }

    public void setLast_irreversible_block(int last_irreversible_block) {
        this.last_irreversible_block = last_irreversible_block;
    }

    public List<ActionsBean> getActions() {
        return actions;
    }

    public void setActions(List<ActionsBean> actions) {
        this.actions = actions;
    }

    public class ActionsBean {
        /**
         * account_action_seq : 0
         * action_trace : {"account_ram_deltas":[],"act":{"account":"eosio.token","authorization":[{"actor":"pieinstantoo","permission":"active"}],"data":{"from":"pieinstantoo","memo":"","quantity":"5.6559 EOS","to":"algorguo1234"},"hex_data":"40699e26e3e994ab40860854b34b5934efdc00000000000004454f530000000000","name":"transfer"},"action_ordinal":3,"block_num":91836719,"block_time":"2019-11-26T06:19:51.000","closest_unnotified_ancestor_action_ordinal":1,"console":"","context_free":false,"creator_action_ordinal":1,"elapsed":7,"error_code":null,"except":null,"producer_block_id":"0579512f75fd2a0e0aeef17ccab53bd8a17d8ca5b66df86e0da03179dd2c0c3a","receipt":{"abi_sequence":4,"act_digest":"7172e82ea6627b7e79df5d9665000cba3df87d062877a4a49a680aa3610e4a0f","auth_sequence":[["pieinstantoo",46343]],"code_sequence":4,"global_sequence":"16577199350","receiver":"algorguo1234","recv_sequence":1},"receiver":"algorguo1234","trx_id":"6873990b48055bd073219f9f6e162bf741293cf23a5c5aacd53746478d91dd22"}
         * block_num : 91836719
         * block_time : 2019-11-26T06:19:51.000
         * global_action_seq : 16577199350
         * irreversible : true
         */

        private int account_action_seq;
        private ActionTraceBean action_trace;
        private int block_num;
        private String block_time;
        private long global_action_seq;
        private boolean irreversible;

        public int getAccount_action_seq() {
            return account_action_seq;
        }

        public void setAccount_action_seq(int account_action_seq) {
            this.account_action_seq = account_action_seq;
        }

        public ActionTraceBean getAction_trace() {
            return action_trace;
        }

        public void setAction_trace(ActionTraceBean action_trace) {
            this.action_trace = action_trace;
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

        public long getGlobal_action_seq() {
            return global_action_seq;
        }

        public void setGlobal_action_seq(long global_action_seq) {
            this.global_action_seq = global_action_seq;
        }

        public boolean isIrreversible() {
            return irreversible;
        }

        public void setIrreversible(boolean irreversible) {
            this.irreversible = irreversible;
        }

        public class ActionTraceBean {
            /**
             * account_ram_deltas : []
             * act : {"account":"eosio.token","authorization":[{"actor":"pieinstantoo","permission":"active"}],"data":{"from":"pieinstantoo","memo":"","quantity":"5.6559 EOS","to":"algorguo1234"},"hex_data":"40699e26e3e994ab40860854b34b5934efdc00000000000004454f530000000000","name":"transfer"}
             * action_ordinal : 3
             * block_num : 91836719
             * block_time : 2019-11-26T06:19:51.000
             * closest_unnotified_ancestor_action_ordinal : 1
             * console :
             * context_free : false
             * creator_action_ordinal : 1
             * elapsed : 7
             * error_code : null
             * except : null
             * producer_block_id : 0579512f75fd2a0e0aeef17ccab53bd8a17d8ca5b66df86e0da03179dd2c0c3a
             * receipt : {"abi_sequence":4,"act_digest":"7172e82ea6627b7e79df5d9665000cba3df87d062877a4a49a680aa3610e4a0f","auth_sequence":[["pieinstantoo",46343]],"code_sequence":4,"global_sequence":"16577199350","receiver":"algorguo1234","recv_sequence":1}
             * receiver : algorguo1234
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
            private ReceiptBean receipt;
            private String receiver;
            private String trx_id;
            private List<?> account_ram_deltas;

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

            public ReceiptBean getReceipt() {
                return receipt;
            }

            public void setReceipt(ReceiptBean receipt) {
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

            public List<?> getAccount_ram_deltas() {
                return account_ram_deltas;
            }

            public void setAccount_ram_deltas(List<?> account_ram_deltas) {
                this.account_ram_deltas = account_ram_deltas;
            }

            public class ActBean {
                /**
                 * account : eosio.token
                 * authorization : [{"actor":"pieinstantoo","permission":"active"}]
                 * data : {"from":"pieinstantoo","memo":"","quantity":"5.6559 EOS","to":"algorguo1234"}
                 * hex_data : 40699e26e3e994ab40860854b34b5934efdc00000000000004454f530000000000
                 * name : transfer
                 */

                private String account;
                private Object data;
                private String hex_data;
                private String name;
                private List<AuthorizationBean> authorization;

                public String getAccount() {
                    return account;
                }

                public void setAccount(String account) {
                    this.account = account;
                }

                public Object getData() {
                    return data;
                }

                public void setData(Object data) {
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

//                public class DataBean {
//                    /**
//                     * from : pieinstantoo
//                     * memo :
//                     * quantity : 5.6559 EOS
//                     * to : algorguo1234
//                     */
//
//                    private String from;
//                    private String memo;
//                    private String quantity;
//                    private String to;
//
//                    public String getFrom() {
//                        return from;
//                    }
//
//                    public void setFrom(String from) {
//                        this.from = from;
//                    }
//
//                    public String getMemo() {
//                        return memo;
//                    }
//
//                    public void setMemo(String memo) {
//                        this.memo = memo;
//                    }
//
//                    public String getQuantity() {
//                        return quantity;
//                    }
//
//                    public void setQuantity(String quantity) {
//                        this.quantity = quantity;
//                    }
//
//                    public String getTo() {
//                        return to;
//                    }
//
//                    public void setTo(String to) {
//                        this.to = to;
//                    }
//                }

                public class AuthorizationBean {
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

            public class ReceiptBean {
                /**
                 * abi_sequence : 4
                 * act_digest : 7172e82ea6627b7e79df5d9665000cba3df87d062877a4a49a680aa3610e4a0f
                 * auth_sequence : [["pieinstantoo",46343]]
                 * code_sequence : 4
                 * global_sequence : 16577199350
                 * receiver : algorguo1234
                 * recv_sequence : 1
                 */

                private int abi_sequence;
                private String act_digest;
                private int code_sequence;
                private String global_sequence;
                private String receiver;
                private String recv_sequence;
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

                public String getRecv_sequence() {
                    return recv_sequence;
                }

                public void setRecv_sequence(String recv_sequence) {
                    this.recv_sequence = recv_sequence;
                }

                public List<List<String>> getAuth_sequence() {
                    return auth_sequence;
                }

                public void setAuth_sequence(List<List<String>> auth_sequence) {
                    this.auth_sequence = auth_sequence;
                }
            }
        }
    }
}
