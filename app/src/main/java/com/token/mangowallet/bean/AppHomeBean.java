package com.token.mangowallet.bean;

import java.util.List;

public class AppHomeBean {

    /**
     * code : 0
     * data : {"app":[{"childTitle":"链上绑定，智能识别","id":5,"isDel":false,"lang":"zh_CN","sort":1,"tab":"去中心化网络","title":"MID身份标识","type":0},{"childTitle":"锁仓即挖矿","id":21,"isDel":false,"lang":"zh_CN","sort":1,"tab":"Mango 算力","title":"POS抵押","type":1},{"childTitle":"随存随取，实时收益","id":1,"isDel":false,"lang":"zh_CN","sort":2,"tab":"Mango 算力","title":"持币生息","type":0},{"childTitle":"消费即挖矿","id":25,"isDel":false,"lang":"zh_CN","sort":2,"tab":"Mango 链商","title":"MangoMall","type":1},{"childTitle":"智能结算，公平分配","id":9,"isDel":false,"lang":"zh_CN","sort":3,"tab":"Mango 社群","title":"团队分享","type":0},{"childTitle":"去中心化金融","id":29,"isDel":false,"lang":"zh_CN","sort":3,"tab":"Mango 金融","title":"MangoDeFi","type":1},{"childTitle":"智能结算，实时激励","id":13,"isDel":false,"lang":"zh_CN","sort":4,"tab":"Mango 轻节点","title":"KOL激励","type":0}],"slider":["https://api.coom.pub/img/1-1.jpg","https://api.coom.pub/img/1-2.jpg","https://api.coom.pub/img/1-3.jpg","https://api.coom.pub/img/1-4.jpg"]}
     * msg : null
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private List<AppBean> app;
        private List<String> slider;

        public List<AppBean> getApp() {
            return app;
        }

        public void setApp(List<AppBean> app) {
            this.app = app;
        }

        public List<String> getSlider() {
            return slider;
        }

        public void setSlider(List<String> slider) {
            this.slider = slider;
        }

        public static class AppBean implements Comparable<AppBean> {
            /**
             * childTitle : 链上绑定，智能识别
             * id : 5
             * isDel : false
             * lang : zh_CN
             * sort : 1
             * tab : 去中心化网络
             * title : MID身份标识
             * type : 0
             * <p>
             * <p>
             * "img":"https://api.mgpchain.com/static/img/9723780661781597831851785.png",
             * "tabImg":"https://api.mgpchain.com/static/img/2685421717411597831657177.png",
             * "appType":
             */

            private String childTitle;
            private int id;
            private boolean isDel;
            private String lang;
            private int sort;
            private String tab;
            private String title;
            private int type;
            private String subTitle;
            private String img;
            private String tabImg;
            private int appTypel;

            public String getChildTitle() {
                return childTitle;
            }

            public void setChildTitle(String childTitle) {
                this.childTitle = childTitle;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public boolean isIsDel() {
                return isDel;
            }

            public void setIsDel(boolean isDel) {
                this.isDel = isDel;
            }

            public String getLang() {
                return lang;
            }

            public void setLang(String lang) {
                this.lang = lang;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getTab() {
                return tab;
            }

            public void setTab(String tab) {
                this.tab = tab;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getSubTitle() {
                return subTitle;
            }

            public void setSubTitle(String subTitle) {
                this.subTitle = subTitle;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getTabImg() {
                return tabImg;
            }

            public void setTabImg(String tabImg) {
                this.tabImg = tabImg;
            }

            public int getAppTypel() {
                return appTypel;
            }

            public void setAppTypel(int appTypel) {
                this.appTypel = appTypel;
            }

            @Override
            public int compareTo(AppBean o) {
                int i = this.getSort() - o.getSort();//先按照年龄排序
                if (i == 0) {
                    return this.id - o.getId();//如果年龄相等了再用分数进行排序
                }
                return i;
            }
        }
    }
}
