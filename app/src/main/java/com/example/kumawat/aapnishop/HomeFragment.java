package com.example.kumawat.aapnishop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

//import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;
import static android.support.v4.view.MenuItemCompat.getActionView;
import static com.example.kumawat.aapnishop.MainActivity.TABLE_SHOPITEM;
import static com.example.kumawat.aapnishop.MainActivity.TABLE_ShopTransaction;

/**
 * Created by Kumawat on 4/21/2018.
 */

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener, AlertDialogHelper.AlertDialogListener {
    ActionMode mActionMode;
    Menu context_menu;
    private List<Item> itemList;
    private List<Item> filteredList;
    private List<Item> multiselect_list;
    RecyclerView recyclerView_home;
    LinearLayoutManager linearLayoutManager;
    SQLiteDatabase mDatabase;
    ItemAdapter itemAdapter;
    //MultiSelectAdapter multiSelectAdapter;
    boolean isMultiSelect = false;
    private Toolbar mToolbar;
    AlertDialogHelper alertDialogHelper;

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
        multiselect_list = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_home, null);

        mToolbar = view.findViewById(R.id.toolbar_home);
        mToolbar.setTitle("Chaudhary Hardware");
        ((MainActivity)getActivity()).setSupportActionBar(mToolbar);
        //alertDialogHelper =new AlertDialogHelper(getActivity());
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

        filteredList.addAll(itemList);
        recyclerView_home = (RecyclerView) view.findViewById(R.id.recyclerView_home);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView_home.setLayoutManager(linearLayoutManager);
        recyclerView_home.setItemAnimator(new DefaultItemAnimator());
        itemAdapter = new ItemAdapter(getActivity(), filteredList, multiselect_list);
        recyclerView_home.setAdapter(itemAdapter);
        recyclerView_home.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView_home, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);
                else
                    Toast.makeText(getActivity(), "Details Page", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<Item>();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = HomeFragment.this.getActivity().startActionMode(mActionModeCallback);
                    }
                }

                multi_select(position);

            }
        }));
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
            case R.id.clear_database:
                clearDB();
                break;
        }
        return true;
    }

    private void clearDB() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_db_text)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSIL
                        // Drop older table if existed
                        mDatabase = getActivity().openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
                        mDatabase.execSQL("DELETE FROM " + TABLE_SHOPITEM);
                        //mDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ShopTransaction);
                        dialog.dismiss();
                        itemList.clear();
                        itemAdapter.setAllClearItem();
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Item> newItemList = new ArrayList<>();
        //itemList = itemAdapter.getItemList();
        if(newText != null) {
            for (Item item : itemList) {
                String pName = item.getpName();
                if (pName.contains(newText)) {
                    newItemList.add(item);
                }
            }
        }
        else{
            newItemList.addAll(itemList);
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
            itemList = itemAdapter.addNewItemEntry(newItem);
        }
    }
    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(itemList.get(position)))
                multiselect_list.remove(itemList.get(position));
            else
                multiselect_list.add(itemList.get(position));

            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else
                mActionMode.setTitle("");

            refreshAdapter();
        }
    }
    public void refreshAdapter()
    {
        itemAdapter.selected_itemList=multiselect_list;
        itemAdapter.itemList=itemList;
        itemAdapter.notifyDataSetChanged();
    }
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //mToolbar.setVisibility(View.GONE);
            //mToolbar.inflateMenu(R.menu.menu_multi_select);
            // Inflate a menu resource providing context menu items
            //MenuInflater inflater = mode.getMenuInflater();
            //inflater.inflate(R.menu.menu_multi_select, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    alertDialogHelper.showAlertDialog("","Delete Contact","DELETE","CANCEL",1,false);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<Item>();
            refreshAdapter();
            mToolbar.inflateMenu(R.menu.toolbar_menu_main);
        }
    };
    // AlertDialog Callback Functions

    @Override
    public void onPositiveClick(int from) {
        if(from==1)
        {
            if(multiselect_list.size()>0)
            {
                for(int i=0;i<multiselect_list.size();i++)
                    itemList.remove(multiselect_list.get(i));

                itemAdapter.notifyDataSetChanged();

                if (mActionMode != null) {
                    mActionMode.finish();
                }
                Toast.makeText(getContext(), "Delete Click", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onNegativeClick(int from) {

    }

    @Override
    public void onNeutralClick(int from) {

    }
}
