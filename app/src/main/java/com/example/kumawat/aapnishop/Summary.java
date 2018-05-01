package com.example.kumawat.aapnishop;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Summary extends AppCompatActivity {
    TextView amount_paid, amount_received, amount_udhar;
    Button button_ok;
    double amountSum = 0;
    SQLiteDatabase mDatabase;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        toolbar = (Toolbar) findViewById(R.id.toolbar_summary);
        toolbar.setTitle("Summary");
        setSupportActionBar(toolbar);

        amount_paid = (TextView)findViewById(R.id.amount_paid);
        amount_received = (TextView)findViewById(R.id.amount_received);
        amount_udhar = (TextView)findViewById(R.id.amount_udhar);
        button_ok = (Button)findViewById(R.id.button_ok);

        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        amountSum = query_getAmount("Paid");
        amount_paid.setText(String.valueOf(amountSum));
        amountSum = query_getAmount("Received");
        amount_received.setText(String.valueOf(amountSum));
        amountSum = query_getAmount("Udhar");
        amount_udhar.setText(String.valueOf(amountSum));
        mDatabase.close();
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private double query_getAmount(String qstr) {
        double query_amount=-1;
        String insertSQL = "SELECT SUM(trAmount) FROM ShopTransaction where trType=\'"+qstr+"\';";
        Cursor cursorItems = mDatabase.rawQuery(insertSQL, null);
        if(cursorItems.moveToFirst()){
            query_amount=cursorItems.getDouble(0);
        }
        cursorItems.close();
        return query_amount;
    }
}
