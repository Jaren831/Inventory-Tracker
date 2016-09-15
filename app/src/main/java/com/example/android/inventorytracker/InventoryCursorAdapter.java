package com.example.android.inventorytracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventorytracker.Data.InventoryContract;
import com.example.android.inventorytracker.Data.InventoryDbHelper;

/**
 * Created by Jaren Lynch on 9/10/2016.
 */
public class InventoryCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;

    InventoryDbHelper mDbHelper;

    Button saleButton;

    public InventoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //The newView method is used to inflate a new view and return it.
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.list_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        saleButton = (Button) view.findViewById(R.id.sale_button);

        // Find fields to populate in inflated template
        TextView itemName = (TextView) view.findViewById(R.id.item_name);
        TextView itemPrice = (TextView) view.findViewById(R.id.item_price);
        final TextView itemQuantity = (TextView) view.findViewById(R.id.item_quantity);

        String name = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME));
        String price = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE));
        final String quantity = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY));

        itemName.setText(name);
        itemPrice.setText(price);
        itemQuantity.setText(quantity);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDbHelper = new InventoryDbHelper(context);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                String rowId = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry._ID));
                String filter = "_ID=" + rowId;
                int saleCurrentQuantity = Integer.parseInt(quantity);
                if (saleCurrentQuantity > 0) {
                    int saleNewQuantity = saleCurrentQuantity - 1;
                    ContentValues updateValues = new ContentValues();
                    updateValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, saleNewQuantity);
                    db.update(InventoryContract.InventoryEntry.TABLE_NAME, updateValues, filter, null);
                    notifyDataSetInvalidated();
                }
                db.close();
            }
        });

    }

}
