package com.example.maxim_ozarovskiy.ajtestapp.network;

import com.example.maxim_ozarovskiy.ajtestapp.network.service.CompleteTickerService;
import com.example.maxim_ozarovskiy.ajtestapp.network.service.CurrencyTypesService;
import com.example.maxim_ozarovskiy.ajtestapp.network.service.SimpleTickerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;


import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public class RESTClient {

    private static final String BASE_URL = "https://api.cryptonator.com/api/";
    private Retrofit retrofit;
    private final OkHttpClient client;

    private static RESTClient ourInstance = new RESTClient();

    public static RESTClient getInstance() {
        return ourInstance;
    }

    public RESTClient() {
        Gson gson = new GsonBuilder().create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                })
                .connectTimeout(1, TimeUnit.MINUTES)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(client)
                .build();
    }

    public CurrencyTypesService getCurrencyTypesService() {
        return retrofit.create(CurrencyTypesService.class);
    }
    public SimpleTickerService getSimpleTickerService() {
        return retrofit.create(SimpleTickerService.class);
    }
    public CompleteTickerService getCompleteTickerService() {
        return retrofit.create(CompleteTickerService.class);
    }
}
