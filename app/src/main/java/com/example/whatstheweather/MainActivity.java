package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText cityname;
    TextView resultTextView;
    static int r=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityname=findViewById(R.id.cityName);
        resultTextView=findViewById(R.id.result);
    }
    public void findWeather(View view){
        DownloadTask task=new DownloadTask();
        String result= null;
        try {
            result = task.execute("https://api.openweathermap.org/data/2.5/weather?q="+cityname.getText().toString()+"&appid=7cb14e137c9ac03345a21d7324138fa5").get();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result==null){
            Toast.makeText(MainActivity.this, "Could not find weather, TRY AGAIN!", Toast.LENGTH_LONG).show();

        }
        else {
            try {
                StringBuilder message = new StringBuilder();
                JSONObject jsonObject = new JSONObject(result);
                String weatherinfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatherinfo);
                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonpart = arr.getJSONObject(i);
                    String main;
                    String description;
                    main = jsonpart.getString("main");
                    description = jsonpart.getString("description");
                    message.append(main).append(": ").append(description).append("\r\n");
                }
                resultTextView.setText(message.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("WEBSITE CONTENT", result);
        }
    }

     public static class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            URL url;
            HttpURLConnection urlConnection;
            try {
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream inputStream=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(inputStream);
                int data = reader.read();
                while (data!=-1){
                    char current=(char)data;
                    result.append(current);
                    data=reader.read();
                }
                Log.i("string",result.toString());
                return result.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            r=1;
            Log.i("result fount", String.valueOf(r));
        }
    }

}