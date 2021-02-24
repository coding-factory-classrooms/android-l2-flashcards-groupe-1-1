package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

public class StatsActivity extends AppCompatActivity {

    private final String TAG = "StatsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Log.i(TAG, "create stats window");
        Intent intent = getIntent();
        Questions quest = intent.getParcelableExtra("questionsClass");

        Log.i(TAG, quest.toString());
        TextView modeTextView = findViewById(R.id.modeTextView);
        modeTextView.setText(quest.getMode());

        TextView rateTextView = findViewById(R.id.rateTextView);
        rateTextView.setText(quest.getQuizRate());

        TextView percentTextView = findViewById(R.id.percentTextView);
        percentTextView.setText("" + quest.getSuccessPercent() + " %");

        Log.i(TAG, "have update window textview");
    }

    private void primalTest(){
        try {
            JsonHelper jHelper = new JsonHelper();
            String str = jHelper.loadJSONFromRes(getBaseContext());
            JSONObject obj = new JSONObject(str);

            Questions quest = new Questions("easy", obj);

            quest.setUserResponse("eux");
            quest.getFurtherQuestion();
            quest.setUserResponse("eux");

            TextView modeTextView = findViewById(R.id.modeTextView);
            modeTextView.setText(quest.getMode());

            TextView rateTextView = findViewById(R.id.rateTextView);
            rateTextView.setText(quest.getQuizRate());

            TextView percentTextView = findViewById(R.id.percentTextView);
            percentTextView.setText("" + quest.getSuccessPercent());

        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }
}