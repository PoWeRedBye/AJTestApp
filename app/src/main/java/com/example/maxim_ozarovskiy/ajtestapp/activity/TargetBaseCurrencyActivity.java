package com.example.maxim_ozarovskiy.ajtestapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.maxim_ozarovskiy.ajtestapp.R;
import com.example.maxim_ozarovskiy.ajtestapp.adapter.CurrencyTypesAdapter;
import com.example.maxim_ozarovskiy.ajtestapp.interfaces.TargetBaseCurrencyActivityContract;
import com.example.maxim_ozarovskiy.ajtestapp.model.CurrencyTypes;
import com.example.maxim_ozarovskiy.ajtestapp.presenter.TargetBaseCurrencyActivityPresenter;

import java.util.List;

/**
 * Created by Maxim_Ozarovskiy on 12.12.2017.
 */

public class TargetBaseCurrencyActivity extends AppCompatActivity implements TargetBaseCurrencyActivityContract.View,
        CurrencyTypesAdapter.CurrencyItemClickListener<CurrencyTypes> {

    private EditText searchCurrencyEt;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorMsg;
    private LinearLayoutManager mLayoutManager;
    private CurrencyTypesAdapter newCurrencyTypesAdapter;
    private List<CurrencyTypes> currencyList;
    private String searchInputText;
    private Button refresh;

    private static int SIMPLE_REQUEST = 1;
    private static int SIMPLE_RESULT = 1;
    private static int COMPLETE_REQUEST = 2;
    private static int COMPLETE_RESULT = 2;


    private TargetBaseCurrencyActivityPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.target_base_currency_activity);
        initUI();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        presenter = new TargetBaseCurrencyActivityPresenter(this);
        presenter.getCurrency();
        refresh.setOnClickListener(this::refreshClick);

    }

    private void refreshClick(View v){
        presenter.getCurrency();
        invisible();
    }

    private void visible(){
        recyclerView.setVisibility(android.view.View.VISIBLE);
        progressBar.setVisibility(android.view.View.INVISIBLE);
        refresh.setVisibility(android.view.View.INVISIBLE);
        errorMsg.setVisibility(android.view.View.INVISIBLE);
        progressBar.clearAnimation();
    }
    private void invisible(){
        recyclerView.setVisibility(android.view.View.INVISIBLE);
        refresh.setVisibility(android.view.View.INVISIBLE);
        errorMsg.setVisibility(android.view.View.INVISIBLE);
        progressBar.setVisibility(android.view.View.VISIBLE);
        progressBar.animate();
    }
    private void refreshBtnVisible(){
        recyclerView.setVisibility(android.view.View.INVISIBLE);
        progressBar.setVisibility(android.view.View.INVISIBLE);
        progressBar.clearAnimation();
        refresh.setVisibility(android.view.View.VISIBLE);
        errorMsg.setVisibility(android.view.View.VISIBLE);
    }


    private void inputListener() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchInputText = searchCurrencyEt.getText().toString();
                presenter.searchInputCurrency(currencyList,searchInputText);
            }
        };
        searchCurrencyEt.addTextChangedListener(textWatcher);
    }

    private void setNewRecyclerAdapter(List<CurrencyTypes> newList){
        newCurrencyTypesAdapter = new CurrencyTypesAdapter(this, newList,
                this);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(newCurrencyTypesAdapter);
        newCurrencyTypesAdapter.notifyDataSetChanged();
    }

    private void initUI(){
        refresh = findViewById(R.id.refresh_btn_target_base_activity);
        searchCurrencyEt = findViewById(R.id.search_current_currency_base_target_activity);
        recyclerView = findViewById(R.id.base_target_currency_recycer_view);
        progressBar = findViewById(R.id.progress_bar_target_base_activity);
        errorMsg = findViewById(R.id.error_msg_target_base_activity);
        invisible();
    }


    @Override
    public void callbackCurrency(List<CurrencyTypes> currency) {
        visible();
        currencyList = currency;
        setNewRecyclerAdapter(currency);
        inputListener();
    }

    @Override
    public void callbackInputCurrency(List<CurrencyTypes> currency) {
        setNewRecyclerAdapter(currency);
    }

    @Override
    public void callbackHttpError(String error) {
        refreshBtnVisible();
        errorMsg.setText(error);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.clearDisp();
    }

    @Override
    public void currencyItemClick(CurrencyTypes currencyTypes, int position) {
        Bundle request = getIntent().getExtras();
        int requestNumber = request.getInt("Intent");
        if(requestNumber == SIMPLE_REQUEST) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("CurrencyTypes", currencyTypes);
            setResult(SIMPLE_RESULT, intent);
            finish();
        } else if (requestNumber == COMPLETE_REQUEST){
            Intent intent = new Intent(this, CompleteTickerActivity.class);
            intent.putExtra("CurrencyTypes", currencyTypes);
            setResult(COMPLETE_RESULT,intent);
            finish();
        }
    }

}

