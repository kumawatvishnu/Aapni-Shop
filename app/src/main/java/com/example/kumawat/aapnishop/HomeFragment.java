package com.example.kumawat.aapnishop;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

//import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;
import static android.support.v4.view.MenuItemCompat.getActionView;

/**
 * Created by Kumawat on 4/21/2018.
 */

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {
    List<Item> itemList;
    private List<Item> filteredList;
    RecyclerView recyclerView_home;
    LinearLayoutManager linearLayoutManager;
    SQLiteDatabase mDatabase;
    ItemAdapter itemAdapter;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        itemList = new ArrayList<>();
        filteredList = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_home, null);

        mToolbar = view.findViewById(R.id.toolbar_home);
        mToolbar.setTitle("Kumawat Hardware");
        ((MainActivity)getActivity()).setSupportActionBar(mToolbar);

        recyclerView_home = (RecyclerView) view.findViewById(R.id.recyclerView_home);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView_home.setLayoutManager(linearLayoutManager);

        mDatabase = getActivity().openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        //we used rawQuery(sql, selectionargs) for fetching all the employees
        Cursor cursorItems = mDatabase.rawQuery("SELECT * FROM ShopItem", null);

        //if the cursor has some data
        if (cursorItems.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                Item item = new Item(
                        cursorItems.getInt(0),
                        cursorItems.getString(1),
                        cursorItems.getString(2),
                        cursorItems.getDouble(3),
                        cursorItems.getDouble(4)
                );
                itemList.add(item);
            } while (cursorItems.moveToNext());
        }
        //closing the cursor
        cursorItems.close();
        mDatabase.close();
        /*@Override
        public boolean onCreateOptionsMenu(Menu menu) {
            return super.onCreateOptionsMenu(menu);
        }*/
        filteredList.addAll(itemList);
        itemAdapter = new ItemAdapter(getContext(), filteredList);
        recyclerView_home.setAdapter(itemAdapter);
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "Fragment.onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu_main, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "Fragment.onOptionsItemSelected");
        switch(item.getItemId()){
            case R.id.add_new_item:
                Intent intent = new Intent(getActivity(), AddNewItem.class);
                startActivityForResult(intent, MainActivity.REQUEST_CODE);
                break;
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Item> newItemList = new ArrayList<>();
        for(Item item : itemList){
            String pName = item.getpName();
            if(pName.contains(newText)){
                newItemList.add(item);
            }
        }
        itemAdapter.setFilter(newItemList);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            Item newItem = (Item) data.getExtras().get("passed_item");
            // deal with the item yourself
            itemAdapter.addNewItemEntry(newItem);
        }
    }
}
