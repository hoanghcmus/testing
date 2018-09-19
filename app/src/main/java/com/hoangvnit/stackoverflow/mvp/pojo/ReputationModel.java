package com.hoangvnit.stackoverflow.mvp.pojo;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.hoangvnit.stackoverflow.common.Setting;
import com.hoangvnit.stackoverflow.utils.LogUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReputationModel extends BaseModel {

    @Expose
    private String reputation_history_type;

    @Expose
    private String reputation_change;

    @Expose
    private Date creation_date;

    @Expose
    private int post_id;

    public String getReputation_history_type() {
        return reputation_history_type;
    }

    public void setReputation_history_type(String reputation_history_type) {
        this.reputation_history_type = reputation_history_type;
    }

    public String getReputation_change() {
        return reputation_change;
    }

    public void setReputation_change(String reputation_change) {
        this.reputation_change = reputation_change;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.reputation_history_type);
        dest.writeString(this.reputation_change);
        dest.writeString(this.creation_date.toString());
        dest.writeInt(this.post_id);
    }

    protected ReputationModel(Parcel in) {
        this.reputation_history_type = in.readString();
        this.reputation_change = in.readString();
        DateFormat df = new SimpleDateFormat(Setting.GSON_DATE_FORMAT);
        try {
            this.creation_date = df.parse(in.readString());
        } catch (ParseException e) {
            LogUtils.e("Read data from Parcel failed: " + e.getMessage());
        }
        this.post_id = in.readInt();
    }

    public static final Creator<ReputationModel> CREATOR = new Creator<ReputationModel>() {
        @Override
        public ReputationModel createFromParcel(Parcel source) {
            return new ReputationModel(source);
        }

        @Override
        public ReputationModel[] newArray(int size) {
            return new ReputationModel[size];
        }
    };
}
