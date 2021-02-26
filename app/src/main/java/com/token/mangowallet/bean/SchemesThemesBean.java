package com.token.mangowallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SchemesThemesBean {

    /**
     * rows : [{"account":"mgptest11111","id":0,"scheme_title":"嗄咯","scheme_content":"咯女","created_at":"2021-02-23T09:56:23","updated_at":"2021-02-23T09:56:23","vote_count":"2.0000 MGP","is_del":0,"cash_money":"1.0000 MGP","is_remit":0}]
     * more : false
     * next_key :
     */

    private boolean more;
    private String next_key;
    private List<RowsBean> rows;

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public String getNext_key() {
        return next_key;
    }

    public void setNext_key(String next_key) {
        this.next_key = next_key;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean implements Parcelable {
        /**
         * account : mgptest11111
         * id : 0
         * scheme_title : 嗄咯
         * scheme_content : 咯女
         * created_at : 2021-02-23T09:56:23
         * updated_at : 2021-02-23T09:56:23
         * vote_count : 2.0000 MGP
         * is_del : 0
         * cash_money : 1.0000 MGP
         * is_remit : 0
         */

        private int id;
        private String account;
        private String vote_count;
        private String created_at;
        private String scheme_title;
        private String scheme_content;
        private int scheme_id;
        ////////////////////////////////
        private String updated_at;
        private int is_del;
        private String cash_money;
        private int is_remit;
        private int audit_status;
        protected RowsBean(Parcel in) {
            id = in.readInt();
            account = in.readString();
            vote_count = in.readString();
            created_at = in.readString();
            scheme_title = in.readString();
            scheme_content = in.readString();
            scheme_id = in.readInt();
            updated_at = in.readString();
            is_del = in.readInt();
            cash_money = in.readString();
            is_remit = in.readInt();
            audit_status = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(account);
            dest.writeString(vote_count);
            dest.writeString(created_at);
            dest.writeString(scheme_title);
            dest.writeString(scheme_content);
            dest.writeInt(scheme_id);
            dest.writeString(updated_at);
            dest.writeInt(is_del);
            dest.writeString(cash_money);
            dest.writeInt(is_remit);
            dest.writeInt(audit_status);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<RowsBean> CREATOR = new Creator<RowsBean>() {
            @Override
            public RowsBean createFromParcel(Parcel in) {
                return new RowsBean(in);
            }

            @Override
            public RowsBean[] newArray(int size) {
                return new RowsBean[size];
            }
        };

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getScheme_title() {
            return scheme_title;
        }

        public void setScheme_title(String scheme_title) {
            this.scheme_title = scheme_title;
        }

        public String getScheme_content() {
            return scheme_content;
        }

        public void setScheme_content(String scheme_content) {
            this.scheme_content = scheme_content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getVote_count() {
            return vote_count;
        }

        public void setVote_count(String vote_count) {
            this.vote_count = vote_count;
        }

        public int getIs_del() {
            return is_del;
        }

        public void setIs_del(int is_del) {
            this.is_del = is_del;
        }

        public String getCash_money() {
            return cash_money;
        }

        public void setCash_money(String cash_money) {
            this.cash_money = cash_money;
        }

        public int getIs_remit() {
            return is_remit;
        }

        public void setIs_remit(int is_remit) {
            this.is_remit = is_remit;
        }

        public int getScheme_id() {
            return scheme_id;
        }

        public void setScheme_id(int scheme_id) {
            this.scheme_id = scheme_id;
        }

        public int getAudit_status() {
            return audit_status;
        }

        public void setAudit_status(int audit_status) {
            this.audit_status = audit_status;
        }
    }
}
