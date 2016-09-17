package com.example.android.inventorytracker;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.android.inventorytracker.Data.InventoryContract;
import com.example.android.inventorytracker.Data.InventoryDbHelper;

/**
 * Created by Jaren Lynch on 9/9/2016.
 */
public class EditorActivity extends AppCompatActivity {

    private Cursor cursor;

    private InventoryDbHelper mDbhelper;

    Button updateButton;
    Button orderButton;
    Button deleteButton;
    Button incrementQuantityButton;
    Button decrementQuantityButton;
    ImageButton imageButton;

    EditText nameEdit;
    EditText priceEdit;
    EditText quantityEdit;
    EditText addQuantityEdit;

    InventoryDbHelper mDbHelper;

    long itemId;

    Intent returnIntent;

    AlertDialog.Builder builder;
    Intent emailIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);

        updateButton = (Button) findViewById(R.id.update_button);
        orderButton = (Button) findViewById(R.id.order_button);
        deleteButton = (Button) findViewById(R.id.delete_button);
        incrementQuantityButton = (Button) findViewById(R.id.editIncrementButton);
        decrementQuantityButton = (Button) findViewById(R.id.editDecrementButton);
        imageButton = (ImageButton) findViewById(R.id.image_button);

        updateButton.setOnClickListener(onClickListener);
        orderButton.setOnClickListener(onClickListener);
        deleteButton.setOnClickListener(onClickListener);
        incrementQuantityButton.setOnClickListener(onClickListener);
        decrementQuantityButton.setOnClickListener(onClickListener);
        imageButton.setOnClickListener(onClickListener);

        nameEdit = (EditText) findViewById(R.id.editNameText);
        priceEdit = (EditText) findViewById(R.id.editPriceText);
        quantityEdit = (EditText) findViewById(R.id.editQuantityText);
        addQuantityEdit = (EditText) findViewById(R.id.editQuantityAddText);

        itemId = getIntent().getLongExtra("item_id", 0);
        createConfirm();
        returnIntent = new Intent(EditorActivity.this, InventoryActivity.class);
        displayDatabaseInfo(itemId);
    }

    // Adds onClickListener to confirm and cancel buttons.
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.update_button:
                    updateItem(itemId);
                    break;
                case R.id.order_button:
                    orderItem(itemId);
                    break;
                case R.id.delete_button:
                    builder.show();
                    break;
                case R.id.editIncrementButton:
                    incrementQuantity(itemId);
                    break;
                case R.id.editDecrementButton:
                    int currentQuantity = Integer.parseInt(quantityEdit.getText().toString());
                    if (currentQuantity > 0){
                        decrementQuantity(itemId);
                    }
                    break;
                case R.id.image_button:
                    editImage(itemId);
                    break;
            }
        }
    };

    private void displayDatabaseInfo(long rowId) {
        mDbhelper = new InventoryDbHelper(this);
        SQLiteDatabase db = mDbhelper.getReadableDatabase();
        String filter = "_ID=" + rowId;
        String[] columns = {
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE};

        cursor = db.query(InventoryContract.InventoryEntry.TABLE_NAME, columns, filter, null, null, null, null);

        if (cursor.moveToFirst()) {
            int itemNameIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
            String itemNameQuery = cursor.getString(itemNameIndex);
            nameEdit.setText(itemNameQuery);

            int itemPriceIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE);
            String itemPriceQuery = cursor.getString(itemPriceIndex);
            priceEdit.setText(itemPriceQuery);

            int itemQuantityIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            String itemQuantityQuery = cursor.getString(itemQuantityIndex);
            quantityEdit.setText(itemQuantityQuery);

            int itemPictureIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE);
            byte[] image = cursor.getBlob(itemPictureIndex);
            Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
            imageButton.setImageBitmap(bmp);


        }
        db.close();
    }

    private void updateItem(long rowId) {
        mDbhelper = new InventoryDbHelper(this);
        SQLiteDatabase db = mDbhelper.getWritableDatabase();
        String filter = "_ID=" + rowId;

        String itemNameUpdate = nameEdit.getText().toString();
        int itemPriceUpdate = Integer.valueOf(priceEdit.getText().toString());
        int itemQuantityUpdate = Integer.valueOf(quantityEdit.getText().toString());

        ContentValues updateValues = new ContentValues();
        updateValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, itemNameUpdate);
        updateValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE, itemPriceUpdate);
        updateValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, itemQuantityUpdate);

        db.update(InventoryContract.InventoryEntry.TABLE_NAME, updateValues, filter, null);
        db.close();
        startActivity(returnIntent);
    }
    private void orderItem(long rowId) {
        mDbhelper = new InventoryDbHelper(this);
        SQLiteDatabase db = mDbhelper.getReadableDatabase();
        String filter = "_ID=" + rowId;
        String[] columns = {InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY};
        cursor = db.query(InventoryContract.InventoryEntry.TABLE_NAME, columns, filter, null, null, null, null);

        if (cursor.moveToFirst()) {
            int itemNameIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
            String itemNameQuery = cursor.getString(itemNameIndex);
            nameEdit.setText(itemNameQuery);

            int itemQuantityIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            String itemQuantityQuery = cursor.getString(itemQuantityIndex);
            quantityEdit.setText(itemQuantityQuery);

            emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Ordering more " + itemNameQuery);
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Current stock: " + itemQuantityQuery);
            startActivity(emailIntent);
        }
        db.close();
    }
    private void deleteItem(long rowId) {
        mDbhelper = new InventoryDbHelper(this);
        SQLiteDatabase db = mDbhelper.getWritableDatabase();
        db.delete(InventoryContract.InventoryEntry.TABLE_NAME, "_ID" + "=" + rowId, null);
        db.close();
        startActivity(returnIntent);
    }
    private void incrementQuantity(long rowId) {
        mDbhelper = new InventoryDbHelper(this);
        SQLiteDatabase db = mDbhelper.getWritableDatabase();
        String filter = "_ID=" + rowId;
        int currentQuantity = Integer.parseInt(quantityEdit.getText().toString());
        int currentAddQuantity = Integer.parseInt(addQuantityEdit.getText().toString());
        int newQuantity = currentQuantity + currentAddQuantity;
        ContentValues updateValues = new ContentValues();
        updateValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, newQuantity);
        db.update(InventoryContract.InventoryEntry.TABLE_NAME, updateValues, filter, null);
        db.close();
        displayDatabaseInfo(rowId);
    }
    private void decrementQuantity(long rowId) {
        mDbhelper = new InventoryDbHelper(this);
        SQLiteDatabase db = mDbhelper.getWritableDatabase();
        String filter = "_ID=" + rowId;
        int currentQuantity = Integer.parseInt(quantityEdit.getText().toString());
        int currentAddQuantity = Integer.parseInt(addQuantityEdit.getText().toString());
        int newQuantity = 0;
        if ((currentQuantity - currentAddQuantity) >= 0) {
            newQuantity = (currentQuantity - currentAddQuantity);
        }
        ContentValues updateValues = new ContentValues();
        updateValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, newQuantity);
        db.update(InventoryContract.InventoryEntry.TABLE_NAME, updateValues, filter, null);
        db.close();
        displayDatabaseInfo(rowId);

    }
    private void createConfirm() {
        // Initializes alert dialog box. If yes, calls deleteItem method.
        builder = new AlertDialog.Builder(EditorActivity.this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete this item?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteItem(itemId);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create();
    }

    private void editImage(long rowId) {
        mDbhelper = new InventoryDbHelper(this);
        SQLiteDatabase db = mDbhelper.getWritableDatabase();
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");
        startActivityForResult(imageIntent, 0);
        Toast.makeText(EditorActivity.this, String.valueOf("uh"), Toast.LENGTH_SHORT).show();
        db.close();
    }
}
