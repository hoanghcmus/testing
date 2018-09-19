package com.hoangvnit.stackoverflow.rest;

import com.hoangvnit.stackoverflow.mvp.pojo.UserModel;
import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface UserService {

    @GET("users")
    Observable<List<UserModel>> getUsers(@Query("page") int page, @Query("pagesize") int amount, @Query("pagesize") String site);


}
