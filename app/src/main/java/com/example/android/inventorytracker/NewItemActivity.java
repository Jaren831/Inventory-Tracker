package com.example.android.inventorytracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.inventorytracker.Data.InventoryContract;
import com.example.android.inventorytracker.Data.InventoryDbHelper;

/**
 * Created by Jaren Lynch on 9/10/2016.
 */
public class NewItemActivity extends AppCompatActivity{

    InventoryDbHelper mDbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newitem_activity);
    }
    public void insertItem() {
        mDbhelper = new InventoryDbHelper(this);

        SQLiteDatabase db = mDbhelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, "TEST");
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE, 100);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, 100);

        long newRowId = db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);
        Log.v("InventoryActivity", "New row ID " + newRowId);
    }

}
