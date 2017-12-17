package com.example.maxim_ozarovskiy.ajtestapp.presenter;

import com.example.maxim_ozarovskiy.ajtestapp.interfaces.TargetBaseCurrencyActivityContract;
import com.example.maxim_ozarovskiy.ajtestapp.model.CurrencyTypes;
import com.example.maxim_ozarovskiy.ajtestapp.model.CurrencyTypesExample;
import com.example.maxim_ozarovskiy.ajtestapp.network.RESTClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Maxim_Ozarovskiy on 12.12.2017.
 */

public class TargetBaseCurrencyActivityPresenter implements TargetBaseCurrencyActivityContract.Presenter {

    TargetBaseCurrencyActivityContract.View view;

    private String searchString;
    private List<CurrencyTypes> currencyList;
    private List<CurrencyTypes> newCurrencyList;
    private List<CurrencyTypes> currencyTypesExample;



    public TargetBaseCurrencyActivityPresenter(TargetBaseCurrencyActivityContract.View view){
        this.view = view;
    }

    private void getCurrencyTypes() {
        RESTClient.getInstance().getCurrencyTypesService().currencyTypesService().enqueue(new Callback<CurrencyTypesExample>() {
            @Override
            public void onResponse(Call<CurrencyTypesExample> call, Response<CurrencyTypesExample> response) {
                if (response.isSuccessful()){
                    currencyTypesExample = new ArrayList<>();
                    currencyTypesExample.addAll(response.body().getCurrencyTypes());
                    view.callbackCurrency(currencyTypesExample);
                } else {
                    view.callbackHttpError(response.message());
                }
            }
            @Override
            public void onFailure(Call<CurrencyTypesExample> call, Throwable t) {

            }
        });
    }

    private void searchByCurrencyName(){
        List<CurrencyTypes> newCurrencyList = new ArrayList<>();
        if (searchString == null) {
            view.callbackInputCurrency(currencyTypesExample);
        } else {
            for (int i = 1; i <= currencyTypesExample.size() - 1; i++) {
                if (currencyTypesExample.get(i).getCode().contains(searchString)
                        || currencyTypesExample.get(i).getName().contains(searchString)) {
                    for (int j = newCurrencyList.size(); j>=0; j++){
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
        searchByCurrencyName();
    }

    @Override
    public void getCurrency() {
        getCurrencyTypes();
    }

}
