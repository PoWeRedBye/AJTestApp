package com.example.maxim_ozarovskiy.ajtestapp.presenter;


import android.content.Context;

import com.example.maxim_ozarovskiy.ajtestapp.data.AppDatabase;
import com.example.maxim_ozarovskiy.ajtestapp.data.DataManager;
import com.example.maxim_ozarovskiy.ajtestapp.data.Database;
import com.example.maxim_ozarovskiy.ajtestapp.data.dao.DbModelExDao;
import com.example.maxim_ozarovskiy.ajtestapp.interfaces.SimpleContract;
import com.example.maxim_ozarovskiy.ajtestapp.model.DbModelEx;
import com.example.maxim_ozarovskiy.ajtestapp.model.SimpleTickerExample;
import com.example.maxim_ozarovskiy.ajtestapp.network.RESTClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public class SimplePresenter implements SimpleContract.Presenter {

    SimpleContract.View view;

    private String noInet = "No internet connection";
    private String value;
    private SimpleTickerExample simpleTickerExample;
    private Context context;
    private DataManager dataManager;
    private CompositeDisposable mCompositeDisposable;
    private AppDatabase database;
    private DbModelExDao dao;

    public SimplePresenter(Context ctx, SimpleContract.View view) {
        this.context = ctx;
        this.view = view;
    }

    public Disposable addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
        return disposable;
    }

    private void getServerRequest(String base, String target) {
        addDisposable(RESTClient.getInstance()
                .getSimpleTickerService()
                .simpleTickerService(base + "-" + target)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getData));
    }

    private void getData(SimpleTickerExample simple) {
        if (simple.getSimpleTicker() == null){
            view.callbackErrorMessage(simple.getError());
        } else {
            simpleTickerExample = simple;
            convert(simpleTickerExample, value);
        }
    }

    private void convert(SimpleTickerExample ste, String value) {
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
        sendDataToDB(convertedValue, value, simpleTickerExample, formattedDate);
        view.callbackConversion(convertedValue);
    }

    private void sendDataToDB(String targetValue, String baseValue, SimpleTickerExample ste, String time) {
        database = Database.getInstance().getDatabase();
        dao = database.dbModelExDao();
        String targetName = ste.getSimpleTicker().getTarget();
        String targetPrice = ste.getSimpleTicker().getPrice();
        String baseName = ste.getSimpleTicker().getBase();
        String baseCode = ste.getSimpleTicker().getBase();
        String targetCode = ste.getSimpleTicker().getTarget();
        DbModelEx db = new DbModelEx();
        db.setBaseCurrencyCode(baseCode);
        db.setBaseCurrencyName(baseName);
        db.setBaseCurrencyValue(baseValue);
        db.setDayTimeRequest(time);
        db.setTargetCurrencyCode(targetCode);
        db.setTargetCurrencyName(targetName);
        db.setTargetCurrencyValue(targetValue);
        db.setTargetCurrencyCourse(targetPrice);

        Completable.fromAction(()-> dao.insert(db))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();



    }

    @Override
    public void callConversion(String base, String target, String conversionValue) {
        value = conversionValue;
        getServerRequest(base, target);
    }

    private void clearDisposable() {
        if (mCompositeDisposable != null) {
            if (mCompositeDisposable.size() >= 1) {
                mCompositeDisposable.clear();
            }
        }
    }

    @Override
    public void cearDisp() {
        clearDisposable();
    }
}
