package com.test.a7ara.inventory_insertion;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.test.a7ara.BarCodeCaptureAct;
import com.test.a7ara.DatabaseHelper;
import com.test.a7ara.R;

import java.util.Objects;

public class InsertInventory extends AppCompatActivity {


    Button btn_scan;
    Button btn_save;
    TextInputEditText input_name,input_code,input_number,input_price,input_wholesale,input_retail;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_inventory);


        myDB = new DatabaseHelper(this);
        input_name = (TextInputEditText) findViewById(R.id.input_name);
        input_code = (TextInputEditText) findViewById(R.id.input_code);
        input_number = (TextInputEditText) findViewById(R.id.input_number);
        input_price = (TextInputEditText) findViewById(R.id.input_price);
        input_wholesale = (TextInputEditText) findViewById(R.id.input_wholesale);
        input_retail = (TextInputEditText) findViewById(R.id.input_retail);
        disableEditText(input_wholesale);

        Check_Changes();

        btn_scan= findViewById(R.id.button_scan);
        btn_scan.setOnClickListener(v->{
            ScanCode();

        });

        btn_save= findViewById(R.id.btn_save);
        btn_save.setOnClickListener(v->{
            SaveChanges();
        });

    }

    private void SaveChanges() {
        if(myDB.Code_added(Objects.requireNonNull(input_code.getText()).toString())){
            String PID = myDB.getProductIDFromCode(Objects.requireNonNull(input_code.getText()).toString());
            String nu = Objects.requireNonNull(input_number.getText()).toString();
            String pr = Objects.requireNonNull(input_price.getText()).toString();
            String re = Objects.requireNonNull(input_retail.getText()).toString();
            long inserted = myDB.insertInsertion(Objects.requireNonNull(PID),
                    Objects.requireNonNull(nu),
                    Objects.requireNonNull(pr),
                    Objects.requireNonNull(re));
            if(inserted != -1){
                Toast.makeText(InsertInventory.this,"insertion of "+input_number.getText().toString()+" "+input_name.getText().toString()+"'s done.",Toast.LENGTH_LONG).show();
                ClearValues();
            }
            else {
                Toast.makeText(InsertInventory.this, String.valueOf(inserted), Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(InsertInventory.this,"code isn't added.",Toast.LENGTH_LONG).show();
        }
    }
    private void ClearValues() {
        input_name.setText("");
        input_code.setText("");
        input_number.setText("");
        input_price.setText("");
    }

    private void Check_Changes() {
        input_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String nu = String.valueOf(input_number.getText());
                String pr = String.valueOf(input_price.getText());
                if(!pr.equals("") && !nu.equals("")){
                    double price = Double.parseDouble(pr);
                    double number = Double.parseDouble(nu);
                    double result = price / number;
                    input_wholesale.setText(String.valueOf(result));
                }
            }
        });
        input_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String nu = String.valueOf(input_number.getText());
                String pr = String.valueOf(input_price.getText());
                if(!pr.equals("") && !nu.equals("")){
                    double price = Double.parseDouble(pr);
                    double number = Double.parseDouble(nu);
                    double result = price / number;
                    input_wholesale.setText(String.valueOf(result));
                }
            }
        });
        input_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String co = String.valueOf(input_code.getText());
                Boolean found = myDB.Code_added(co);
                if(found){
                    String res = myDB.getProductNameFromCode(co);
                    input_name.setText(String.valueOf(res));
                }
                else{
                    input_name.setText(String.valueOf(""));
                }
            }
        });
    }

    private void ScanCode() {
        ScanOptions op = new ScanOptions();
        op.setPrompt("Volume up بشعل الفلاش");
        op.setBeepEnabled(true);
        op.setOrientationLocked(true);
        op.setCaptureActivity(BarCodeCaptureAct.class);
        barLauncher.launch(op);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->{
        if(result.getContents() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(InsertInventory.this);
            builder.setTitle("Res");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
            TextInputEditText coderes = (TextInputEditText) findViewById(R.id.input_code);
            coderes.setText(result.getContents());
        }
    });

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
}