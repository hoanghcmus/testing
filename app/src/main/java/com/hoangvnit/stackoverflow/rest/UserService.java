package com.hoangvnit.stackoverflow.rest;

import com.hoangvnit.stackoverflow.mvp.pojo.UserModel;
import com.hoangvnit.stackoverflow.mvp.pojo.UserResponseModel;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface UserService {

    @GET("users")
    Observable<UserResponseModel> getUsers(@Query("page") int page, @Query("pagesize") int amount, @Query("site") String site);


}
