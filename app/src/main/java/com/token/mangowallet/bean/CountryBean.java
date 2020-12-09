package com.token.mangowallet.bean;

import java.util.List;

public class CountryBean {

    /**
     * code : 0
     * msg : success
     * data : [{"id":1,"countyName":"中国","countyImage":"https://api.mgpchain.com/static/img/6414950315071598328076602.jpg","sort":1,"isDel":false},{"id":2,"countyName":"日本","countyImage":"https://api.mgpchain.com/static/img/7460795761821598328153001.jpg","sort":2,"isDel":false},{"id":3,"countyName":"韩国","countyImage":"https://api.mgpchain.com/static/img/4135230677841598328285209.jpg","sort":3,"isDel":false},{"id":4,"countyName":"马来西亚","countyImage":"https://api.mgpchain.com/static/img/9670550412981598328380744.jpg","sort":4,"isDel":false},{"id":5,"countyName":"印度","countyImage":"https://api.mgpchain.com/static/img/6414003328121598328427595.jpg","sort":5,"isDel":false}]
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
         * id : 1
         * countyName : 中国
         * countyImage : https://api.mgpchain.com/static/img/6414950315071598328076602.jpg
         * sort : 1
         * isDel : false
         */

        private int id;
        private String countyName;
        private String countyImage;
        private int sort;
        private boolean isDel;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCountyName() {
            return countyName;
        }

        public void setCountyName(String countyName) {
            this.countyName = countyName;
        }

        public String getCountyImage() {
            return countyImage;
        }

        public void setCountyImage(String countyImage) {
            this.countyImage = countyImage;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public boolean isIsDel() {
            return isDel;
        }

        public void setIsDel(boolean isDel) {
            this.isDel = isDel;
        }
    }
}
