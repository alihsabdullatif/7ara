package com.test.a7ara.inventory_insertion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.a7ara.R;
import com.test.a7ara.sale_log.SaleLogRowNestedAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class InsertListAdapter extends RecyclerView.Adapter<InsertListAdapter.MyViewHolder>{

    private final InsertListItemClick InsertExpanded;
    private Context context;
    private ArrayList<InsertListRow> Items;

    public InsertListAdapter(Context context, ArrayList<InsertListRow> items, InsertListItemClick insertexpanded) {
        this.context = context;
        Items = items;
        this.InsertExpanded = insertexpanded;
    }

    @NonNull
    @Override
    public InsertListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_insert,parent,false);
        return new InsertListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InsertListAdapter.MyViewHolder holder, int position) {

        NumberFormat fr = NumberFormat.getInstance(Locale.ENGLISH);
        fr.setMaximumFractionDigits(2);

        holder.ID.setText(String.valueOf(Items.get(position).getID()));
        holder.ProductName.setText(String.valueOf(Items.get(position).getProductName()));
        holder.InsertCount.setText(fr.format(Items.get(position).getInsertCount()));
        holder.InsertValue.setText(fr.format(Items.get(position).getInsertWholesalePrice()));
        holder.CreationDate.setText(Items.get(position).getCreationDateTime());
        holder.Remaining.setMax(Items.get(position).getInsertCount());
        holder.Remaining.setProgress(Items.get(position).getInsertRemainingCount());
        holder.Remaining_count.setText(fr.format(Items.get(position).getInsertRemainingCount()));

        holder.NestedRecyclerView.setText(Items.get(position).getNestedList());
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView ID,ProductName,CreationDate,InsertCount,InsertValue,Remaining_count,NestedRecyclerView;
        ProgressBar Remaining;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ID = itemView.findViewById(R.id.insert_id);
            ProductName = itemView.findViewById(R.id.product_name);
            CreationDate = itemView.findViewById(R.id.creation_date);
            InsertCount = itemView.findViewById(R.id.count_amount);
            InsertValue = itemView.findViewById(R.id.insert_value);
            Remaining = itemView.findViewById(R.id.remaining);
            Remaining_count = itemView.findViewById(R.id.remaining_count);
            NestedRecyclerView = itemView.findViewById(R.id.logs);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
