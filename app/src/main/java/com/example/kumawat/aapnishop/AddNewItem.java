package com.example.kumawat.aapnishop;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import static android.support.constraint.Constraints.TAG;
import static java.security.AccessController.getContext;

public class AddNewItem extends AppCompatActivity {
    private static final int IMAGE_PICK = 1;
    private static final int IMAGE_CAPTURE = 2;
    private static final int MY_PERMISSIONS_REQUESTS = 3;
    private boolean isSaveCliacked = false;
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

        itemImage.isClickable();
        productName = product_name.getText().toString();
        productDetails = product_details.getText().toString();
        purchasePrice = purchase_price.getAlpha();
        customerPrice = customer_price.getAlpha();
        //boolean result = checkPermission();

        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getBaseContext(), "Clicked", Toast.LENGTH_SHORT).show();
                //if(checkPermission()) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, IMAGE_CAPTURE);
                //}
            }
        });
        itemImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Toast.makeText(getBaseContext(), "Long Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Photo"), IMAGE_PICK);
                return true;
            }
        });


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
                isSaveCliacked = true;
                if(addProduct()){
                    finish();
                }
            }
        });
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.CAMERA))
            {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission necessary");
                alertBuilder.setMessage("CAMERA is necessary");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) AddNewItem.this,
                                new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUESTS);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUESTS);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case IMAGE_PICK: this.imageFromGallery(resultCode, data);
                    break;
                case IMAGE_CAPTURE: this.imageFromCamera(resultCode, data);
                    break;
                default:
                    break;
            }

        }
    }
    private void imageFromCamera(int resultCode, Intent data) {
        this.itemImage.setImageBitmap((Bitmap) data.getExtras().get("data")); }

    private void imageFromGallery(int resultCode, Intent data) {
        Uri selectedImage = data.getData();
        String [] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        this.itemImage.setImageBitmap(BitmapFactory.decodeFile(filePath));
    }
    private boolean addProduct(){
        String pName = product_name.getText().toString().trim();
        String pDetails = product_details.getText().toString().trim();
        String purPrice = purchase_price.getText().toString().trim();
        String custPrice = customer_price.getText().toString().trim();
        //ImageView ivphoto = (ImageView)findViewById(R.id.userphoto);
        //code image to string
        itemImage.buildDrawingCache();
        Bitmap bitmap = itemImage.getDrawingCache();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();
        //System.out.println("byte array:"+image);
        //final String img_str = "data:image/png;base64,"+ Base64.encodeToString(image, 0);
        //System.out.println("string:"+img_str);
        String img_str = Base64.encodeToString(image, 0);

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
                    "(id, productName, productDetails, purchasePrice, customerPrice, itemimage) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?);";
            Log.e( "addProduct: addNewItem", "Making new entry");
            mDatabase.execSQL(insertSQL, new String[]{null, pName, pDetails, purPrice, custPrice, img_str});
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        if(isSaveCliacked){
            Intent returnIntent = new Intent();
            returnIntent.putExtra("passed_item", new_item);
            // setResult(RESULT_OK);
            setResult(RESULT_OK, returnIntent); //By not passing the intent in the result, the calling activity will get null data.
            Log.d(TAG, "add new item.finish");
        }
        super.finish();
    }
}
