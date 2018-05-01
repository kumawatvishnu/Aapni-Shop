package com.example.kumawat.aapnishop;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.nfc.Tag;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static android.support.constraint.Constraints.TAG;

public class AddNewItem extends AppCompatActivity {
    SQLiteDatabase mDatabase;
    ImageView itemImage;
    Button button_cancel;
    Button button_save;
    EditText product_name, product_details, purchase_price, customer_price;
    String productName = null;
    String  productDetails = null;
    double purchasePrice;
    double customerPrice;
    Item new_item;
    //ItemAdapter itemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        itemImage = (ImageView)findViewById(R.id.item_image);
        product_name = (EditText)findViewById(R.id.product_name);
        product_details = (EditText)findViewById(R.id.product_details);
        purchase_price = (EditText)findViewById(R.id.purchase_price);
        customer_price = (EditText)findViewById(R.id.customer_price);
        button_cancel = (Button)findViewById(R.id.button_cancel);
        button_save = (Button)findViewById(R.id.button_save);

        productName = product_name.getText().toString();
        productDetails = product_details.getText().toString();
        purchasePrice = purchase_price.getAlpha();
        customerPrice = customer_price.getAlpha();

        //Creating Database
        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addProduct()){
                    finish();
                }
            }
        });
    }
    private boolean addProduct(){
        String pName = product_name.getText().toString().trim();
        String pDetails = product_details.getText().toString().trim();
        String purPrice = purchase_price.getText().toString().trim();
        String custPrice = customer_price.getText().toString().trim();
        if (pName.isEmpty()) {
            product_name.setError("Please Enter Product Name...");
            product_name.requestFocus();
            return false;
        }
        if (purPrice.isEmpty()) {
            purchase_price.setError("Please Enter Price...");
            purchase_price.requestFocus();
            return false;
        }
        if (custPrice.isEmpty()) {
            customer_price.setError("Please Enter Price...");
            customer_price.requestFocus();
            return false;
        }
        double purchasePrice = Double.parseDouble(purPrice);
        double customerPrice = Double.parseDouble(custPrice);
        new_item = new Item(0, pName, pDetails, purchasePrice, customerPrice);
        if(inputsAreCorrect(pName, purPrice, custPrice)){
            String insertSQL = "INSERT INTO ShopItem " +
                    "(id, productName, productDetails, purchasePrice, customerPrice) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?);";
            Log.e( "addProduct: addNewItem", "Making new entry");
            mDatabase.execSQL(insertSQL, new String[]{null, pName, pDetails, purPrice, custPrice});
            Toast.makeText(this, pName+" Item Added Successfully", Toast.LENGTH_LONG).show();
        }
        else{
            return false;
        }
        return true;
    }
    //this method will validate name and price
    private boolean inputsAreCorrect(String name, String purPrice, String custPrice) {
        if (name.isEmpty()) {
            product_name.setError("Please Enter A Name...");
            product_name.requestFocus();
            return false;
        }

        if (Double.parseDouble(String.valueOf(purchasePrice))<= 0.0) {
            purchase_price.setError("Please Enter Price....");
            purchase_price.requestFocus();
            return false;
        }
        if (Double.parseDouble(String.valueOf(customerPrice))<= 0.0) {
            customer_price.setError("Please Enter Price....");
            customer_price.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void finish() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("passed_item", new_item);
        // setResult(RESULT_OK);
        setResult(RESULT_OK, returnIntent); //By not passing the intent in the result, the calling activity will get null data.
        Log.d(TAG, "add new item.finish");
        super.finish();
    }
}
