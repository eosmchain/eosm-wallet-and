package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ProListBean implements Parcelable {

    /**
     * list : [{"merId":7908,"image":"[\"https://api.coom.pub/img/6317738730161598670271804.jpg\"]","image_url":["https://api.coom.pub/img/6317738730161598670271804.jpg"],"sliderImages":["https://api.coom.pub/img/9639475086311598670273246.jpg","https://api.coom.pub/img/3429599893431598670273246.jpg"],"storeName":"50","storeUnit":"1","storeInfo":"123456","storeType":"1","cateId":1,"price":5,"postage":0,"sales":0,"stock":5,"isShow":true,"isHot":false,"isNew":true,"isPostage":true,"isDel":false,"givePro":0.2,"bonusPro":0.3,"buyerPro":0.5,"browse":0,"addTime":"2020-08-29","address":"111","cateName":"ele.name","countryNum":1,"countryName":"中国","auditStatus":1,"proID":17},{"merId":7908,"image":"[\"https://api.coom.pub/static/img/9476636002571598673034715.jpg\"]","image_url":["https://api.coom.pub/static/img/9476636002571598673034715.jpg"],"sliderImages":["https://api.coom.pub/static/img/0364425722801598673037398.jpg","https://api.coom.pub/static/img/6322359800751598673037399.jpg"],"storeName":"hah","storeUnit":"1","storeInfo":"123","storeType":"1","cateId":1,"price":1,"postage":1,"sales":0,"stock":1,"isShow":true,"isHot":false,"isNew":true,"isPostage":true,"isDel":false,"givePro":0.2,"bonusPro":0.47,"buyerPro":0.33,"browse":0,"addTime":"2020-08-29","address":"111","cateName":"ele.name","countryNum":1,"countryName":"中国","auditStatus":1,"proID":18}]
     * part : 1
     * pic : https://api.mgpchain.com/static/img/0931155999481600076779839.png
     */

    private int part;
    private String pic;
    private List<ProBean> list;

    protected ProListBean(Parcel in) {
        part = in.readInt();
        pic = in.readString();
        list = in.createTypedArrayList(ProBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(part);
        dest.writeString(pic);
        dest.writeTypedList(list);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProListBean> CREATOR = new Creator<ProListBean>() {
        @Override
        public ProListBean createFromParcel(Parcel in) {
            return new ProListBean(in);
        }

        @Override
        public ProListBean[] newArray(int size) {
            return new ProListBean[size];
        }
    };

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<ProBean> getList() {
        return list;
    }

    public void setList(List<ProBean> list) {
        this.list = list;
    }

}
