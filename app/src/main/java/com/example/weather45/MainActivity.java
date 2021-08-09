package com.example.weather45;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    private EditText user_field;
    private Button main_btn;
    private TextView result_inf;
    private TextView result_inf2;
    private TextView result_inf3;
    private TextView result_inf4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_field = findViewById(R.id.user_field);
        main_btn = findViewById(R.id.main_btn);
        result_inf = findViewById(R.id.result_inf);
        result_inf2 = findViewById(R.id.result_inf2);
        result_inf3 = findViewById(R.id.result_inf3);
        result_inf4 = findViewById(R.id.result_inf4);

        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_field.getText().toString().trim().equals(""))
                    //Указываем страничку на которой всплывет виджет. Указываем строку с  текстом которы всплывет. длительность. ну и метод сшов чтобы оно показалось
                    Toast.makeText(MainActivity.this, R.string.no_input_user, Toast.LENGTH_LONG).show();
                else {
                    String city= user_field.getText().toString();
                    String key = "e1ba3adb00e1f0839dcb27d0a914d996";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric&lang=ru";

                    new GetURLData().execute(url);
                }
            }
        });
    }

    private class GetURLData extends AsyncTask<String,String,String>{

protected  void onPreExecute(){

    super.onPreExecute();
    result_inf3.setText("Ожидайте...");

}
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                //создали объект на основе которого обращаемся к юрл
                URL url = new URL(strings[0]);
                //открываем соединение
                connection =(HttpURLConnection) url.openConnection();
                connection.connect();
// считываем поток
                InputStream stream = connection.getInputStream();
                //считываем в формате строки
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line ="";
                //считываем текст пока он есть
                while((line=reader.readLine()) !=null)
                    buffer.append(line).append("\n");

                return  buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null)
                    connection.disconnect();

                if(reader!=null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return  null;

        }
        @SuppressLint("SetTextI18n")
        @Override
        protected  void onPostExecute(String result){
    super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                result_inf.setText("Температура: " + jsonObject.getJSONObject("main").getDouble("temp"));
                result_inf2.setText("По ощущеничам: " + jsonObject.getJSONObject("main").getDouble("feels_like"));
                result_inf3.setText("Влажность : " + jsonObject.getJSONObject("main").getDouble("humidity"));
                result_inf4.setText("Давление : " + jsonObject.getJSONObject("main").getDouble("pressure"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}