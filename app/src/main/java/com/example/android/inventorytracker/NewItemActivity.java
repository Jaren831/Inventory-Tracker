package com.example.android.inventorytracker;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.android.inventorytracker.Data.InventoryContract;
import com.example.android.inventorytracker.Data.InventoryDbHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

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
    ImageButton imageButton;

    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newitem_activity);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        imageButton = (ImageButton) findViewById(R.id.pictureButton);
        confirmButton.setOnClickListener(onClickListener);
        cancelButton.setOnClickListener(onClickListener);
        imageButton.setOnClickListener(onClickListener);


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
                case R.id.pictureButton:
                    insertPicture();

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
        if (isEmpty(itemNameEdit) && isEmpty(itemPriceEdit) && isEmpty(itemQuantityEdit) && bmp != null) {
            String itemNameInput = itemNameEdit.getText().toString();
            int itemPriceInput = Integer.valueOf(itemPriceEdit.getText().toString());
            int itemQuantityInput = Integer.valueOf(itemQuantityEdit.getText().toString());

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] bArray = byteArrayOutputStream.toByteArray();

            ContentValues values = new ContentValues();

            values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, itemNameInput);
            values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE, itemPriceInput);
            values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, itemQuantityInput);
            values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE, bArray);
            long newRowId = db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);
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

    private void insertPicture() {
        mDbhelper = new InventoryDbHelper(this);
        SQLiteDatabase db = mDbhelper.getWritableDatabase();
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");
        startActivityForResult(imageIntent, 1);
        Toast.makeText(NewItemActivity.this, String.valueOf("uh"), Toast.LENGTH_SHORT).show();
        db.close();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bmp = Bitmap.createScaledBitmap(bmp, imageButton.getWidth(), imageButton.getHeight(), false);
            imageButton.setImageBitmap(bmp);
    }
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
