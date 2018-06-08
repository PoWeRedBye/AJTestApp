package com.example.maxim_ozarovskiy.ajtestapp.presenter;

import com.example.maxim_ozarovskiy.ajtestapp.interfaces.TargetBaseCurrencyActivityContract;
import com.example.maxim_ozarovskiy.ajtestapp.model.CurrencyTypes;
import com.example.maxim_ozarovskiy.ajtestapp.model.CurrencyTypesExample;
import com.example.maxim_ozarovskiy.ajtestapp.network.RESTClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Maxim_Ozarovskiy on 12.12.2017.
 */

public class TargetBaseCurrencyActivityPresenter implements TargetBaseCurrencyActivityContract.Presenter {

    TargetBaseCurrencyActivityContract.View view;

    private String noInet = "No internet connection";
    private String searchString;
    private List<CurrencyTypes> currencyList;
    private List<CurrencyTypes> newCurrencyList;
    private List<CurrencyTypes> currencyTypesExample;
    private CurrencyTypesExample currencyData;
    private CompositeDisposable mCompositeDisposable;

    public TargetBaseCurrencyActivityPresenter(TargetBaseCurrencyActivityContract.View view) {
        this.view = view;
    }

    private void getDataList(CurrencyTypesExample dataList) {
        if (dataList.getCurrencyTypes() == null) {
            view.callbackHttpError(noInet);
        } else {
            currencyTypesExample = new ArrayList<>();
            currencyTypesExample.addAll(dataList.getCurrencyTypes());
            view.callbackCurrency(currencyTypesExample);
        }
    }

    public Disposable addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
        return disposable;
    }

    private void getCurrencyList() {
        addDisposable(RESTClient.getInstance()
                .getCurrencyTypesService()
                .currencyTypesService()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getDataList));
    }

    private void clearDisposable() {
        if (mCompositeDisposable.size() >= 1) {
            mCompositeDisposable.clear();
        }
    }

    private void searchByCurrencyName(String input) {
        List<CurrencyTypes> newCurrencyList = new ArrayList<>();
        if (input == null) {
            view.callbackInputCurrency(currencyTypesExample);
        } else {
            for (int i = 1; i <= currencyTypesExample.size() - 1; i++) {
                if (currencyTypesExample.get(i).getCode().toLowerCase().contains(input)
                        || currencyTypesExample.get(i).getName().toLowerCase().contains(input)) {
                    for (int j = newCurrencyList.size(); j >= 0; j++) {
                        newCurrencyList.add(j, currencyTypesExample.get(i));
                        break;
                    }
                }
            }
            view.callbackInputCurrency(newCurrencyList);
        }
    }

    @Override
    public void searchInputCurrency(List<CurrencyTypes> currency, String input) {
        currencyList = currency;
        searchString = input;
        searchByCurrencyName(input.toLowerCase());
    }

    @Override
    public void getCurrency() {
        getCurrencyList();
    }

    @Override
    public void clearDisp() {
        clearDisposable();
    }

}
