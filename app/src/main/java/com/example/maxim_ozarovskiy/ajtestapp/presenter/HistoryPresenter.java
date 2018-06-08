package com.example.maxim_ozarovskiy.ajtestapp.presenter;

import android.content.Context;

import com.example.maxim_ozarovskiy.ajtestapp.data.AppDatabase;
import com.example.maxim_ozarovskiy.ajtestapp.data.DataManager;
import com.example.maxim_ozarovskiy.ajtestapp.data.Database;
import com.example.maxim_ozarovskiy.ajtestapp.data.dao.DbModelExDao;
import com.example.maxim_ozarovskiy.ajtestapp.interfaces.HistoryContract;
import com.example.maxim_ozarovskiy.ajtestapp.model.DbModelEx;
import com.example.maxim_ozarovskiy.ajtestapp.model.SimpleTickerExample;
import com.example.maxim_ozarovskiy.ajtestapp.network.RESTClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class HistoryPresenter implements HistoryContract.Presenter {

    HistoryContract.View view;
    private DataManager dataManager;
    private Context context;
    private List<DbModelEx> dbList;
    private SimpleTickerExample simpleTickerExample;
    private CompositeDisposable mCompositeDisposable;
    private AppDatabase database;
    private DbModelExDao dao;

    private String noInet = "No internet connection";
    private String usd = "usd";
    private String euro = "eur";
    private String value = "1";

    public HistoryPresenter(Context ctx, HistoryContract.View view) {
        this.context = ctx;
        this.view = view;
    }

    private void getRecords() {
        database = Database.getInstance().getDatabase();
        dao = database.dbModelExDao();
        addDisposable(dao.getAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getDbData));

    }

    private void getDbData(List<DbModelEx> model) {
        dbList = new ArrayList<>();
        dbList = model;
        if (dbList.size() == 0) {
            makeConversion(euro, usd);
        }
        setRecords();
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
                .getSimpleTickerService()
                .simpleTickerService(base + "-" + target)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getData));
    }

    private void getData(SimpleTickerExample simple) {
        if (simple.getSimpleTicker() == null) {
            view.callbackErrorMsg(simple.getError());
        } else {
            simpleTickerExample = simple;
            convert(simpleTickerExample, value);
        }
    }

    private void setRecords() {
        view.callbackDbRecords(dbList);
    }

    private void convert(SimpleTickerExample ste, String convertValue) {
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
        sendDataToDB(convertedValue, convertValue, simpleTickerExample, formattedDate);

    }

    private void sendDataToDB(String targetValue, String baseValue, SimpleTickerExample ste, String time) {
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

        Completable.fromAction(() -> dao.insert(db))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        getRecords();
    }

    private void clearDisposable() {
        if (mCompositeDisposable != null) {
            if (mCompositeDisposable.size() >= 1) {
                mCompositeDisposable.clear();
            }
        }
    }

    @Override
    public void callDbRecords() {
        getRecords();
    }

    @Override
    public void clearDisp() {
        clearDisposable();
    }
}
