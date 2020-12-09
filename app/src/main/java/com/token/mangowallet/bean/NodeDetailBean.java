package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class NodeDetailBean {

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

    public static class DataBean implements Parcelable {
        private long createAt;
        private String hash;
        private int id;
        private String mgpAddress;
        private String nodeContent;
        private String nodeHeadImg;
        private Object nodeId;
        private String nodeName;
        private String nodeRewardRule;
        private BigDecimal nodeShareRatio;
        private String nodeUrl;
        private Object updateAt;

        protected DataBean(Parcel in) {
            createAt = in.readLong();
            hash = in.readString();
            id = in.readInt();
            mgpAddress = in.readString();
            nodeContent = in.readString();
            nodeHeadImg = in.readString();
            nodeName = in.readString();
            nodeRewardRule = in.readString();
            nodeUrl = in.readString();
            nodeShareRatio = new BigDecimal(in.readString());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(createAt);
            dest.writeString(hash);
            dest.writeInt(id);
            dest.writeString(mgpAddress);
            dest.writeString(nodeContent);
            dest.writeString(nodeHeadImg);
            dest.writeString(nodeName);
            dest.writeString(nodeRewardRule);
            dest.writeString(nodeUrl);
            dest.writeString(nodeShareRatio.toPlainString());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public long getCreateAt() {
            return createAt;
        }

        public void setCreateAt(long createAt) {
            this.createAt = createAt;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMgpAddress() {
            return mgpAddress;
        }

        public void setMgpAddress(String mgpAddress) {
            this.mgpAddress = mgpAddress;
        }

        public String getNodeContent() {
            return nodeContent;
        }

        public void setNodeContent(String nodeContent) {
            this.nodeContent = nodeContent;
        }

        public String getNodeHeadImg() {
            return nodeHeadImg;
        }

        public void setNodeHeadImg(String nodeHeadImg) {
            this.nodeHeadImg = nodeHeadImg;
        }

        public Object getNodeId() {
            return nodeId;
        }

        public void setNodeId(Object nodeId) {
            this.nodeId = nodeId;
        }

        public String getNodeName() {
            return nodeName;
        }

        public void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }

        public String getNodeRewardRule() {
            return nodeRewardRule;
        }

        public void setNodeRewardRule(String nodeRewardRule) {
            this.nodeRewardRule = nodeRewardRule;
        }

        public BigDecimal getNodeShareRatio() {
            return nodeShareRatio;
        }

        public void setNodeShareRatio(BigDecimal nodeShareRatio) {
            this.nodeShareRatio = nodeShareRatio;
        }

        public String getNodeUrl() {
            return nodeUrl;
        }

        public void setNodeUrl(String nodeUrl) {
            this.nodeUrl = nodeUrl;
        }

        public Object getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(Object updateAt) {
            this.updateAt = updateAt;
        }
    }
}
