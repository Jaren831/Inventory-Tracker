package com.example.android.inventorytracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.inventorytracker.Data.InventoryContract;
import com.example.android.inventorytracker.Data.InventoryDbHelper;

import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    private InventoryDbHelper mDbhelper;

    //Adapter for the list of items
    private InventoryAdapter mAdapter;

    //ListView for the list of items
    ListView itemListView;

    //Empty TextView if nothing to show
    TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_list);

        //Find a reference to the ListView in the layout.
        itemListView = (ListView) findViewById(R.id.inventory_list);

        //Create a new adapter that takes an empty list of articles as input.
        mAdapter = new InventoryAdapter(this, new ArrayList<InventoryItem>());

        //Set the adapter on the ListView.
        itemListView.setAdapter(mAdapter);

        //Set the TextView with id empty to an empty view.
        emptyTextView = (TextView) findViewById(R.id.empty);
        itemListView.setEmptyView(emptyTextView);

        //Setup FAB to open editorActivity
        

        //Setup onClickListener for when an item is clicked
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Find the item that was clicked on
                InventoryItem currentItem = mAdapter.getItem(position);

                //Create a new intent to view the EditorActivity.
                Intent editorIntent = new Intent(InventoryActivity.this, EditorActivity.class);
                startActivity(editorIntent);
            }
        });

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

        List<InventoryItem> inventoryItems = new ArrayList<>();

        try {
            //Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY);

            //Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                //Use the index to extract the String or Int value at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                inventoryItems.add(new InventoryItem(currentName, currentPrice, currentQuantity));

            }
        }
        finally {
            cursor.close();
        }

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
