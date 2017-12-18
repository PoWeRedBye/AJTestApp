package com.example.maxim_ozarovskiy.ajtestapp.presenter;


import android.content.Context;

import com.example.maxim_ozarovskiy.ajtestapp.data.DataManager;
import com.example.maxim_ozarovskiy.ajtestapp.interfaces.MainActivityContract;
import com.example.maxim_ozarovskiy.ajtestapp.model.SimpleTickerExample;
import com.example.maxim_ozarovskiy.ajtestapp.network.RESTClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {

    MainActivityContract.View view;

    private String value;
    private SimpleTickerExample simpleTickerExample;
    private Context context;
    private DataManager dataManager;


    public MainActivityPresenter(Context ctx, MainActivityContract.View view){
        this.context = ctx;
        this.view = view;
    }

    private void makeConversion(String base, String target){
        RESTClient.getInstance().getSimpleTickerService().simpleTickerService(base +"-" + target).enqueue(new Callback<SimpleTickerExample>() {
            @Override
            public void onResponse(Call<SimpleTickerExample> call, Response<SimpleTickerExample> response) {
                if(response.isSuccessful()){
                    if (response.body().getSuccess()){
                        simpleTickerExample = response.body();
                        convert(simpleTickerExample,value);
                    } else {
                        view.callbackErrorMessage(response.body().getError());
                    }
                }else {
                    view.callbackErrorMessage(response.message());
                }
            }
            @Override
            public void onFailure(Call<SimpleTickerExample> call, Throwable t) {

            }
        });
    }

    private void convert(SimpleTickerExample ste, String value){
        double one = Double.parseDouble(ste.getSimpleTicker().getPrice());
        double two = Double.parseDouble(value);
        double converted = two * one;
        double newDouble = new BigDecimal(converted).setScale(2, RoundingMode.UP).doubleValue();
        String convertedValue = String.valueOf(newDouble);
        long date = ste.getTimestamp();
        Date time = new Date();
        time.setTime(date * 1000);
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String formattedDate = format.format(time);
        sendDataToDB(convertedValue,value,simpleTickerExample,formattedDate);
        view.callbackConversion(convertedValue);
    }

    private void sendDataToDB(String targetValue, String baseValue, SimpleTickerExample ste, String time) {
        dataManager = new DataManager(context);
        dataManager.open();
        String targetName = ste.getSimpleTicker().getTarget();
        String targetPrice = ste.getSimpleTicker().getPrice();
        String baseName = ste.getSimpleTicker().getBase();
        String baseCode = ste.getSimpleTicker().getBase();
        String targetCode = ste.getSimpleTicker().getTarget();
        dataManager.saveNewConverterRequest(targetName,targetPrice,baseValue,baseName,baseCode,targetValue,targetCode,time);
        dataManager.close();
    }

    @Override
    public void callConversion(String base, String target, String conversionValue) {
        value = conversionValue;
        makeConversion(base,target);
    }
}
