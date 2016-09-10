package com.example.android.inventorytracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jaren Lynch on 9/9/2016.
 */
public class InventoryAdapter extends ArrayAdapter<InventoryItem> {
    public InventoryAdapter (Context context, ArrayList<InventoryItem> items) {
        super(context, 0, items);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
             listItemView = LayoutInflater.from(getContext()).inflate(
                     R.layout.list_item, parent, false);
        }
        InventoryItem currentInventoryItem = getItem(position);

        //Find the TextView with the id item_name
        TextView itemNameView = (TextView) listItemView.findViewById(R.id.item_name);
        //Display item name in TextView item_name
        itemNameView.setText(currentInventoryItem.getItemName());

        //Find the TextView with id item_price
        TextView itemPriceView = (TextView) listItemView.findViewById(R.id.item_price);
        //Display item price in TextView item_price
        itemPriceView.setText(currentInventoryItem.getItemPrice());

        //Find the TextView with id item_quantity
        TextView itemQuantityView = (TextView) listItemView.findViewById(R.id.item_quantity);
        //Display item quantity in TextView item_quantity
        itemQuantityView.setText(currentInventoryItem.getItemStock());

        return listItemView;
    }
}
