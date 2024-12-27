package com.test.a7ara.product_insertion;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.test.a7ara.BarCodeCaptureAct;
import com.test.a7ara.DatabaseHelper;
import com.test.a7ara.R;

import java.util.Objects;

public class InsertProduct extends AppCompatActivity {

    Button btn_scan;
    Button btn_save;
    TextInputEditText input_name,input_cat,input_code;
    Switch ByWeight;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product);

        myDB = new DatabaseHelper(this);
        input_name = (TextInputEditText) findViewById(R.id.input_name);
        input_cat = (TextInputEditText) findViewById(R.id.input_cat);
        input_code = (TextInputEditText) findViewById(R.id.input_code);
        ByWeight = findViewById(R.id.byweightswitch);

        btn_scan= findViewById(R.id.button_scan);
        btn_scan.setOnClickListener(v->{
            ScanCode();
        });

        btn_save= findViewById(R.id.btn_save);
        btn_save.setOnClickListener(v->{
            SaveChanges();
        });

    }

    private void ClearValues() {
        input_name.setText("");
        input_cat.setText("");
        input_code.setText("");
        ByWeight.setChecked(false);
    }

    private void SaveChanges() {
        if(myDB.Code_added(Objects.requireNonNull(input_code.getText()).toString())){
            Toast.makeText(InsertProduct.this,"code already added.",Toast.LENGTH_LONG).show();
        }
        else{
            Integer bw = ByWeight.isChecked() ? 1 : 0;
            long inserted = myDB.insertProduct(Objects.requireNonNull(input_name.getText()).toString(),
                    Objects.requireNonNull(input_code.getText()).toString(),
                    Objects.requireNonNull(input_cat.getText()).toString(),
                    bw);
            boolean code_inserted = myDB.insertCode(inserted,Objects.requireNonNull((input_code.getText().toString())));
            if(inserted != -1 && code_inserted){
                boolean inserted_code = myDB.insertCode(inserted,
                        Objects.requireNonNull(input_code.getText()).toString());
                if(inserted_code){
                    Toast.makeText(InsertProduct.this,"id"+input_name.getText().toString()+" with code "+input_code.getText().toString()+" inserted.",Toast.LENGTH_LONG).show();
                    ClearValues();
                }
            }
            else {
                Toast.makeText(InsertProduct.this, "Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void ScanCode() {
        ScanOptions op = new ScanOptions();
        op.setPrompt("press Volume بشعل الفلاش");
        op.setBeepEnabled(true);
        op.setOrientationLocked(true);
        op.setCaptureActivity(BarCodeCaptureAct.class);
        barLauncher.launch(op);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->{
        if(result.getContents() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(InsertProduct.this);
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
}