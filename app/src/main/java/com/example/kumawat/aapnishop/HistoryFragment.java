package com.example.kumawat.aapnishop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;
import static com.example.kumawat.aapnishop.MainActivity.TABLE_SHOPITEM;
import static com.example.kumawat.aapnishop.MainActivity.TABLE_ShopTransaction;

/**
 * Created by Kumawat on 4/21/2018.
 */

public class HistoryFragment extends Fragment {
    List<Transaction> transactionList;
    TransactionAdapter transactionAdapter;
    RecyclerView recyclerView_history;
    LinearLayoutManager linearLayoutManager_history;
    SQLiteDatabase mDatabase;
    Toolbar mToolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
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
        transactionAdapter = new TransactionAdapter(getContext(), transactionList);
        recyclerView_history.setAdapter(transactionAdapter);
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "Fragment.onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_historyfragment, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "Fragment.onOptionsItemSelected");
        switch(item.getItemId()){
            case R.id.clear_transaction:
                clearTransactionDB();
                break;
        }
        return true;
    }
    private void clearTransactionDB() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_db_text)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSIL
                        // Drop older table if existed
                        mDatabase = getActivity().openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
                        mDatabase.execSQL("DELETE FROM " + TABLE_ShopTransaction);
                        //mDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ShopTransaction);
                        dialog.dismiss();
                        transactionList.clear();
                        transactionAdapter.setAllClearItem();
                        mDatabase.close();
                        Toast.makeText(getActivity(), "All Item Deleted Successfully", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }
}
