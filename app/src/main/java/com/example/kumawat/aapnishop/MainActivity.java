package com.example.kumawat.aapnishop;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.SearchManager;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    public static final String DATABASE_NAME = "myshop_database";
    public static final String TABLE_ShopTransaction = "ShopTransaction";
    public static final String TABLE_SHOPITEM = "ShopItem";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_pName = "productName";
    public static final String COLUMN_pDetails = "productDetails";
    public static final String COLUMN_purPrice = "purchasePrice";
    public static final String COLUMN_custPrice = "customerPrice";
    public static int REQUEST_CODE = 1;
    private boolean backPressToExit = false;
    CoordinatorLayout coordinatorLayout;

    BottomNavigationView bottomNavigationView;
    Fragment fragment = null;
    FragmentTransaction fragmentTransaction;
    private TextView mTextMessage;
    //private Toolbar mToolbar;
    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        bottomNavigationView = findViewById(R.id.navigationView);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //Log.e("onCreate: Database", "creating");
        //Creating Database
        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        // Drop older table if existed
        //mDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPITEM);
        //mDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ShopTransaction);
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS ShopItem (\n" +
                        " id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        " productName varchar(200) NOT NULL DEFAULT 'not available',\n" +
                        " productDetails varchar(400) NOT NULL DEFAULT 'not available',\n" +
                        " purchasePrice double NOT NULL DEFAULT 0, \n" +
                        " customerPrice double NOT NULL DEFAULT 0, \n" +
                        " itemimage varchar \n" +
                        ");"
        );
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS ShopTransaction (\n" +
                        " id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        " trAmount INTEGER NOT NULL DEFAULT 0,\n" +
                        " trDetails varchar(400) NOT NULL DEFAULT 'not available',\n" +
                        " trType varchar(20) NOT NULL\n" +
                        ");"
        );

        /*String insertSQL = "INSERT INTO ShopItem (id, productName, productDetails, purchasePrice, customerPrice) VALUES (null, \"pipe\", \"PVC plastic\", 20, 25);";
        mDatabase.execSQL(insertSQL);
        insertSQL = "INSERT INTO ShopItem (id, productName, productDetails, purchasePrice, customerPrice) VALUES (null, \"pipe2\", \"PVC plastic2\", 20, 25);";
        mDatabase.execSQL(insertSQL);
        insertSQL = "INSERT INTO ShopItem (id, productName, productDetails, purchasePrice, customerPrice) VALUES (null, \"pipe3\", \"PVC plastic3\", 20, 25);";
        mDatabase.execSQL(insertSQL);
        insertSQL = "INSERT INTO ShopItem (id, productName, productDetails, purchasePrice, customerPrice) VALUES (null, \"pipe4\", \"PVC plastic4\", 20, 25);";
        mDatabase.execSQL(insertSQL);
        insertSQL = "INSERT INTO ShopItem (id, productName, productDetails, purchasePrice, customerPrice) VALUES (null, \"pipe5\", \"PVC plastic5\", 20, 25);";
        mDatabase.execSQL(insertSQL);
        insertSQL = "INSERT INTO ShopItem (id, productName, productDetails, purchasePrice, customerPrice) VALUES (null, \"pipe6\", \"PVC plastic6\", 20, 25);";
        mDatabase.execSQL(insertSQL);
        insertSQL = "INSERT INTO ShopItem (id, productName, productDetails, purchasePrice, customerPrice) VALUES (null, \"pipe7\", \"PVC plastic7\", 20, 25);";
        mDatabase.execSQL(insertSQL);

        insertSQL = "INSERT INTO ShopTransaction (id, trAmount, trDetails, trType) VALUES (null, 50, \"PVC pipe\", \"Received\");";
        mDatabase.execSQL(insertSQL);
        insertSQL = "INSERT INTO ShopTransaction (id, trAmount, trDetails, trType) VALUES (null, 60, \"PVC pipe2\", \"Paid\");";
        mDatabase.execSQL(insertSQL);
        insertSQL = "INSERT INTO ShopTransaction (id, trAmount, trDetails, trType) VALUES (null, 70, \"PVC pipe3\", \"Udhar\");";
        mDatabase.execSQL(insertSQL);
        insertSQL = "INSERT INTO ShopTransaction (id, trAmount, trDetails, trType) VALUES (null, 80, \"PVC pipe4\", \"Received\");";
        mDatabase.execSQL(insertSQL);*/
        mDatabase.close();
        LoadFragment(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;

            case R.id.navigation_transaction:
                Intent intent = new Intent(this, AddTransaction.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;

            case R.id.navigation_history:
                fragment = new HistoryFragment();
                break;

            case R.id.navigation_summary:
                fragment = new SummaryFragment();
                break;
            case R.id.my_account:
                fragment = new MyAccountFragment();
                break;
        }
        return LoadFragment(fragment);
    }
    private boolean LoadFragment(Fragment fragment) {
        if(fragment != null){
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            Transaction newTransaction = (Transaction) data.getExtras().get("passed_transaction");
            // deal with the item yourself
            //itemAdapter.addNewItemEntry(newItem);
            LoadFragment(new HistoryFragment());
        }
    }
    @Override
    public void onBackPressed() {

        if (backPressToExit) {
            super.onBackPressed();
            return;
        }
        this.backPressToExit = true;
        Snackbar snack = Snackbar.make(findViewById(R.id.coordinatorLayout),
                R.string.exit_msg, Snackbar.LENGTH_LONG);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                snack.getView().getLayoutParams();
        params.setMargins(0, 0, 0, bottomNavigationView.getHeight());
        snack.getView().setLayoutParams(params);
        snack.show();
        LoadFragment(new HomeFragment());
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                backPressToExit = false;
            }
        }, 2000);
    }

    /*public boolean isCurrentFragment(Fragment fragment) {
        if (fragment instanceof HomeFragment && getLastAddedFragment() instanceof HomeFragment) { // getLastAddedFragment() is a method which return the last added fragment instance
            return true;
        } else
            return false;
    }*/
}
