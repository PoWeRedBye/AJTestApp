package com.example.maxim_ozarovskiy.ajtestapp.presenter;

import android.content.Context;

import com.example.maxim_ozarovskiy.ajtestapp.data.DataManager;
import com.example.maxim_ozarovskiy.ajtestapp.interfaces.CompleteTickerActivityContract;
import com.example.maxim_ozarovskiy.ajtestapp.model.CompleteTickerExample;
import com.example.maxim_ozarovskiy.ajtestapp.model.Market;
import com.example.maxim_ozarovskiy.ajtestapp.network.RESTClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public class CompleteTickerActivityPresenter implements CompleteTickerActivityContract.Presenter {

    CompleteTickerActivityContract.View view;

    private String value;
    private CompleteTickerExample completeTickerExample;
    private List<Market> marketList;
    private Context context;
    private DataManager dataManager;

    public CompleteTickerActivityPresenter(Context ctx, CompleteTickerActivityContract.View view) {
        this.context = ctx;
        this.view = view;
    }

    private void makeConversion(String base, String target){

        RESTClient.getInstance().getCompleteTickerService().completeTickerService(base + "-" + target).enqueue(new Callback<CompleteTickerExample>() {
            @Override
            public void onResponse(Call<CompleteTickerExample> call, Response<CompleteTickerExample> response) {
                if(response.isSuccessful()){
                    completeTickerExample = response.body();
                    marketList = response.body().getCompleteTicker().getMarkets();
                    convert(completeTickerExample,value);
                }else {
                    view.callbackErrorMessage(response.message());
                }
            }
            @Override
            public void onFailure(Call<CompleteTickerExample> call, Throwable t) {

            }
        });
    }

    private void convert(CompleteTickerExample cte, String value){
        double one = Double.parseDouble(cte.getCompleteTicker().getPrice());
        double two = Double.parseDouble(value);
        double converted = two * one;
        double newDouble = new BigDecimal(converted).setScale(2, RoundingMode.UP).doubleValue();
        String convertedValue = String.valueOf(newDouble);
        long date = cte.getTimestamp();
        Date time = new Date();
        time.setTime(date * 1000);
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String formattedDate = format.format(time);
        sendDataToDB(convertedValue,value,cte,formattedDate);
        view.callbackConversionRequest(convertedValue, marketList);
    }

    private void sendDataToDB(String targetValue, String baseValue, CompleteTickerExample cte, String time){
        dataManager = new DataManager(context);
        dataManager.open();
        String targetName = cte.getCompleteTicker().getTarget();
        String targetPrice = cte.getCompleteTicker().getPrice();
        String baseName = cte.getCompleteTicker().getBase();
        String baseCode = cte.getCompleteTicker().getBase();
        String targetCode = cte.getCompleteTicker().getTarget();
        dataManager.saveNewConverterRequest(targetName,targetPrice,baseValue,baseName,baseCode,targetValue,targetCode,time);
        dataManager.close();
    }

    @Override
    public void callConversion(String base, String target, String ConversionValue) {
        value = ConversionValue;
        makeConversion(base,target);
    }
}
