package com.token.mangowallet.bean;

import java.util.List;

public class IndexMarkIndexBean {

    /**
     * code : 0
     * msg : success
     * data : {"num":"52.58","list":[{"createTime":"2020-06-28","num":"52.58"}]}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * num : 52.58
         * list : [{"createTime":"2020-06-28","num":"52.58"}]
         */

        private String num;
        private List<ListBean> list;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * createTime : 2020-06-28
             * num : 52.58
             */

            private String createTime;
            private String num;

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }
        }
    }
}
