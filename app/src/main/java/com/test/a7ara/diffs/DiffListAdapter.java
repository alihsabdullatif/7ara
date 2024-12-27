package com.test.a7ara.diffs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.test.a7ara.R;
import com.test.a7ara.product_list.ProductListItemClick;
import com.test.a7ara.product_list.ProductListRow;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DiffListAdapter extends RecyclerView.Adapter<DiffListAdapter.MyViewHolder> {

    private final DiffListItemClick DiffExpanded;
    private Context context;
    private ArrayList<DiffListRow> Items;

    public DiffListAdapter(Context context, ArrayList<DiffListRow> items, DiffListItemClick diffexpanded) {
        this.context = context;
        Items = items;
        this.DiffExpanded = diffexpanded;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_diff,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DiffListAdapter.MyViewHolder holder, int position) {

        NumberFormat fr = NumberFormat.getInstance(Locale.ENGLISH);
        fr.setMaximumFractionDigits(2);

        holder.ID.setText(String.valueOf(Items.get(position).getID()));
        holder.ProductName.setText(String.valueOf(Items.get(position).getProductName()));
        holder.DiffType.setText(String.valueOf(Items.get(position).getDiffType()));
        if(String.valueOf(Items.get(position).getLoss()).equals("0")){
            holder.diff_v_lo.setBackground(ContextCompat.getDrawable(context , R.drawable.box_green));
            holder.diff_c_lo.setBackground(ContextCompat.getDrawable(context , R.drawable.box_green_faint));
            holder.diff_n_tv.setTextColor(ContextCompat.getColor(context , R.color.Green));
            holder.diff_c_tv.setTextColor(ContextCompat.getColor(context , R.color.Green));
            holder.DiffType.setTextColor(ContextCompat.getColor(context , R.color.Green));
        }
        holder.DiffCount.setText(fr.format(Items.get(position).getDiffCount()));
        holder.DiffValue.setText(fr.format(Items.get(position).getDiffWholesalePrice()));
        holder.CreationDate.setText(Items.get(position).getCreationDateTime());

    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView ID,ProductName,CreationDate,DiffCount,DiffValue,DiffType;
        ConstraintLayout diff_v_lo,diff_c_lo;
        TextView diff_n_tv,diff_c_tv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ID = itemView.findViewById(R.id.diff_id);
            ProductName = itemView.findViewById(R.id.product_name);
            CreationDate = itemView.findViewById(R.id.creation_date);
            DiffType = itemView.findViewById(R.id.difftype);
            DiffCount = itemView.findViewById(R.id.diffs_amount);
            DiffValue = itemView.findViewById(R.id.diffs_value);
            diff_v_lo = itemView.findViewById(R.id.diffs_value_name_layout);
            diff_c_lo = itemView.findViewById(R.id.diffcounts);
            diff_n_tv = itemView.findViewById(R.id.diffs_name);
            diff_c_tv = itemView.findViewById(R.id.diffs_amount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
