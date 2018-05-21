package com.example.maxim_ozarovskiy.ajtestapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maxim_ozarovskiy.ajtestapp.R;
import com.example.maxim_ozarovskiy.ajtestapp.activity.TargetBaseCurrencyActivity;
import com.example.maxim_ozarovskiy.ajtestapp.interfaces.SimpleContract;
import com.example.maxim_ozarovskiy.ajtestapp.model.CurrencyTypes;
import com.example.maxim_ozarovskiy.ajtestapp.presenter.SimplePresenter;

public class MainFragment extends Fragment implements SimpleContract.View{

    private Button checkBaseCurrencyBtn;
    private Button checkTargetCurrencyBtn;
    private Button convertBtn;
    private EditText baseCurrencyValueEt;
    private TextView targetCurrencyValueTv;
    private TextView baseCurrencyNameTv;
    private TextView targetCurrencyNameTv;
    private TextView errorTv;
    private BottomNavigationView bottomNavigationView;

    private SimplePresenter presenter;

    private CurrencyTypes baseCurrency;
    private CurrencyTypes targetCurrency;
    private String baseCurrencyName;
    private String baseCurrencyCode;
    private String targetCurrencyName;
    private String targetCurrencyCode;

    private static int RESULT_CODE = 1;
    private static int BASE_CURRENCY_REQUEST_CODE = 1;
    private static int TARGET_CURRENCY_REQUEST_CODE = 2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.simple_fragment, container, false);
        initUI(v);
        presenter = new SimplePresenter(getActivity(),this);

        checkBaseCurrencyBtn.setOnClickListener(this::checkBaseClick);

        checkTargetCurrencyBtn.setOnClickListener(this::checkTargetClick);

        convertBtn.setOnClickListener(this::checkConvertClick);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String baseCount = baseCurrencyValueEt.getText().toString();
        String targetCount = targetCurrencyValueTv.getText().toString();
        if (baseCurrencyName != null &&
                baseCurrencyCode != null &&
                targetCurrencyName != null &&
                targetCurrencyCode != null &&
                baseCount != null &&
                targetCount != null) {
            outState.putString("count", baseCount);
            outState.putString("baseCurrencyName", baseCurrencyName);
            outState.putString("baseCurrencyCode", baseCurrencyCode);
            outState.putString("targetCurrencyValue", targetCount);
            outState.putString("targetCurrencyName", targetCurrencyName);
            outState.putString("targetCurrencyCode", targetCurrencyCode);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            String baseCount = savedInstanceState.getString("count");
            baseCurrencyValueEt.setText(baseCount);
            baseCurrencyName = savedInstanceState.getString("baseCurrencyName");
            baseCurrencyCode = savedInstanceState.getString("baseCurrencyCode");
            String targetCount = savedInstanceState.getString("targetCurrencyValue");
            targetCurrencyValueTv.setText(targetCount);
            targetCurrencyName = savedInstanceState.getString("targetCurrencyName");
            targetCurrencyCode = savedInstanceState.getString("targetCurrencyCode");
            setBaseCurrencyName();
            setTargetCurrencyName();
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.cearDisp();
    }

    private void checkBaseClick(View v){
        checkBaseCurrencyType();
    }

    private void checkTargetClick(View v){
        checkTargetCurrencyType();
    }

    private void checkConvertClick(View v){
        errorTv.setText("");
        String value = baseCurrencyValueEt.getText().toString();
        if (TextUtils.isEmpty(baseCurrencyName)) {
            Toast.makeText(getActivity(), R.string.enter_base_currency, Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(targetCurrencyName)) {
            Toast.makeText(getActivity(), R.string.enter_target_currency, Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(value)) {
            Toast.makeText(getActivity(), R.string.enter_a_base_currency_value, Toast.LENGTH_LONG).show();
        } else {
            presenter.callConversion(baseCurrencyCode, targetCurrencyCode, value);
        }
    }

    private void initUI(View v) {
        checkBaseCurrencyBtn = v.findViewById(R.id.check_base_currency_simple_ticker_activity);
        checkTargetCurrencyBtn = v.findViewById(R.id.check_target_currency_simple_ticker_activity);
        convertBtn = v.findViewById(R.id.convert_btn_simple_ticker_activity);
        baseCurrencyNameTv = v.findViewById(R.id.base_currency_name_tv_simple_ticker_activity);
        baseCurrencyValueEt = v.findViewById(R.id.base_currency_value_et_simple_ticker_activity);
        targetCurrencyNameTv = v.findViewById(R.id.target_currency_name_tv_simple_ticker_activity);
        targetCurrencyValueTv = v.findViewById(R.id.target_currency_value_tv_simple_ticker_activity);
        errorTv = v.findViewById(R.id.error_message_simple_ticker_activity);
    }

    private void setBaseCurrencyName() {
        baseCurrencyNameTv.setText(baseCurrencyName);
    }

    private void setTargetCurrencyName() {
        targetCurrencyNameTv.setText(targetCurrencyName);
    }

    private void checkBaseCurrencyType() {
        targetCurrencyValueTv.setText(R.string.some_currency_value);
        Intent intent = new Intent(getActivity(), TargetBaseCurrencyActivity.class);
        intent.putExtra("Intent", RESULT_CODE);
        startActivityForResult(intent, BASE_CURRENCY_REQUEST_CODE);
    }

    private void checkTargetCurrencyType() {
        targetCurrencyValueTv.setText(R.string.some_currency_value);
        Intent intent = new Intent(getActivity(), TargetBaseCurrencyActivity.class);
        intent.putExtra("Intent", RESULT_CODE);
        startActivityForResult(intent, TARGET_CURRENCY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == BASE_CURRENCY_REQUEST_CODE) {
            baseCurrency = data.getParcelableExtra("CurrencyTypes");
            baseCurrencyName = baseCurrency.getName();
            baseCurrencyCode = baseCurrency.getCode();
            setBaseCurrencyName();

        } else if (requestCode == TARGET_CURRENCY_REQUEST_CODE) {
            targetCurrency = data.getParcelableExtra("CurrencyTypes");
            targetCurrencyName = targetCurrency.getName();
            targetCurrencyCode = targetCurrency.getCode();
            setTargetCurrencyName();
        }
    }

    private void setConvertedValue(String value) {
        targetCurrencyValueTv.setText(value);
    }

    @Override
    public void callbackErrorMessage(String error) {
        errorTv.setText(error);
    }

    @Override
    public void callbackConversion(String targetValue) {
        setConvertedValue(targetValue);
    }

}
