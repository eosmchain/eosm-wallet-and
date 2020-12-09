package com.token.mangowallet.bean;

import java.util.List;

public class GoodsTypeBean {
    /**
     * code : 0
     * data : [{"cateName":"电子产品","sort":1,"pic":"http://gfs9.gomein.net.cn/T15dxvBsdT1RCvBVdK.png","cateImg":"https://api.coom.pub/img/itenTwo-Chinese.png","addTime":"2020-07-29","isDel":false,"id":1,"cateID":1},{"cateName":"生活用品","sort":2,"pic":"http://gfs7.gomein.net.cn/T1cvKvBbhT1RCvBVdK.png","cateImg":"https://api.coom.pub/img/itenTwo-Chinese.png","addTime":"2020-07-29","isDel":false,"id":2,"cateID":2},{"cateName":"家居家装","sort":3,"pic":"http://gfs7.gomein.net.cn/T1cvKvBbhT1RCvBVdK.png","cateImg":"https://api.coom.pub/img/itenTwo-Chinese.png","addTime":"2020-07-30","isDel":false,"id":3,"cateID":3},{"cateName":"领美逗","sort":4,"pic":"http://gfs9.gomein.net.cn/T1VGDvB4Vj1RCvBVdK.png","cateImg":"https://api.coom.pub/img/itenTwo-Chinese.png","addTime":"2020-07-30","isDel":false,"id":4,"cateID":4},{"cateName":"领券中心","sort":5,"pic":"http://gfs5.gomein.net.cn/T1EvKvBv_v1RCvBVdK.png","cateImg":"https://api.coom.pub/img/itenTwo-Chinese.png","addTime":"2020-07-30","isDel":false,"id":5,"cateID":5}]
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
         * cateName : 电子产品
         * sort : 1
         * pic : http://gfs9.gomein.net.cn/T15dxvBsdT1RCvBVdK.png
         * cateImg : https://api.coom.pub/img/itenTwo-Chinese.png
         * addTime : 2020-07-29
         * isDel : false
         * id : 1
         * cateID : 1
         */

        private String cateName;
        private int sort;
        private String pic;
        private String cateImg;
        private String addTime;
        private boolean isDel;
        private int id;
        private int cateID;

        public String getCateName() {
            return cateName;
        }

        public void setCateName(String cateName) {
            this.cateName = cateName;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getCateImg() {
            return cateImg;
        }

        public void setCateImg(String cateImg) {
            this.cateImg = cateImg;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public boolean isIsDel() {
            return isDel;
        }

        public void setIsDel(boolean isDel) {
            this.isDel = isDel;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCateID() {
            return cateID;
        }

        public void setCateID(int cateID) {
            this.cateID = cateID;
        }
    }
}
