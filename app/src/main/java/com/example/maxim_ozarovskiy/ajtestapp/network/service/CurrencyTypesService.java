package com.example.maxim_ozarovskiy.ajtestapp.network.service;

import com.example.maxim_ozarovskiy.ajtestapp.model.CurrencyTypesExample;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public interface CurrencyTypesService {

    @GET("currencies")
    Call<CurrencyTypesExample> currencyTypesService();
}
