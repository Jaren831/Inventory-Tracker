package com.example.android.inventorytracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Jaren Lynch on 9/9/2016.
 */
public class EditorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);
        String itemId = getIntent().getStringExtra("id");
        TextView test = (TextView) findViewById(R.id.editorTest);
        test.setText(itemId);
    }
}
