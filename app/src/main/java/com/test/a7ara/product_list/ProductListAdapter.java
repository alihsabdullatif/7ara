package com.test.a7ara.product_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.a7ara.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {
    private final ProductListItemClick ProductExpanded;
    private Context context;
    private ArrayList<ProductListRow> Items;

    public ProductListAdapter(Context context, ArrayList<ProductListRow> items, ProductListItemClick productexpanded) {
        this.context = context;
        Items = items;
        this.ProductExpanded = productexpanded;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_product,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        NumberFormat fr = NumberFormat.getInstance(Locale.ENGLISH);
        fr.setMaximumFractionDigits(2);

        holder.ID.setText(String.valueOf(Items.get(position).getID()));
        holder.Name.setText(String.valueOf(Items.get(position).getName()));
        holder.CreationDate.setText("أضيف في "+String.valueOf(Items.get(position).getCreationDateTime()));
        holder.Inserts.setText(Items.get(position).getByWeight()?fr.format(Items.get(position).getInserts()/1000)+" كغ":fr.format(Items.get(position).getInserts()));
        holder.Sales.setText(Items.get(position).getByWeight()?fr.format(Items.get(position).getSales()/1000)+" كغ":fr.format(Items.get(position).getSales()));
        holder.Diffs.setText(Items.get(position).getByWeight()?fr.format(Items.get(position).getDiffs()/1000)+" كغ":fr.format(Items.get(position).getDiffs()));
        holder.Inventory.setText(Items.get(position).getByWeight()?fr.format(Items.get(position).getInventory()/1000)+" كغ":fr.format(Items.get(position).getInventory()));
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ID,Name,CreationDate,Inventory,Sales,Inserts,Diffs;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ID = itemView.findViewById(R.id.product_id);
            Name = itemView.findViewById(R.id.product_name);
            CreationDate = itemView.findViewById(R.id.creation_date);
            Inventory = itemView.findViewById(R.id.inv_amount);
            Inventory.setSelected(true);
            Sales = itemView.findViewById(R.id.sales_amount);
            Sales.setSelected(true);
            Inserts = itemView.findViewById(R.id.inserts_amount);
            Inserts.setSelected(true);
            Diffs = itemView.findViewById(R.id.diffs_amount);
            Diffs.setSelected(true);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ProductExpanded != null){
                        int pos = getBindingAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            ProductExpanded.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
