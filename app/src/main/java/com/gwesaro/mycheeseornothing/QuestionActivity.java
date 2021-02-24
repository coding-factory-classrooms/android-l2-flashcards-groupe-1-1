package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gwesaro.mycheeseornothing.Question.QuestionCollection;

public class QuestionActivity extends AppCompatActivity {

    private final String TAG = "QuestionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent srcIntent = getIntent();
        QuestionCollection questionCollection = srcIntent.getParcelableExtra("questionCollection");
        Log.i(TAG, "MODE : " + questionCollection.getMode());
        Log.i(TAG, "COUNT : " + questionCollection.getQuestionsCount());
    }


    private void updateInterface(){

    }

    private void navigateToStats() {
        //        Intent intent = new Intent(this, StatsActivity.class);
        //        intent.putExtra("percent" , this.quest.getSuccessPercent());
        //        intent.putExtra("rate" , this.quest.getQuizRate());
        //        intent.putExtra("mode" , this.quest.getMode());
        //        startActivity(intent);
    }
}