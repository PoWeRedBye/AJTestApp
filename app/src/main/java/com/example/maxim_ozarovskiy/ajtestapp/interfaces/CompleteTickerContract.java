package com.example.maxim_ozarovskiy.ajtestapp.interfaces;

import com.example.maxim_ozarovskiy.ajtestapp.model.Market;

import java.util.List;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public interface CompleteTickerContract {

    interface View {
        void callbackConversionRequest(String targetValue, List<Market> marketList);
        void callbackErrorMessage(String error);
    }

    interface Presenter {
        void callConversion(String base, String target, String convertationValue);
        void clearDisp();
    }
}
