package com.example.maxim_ozarovskiy.ajtestapp.presenter;

import android.content.Context;

import com.example.maxim_ozarovskiy.ajtestapp.R;
import com.example.maxim_ozarovskiy.ajtestapp.data.DataManager;
import com.example.maxim_ozarovskiy.ajtestapp.interfaces.HistoryActivityContract;
import com.example.maxim_ozarovskiy.ajtestapp.model.DbModelEx;
import com.example.maxim_ozarovskiy.ajtestapp.model.SimpleTickerExample;
import com.example.maxim_ozarovskiy.ajtestapp.network.RESTClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public class HistoryActivityPresenter implements HistoryActivityContract.Presenter{

    HistoryActivityContract.View view;
    private DataManager dataManager;
    private Context context;
    private List<DbModelEx> dbList;
    private SimpleTickerExample simpleTickerExample;

    private String noInet = "No internet connection";
    private String usd = "usd";
    private String bitcoin = "btc";
    private String euro = "eur";

    public HistoryActivityPresenter(Context ctx,HistoryActivityContract.View view) {
        this.context = ctx;
        this.view = view;
    }

    private void getRecords(){
        dataManager = new DataManager(context);
        dataManager.open();
        dbList = new ArrayList<>();
        dbList = dataManager.getHistory();
        setPreviewRecords();
    }

    private void setPreviewRecords(){
        if (dbList.size() == 0){
            makeConversion(euro,usd);
        } else if(dbList.size() == 1) {
            makeConversion(bitcoin, usd);
        } else if(dbList.size() == 2){
            makeConversion(bitcoin, euro);
        } else if(dbList.size() >= 3){
            view.callbackDbRecords(dbList);
            dataManager.close();
        }
    }

    private void makeConversion(String base, String target){
        RESTClient.getInstance().getSimpleTickerService().simpleTickerService(base +"-" + target).enqueue(new Callback<SimpleTickerExample>() {
            @Override
            public void onResponse(Call<SimpleTickerExample> call, Response<SimpleTickerExample> response) {
                if(response.isSuccessful()){
                    simpleTickerExample = response.body();
                    convert(simpleTickerExample, "1");
                }else {
                    view.callbackErrorMsg(response.message());
                }
            }
            @Override
            public void onFailure(Call<SimpleTickerExample> call, Throwable t) {
                view.callbackErrorMsg(noInet);
            }
        });
    }

    private void convert(SimpleTickerExample ste, String convertValue){
        double one = Double.parseDouble(ste.getSimpleTicker().getPrice());
        double two = Double.parseDouble(convertValue);
        double converted = two * one;
        double newDouble = new BigDecimal(converted).setScale(2, RoundingMode.UP).doubleValue();
        String convertedValue = String.valueOf(newDouble);
        long date = ste.getTimestamp();
        Date time = new Date();
        time.setTime(date * 1000);
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String formattedDate = format.format(time);
        sendDataToDB(convertedValue,convertValue,simpleTickerExample,formattedDate);

    }

    private void sendDataToDB(String targetValue, String baseValue, SimpleTickerExample ste, String time) {
        String targetName = ste.getSimpleTicker().getTarget();
        String targetPrice = ste.getSimpleTicker().getPrice();
        String baseName = ste.getSimpleTicker().getBase();
        String baseCode = ste.getSimpleTicker().getBase();
        String targetCode = ste.getSimpleTicker().getTarget();
        dataManager.saveNewConverterRequest(targetName,targetPrice,baseValue,baseName,baseCode,targetValue,targetCode,time);
        getRecords();
    }

    @Override
    public void callDbRecords() {
        getRecords();
    }
}
