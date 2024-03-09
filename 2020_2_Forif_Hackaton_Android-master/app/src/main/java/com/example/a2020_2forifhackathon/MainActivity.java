package com.example.a2020_2forifhackathon;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    EditText editCity;
    ImageButton imgBtn1; // 검색 버튼
    ImageButton imgBtn2; // login 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        // To-do 1 검색 기능
        editCity = findViewById(R.id.editCity);
        imgBtn2 = (ImageButton)findViewById(R.id.imageButton2);
        imgBtn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String city = editCity.getText().toString();

                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("CITY", city);
                startActivity(intent);
            }
        });

        // login 화면 이동
        imgBtn1 = (ImageButton)findViewById(R.id.imageButton3);
        imgBtn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }



}