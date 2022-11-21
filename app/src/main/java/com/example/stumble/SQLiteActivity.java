package com.example.stumble;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SQLiteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEdit, typeEdit, latEdit, longEdit;
    private String nameVal, typeVal, latVal, longVal;
    private Button addButton, removeButton;
    private MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        nameEdit = (EditText) findViewById(R.id.nameEditText);
        typeEdit = (EditText) findViewById(R.id.typeEditText);
        latEdit = (EditText) findViewById(R.id.latitudeEditText);
        longEdit = (EditText) findViewById(R.id.longitudeEditText);

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        removeButton = (Button) findViewById(R.id.removeButton);
        removeButton.setOnClickListener(this);
        db = new MyDatabase(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addButton:
                if (nameEdit.getText() == null) {
                    nameVal = "empty";
                }
                else {
                    nameVal = nameEdit.getText().toString();
                }
                if (typeEdit.getText() == null) {
                    typeVal = "event";
                }
                else {
                    typeVal = typeEdit.getText().toString();
                }
                if (latEdit.getText() == null) {
                    latVal = "0";
                }
                else {
                    latVal = latEdit.getText().toString();
                }
                if (longEdit.getText() == null) {
                    longVal = "0";
                }
                else {
                    longVal = longEdit.getText().toString();
                }
                long id = db.insertData(nameVal, typeVal, latVal + "," + longVal);
                if (id < 0) {
                    Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "added to database", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.removeButton:
                if (nameEdit.getText() == null) {
                    break;
                }
                else {
                    db.deleteRowByName(nameEdit.getText().toString());
                }
                break;
        }
    }
}