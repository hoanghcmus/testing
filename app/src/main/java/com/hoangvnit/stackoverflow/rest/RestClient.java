package com.hoangvnit.stackoverflow.rest;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hoangvnit.stackoverflow.common.Setting;
import com.hoangvnit.stackoverflow.utils.LogUtils;
import com.hoangvnit.stackoverflow.utils.NetworkUtils;
import com.hoangvnit.stackoverflow.utils.NoNetworkException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * Rest client used for synchronize data via network
 *
 * @author Nguyen Ngoc Hoang (www.hoangvnit.com)
 */
public class RestClient {
    private static RestClient sInstance = null;
    private OkHttpClient mHttpClient;
    private UserService mUserService;
    private boolean isInitialized = false;

    public static synchronized RestClient getInstance() {
        if (sInstance == null) {
            sInstance = new RestClient();
        }
        return sInstance;
    }

    private void setupCache(Context context) {
        try {
            File cacheDir = new File(context.getCacheDir(), Setting.OK_HTTP_CLIENT_CACHE_FILE_NAME);
            Cache cache = new Cache(cacheDir, Setting.OK_HTTP_CLIENT_CACHE_SIZE);

            Interceptor headerInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request newRequest;

                    newRequest = request.newBuilder()
                            .addHeader(Setting.REQUEST_HEADER_CONTENT_TYPE, Setting.REQUEST_HEADER_CONTENT_TYPE_JSON_VALUE)
                            .addHeader(Setting.REQUEST_HEADER_ACCEPT_LANGUAGE, Setting.REQUEST_HEADER_ACCEPT_LANGUAGE_VALUE)
                            .build();
                    return chain.proceed(newRequest);
                }
            };

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            mHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new NetworkInterceptor(context))
                    .addInterceptor(headerInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .cache(cache)
                    .connectTimeout(Setting.OK_HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(Setting.OK_HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(Setting.OK_HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
                    .build();

        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
    }

    public void init(final Context context) {
        if (isInitialized) {
            return;
        }

        setupCache(context);

        Gson gson = new GsonBuilder().setDateFormat(Setting.GSON_DATE_FORMAT).create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Setting.REST_ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(mHttpClient)
                .build();


        mUserService = retrofit.create(UserService.class);

        isInitialized = true;
    }

    public UserService getUserService() {
        return mUserService;
    }

    class NetworkInterceptor implements Interceptor {

        Context mContext;

        public NetworkInterceptor(Context context) {
            mContext = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            if (mContext != null && !NetworkUtils.isNetworkConnected(mContext)) {
                throw new NoNetworkException();
            }
            mContext = null;
            return chain.proceed(chain.request());
        }
    }
}
