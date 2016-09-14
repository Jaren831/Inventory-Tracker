package com.example.android.inventorytracker.Data;

import android.provider.BaseColumns;

/**
 * Created by Jaren Lynch on 9/9/2016.
 */
public final class InventoryContract {

    private InventoryContract () {}

    public static final class InventoryEntry implements BaseColumns {

        public final static String TABLE_NAME = "inventory";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME = "product";
        public final static String COLUMN_PRODUCT_PRICE = "price";
        public final static String COLUMN_PRODUCT_QUANTITY = "quantity";
    }
}
