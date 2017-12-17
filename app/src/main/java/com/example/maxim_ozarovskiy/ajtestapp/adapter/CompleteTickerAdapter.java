package com.example.maxim_ozarovskiy.ajtestapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maxim_ozarovskiy.ajtestapp.R;
import com.example.maxim_ozarovskiy.ajtestapp.model.Market;

import java.util.List;

/**
 * Created by Maxim_Ozarovskiy on 11.12.2017.
 */

public class CompleteTickerAdapter extends RecyclerView.Adapter<CompleteTickerAdapter.ViewHolder> {

    private Context context;
    private List<Market> marketList;

    public CompleteTickerAdapter(Context context, List<Market> list){
        this.context = context;
        this.marketList = list;
    }

    @Override
    public CompleteTickerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_card_item, null);
        CompleteTickerAdapter.ViewHolder holder = new CompleteTickerAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(CompleteTickerAdapter.ViewHolder holder, int position) {
        final int pos = holder.getAdapterPosition();
        holder.marketName.setText(marketList.get(pos).getMarket());
        holder.marketPrice.setText(marketList.get(pos).getPrice());
        holder.marketVolume.setText(marketList.get(pos).getVolume());
    }

    @Override
    public int getItemCount() {
        return marketList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView marketName;
        TextView marketPrice;
        TextView marketVolume;

        public ViewHolder(View v) {
            super(v);

            marketName = v.findViewById(R.id.market_name_item);
            marketPrice = v.findViewById(R.id.market_price_item);
            marketVolume = v.findViewById(R.id.market_volume_item);

        }
    }
}
