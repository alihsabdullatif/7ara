package com.test.a7ara.point_of_sale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.test.a7ara.R;
import com.test.a7ara.product_insertion.InsertProduct;

import java.util.Locale;

public class PointOfSaleCountDialog extends AppCompatDialogFragment {

    PointOfSaleCountDialog.CountDialogInterface dis_interface;
    Double PerPiecePrice , Count , Inventory , Max;
    Integer CountWithPrefix;
    int Position;
    TextInputEditText Count_tie;
    TextInputLayout Count_layout;
    Switch ByWeight;
    Boolean ByPriceChecked;

    public PointOfSaleCountDialog (Double ppp , Double count, Integer countWithPrefix,Double inv , int position){
        PerPiecePrice = ppp;
        Count = count;
        Position = position;
        CountWithPrefix = countWithPrefix;
        Inventory = inv;
        Max = Count + Inventory;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        Locale.setDefault(new Locale("en"));
        View view = inflater.inflate(R.layout.dialog_checkout_count_set, null);
        builder.setView(view)
                .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("تأكيد", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        ByWeight = view.findViewById(R.id.status_checkbox);
        if(CountWithPrefix != 1){
            ByWeight.setVisibility(view.GONE);
        }
        Count_tie = view.findViewById(R.id.count_amount);
        Count_tie.setText(String.format("%.0f",Count));
        Count_layout = view.findViewById(R.id.count_layout);
        Count_layout.setHint("العدد");

        ByPriceChecked = false;

        AlertDialog dialog = builder.create();
        dialog.show();


        ByWeight.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                Double inputvalue = 0.0;
                String val = Count_tie.getText().toString();
                if(!val.equals("")){
                    inputvalue = Double.valueOf(val);
                }
                if(inputvalue <= 0){
                    Count_layout.setError("الرجاء ادخال رقم أكبر من صفر");
                }
                Count_layout.setHint("السعر");
                String prefix = CountWithPrefix == 1 ? " غ" : "";
                Count_layout.setHelperText("الكمية ستكون " + String.format("%.0f",Count) + prefix);
                ByPriceChecked = true;
                Count = inputvalue / PerPiecePrice;
            }else{
                Count_layout.setHint("العدد");
                Count_layout.setHelperText(null);
                ByPriceChecked = false;
            }
        });


        Count_tie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                Double inputvalue = 0.0;
                if(!s.toString().isEmpty()){
                    inputvalue = Double.valueOf(s.toString());
                }
                if(inputvalue <= 0){
                    Count_layout.setError("الرجاء ادخال رقم أكبر من صفر");
                }
                else{
                    Count_layout.setError(null);
                    if(ByPriceChecked){
                        Count = Double.valueOf(s.toString()) / PerPiecePrice;
                        String prefix = CountWithPrefix == 1 ? " غ" : "";
                        Count_layout.setHelperText("الكمية ستكون " + String.format("%.0f",Count) + prefix);
                    }else{
                        Count = inputvalue;
                        Count_layout.setHelperText(null);
                    }
                }
            }
        });



        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            boolean error = false;



            if(Count > Max){
                Count_layout.setError("الرجاء ادخال رقم أصغر أو يساوي " + String.format("%.0f",Max));
                error = true;
            }

            if(Count <= 0){
                Count_layout.setError("الرجاء ادخال رقم أكبر من صفر");
                error = true;
            }

            if(!error){
                dialog.dismiss();
                dis_interface.applyCount(Count, Position);
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dis_interface = (PointOfSaleCountDialog.CountDialogInterface) context;
    }

    public interface CountDialogInterface {
        void applyCount (Double dis , int pos);
    }
}
