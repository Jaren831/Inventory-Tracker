package com.example.android.inventorytracker;

import android.content.Context;
import android.database.Cursor;
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

    private InventoryDbHelper mDBHelper;

    public InventoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //    // The newView method is used to inflate a new view and return it.
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.list_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // Find fields to populate in inflated template
        int pos = cursor.getPosition();
        mDBHelper = new InventoryDbHelper(context);
        TextView itemName = (TextView) view.findViewById(R.id.item_name);
        itemName.setTag(pos + 1);
        TextView itemPrice = (TextView) view.findViewById(R.id.item_price);
        itemName.setTag(pos + 1);
        TextView itemQuantity = (TextView) view.findViewById(R.id.item_quantity);
        itemQuantity.setTag(pos + 1);
        Button saleButton = (Button) view.findViewById(R.id.sale_button);
        saleButton.setTag(pos + 1);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null) {
                    Toast.makeText(context, String.valueOf(view.getTag()), Toast.LENGTH_SHORT).show();
                }
            }
        });

        String name = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME));
        String price = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE));
        String quantity = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY));

        itemName.setText(name);
        itemPrice.setText(price);
        itemQuantity.setText(quantity);
    }
}
