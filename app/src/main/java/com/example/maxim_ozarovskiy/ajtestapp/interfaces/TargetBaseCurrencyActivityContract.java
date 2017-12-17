package com.example.maxim_ozarovskiy.ajtestapp.interfaces;

import com.example.maxim_ozarovskiy.ajtestapp.model.CurrencyTypes;

import java.util.List;

/**
 * Created by Maxim_Ozarovskiy on 12.12.2017.
 */

public interface TargetBaseCurrencyActivityContract {

    interface View {
        void callbackCurrency(List<CurrencyTypes> currency);
        void callbackInputCurrency(List<CurrencyTypes> currency);
        void callbackHttpError(String error);
    }

    interface Presenter {
        void searchInputCurrency(List<CurrencyTypes> currency, String input);
        void getCurrency();

    }
}
