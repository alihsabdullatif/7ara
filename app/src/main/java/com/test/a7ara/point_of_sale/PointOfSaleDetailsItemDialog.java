package com.test.a7ara.point_of_sale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.test.a7ara.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PointOfSaleDetailsItemDialog extends AppCompatDialogFragment {
    TextView Name, Retail, Wholesale, Profit, ProfitPercent;
    TextView RetailT,WholesaleT, ProfitT, ProfitPercentT;
    PointOfSaleRow Item;
    ListView listView,listView2;
    WholesalePriceChangeAdapter item_pricechange_adapter,pricechange_adapter;

    public PointOfSaleDetailsItemDialog(PointOfSaleRow item){
        Item = item;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_checkout_item_details, null);
        builder.setView(view);

        Name = view.findViewById(R.id.name_value);
        Retail = view.findViewById(R.id.retail_value);
        Wholesale = view.findViewById(R.id.wholesale_value);
        Profit = view.findViewById(R.id.profit_value);
        ProfitPercent = view.findViewById(R.id.profit_percent_value);

        pricechange_adapter = new WholesalePriceChangeAdapter(getContext(),R.layout.row_wholesalepricechange,Item.getPrices());
        listView = view.findViewById(R.id.pricechangelist);
        listView.setAdapter(pricechange_adapter);
        item_pricechange_adapter = new WholesalePriceChangeAdapter(getContext(),R.layout.row_wholesalepricechange,Item.getWSPrices());
        listView2 = view.findViewById(R.id.itempricechangelist);
        listView2.setAdapter(item_pricechange_adapter);

        NumberFormat fr = NumberFormat.getInstance(Locale.ENGLISH);
        fr.setMaximumFractionDigits(0);

        Name.setText(Item.getName());
        Retail.setText(fr.format(Item.getPrice()));
        Wholesale.setText(fr.format(Item.getWholesalePrice()));
        Profit.setText(fr.format(Item.getPieceProfit()));
        ProfitPercent.setText(fr.format(Item.getTotalProfitPercent()) + "%");

        RetailT = view.findViewById(R.id.retail_name);
        WholesaleT = view.findViewById(R.id.wholesale_name);
        ProfitT = view.findViewById(R.id.profit_name);
        ProfitPercentT = view.findViewById(R.id.profit_percent_name);
        if(Item.getByWeight() == 1){
            RetailT.setText("سعر ال غ بالمفرق");
            WholesaleT.setText("سعر ال غ بالجملة");
            ProfitT.setText("الربح في ال غ");
            ProfitPercentT.setText("نسبة الربح في ال غ");
        }
        else{
            RetailT.setText("سعر القطعة بالمفرق");
            WholesaleT.setText("سعر القطعة بالجملة");
            ProfitT.setText("الربح في القطعة");
            ProfitPercentT.setText("نسبة الربح في القطعة");
        }

        AlertDialog dialog = builder.create();

        return dialog;
    }
}
