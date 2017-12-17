package com.example.maxim_ozarovskiy.ajtestapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.maxim_ozarovskiy.ajtestapp.R;
import com.example.maxim_ozarovskiy.ajtestapp.model.CurrencyTypes;

import java.util.List;

/**
 * Created by Maxim_Ozarovskiy on 12.12.2017.
 */

public class CurrencyTypesAdapter extends RecyclerView.Adapter<CurrencyTypesAdapter.ViewHolder> {

    private Context context;
    private List<CurrencyTypes> currencyList;
    private CurrencyItemClickListener<CurrencyTypes> currencyItemClickListener;

    public CurrencyTypesAdapter(Context context, List<CurrencyTypes> list, CurrencyItemClickListener<CurrencyTypes> currencyItemClickListener){
        this.context = context;
        this.currencyList =list;
        this.currencyItemClickListener = currencyItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.target_base_currency_item, null);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(CurrencyTypesAdapter.ViewHolder holder, int position) {
        final int pos = holder.getAdapterPosition();

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currencyItemClickListener.currencyItemClick(currencyList.get(pos),pos);
            }
        });

        holder.currencyName.setText(currencyList.get(pos).getName());
        holder.currencyCode.setText(currencyList.get(pos).getCode());

    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView currencyName;
        TextView currencyCode;
        Button check;

        public ViewHolder(View v) {
            super(v);
            check = v.findViewById(R.id.take_some_curency);
            currencyName = v.findViewById(R.id.base_target_curency_name_item_tv);
            currencyCode = v.findViewById(R.id.base_target_currency_code_item_tv);

        }
    }

    public interface CurrencyItemClickListener<C> {
        void currencyItemClick(C c, int position);
    }
}