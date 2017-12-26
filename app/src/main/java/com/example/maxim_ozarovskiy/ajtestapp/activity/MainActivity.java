package com.example.maxim_ozarovskiy.ajtestapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maxim_ozarovskiy.ajtestapp.R;
import com.example.maxim_ozarovskiy.ajtestapp.interfaces.MainActivityContract;
import com.example.maxim_ozarovskiy.ajtestapp.model.CurrencyTypes;
import com.example.maxim_ozarovskiy.ajtestapp.presenter.MainActivityPresenter;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    private Button checkBaseCurrencyBtn;
    private Button checkTargetCurrencyBtn;
    private Button convertBtn;
    private EditText baseCurrencyValueEt;
    private TextView targetCurrencyValueTv;
    private TextView baseCurrencyNameTv;
    private TextView targetCurrencyNameTv;
    private TextView errorTv;
    private BottomNavigationView bottomNavigationView;

    private MainActivityPresenter presenter;

    private CurrencyTypes baseCurrency;
    private CurrencyTypes targetCurency;
    private String baseCurrencyName;
    private String baseCurrencyCode;
    private String targetCurrencyName;
    private String targetCurrencyCode;

    private static int RESULT_CODE = 1;
    private static int BASE_CURRENCY_REQUEST_CODE = 1;
    private static int TARGET_CURRENCY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        presenter = new MainActivityPresenter(this, this);

        checkBaseCurrencyBtn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                checkBaseCurrencyType();
            }
        });

        checkTargetCurrencyBtn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                checkTargetCurrencyType();
            }
        });

        convertBtn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                errorTv.setText("");
                String value = baseCurrencyValueEt.getText().toString();
                if (TextUtils.isEmpty(baseCurrencyName)) {
                    Toast.makeText(getApplicationContext(), R.string.enter_base_currency, Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(targetCurrencyName)) {
                    Toast.makeText(getApplicationContext(), R.string.enter_target_currency, Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(value)) {
                    Toast.makeText(getApplicationContext(), R.string.enter_a_base_currency_value, Toast.LENGTH_LONG).show();
                } else {
                    presenter.callConversion(baseCurrencyCode, targetCurrencyCode, value);
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.main_activity:

                                break;
                            case R.id.history_activity:
                                startHistoryActivity();
                                break;
                            case R.id.compete_activity:
                                startCompleteTickerActivity();
                                break;
                        }
                        return false;
                    }
                });

    }

    protected void onSaveInstanceState(Bundle outState) {
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

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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

    private void initUI() {
        setTitle(R.string.simple_activity);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.main_activity);
        checkBaseCurrencyBtn = findViewById(R.id.check_base_currency_simple_ticker_activity);
        checkTargetCurrencyBtn = findViewById(R.id.check_target_currency_simple_ticker_activity);
        convertBtn = findViewById(R.id.convert_btn_simple_ticker_activity);
        baseCurrencyNameTv = findViewById(R.id.base_currency_name_tv_simple_ticker_activity);
        baseCurrencyValueEt = findViewById(R.id.base_currency_value_et_simple_ticker_activity);
        targetCurrencyNameTv = findViewById(R.id.target_currency_name_tv_simple_ticker_activity);
        targetCurrencyValueTv = findViewById(R.id.target_currency_value_tv_simple_ticker_activity);
        errorTv = findViewById(R.id.error_message_simple_ticker_activity);
    }

    private void setBaseCurrencyName() {
        baseCurrencyNameTv.setText(baseCurrencyName);
    }

    private void setTargetCurrencyName() {
        targetCurrencyNameTv.setText(targetCurrencyName);
    }

    private void startHistoryActivity() {
        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
        startActivity(intent);
    }

    private void startCompleteTickerActivity() {
        Intent intent = new Intent(getApplicationContext(), CompleteTickerActivity.class);
        startActivity(intent);
    }

    private void checkBaseCurrencyType() {
        targetCurrencyValueTv.setText(R.string.some_currency_value);
        Intent intent = new Intent(this, TargetBaseCurrencyActivity.class);
        intent.putExtra("Intent", RESULT_CODE);
        startActivityForResult(intent, BASE_CURRENCY_REQUEST_CODE);
    }

    private void checkTargetCurrencyType() {
        targetCurrencyValueTv.setText(R.string.some_currency_value);
        Intent intent = new Intent(this, TargetBaseCurrencyActivity.class);
        intent.putExtra("Intent", RESULT_CODE);
        startActivityForResult(intent, TARGET_CURRENCY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            targetCurency = data.getParcelableExtra("CurrencyTypes");
            targetCurrencyName = targetCurency.getName();
            targetCurrencyCode = targetCurency.getCode();
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
