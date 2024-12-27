package com.test.a7ara.point_of_sale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.test.a7ara.R;

import java.text.NumberFormat;
import java.util.Locale;

public class PointOfSaleDetailsDialog extends AppCompatDialogFragment {

    Double RetailValue, WholesaleValue, ProfitValue, ProfitPercentValue, DiscountValue;
    TextView Retail, Wholesale, Profit, ProfitPercent, Discount, TotalAD, ProfitAD, ProfitPercentAD;
    ConstraintLayout row4, row5;

    public PointOfSaleDetailsDialog(Double retail, Double wholesale, Double profit, Double profitpercent, Double discount){
        RetailValue = retail;
        WholesaleValue = wholesale;
        ProfitValue = profit;
        ProfitPercentValue = profitpercent;
        DiscountValue = discount;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_checkout_details, null);
        builder.setView(view);

        Retail = view.findViewById(R.id.retail_value);
        Wholesale = view.findViewById(R.id.wholesale_value);
        Profit = view.findViewById(R.id.profit_value);
        ProfitPercent = view.findViewById(R.id.profit_percent_value);
        Discount = view.findViewById(R.id.discount_value);
        TotalAD = view.findViewById(R.id.total_ad_value);
        ProfitAD = view.findViewById(R.id.profit_ad_value);
        ProfitPercentAD = view.findViewById(R.id.profit_ad_percent_value);

        row4 = view.findViewById(R.id.row4);
        row5 = view.findViewById(R.id.row5);

        NumberFormat fr = NumberFormat.getInstance(Locale.ENGLISH);
        fr.setMaximumFractionDigits(0);

        Retail.setText(fr.format(RetailValue));
        Wholesale.setText(fr.format(WholesaleValue));
        Profit.setText(fr.format(ProfitValue));
        ProfitPercent.setText(fr.format(ProfitPercentValue) + "%");

        if(DiscountValue == 0){
            row4.setVisibility(view.GONE);
            row5.setVisibility(view.GONE);
        }
        else{
            row4.setVisibility(view.VISIBLE);
            row5.setVisibility(view.VISIBLE);

            Discount.setText(fr.format(DiscountValue));
            TotalAD.setText(fr.format(RetailValue - DiscountValue));
            ProfitAD.setText(fr.format(ProfitValue - DiscountValue));
            ProfitPercentAD.setText(fr.format(((ProfitValue - DiscountValue)/RetailValue)*100) + "%");
        }

        AlertDialog dialog = builder.create();

        return dialog;
    }
}
