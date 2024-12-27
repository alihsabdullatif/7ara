package com.test.a7ara.diffs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.test.a7ara.DatabaseHelper;
import com.test.a7ara.ProductAutoCompleteHelper;
import com.test.a7ara.R;
import com.test.a7ara.point_of_sale.PointOfSale;
import com.test.a7ara.point_of_sale.PointOfSaleCountDialog;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DiffsListInsertDialog extends AppCompatDialogFragment {

    DiffsListInsertDialog.DiffInsertDialogInterface diff_interface;
    Integer position;
    DiffListRow Row;
    Integer isLoss;

    AutoCompleteTextView ProductName,DiffType;
    TextInputEditText DiffCount;
    TextInputLayout ProductNameLayout,DiffCountLayout,DiffTypeLayout;

    ProductAutoCompleteHelper autoCompleteHelper;



    public DiffsListInsertDialog(DiffListRow row, Integer pos) {
        Row = row;
        position = pos;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        Locale.setDefault(new Locale("en"));
        View view = inflater.inflate(R.layout.dialog_add_diff, null);

        builder.setView(view)
                .setTitle(position==null?"فرق جديد":"تعديل")
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


        DatabaseHelper DB = new DatabaseHelper(getContext());
        autoCompleteHelper = DB.getAutoCompleteHelper();

        ProductNameLayout = view.findViewById(R.id.product_layout);
        ProductName = view.findViewById(R.id.product_name);
        if(autoCompleteHelper!=null){
            ArrayAdapter<String> namesadapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, autoCompleteHelper.getNames());
            ProductName.setAdapter(namesadapter);
        }


        DiffCountLayout = view.findViewById(R.id.diff_count_layout);
        DiffCount = view.findViewById(R.id.diff_count);

        DiffTypeLayout = view.findViewById(R.id.diff_type_layout);
        DiffType = view.findViewById(R.id.diff_type);

        NumberFormat fr = NumberFormat.getInstance(Locale.ENGLISH);
        fr.setMaximumFractionDigits(2);
        if(position!=null){
            ProductName.setText(Row.getProductName());
            DiffCount.setText(fr.format(Row.getDiffCount()));
            DiffType.setText(Row.getDiffType());
        }

        String[] difftypes = getResources().getStringArray(R.array.diffTypes);
        ArrayAdapter<String> typesadapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, difftypes);
        DiffType.setAdapter(typesadapter);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean error = false;
                boolean okproduct = false;
                boolean oknumber = false;

                isLoss = 1;
                if(autoCompleteHelper!=null && autoCompleteHelper.getNames().contains(ProductName.getText().toString())){
                    ProductNameLayout.setError(null);
                    okproduct=true;
                } else{
                    ProductNameLayout.setError("الرجاء ادخال اسم صحيح");
                    error = true;
                }

                if(!DiffCount.getText().toString().isEmpty() && Double.valueOf(DiffCount.getText().toString())>0){
                    DiffCountLayout.setError(null);
                    oknumber=true;
                } else{
                    DiffCountLayout.setError("الرجاء ادخال عدد موجب");
                    error = true;
                }



                if(!DiffType.getText().toString().isEmpty()){
                    DiffTypeLayout.setError(null);
                    if(DiffType.getText().toString().equals("زيادة")){
                        isLoss = 0;
                    }

                } else{
                    DiffTypeLayout.setError("الرجاء تحديد نوع");
                    error = true;
                }

                Double ChosenItemInventory = Double.valueOf(autoCompleteHelper.getInventories().get(autoCompleteHelper.getNames().indexOf(ProductName.getText().toString())));
                Double EditItemInventory = ChosenItemInventory + (Row==null?0:Row.getDiffCount());
                if(oknumber && okproduct && isLoss == 1 &&
                        Double.valueOf(DiffCount.getText().toString())>EditItemInventory){
                    DiffCountLayout.setError("الرجاء ادخال عدد أصغر أو يساوي "+ fr.format(EditItemInventory));
                    error = true;
                }

                if(!error){
                    dialog.dismiss();
                    if(position!=null){
                        diff_interface.InsertDiff(autoCompleteHelper.getIds().get(autoCompleteHelper.getNames().indexOf(ProductName.getText().toString())),
                                Double.valueOf(DiffCount.getText().toString()),DiffType.getText().toString(),Row.getID(),isLoss);
                    }
                    else{
                        diff_interface.InsertDiff(autoCompleteHelper.getIds().get(autoCompleteHelper.getNames().indexOf(ProductName.getText().toString())),
                                Double.valueOf(DiffCount.getText().toString()),DiffType.getText().toString(),null,isLoss);
                    }


                }
            }
        });


        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        diff_interface = (DiffsListInsertDialog.DiffInsertDialogInterface) context;
    }

    public interface DiffInsertDialogInterface {
        void InsertDiff (String ProductID , Double DiffCount , String DiffType , String DiffID, Integer Loss);
    }
}
