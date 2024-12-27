package com.test.a7ara.sale_log;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.test.a7ara.R;

public class SaleLogFilterDialog extends AppCompatDialogFragment {

    SaleLogFilterDialogInterface sl_interface;
    Switch Grouped;
    Boolean GroupedValue;

    public SaleLogFilterDialog (Boolean G){
        GroupedValue = G;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_sale_log_filter, null);
        builder.setView(view)
                .setTitle("Filters")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean gr  = Grouped.isChecked();
                        sl_interface.applyFilter(gr);
                    }
                });


        Grouped = view.findViewById(R.id.grouped);
        Grouped.setChecked(GroupedValue);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sl_interface = (SaleLogFilterDialogInterface) context;
    }

    public interface SaleLogFilterDialogInterface {
        void applyFilter (Boolean grouped);
    }
}
