package com.example.a2020_2forifhackathon;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Weather {

    static class weatherChecker extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... address) {
            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //connect to URL
                connection.connect();

                //Retrieving Data
                InputStream streamIn = connection.getInputStream();
                InputStreamReader streamReader = new InputStreamReader(streamIn);

                int data = streamReader.read();
                StringBuilder weatherContent = new StringBuilder();

//sting을 쌓는 것 string data + string data or char data + char data >>new one

                while (data != -1) {//data가 읽어올게 없을때 -1을 출력하기 때문
                    char ch = (char) data;
                    weatherContent.append(ch); //data를 weathercontent에 붙임
                    data = streamReader.read();//streamReader에 온전한 데이터를 들고와서 읽음

                }
                return weatherContent.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

    }


    public String[] callWeatherData(String city) {
        String url = "https://openweathermap.org/data/2.5/weather?q=" + city + "&appid=439d4b804bc8187953eb36d2a8c26a02";
        weatherChecker weatherChecker = new weatherChecker();
        try {

            String dataReceived = weatherChecker.execute(url).get();
            //asynctask를 선언하고 한 작업을 실행시키는 것,content 가 string으로 가서 url으로 인식됨

            JSONObject jsonObject = new JSONObject(dataReceived);

            String weatherInfo = jsonObject.getString("weather");

            JSONArray arrayInfo = new JSONArray(weatherInfo);
            String iconInfo = "";

            for (int i = 0; i < arrayInfo.length(); i++) {
                JSONObject dataFromArray = arrayInfo.getJSONObject(i);
                iconInfo = dataFromArray.getString("icon");
            }

            //main info
//            iconInfo = jsonObject.getString("icon");
            String main = iconInfo.substring(0, 2);
            String maininfo = "";

            if (main.equals("01")) {
                maininfo = "0";
            }//맑음

            if (main.equals("02") || main.equals("03") || main.equals("04")) {
                maininfo = "1";
            }//흐림

            if (main.equals("09") || main.equals("10") || main.equals("11")) {
                maininfo = "2";
            }//비

            if (main.equals("13")) {
                maininfo = "3";
            }//눈

            if (main.equals("50")) {
                maininfo = "4";
            }//안개

            //main info
            JSONObject Main = jsonObject.getJSONObject("main");
            String tempData = Main.getString("temp");
            return new String[]{city, tempData + '\u2103', maininfo};
            //도시 이름,기온,날씨 정보
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
