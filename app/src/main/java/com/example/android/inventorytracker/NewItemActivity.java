package com.example.android.inventorytracker;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newitem_activity);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        confirmButton.setOnClickListener(onClickListener);
        cancelButton.setOnClickListener(onClickListener);

        // Initializes alert dialog box.
        builder = new AlertDialog.Builder(NewItemActivity.this);
        builder.setTitle("Input Error");
        builder.setMessage("No input field can be blank.");
        builder.setCancelable(false);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
    }

    // Adds onClickListener to confirm and cancel buttons.
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

        // Makes database writable.
        SQLiteDatabase db = mDbhelper.getWritableDatabase();

        itemNameEdit = (EditText) findViewById(R.id.newNameText);
        itemPriceEdit = (EditText)findViewById(R.id.newPriceText);
        itemQuantityEdit = (EditText) findViewById(R.id.newQuantityText);

        // Checks if input fields are empty.
        // If input present, adds input to database.
        if (isEmpty(itemNameEdit) && isEmpty(itemPriceEdit) && isEmpty(itemQuantityEdit)) {
            String itemNameInput = itemNameEdit.getText().toString();
            int itemPriceInput = Integer.valueOf(itemPriceEdit.getText().toString());
            int itemQuantityInput = Integer.valueOf(itemQuantityEdit.getText().toString());

            ContentValues values = new ContentValues();

            values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, itemNameInput);
            values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE, itemPriceInput);
            values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, itemQuantityInput);

            long newRowId = db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);
            Log.v("InventoryActivity", "New row ID " + newRowId);
            Intent returnIntent = new Intent(NewItemActivity.this, InventoryActivity.class);
            startActivity(returnIntent);
        } else {
            // If input fields are empty, displays error alert.
            AlertDialog error = builder.create();
            error.show();
        }
        db.close();
    }

    // Method to return to InventoryActivity and not save inputs if cancel button is pressed.
    public void cancelItem() {
        Intent cancelIntent = new Intent(NewItemActivity.this, InventoryActivity.class);
        startActivity(cancelIntent);
    }

    // Method to check if input fields are empty and return true or false.
    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() != 0;
    }


}
