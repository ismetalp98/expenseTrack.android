package com.example.myapplication.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication.R;
import com.example.myapplication.Resources.Expense;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class Add extends AppCompatActivity {

    private EditText et_price,et_content;
    private Button btn_add,btn_save;
    private RadioGroup radioGroup;
    private SharedPreferences mPrefs;
    private ArrayList<Expense> mExpenses ;
    private int addcount = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        mPrefs = getSharedPreferences("shared preferences",MODE_PRIVATE);
        getExpenses();



        radioGroup = findViewById(R.id.radiogroup);

        et_price = findViewById(R.id.et_price);
        et_content = findViewById(R.id.et_content);

        btn_add = findViewById(R.id.btn_add);
        btn_save = findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToFile();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public String getrbText(int rbId)
    {
        View radioButton = radioGroup.findViewById(rbId);
        int idx = radioGroup.indexOfChild(radioButton);
        RadioButton r = (RadioButton) radioGroup.getChildAt(idx);
        return r.getText().toString();
    }

    public void  writeToFile()
    {
        /*String pattern = "MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());*/

        SharedPreferences.Editor prefedit = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mExpenses);
        prefedit.putString("myExpenses",json);
        prefedit.apply();

    }

    public void getExpenses() {

        Gson gson = new Gson();
        String json = mPrefs.getString("myExpenses", null);
        Type type = new TypeToken<ArrayList<Expense>>() {
        }.getType();
        mExpenses = gson.fromJson(json, type);

        if (json == null) {
            mExpenses = new ArrayList<>();
        }
    }

    public void addToArray()
    {
        String pattern = "dd:MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());

        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        String content = et_content.getText().toString().trim().toLowerCase();
        String catagory = getrbText(radioButtonID);

        double price = Double.valueOf(et_price.getText().toString());

        Expense expense = new Expense(content,catagory,price,date);
        mExpenses.add(expense);
        addcount++;
        btn_add.setText("Add: " + addcount);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), Main.class);
        startActivity(intent);
        finish();
    }
}
