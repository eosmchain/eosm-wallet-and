package com.token.mangowallet.bean;

import java.util.List;

public class PayConfigBean {


    /**
     * code : 0
     * msg : success
     * data : [{"id":1,"name":"MGP","isDel":false,"createTime":"2020-09-22","pic":"https://api.mgpchain.com/static/img/4059368566361600828643744.png"},{"id":2,"name":"BTC","isDel":false,"createTime":"2020-09-22","pic":"https://api.mgpchain.com/static/img/0843551654301600828577152.png"},{"id":3,"name":"USDT","isDel":false,"createTime":"2020-09-22","pic":"https://api.mgpchain.com/static/img/4388383598131600828669488.png"},{"id":4,"name":"EOS","isDel":false,"createTime":"2020-09-22","pic":"https://api.mgpchain.com/static/img/4015614171021600828690521.png"},{"id":5,"name":"ETH","isDel":false,"createTime":"2020-09-22","pic":"https://api.mgpchain.com/static/img/1377065570761600828704936.png"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * payId : 1
         * name : MGP
         * isDel : false
         * createTime : 2020-09-22
         * pic : https://api.mgpchain.com/static/img/4059368566361600828643744.png
         */

        private int payId;
        private String name;
        private boolean isDel;
        private String createTime;
        private String pic;

        public int getId() {
            return payId;
        }

        public void setId(int payId) {
            this.payId = payId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isIsDel() {
            return isDel;
        }

        public void setIsDel(boolean isDel) {
            this.isDel = isDel;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
