package com.test.a7ara.sale_log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.a7ara.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class SaleLogAdapter extends RecyclerView.Adapter<SaleLogAdapter.MyViewHolder>{
    private final SaleLogItemClick SaleExpanded;
    private Context context;
    private ArrayList<SaleLogRow> Items;
    private ArrayList<SaleLogRowExpandable> NestedItems;

    public SaleLogAdapter(Context context, ArrayList<SaleLogRow> items, SaleLogItemClick saleExpanded) {
        this.context = context;
        Items = items;
        SaleExpanded = saleExpanded;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_sale_log,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleLogAdapter.MyViewHolder holder , int position) {

        NumberFormat fr = NumberFormat.getInstance(Locale.ENGLISH);
        fr.setMaximumFractionDigits(0);


        holder.Title.setText(String.valueOf(Items.get(position).getTitle()));
        holder.Date.setText(String.valueOf(Items.get(position).getCreationDate_notime()));
        holder.Time.setText(Items.get(position).getCreationTime());
        holder.Ago.setText(" (" + Items.get(position).getAgo() + ")");
        holder.RetailAmount.setText(fr.format(Items.get(position).getRetailAmount()));
        holder.WholesaleAmount.setText(fr.format(Items.get(position).getWholesaleAmount()));
        holder.Profit.setText(fr.format(Items.get(position).getProfit()));
        holder.Count.setText(Items.get(position).getID());
        holder.Discount.setText(fr.format(Items.get(position).getDiscount()));
        holder.expandableLayout.setVisibility(Items.get(position).getExpandable() ? View.VISIBLE : View.GONE);

        if(Items.get(position).getExpandable()){
            holder.ArrowImage.setImageResource(R.drawable.icon_arrow_up);
        }else{
            holder.ArrowImage.setImageResource(R.drawable.icon_arrow_down );
        }

        SaleLogRowNestedAdapter adapter = new SaleLogRowNestedAdapter(Items.get(position).getNestedList());
        if(Items.get(position).getNestedList().size()>0){
            holder.NestedRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
            holder.NestedRecyclerView.setHasFixedSize(true);
            holder.NestedRecyclerView.setAdapter(adapter);

            holder.ArrowImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Items.get(holder.getBindingAdapterPosition()).setExpandable(!Items.get(holder.getBindingAdapterPosition()).getExpandable());
                    NestedItems = Items.get(holder.getBindingAdapterPosition()).getNestedList();
                    notifyDataSetChanged();
                }
            });
        }
        else{
            holder.ArrowImage.setVisibility(View.GONE);
        }




    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ID,Title,Date,Time,RetailAmount,WholesaleAmount,Profit,Count,Discount,Ago;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;
        ImageView ArrowImage;
        RecyclerView NestedRecyclerView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            ID = itemView.findViewById(R.id.count_id);
            Title = itemView.findViewById(R.id.sale_title);
            Date = itemView.findViewById(R.id.creation_date);
            Time = itemView.findViewById(R.id.creation_date_time);
            Ago = itemView.findViewById(R.id.creation_date_ago);
            RetailAmount = itemView.findViewById(R.id.retail_sale_amount);
            WholesaleAmount = itemView.findViewById(R.id.wholesale_sale_amount);
            Profit = itemView.findViewById(R.id.sale_profit_amount);
            Count = itemView.findViewById(R.id.sale_count);
            Discount = itemView.findViewById(R.id.sale_discount_amount);
            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            ArrowImage = itemView.findViewById(R.id.arrow_imageview);
            NestedRecyclerView = itemView.findViewById(R.id.child_rv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
