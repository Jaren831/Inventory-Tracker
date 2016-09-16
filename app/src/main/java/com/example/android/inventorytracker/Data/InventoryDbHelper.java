package com.example.android.inventorytracker.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jaren Lynch on 9/9/2016.
 */
public class InventoryDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = InventoryDbHelper.class.getSimpleName();

    //Name of database file
    private static final String DATABASE_NAME = "store.db";

    //Database version. If database schema is changed, must increment database version.
    private static final int DATABASE_VERSION = 1;


    /**
     * Constructs a new instance of PetDbHelper
     * @param context of the app
     */
    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a string that contains the SQL statement to create the pets table
        String SQL_CREATE_INVENTORY_TABLE_TABLE = "CREATE TABLE " + InventoryContract.InventoryEntry.TABLE_NAME + " ("
                + InventoryContract.InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, "
                + InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL);"
                + InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE + " BLOB NOT NULL";

        //Execute the SQL statement
        db.execSQL(SQL_CREATE_INVENTORY_TABLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
