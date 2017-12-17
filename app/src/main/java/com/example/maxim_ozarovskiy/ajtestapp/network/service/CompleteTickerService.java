package com.example.maxim_ozarovskiy.ajtestapp.network.service;

import com.example.maxim_ozarovskiy.ajtestapp.model.CompleteTickerExample;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public interface CompleteTickerService {

    @GET("full/{currency}")
    Call<CompleteTickerExample> completeTickerService(@Path("currency") String currency);
}
