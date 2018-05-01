package com.example.kumawat.aapnishop;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class AddTransaction extends AppCompatActivity implements SelectionListener{
    EditText amount_transaction, details_transaction;
    TextView type_transaction;
    Button button_save, button_cancel;
    SQLiteDatabase mDatabase;
    Toolbar toolbar;
    Transaction new_transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        toolbar = (Toolbar)findViewById(R.id.toolbar_transaction);
        toolbar.setTitle("Transaction");
        setSupportActionBar(toolbar);
        amount_transaction = (EditText)findViewById(R.id.amount_transaction);
        details_transaction = (EditText)findViewById(R.id.details_transaction);
        type_transaction = (TextView)findViewById(R.id.type_transaction);
        button_save = (Button)findViewById(R.id.button_save);
        button_cancel = (Button)findViewById(R.id.button_cancel);
        if(amount_transaction.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        type_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //list of items
                //final String[] trTypes = getResources().getStringArray(R.array.Transsaction_type_list);
                FragmentManager manager = getFragmentManager();
                SingleChoiceDialogFragment dialog = new SingleChoiceDialogFragment();

                Bundle bundle = new Bundle();
                bundle.putStringArrayList(SingleChoiceDialogFragment.DATA, gettrTypes());     // Require ArrayList
                bundle.putInt(SingleChoiceDialogFragment.SELECTED, 0);
                dialog.setArguments(bundle);
                dialog.show(manager, "Dialog");
            }
        });
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addTransaction()) {
                    finish();
                }
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean addTransaction() {
        String trAmount = amount_transaction.getText().toString().trim();
        if (trAmount.isEmpty()) {
            amount_transaction.setError("Please Enter Price....");
            amount_transaction.requestFocus();
            return false;
        }
        String trDetails = details_transaction.getText().toString().trim();
        String trType = type_transaction.getText().toString().trim();
        double trnsactionAmount = Double.parseDouble(trAmount);
        new_transaction = new Transaction(0, trnsactionAmount, trDetails, trType);
        if(inputsAreCorrect(trAmount, trType)){
            //String insertSQL = "INSERT INTO ShopItem VALUES (null, 'pipe', 'PVC plastic', 20, 25);";
            String insertSQL = "INSERT INTO ShopTransaction " +
                    "(id, trAmount, trDetails, trType) " +
                    "VALUES " +
                    "(?, ?, ?, ?);";
            Log.e( "addTransaction: ", "Making new trns entry");
            mDatabase.execSQL(insertSQL, new String[]{null, trAmount, trDetails, trType});
            Toast.makeText(this, trAmount+" Item Added Successfully", Toast.LENGTH_LONG).show();
        }
        else{
            return false;
        }
        return true;
    }

    private boolean inputsAreCorrect(String trAmount, String trType) {
        if (trType.equals("Transaction Type")) {
            type_transaction.setError("Please Select Transaction Type...");
            type_transaction.requestFocus();
            return false;
        }

        if (trAmount.isEmpty()) {
            amount_transaction.setError("Please Enter Price....");
            amount_transaction.requestFocus();
            return false;
        }
        return true;
    }

    private ArrayList<String> gettrTypes()
    {
        ArrayList<String> trTypes = new ArrayList<String>();

        trTypes.add("Paid");
        trTypes.add("Received");
        trTypes.add("Udhar");
        return trTypes;
    }

    @Override
    public void selectItem(int position) {
        String trType = gettrTypes().get(position);
        type_transaction.setText(trType);
        Log.d("Tr_Type:", trType);
    }

    @Override
    public void finish() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("passed_item", new_transaction);
        // setResult(RESULT_OK);
        setResult(RESULT_OK, returnIntent); //By not passing the intent in the result, the calling activity will get null data.
        Log.d(TAG, "add new item.finish");
        super.finish();
    }
}
