package com.example.a2020_2forifhackathon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class ResultActivity extends AppCompatActivity {
    ImageView imageViewBack;
    TextView textViewCity, textViewTemp, textViewWeather;
    ImageButton imageButtonRefresh, imageButtonWrite;
    Translator translator = null;
    Weather weather = null;
    String[] weatherData = {"맑음", "흐림", "비", "눈", "안개"};
    String[] oldData = {"o1", "o2", "o3"}, newData = {"n1", "n2", "n3"};
    int[] backgroundIDs = {R.drawable.sunny, R.drawable.cloudy, R.drawable.rainy, R.drawable.snowy, R.drawable.foggy};
    String inputText = "서울";//인텐트로 받아올 도시

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_result);

        ///
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(String.format("TEXT %d", i));
        }
        /// 이거 빼고 데이터 넣어야해요!!!  -->  데이터 형식도 바꿔 주셔야 합니다.

        MyAdapter adapter = new MyAdapter(list);
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViews();
        translator = new Translator();
        weather = new Weather();
        //inputText=인텐트!!!!!!!!!!!
        updateWeather(inputText);

        imageButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWeather(inputText);
            }
        });

        imageButtonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this,PostActivity.class);
                startActivity(intent);
            }
        });

    }

    void updateWeather(String inputText) {
        String city = translator.translation(inputText);
        newData = weather.callWeatherData(city);
        if (newData != null && !Arrays.equals(oldData, newData)) {
            textViewCity.setText("지금 " + inputText + " 날씨는");
            textViewTemp.setText(newData[1]);//기온
            textViewWeather.setText(weatherData[Integer.parseInt(newData[2])]);//날씨 정보 설정
            imageViewBack.setImageResource(backgroundIDs[Integer.parseInt(newData[2])]);//배경 바꾸기
            oldData = newData;
        }
    }

    void findViews() {
        imageViewBack = findViewById(R.id.ImageViewBack);
        textViewCity = findViewById(R.id.textViewCity);
        textViewTemp = findViewById(R.id.textViewTemperature);
        textViewWeather = findViewById(R.id.textViewWeather);
        imageButtonRefresh = findViewById(R.id.imageButtonRefresh);
        imageButtonWrite = findViewById(R.id.imageButtonWrite);
    }

}