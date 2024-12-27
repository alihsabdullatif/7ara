package com.test.a7ara.point_of_sale;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.test.a7ara.databinding.RowCheckoutBinding;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PointOfSaleListAdapter extends RecyclerView.Adapter<PointOfSaleListAdapter.DataHolder> {
    private ArrayList<PointOfSaleRow> itemsList;
    private PointOfSaleItemClick listener;

    public PointOfSaleListAdapter(ArrayList<PointOfSaleRow> arrayList1 , PointOfSaleItemClick listener) {
        this.itemsList = arrayList1;
        this.listener = listener;
    }

    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowCheckoutBinding rv_rowBinding = RowCheckoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new DataHolder(rv_rowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, int position) {
        NumberFormat fr = NumberFormat.getInstance(Locale.ENGLISH);
        fr.setMaximumFractionDigits(0);
        holder.binding.dataTextView.setText(itemsList.get(position).getName());
        holder.binding.priceTextView.setText(fr.format(itemsList.get(position).getRowPrice()));
        holder.binding.countTextView.setText(itemsList.get(position).getCountWithPrefix());
        holder.plusbutton.setOnClickListener(v -> listener.OnIncreaseClicked(holder.getBindingAdapterPosition()));
        holder.minusbutton.setOnClickListener(v -> listener.OnDecreaseClicked(holder.getBindingAdapterPosition()));
        holder.rowlayout.setOnClickListener(v -> listener.OnRowClicked(holder.getBindingAdapterPosition()));
        holder.count_tv.setOnClickListener(v -> listener.OnCountClicked(holder.getBindingAdapterPosition()));
        holder.binding.remainingTextview.setText("المتبقي "+itemsList.get(position).getRemainingWithPrefix());

        if(itemsList.get(position).getCount().equals(1.0)){
            holder.minusbutton.setVisibility(View.GONE);
        }else if(itemsList.get(position).getCount()>1.0){
            holder.minusbutton.setVisibility(View.VISIBLE);
        }

        if(itemsList.get(position).getRemaining().equals(0.0)){
            holder.plusbutton.setVisibility(View.GONE);
        }else if(itemsList.get(position).getRemaining()>0.0){
            holder.plusbutton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder{

        private RowCheckoutBinding binding;
        public ImageView plusbutton,minusbutton;
        public TextView count_tv,remaining_tv;
        public ConstraintLayout rowlayout;
        public DataHolder(RowCheckoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            plusbutton = binding.countTextViewPlus;
            minusbutton = binding.countTextViewMinus;
            rowlayout = binding.infoLayout;
            count_tv = binding.countTextView;
            remaining_tv = binding.remainingTextview;
        }
    }
}
