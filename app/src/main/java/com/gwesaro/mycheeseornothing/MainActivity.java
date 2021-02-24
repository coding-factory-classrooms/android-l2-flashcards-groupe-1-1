package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        try {
            JsonHelper jHelper = new JsonHelper();
            String str = jHelper.loadJSONFromRes(getBaseContext());
            JSONObject obj = new JSONObject(str);
            FlashCard card2 = new FlashCard((JSONObject) obj.getJSONObject("questions").getJSONArray("easy").get(1));
            Log.i(TAG, card2.toString());

        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }


}