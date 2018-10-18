package com.hacktown.bigiot.ecorouting.network.util;

import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hacktown.bigiot.ecorouting.network.service.GoogleService;
import com.hacktown.bigiot.ecorouting.network.service.RouteService;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    /**
     * Get Retrofit Instance
     */
    private static Retrofit.Builder getRetrofitInstance(@Nullable Interceptor interceptor) {
        Gson gson = new GsonBuilder()
            .setLenient()
            .create();

        return new Retrofit.Builder()
                .client(new OkHttpClient.Builder()
                .readTimeout(500000L, TimeUnit.SECONDS)
                .connectTimeout(500000L, TimeUnit.SECONDS)
                .writeTimeout(500000L, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .build())
                .addConverterFactory(GsonConverterFactory.create(gson));
    }

    /**
     * Get Route Service API.
     *
     * @return Route Service
     */
    public static RouteService getRoutingService(String url, @Nullable Interceptor interceptor) {
        return getRetrofitInstance(interceptor).baseUrl(url).build().create(RouteService.class);
    }

    /**
     * Get Route Service API.
     *
     * @return Route Service
     */
    public static GoogleService getGoogleService(String url, @Nullable Interceptor interceptor) {
        return getRetrofitInstance(interceptor).baseUrl(url).build().create(GoogleService.class);
    }


}
