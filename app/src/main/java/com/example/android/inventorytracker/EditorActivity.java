package com.example.android.inventorytracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.inventorytracker.Data.InventoryContract;
import com.example.android.inventorytracker.Data.InventoryDbHelper;

/**
 * Created by Jaren Lynch on 9/9/2016.
 */
public class EditorActivity extends AppCompatActivity {

    private Cursor cursor;

    private InventoryDbHelper mDbhelper;

    Button updateButton;
    Button orderButton;
    Button deleteButton;
    Button incrementQuantityButton;
    Button decrementQuantityButton;

    EditText nameEdit;
    EditText priceEdit;
    EditText quantityEdit;
    EditText addQuantityEdit;

    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);

        updateButton = (Button) findViewById(R.id.update_button);
        orderButton = (Button) findViewById(R.id.order_button);
        deleteButton = (Button) findViewById(R.id.delete_button);
        incrementQuantityButton = (Button) findViewById(R.id.editIncrementButton);
        decrementQuantityButton = (Button) findViewById(R.id.editDecrementButton);

        updateButton.setOnClickListener(onClickListener);
        orderButton.setOnClickListener(onClickListener);
        deleteButton.setOnClickListener(onClickListener);
        incrementQuantityButton.setOnClickListener(onClickListener);
        decrementQuantityButton.setOnClickListener(onClickListener);

        nameEdit = (EditText) findViewById(R.id.editNameText);
        priceEdit = (EditText) findViewById(R.id.editPriceText);
        quantityEdit = (EditText) findViewById(R.id.editQuantityText);
        addQuantityEdit = (EditText) findViewById(R.id.editQuantityAddText);

        displayDatabaseInfo();
    }

    // Adds onClickListener to confirm and cancel buttons.
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.update_button:
                    updateItem();
                    break;
                case R.id.order_button:
                    orderItem();
                    break;
                case R.id.delete_button:
                    deleteItem();
                    break;
                case R.id.editIncrementButton:
                    incrementQuantity();
                    break;
                case R.id.editDecrementButton:
                    decrementQuantity();
                    break;
            }
        }
    };

    private void displayDatabaseInfo() {
        mDbhelper = new InventoryDbHelper(this);

        SQLiteDatabase db = mDbhelper.getWritableDatabase();

        cursor = db.query(InventoryContract.InventoryEntry.TABLE_NAME,
                new String[] {InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME},
                InventoryContract.InventoryEntry._ID,
                new String [] { getIntent().getStringExtra("item_id") },
                null,
                null,
                null);


    }

    private void updateItem() {

    }
    private void orderItem() {

    }
    private void deleteItem() {

    }
    private void incrementQuantity() {

    }
    private void decrementQuantity() {

    }
}
