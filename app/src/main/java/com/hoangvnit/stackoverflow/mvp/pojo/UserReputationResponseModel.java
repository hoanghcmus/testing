package com.hoangvnit.stackoverflow.mvp.pojo;

import android.os.Parcel;

import com.google.gson.annotations.Expose;

import java.util.List;

public class UserReputationResponseModel extends BaseModel {

    @Expose
    List<ReputationModel> items;

    public List<ReputationModel> getItems() {
        return items;
    }

    public void setItems(List<ReputationModel> items) {
        this.items = items;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(items);
    }

    protected UserReputationResponseModel(Parcel in) {
        in.readList(this.items, List.class.getClassLoader());
    }

    public static final Creator<UserReputationResponseModel> CREATOR = new Creator<UserReputationResponseModel>() {
        @Override
        public UserReputationResponseModel createFromParcel(Parcel source) {
            return new UserReputationResponseModel(source);
        }

        @Override
        public UserReputationResponseModel[] newArray(int size) {
            return new UserReputationResponseModel[size];
        }
    };
}

