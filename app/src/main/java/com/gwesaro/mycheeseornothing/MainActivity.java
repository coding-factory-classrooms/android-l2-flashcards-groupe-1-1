package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;


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

            Questions quest = new Questions("easy", obj);

            Log.i(TAG, quest.toString());
            Log.i(TAG, quest.getCurrentQuestion().toString());
            quest.setUserResponse("eux");

            Log.i(TAG, quest.getFurtherQuestion().toString());

            quest.setUserResponse("eux");

            Log.i(TAG, quest.toString());
            Log.i(TAG, quest.getAnswerCount() + "/" + quest.getNbQuestions() + " -- " + quest.getSuccessPercent() + "%");
            Log.i(TAG, quest.getQuizSumary().toString());

            FlashCard card2 = new FlashCard(  quest.getCurrentQuestion());
            Log.i(TAG, card2.toString());
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }


}