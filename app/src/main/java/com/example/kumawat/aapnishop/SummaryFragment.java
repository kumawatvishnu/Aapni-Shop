package com.example.kumawat.aapnishop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;
import static com.example.kumawat.aapnishop.MainActivity.TABLE_SHOPITEM;

public class SummaryFragment extends Fragment {
    TextView amount_paid, amount_received, amount_udhar;
    Button button_ok;
    double amountSum = 0;
    SQLiteDatabase mDatabase;
    private Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, null);
        toolbar = view.findViewById(R.id.toolbar_summary);
        toolbar.setTitle("Summary");
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);

        amount_paid = (TextView)view.findViewById(R.id.amount_paid);
        amount_received = (TextView)view.findViewById(R.id.amount_received);
        amount_udhar = (TextView)view.findViewById(R.id.amount_udhar);
        button_ok = (Button)view.findViewById(R.id.button_ok);

        mDatabase = getActivity().openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
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
                //finish();
            }
        });
        return view;
    }

    private double query_getAmount(String qstr) {
        double query_amount = 0;
        String insertSQL = "SELECT SUM(trAmount) FROM ShopTransaction where trType=\'"+qstr+"\';";
        Cursor cursorItems = mDatabase.rawQuery(insertSQL, null);
        if(cursorItems.moveToFirst()){
            query_amount=cursorItems.getDouble(0);
        }
        cursorItems.close();
        return query_amount;
    }
}
