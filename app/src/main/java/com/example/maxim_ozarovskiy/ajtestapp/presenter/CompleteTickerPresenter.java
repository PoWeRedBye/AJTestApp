package com.example.maxim_ozarovskiy.ajtestapp.presenter;

import android.content.Context;

import com.example.maxim_ozarovskiy.ajtestapp.data.AppDatabase;
import com.example.maxim_ozarovskiy.ajtestapp.data.DataManager;
import com.example.maxim_ozarovskiy.ajtestapp.data.Database;
import com.example.maxim_ozarovskiy.ajtestapp.data.dao.DbModelExDao;
import com.example.maxim_ozarovskiy.ajtestapp.interfaces.CompleteTickerContract;
import com.example.maxim_ozarovskiy.ajtestapp.model.CompleteTickerExample;
import com.example.maxim_ozarovskiy.ajtestapp.model.DbModelEx;
import com.example.maxim_ozarovskiy.ajtestapp.model.Market;
import com.example.maxim_ozarovskiy.ajtestapp.network.RESTClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public class CompleteTickerPresenter implements CompleteTickerContract.Presenter {

    CompleteTickerContract.View view;

    private String noInet = "No internet connection";
    private String value;
    private CompleteTickerExample completeTickerExample;
    private List<Market> marketList;
    private Context context;
    private DataManager dataManager;
    private CompositeDisposable mCompositeDisposable;
    private AppDatabase database;
    private DbModelExDao dao;

    public CompleteTickerPresenter(Context ctx, CompleteTickerContract.View view) {
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

    private void makeConversion(String base, String target) {
        addDisposable(RESTClient.getInstance()
                .getCompleteTickerService()
                .completeTickerService(base + "-" + target)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getData));
    }

    private void getData(CompleteTickerExample complete) {
        if (complete.getCompleteTicker() == null) {
            view.callbackErrorMessage(complete.getError());
        } else {
            completeTickerExample = complete;
            convert(completeTickerExample, value);
        }
    }

    private void convert(CompleteTickerExample cte, String value) {
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
        marketList = cte.getCompleteTicker().getMarkets();
        sendDataToDB(convertedValue, value, cte, formattedDate);
        view.callbackConversionRequest(convertedValue, marketList);
    }

    private void sendDataToDB(String targetValue, String baseValue, CompleteTickerExample cte, String time) {
        database = Database.getInstance().getDatabase();
        dao = database.dbModelExDao();
        String targetName = cte.getCompleteTicker().getTarget();
        String targetPrice = cte.getCompleteTicker().getPrice();
        String baseName = cte.getCompleteTicker().getBase();
        String baseCode = cte.getCompleteTicker().getBase();
        String targetCode = cte.getCompleteTicker().getTarget();

        DbModelEx db = new DbModelEx();
        db.setBaseCurrencyCode(baseCode);
        db.setBaseCurrencyName(baseName);
        db.setBaseCurrencyValue(baseValue);
        db.setDayTimeRequest(time);
        db.setTargetCurrencyCode(targetCode);
        db.setTargetCurrencyName(targetName);
        db.setTargetCurrencyValue(targetValue);
        db.setTargetCurrencyCourse(targetPrice);

        Completable.fromAction(() -> dao.insert(db))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void callConversion(String base, String target, String conversionValue) {

        value = conversionValue;
        makeConversion(base, target);
    }

    private void clearDisposable() {
        if (mCompositeDisposable != null) {
            if (mCompositeDisposable.size() >= 1) {
                mCompositeDisposable.clear();
            }
        }
    }

    @Override
    public void clearDisp() {
        clearDisposable();
    }
}
