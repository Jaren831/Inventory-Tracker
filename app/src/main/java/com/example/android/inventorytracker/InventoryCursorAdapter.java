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
import android.widget.Toast;

import com.example.android.inventorytracker.Data.InventoryContract;
import com.example.android.inventorytracker.Data.InventoryDbHelper;

/**
 * Created by Jaren Lynch on 9/10/2016.
 */
public class InventoryCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;

    InventoryDbHelper mDbHelper;

    Button saleButton;

    TextView quantitySale;

    public InventoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //    // The newView method is used to inflate a new view and return it.
    @Override
    public View newView(final Context context, Cursor cursor, final ViewGroup parent) {
        View row = cursorInflater.inflate(R.layout.list_item, parent, false);
        saleButton = (Button) row.findViewById(R.id.sale_button);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDbHelper = new InventoryDbHelper(context);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                String filter = "_ID=" + saleButton.getTag();
                quantitySale = (TextView) parent.findViewById(R.id.item_quantity);
                int itemQuantity = Integer.valueOf(quantitySale.getText().toString());
                int itemQuantityUpdate = itemQuantity - 1;

                ContentValues updateValues = new ContentValues();
                updateValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, itemQuantityUpdate);

                db.update(InventoryContract.InventoryEntry.TABLE_NAME, updateValues, filter, null);

//
//
                Toast.makeText(context, String.valueOf(updateValues), Toast.LENGTH_SHORT).show();
//                db.close();
            }
        });
        return row;
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        int pos = cursor.getPosition();
        saleButton = (Button) view.findViewById(R.id.sale_button);

        // Find fields to populate in inflated template
        TextView itemName = (TextView) view.findViewById(R.id.item_name);
        TextView itemPrice = (TextView) view.findViewById(R.id.item_price);
        final TextView itemQuantity = (TextView) view.findViewById(R.id.item_quantity);

        String name = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME));
        String price = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE));
        final String quantity = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY));
        itemQuantity.setTag(pos + 1);

        itemName.setText(name);
        itemPrice.setText(price);
        itemQuantity.setText(quantity);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDbHelper = new InventoryDbHelper(context);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                long rowId = Long.valueOf(itemQuantity.getTag().toString());
                String filter = "_ID=" + rowId;
                int saleCurrentQuantity = Integer.valueOf(itemQuantity.getText().toString());
                if (saleCurrentQuantity > 0) {
                    int saleNewQuantity = saleCurrentQuantity - 1;
                    ContentValues updateValues = new ContentValues();
                    updateValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, saleNewQuantity);
                    db.update(InventoryContract.InventoryEntry.TABLE_NAME, updateValues, filter, null);
                    itemQuantity.setText(String.valueOf(saleNewQuantity));
                }
                Toast.makeText(context, String.valueOf(rowId), Toast.LENGTH_SHORT).show();
                db.close();
            }
        });



    }

}
