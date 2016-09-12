package com.example.android.inventorytracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.inventorytracker.Data.InventoryContract;
import com.example.android.inventorytracker.Data.InventoryDbHelper;

/**
 * Created by Jaren Lynch on 9/10/2016.
 */
public class NewItemActivity extends AppCompatActivity{

    InventoryDbHelper mDbhelper;
    Button confirmButton;
    Button cancelButton;
    EditText itemNameEdit;
    EditText itemPriceEdit;
    EditText itemQuantityEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newitem_activity);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        confirmButton.setOnClickListener(onClickListener);
        cancelButton.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.confirmButton:
                    insertItem();
                    break;
                case R.id.cancelButton:
                    cancelItem();
                    break;
            }
        }
    };

    public void insertItem() {
        mDbhelper = new InventoryDbHelper(this);

        SQLiteDatabase db = mDbhelper.getWritableDatabase();

        itemNameEdit = (EditText) findViewById(R.id.newNameText);
        itemPriceEdit = (EditText)findViewById(R.id.newPriceText);
        itemQuantityEdit = (EditText) findViewById(R.id.newQuantityText);

        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, itemNameEdit.getText().toString());
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE, Integer.valueOf(itemPriceEdit.getText().toString()));
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, Integer.valueOf(itemQuantityEdit.getText().toString()));

        long newRowId = db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);
        Log.v("InventoryActivity", "New row ID " + newRowId);
        Intent returnIntent = new Intent(NewItemActivity.this, InventoryActivity.class);
        startActivity(returnIntent);
    }

    public void cancelItem() {

    }

}
