package com.example.android.inventorytracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.android.inventorytracker.Data.InventoryContract;
import com.example.android.inventorytracker.Data.InventoryDbHelper;

public class InventoryActivity extends AppCompatActivity {

    private InventoryDbHelper mDbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_list);
        mDbhelper = new InventoryDbHelper(this);
        displayDatabaseInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        InventoryDbHelper mDbHelper = new InventoryDbHelper(this);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] project = {
            InventoryContract.InventoryEntry._ID,
                    InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                    InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE,
                    InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY
        };

        Cursor cursor = db.query(
                InventoryContract.InventoryEntry.TABLE_NAME,
                project,
                null,
                null,
                null,
                null,
                null);

        TextView
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
