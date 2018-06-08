package com.example.maxim_ozarovskiy.ajtestapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maxim_ozarovskiy.ajtestapp.R;
import com.example.maxim_ozarovskiy.ajtestapp.model.DbModelEx;

import java.util.List;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public class HistoryActivityAdapter extends RecyclerView.Adapter<HistoryActivityAdapter.ViewHolder> {

    private Context context;
    private List<DbModelEx> historyList;

    public HistoryActivityAdapter(Context context, List<DbModelEx> list){
        this.context = context;
        this.historyList = list;
    }

    @Override
    public HistoryActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_activity_item, null);
        HistoryActivityAdapter.ViewHolder holder = new HistoryActivityAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoryActivityAdapter.ViewHolder holder, int position) {
        final int pos = holder.getAdapterPosition();

        holder.historyTime.setText(historyList.get(pos).getDayTimeRequest());
        holder.baseCurrencyName.setText(historyList.get(pos).getBaseCurrencyName());
        holder.targetCurrencyName.setText(historyList.get(pos).getTargetCurrencyName());
        holder.baseCurrencyValue.setText(historyList.get(pos).getBaseCurrencyValue());
        holder.targetCurrencyValue.setText(historyList.get(pos).getTargetCurrencyValue());
        holder.targetCurrencyPrice.setText(historyList.get(pos).getTargetCurrencyCourse());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView historyTime;
        TextView baseCurrencyName;
        TextView targetCurrencyName;
        TextView baseCurrencyValue;
        TextView targetCurrencyValue;
        TextView targetCurrencyPrice;


        public ViewHolder(View v) {
            super(v);

            cardView = v.findViewById(R.id.history_card_item);
            historyTime = v.findViewById(R.id.history_time_item);
            baseCurrencyName = v.findViewById(R.id.base_history_item_currency_name);
            targetCurrencyName = v.findViewById(R.id.target_history_item_currency_name);
            baseCurrencyValue = v.findViewById(R.id.base_history_item_currency_value);
            targetCurrencyValue = v.findViewById(R.id.target_history_item_currency_value);
            targetCurrencyPrice = v.findViewById(R.id.history_item_target_currency_price);

        }
    }
}
