package com.example.maxim_ozarovskiy.ajtestapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maxim_ozarovskiy.ajtestapp.R;
import com.example.maxim_ozarovskiy.ajtestapp.adapter.CompleteTickerAdapter;
import com.example.maxim_ozarovskiy.ajtestapp.interfaces.CompleteTickerActivityContract;
import com.example.maxim_ozarovskiy.ajtestapp.model.CurrencyTypes;
import com.example.maxim_ozarovskiy.ajtestapp.model.Market;
import com.example.maxim_ozarovskiy.ajtestapp.presenter.CompleteTickerActivityPresenter;

import java.util.List;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public class CompleteTickerActivity extends AppCompatActivity implements CompleteTickerActivityContract.View {

    private Button checkBaseCurrencyBtn;
    private Button checkTargetCurrencyBtn;
    private Button convertBtn;
    private EditText baseCurrencyValueEt;
    private TextView targetCurrencyValueTv;
    private TextView baseCurrencyNameTv;
    private TextView targetCurrencyNameTv;
    private TextView errorTv;
    private TextView marketsError;
    private BottomNavigationView bottomNavigationView;

    private CompleteTickerActivityPresenter presenter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private CompleteTickerAdapter marketAdapter;

    private CurrencyTypes baseCurrency;
    private CurrencyTypes targetCurency;
    private String baseCurrencyName;
    private String baseCurrencyCode;
    private String targetCurrencyName;
    private String targetCurrencyCode;

    private static int RESULT_CODE = 2;
    private static int BASE_CURRENCY_REQUEST_CODE = 3;
    private static int TARGET_CURRENCY_REQUEST_CODE = 4;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_ticker_activity);
        initUI();

        presenter = new CompleteTickerActivityPresenter(this, this);

        checkBaseCurrencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBaseCurrencyType();
            }
        });

        checkTargetCurrencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkTargetCurrencyType();
            }
        });

        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorTv.setText("");
                marketsError.setText("");
                String value = baseCurrencyValueEt.getText().toString();
                if(TextUtils.isEmpty(baseCurrencyName)){
                    Toast.makeText(getApplicationContext(),R.string.enter_base_currency, Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(targetCurrencyName)){
                    Toast.makeText(getApplicationContext(),R.string.enter_target_currency, Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(value)) {
                    Toast.makeText(getApplicationContext(),R.string.enter_a_base_currency_value, Toast.LENGTH_LONG).show();
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
                                startSimpleTickerActivity();
                                break;
                            case R.id.history_activity:
                                startHistoryActivity();
                                break;
                            case R.id.compete_activity:
                                break;
                        }
                        return false;
                    }
                });
    }

    private void showRecyclerView(){
        recyclerView.setVisibility(View.VISIBLE);
        marketsError.setVisibility(View.INVISIBLE);
    }
    private void hideRecyclerShowError(){
        recyclerView.setVisibility(View.INVISIBLE);
        marketsError.setVisibility(View.VISIBLE);
    }

   private void initUI(){
       setTitle(R.string.complete_ticker_menu);
       bottomNavigationView = findViewById(R.id.bottom_navigation);
       bottomNavigationView.setSelectedItemId(R.id.compete_activity);
       checkBaseCurrencyBtn = findViewById(R.id.check_base_currency_complete_ticker_activity);
       checkTargetCurrencyBtn = findViewById(R.id.check_target_currency_complete_ticker_activity);
       convertBtn = findViewById(R.id.convert_btn_complete_ticker_activity);
       baseCurrencyValueEt = findViewById(R.id.base_currency_value_et_complete_ticker_activity);
       targetCurrencyValueTv = findViewById(R.id.target_currency_value_tv_complete_ticker_activity);
       baseCurrencyNameTv = findViewById(R.id.base_currency_name_tv_complete_ticker_activity);
       targetCurrencyNameTv = findViewById(R.id.target_currency_name_tv_complete_ticker_activity);
       errorTv = findViewById(R.id.error_message_complete_ticker_activity);
       recyclerView = findViewById(R.id.recycler_view_complete_ticker_activity);
       marketsError = findViewById(R.id.markets_error);
   }

    private void setNewRecyclerAdapter(List<Market> newList){
        marketAdapter = new CompleteTickerAdapter(this, newList);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(marketAdapter);
        marketAdapter.notifyDataSetChanged();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String baseCount = baseCurrencyValueEt.getText().toString();
        String targetCount = targetCurrencyValueTv.getText().toString();
        String errorMsg = errorTv.getText().toString();
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
            outState.putString("errorMsg", errorMsg);
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
        marketsError.setText(R.string.stores_not_available);
        String error = savedInstanceState.getString("errorMsg");
        errorTv.setText(error);
        setBaseCurrencyName();
        setTargetCurrencyName();
    }

    private void startHistoryActivity() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
        finish();
    }

    private void startSimpleTickerActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setBaseCurrencyName() {
        baseCurrencyNameTv.setText(baseCurrencyName);
    }

    private void setTargetCurrencyName() {
        targetCurrencyNameTv.setText(targetCurrencyName);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public void callbackConversionRequest(String targetValue, List<Market> marketList) {
        if(marketList.size() == 0){
            hideRecyclerShowError();
            marketsError.setText(R.string.no_stores_found);
        }else{
            showRecyclerView();
            setNewRecyclerAdapter(marketList);
        }
        setConvertedValue(targetValue);
    }

    @Override
    public void callbackErrorMessage(String error) {
        errorTv.setText(error);
    }
}
