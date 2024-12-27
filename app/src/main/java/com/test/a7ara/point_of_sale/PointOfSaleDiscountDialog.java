package com.test.a7ara.point_of_sale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.test.a7ara.R;

public class PointOfSaleDiscountDialog extends AppCompatDialogFragment {

    DiscountDialogInterface dis_interface;
    TextInputEditText Amount;
    TextInputLayout AmountLayout;
    TextView Warning;
    Double Profit;

    public PointOfSaleDiscountDialog(Double profit){
        Profit = profit;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_discount, null);
        builder.setView(view)
                .setTitle("الحسم")
                .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("تأكيد", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!Amount.getText().toString().isEmpty()){
                            dis_interface.applyDiscount(Double.valueOf(Amount.getText().toString()));
                        }
                    }
                });


        AlertDialog dialog = builder.create();

        Amount = view.findViewById(R.id.discount_amount);
//        Amount.setText("0");
        AmountLayout = view.findViewById(R.id.discount_amount_layout);
        AmountLayout.setHelperText("الحد الأعلى للحسم هو  " + String.format("%.0f",Profit));
        Amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                Double amount = 0.0;
                if(!s.toString().isEmpty()){
                    amount = Double.valueOf(s.toString());
                    if(amount > Profit  || amount < 0){
                        AmountLayout.setError("القيمة أكبر من الحد الأعلى, الحسم لا يمكن أن يكون أكبر من الربح.");
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                    else{
                        AmountLayout.setError(null);
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                }else{
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dis_interface = (DiscountDialogInterface) context;
    }

    public interface DiscountDialogInterface {
        void applyDiscount (Double dis);
    }
}
