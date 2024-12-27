package com.test.a7ara.sale_log;

import static java.lang.String.format;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.a7ara.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SaleLogRowNestedAdapter extends RecyclerView.Adapter<SaleLogRowNestedAdapter.NestedViewHolder> {

    private ArrayList<SaleLogRowExpandable> NestedList;

    public SaleLogRowNestedAdapter (ArrayList<SaleLogRowExpandable> nestedlist){
        this.NestedList = nestedlist;
    }

    @NonNull
    @Override
    public NestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sale_log_expantion , parent , false);
        return new NestedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedViewHolder holder, int position) {

        NumberFormat fr = NumberFormat.getInstance(Locale.ENGLISH);
        fr.setMaximumFractionDigits(0);

        holder.Name.setText(NestedList.get(position).getName());
        holder.TotalRetailPrice.setText(fr.format(NestedList.get(position).getTotalRetailPrice()));
        holder.TotalWholesalePrice.setText(fr.format(NestedList.get(position).getTotalWholesalePrice()));
        holder.Profit.setText(fr.format(NestedList.get(position).getTotalProfit()));
        holder.Count.setText(fr.format(NestedList.get(position).getCount()) + "x" + fr.format(NestedList.get(position).getItemRetailPrice()));

    }

    @Override
    public int getItemCount() {
        return NestedList.size();
    }

    public class NestedViewHolder extends RecyclerView.ViewHolder{

        private TextView Name,TotalRetailPrice, TotalWholesalePrice,Profit,Count;
        public NestedViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.sale_item_name);
            TotalRetailPrice = itemView.findViewById(R.id.sale_item_retail);
            TotalWholesalePrice = itemView.findViewById(R.id.sale_item_wholesale);
            Profit = itemView.findViewById(R.id.sale_item_profit);
            Count = itemView.findViewById(R.id.sale_item_count);
        }
    }
}
