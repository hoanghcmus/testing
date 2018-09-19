package com.hoangvnit.stackoverflow.rest;


import com.hoangvnit.stackoverflow.mvp.pojo.UserReputationResponseModel;
import com.hoangvnit.stackoverflow.mvp.pojo.UserResponseModel;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface UserService {

    @GET("users")
    Observable<UserResponseModel> getUsers(@Query("page") int page, @Query("pagesize") int amount, @Query("site") String site);

    @GET("users/{userId}/reputation-history")
    Observable<UserReputationResponseModel> getUserReputations(@Path("userId") int userId, @Query("page") int page, @Query("pagesize") int amount, @Query("site") String site);


}
