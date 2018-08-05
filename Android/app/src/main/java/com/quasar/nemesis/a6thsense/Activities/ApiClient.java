package com.quasar.nemesis.a6thsense.Activities;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vipla on 31-03-2018.
 */

public class ApiClient {
    private static Retrofit retrofit = null;

    static Retrofit getClient() {
/*
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("app_id","de04b272")
                        .addHeader("app_key","3d55d8284df120c1913cf19af6e4c1c2").build();
                return chain.proceed(request);
            }
        });*/
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();




        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.kairos.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();



        return retrofit;
    }
}
