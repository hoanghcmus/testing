package com.hoangvnit.stackoverflow.mvp.pojo;

import android.os.Parcel;

import com.google.gson.annotations.Expose;

public class UserModel extends BaseModel{

    @Expose
    private int user_id;

    @Expose
    private String display_name;

    @Expose
    private String location;

    @Expose
    private int age;

    @Expose
    private String profile_image;

    @Expose
    private boolean is_employee;

    @Expose
    private int reputation;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUser_image() {
        return profile_image;
    }

    public void setUser_image(String User_image) {
        this.profile_image = User_image;
    }

    public boolean is_employee() {
        return is_employee;
    }

    public void setIs_employee(boolean is_employee) {
        this.is_employee = is_employee;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.display_name);
        dest.writeString(this.profile_image);
        dest.writeByte((byte) (this.is_employee ? 1 : 0));
        dest.writeInt(this.reputation);
    }

    protected UserModel(Parcel in) {
        this.display_name = in.readString();
        this.profile_image = in.readString();
        this.is_employee = in.readByte() != 0;
        this.reputation = in.readInt();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
}
