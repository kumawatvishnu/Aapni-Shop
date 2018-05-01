package com.example.kumawat.aapnishop;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Kumawat on 4/21/2018.
 */

public class HistoryFragment extends Fragment {
    List<Transaction> transactionList;
    RecyclerView recyclerView_history;
    LinearLayoutManager linearLayoutManager_history;
    SQLiteDatabase mDatabase;
    Toolbar mToolbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        transactionList = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_history, null);

        mToolbar = view.findViewById(R.id.toolbar_history);
        mToolbar.setTitle("History");
        ((MainActivity)getActivity()).setSupportActionBar(mToolbar);

        recyclerView_history = (RecyclerView) view.findViewById(R.id.recyclerView_history);
        linearLayoutManager_history = new LinearLayoutManager(this.getActivity());
        recyclerView_history.setLayoutManager(linearLayoutManager_history);

        mDatabase = getActivity().openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        //we used rawQuery(sql, selectionargs) for fetching all the employees
        Cursor cursorItems = mDatabase.rawQuery("SELECT * FROM ShopTransaction", null);

        //if the cursor has some data
        if (cursorItems.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                Transaction transaction = new Transaction(
                        cursorItems.getInt(0),
                        cursorItems.getDouble(1),
                        cursorItems.getString(2),
                        cursorItems.getString(3)
                );
                transactionList.add(transaction);
            } while (cursorItems.moveToNext());
        }
        //closing the cursor
        cursorItems.close();
        mDatabase.close();
        TransactionAdapter transactionAdapter = new TransactionAdapter(getContext(), transactionList);
        recyclerView_history.setAdapter(transactionAdapter);
        return view;
    }
}
