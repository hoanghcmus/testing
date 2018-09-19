package com.hoangvnit.stackoverflow.mvp.pojo;

import android.os.Parcel;

import com.google.gson.annotations.Expose;

import java.util.List;

public class UserResponseModel extends BaseModel {

    @Expose
    List<UserModel> items;

    public List<UserModel> getItems() {
        return items;
    }

    public void setItems(List<UserModel> items) {
        this.items = items;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(items);
    }

    protected UserResponseModel(Parcel in) {
        in.readList(this.items, List.class.getClassLoader());
    }

    public static final Creator<UserResponseModel> CREATOR = new Creator<UserResponseModel>() {
        @Override
        public UserResponseModel createFromParcel(Parcel source) {
            return new UserResponseModel(source);
        }

        @Override
        public UserResponseModel[] newArray(int size) {
            return new UserResponseModel[size];
        }
    };
}

